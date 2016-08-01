package zhiyuan3g.com.mymusic.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import zhiyuan3g.com.mymusic.R;
import zhiyuan3g.com.mymusic.adapter.MyFragmentPagerAdapter;
import zhiyuan3g.com.mymusic.mainfragment.MyMusicFragment;
import zhiyuan3g.com.mymusic.mainfragment.MusicLibraryFragment;
import zhiyuan3g.com.mymusic.mainfragment.SingSongFragment;
import zhiyuan3g.com.mymusic.mainfragment.VideoFragment;

public class MainActivity extends FragmentActivity implements
        RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener,MyMusicFragment.LocalCallBack {


    private ViewPager mViewPager;
    private FragmentPagerAdapter adapter;
    private List<Fragment> mFragments = new ArrayList<Fragment>();

    //四个按键
    private RadioGroup rg_top_bar;
    private RadioButton rb_mine;
//    private RadioButton rb_music;
//    private RadioButton rb_sing;
//    private RadioButton rb_vedio;    // TODO: 2016/3/17   四个fragment改为两个fragment

    private ImageButton id_iv_right;    //侧滑菜单按键
    public static final String SLIDING_MENU= "SEARCH_NEW_BROAD";

    //代表页面的常量
    public static final int PAGER_ONE = 0;
//    public static final int PAGER_TWO = 1;
//    public static final int PAGER_THREE = 2;
//    public static final int PAGER_FOUR = 3;       // TODO: 2016/3/17   四个fragment改为两个fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化RadioButton
        init();

        // 初始化ViewPager
        initViewPager();
        rg_top_bar.setOnCheckedChangeListener(this);
    }


    //region初始化RadioButton
    private void init() {
        rg_top_bar = (RadioGroup) findViewById(R.id.rg_top_bar);
        rb_mine = (RadioButton) findViewById(R.id.rb_mine);
//        rb_music = (RadioButton) findViewById(R.id.rb_music);
//        rb_sing = (RadioButton) findViewById(R.id.rb_sing);
//        rb_vedio = (RadioButton) findViewById(R.id.rb_vedio);     // TODO: 2016/3/17   四个fragment改为两个fragment

        id_iv_right= (ImageButton) findViewById(R.id.id_iv_right);
        id_iv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //使用广播发送信息用来打开侧边栏
                Intent intent = new Intent(SLIDING_MENU);
                intent.putExtra("msgContent","msgOpen");
                sendBroadcast(intent);
            }
        });
    }
    //endregion

    //region 初始化ViewPager
    private void initViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
        MyMusicFragment myMusicFragment = new MyMusicFragment();
//        MusicLibraryFragment musicLibraryFragment = new MusicLibraryFragment();
//        SingSongFragment singSongFragment = new SingSongFragment();
//        VideoFragment videoFragment = new VideoFragment();    // TODO: 2016/3/17   四个fragment改为两个fragment

        mFragments.add(myMusicFragment);
//        mFragments.add(musicLibraryFragment);
//        mFragments.add(singSongFragment);
//        mFragments.add(videoFragment);        // TODO: 2016/3/17   四个fragment改为两个fragment
        //初始化Adapter
        adapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(0);
        mViewPager.addOnPageChangeListener(this);
        rb_mine.setChecked(true);
    }
    //endregion

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {
            case R.id.rb_mine:
                mViewPager.setCurrentItem(PAGER_ONE);

                break;
//            case R.id.rb_music:
//                mViewPager.setCurrentItem(PAGER_TWO);
//
//                break;
//            case R.id.rb_sing:
//                mViewPager.setCurrentItem(PAGER_THREE);       // TODO: 2016/3/17   四个fragment改为两个fragment
//
//                break;
//            case R.id.rb_vedio:
//                mViewPager.setCurrentItem(PAGER_FOUR);
//
//                break;
        }
    }

    //region重写ViewPager页面切换的处理方法
    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

        // i 的状态有三个,1表示没有操作,2表示正在滑动,3表示滑动完毕
        if (state == 2) {
            switch (mViewPager.getCurrentItem()) {
                case PAGER_ONE:
                    rb_mine.setChecked(true);
                    break;
//                case PAGER_TWO:
//                    rb_music.setChecked(true);
//                    break;
//                case PAGER_THREE:
//                    rb_sing.setChecked(true);     // TODO: 2016/3/17   四个fragment改为两个fragment
//                    break;
//                case PAGER_FOUR:
//                    rb_vedio.setChecked(true);
//                    break;
            }
        }
    }

    /**
     * LocalMusic  我的  碎片的接口 回调
     * @param task
     */
    @Override
    public void onListenLocal(String task) {
        if (task.endsWith("jump")){
            Message message = new Message();
            message.what = 0x123;
            handler.sendMessage(message);
        }
    }

    /**
     * 接收回调发来的信息来执行相应的操作
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==0x123){
                jumpMyMusic();
            }
        }
    };

    /**
     * 向 本地歌曲界面跳转
     */
    public void jumpMyMusic(){
        // 得到管理界面,添加一个界面.!
        TabGroupActivity parentActivity1 = (TabGroupActivity) getParent();
        parentActivity1.startChildActivity("LocalMusicActivity", new Intent(
                MainActivity.this, LocalMusicActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
}
