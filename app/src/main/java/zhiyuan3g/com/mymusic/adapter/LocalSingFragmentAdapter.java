package zhiyuan3g.com.mymusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;



import java.util.List;

import zhiyuan3g.com.mymusic.R;
import zhiyuan3g.com.mymusic.entity.MusicInfo;

/**
 * Created by Administrator on 2016/3/3.
 * 本地歌曲的歌曲碎片
 */
public class LocalSingFragmentAdapter extends BaseAdapter implements SectionIndexer{

    private Context context;
    private List<MusicInfo> musicInfoList;

    public LocalSingFragmentAdapter(Context context, List<MusicInfo> musicInfoList) {
        this.context = context;
        this.musicInfoList = musicInfoList;
    }


    public void updateListView(List<MusicInfo> list){
        this.musicInfoList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return musicInfoList.size();
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
        if (convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.lst_show_local_sing_item,null);
            viewHolder = new ViewHolder();
            viewHolder.txt_show_local_sing_name = (TextView) convertView.findViewById(R.id.txt_show_local_sing_name);
            viewHolder.txt_show_local_sing_singerName = (TextView) convertView.findViewById(R.id.txt_show_local_sing_singerName);
            viewHolder.img_btn_local_other = (ImageButton) convertView.findViewById(R.id.img_btn_local_other);
            viewHolder.img_show_sing_state = (ImageView) convertView.findViewById(R.id.img_show_sing_state);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        MusicInfo musicInfo = musicInfoList.get(position);
        viewHolder.txt_show_local_sing_name.setText(musicInfo.getMusic_title());
        viewHolder.txt_show_local_sing_singerName.setText(musicInfo.getMusic_singer());
        viewHolder.img_btn_local_other.setBackgroundResource(R.drawable.mine_list_opt_normal);
        viewHolder.img_show_sing_state.setBackgroundResource(R.drawable.mine_local_icon);

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
            String sortStr = musicInfoList.get(i).getMusic_title();
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
        return musicInfoList.get(position).getMusic_title().charAt(0);
    }

    private class ViewHolder{
        TextView txt_show_local_sing_name;
        ImageView img_show_sing_state;
        TextView txt_show_local_sing_singerName;
        ImageButton img_btn_local_other;
    }
}
