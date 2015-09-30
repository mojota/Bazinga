package com.mojota.bazinga;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends ToolBarActivity {

    private NavigationView mNavigation;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigation = (NavigationView) findViewById(R.id.view_navigation);
        mNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    default:
                        break;
                }
                menuItem.setChecked(true); // 当item在group内且checkableBehavior为all或single时有生效
                mDrawerLayout.closeDrawers();
                return false;
            }
        });
    }

    public void onVolleyClick(View view) {
        Intent intent = new Intent(MainActivity.this, VolleyActivity.class);
        startActivity(intent);
    }
}
