package zhiyuan3g.com.mymusic.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import zhiyuan3g.com.mymusic.R;
import zhiyuan3g.com.mymusic.adapter.LocalFragmentPagerAdapter;
import zhiyuan3g.com.mymusic.dao.MusicInfoDao;
import zhiyuan3g.com.mymusic.entity.MusicInfo;
import zhiyuan3g.com.mymusic.localfragment.LocalHolderFragment;
import zhiyuan3g.com.mymusic.localfragment.LocalSingFragment;
import zhiyuan3g.com.mymusic.localfragment.LocalSingerFragment;
import zhiyuan3g.com.mymusic.localfragment.LocalSpecialFragment;
import zhiyuan3g.com.mymusic.service.Config;
import zhiyuan3g.com.mymusic.service.Player;
import zhiyuan3g.com.mymusic.service.PlayerService;

public class LocalMusicActivity extends AppCompatActivity implements View.OnClickListener,
        RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener ,LocalSingFragment.LocalSingFragmentCallBack{


    TabGroupActivity parentActivity1;       //Table  对象
    private ViewPager localViewPager;       //viewPager对象
    private List<Fragment> fragmentList;    //存放碎片的集合
    private LocalFragmentPagerAdapter pagerAdapter;     //碎片适配器

    private ImageButton btn_img_back_local;     //返回按键

    //代表页面的常量      用来表示切换页面
    public static final int LOCAL_SING = 0;
    public static final int LOCAL_SINGER = 1;
    public static final int LOCAL_SPACIAL = 2;
    public static final int LOCAL_FOLDER = 3;

    //四个按键
    private RadioGroup rg_local_top_bar;
    private RadioButton rb_local_sing;       //歌曲按键
    private RadioButton rb_local_singer;     //歌手按键
    private RadioButton rb_local_special;    //专辑按键
    private RadioButton rb_local_folder;     //文件夹按键

    //四个按键下面的走动条
    private ImageView img_local_sing;
    private ImageView img_local_singer;
    private ImageView img_local_special;
    private ImageView img_local_folder;

    //收索歌曲
    private ImageButton img_btn_search_sing;

    public static final String SEARCH_NEW_BROAD = "SEARCH_NEW_BROAD";



    PlayerService mService;   //服务对象
    MusicInfoDao musicInfoDao;     //MusicInfo  的操作类


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_music);


        intoView();         //实例化控件
        onListen();         //注册监听事件
        intoViewPager();    //实例化viewPager
        intoImage();        //实例化滑动条

        receiverFlog();     //注册广播

        localViewPager.getBackground().setAlpha(70);     //设置透明度

        Intent intent = new Intent(LocalMusicActivity.this,PlayerService.class);
        getApplicationContext().bindService(intent,conn, Context.BIND_AUTO_CREATE);    //绑定服务
    }

    //创建服务控制类
    public ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = ((PlayerService.LocalPlayer)service).getService();
            Log.i("TAG","绑定");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    /**
     * 实例化控件
     */
    private void intoView() {
        parentActivity1 = (TabGroupActivity) getParent();

        //按键实例化
        btn_img_back_local = (ImageButton) findViewById(R.id.btn_img_back_local);

        //初始化RadioButton
        rg_local_top_bar = (RadioGroup) findViewById(R.id.rg_local_top_bar);
        rb_local_sing = (RadioButton) findViewById(R.id.rb_local_sing);
        rb_local_singer = (RadioButton) findViewById(R.id.rb_local_singer);
        rb_local_special = (RadioButton) findViewById(R.id.rb_local_special);
        rb_local_folder = (RadioButton) findViewById(R.id.rb_local_folder);

        musicInfoDao = new MusicInfoDao(LocalMusicActivity.this);

        //实例化收索按键
        img_btn_search_sing = (ImageButton) findViewById(R.id.img_btn_search_sing);

    }

    //注册按键的监听事件
    private void onListen() {
        btn_img_back_local.setOnClickListener(this);
        rg_local_top_bar.setOnCheckedChangeListener(this);     //RadioGroup 注册监听事件
        img_btn_search_sing.setOnClickListener(this);
    }

    /**
     * 初始化viewPager
     */
    private void intoViewPager() {
        localViewPager = (ViewPager) findViewById(R.id.localViewPager);
        LocalSingFragment singFragment = new LocalSingFragment();
        LocalSingerFragment singerFragment = new LocalSingerFragment();
        LocalSpecialFragment specialFragment = new LocalSpecialFragment();
        LocalHolderFragment holderFragment = new LocalHolderFragment();

        //实例化fragment并添加到fragment的集合中
        fragmentList = new ArrayList<>();
        fragmentList.add(singFragment);
        fragmentList.add(singerFragment);
        fragmentList.add(specialFragment);
        fragmentList.add(holderFragment);

        //实例化适配器  并设置适配器
        pagerAdapter = new LocalFragmentPagerAdapter(getSupportFragmentManager(),this);
        localViewPager.setAdapter(pagerAdapter);
        //默认显示第一个  歌曲 窗口
        localViewPager.setCurrentItem(LOCAL_SING);
        rb_local_sing.setChecked(true);
        //注册监听事件
        localViewPager.addOnPageChangeListener(this);
    }



    /**
     * 实例化  按键下的 滑动条
     */
    private void intoImage() {
        img_local_sing = (ImageView) findViewById(R.id.img_local_sing);
        img_local_singer = (ImageView) findViewById(R.id.img_local_singer);
        img_local_special = (ImageView) findViewById(R.id.img_local_special);
        img_local_folder = (ImageView) findViewById(R.id.img_local_folder);
    }

    /**
     * 清除滑动条的状态   全部都设置成透明的
     */
    private void clearImageStates() {
        img_local_sing.setBackgroundColor(android.graphics.Color.parseColor("#00ffffff"));
        img_local_singer.setBackgroundColor(android.graphics.Color.parseColor("#00ffffff"));
        img_local_special.setBackgroundColor(android.graphics.Color.parseColor("#00ffffff"));
        img_local_folder.setBackgroundColor(android.graphics.Color.parseColor("#00ffffff"));
    }


    /**
     * 按键监听事件   显示接口的方法
     *
     * @param v 当前点击的按键对象
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_img_back_local:
                parentActivity1.goBack();
                break;
            case R.id.img_btn_search_sing:
                //收索按键
                Intent intent = new Intent(SEARCH_NEW_BROAD);
                intent.putExtra("msgContent", "jump");
                sendBroadcast(intent);
//                Toast.makeText(LocalMusicActivity.this, "发送", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    //RadioGroup的按键监听事件
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_local_sing:        //歌曲按键
                localViewPager.setCurrentItem(LOCAL_SING);

                break;
            case R.id.rb_local_singer:      //歌手按键
                localViewPager.setCurrentItem(LOCAL_SINGER);
                break;
            case R.id.rb_local_special:     //专辑按键
                localViewPager.setCurrentItem(LOCAL_SPACIAL);
                break;
            case R.id.rb_local_folder:      //文件夹按键
                localViewPager.setCurrentItem(LOCAL_FOLDER);
                break;

        }
    }

    //重写viewPager的页面切换处理方法
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    /**
     * 此方法是在状态改变的时候调用，其中arg0这个参数有三种状态（0，1，2）。
     * arg0 ==1的时辰默示正在滑动，arg0==2的时辰默示滑动完毕了，arg0==0的时辰默示什么都没做
     *
     * @param state 状态选择
     */
    @Override
    public void onPageScrollStateChanged(int state) {

        if (state == 2) {
            switch (localViewPager.getCurrentItem()) {
                case LOCAL_SING:        //歌曲页面
                    rb_local_sing.setChecked(true);
                    clearImageStates();     //先清除所有的状态  再给当前的image设置背景
                    img_local_sing.setBackgroundResource(R.color.image_back);
                    break;
                case LOCAL_SINGER:      //歌手页面
                    rb_local_singer.setChecked(true);
                    clearImageStates();     //先清除所有的状态  再给当前的image设置背景
                    img_local_singer.setBackgroundResource(R.color.image_back);
                    break;
                case LOCAL_SPACIAL:      //专辑页面
                    rb_local_special.setChecked(true);
                    clearImageStates();     //先清除所有的状态  再给当前的image设置背景
                    img_local_special.setBackgroundResource(R.color.image_back);
                    break;
                case LOCAL_FOLDER:     //文件夹页面
                    rb_local_folder.setChecked(true);
                    clearImageStates();     //先清除所有的状态  再给当前的image设置背景
                    img_local_folder.setBackgroundResource(R.color.image_back);
                    break;
            }
        }
    }

    Handler mHandle = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x123) {
//                Toast.makeText(LocalMusicActivity.this,musicInfoDao.getAllMusicInfo().get(musicInfoPosition).getMusic_title() , Toast.LENGTH_SHORT).show();

                mService.play(musicInfoPosition);          //播放从Fragment回调过来的索引对应的歌曲

                //把当前播放的音乐在数据库中的索引发送出去
                Intent intent = new Intent(SEARCH_NEW_BROAD);
                intent.putExtra("msgContent","player");
                intent.putExtra("position",musicInfoPosition);
                sendBroadcast(intent);
            }
        }
    };

    int musicInfoPosition;      //当前的音乐索引

    //回调
    @Override
    public void getFlag(int position) {
        musicInfoPosition = position;
        Message message = new Message();
        message.what = 0x123;
        mHandle.sendMessage(message);
    }


    //注册广播  用来接收打开或关闭侧边栏
    private void receiverFlog() {
        //注册广播接收器
        IntentFilter filter = new IntentFilter("PLAY_OR_PAUSE");
        MyReceiver receiver = new MyReceiver();
        registerReceiver(receiver, filter);
    }

    class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String content = intent.getStringExtra("content");
            switch (content){
                case "play":             //暂停之后继续播放
                    mService.replay();
                    break;
                case "pause":           //暂停
                    mService.pause();
                    break;
                case "next":            //下一首
                    mService.next();
                    break;
                case "play_search":
                    musicInfoPosition = intent.getIntExtra("position",1);
                    Message message = new Message();
                    message.what = 0x123;
                    mHandle.sendMessage(message);
                    break;
            }
        }
    }
}
