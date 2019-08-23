package com.kunfei.bookshelf.presenter.contract;

import android.content.SharedPreferences;

import com.kunfei.basemvplib.impl.IPresenter;
import com.kunfei.basemvplib.impl.IView;
import com.kunfei.bookshelf.bean.BookShelfBean;
import com.kunfei.bookshelf.bean.RecommendBookListBean;
import com.kunfei.bookshelf.bean.RecommendIndexBean;

import java.util.List;

public interface SimpleBookListContract {

    interface View extends IView {

        /**
         * 刷新书架书籍小说信息 更新UI
         *
         * @param recommendIndexBean 书架
         */
        void refreshAll(List<RecommendIndexBean> recommendIndexBean);

        void refreshBookList(List<RecommendBookListBean> list);

    }

    interface Presenter extends IPresenter {
        void queryAll(Boolean needRefresh);

        void queryBookList(RecommendIndexBean recommendIndexBean);
    }
}
