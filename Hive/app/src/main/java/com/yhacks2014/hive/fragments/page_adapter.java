package com.yhacks2014.hive.fragments;

import android.app.Activity;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v13.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.yhacks2014.hive.R;

import java.util.List;

public class page_adapter extends FragmentPagerAdapter{

    private final List fragments;

    public page_adapter(FragmentManager fm, List fragments){
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position){
        return (Fragment) this.fragments.get(position);
    }

    @Override
    public int getCount(){
        return this.fragments.size();
    }
}