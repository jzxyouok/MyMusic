package zhiyuan3g.com.mymusic.localfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import zhiyuan3g.com.mymusic.R;
import zhiyuan3g.com.mymusic.adapter.LocalSingerFragmentAdapter;
import zhiyuan3g.com.mymusic.dao.ArtistInfoDao;
import zhiyuan3g.com.mymusic.entity.ArtistInfo;
import zhiyuan3g.com.mymusic.entity.MusicInfo;
import zhiyuan3g.com.mymusic.utils.ArtistInfoPinyinComparator;
import zhiyuan3g.com.mymusic.utils.CharacterParser;
import zhiyuan3g.com.mymusic.utils.PinyinComparator;
import zhiyuan3g.com.mymusic.view.SideBar;

/**
 * Created by xuGuang on 2016/3/15.
 */
public class LocalSingerFragment extends Fragment{

    private ListView lst_show_local_singer;       //ListView 对象
    private List<ArtistInfo> artistInfoList;
    private LocalSingerFragmentAdapter adapter;   //适配器对象
    private TextView txt_show_local_singer_aToz;  //显示当前的字母
    private SideBar local_singer_sidebar;         //右侧查询栏对象

    //汉字转换成拼音的类
    private CharacterParser characterParser;

    //根据拼音来排序ListView 里面的数据类
    private ArtistInfoPinyinComparator pinyinComparator;

    private ArtistInfoDao artistInfoDao;

    private View newLayout;     //碎片视图

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        newLayout = inflater.inflate(R.layout.local_singer_fragment, container, false);

        intoView();

        return newLayout;
    }

    private void intoView(){
        lst_show_local_singer = (ListView) newLayout.findViewById(R.id.lst_show_local_singer);

        //实例化拼音比较类
        pinyinComparator = new ArtistInfoPinyinComparator();

        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        artistInfoDao = new ArtistInfoDao(getActivity());   //实例化数据库帮助类

        //实例化自定义控件
        local_singer_sidebar = (SideBar) newLayout.findViewById(R.id.local_singer_sidebar);
        txt_show_local_singer_aToz = (TextView) newLayout.findViewById(R.id.txt_show_local_singer_aToz);
        local_singer_sidebar.setTextView(txt_show_local_singer_aToz);

        local_singer_sidebar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    lst_show_local_singer.setSelection(position);
                }
            }
        });

        lst_show_local_singer = (ListView) newLayout.findViewById(R.id.lst_show_local_singer);
        lst_show_local_singer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //这里是利用adapter.getItem(position) 来获取当前position所对应的对象
            }
        });

        artistInfoList = artistInfoDao.getAllArtistInfo();



        adapter = new LocalSingerFragmentAdapter(getActivity(), artistInfoList);
        lst_show_local_singer.setAdapter(adapter);

    }


}
