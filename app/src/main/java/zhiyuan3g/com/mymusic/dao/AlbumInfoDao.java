package zhiyuan3g.com.mymusic.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import zhiyuan3g.com.mymusic.db.DatabaseHelper;
import zhiyuan3g.com.mymusic.entity.AlbumInfo;
import zhiyuan3g.com.mymusic.entity.ArtistInfo;
import zhiyuan3g.com.mymusic.utils.Constant;

/**
 * Created by xuGuang on 2016/3/15.
 */
public class AlbumInfoDao {

    private Context context;
    DatabaseHelper helper;
    SQLiteDatabase db;

    //构造方法  对属性赋初值
    public AlbumInfoDao(Context context){
        this.context = context;
        helper = DatabaseHelper.getHelper(context);
        db = helper.getReadableDatabase();
    }

    //对ArtistInfo进行增加操作，通过你的指定的对象
    public boolean addAlbumInfo(AlbumInfo albumInfo){
        boolean result = false;
        ContentValues values = new ContentValues();
        values.put("number_of_songs", albumInfo.getNumber_of_songs());
        values.put("album_name",albumInfo.getAlbum_name());
        values.put("album_sortLetters",albumInfo.getAlbum_sortLetters());
        values.put("album_art",albumInfo.getAlbum_art());
        long rows = db.insert("album_info",null,values);
        if (rows>0){
            result = true;
        }
        return result;
    }

    //对MusicInfo进行查找全部操作
    public List<AlbumInfo> getAllAlbumInfo(){
        List<AlbumInfo> albumInfoList = new ArrayList<>();
        String[] columns = {"album_id","number_of_songs","album_name","album_sortLetters","album_art"};
        Cursor cursor = db.query("album_info", columns, null, null, null, null, "album_id");
        while (cursor.moveToNext()){
            int album_id = cursor.getInt(cursor.getColumnIndex("album_id"));
            String album_name = cursor.getString(cursor.getColumnIndex("album_name"));
            int number_of_songs = cursor.getInt(cursor.getColumnIndex("number_of_songs"));
            String album_art = cursor.getString(cursor.getColumnIndex("album_art"));
            String album_sortLetters = cursor.getString(cursor.getColumnIndex("album_sortLetters"));
            AlbumInfo albumInfo = new AlbumInfo(album_id,album_name,number_of_songs,album_art,album_sortLetters);
            albumInfoList.add(albumInfo);
        }
//        cursor.close();
        return albumInfoList;
    }
}
