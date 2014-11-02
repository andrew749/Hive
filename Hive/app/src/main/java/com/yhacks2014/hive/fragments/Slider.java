package com.yhacks2014.hive.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;


import com.yhacks2014.hive.R;

import java.util.List;
import java.util.Vector;

public class Slider extends FragmentActivity {

    private PagerAdapter mPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_slider);

        List fragments = new Vector();

        fragments.add(Fragment.instantiate(this,slide_info.class.getName()));
        fragments.add(Fragment.instantiate(this,slide_maps.class.getName()));
        fragments.add(Fragment.instantiate(this,slide_contact.class.getName()));

        this.mPagerAdapter = new page_adapter(super.getFragmentManager(),fragments);

        ViewPager pager = (ViewPager) super.findViewById(R.id.slider);

        pager.setAdapter(this.mPagerAdapter);
    }
}
