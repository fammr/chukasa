package pro.hirooka.chukasa.chukasa_api.app.api.v1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pro.hirooka.chukasa.chukasa_api.app.api.v1.exception.ChukasaBadRequestException;
import pro.hirooka.chukasa.chukasa_api.app.api.v1.exception.ChukasaInternalServerErrorException;
import pro.hirooka.chukasa.chukasa_api.app.api.v1.helper.ChukasaUtility;
import pro.hirooka.chukasa.chukasa_api.domain.model.ChukasaResponse;
import pro.hirooka.chukasa.chukasa_api.domain.model.HlsPlaylist;
import pro.hirooka.chukasa.chukasa_common.domain.configuration.SystemConfiguration;
import pro.hirooka.chukasa.chukasa_common.domain.enums.FfmpegVcodecType;
import pro.hirooka.chukasa.chukasa_common.domain.service.ICommonUtilityService;
import pro.hirooka.chukasa.chukasa_common.domain.service.ISystemService;
import pro.hirooka.chukasa.chukasa_hls.domain.configuration.HlsConfiguration;
import pro.hirooka.chukasa.chukasa_hls.domain.model.ChukasaModel;
import pro.hirooka.chukasa.chukasa_hls.domain.model.ChukasaSettings;
import pro.hirooka.chukasa.chukasa_hls.domain.service.IChukasaModelManagementComponent;
import pro.hirooka.chukasa.chukasa_hls.domain.service.ICoordinatorService;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

import static pro.hirooka.chukasa.chukasa_common.domain.constants.ChukasaConstants.USER_AGENT;

@Slf4j
@RestController
@RequestMapping("api/v1/hls")
public class  HlsPlayerRestController {

    @Autowired
    SystemConfiguration systemConfiguration;
    @Autowired
    HlsConfiguration hlsConfiguration;
    @Autowired
    IChukasaModelManagementComponent chukasaModelManagementComponent;
    @Autowired
    HttpServletRequest httpServletRequest;
    @Autowired
    ISystemService systemService;
    @Autowired
    ICommonUtilityService commonUtilityService;

    @Autowired
    ICoordinatorService taskCoordinatorService;

    @RequestMapping(value = "/start", method = RequestMethod.POST)
    HlsPlaylist play(@RequestBody @Validated ChukasaSettings chukasaSettings) throws ChukasaBadRequestException, ChukasaInternalServerErrorException {

        String userAgent = httpServletRequest.getHeader("user-agent");
        log.info("user-agent: {}", userAgent);
        if(!userAgent.contains(USER_AGENT)){
            throw new ChukasaBadRequestException("User-Agent is invalid");
        }

        FfmpegVcodecType ffmpegVcodecType = systemService.getFfmpegVcodecType(userAgent);
        if(ffmpegVcodecType == FfmpegVcodecType.UNKNOWN){
            throw new ChukasaInternalServerErrorException("FFmpeg configuration is not suitable for this application.");
        }

        ChukasaUtility.initializeRunner(chukasaModelManagementComponent, systemConfiguration);
        if(chukasaModelManagementComponent.get().size() > 0){
            throw new ChukasaInternalServerErrorException("Cannot start streaming bacause previous one is not finished.");
        }

        log.info("ChukasaSettings -> {}", chukasaSettings.toString());

        ChukasaModel chukasaModel = new ChukasaModel();
        chukasaModel.setSystemConfiguration(systemConfiguration);
        chukasaModel.setHlsConfiguration(hlsConfiguration);
        chukasaModel.setChukasaSettings(chukasaSettings);

        chukasaModel.setUuid(UUID.randomUUID());
        chukasaModel.setAdaptiveBitrateStreaming(0);
        chukasaModel.setFfmpegVcodecType(ffmpegVcodecType);
        // TODO: -> system service
        if(ffmpegVcodecType == FfmpegVcodecType.HEVC_NVENC || ffmpegVcodecType == FfmpegVcodecType.HEVC_QSV){
            chukasaModel.setStreamFileExtension(".m4s");
        }

        chukasaModel = ChukasaUtility.operateEncodingSettings(chukasaModel);
        if(chukasaModel == null){
            throw new ChukasaBadRequestException("TranscodingSettings is invalid");
        }

        String servletRealPath = httpServletRequest.getSession().getServletContext().getRealPath("");
        String streamRootPath = commonUtilityService.getStreamRootPath(servletRealPath);
        chukasaModel.setStreamRootPath(streamRootPath);
        chukasaModel = ChukasaUtility.createChukasaDerectory(chukasaModel);
        chukasaModel = ChukasaUtility.calculateTimerTaskParameter(chukasaModel);

        String playlistURI = ChukasaUtility.buildM3u8URI(chukasaModel);
        if(playlistURI.equals("/")){
            throw new ChukasaInternalServerErrorException("Cannot create playlist.");
        }

        chukasaModelManagementComponent.create(0, chukasaModel);

        //chukasaTaskService.execute(0);
        taskCoordinatorService.execute();

        HlsPlaylist hlsPlaylist = new HlsPlaylist();
        hlsPlaylist.setUri(playlistURI);
        return hlsPlaylist;
    }

    @RequestMapping(value = "/stop", method = RequestMethod.GET)
    ChukasaResponse stop() throws ChukasaInternalServerErrorException {
        taskCoordinatorService.cancel();
        taskCoordinatorService.stop();
        //chukasaStopper.stop();
        return remove();
    }

    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    ChukasaResponse remove() throws ChukasaInternalServerErrorException {
        removeStreamingFiles();
        ChukasaResponse chukasaResponseModel = new ChukasaResponse();
        chukasaResponseModel.setMessage("Streaming stopped successfully.");
        return chukasaResponseModel;
    }

    private void removeStreamingFiles(){
        taskCoordinatorService.remove();
        //taskCoordinatorService.cancel();
    }

//    private void removeStreamingFiles() throws ChukasaInternalServerErrorException {
//        String streamRootPath = commonUtilityService.getStreamRootPath(httpServletRequest.getSession().getServletContext().getRealPath(""));
//        if(Files.exists(new File(streamRootPath).toPath())) {
//            chukasaRemover.setStreamRootPath(streamRootPath);
//            chukasaRemover.remove();
//        }else {
//            log.warn("cannot remove files bacause streamRootPath: {} does not exist.", streamRootPath);
//            throw new ChukasaInternalServerErrorException("Cannot remove files bacause streamRootPath does not exist.");
//        }
//    }
}

