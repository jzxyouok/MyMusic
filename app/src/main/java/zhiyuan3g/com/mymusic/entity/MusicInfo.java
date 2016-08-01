package zhiyuan3g.com.mymusic.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xuGuang on 2016/3/14.
 */
public class MusicInfo {

    private int music_id;
    private String music_fileName;        //文件名
    private String music_title;           //音乐名字
    private int music_duration;           //音乐时长
    private String music_singer;          //歌手名字
    private String music_album;           //专辑名字
    private String music_year;            //年份
    private String music_type;            //类型
    private String music_size;            //文件大小
    private String music_fileUrl;         //地址  播放

    private String music_sortLetters;  //显示数据拼音的首字母


    public MusicInfo(){

    }

    public MusicInfo(int music_id, String music_fileName, String music_title, int music_duration, String music_singer, String music_album, String music_year, String music_type, String music_size, String music_fileUrl, String music_sortLetters) {
        this.music_id = music_id;
        this.music_fileName = music_fileName;
        this.music_title = music_title;
        this.music_duration = music_duration;
        this.music_singer = music_singer;
        this.music_album = music_album;
        this.music_year = music_year;
        this.music_type = music_type;
        this.music_size = music_size;
        this.music_fileUrl = music_fileUrl;
        this.music_sortLetters = music_sortLetters;
    }

    public int getMusic_id() {
        return music_id;
    }

    public void setMusic_id(int music_id) {
        this.music_id = music_id;
    }

    public String getMusic_fileName() {
        return music_fileName;
    }

    public void setMusic_fileName(String music_fileName) {
        this.music_fileName = music_fileName;
    }

    public String getMusic_title() {
        return music_title;
    }

    public void setMusic_title(String music_title) {
        this.music_title = music_title;
    }

    public int getMusic_duration() {
        return music_duration;
    }

    public void setMusic_duration(int music_duration) {
        this.music_duration = music_duration;
    }

    public String getMusic_album() {
        return music_album;
    }

    public void setMusic_album(String music_album) {
        this.music_album = music_album;
    }

    public String getMusic_singer() {
        return music_singer;
    }

    public void setMusic_singer(String music_singer) {
        this.music_singer = music_singer;
    }

    public String getMusic_year() {
        return music_year;
    }

    public void setMusic_year(String music_year) {
        this.music_year = music_year;
    }

    public String getMusic_type() {
        return music_type;
    }

    public void setMusic_type(String music_type) {
        this.music_type = music_type;
    }

    public String getMusic_size() {
        return music_size;
    }

    public void setMusic_size(String music_size) {
        this.music_size = music_size;
    }

    public String getMusic_fileUrl() {
        return music_fileUrl;
    }

    public void setMusic_fileUrl(String music_fileUrl) {
        this.music_fileUrl = music_fileUrl;
    }

    public String getMusic_sortLetters() {
        return music_sortLetters;
    }

    public void setMusic_sortLetters(String music_sortLetters) {
        this.music_sortLetters = music_sortLetters;
    }
}
