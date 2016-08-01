package zhiyuan3g.com.mymusic.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import zhiyuan3g.com.mymusic.R;
import zhiyuan3g.com.mymusic.adapter.GuideViewPagerAdapter;
import zhiyuan3g.com.mymusic.dao.AlbumInfoDao;
import zhiyuan3g.com.mymusic.dao.ArtistInfoDao;
import zhiyuan3g.com.mymusic.dao.FolderInfoDao;
import zhiyuan3g.com.mymusic.dao.MusicInfoDao;
import zhiyuan3g.com.mymusic.entity.AlbumInfo;
import zhiyuan3g.com.mymusic.entity.ArtistInfo;
import zhiyuan3g.com.mymusic.entity.FolderInfo;
import zhiyuan3g.com.mymusic.entity.MusicInfo;
import zhiyuan3g.com.mymusic.utils.AlbumInfoPinyinComparator;
import zhiyuan3g.com.mymusic.utils.ArtistInfoPinyinComparator;
import zhiyuan3g.com.mymusic.utils.CharacterParser;
import zhiyuan3g.com.mymusic.utils.MediaUtil;
import zhiyuan3g.com.mymusic.utils.PinyinComparator;

public class GuideActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    private GuideViewPagerAdapter vpAdapter;
    private ViewPager vp;
    private List<View> viewList;
    private Button btn_start_in;   //进入按键
    private boolean isLogin = false;       //判断数据库是否加载完毕
    private boolean isOK = false;          //判断是否点击了按键

    private ImageView[] dots;      //点图的图片视图
    private int[] ids = {R.id.iv_one,R.id.iv_two,R.id.iv_three};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        initViews();
        initDots();

        new MyAsyncTask().execute();     //异步任务开始
    }

    //实例化
    private void initViews(){
        final LayoutInflater inflater = LayoutInflater.from(this);

        viewList = new ArrayList<>();
        viewList.add(inflater.inflate(R.layout.guide_one,null));
        viewList.add(inflater.inflate(R.layout.guide_two, null));
        viewList.add(inflater.inflate(R.layout.guide_three,null));

        vpAdapter = new GuideViewPagerAdapter(viewList,this);
        vp = (ViewPager) findViewById(R.id.viewPager);
        vp.setAdapter(vpAdapter);

        btn_start_in = (Button) viewList.get(2).findViewById(R.id.btn_start_in);
        btn_start_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOK = true;
                mHandle.sendEmptyMessage(1);
            }
        });

        vp.setOnPageChangeListener(this);
    }

    //循环实例化点
    private void initDots(){
        dots = new ImageView[viewList.size()];
        for (int i = 0;i<viewList.size();i++){
            dots[i] = (ImageView) findViewById(ids[i]);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0;i<ids.length;i++){
            if (position == i){
                dots[i].setImageResource(R.drawable.dian_red);
            }else {
                dots[i].setImageResource(R.drawable.dian_blue);
            }
        }
    }

    //当滑动状态被改变时的方法
    @Override
    public void onPageScrollStateChanged(int state) {

    }


    class MyAsyncTask extends AsyncTask<String,Void,String>{

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

        MusicInfoDao musicInfoDao = new MusicInfoDao(GuideActivity.this);
        ArtistInfoDao artistInfoDao = new ArtistInfoDao(GuideActivity.this);
        AlbumInfoDao albumInfoDao = new AlbumInfoDao(GuideActivity.this);
        FolderInfoDao folderInfoDao = new FolderInfoDao(GuideActivity.this);

        @Override
        protected String doInBackground(String... params) {
            //把数据写入数据库中　　　音乐
            musicInfoList = musicFilledData(MediaUtil.getAllMusicInfo(GuideActivity.this));
            for (int i=0;i<musicInfoList.size();i++){
                musicInfo= musicInfoList.get(i);
                musicInfoDao.addMusicInfo(musicInfo);
                Log.i("music_info",musicInfo.getMusic_title()+musicInfo.getMusic_sortLetters());
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

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            isLogin = true;
            mHandle.sendEmptyMessage(1);    //发送消息  表示异步加载已经完成
            super.onPostExecute(s);
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
            musicInfoList = MediaUtil.getMusicInfoList(GuideActivity.this);
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
            musicInfoList = MediaUtil.getMusicInfoList(GuideActivity.this);
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
            musicInfoList = MediaUtil.getMusicInfoList(GuideActivity.this);
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

    //判断异步加载 和 按键都是OK了
    private void initOK(){
        if (isLogin&&isOK){
            Intent intent = new Intent(GuideActivity.this,BelowMainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    Handler mHandle = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            initOK();
        }
    };

}
