package zhiyuan3g.com.mymusic.mainfragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import zhiyuan3g.com.mymusic.R;
import zhiyuan3g.com.mymusic.dao.MusicInfoDao;

/*我的音乐   碎片窗口*/
public class MyMusicFragment extends Fragment {

    LocalCallBack callBack;     //回调接口对象
    private ImageButton btn_img_localMusic;      //本地歌曲按键

    private LinearLayout local_line_one;
    private LinearLayout line_localMusic;        //本地歌曲按键

    private TextView txt_show_number_of_songs;   //显示本地歌曲数量
    private MusicInfoDao musicInfoDao;           //数据库帮助类对象

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View lookLayout = inflater.inflate(R.layout.my_music_fragment, container, false);

        //实例化  本地歌曲按键
        btn_img_localMusic = (ImageButton) lookLayout.findViewById(R.id.btn_img_localMusic);

        txt_show_number_of_songs = (TextView) lookLayout.findViewById(R.id.txt_show_number_of_songs);
        musicInfoDao = new MusicInfoDao(getActivity());
        //显示本地歌曲的数量
        txt_show_number_of_songs.setText(String.valueOf(musicInfoDao.getAllMusicInfo().size()));

        line_localMusic = (LinearLayout) lookLayout.findViewById(R.id.line_localMusic);
        line_localMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onListenLocal("jump");
            }
        });

        local_line_one = (LinearLayout) lookLayout.findViewById(R.id.local_line_one);
        local_line_one.getBackground().setAlpha(180);

        btn_img_localMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onListenLocal("jump");
            }
        });

        return lookLayout;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callBack = (LocalCallBack) activity;
    }

    //定义一个接口  让activity继承它  可以实现这个接口中的方法
    public interface LocalCallBack{
        public void onListenLocal(String task);
    }
}
