package com.sonnguyen.callrecorder.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.sonnguyen.callrecorder.OnActionCallbackFragment;
import com.sonnguyen.callrecorder.ui.fragment.Caller.CallFragment;
import com.sonnguyen.callrecorder.ui.fragment.Favourite.FavouriteFragment;
import com.sonnguyen.callrecorder.ui.fragment.Home.HomeFragment;
import com.sonnguyen.callrecorder.ui.fragment.IncomingCall.IncomingCallFragment;
import com.sonnguyen.callrecorder.ui.fragment.OutgoingCall.OutgoingCallFragment;
import com.sonnguyen.callrecorder.ui.fragment.Trimmed.TrimmedFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter implements OnActionCallbackFragment {
    private String listTab[] = {"All","Caller ID","Favourite","Incoming Call","Outgoing Call","Trimmed"};
    private HomeFragment homeFragment;
    private CallFragment callFragment;
    private FavouriteFragment favouriteFragment;
    private IncomingCallFragment incoming;
    private OutgoingCallFragment outgoing;
    private TrimmedFragment trimmed;
    private OnActionCallbackFragment callbackFragment;

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

    @Override
    public void onCallback(String key, Object object) {

    }
}
