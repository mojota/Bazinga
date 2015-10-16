package com.mojota.bazinga;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class MainActivity extends ToolBarActivity implements View.OnClickListener,ThreeFragment.OnFragmentInteractionListener {

    private NavigationView mNavigation;
    private DrawerLayout mDrawerLayout;
    private TabLayout mTab;
    private ViewPager mViewPager;
    private ArrayList<String> mTabTitleList;
    private ArrayList<Fragment> mFragmentList;
    private ViewPagerAdapter mPagerAdapter;
    private RelativeLayout mLayoutMain;
    private FloatingActionButton mFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mLayoutMain = (RelativeLayout) findViewById(R.id.layout_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mFAB = (FloatingActionButton)findViewById(R.id.fab);
        mFAB.setOnClickListener(this);
        mNavigation = (NavigationView) findViewById(R.id.view_navigation);
        mNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.group_two_menu_one:
                        Snackbar.make(mLayoutMain, R.string.str_group_one, Snackbar.LENGTH_SHORT).show();
                        break;
                    case R.id.group_two_menu_two:
                        Snackbar.make(mLayoutMain, R.string.str_group_two_menu_two, Snackbar.LENGTH_LONG).setAction(R.string.cancle, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).setActionTextColor(Color.parseColor("#00BCD4")).show();
                        break;
                    default:
                        break;
                }
                menuItem.setChecked(true); // 当item在group内且checkableBehavior为all或single时有生效
                mDrawerLayout.closeDrawers();
                return false;
            }
        });
        mTab = (TabLayout) findViewById(R.id.tab);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        initList();
        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mTab.setTabsFromPagerAdapter(mPagerAdapter);
        mTab.setupWithViewPager(mViewPager);
        mTab.setTabTextColors(Color.parseColor("#335309"), Color.WHITE);
    }

    private void initList() {
        mTabTitleList = new ArrayList<String>();
        mTabTitleList.add(getString(R.string.str_tab_one));
        mTabTitleList.add(getString(R.string.str_tab_two));
        mTabTitleList.add(getString(R.string.str_tab_three));
        mFragmentList = new ArrayList<>();
        mFragmentList.add(OneFragment.newInstance("", ""));
        mFragmentList.add(TwoFragment.newInstance("", ""));
        mFragmentList.add(ThreeFragment.newInstance("", ""));
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                Snackbar.make(mLayoutMain,"fab clicked",Snackbar.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabTitleList.get(position);
        }
    }

}
