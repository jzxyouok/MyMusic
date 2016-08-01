package zhiyuan3g.com.mymusic.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import zhiyuan3g.com.mymusic.db.DatabaseHelper;
import zhiyuan3g.com.mymusic.entity.ArtistInfo;
import zhiyuan3g.com.mymusic.utils.Constant;

/**
 * Created by xuGuang on 2016/3/15.
 */
public class ArtistInfoDao {

    private Context context;
    DatabaseHelper helper;
    SQLiteDatabase db;

    //构造方法  对属性赋初值
    public ArtistInfoDao(Context context){
        this.context = context;
        helper = DatabaseHelper.getHelper(context);
        db = helper.getReadableDatabase();
    }

    //对ArtistInfo进行增加操作，通过你的指定的对象
    public boolean addArtistInfo(ArtistInfo artistInfo){
        boolean result = false;
        ContentValues values = new ContentValues();
        values.put("singerCount",artistInfo.getSingerCount());
        values.put("singerName",artistInfo.getSingerName());
        values.put("artist_sortLetters",artistInfo.getArtist_sortLetters());
        long rows = db.insert("artist_info",null,values);
        if (rows>0){
            result = true;
        }
        return result;
    }

    //对MusicInfo进行查找全部操作
    public List<ArtistInfo> getAllArtistInfo(){
        List<ArtistInfo> artistInfoList = new ArrayList<>();
        String[] columns = {"singer_id","singerCount","singerName","artist_sortLetters"};
        Cursor cursor = db.query("artist_info",columns,null,null,null,null,"singer_id");
        while (cursor.moveToNext()){
            int singer_id = cursor.getInt(cursor.getColumnIndex("singer_id"));
            String singerName = cursor.getString(cursor.getColumnIndex("singerName"));
            int singerCount = cursor.getInt(cursor.getColumnIndex("singerCount"));
            String artist_sortLetters = cursor.getString(cursor.getColumnIndex("artist_sortLetters"));
            ArtistInfo artistInfo = new ArtistInfo(singer_id,singerName,singerCount,artist_sortLetters);
            artistInfoList.add(artistInfo);
        }
        return artistInfoList;
    }
}
