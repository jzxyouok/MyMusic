package zhiyuan3g.com.mymusic.searchmusicfragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import zhiyuan3g.com.mymusic.R;
import zhiyuan3g.com.mymusic.activity.AgainSearchActivity;
import zhiyuan3g.com.mymusic.dao.AlbumInfoDao;
import zhiyuan3g.com.mymusic.dao.ArtistInfoDao;
import zhiyuan3g.com.mymusic.dao.FolderInfoDao;
import zhiyuan3g.com.mymusic.dao.MusicInfoDao;
import zhiyuan3g.com.mymusic.db.DatabaseHelper;
import zhiyuan3g.com.mymusic.entity.AlbumInfo;
import zhiyuan3g.com.mymusic.entity.ArtistInfo;
import zhiyuan3g.com.mymusic.entity.FolderInfo;
import zhiyuan3g.com.mymusic.entity.MusicInfo;
import zhiyuan3g.com.mymusic.utils.AlbumInfoPinyinComparator;
import zhiyuan3g.com.mymusic.utils.ArtistInfoPinyinComparator;
import zhiyuan3g.com.mymusic.utils.CharacterParser;
import zhiyuan3g.com.mymusic.utils.MediaUtil;
import zhiyuan3g.com.mymusic.utils.PinyinComparator;

/**
 * Created by xuGuang on 2016/3/18.
 */
public class MenuScanFragment extends Fragment implements View.OnClickListener{

    private Button mScanBtn;
    private ImageButton mBackBtn;
    private Handler mHandler;
    private DatabaseHelper mHelper;
    private ProgressDialog mProgress;


    List<MusicInfo> musicInfoList;
    List<ArtistInfo> artistInfoList;
    List<AlbumInfo> albumInfoList;
    List<FolderInfo> folderInfoList;

    CharacterParser characterParser = CharacterParser.getInstance();

    PinyinComparator pinyinComparator = new PinyinComparator();
    AlbumInfoPinyinComparator albumInfoPinyinComparator = new AlbumInfoPinyinComparator();
    ArtistInfoPinyinComparator artistInfoPinyinComparator = new ArtistInfoPinyinComparator();

    MusicInfo musicInfo;
    ArtistInfo artistInfo;
    AlbumInfo albumInfo;
    FolderInfo folderInfo;

    MusicInfoDao musicInfoDao = new MusicInfoDao(getActivity());
    ArtistInfoDao artistInfoDao = new ArtistInfoDao(getActivity());
    AlbumInfoDao albumInfoDao = new AlbumInfoDao(getActivity());
    FolderInfoDao folderInfoDao = new FolderInfoDao(getActivity());


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new DatabaseHelper(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_scan_fragment, container, false);

