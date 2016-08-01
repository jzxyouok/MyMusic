package zhiyuan3g.com.mymusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.List;

import zhiyuan3g.com.mymusic.R;
import zhiyuan3g.com.mymusic.entity.AlbumInfo;

/**
 * Created by Administrator on 2016/3/3.
 */
public class LocalSpacialFragmentAdapter extends BaseAdapter implements SectionIndexer {

    private Context context;
    private List<AlbumInfo> albumInfoList;     //存放筛选之后的信息的集合

    public LocalSpacialFragmentAdapter(List<AlbumInfo> albumInfoList, Context context) {
        this.albumInfoList = albumInfoList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return albumInfoList.size();
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
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.local_spaciao_fragment_item, null);
            viewHolder = new ViewHolder();
            viewHolder.img_show_local_spacial_icon = (ImageView) convertView.findViewById(R.id.img_show_local_spacial_icon);
            viewHolder.txt_show_local_spacial = (TextView) convertView.findViewById(R.id.txt_show_local_spacial);
            viewHolder.txt_show_local_spacial_singer_count = (TextView) convertView.findViewById(R.id.txt_show_local_spacial_singer_count);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        AlbumInfo albumInfo = albumInfoList.get(position);


        viewHolder.img_show_local_spacial_icon.setBackgroundResource(R.drawable.album_ico);
        if (albumInfo.getAlbum_name().equals("音乐")) {
            viewHolder.txt_show_local_spacial.setText("未知专辑");
        } else {
            viewHolder.txt_show_local_spacial.setText(albumInfo.getAlbum_name());
        }

        viewHolder.txt_show_local_spacial_singer_count.setText(albumInfo.getNumber_of_songs()+"首");

        return convertView;
    }


    @Override
    public Object[] getSections() {
        return null;
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = albumInfoList.get(i).getAlbum_name();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == sectionIndex) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    @Override
    public int getSectionForPosition(int position) {
        return albumInfoList.get(position).getAlbum_name().charAt(0);
    }


    private class ViewHolder {
        ImageView img_show_local_spacial_icon;
        TextView txt_show_local_spacial;
        TextView txt_show_local_spacial_singer_count;
    }
}
