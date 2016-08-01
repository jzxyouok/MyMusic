package zhiyuan3g.com.mymusic.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.widget.ImageView;

import zhiyuan3g.com.mymusic.R;

public class FlashActivity extends AppCompatActivity {


    private static final int TIME = 1500;
    private static final int GO_HOME = 1000;
    private static final int GO_GUIDE = 1001;
    private boolean isFirstIn;

    private SharedPreferences sp;
    private Editor editor;

    private ImageView img_show_flash;
    private Animation animation;
    private boolean isOK;      //代表动画结束了


    private Handler mHandle = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GO_HOME:
                    goHome();
                    break;
                case GO_GUIDE:
                    goGuide();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);

        init();
    }

    //判断是否是第一次进入
    private void init(){
        sp = getSharedPreferences("isFirst", MODE_PRIVATE);
        isFirstIn = sp.getBoolean("isFirst",true);
        if (!isFirstIn){
            mHandle.sendEmptyMessageDelayed(GO_HOME,TIME);
        }else {
            mHandle.sendEmptyMessageDelayed(GO_GUIDE,TIME);
            editor = sp.edit();
            editor.putBoolean("isFirst",false);   //第一次之后改为false
            editor.commit();
        }
    }

    //跳到主页面
    private void goHome(){
        Intent intent = new Intent(FlashActivity.this,BelowMainActivity.class);
        startActivity(intent);
        finish();
    }

    //跳到引导页面
    private void goGuide(){
        Intent intent = new Intent(FlashActivity.this,GuideActivity.class);
        startActivity(intent);
        finish();
    }
}
