package pro.hirooka.chukasa.chukasa_common.domain.constants;

import java.io.File;

public class ChukasaConstants {
    public static final String FILE_SEPARATOR = File.separator;
    public static final String INITIAL_STREAM_PATH = "/istream";
    public static final String STREAM_ROOT_PATH_NAME = "stream";
    public static final String LIVE_PATH_NAME = "live";
    public static final String STREAM_FILE_NAME_PREFIX = "chukasa";
    public static final String M3U8_FILE_NAME = "chukasa";
    public static final String FMP4_INIT_FILE_NAME = "chukasa";
    public static final String FMP4_INIT_FILE_EXTENSION = ".mp4";
    public static final String FFMPEG_HLS_M3U8_FILE_NAME = "ffmpeg";
    public static final String M3U8_FILE_EXTENSION = ".m3u8";
    public static final String HLS_KEY_FILE_EXTENSION = ".key";
    public static final String HLS_IV_FILE_EXTENSION = ".iv";
    public static final int HLS_KEY_LENGTH = 128;
    public static final int MPEG2_TS_PACKET_LENGTH = 188;
    public static final String ALTERNATIVE_HLS_PLAYER = "hlsjs";
    public static final String USER_AGENT = "chukasa-ios";
    public static final String DVB_DEVICE = "/dev/dvb/adapter";
    public static final String CHARACTER_DEVICE = "/dev/pt3video";
}
