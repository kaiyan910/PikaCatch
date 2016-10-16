package com.crookk.pikaplus.core.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.crookk.pikaplus.R;
import com.crookk.pikaplus.core.bean.OttoBus;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity
public abstract class BaseActivity extends AppCompatActivity {

    @ViewById(R.id.toolbar)
    protected Toolbar mViewToolbar;

    @Bean
    protected OttoBus mOttoBus;

    @AfterViews
    protected void afterViews() {

    }

    @Override
    public void onResume() {
        super.onResume();
        mOttoBus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mOttoBus.unregister(this);
    }

    protected void setupBackNavigation() {

        if (mViewToolbar != null) {
            mViewToolbar.setTitle(getActivityTitle());
            setSupportActionBar(mViewToolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                mViewToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
            }
        }
    }

    protected abstract String getActivityTitle();
}
