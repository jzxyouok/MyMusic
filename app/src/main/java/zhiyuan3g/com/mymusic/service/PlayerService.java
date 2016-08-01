package zhiyuan3g.com.mymusic.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;



import java.util.List;

import zhiyuan3g.com.mymusic.R;
import zhiyuan3g.com.mymusic.activity.BelowMainActivity;
import zhiyuan3g.com.mymusic.dao.MusicInfoDao;
import zhiyuan3g.com.mymusic.entity.MusicInfo;

/**
 * 音乐播放的后台服务.
 */
public class PlayerService extends Service {

    public LocalPlayer localPlayer = new LocalPlayer();
    private SystemReceiver mReceiver;
    private MusicInfoDao musicInfoDao;

    private Notification mNotification;     //notification对象
    private NotificationManager mNotificationManager;   //管理对象

    private ControlBroadcast mControlBroadcast;       //广播接收器

    private RemoteViews contentViews;        //notification

    public class LocalPlayer extends Binder{
        public PlayerService getService(){
            return PlayerService.this;
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        IntentFilter intentFilter = new IntentFilter();
        mReceiver = new SystemReceiver();
        intentFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
        intentFilter.addAction("android.intent.action.PHONE_STATE");
        registerReceiver(mReceiver,intentFilter);
        return localPlayer;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        musicInfoDao = new MusicInfoDao(this);       //实例化musicInfo数据库操作类对象

        mNotification = new Notification();    //实例化notification

        //用来管理notification的显示和消失等
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        upDateMyNotification("Relax 音乐，做自己！","");

        mControlBroadcast = new ControlBroadcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction("NOTIFICATION_PLAY_OR_PAUSE");
        filter.addAction("NOTIFICATION_NEXT_SING");
        filter.addAction("NOTIFICATION_EXIT_NOTIFICATION");
        filter.addAction("TITLE_AND_NAME");
        registerReceiver(mControlBroadcast, filter);

        Log.i("TAG", "PlayerService服务");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Player.getInstance().destroy();
        unregisterReceiver(mReceiver);
        stopForeground(true);
    }



    private void upDateMyNotification(String title, String name){

        mNotification.icon = R.drawable.play_singer_icon;
        mNotification.tickerText = title;
        mNotification.when = System.currentTimeMillis();
//        mNotification.flags = Notification.FLAG_NO_CLEAR;  //不能自动清除

        //对刚才创建的一个notification实例设置各种参数
        //其中的一个参数是flags
        //这个参数可以设置该notification是何种状态
        //这里设置的是ONGOING,表示这个notification会一直呆在通知栏上面
        //也可以设置滑动后就消失的样式,看个人的不同需求
        mNotification.flags = Notification.FLAG_ONGOING_EVENT;

        /**
         * 在我们创建的notification中有一个View子类的实例下面创建了一个RemoteViews的实例
         并且配置好各种显示的信息后将该实例传给notification往后面看会有一句
         myNotification.contentView = contentViews;这个就是传递实例了
         */
        contentViews = new RemoteViews(getPackageName(), R.layout.notification_music);
        //设置播放条的图片
        contentViews.setImageViewResource(R.id.img_show_notification,R.drawable.person_icn);

        contentViews.setTextViewText(R.id.txt_show_notification_sing_name, title);
        contentViews.setTextViewText(R.id.txt_show_notification_singer_name,name);

        //如果正在播放
//        if (Player.getInstance().getPlaying()==2){
//            contentViews.setImageViewResource(R.id.img_btn_notification_playAndPause,R.drawable.pausemusic);
//        }else {
//            contentViews.setImageViewResource(R.id.img_btn_notification_playAndPause,R.drawable.playmusic);
//        }

        contentViews.setImageViewResource(R.id.img_btn_notification_playAndPause,R.drawable.btn_play_and_pause);

        //将处理好的view传递给notification  让其在通知栏上显示
        mNotification.contentView = contentViews;

        //播放或者暂停按键的响应
        Intent playAndPauseButtonIntent = new Intent("NOTIFICATION_PLAY_OR_PAUSE");
        playAndPauseButtonIntent.putExtra("FLAG",1);
        PendingIntent pendPreviousButtonIntent = PendingIntent.getBroadcast(this, 0, playAndPauseButtonIntent, 0);
        contentViews.setOnClickPendingIntent(R.id.img_btn_notification_playAndPause, pendPreviousButtonIntent);


        //下一首按键的响应事件
        Intent nextButtonIntent = new Intent("NOTIFICATION_NEXT_SING");
        nextButtonIntent.putExtra("FLAG",2);
        PendingIntent pendNextButtonIntent = PendingIntent.getBroadcast(this, 0, nextButtonIntent, 0);
        contentViews.setOnClickPendingIntent(R.id.img_btn_notification_next, pendNextButtonIntent);

        //退出播放栏的响应事件
        Intent exitButtonIntent = new Intent("NOTIFICATION_EXIT_NOTIFICATION");
        exitButtonIntent.putExtra("FLAG",3);
        PendingIntent pendPlayButtonIntent = PendingIntent.getBroadcast(this, 0, exitButtonIntent, 0);
        contentViews.setOnClickPendingIntent(R.id.img_btn_notification_exit, pendPlayButtonIntent);

        //通知栏点击事件的响应  点击通知栏之后 下拉通知栏关闭，界面跳转到主界面
        Intent notificationIntent = new Intent(this,BelowMainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 1, notificationIntent, 0);
        mNotification.contentIntent = contentIntent;


        //用notificationManager来启动notification
        //通过调用notify这个函数来启动
        mNotificationManager.notify(1,mNotification);
    }



