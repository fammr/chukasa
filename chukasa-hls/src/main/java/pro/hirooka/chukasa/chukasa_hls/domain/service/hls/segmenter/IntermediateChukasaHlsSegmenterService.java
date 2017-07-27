package pro.hirooka.chukasa.chukasa_hls.domain.service.hls.segmenter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import pro.hirooka.chukasa.chukasa_hls.domain.service.hls.playlist.IPlaylistCreator;

import java.util.Date;

@Slf4j
@Service
public class IntermediateChukasaHlsSegmenterService implements IIntermediateChukasaHlsSegmenterService {

    @Autowired
    private ChukasaHlsSegmenter chukasaHLSSegmenter;
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Autowired
    IPlaylistCreator playlistCreator;

    @Async
    @Override
    public void schedule(int adaptiveBitrateStreaming, Date startTime, long period) {
        playlistCreator.create();
        chukasaHLSSegmenter.setAdaptiveBitrateStreaming(adaptiveBitrateStreaming);
        threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setThreadNamePrefix(Integer.toString(adaptiveBitrateStreaming));
        threadPoolTaskScheduler.setPoolSize(3);
        threadPoolTaskScheduler.initialize();
        threadPoolTaskScheduler.scheduleAtFixedRate(chukasaHLSSegmenter, startTime, period);
    }

    @Override
    public void cancel(int adaptiveBitrateStreaming) {
        if(threadPoolTaskScheduler != null){
            if(threadPoolTaskScheduler.getThreadNamePrefix().equals(Integer.toString(adaptiveBitrateStreaming))){
                log.info("shutdown - {}", adaptiveBitrateStreaming);
                threadPoolTaskScheduler.shutdown();
            }
        }
    }
}