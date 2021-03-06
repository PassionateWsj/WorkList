package com.jameswong.worklist.fragment;

import android.support.design.widget.NavigationView;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.jameswong.worklist.MainActivity;
import com.jameswong.worklist.R;
import com.jameswong.worklist.database.AlarmDBSupport;
import com.jameswong.worklist.models.CalendarEvent;
import com.jameswong.worklist.pager.AboutMePager;
import com.jameswong.worklist.pager.BasePager;
import com.jameswong.worklist.pager.DayPager;
import com.jameswong.worklist.pager.HomePager;
import com.jameswong.worklist.pager.WeekPager;
import com.jameswong.worklist.utils.BusProvider;
import com.jameswong.worklist.utils.CalendarManager;
import com.jameswong.worklist.utils.Events;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ContentFragment extends BaseFragment {

    @Bind(R.id.vp_content)
    ViewPager vpContent;
    private List<BasePager> mPageList;
    private NavigationView navigationView;//菜单栏
    private DrawerLayout drawerLayout;//DrawerLayout

    private List<CalendarEvent> eventList;
    private AlarmDBSupport support;
    private HomePager homePager;
    private DayPager dayPager;
    private WeekPager weekPager;
    private AboutMePager aboutMePager;

    @Override
    public View initView() {

        View view = View.inflate(mActivity, R.layout.fragment_content, null);

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        homePager.destroy();
    }


    @Override
    public void initDate() {

        homePager = new HomePager(mActivity);
        dayPager = new DayPager(mActivity);
        weekPager = new WeekPager(mActivity);
        aboutMePager = new AboutMePager(mActivity);

        //主界面添加数据
        mPageList = new ArrayList<>();

        mPageList.add(homePager);
        mPageList.add(dayPager);
        mPageList.add(weekPager);
        mPageList.add(aboutMePager);

        vpContent.setAdapter(new VpContentAdapter());

        //获取侧边栏
        MainActivity mainUi = (MainActivity) mActivity;
        navigationView = mainUi.getNavigationView();
        navigationView.setCheckedItem(R.id.schedule);
        buildHomePager();
        drawerLayout = mainUi.getDrawerLayout();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.schedule:
                        vpContent.setCurrentItem(0, false);//设置当前的页面，取消平滑滑动
                        buildHomePager();
                        BusProvider.getInstance().send(new Events.OpenHeaderView());
                        break;
                    case R.id.day:
                        vpContent.setCurrentItem(1, false);
                        dayPager.initData();
                        BusProvider.getInstance().send(new Events.OpenHeaderView());
                        break;
                    case R.id.week:
                        vpContent.setCurrentItem(2, false);
                        weekPager.initData();
                        BusProvider.getInstance().send(new Events.OpenHeaderView());
                        break;
                    case R.id.aboutMe:
                        vpContent.setCurrentItem(3, false);
                        aboutMePager.initData();
                        BusProvider.getInstance().send(new Events.CloseHeaderView());
                        break;
                }
                item.setChecked(true);//点击了设置为选中状态
                drawerLayout.closeDrawers();
                return true;
            }
        });

    }

    /**
     * 主界面设置
     */
    private void buildHomePager() {
        homePager.initData();
        BusProvider.getInstance().toObserverable().subscribe(event -> {
            if (event instanceof Events.GoBackToDay) {
                homePager.agenda_view.getAgendaListView().scrollToCurrentDate(CalendarManager.getInstance().getToday());
            }
        });
    }


    /**
     * viewPager数据适配器
     */
    class VpContentAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mPageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager pager = mPageList.get(position);
            container.addView(pager.mRootView);
            return pager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mPageList.get(position).mRootView);
        }
    }

}
