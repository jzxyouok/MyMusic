package zhiyuan3g.com.mymusic.service;

/**
 * 封装了一些常亮  表示播放状态
 */
public class Config {

    //当前版本号
    private static final double VERSION = 1.0;

    //应用是否是第一次安装
    public static final String FIRST_USER_FILE = "first";
    public static final String FIRST_USER_KEY = "firstinstall";

    //数据库名字
    public static final String DB_NAME = "my_music_db";

    //播放列表循环模式
    public static final int MODE_REPEAT_SINGLE = 0;    // 单曲播放
    public static final int MODE_REPEAT_ALL = 1;       // 单曲循环
    public static final int MODE_SEQUENCE = 2;         // 列表循环
    public static final int MODE_RANDOM = 3;           // 随机循环

    //播放列表循环模式本地存储
    public static final String LOOP_MODE_FILE = "loop_mode_file";
    public static final String LOOP_MODE_KEY = "loop_mode_key";

    //播放器状态
    public static final int PLAYING_STOP = 0;
    public static final int PLAYING_PAUSE = 1;
    public static final int PLAYING_PLAY = 2;

    //音乐改变的广播     就是当前的音乐播放完毕的广播
    public static final String RECEIVER_MUSIC_CHANGE = "SEARCH_NEW_BROAD";

    //音乐列表信息被改变
    public static boolean changeMusicInfo = false;
    public static boolean changeCollectInfo = false;

    //扫描到的歌曲的数量
    public static final String SCAN_MUSIC_COUNT = "SCAN_MUSIC_COUNT";

}
