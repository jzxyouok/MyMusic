package zhiyuan3g.com.mymusic.mainfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import zhiyuan3g.com.mymusic.R;


public class SingSongFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View newsLayout = inflater.inflate(R.layout.sing_song_fragment, container, false);
        return newsLayout;
    }

}
