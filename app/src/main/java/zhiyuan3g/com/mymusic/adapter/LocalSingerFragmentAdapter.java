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
import zhiyuan3g.com.mymusic.dao.ArtistInfoDao;
import zhiyuan3g.com.mymusic.entity.ArtistInfo;
import zhiyuan3g.com.mymusic.entity.MusicInfo;

/**
 * Created by Administrator on 2016/3/3.
 */
public class LocalSingerFragmentAdapter extends BaseAdapter implements SectionIndexer{

    private Context context;
    private List<ArtistInfo> artistInfoList;    //存放筛选之后的信息的集合


    public LocalSingerFragmentAdapter(Context context, List<ArtistInfo> artistInfoList) {
        this.context = context;
        this.artistInfoList = artistInfoList;
    }

    @Override
    public int getCount() {
        return artistInfoList.size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.local_singer_fragment_item, null);
            viewHolder = new ViewHolder();
            viewHolder.img_show_local_singer_icon = (ImageView) convertView.findViewById(R.id.img_show_local_singer_icon);
            viewHolder.txt_show_local_singer_singer = (TextView) convertView.findViewById(R.id.txt_show_local_singer_singer);
            viewHolder.txt_show_local_singer_singer_count = (TextView) convertView.findViewById(R.id.txt_show_local_singer_singer_count);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ArtistInfo artistInfo = artistInfoList.get(position);

        viewHolder.img_show_local_singer_icon.setBackgroundResource(R.drawable.person_icn);
        viewHolder.txt_show_local_singer_singer.setText(artistInfo.getSingerName());
        viewHolder.txt_show_local_singer_singer_count.setText(artistInfo.getSingerCount() + "首");

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
            String sortStr = artistInfoList.get(i).getSingerName();
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
        return artistInfoList.get(position).getSingerName().charAt(0);
    }


    private class ViewHolder {
        ImageView img_show_local_singer_icon;
        TextView txt_show_local_singer_singer;
        TextView txt_show_local_singer_singer_count;
    }
}
