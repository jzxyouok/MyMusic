package zhiyuan3g.com.mymusic.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import zhiyuan3g.com.mymusic.R;
import zhiyuan3g.com.mymusic.adapter.LocalSingFragmentAdapter;
import zhiyuan3g.com.mymusic.dao.MusicInfoDao;
import zhiyuan3g.com.mymusic.entity.MusicInfo;
import zhiyuan3g.com.mymusic.utils.CharacterParser;
import zhiyuan3g.com.mymusic.utils.PinyinComparator;

public class SearchActivity extends AppCompatActivity {

    private ImageButton btn_img_back_search_to_local;    //返回按键
    private EditText et_show_search ;                    //收索输入框
    private TextView txt_show_search_back;               //取消
    private ListView lst_show_search_local_sing;         //歌曲列表
    private LinearLayout line_search_bg;                 //列表的父容器

    List<MusicInfo> musicList = null;
    LocalSingFragmentAdapter adapter;

    private MusicInfoDao musicInfoDao;

    //汉字转换成拼音的类
    private CharacterParser characterParser;

    //根据拼音来排序ListView 里面的数据类
    private PinyinComparator pinyinComparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        intoView();  //实例化控件

        operateView();  //操作控件
    }

    //实例化控件
    private void intoView(){
        btn_img_back_search_to_local = (ImageButton) findViewById(R.id.btn_img_back_search_to_local);
        et_show_search = (EditText) findViewById(R.id.et_show_search);
        txt_show_search_back= (TextView) findViewById(R.id.txt_show_search_back);
        lst_show_search_local_sing= (ListView) findViewById(R.id.lst_show_search_local_sing);
        line_search_bg = (LinearLayout) findViewById(R.id.line_search_bg);

        musicInfoDao = new MusicInfoDao(SearchActivity.this);

        line_search_bg.getBackground().setAlpha(70);    //设置透明度
    }

    //控件操作
    public void operateView(){
        pinyinComparator = new PinyinComparator();    //实例化拼音比较类

        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        musicList = musicInfoDao.getAllMusicInfo();

        //根据a-z 进行排序数据源
        Collections.sort(musicList, pinyinComparator);
        adapter = new LocalSingFragmentAdapter(this, musicList);
        lst_show_search_local_sing.setAdapter(adapter);

        //列表的每一项监听
        lst_show_search_local_sing.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent("PLAY_OR_PAUSE");      //发送你点击的这首歌的索引
                intent.putExtra("content","play_search");         //发送标示
                intent.putExtra("position",position);               //发送索引
                sendBroadcast(intent);                              //发送广播
            }
        });

        et_show_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    //过滤输入框中的数据
    private void filterData(String filterStr){
        List<MusicInfo> musicInfoList = new ArrayList<>();

        if (TextUtils.isEmpty(filterStr)){
            musicInfoList = musicList;
        }else {
            musicInfoList.clear();
            for (MusicInfo musicInfo:musicList){
                String singName = musicInfo.getMusic_title();
                if (singName.indexOf(filterStr.toString())!=-1||characterParser.getSelling(singName).startsWith(filterStr.toString())){
                    musicInfoList.add(musicInfo);
                }
            }
        }

        //根据a-z进行排序
        Collections.sort(musicInfoList, pinyinComparator);
        adapter.updateListView(musicInfoList);
    }
}
