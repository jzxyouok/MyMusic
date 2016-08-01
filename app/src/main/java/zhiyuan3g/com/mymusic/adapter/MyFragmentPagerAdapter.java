package zhiyuan3g.com.mymusic.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import zhiyuan3g.com.mymusic.activity.MainActivity;
import zhiyuan3g.com.mymusic.mainfragment.MyMusicFragment;
import zhiyuan3g.com.mymusic.mainfragment.MusicLibraryFragment;
import zhiyuan3g.com.mymusic.mainfragment.SingSongFragment;
import zhiyuan3g.com.mymusic.mainfragment.VideoFragment;


/**
 * Created by GuanHui on 2016/01/30.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private final int PAGER_COUNT = 1;     // TODO: 2016/3/17    4个页面改成两个页面

    private MyMusicFragment myFragment1;
//    private MusicLibraryFragment myFragment2;
//    private SingSongFragment myFragment3;     // TODO: 2016/3/17   四个fragment改为两个fragment
//    private VideoFragment myFragment4;

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        myFragment1 = new MyMusicFragment();
//        myFragment2 = new MusicLibraryFragment();
//        myFragment3 = new SingSongFragment();         // TODO: 2016/3/17   四个fragment改为两个fragment
//        myFragment4 = new VideoFragment();


    }

    @Override
    public Fragment getItem(int i) {

        Fragment fragment = null;

        switch (i) {

            case MainActivity.PAGER_ONE:
                fragment = myFragment1;
                break;
//            case MainActivity.PAGER_TWO:
//                fragment = myFragment2;
//                break;
//            case MainActivity.PAGER_THREE:        // TODO: 2016/3/17   四个fragment改为两个fragment
//                fragment = myFragment3;
//                break;
//            case MainActivity.PAGER_FOUR:
//                fragment = myFragment4;
//                break;

        }

        return fragment;

    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
