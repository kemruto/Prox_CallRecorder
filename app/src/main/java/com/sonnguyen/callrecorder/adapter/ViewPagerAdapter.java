package com.sonnguyen.callrecorder.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.sonnguyen.callrecorder.view.fragment.Caller.CallFragment;
import com.sonnguyen.callrecorder.view.fragment.Favourite.FavouriteFragment;
import com.sonnguyen.callrecorder.view.fragment.Home.HomeFragment;
import com.sonnguyen.callrecorder.view.fragment.IncomingCall.IncomingCallFragment;
import com.sonnguyen.callrecorder.view.fragment.OutgoingCall.OutgoingCallFragment;
import com.sonnguyen.callrecorder.view.fragment.Trimmed.TrimmedFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private String listTab[] = {"All","Caller ID","Favourite","Incoming Call","Outgoing Call","Trimmed"};
    private HomeFragment homeFragment;
    private CallFragment callFragment;
    private FavouriteFragment favouriteFragment;
    private IncomingCallFragment incoming;
    private OutgoingCallFragment outgoing;
    private TrimmedFragment trimmed;

    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        homeFragment = new HomeFragment();
        callFragment = new CallFragment();
        favouriteFragment = new FavouriteFragment();
        incoming = new IncomingCallFragment();
        outgoing = new OutgoingCallFragment();
        trimmed = new TrimmedFragment();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            return homeFragment;
        }else if(position == 1){
            return callFragment;
        }else if (position == 2){
            return favouriteFragment;
        }else if (position == 3){
            return incoming;
        }else if (position == 4){
            return outgoing;
        }else if (position == 5){
            return trimmed;
        }
        return null;
    }

    @Override
    public int getCount() {
        return listTab.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return listTab[position];
    }
}