    private class ControlBroadcast extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int flag = intent.getIntExtra("FLAG",-1);
            switch (flag){
                case 1:                     //播放或者暂停
                    if (Player.getInstance().getPlaying()==Config.PLAYING_PAUSE){
                        localPlayer.getService().replay();
                    }else if (Player.getInstance().getPlaying()==Config.PLAYING_PLAY){
                        localPlayer.getService().pause();
                        Intent intent1 = new Intent("PLAY_OR_PAUSE");
                        intent1.putExtra("content","pause");
                        sendBroadcast(intent1);        //发送广播
                    }
                    break;
                case 2:                     //下一首歌曲
                    localPlayer.getService().next();
                    Intent intent1 = new Intent(Config.RECEIVER_MUSIC_CHANGE);
                    intent1.putExtra("msgContent","notification");
                    sendBroadcast(intent1);
                    break;
                case 3:                     //退出通知栏
                    mNotificationManager.cancel(1);
                    break;
                case 4:                     //接收当前播放音乐的信息
                    String title = intent.getStringExtra("title");
                    String name = intent.getStringExtra("name");
                    upDateMyNotification(title,name);
                    break;
            }
        }
    }




    //播放
    public MusicInfo play(List<MusicInfo> musicInfoList,int position){
        MusicInfo musicInfo = null;
        if (musicInfoList == null||musicInfoList.isEmpty()){
            play(position);
        }else {
            musicInfo = Player.getInstance().play(this, musicInfoList, position);
        }
        return musicInfo;
    }

    //默认播放
    public MusicInfo play(int position){
        MusicInfo musicInfo = new MusicInfo();
        if (musicInfoDao.getAllMusicInfo().isEmpty()){
            Toast.makeText(this,"还没有音乐呢！",Toast.LENGTH_SHORT).show();
        }else {
            musicInfo = Player.getInstance().play(this, musicInfoDao.getAllMusicInfo(), position);
        }
        return musicInfo;
    }

    //暂停
    public void pause(){
        Player.getInstance().pause();
    }

    //正在暂停，调用后开始继续播放
    public void replay(){
        Player.getInstance().replay();
    }

    //下一首
    public MusicInfo next(){
        MusicInfo musicInfo = null;
        if (Player.getInstance().getPlaying() == Config.PLAYING_STOP
                ||Player.getInstance().getList().isEmpty()){
            play(0);
        }else {
            musicInfo = Player.getInstance().next(this);

        }
        return musicInfo;
    }

    public MusicInfo previous(){
        MusicInfo musicInfo = null;
        if (Player.getInstance().getPlaying()==Config.PLAYING_STOP||Player.getInstance().getList().isEmpty()){
//            play();
        }else {
            musicInfo =  Player.getInstance().previous(this);

        }
        return musicInfo;
    }

    //打电话的时候暂停
    public class SystemReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            //如果是打电话
            if (Intent.ACTION_NEW_OUTGOING_CALL.equals(intent.getAction())){
                pause();
            }else {
                //如果是来电
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
                switch (tm.getCallState()){
                    //响铃
                    case TelephonyManager.CALL_STATE_RINGING:
                        pause();
                        break;
                    //摘机
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        pause();
                        break;
                    //空闲
                    case TelephonyManager.CALL_STATE_IDLE:
                        replay();
                        break;
                }
            }
        }
    }



    //    private final String TAG = "MusicService";
//    private MediaPlayer player;
//    private final IBinder binder = new MyBinder();
//
//
//    @Override
//    public void onCreate() {
//        Log.i(TAG,"onCreate...");
//    }
//
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.i(TAG,"onStartCommand...");
//        String state = intent.getStringExtra("PlayerState");
//        if (state!=null){
//            if (state.equals("START")){
//
//            }
//        }
//
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//
//    //开始
//    public void start() throws IOException {
//        if (player==null){
//            player = MediaPlayer.create(this, R.raw.hongmeigui);
//            player.setLooping(false);
//            //防止prepareAsync called in state 8 错误
//            if (player!=null){
//                player.stop();
//                player.prepare();
//            }
//            if (!player.isPlaying()){
//                Log.i(TAG,"player start...");
//                player.start();
//            }
//        }
//    }
//
//    //暂停
//    public void pause(){
//        if (player !=null && player.isPlaying()){
//            Log.i(TAG,"player Paused...");
//            player.pause();
//        }
//    }
//
//    //停止
//    public void stop() {
//        Log.i(TAG,"player Stop...");
//        player.stop();
//        try {
//            player.prepare();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public boolean onUnbind(Intent intent) {
//        Log.i(TAG, "Service onUnbind...");
//        stop();
//        return super.onUnbind(intent);
//    }
//
//    @Override
//    public void onDestroy() {
//        Log.i(TAG,"Service onDestroy...");
//        player.stop();
//        player.release();
//    }
//
//    //自定义类实现使用绑定方式调用时获得服务接口
//    public class MyBinder extends Binder{
//        PlayerService getService(){
//            return PlayerService.this;
//        }
//    }
}
