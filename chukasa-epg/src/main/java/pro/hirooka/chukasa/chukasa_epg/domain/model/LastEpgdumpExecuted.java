package pro.hirooka.chukasa.chukasa_epg.domain.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class LastEpgdumpExecuted {
    @Id
    private int unique;
    private long date;
}
