package pro.hirooka.chukasa.chukasa_common.domain.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "epgdump")
public class EpgdumpConfiguration {
    String path;
    String temporaryPath;
    int recordingDuration;
}
