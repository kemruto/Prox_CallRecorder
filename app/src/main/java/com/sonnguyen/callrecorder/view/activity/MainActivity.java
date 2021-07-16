package com.sonnguyen.callrecorder.view.activity;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.sonnguyen.callrecorder.R;
import com.sonnguyen.callrecorder.adapter.ViewPagerAdapter;
import com.sonnguyen.callrecorder.base.BaseAct;
import com.sonnguyen.callrecorder.view.fragment.Home.HomeFragment;

public class MainActivity extends BaseAct<MainActViewModel> {
    private ViewPager viewPager;



    @Override
    protected void initViews() {        
        
        HomeFragment homeFragment = new HomeFragment();
        showFragment(R.id.frame_layout,homeFragment,false);


        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected Class<MainActViewModel> getClassViewModel() {
        return MainActViewModel.class;
    }
}