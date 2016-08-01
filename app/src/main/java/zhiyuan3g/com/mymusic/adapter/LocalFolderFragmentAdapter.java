package zhiyuan3g.com.mymusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.List;

import zhiyuan3g.com.mymusic.R;
import zhiyuan3g.com.mymusic.entity.FolderInfo;

/**
 * Created by Administrator on 2016/3/3.
 */
public class LocalFolderFragmentAdapter extends BaseAdapter {

    private Context context;
    private List<FolderInfo> folderInfoList;    //存放的是筛选之后的信息集合

    public LocalFolderFragmentAdapter(List<FolderInfo> folderInfoList, Context context) {
        this.folderInfoList = folderInfoList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return folderInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.local_holder_fragment_item, null);
            viewHolder = new ViewHolder();
            viewHolder.img_show_local_holder_icon = (ImageView) convertView.findViewById(R.id.img_show_local_holder_icon);
            viewHolder.txt_show_local_holder_fileName = (TextView) convertView.findViewById(R.id.txt_show_local_holder_fileName);
            viewHolder.txt_show_local_holder_singer_count = (TextView) convertView.findViewById(R.id.txt_show_local_holder_singer_count);
            viewHolder.txt_show_local_holder_singer_path = (TextView) convertView.findViewById(R.id.txt_show_local_holder_singer_path);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        FolderInfo folderInfo = folderInfoList.get(position);

        viewHolder.img_show_local_holder_icon.setBackgroundResource(R.drawable.local_misic_path);
        viewHolder.txt_show_local_holder_fileName.setText(folderInfo.getFolder_path());
        viewHolder.txt_show_local_holder_singer_path.setText(folderInfo.getFolder_name());
        viewHolder.txt_show_local_holder_singer_count.setText(folderInfo.getNumber_of_songs() + "首");

        return convertView;
    }


    private class ViewHolder {
        ImageView img_show_local_holder_icon;
        TextView txt_show_local_holder_fileName;
        TextView txt_show_local_holder_singer_count;
        TextView txt_show_local_holder_singer_path;
    }
}
