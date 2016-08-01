package zhiyuan3g.com.mymusic.entity;

/**
 * Created by xuGuang on 2016/3/15.
 */
public class ArtistInfo {

    private int singer_id;
    private String singerName;
    private int singerCount;
    private String artist_sortLetters;

    public ArtistInfo(int singer_id, String singerName, int singerCount, String artist_sortLetters) {
        this.singer_id = singer_id;
        this.singerName = singerName;
        this.singerCount = singerCount;
        this.artist_sortLetters = artist_sortLetters;
    }

    public int getSinger_id() {
        return singer_id;
    }

    public void setSinger_id(int singer_id) {
        this.singer_id = singer_id;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public int getSingerCount() {
        return singerCount;
    }

    public void setSingerCount(int singerCount) {
        this.singerCount = singerCount;
    }

    public String getArtist_sortLetters() {
        return artist_sortLetters;
    }

    public void setArtist_sortLetters(String artist_sortLetters) {
        this.artist_sortLetters = artist_sortLetters;
    }
}
