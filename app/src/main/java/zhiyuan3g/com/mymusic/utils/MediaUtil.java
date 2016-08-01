package zhiyuan3g.com.mymusic.utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Audio.Media;
import android.widget.SectionIndexer;

import zhiyuan3g.com.mymusic.entity.MusicInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2016/3/2.
 */
public class MediaUtil {

    private static PinyinComparator pinyinComparator;
    private static CharacterParser characterParser;
    private static List<MusicInfo> musicInfoList;

    /**
     * 获取SD卡上的所有的音乐文件
     */
    public static List<MusicInfo> getAllMusicInfo(Context context){
        List<MusicInfo> musicInfoList = null;
        Cursor cursor = context.getContentResolver().query(
                Media.EXTERNAL_CONTENT_URI,
                new String[] { Media._ID,
                        Media.DISPLAY_NAME,
                        Media.TITLE,
                        Media.DURATION,
                        Media.ARTIST,
                        Media.ALBUM,
                        Media.YEAR,
                        Media.MIME_TYPE,
                        Media.SIZE,
                        Media.DATA },
                Media.MIME_TYPE + "=? or "
                        + Media.MIME_TYPE + "=?",
                new String[] { "audio/mpeg", "audio/x-ms-wma" }, null);

        musicInfoList = new ArrayList<>();
        if (cursor.moveToFirst()){
            MusicInfo musicInfo=null;
            do {
//                if (cursor.getInt(3)>120000){       //判断一首歌的时长大于两分钟
                    musicInfo = new MusicInfo();
                    // 文件名
                    musicInfo.setMusic_fileName(cursor.getString(1));
                    // 歌曲名
                    musicInfo.setMusic_title(cursor.getString(2));
                    // 时长
                    musicInfo.setMusic_duration(cursor.getInt(3));
                    // 歌手名
                    musicInfo.setMusic_singer(cursor.getString(4));
                    // 专辑名
                    musicInfo.setMusic_album(cursor.getString(5));
                    // 年代
                    if (cursor.getString(6) != null) {
                        musicInfo.setMusic_year(cursor.getString(6));
                    } else {
                        musicInfo.setMusic_year("未知");
                    }
                    // 歌曲格式
                    if ("audio/mpeg".equals(cursor.getString(7).trim())) {
                        musicInfo.setMusic_type("mp3");
                    } else if ("audio/x-ms-wma".equals(cursor.getString(7).trim())) {
                        musicInfo.setMusic_type("wma");
                    }
                    // 文件大小
                    if (cursor.getString(8) != null) {
                        float size = cursor.getInt(8) / 1024f / 1024f;
                        musicInfo.setMusic_size((size + "").substring(0, 4) + "M");
                    } else {
                        musicInfo.setMusic_size("未知");
                    }
                    // 文件路径
                    if (cursor.getString(9) != null) {
                        musicInfo.setMusic_fileUrl(cursor.getString(9));
                    }
                    musicInfo.setMusic_sortLetters("");
                    musicInfoList.add(musicInfo);
//                }

            }while (cursor.moveToNext());
        }

        return musicInfoList;
    }


    /**
     *
     * @param context
     * @return    返回的集合就是标记完成的集合
     */
    public static List<MusicInfo> getMusicInfoList(Context context){


        pinyinComparator = new PinyinComparator();    //实例化拼音比较类
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        musicInfoList = filledData(MediaUtil.getAllMusicInfo(context));

        return musicInfoList;
    }

    /**
     * 为ListView填充数据
     * @param musicList   要排序对象的集合
     * @return       返回的是排序之后的集合
     */
    private static List<MusicInfo> filledData(List<MusicInfo> musicList) {
        List<MusicInfo> musicInfoArrayList = new ArrayList<>();
        MusicInfo musicInfo;
        for (int i = 0; i < musicList.size(); i++) {
            musicInfo = musicList.get(i);

            String title = musicInfo.getMusic_title();    //这个对象的歌曲名字
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(title);
            String sortString = pinyin.substring(0, 1).toUpperCase();
            //正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                musicInfo.setMusic_sortLetters(sortString.toUpperCase());
            } else {
                musicInfo.setMusic_sortLetters("#");
            }

            musicInfoArrayList.add(musicInfo);
        }
        return musicInfoArrayList;
    }


}


