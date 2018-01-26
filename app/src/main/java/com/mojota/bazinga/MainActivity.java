package com.mojota.bazinga;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        ThreeFragment.OnFragmentInteractionListener {

    private NavigationView mNavigation;
    private DrawerLayout mDrawerLayout;
    private TabLayout mTab;
    private ViewPager mViewPager;
    private ArrayList<String> mTabTitleList;
    private ArrayList<Fragment> mFragmentList;
    private ViewPagerAdapter mPagerAdapter;
    private ViewGroup mLayoutMain;
    private FloatingActionButton mFAB;
    private Toolbar mToolBar;
    private TextView mCenterTitle;
    private CollapsingToolbarLayout mLayoutCollaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolBar = (Toolbar) findViewById(R.id.toolbar_header);
        mCenterTitle = (TextView) findViewById(R.id.toolbar_title);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        PackageManager pkgManager = getPackageManager();
        // 读写 sd card 权限非常重要, android6.0默认禁止的, 建议初始化之前就弹窗让用户赋予该权限
        boolean sdCardWritePermission = pkgManager.checkPermission(android.Manifest.permission
                .WRITE_EXTERNAL_STORAGE, getPackageName()) == PackageManager.PERMISSION_GRANTED;

        // read phone state用于获取 imei 设备信息
        boolean phoneSatePermission = pkgManager.checkPermission(android.Manifest.permission
                .READ_PHONE_STATE, getPackageName()) == PackageManager.PERMISSION_GRANTED;
        if (Build.VERSION.SDK_INT >= 23 && !sdCardWritePermission || !phoneSatePermission) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission
                    .WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_PHONE_STATE}, 0);
        }

        mLayoutMain = (ViewGroup) findViewById(R.id.layout_main);

//        mLayoutCollaps = (CollapsingToolbarLayout) findViewById(R.id.layout_collapsing);
//        mLayoutCollaps.setTitle(getString(R.string.app_name));
//        mLayoutCollaps.setCollapsedTitleTextColor(Color.WHITE);
//        mLayoutCollaps.setExpandedTitleColor(Color.YELLOW);
//        mLayoutCollaps.setContentScrimColor(Color.BLACK);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mFAB = (FloatingActionButton) findViewById(R.id.fab);
        mFAB.setOnClickListener(this);
        mNavigation = (NavigationView) findViewById(R.id.view_navigation);
        mNavigation.setNavigationItemSelectedListener(new NavigationView
                .OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_group_one:
                        startActivity(new Intent(MainActivity.this, MyWebviewActivity.class));
                        break;
                    case R.id.group_two_menu_one:
                        Snackbar.make(mLayoutMain, R.string.str_group_one, Snackbar.LENGTH_SHORT)
                                .show();
                        break;
                    case R.id.group_two_menu_two:
                        Snackbar.make(mLayoutMain, R.string.str_group_two_menu_two, Snackbar
                                .LENGTH_LONG).setAction(R.string.cancle, new View.OnClickListener
                                () {
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
    }

    private void initList() {
        mTabTitleList = new ArrayList<String>();
        mTabTitleList.add(getString(R.string.str_tab_one));
        mTabTitleList.add(getString(R.string.str_tab_two));
        mTabTitleList.add(getString(R.string.str_tab_three));
        mTabTitleList.add(getString(R.string.str_tab_four));
        mTabTitleList.add(getString(R.string.str_tab_five));
        mFragmentList = new ArrayList<>();
        mFragmentList.add(OneFragment.newInstance("", ""));
        mFragmentList.add(TwoFragment.newInstance("", ""));
        mFragmentList.add(ThreeFragment.newInstance("", ""));
        mFragmentList.add(FourFragment.newInstance("", ""));
        mFragmentList.add(FiveFragment.newInstance("", ""));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                Snackbar.make(mLayoutMain, "fab clicked", Snackbar.LENGTH_SHORT).show();
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
