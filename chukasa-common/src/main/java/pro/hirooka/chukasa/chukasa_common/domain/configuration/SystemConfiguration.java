package pro.hirooka.chukasa.chukasa_common.domain.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "system")
public class SystemConfiguration {
    String haryukaPath;
    String ffmpegPath;
    String recxxxPath;
    String webcamDeviceName;
    int webcamAudioChannel;
    String temporaryPath;
    String filePath;
    int ffmpegThreads;
}
