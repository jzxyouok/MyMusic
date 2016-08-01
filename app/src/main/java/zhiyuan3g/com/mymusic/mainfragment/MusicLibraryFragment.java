package zhiyuan3g.com.mymusic.mainfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import zhiyuan3g.com.mymusic.R;


public class MusicLibraryFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View newLayout = inflater.inflate(R.layout.music_library_fragment, container, false);
        return newLayout;
    }

}
