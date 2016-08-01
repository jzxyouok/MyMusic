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
import zhiyuan3g.com.mymusic.adapter.LocalSpacialFragmentAdapter;
import zhiyuan3g.com.mymusic.dao.AlbumInfoDao;
import zhiyuan3g.com.mymusic.entity.AlbumInfo;
import zhiyuan3g.com.mymusic.utils.AlbumInfoPinyinComparator;
import zhiyuan3g.com.mymusic.utils.CharacterParser;
import zhiyuan3g.com.mymusic.utils.PinyinComparator;
import zhiyuan3g.com.mymusic.view.SideBar;

/**
 * Created by xuGuang on 2016/3/15.
 */
public class LocalSpecialFragment extends Fragment{

    private ListView lst_show_local_spacial;      //列表对象
    private List<AlbumInfo> albumInfoList;        //集合
    private LocalSpacialFragmentAdapter adapter;  //适配器对象
    private TextView txt_show_local_spacial_aToz; //显示当前字母
    private SideBar local_spacial_sidebar;        //自定义控件对象
    private AlbumInfoDao albumInfoDao;

    //汉字转换成拼音的类
    private CharacterParser characterParser;

    //根据拼音来排序ListView 里面的数据类
    private AlbumInfoPinyinComparator pinyinComparator;

    private View newLayout;     //碎片视图

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        newLayout = inflater.inflate(R.layout.local_special_fragment, container, false);

        intoView();

        return newLayout;
    }

    private void intoView(){
        lst_show_local_spacial = (ListView) newLayout.findViewById(R.id.lst_show_local_spacial);

        pinyinComparator = new AlbumInfoPinyinComparator();    //实例化拼音比较类

        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        albumInfoDao = new AlbumInfoDao(getActivity());

        //自定义控件实例化
        local_spacial_sidebar = (SideBar) newLayout.findViewById(R.id.local_spacial_sidebar);
        txt_show_local_spacial_aToz = (TextView) newLayout.findViewById(R.id.txt_show_local_spacial_aToz);
        local_spacial_sidebar.setTextView(txt_show_local_spacial_aToz);

        local_spacial_sidebar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    lst_show_local_spacial.setSelection(position);
                }
            }
        });

        lst_show_local_spacial.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //这里是利用adapter.getItem(position) 来获取当前position所对应的对象
            }
        });

        albumInfoList = albumInfoDao.getAllAlbumInfo();
        adapter = new LocalSpacialFragmentAdapter(albumInfoList,getActivity());
        lst_show_local_spacial.setAdapter(adapter);
    }


}
