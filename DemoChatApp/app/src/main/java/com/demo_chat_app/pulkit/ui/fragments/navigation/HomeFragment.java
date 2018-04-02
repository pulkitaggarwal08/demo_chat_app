package com.demo_chat_app.pulkit.ui.fragments.navigation;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo_chat_app.pulkit.R;
import com.demo_chat_app.pulkit.adapter.ViewPagerAdapter;
import com.demo_chat_app.pulkit.ui.fragments.tabs.AboutUsFragment;
import com.demo_chat_app.pulkit.ui.fragments.tabs.ChatFragment;
import com.demo_chat_app.pulkit.ui.fragments.tabs.GroupChatFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pulkit on 18/2/18.
 */

public class HomeFragment extends Fragment {

    //    private View view;
    private String tabValue = null;

    @BindView(R.id.home_tab_layout)
    public TabLayout homeTabLayout = null;
    @BindView(R.id.home_tab_viewpager)
    public ViewPager homeTabViewPager;

    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, view);

        tabValue = getArguments().getString("home_tab");

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /*For Simple Name Tab*/
//        initSimpleNameTabs();

        /*For Icon Tab*/
        initIconTabs();
    }

    private void initSimpleNameTabs() {

        setupNameViewPager(homeTabViewPager);
        if (homeTabViewPager != null) {
            homeTabViewPager.clearOnPageChangeListeners();
            setupNameViewPager(homeTabViewPager);
        }

        homeTabLayout.setupWithViewPager(homeTabViewPager);

        if (tabValue != null) {
            if (tabValue.equalsIgnoreCase("2")) {
                homeTabViewPager.setCurrentItem(1);
            }
        }
//        if (homeTabViewPager.getCurrentItem() == 1) {
//            header_name.setText("Settings");
//        }

    }

    private void setupNameViewPager(ViewPager homeTabViewPager) {

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        viewPagerAdapter.addFragment(new GroupChatFragment(), "Group Chat");
        viewPagerAdapter.addFragment(new ChatFragment(), "Chat");
        viewPagerAdapter.addFragment(new AboutUsFragment(), "About");
        homeTabViewPager.setAdapter(viewPagerAdapter);

    }

    private void initIconTabs() {

        homeTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorAccent));

        setupIconViewPager(homeTabViewPager);
        if (homeTabViewPager != null) {
            homeTabViewPager.clearOnPageChangeListeners();
            setupIconViewPager(homeTabViewPager);
        }

        homeTabLayout.setupWithViewPager(homeTabViewPager);

        if (tabValue != null) {
            if (tabValue.equalsIgnoreCase("2")) {
                homeTabViewPager.setCurrentItem(1);
            }
        }

        setupTabIcons();
    }

    private void setupTabIcons() {
        int[] tabIcons = {
                R.drawable.ic_group_purple,
                R.drawable.ic_person_purple,
                R.drawable.ic_aboutus_purple
        };

        homeTabLayout.getTabAt(0).setIcon(tabIcons[0]);
        homeTabLayout.getTabAt(1).setIcon(tabIcons[1]);
        homeTabLayout.getTabAt(2).setIcon(tabIcons[2]);

    }

    private void setupIconViewPager(ViewPager homeTabViewPager) {

        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        viewPagerAdapter.addFragment(new GroupChatFragment(), "");
        viewPagerAdapter.addFragment(new ChatFragment(), "");
        viewPagerAdapter.addFragment(new AboutUsFragment(), "");
        homeTabViewPager.setAdapter(viewPagerAdapter);

    }

}