        mScanBtn = (Button) view.findViewById(R.id.scanBtn);
        mBackBtn = (ImageButton) view.findViewById(R.id.backBtn);
        mScanBtn.setOnClickListener(this);
        mBackBtn.setOnClickListener(this);

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mProgress.dismiss();
                ((AgainSearchActivity)getActivity()).viewPager_again.setCurrentItem(0, true);
            }
        };

        return view;
    }

    private void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mHelper.deleteTables(getActivity());

                //把数据写入数据库中　　　音乐
                musicInfoList = musicFilledData(MediaUtil.getAllMusicInfo(getActivity()));
                for (int i=0;i<musicInfoList.size();i++){
                    musicInfo= musicInfoList.get(i);
                    musicInfoDao.addMusicInfo(musicInfo);
                    Log.i("music_info", musicInfo.getMusic_title() + musicInfo.getMusic_sortLetters());
                }

                //把数据写入数据库中　　　歌手
                artistInfoList = getArtistInfoList();
                for (int i = 0;i<artistInfoList.size();i++){
                    artistInfo = artistInfoList.get(i);
                    artistInfoDao.addArtistInfo(artistInfo);
                    Log.i("artistInfo",artistInfo.getSingerCount()+"");
                }

                //把数据写入数据库中     专辑
                albumInfoList = getAlbumInfoList();
                for (int i = 0;i<albumInfoList.size();i++){
                    albumInfo = albumInfoList.get(i);
                    albumInfoDao.addAlbumInfo(albumInfo);
                    Log.i("albumInfo",albumInfo.getAlbum_name());
                }

                //把数据写入数据库中     文件夹
                folderInfoList = getFolderInfos();
                for (int i =0;i<folderInfoList.size();i++){
                    folderInfo = folderInfoList.get(i);
                    folderInfoDao.addFolderInfo(folderInfo);
                    Log.i("folderInfo",folderInfo.getFolder_name());
                }

                mHandler.sendEmptyMessage(1);
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        if (v == mScanBtn){
            mProgress = new ProgressDialog(getActivity());
            mProgress.setMessage("正在扫描歌曲，请勿退出软件！");
            mProgress.setCancelable(false);
            mProgress.setCanceledOnTouchOutside(false);
            mProgress.show();
            getData();
        } else if(v == mBackBtn) {
            ((AgainSearchActivity)getActivity()).viewPager_again.setCurrentItem(0, true);
        }
    }








    /**
     * 为ListView填充数据
     * @param musicList   要排序对象的集合
     * @return       返回的是排序之后的集合
     */
    private List<MusicInfo> musicFilledData(List<MusicInfo> musicList) {
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
        Collections.sort(musicInfoArrayList, pinyinComparator);
        return musicInfoArrayList;
    }




    /**
     * 为ListView添加数据
     * @param artistInfoList   要排序的对象
     * @return    排序完成后的集合
     */
    private List<ArtistInfo> singerFilledData(List<ArtistInfo> artistInfoList){
        List<ArtistInfo> artistInfos = new ArrayList<>();
        ArtistInfo artistInfo;
        for (int i=0;i<artistInfoList.size();i++){
            artistInfo = artistInfoList.get(i);

            String singer = artistInfo.getSingerName();   //这个对象的歌手名字
            String pinyin = characterParser.getSelling(singer);
            String sortString = pinyin.substring(0,1).toUpperCase();
            //正则表达式  判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")){
                artistInfo.setArtist_sortLetters(sortString.toUpperCase());
            }else {
                artistInfo.setArtist_sortLetters("#");
            }
            artistInfos.add(artistInfo);
        }
        return artistInfos;
    }


    /**
     * 歌手相同筛选方法    加排序
     * 必须注意  j 和 i 的条件是一样的
     * @return  一个新的集合  存放的是筛选之后的信息
     */
    private List<ArtistInfo> getArtistInfoList(){
        //把数据写入数据库中
        musicInfoList = MediaUtil.getMusicInfoList(getActivity());
        List<ArtistInfo> artistInfoList = null;
        artistInfoList = new ArrayList<>();
        for (int i =0;i<musicInfoList.size();i++){
            int a = 1;
            for (int j =i+1;j<musicInfoList.size();j++){
                if (!musicInfoList.get(j).getMusic_singer().equals("")){
                    if (musicInfoList.get(i).getMusic_singer().equals(musicInfoList.get(j).getMusic_singer())){
                        a += 1;
                        musicInfoList.get(j).setMusic_singer("");
                    }
                }
            }
            if (!musicInfoList.get(i).getMusic_singer().equals("")){
                ArtistInfo artistInfo = new ArtistInfo(0,musicInfoList.get(i).getMusic_singer(),a,musicInfoList.get(i).getMusic_sortLetters());
                artistInfoList.add(artistInfo);
            }
        }
        List<ArtistInfo> artistInfos = singerFilledData(artistInfoList);
        //根据a-z 进行排序数据源
        Collections.sort(artistInfos, artistInfoPinyinComparator);
        return artistInfos;
    }

    /**
     * 专辑筛选条件      加排序方法
     * 必须注意  j 和 i 的条件是一样的
     * @return  一个新的集合  存放的是筛选之后的信息
     */
    private List<AlbumInfo> getAlbumInfoList() {
        //把数据写入数据库中　　　音乐
        musicInfoList = MediaUtil.getMusicInfoList(getActivity());
        List<AlbumInfo> albumInfoList = new ArrayList<>();
        for (int i = 0; i < musicInfoList.size(); i++) {
            int a = 1;
            for (int j = i + 1; j < musicInfoList.size(); j++) {
                if (!musicInfoList.get(i).getMusic_album().equals("")) {
                    if (musicInfoList.get(i).getMusic_album().equals(musicInfoList.get(j).getMusic_album())) {
                        a += 1;
                        musicInfoList.get(j).setMusic_album("");
                    }
                }
            }
            if (!musicInfoList.get(i).getMusic_album().equals("")) {
                AlbumInfo albumInfo = new AlbumInfo(0,musicInfoList.get(i).getMusic_album(),a,musicInfoList.get(i).getMusic_singer(),"");
                albumInfoList.add(albumInfo);
            }
        }
        List<AlbumInfo> albumInfos = filledData(albumInfoList);
        //根据a-z 进行排序数据源
        Collections.sort(albumInfos, albumInfoPinyinComparator);
        return albumInfos;
    }


    /**
     * 为ListView填充数据        专辑
     * @param albumInfoList   要排序对象的集合
     * @return       返回的是排序之后的集合
     */
    private List<AlbumInfo> filledData(List<AlbumInfo> albumInfoList) {
        List<AlbumInfo> albumInfoArrayList = new ArrayList<>();
        AlbumInfo albumInfo;
        for (int i = 0; i < albumInfoList.size(); i++) {
            albumInfo = albumInfoList.get(i);

            String album = albumInfo.getAlbum_name();    //这个对象的专辑名字
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(album);
            String sortString = pinyin.substring(0, 1).toUpperCase();
            //正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                albumInfo.setAlbum_sortLetters(sortString.toUpperCase());
            } else {
                albumInfo.setAlbum_sortLetters("#");
            }

            albumInfoArrayList.add(albumInfo);
        }
        return albumInfoArrayList;
    }


    /**
     * 专辑筛选条件     无须排序
     * 必须注意  j 和 i 的条件是一样的
     * @return  一个新的集合  存放的是筛选之后的信息
     */
    private List<FolderInfo> getFolderInfos() {
        //把数据写入数据库中　　　音乐
        musicInfoList = MediaUtil.getMusicInfoList(getActivity());
        List<FolderInfo> folderInfoList = null;
        folderInfoList = new ArrayList<>();
        for (int i = 0; i < musicInfoList.size(); i++) {
            int a = 1;
            for (int j = i + 1; j < musicInfoList.size(); j++) {
                if (!musicInfoList.get(i).getMusic_fileUrl().equals("")) {
                    //i 的文件夹名称
                    String folderI = musicInfoList.get(i).getMusic_fileUrl().substring(0, musicInfoList.get(i).getMusic_fileUrl().lastIndexOf("/"));
                    //j 的文件夹名称
                    String folderJ = musicInfoList.get(j).getMusic_fileUrl().substring(0, musicInfoList.get(j).getMusic_fileUrl().lastIndexOf("/"));
                    if (folderI.equals(folderJ)) {      //比较两个文件夹名称是否一致
                        a += 1;
                        musicInfoList.get(j).setMusic_fileUrl("");
                    }
                }
            }

            if (!musicInfoList.get(i).getMusic_fileUrl().equals("")) {
                //文件夹的名字
                String folderName = musicInfoList.get(i).getMusic_fileUrl().substring(0, musicInfoList.get(i).getMusic_fileUrl().lastIndexOf("/"));
                //文件名
                String fileName = folderName.substring(folderName.lastIndexOf("/") + 1, folderName.length());
                FolderInfo folderInfo = new FolderInfo(0,a,folderName,fileName);
                folderInfoList.add(folderInfo);
            }
        }
        return folderInfoList;
    }
}
