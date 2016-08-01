package zhiyuan3g.com.mymusic.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import zhiyuan3g.com.mymusic.activity.LocalMusicActivity;
import zhiyuan3g.com.mymusic.localfragment.LocalHolderFragment;
import zhiyuan3g.com.mymusic.localfragment.LocalSingFragment;
import zhiyuan3g.com.mymusic.localfragment.LocalSingerFragment;
import zhiyuan3g.com.mymusic.localfragment.LocalSpecialFragment;


/**
 * 本地歌曲Fragment适配器
 */
public class LocalFragmentPagerAdapter extends FragmentPagerAdapter {

    private final int PAGER_COUNT =4;

    private LocalSingFragment localSingFragment;
    private LocalSingerFragment localSingerFragment;
    private LocalSpecialFragment localSpecialFragment;
    private LocalHolderFragment localHolderFragment;


    public LocalFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        localSingFragment = new LocalSingFragment();
        localSingerFragment = new LocalSingerFragment();
        localHolderFragment = new LocalHolderFragment();
        localSpecialFragment = new LocalSpecialFragment();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;   // 当前的Fragment对象

        switch (position){
            case LocalMusicActivity.LOCAL_SING:
                fragment = localSingFragment;
                break;
            case LocalMusicActivity.LOCAL_SINGER:
                fragment = localSingerFragment;
                break;
            case LocalMusicActivity.LOCAL_SPACIAL:
                fragment = localSpecialFragment;
                break;
            case LocalMusicActivity.LOCAL_FOLDER:
                fragment = localHolderFragment;
                break;
        }


        return fragment;
    }

    @Override
    public int getCount() {
        return PAGER_COUNT;   //返回的是页面的数量
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
