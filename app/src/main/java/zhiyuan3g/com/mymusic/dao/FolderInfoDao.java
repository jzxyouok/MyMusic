package zhiyuan3g.com.mymusic.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import zhiyuan3g.com.mymusic.db.DatabaseHelper;
import zhiyuan3g.com.mymusic.entity.ArtistInfo;
import zhiyuan3g.com.mymusic.entity.FolderInfo;
import zhiyuan3g.com.mymusic.utils.Constant;

/**
 * Created by xuGuang on 2016/3/15.
 */
public class FolderInfoDao {

    private Context context;
    DatabaseHelper helper;
    SQLiteDatabase db;

    //构造方法  对属性赋初值
    public FolderInfoDao(Context context){
        this.context = context;
        helper = DatabaseHelper.getHelper(context);
        db = helper.getReadableDatabase();
    }

    //对ArtistInfo进行增加操作，通过你的指定的对象
    public boolean addFolderInfo(FolderInfo folderInfo){
        boolean result = false;
        ContentValues values = new ContentValues();
        values.put("number_of_songs",folderInfo.getNumber_of_songs());
        values.put("folder_name",folderInfo.getFolder_name());
        values.put("folder_path",folderInfo.getFolder_path());
        long rows = db.insert("folder_info",null,values);
        if (rows>0){
            result = true;
        }
        return result;
    }

    //对MusicInfo进行查找全部操作
    public List<FolderInfo> getAllFolderInfo(){
        List<FolderInfo> folderInfoList = new ArrayList<>();
        String[] columns = {"folder_id","number_of_songs","folder_name","folder_path"};
        Cursor cursor = db.query("folder_info",columns,null,null,null,null,"folder_id");
        while (cursor.moveToNext()){
            int folder_id = cursor.getInt(cursor.getColumnIndex("folder_id"));
            int number_of_songs = cursor.getInt(cursor.getColumnIndex("number_of_songs"));;
            String folder_name = cursor.getString(cursor.getColumnIndex("folder_name"));
            String folder_path = cursor.getString(cursor.getColumnIndex("folder_path"));
            FolderInfo folderInfo = new FolderInfo(folder_id,number_of_songs,folder_name,folder_path);
            folderInfoList.add(folderInfo);
        }
        return folderInfoList;
    }
}
