package zhiyuan3g.com.mymusic.localfragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
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
import zhiyuan3g.com.mymusic.utils.MediaUtil;
import zhiyuan3g.com.mymusic.utils.PinyinComparator;
import zhiyuan3g.com.mymusic.view.SideBar;

/**
 * Created by xuGuang on 2016/3/15.
 */
public class LocalSingFragment extends Fragment{

    View newLayout;                             //碎片对象

    private ImageButton img_btn_random_play;    //随机播放按键
    private ListView lst_show_local_sing;       //歌曲列表

    List<MusicInfo> musicList = null;

    private SideBar sideBar;                    //右侧的字母栏
    private TextView txt_show_aToz;             //显示当前的字母
    LocalSingFragmentAdapter adapter;

    private MusicInfoDao musicInfoDao;

    //汉字转换成拼音的类
    private CharacterParser characterParser;
    //根据拼音来排序ListView 里面的数据类
    private PinyinComparator pinyinComparator;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        newLayout = inflater.inflate(R.layout.local_sing_fragment, container, false);

        intoView();

        return newLayout;
    }

    /**
     * 实例化控件
     */
    private void intoView() {
        //实例化控件
        img_btn_random_play = (ImageButton) newLayout.findViewById(R.id.img_btn_random_play);
        lst_show_local_sing = (ListView) newLayout.findViewById(R.id.lst_show_local_sing);

        pinyinComparator = new PinyinComparator();    //实例化拼音比较类

        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        musicInfoDao = new MusicInfoDao(getActivity());

        //实例化自定义控件
        sideBar = (SideBar) newLayout.findViewById(R.id.sidebar);
        txt_show_aToz = (TextView) newLayout.findViewById(R.id.txt_show_aToz);
        sideBar.setTextView(txt_show_aToz);

        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    lst_show_local_sing.setSelection(position);
                }
            }
        });

        musicList = musicInfoDao.getAllMusicInfo();     //返回的就是标记完的

        lst_show_local_sing.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //这里是利用adapter.getItem(position) 来获取当前position所对应的对象
                MusicInfo musicInfo = musicList.get(position);
                singFragmentCallBack.getFlag(position);
            }
        });


        img_btn_random_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //随机生成一个随机数  就是在数据库中的索引  并通过回调 发给activity
                int randomNum = (int) (Math.random()*(musicInfoDao.getAllMusicInfo().size()+1-1)+1);
                singFragmentCallBack.getFlag(randomNum);
            }
        });

        //根据a-z 进行排序数据源
        adapter = new LocalSingFragmentAdapter(getActivity(), musicList);

        lst_show_local_sing.setAdapter(adapter);
    }

    LocalSingFragmentCallBack singFragmentCallBack;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        singFragmentCallBack = (LocalSingFragmentCallBack) activity;
    }

    public interface LocalSingFragmentCallBack{
        void getFlag(int position);
    }
}
