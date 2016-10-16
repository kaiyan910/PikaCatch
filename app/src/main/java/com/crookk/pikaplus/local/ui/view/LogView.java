package com.crookk.pikaplus.local.ui.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.crookk.pikaplus.Constant;
import com.crookk.pikaplus.R;
import com.crookk.pikaplus.core.utils.FontUtils;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.res.ColorRes;
import org.androidannotations.annotations.res.DimensionPixelOffsetRes;

import java.util.Date;
import java.util.Locale;

@EViewGroup
public class LogView extends ScrollView {

    @DimensionPixelOffsetRes(R.dimen.logView_item_padding)
    int mDimenPadding;
    @DimensionPixelOffsetRes(R.dimen.logView_item_height)
    int mDimenHeight;

    @ColorRes(android.R.color.white)
    int mColorWhite;

    private LinearLayout mContainer;

    public LogView(Context context) {
        super(context);
        init();
    }

    public LogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        mContainer = new LinearLayout(getContext());

        ScrollView.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mContainer.setLayoutParams(params);
        mContainer.setOrientation(LinearLayout.VERTICAL);

        addView(mContainer);
    }

    public void add(String message) {

        TextView view = new TextView(getContext());
        view.setPadding(mDimenPadding, 0, mDimenPadding, 0);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.height = mDimenHeight;
        view.setLayoutParams(params);
        view.setSingleLine(true);
        view.setEllipsize(TextUtils.TruncateAt.END);
        view.setTypeface(FontUtils.obtainTypeface(getContext()));
        view.setTextColor(mColorWhite);

        String date = Constant.TIME_FORMAT.format(new Date());
        String text = String.format(Locale.getDefault(), "[%s] %s", date, message);

        view.setText(text);

        mContainer.addView(view);
        scroll();
    }

    @UiThread(delay = 200)
    void scroll() {

        fullScroll(ScrollView.FOCUS_DOWN);
    }

}
