package zhiyuan3g.com.mymusic.activity;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

import zhiyuan3g.com.mymusic.R;
import zhiyuan3g.com.mymusic.dao.MusicInfoDao;
import zhiyuan3g.com.mymusic.entity.MusicInfo;
import zhiyuan3g.com.mymusic.lib.SlidingMenu;
import zhiyuan3g.com.mymusic.service.Config;
import zhiyuan3g.com.mymusic.service.Player;
import zhiyuan3g.com.mymusic.utils.Constant;

public class BelowMainActivity extends TabActivity implements View.OnClickListener, SlidingMenu.OnOpenedListener {

    private TabHost tabHost;      //切换窗口对象

    SlidingMenu menu;             //获得侧滑栏对象
    private ProgressBar progress_bar;     //进度条对象
    private MusicInfoDao musicInfoDao;
    private TextView txt_show_flog;      //显示  Relax  音乐
    private TextView txt_show_sing_name;  //显示当前播放的音乐名字
    private TextView txt_show_singer_name;   //显示当前播放歌曲的歌手名字
    private ImageButton img_btn_playAndPauseMusic;    //播放和暂停播放
    private ImageButton img_btn_nextMusic;       //下一首播放
    private ImageButton img_btn_musicList;       //播放列表按键

    // TODO: 2016/3/16 gao
    private boolean isFirst = true;     //判断是否是第一次播放  就是当前有没有播放的歌曲

    private int playFlog = 1;     //标志当前的播放按键的背景是 播放还是暂停

    Player player = Player.getInstance();
    private int mode ;               //获取当前的播放模式

    private TextView txt_show_search_song;          //侧边栏的收索歌曲
    private TextView txt_play_mode;                 //侧边栏的歌曲播放模式
    private TextView txt_show_change_back;          //侧边栏的改变背景
    private TextView txt_show_sleep;                //侧边栏的睡眠模式
    private TextView txt_show_setting;              //侧边栏的设置
    //模式设置 显示 的文字
    private static final String modeName[] = { "单曲播放", "单曲循环", "列表循环", "随机播放" };
    //模式设置 显示 的图片
    private int modeDrawable[] = { R.drawable.icon_sequence,
            R.drawable.icon_single_repeat, R.drawable.icon_list_reapeat,
            R.drawable.icon_shuffle };

    private int mScreenWidth;                //屏幕宽度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_below_main);

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;

        init_tab();      //实例化

//        intoMode();      //初始化播放模式
        player.setMode(Config.MODE_SEQUENCE);     //默认是列表循环播放

        initView();      //实例化控件

        slidingMenu();   //右边侧滑栏

