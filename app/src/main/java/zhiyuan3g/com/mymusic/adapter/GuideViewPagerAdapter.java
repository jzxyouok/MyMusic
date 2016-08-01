package zhiyuan3g.com.mymusic.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by xuGuang on 2016/3/15.
 */
public class GuideViewPagerAdapter extends PagerAdapter {

    private List<View> views;
    private Context context;

    public GuideViewPagerAdapter(List<View> views, Context context) {
        this.views = views;
        this.context = context;
    }

    //用完之后需要销毁
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager)container).removeView(views.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager)container).addView(views.get(position));

        return views.get(position);
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }
}
