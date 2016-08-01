package zhiyuan3g.com.mymusic.localfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import zhiyuan3g.com.mymusic.R;
import zhiyuan3g.com.mymusic.adapter.LocalFolderFragmentAdapter;
import zhiyuan3g.com.mymusic.dao.FolderInfoDao;
import zhiyuan3g.com.mymusic.entity.FolderInfo;
import zhiyuan3g.com.mymusic.utils.MediaUtil;

/**
 * Created by xuGuang on 2016/3/15.
 */
public class LocalHolderFragment extends Fragment{

    private ListView lst_show_local_holder;
    private List<FolderInfo> folderInfoList;
    private LocalFolderFragmentAdapter adapter;
    private FolderInfoDao folderInfoDao;

    private View newLayout;     //碎片视图

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        newLayout = inflater.inflate(R.layout.local_holder_fragment, container, false);

        lst_show_local_holder = (ListView) newLayout.findViewById(R.id.lst_show_local_holder);

        folderInfoDao = new FolderInfoDao(getActivity());

        folderInfoList = folderInfoDao.getAllFolderInfo();
        adapter = new LocalFolderFragmentAdapter(folderInfoList,getActivity());
        lst_show_local_holder.setAdapter(adapter);

        return newLayout;
    }
}
