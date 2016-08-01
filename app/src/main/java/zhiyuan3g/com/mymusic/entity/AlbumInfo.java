package zhiyuan3g.com.mymusic.entity;

/**
 * Created by xuGuang on 2016/3/15.
 */
public class AlbumInfo {
    private int album_id;
    private String album_name;
    private int number_of_songs;
    private String album_art;

    private String album_sortLetters;

    public AlbumInfo(int album_id, String album_name, int number_of_songs, String album_art, String album_sortLetters) {
        this.album_id = album_id;
        this.album_name = album_name;
        this.number_of_songs = number_of_songs;
        this.album_art = album_art;
        this.album_sortLetters = album_sortLetters;
    }

    public int getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(int album_id) {
        this.album_id = album_id;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }

    public int getNumber_of_songs() {
        return number_of_songs;
    }

    public void setNumber_of_songs(int number_of_songs) {
        this.number_of_songs = number_of_songs;
    }

    public String getAlbum_art() {
        return album_art;
    }

    public void setAlbum_art(String album_art) {
        this.album_art = album_art;
    }

    public String getAlbum_sortLetters() {
        return album_sortLetters;
    }

    public void setAlbum_sortLetters(String album_sortLetters) {
        this.album_sortLetters = album_sortLetters;
    }
}
