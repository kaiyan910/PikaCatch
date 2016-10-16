package com.crookk.pikaplus.core.ui.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.crookk.pikaplus.Constant;
import com.crookk.pikaplus.R;
import com.crookk.pikaplus.core.ui.view.LCEView;
import com.crookk.pikaplus.core.utils.LogUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity
public abstract class ListActivity<M> extends BaseActivity implements LCEView<M>, SwipeRefreshLayout.OnRefreshListener {

    @ViewById(R.id.list)
    protected RecyclerView mViewList;
    @ViewById(R.id.empty_data)
    protected TextView mViewEmptyData;
    @ViewById(R.id.refresh_layout)
    protected SwipeRefreshLayout mViewRefreshLayout;

    protected List<M> mData = new ArrayList<>();

    @Override
    protected void afterViews() {

        super.afterViews();

        mViewRefreshLayout.setOnRefreshListener(this);
        mViewRefreshLayout.setDistanceToTriggerSync(Constant.REFRESH_DISTANCE);
        mViewRefreshLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mViewRefreshLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                if (mData.size() == 0) mViewRefreshLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void showLoading() {

        mViewList.setVisibility(View.GONE);
        mViewEmptyData.setVisibility(View.GONE);
        mViewRefreshLayout.setRefreshing(true);
    }

    @Override
    public void showEmptyData() {

        mViewList.setVisibility(View.GONE);
        mViewEmptyData.setVisibility(View.VISIBLE);
        mViewRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showData() {

        mViewList.setVisibility(View.VISIBLE);
        mViewEmptyData.setVisibility(View.GONE);
        mViewRefreshLayout.setRefreshing(false);
    }

    @UiThread
    @Override
    public void setData(List<M> data) {

        LogUtils.d(this, "setData()");
        mData = new ArrayList<>(data);

        if (mData.size() > 0) {

            showData();
            onDataDownloaded();

        } else {

            showEmptyData();
        }
    }

    protected abstract void onDataDownloaded();
}
