package cn.yunchuang.im.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.yunchuang.im.ui.fragment.HomepageFragment_new;
import cn.yunchuang.im.ui.fragment.HomepageLikeFragment;
import cn.yunchuang.im.ui.fragment.HomepageNearByFragment;
import cn.yunchuang.im.ui.fragment.HomepageRateFragment;

/**
 * 直播列表类别 fragment 分页适配器
 * <p>
 * Created by mg on 2019/1/21
 */
public class WodeYuehuiLbPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList = new ArrayList<>();

    public WodeYuehuiLbPagerAdapter(FragmentManager fm) {
        super(fm);

        fragmentList.add(new HomepageNearByFragment());
        fragmentList.add(new HomepageLikeFragment());
        fragmentList.add(new HomepageRateFragment());
    }

    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "全部";
    }

}
