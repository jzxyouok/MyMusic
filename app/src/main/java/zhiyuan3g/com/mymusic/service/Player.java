package zhiyuan3g.com.mymusic.service;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.Toast;

import java.util.List;

import zhiyuan3g.com.mymusic.dao.MusicInfoDao;
import zhiyuan3g.com.mymusic.entity.MusicInfo;

/**
 * 播放器类  单例模式：封装了播放器的相关的操作
 */
public class Player {
    private MediaPlayer mediaPlayer;

    private int mode;     //播放模式
    private int playing = Config.PLAYING_STOP;

    private List<MusicInfo> list;    //歌曲列表
    private int position = 0;
    private Context context;
    private MusicInfoDao musicInfoDao;


    private Player(Context context) {
        this.context = context;
        musicInfoDao = new MusicInfoDao(context);
        if (list == null || Config.changeMusicInfo) {
            list = musicInfoDao.getAllMusicInfo();
        }
    }


    // TODO: 2016/3/16
    private static Player instance;

    private Player() {

    }

    public static synchronized Player getInstance() {
        if (instance == null) {
            instance = new Player();
        }
        return instance;
    }


    public List<MusicInfo> getList() {
        return this.list;
    }

    public int getListPosition() {
        return this.position;
    }

    public MusicInfo getMusic() {
        MusicInfo musicInfo = null;
        if (position >= list.size()) {
            Toast.makeText(context, "索引大于你的歌曲数", Toast.LENGTH_SHORT).show();
        } else {
            musicInfo = list.get(position);
        }
        return musicInfo;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return this.mode;
    }

    public int getPlaying() {
        return playing;
    }

    // 获取播放的音乐文件总时间长度
    public int getDuration() {
        int duration = 0;
        if (mediaPlayer != null) {
            duration = mediaPlayer.getDuration();
        }
        return duration;
    }

    // 获取当前播放音乐时间点
    public int getCurrentPosition() {
        int currentPosition = 0;
        if (mediaPlayer != null) {
            currentPosition = mediaPlayer.getCurrentPosition();
        }
        return currentPosition;
    }

    // 将音乐播放跳转到某一时间点,以毫秒为单位
    public void seekTo(int msec) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(msec);
        }
    }

    //销毁播放
    public void destroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            playing = Config.PLAYING_STOP;
        }
    }

    //停止播放
    public void stop() {
        if (playing != Config.PLAYING_STOP) {
            mediaPlayer.reset();
            playing = Config.PLAYING_STOP;
            Intent intent = new Intent(Config.RECEIVER_MUSIC_CHANGE);
            intent.putExtra("msgContent", "");
            context.sendBroadcast(intent);
        }
    }

    //暂停播放
    public void pause() {
        if (playing != Config.PLAYING_PAUSE) {
            mediaPlayer.pause();
            playing = Config.PLAYING_PAUSE;
            Intent intent = new Intent(Config.RECEIVER_MUSIC_CHANGE);
            intent.putExtra("msgContent", "pause");
            context.sendBroadcast(intent);
        }
    }

    // 正在暂停，即将开始继续播放
    public MusicInfo replay() {
        if (playing != Config.PLAYING_PLAY) {
            mediaPlayer.start();
            playing = Config.PLAYING_PLAY;
            Intent intent = new Intent(Config.RECEIVER_MUSIC_CHANGE);
            intent.putExtra("msgContent", "rePlay");
            context.sendBroadcast(intent);
        }
        return list.get(position);
    }

    //播放
    public MusicInfo play(Context context, List<MusicInfo> list, int position) {
        // 如果有正在播放的歌曲，将它停止
        if (playing == Config.PLAYING_PLAY) {
            mediaPlayer.reset();
        }
        mediaPlayer = MediaPlayer.create(context, Uri.parse(list.get(position).getMusic_fileUrl()));
        try {
            mediaPlayer.start();
            this.list = list;
            this.position = position;
            this.context = context;
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Intent intent = new Intent(Config.RECEIVER_MUSIC_CHANGE);
                    intent.putExtra("msgContent", "");
                    Player.this.context.sendBroadcast(intent);       //音乐播放完毕的回调  发送广播
                    completion(Player.this.context, Player.this.list,
                            Player.this.position);
                }
            });
            playing = Config.PLAYING_PLAY;
//            Intent intent = new Intent(Config.RECEIVER_MUSIC_CHANGE);
//            intent.putExtra("msgContent","");
//            context.sendBroadcast(intent);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return list.get(position);
    }

    //下一首
    public MusicInfo next(Context context) {
        MusicInfo musicInfo = null;
        if (list.size() < 1) {
            this.destroy();
        } else {
            mediaPlayer.reset(); // 停止上一首
            position = (position + 1) % list.size();
            play(context, list, position);
            musicInfo = list.get(position);
        }
        return musicInfo;
    }

    //上一首
    public MusicInfo previous(Context context) {
        MusicInfo musicInfo = null;
        if (list.size() < 1) {
            this.destroy();
            musicInfo = null;
        } else {
            mediaPlayer.reset(); // 停止上一首
            position = (position + list.size() - 1) % list.size();
            play(context, list, position);
            musicInfo = list.get(position);
        }
        return musicInfo;
    }

    //播放完成
    public MusicInfo completion(Context context, List<MusicInfo> list, int position) {
        MusicInfo musicInfo = null;
        switch (mode) {
            case Config.MODE_REPEAT_SINGLE:
                // 单曲播放
                stop();
                break;
            case Config.MODE_REPEAT_ALL:
                // 单曲循环
                musicInfo = play(context, list, position);
                break;
            case Config.MODE_SEQUENCE:
                // 列表循环
                musicInfo = play(context, list, (position + 1) % list.size());
                break;
            case Config.MODE_RANDOM:
                // 随机循环
                int randomNum = (int) (Math.random() * list.size());  //随机产生的随机数
                musicInfo = play(context, list, randomNum);
                Intent intent = new Intent(Config.RECEIVER_MUSIC_CHANGE);  //再把这个随机数发送出去 更新播放栏
                intent.putExtra("msgContent", "");
                intent.putExtra("number", randomNum);
                Player.this.context.sendBroadcast(intent);
                break;
            default:
                break;
        }
        return musicInfo;
    }
}
