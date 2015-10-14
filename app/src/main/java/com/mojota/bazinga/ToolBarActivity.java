package com.mojota.bazinga;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by mojota on 15-9-25.
 */
public class ToolBarActivity extends AppCompatActivity {
    private Toolbar mToolBar;
    private TextView mCenterTitle;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.view_header);
        LinearLayout parentView = (LinearLayout) findViewById(R.id.layout_root);
        View view = getLayoutInflater().inflate(layoutResID, parentView, false);
        parentView.addView(view);
        initToolBar();
    }

    private void initToolBar() {
        mToolBar = (Toolbar) findViewById(R.id.toolbar_header);
        mCenterTitle = (TextView) findViewById(R.id.toolbar_title);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public Toolbar getToolBar() {
        return mToolBar;
    }

    public void setCenterTitle(int resId) {
        mCenterTitle.setText(resId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
