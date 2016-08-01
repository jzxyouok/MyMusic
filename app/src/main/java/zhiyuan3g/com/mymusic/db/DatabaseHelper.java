package zhiyuan3g.com.mymusic.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import zhiyuan3g.com.mymusic.utils.Constant;

/**
 * Created by xuGuang on 2016/3/14.
 * 数据库帮助类             单例模式
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static SQLiteDatabase mDb;
    private static DatabaseHelper mHelper;
    private static final String TABLE_ALBUM = "album_info";
    private static final String TABLE_ARTIST = "artist_info";
    private static final String TABLE_MUSIC = "music_info";
    private static final String TABLE_FOLDER = "folder_info";
    private static final String TABLE_FAVORITE = "favorite_info";

    //构造方法依次是  上下文 库名 游标 版本号
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    public static SQLiteDatabase getInstance(Context context) {
        if (mDb == null) {
            mDb = getHelper(context).getWritableDatabase();
        }
        return mDb;
    }

    public static DatabaseHelper getHelper(Context context) {
        if (mHelper == null) {
            mHelper = new DatabaseHelper(context);
        }
        return mHelper;
    }

    public DatabaseHelper(Context context) {
        super(context, Constant.DATABASE_NAME, null, Constant.CURRENT_VERSION);
    }


    //重写 onCreate方法
    @Override
    public void onCreate(SQLiteDatabase db) {

        String sp_MusicInfo = "create table "
                + TABLE_MUSIC
                + "("
                + "music_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "music_fileName VARCHAR(50),"
                + "music_title VARCHAR(30),"
                + "music_duration INTEGER,"
                + "music_singer VARCHAR(30),"
                + "music_album VARCHAR(50),"
                + "music_year VARCHAR(50),"
                + "music_type VARCHAR(50),"
                + "music_size VARCHAR(50),"
                + "music_fileUrl VARCHAR(60),"
                + "music_sortLetters VARCHAR(10)"
                + ")";

        String sp_ArtistInfo = "create table "
                + TABLE_ARTIST
                + "("
                + "singer_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "singerCount INTEGER,"
                + "artist_sortLetters VARCHAR(20),"
                + "singerName VARCHAR(50)"
                + ")";

        String sp_AlbumInfo = "create table "
                + TABLE_ALBUM
                + "("
                + "album_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "number_of_songs INTEGER,"
                + "album_name VARCHAR(50),"
                + "album_sortLetters VARCHAR(20),"
                + "album_art VARCHAR(50)"
                + ")";

        String sp_FolderInfo = "create table "
                + TABLE_FOLDER
                + "("
                + "folder_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "number_of_songs INTEGER,"
                + "folder_name VARCHAR(50),"
                + "folder_path VARCHAR(50)"
                + ")";

        db.execSQL(sp_MusicInfo);
        db.execSQL(sp_ArtistInfo);
        db.execSQL(sp_AlbumInfo);
        db.execSQL(sp_FolderInfo);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //如果点击扫描歌曲的话，就要先删除以前的旧表
    public void deleteTables(Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ALBUM, null, null);
        db.delete(TABLE_ARTIST, null, null);
//        db.delete(TABLE_FAVORITE, null, null);
        db.delete(TABLE_FOLDER, null, null);
        db.delete(TABLE_MUSIC, null, null);
    }
}
