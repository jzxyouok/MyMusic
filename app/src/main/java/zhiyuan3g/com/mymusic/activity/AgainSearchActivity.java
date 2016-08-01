package zhiyuan3g.com.mymusic.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import zhiyuan3g.com.mymusic.R;
import zhiyuan3g.com.mymusic.searchmusicfragment.LeftFragment;
import zhiyuan3g.com.mymusic.searchmusicfragment.MenuScanFragment;
import zhiyuan3g.com.mymusic.searchmusicfragment.RightFragment;

public class AgainSearchActivity extends FragmentActivity {

    public ViewPager viewPager_again;            //viewPager对象   可以滑动退出
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();       //存放Fragment的集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_again_search);

        //实例化viewPager
        viewPager_again = (ViewPager) findViewById(R.id.viewPager_again);

        initViewPager();
    }

    //实例化fragment
    private void initViewPager() {
        Fragment leftFragment = new LeftFragment();          //首先  实例化两个碎片 里面的布局是透明的
        Fragment rightFragment = new RightFragment();
        Fragment menuFragment = new MenuScanFragment();      //再实例化这个操作碎片 都添加到List中

        mFragmentList.add(leftFragment);
        mFragmentList.add(menuFragment);
        mFragmentList.add(rightFragment);

        //设置适配器
        viewPager_again.setAdapter(new MyPagerAdapter(getSupportFragmentManager(),
                mFragmentList));
        //设置viewPager的页面变化监听事件
        viewPager_again.setOnPageChangeListener(new MyOnPageChangeListener());
        viewPager_again.setCurrentItem(1, true);        //默认显示第二个碎片 就是你要进行操作的碎片
    }

    //自定义一个viewPager适配器
    private class MyPagerAdapter extends FragmentPagerAdapter{

        //用来装fragment的集合
        List<Fragment> fragmentList;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public MyPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragmentList = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

    //页面改变监听事件
    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        int onPageScrolled = -1;

        // 当当前页面被滑动时调用
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            onPageScrolled = position;
        }

        // 当新的页面被选中时调用
        @Override
        public void onPageSelected(int position) {

        }

        // state ==1的时辰默示正在滑动，state==2的时辰默示滑动完毕了，state==0的时辰默示什么都没做
        // 当滑动状态改变时调用
        @Override
        public void onPageScrollStateChanged(int state) {
            if ((onPageScrolled == 0 || onPageScrolled == 2) && state == 0) {
                setResult(1);
                finish();
            }
        }
    }

    //按下返回按键的操作
    @Override
    public void onBackPressed() {
        if(viewPager_again.isShown()) {
            viewPager_again.setCurrentItem(0, true);     //按下返回键就显示第一个viewPager界面  也就是透明的界面
        }
    }
}
