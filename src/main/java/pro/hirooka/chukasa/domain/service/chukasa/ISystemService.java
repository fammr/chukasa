package pro.hirooka.chukasa.domain.service.chukasa;

import pro.hirooka.chukasa.domain.model.chukasa.enums.HardwareAccelerationType;

public interface ISystemService {
    boolean isFFmpeg();
    boolean isWebCamera();
    String getWebCameraDeviceName();
    boolean isTuner();
    boolean isRecxxx();
    boolean isEpgdump();
    boolean isMongoDB();
    boolean canWebCameraStreaming();
    boolean canFileStreaming();
    boolean canPTxStreaming();
    boolean canRecording();
    HardwareAccelerationType getHardwareAccelerationType();
}