        receiverFlog();  //注册广播
    }

    //初始化当前的模式
    private void intoMode(){
        mode = player.getMode();
    }

    //实例化控件
    public void initView() {
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        txt_show_flog = (TextView) findViewById(R.id.txt_show_flog);
        txt_show_flog.setVisibility(View.VISIBLE);          //开始是显示的
        txt_show_sing_name = (TextView) findViewById(R.id.txt_show_sing_name);
        txt_show_singer_name = (TextView) findViewById(R.id.txt_show_singer_name);
        txt_show_sing_name.setVisibility(View.INVISIBLE);    //开始是不显示的
        txt_show_singer_name.setVisibility(View.INVISIBLE);

        img_btn_playAndPauseMusic = (ImageButton) findViewById(R.id.img_btn_playAndPauseMusic);
        img_btn_nextMusic = (ImageButton) findViewById(R.id.img_btn_nextMusic);
        img_btn_musicList = (ImageButton) findViewById(R.id.img_btn_musicList);

        img_btn_playAndPauseMusic.setOnClickListener(this);
        img_btn_musicList.setOnClickListener(this);
        img_btn_nextMusic.setOnClickListener(this);

        musicInfoDao = new MusicInfoDao(BelowMainActivity.this);
    }

    int currentPosition;   //当前播放音乐在数据库中的索引值
    MusicInfo musicInfo;   //当前播放的音乐对象
    int time = 0;          //起始位置是0   也就是当前位置
    int max;              //最大位置

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            isFirst = false;// TODO: 2016/3/16
            if (time < max) {
                time += 1;
                Message message = new Message();
                message.what = 0x123;
                mHandle.sendMessage(message);
                mHandle.postDelayed(runnable, 1000);    //延时一秒钟启动这个线程
            }
        }
    };

    Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x123) {
                progress_bar.setMax(max);
                progress_bar.setProgress(time);
            }
        }
    };


    //注册广播  用来接收打开或关闭侧边栏
    private void receiverFlog() {
        //注册广播接收器
        IntentFilter filter = new IntentFilter(Config.RECEIVER_MUSIC_CHANGE);
        SearchReceiver receiver = new SearchReceiver();
        registerReceiver(receiver, filter);
    }

    //点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_btn_playAndPauseMusic:    //暂停或者播放
                if (playFlog == 1){     //表示正在暂停
                    playFlog = 0;
                    Intent intent = new Intent("PLAY_OR_PAUSE");
                    intent.putExtra("content", "play");
                    sendBroadcast(intent);        //发送广播

                    mHandle.postDelayed(runnable, 0);               //立即启动线程

                    img_btn_playAndPauseMusic.setBackgroundResource(R.drawable.pausemusic);
                }else if (playFlog == 0){    //表示正在播放
                    playFlog = 1;
                    Intent intent = new Intent("PLAY_OR_PAUSE");
                    intent.putExtra("content","pause");
                    sendBroadcast(intent);        //发送广播

                    mHandle.removeCallbacks(runnable);              //销毁当前的线程
                    progress_bar.setProgress(time);                 //把当前的进度条进度保持不变


                    img_btn_playAndPauseMusic.setBackgroundResource(R.drawable.playmusic);
                }
                break;
            case R.id.img_btn_nextMusic:            //下一首
                Intent intent = new Intent("PLAY_OR_PAUSE");intent.putExtra("content","next");
                sendBroadcast(intent);        //发送广播

                currentPosition += 1;       //当前索引加一
                if (currentPosition<musicInfoDao.getAllMusicInfo().size()){
                    changeView();           //再创建一个新的线程区
                }

                break;
            case R.id.img_btn_musicList:            //音乐列表

                break;

            case R.id.txt_show_search_song:         //扫描歌曲
                Intent intent1 = new Intent(BelowMainActivity.this,AgainSearchActivity.class);
                startActivityForResult(intent1,1);
                break;

            case R.id.txt_play_mode:                //播放模式
                changeMode();       //改变模式
                break;

            case R.id.txt_show_change_back:         //改变背景
                break;

            case R.id.txt_show_sleep:               //睡眠模式
                showSleepDialog();
                break;

            case R.id.txt_show_setting:             //设置
                break;
        }
    }

    //改变播放模式
    private void changeMode() {
        mode++;       //模式自加
        if (mode > Config.MODE_RANDOM) {      //如果模式大于 3 就是随机播放
            mode = Config.MODE_REPEAT_SINGLE;    //就把模式设置成单曲播放模式
        }
        player.setMode(mode);       //设置模式
        initPlayMode();
    }

    //改变设置模式 显示的 图片 和 文字
    private void initPlayMode() {
        txt_play_mode.setText(modeName[mode]);
        Drawable drawable = getResources().getDrawable(modeDrawable[mode]);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        txt_play_mode.setCompoundDrawables(drawable, null, null, null);
    }

    @Override
    public void onOpened() {
        mode = player.getMode();
        initPlayMode();
    }


    //广播接收者  用来接收广播来打开或者关闭侧边栏
    public class SearchReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String content = intent.getStringExtra("msgContent");
            if (!content.equals("")){
                switch (content) {
                    case "msgOpen":             //接收来自LocalMusicActivity 窗口的广播
                        showRightMenu();        //打开或者关闭侧边栏
                        break;
                    case "jump":                //接收来自LocalSingFragment 碎片的广播
                        Intent intent1 = new Intent(BelowMainActivity.this, SearchActivity.class);
                        startActivity(intent1); //用来跳转到收索歌曲窗口
                        break;
                    case "player":              //接收当前播放歌曲在数据库中的索引位置
                        currentPosition = intent.getIntExtra("position", 0);
                        changeView();
                        break;
                    case "pause":               //暂停返回的广播
                        playFlog = 1;
                        img_btn_playAndPauseMusic.setBackgroundResource(R.drawable.playmusic);
                        mHandle.removeCallbacks(runnable);              //先销毁当前的线程
                        progress_bar.setProgress(time);                 //再把进度条定在暂停的位置上
                        break;
                    case "rePlay":              //暂停之后重新播放返回的广播
                        playFlog = 0;
                        img_btn_playAndPauseMusic.setBackgroundResource(R.drawable.pausemusic);
                        if (time!=0){
                            progress_bar.setProgress(time);  //如果进度不是零 则从零开始
                        }
                        mHandle.postDelayed(runnable, 0);               //立即启动线程
                        break;
                    case "exit":
                        System.exit(0);      //退出程序
                        break;

                    case "notification":        //接收了来自notification的消息
                        currentPosition +=1;
                        changeView();
                        break;


                    default:

                        break;
                }
            }else {
                mode = player.getMode();               //获取当前的播放模式
                // TODO: 2016/3/20 注释提示
//                Toast.makeText(BelowMainActivity.this,"没有信息",Toast.LENGTH_SHORT).show();
                Log.i("TAG", mode + "模式========================");
                switch (mode){
                    case Config.MODE_REPEAT_SINGLE:
                        // 单曲播放

                        break;
                    case Config.MODE_REPEAT_ALL:
                        // 单曲循环
                        changeView();
                        break;
                    case Config.MODE_SEQUENCE:
                        // 列表循环
                        currentPosition += 1;       //当前索引加一
                        if (currentPosition<=musicInfoDao.getAllMusicInfo().size()){
                            changeView();           //再创建一个新的线程区
                        }else {
                            currentPosition = 1;
                            changeView();           //再创建一个新的线程区
                        }
                        break;
                    case Config.MODE_RANDOM:
                        // 随机循环
                        int randomNum = intent.getIntExtra("number",1);
                        currentPosition = randomNum;
                        changeView();
                        break;
                    default:
                        break;
                }
            }

        }
    }

    //接到新的音乐索引时
    private void changeView(){
        time = 0;
        // TODO: 2016/3/16
        musicInfo = musicInfoDao.getAllMusicInfo().get(currentPosition);//获取这首歌的实体类对象。
        max = musicInfo.getMusic_duration() / 1000;         //设置进度条的最大值
        if (isFirst) {
            mHandle.postDelayed(runnable, 1000);               //立即启动线程
        } else {
            mHandle.removeCallbacks(runnable);              //销毁当前的线程
            mHandle.postDelayed(runnable, 1000);               //立即重新启动一个新的线程
        }

        txt_show_flog.setVisibility(View.GONE);
        txt_show_sing_name.setVisibility(View.VISIBLE);
        txt_show_singer_name.setVisibility(View.VISIBLE);

        //判断歌曲名字的长度是否超长
        if (musicInfo.getMusic_title().length()>15){
            txt_show_sing_name.setText(musicInfo.getMusic_title().substring(0,15)+"...");
        }else {
            txt_show_sing_name.setText(musicInfo.getMusic_title());
        }

        txt_show_singer_name.setText(musicInfo.getMusic_singer());
        img_btn_playAndPauseMusic.setBackgroundResource(R.drawable.pausemusic);    //把播放按键变成暂停按键
        playFlog = 0;    //如果是暂停则是 0


        Intent intent = new Intent("TITLE_AND_NAME");
        intent.putExtra("FLAG",4);
        intent.putExtra("title",musicInfo.getMusic_title());
        intent.putExtra("name",musicInfo.getMusic_singer());
        sendBroadcast(intent);
    }









    //睡眠模式  弹框
    public void showSleepDialog() {

        if (Constant.mIsSleepClockSetting) {
            cancleSleepClock();     //取消睡眠时钟
            Toast.makeText(getApplicationContext(), "已取消睡眠模式！",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        View view = View.inflate(this, R.layout.sleep_time, null);
        final Dialog dialog = new Dialog(this, R.style.lrc_dialog);    //设置一些弹框属性
        dialog.setContentView(view);                    //压入布局
        dialog.setCanceledOnTouchOutside(false);        //设置点击侧边不取消

        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        // lp.x = 100; // 新位置X坐标
        // lp.y = 100; // 新位置Y坐标
        lp.width = (int) (mScreenWidth * 0.7); // 宽度
        // lp.height = 400; // 高度

        // 当Window的Attributes改变时系统会调用此函数,可以直接调用以应用上面对窗口参数的更改,也可以用setAttributes
        // dialog.onWindowAttributesChanged(lp);
        dialogWindow.setAttributes(lp);        //设置属性

        dialog.show();

        //实例化控件  弹框中的
        final Button cancleBtn = (Button) view.findViewById(R.id.cancle_btn);
        final Button okBtn = (Button) view.findViewById(R.id.ok_btn);
        final EditText timeEt = (EditText) view.findViewById(R.id.time_et);
        View.OnClickListener listener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (v == cancleBtn) {
                    dialog.dismiss();
                } else if (v == okBtn) {
                    String timeS = timeEt.getText().toString();
                    if (TextUtils.isEmpty(timeS)
                            || Integer.parseInt(timeS) == 0) {
                        Toast.makeText(getApplicationContext(), "输入无效！",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    setSleepClock(timeS);    //设置睡眠闹钟
                    dialog.dismiss();
                }
            }
        };

        cancleBtn.setOnClickListener(listener);
        okBtn.setOnClickListener(listener);
    }

    /**
     * 设置睡眠闹钟
     *
     * @param timeS
     */
    private void setSleepClock(String timeS) {
        Intent intent = new Intent(Config.RECEIVER_MUSIC_CHANGE);
        intent.putExtra("msgContent","exit");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(BelowMainActivity.this, 0, intent, 0);
        // 设置time时间之后退出程序
        int time = Integer.parseInt(timeS);
        long longTime = time * 60 * 1000L;
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.set(AlarmManager.RTC, System.currentTimeMillis() + longTime,
                pendingIntent);
        Constant.mIsSleepClockSetting = true;
        Toast.makeText(getApplicationContext(), "将在"+timeS+"分钟后退出软件", Toast.LENGTH_SHORT)
                .show();
    }

    /**
     * 取消睡眠闹钟
     */
    private void cancleSleepClock() {
        Intent intent = new Intent(Config.RECEIVER_MUSIC_CHANGE);
        intent.putExtra("msgContent","");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(BelowMainActivity.this, 0, intent, 0);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(pendingIntent);
        Constant.mIsSleepClockSetting = false;
    }















    //右边的侧边栏
    private void slidingMenu() {
        //configure the SlidingMenu
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.RIGHT);
        // 设置触摸屏幕的模式
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);     //窗口和侧边栏之间的竖线之间的过渡条

        // 设置滑动菜单视图的宽度
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        menu.setFadeDegree(0.35f);
        /**
         * SLIDING_WINDOW will include the Title/ActionBar in the content
         * section of the SlidingMenu, while SLIDING_CONTENT does not.
         */
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        //为侧滑菜单设置布局
        menu.setMenu(R.layout.leftmenu);


        //实例化侧边栏中的控件
        txt_show_search_song = (TextView) menu.findViewById(R.id.txt_show_search_song);
        txt_play_mode = (TextView) menu.findViewById(R.id.txt_play_mode);
        txt_show_change_back = (TextView) menu.findViewById(R.id.txt_show_change_back);
        txt_show_sleep = (TextView) menu.findViewById(R.id.txt_show_sleep);
        txt_show_setting = (TextView) menu.findViewById(R.id.txt_show_setting);

        //给侧边栏总的菜单注册点击事件
        txt_show_search_song.setOnClickListener(this);
        txt_play_mode.setOnClickListener(this);
        txt_show_change_back.setOnClickListener(this);
        txt_show_sleep.setOnClickListener(this);
        txt_show_setting.setOnClickListener(this);


        Button btn_exit = (Button) menu.findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });
    }

    //右边菜单打开按键
    public void showRightMenu() {
        menu.showMenu();
    }

    //region 设置底部每个按钮点击后跳转的界面.
    private void init_tab() {
        // 获得tabHost对象,继承的TabActivity,直接取对象.
        tabHost = getTabHost();
        TabHost.TabSpec spec_home = tabHost
                .newTabSpec("home")
                .setIndicator("home")
                .setContent(
                        new Intent().setClass(BelowMainActivity.this,
                                GotoMainActivity.class).addFlags(
                                Intent.FLAG_ACTIVITY_CLEAR_TOP));
        tabHost.addTab(spec_home);

    }
    //endregion


    //region 返回按键   操作
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click();      //调用双击退出函数
        }
        if (keyCode == KeyEvent.KEYCODE_MENU){
            Toast.makeText(BelowMainActivity.this,"点击了111",Toast.LENGTH_SHORT).show();
        }
        return super.onKeyUp(keyCode, event);
    }
    //endregion

    //region 双击退出函数
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (!menu.isMenuShowing()){
            if (isExit == false) {
                isExit = true; // 准备退出
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                tExit = new Timer();
                tExit.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        isExit = false; // 取消退出
                    }
                }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

            } else {
                finish();
                System.exit(0);
            }
        }else {
            menu.showContent();     //如果侧滑栏是打开的  则先关闭侧滑栏  再退出程序
        }

    }
    //endregion
}
