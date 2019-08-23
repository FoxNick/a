package com.kunfei.bookshelf.presenter.contract;

import com.kunfei.basemvplib.impl.IPresenter;
import com.kunfei.basemvplib.impl.IView;
import com.kunfei.bookshelf.bean.BookSourceBean;
import com.kunfei.bookshelf.bean.FindKindBean;
import com.kunfei.bookshelf.bean.FindKindGroupBean;
import com.kunfei.bookshelf.bean.MyFindKindGroupBean;
import com.kunfei.bookshelf.bean.SearchBookBean;
import com.kunfei.bookshelf.widget.recycler.expandable.bean.RecyclerViewData;

import java.util.List;

public interface MyFindBookContract {
    interface Presenter extends IPresenter {

        void initData();

        void getSecondFind(MyFindKindGroupBean findKindGroupBean);


        //
        void initPage();

        void initKindPage(String url, String tag);

        void toKindSearch();



    }

    interface View extends IView {

        /**
         * 更新UI
         */
        void updateUI(List<MyFindKindGroupBean> group);


        void ShowSecond(List<FindKindBean> list,String GroupName);


        //发现相关

        void refreshKindBook(List<SearchBookBean> value);

        /**
         * 刷新成功
         */
        void refreshKindFinish(Boolean isAll);

        /**
         * 加载更多书籍成功 更新UI
         */
        void loadMoreKindBook(List<SearchBookBean> books);

        /**
         * 加载成功
         */
        void loadMoreKindFinish(Boolean isAll);
    }
}
