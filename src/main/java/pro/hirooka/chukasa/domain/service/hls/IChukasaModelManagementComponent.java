package pro.hirooka.chukasa.domain.service.hls;

import pro.hirooka.chukasa.domain.model.hls.ChukasaModel;

import java.util.List;

public interface IChukasaModelManagementComponent {
    ChukasaModel create(int adaptiveBitrateStreaming, ChukasaModel chukasaModel);
    List<ChukasaModel> get();
    ChukasaModel get(int adaptiveBitrateStreaming);
    ChukasaModel update(int adaptiveBitrateStreaming, ChukasaModel chukasaModel);
    void delete(int adaptiveBitrateStreaming);
    void deleteAll();
}
