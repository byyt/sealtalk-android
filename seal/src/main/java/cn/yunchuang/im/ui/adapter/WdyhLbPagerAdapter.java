package cn.yunchuang.im.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.yunchuang.im.ui.fragment.WdyhLbBaseFragment;

/**
 * 直播列表类别 fragment 分页适配器
 * <p>
 * Created by mg on 2019/1/21
 */
public class WdyhLbPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList = new ArrayList<>();

    public WdyhLbPagerAdapter(FragmentManager fm) {
        super(fm);

        fragmentList.add(new WdyhLbBaseFragment());
        fragmentList.add(new WdyhLbBaseFragment());
        fragmentList.add(new WdyhLbBaseFragment());
        fragmentList.add(new WdyhLbBaseFragment());
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
        if (position == 0) {
            return "全部";
        } else if (position == 1) {
            return "进行中";
        } else if (position == 2) {
            return "待评价";
        } else {
            return "已结束";
        }
    }

}
