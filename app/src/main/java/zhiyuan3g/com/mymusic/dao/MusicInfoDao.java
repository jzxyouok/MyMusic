package zhiyuan3g.com.mymusic.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import zhiyuan3g.com.mymusic.db.DatabaseHelper;
import zhiyuan3g.com.mymusic.entity.MusicInfo;
import zhiyuan3g.com.mymusic.utils.Constant;

/**
 * Created by xuGuang on 2016/3/15.
 */
public class MusicInfoDao {

    private Context context;
    DatabaseHelper helper;
    SQLiteDatabase db;

    //构造方法  对属性赋初值
    public MusicInfoDao(Context context){
        this.context = context;
        helper = DatabaseHelper.getHelper(context);
        db = helper.getReadableDatabase();
    }

    //对MusicInfo进行增加操作，通过你的指定的对象
    public boolean addMusicInfo(MusicInfo musicInfo){
        boolean result = false;
        ContentValues values = new ContentValues();
        values.put("music_fileName",musicInfo.getMusic_fileName());
        values.put("music_title",musicInfo.getMusic_title());
        values.put("music_duration",musicInfo.getMusic_duration());
        values.put("music_singer",musicInfo.getMusic_singer());
        values.put("music_album",musicInfo.getMusic_album());
        values.put("music_year",musicInfo.getMusic_year());
        values.put("music_type",musicInfo.getMusic_type());
        values.put("music_size",musicInfo.getMusic_size());
        values.put("music_fileUrl",musicInfo.getMusic_fileUrl());
        values.put("music_sortLetters",musicInfo.getMusic_sortLetters());
        long rows = db.insert("music_info",null,values);
        if (rows>0){
            result = true;
        }
        return result;
    }

    //对MusicInfo进行查找全部操作
    public List<MusicInfo> getAllMusicInfo(){
        List<MusicInfo> musicInfos = new ArrayList<>();
        String[] columns = {"music_id","music_fileName","music_title","music_duration","music_singer","music_album","music_year","music_type","music_size","music_fileUrl","music_sortLetters"};
        Cursor cursor = db.query("music_info",columns,null,null,null,null,"music_id");
        while (cursor.moveToNext()){
            int music_id = cursor.getInt(cursor.getColumnIndex("music_id"));
            String music_fileName = cursor.getString(cursor.getColumnIndex("music_fileName"));
            String music_title = cursor.getString(cursor.getColumnIndex("music_title"));
            int music_duration = cursor.getInt(cursor.getColumnIndex("music_duration"));
            String music_singer = cursor.getString(cursor.getColumnIndex("music_singer"));
            String music_album = cursor.getString(cursor.getColumnIndex("music_album"));
            String music_year = cursor.getString(cursor.getColumnIndex("music_year"));
            String music_type = cursor.getString(cursor.getColumnIndex("music_type"));
            String music_size = cursor.getString(cursor.getColumnIndex("music_size"));
            String music_fileUrl = cursor.getString(cursor.getColumnIndex("music_fileUrl"));
            String music_sortLetters = cursor.getString(cursor.getColumnIndex("music_sortLetters"));
            MusicInfo musicInfo = new MusicInfo(music_id,music_fileName,music_title,music_duration,music_singer,music_album,music_year,music_type,music_size,music_fileUrl,music_sortLetters);
            musicInfos.add(musicInfo);
        }
        return musicInfos;
    }
}
