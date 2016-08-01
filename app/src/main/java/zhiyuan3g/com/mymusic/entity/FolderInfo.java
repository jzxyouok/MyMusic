package zhiyuan3g.com.mymusic.entity;

/**
 * Created by xuGuang on 2016/3/15.
 */
public class FolderInfo {
    private int folder_id;
    private int number_of_songs;
    private String folder_name;
    private String folder_path;

    public FolderInfo(int folder_id, int number_of_songs, String folder_name, String folder_path) {
        this.folder_id = folder_id;
        this.number_of_songs = number_of_songs;
        this.folder_name = folder_name;
        this.folder_path = folder_path;
    }

    public String getFolder_path() {
        return folder_path;
    }

    public void setFolder_path(String folder_path) {
        this.folder_path = folder_path;
    }

    public int getFolder_id() {
        return folder_id;
    }

    public void setFolder_id(int folder_id) {
        this.folder_id = folder_id;
    }

    public int getNumber_of_songs() {
        return number_of_songs;
    }

    public void setNumber_of_songs(int number_of_songs) {
        this.number_of_songs = number_of_songs;
    }

    public String getFolder_name() {
        return folder_name;
    }

    public void setFolder_name(String folder_name) {
        this.folder_name = folder_name;
    }
}
