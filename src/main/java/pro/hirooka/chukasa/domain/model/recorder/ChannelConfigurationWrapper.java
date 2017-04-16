package pro.hirooka.chukasa.domain.model.recorder;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ChannelConfigurationWrapper { // TODO: -> common
    @JsonProperty("channelConfiguration")
    private List<ChannelConfiguration> channelConfigurationList;
}
