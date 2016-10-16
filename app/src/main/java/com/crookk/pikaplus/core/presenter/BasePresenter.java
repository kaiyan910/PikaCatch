package com.crookk.pikaplus.core.presenter;

import com.crookk.pikaplus.core.ui.view.BaseView;

public interface BasePresenter<V extends BaseView> {
    void attach(V view);
}
