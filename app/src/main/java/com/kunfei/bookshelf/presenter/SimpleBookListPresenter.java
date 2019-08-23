package com.kunfei.bookshelf.presenter;

import android.annotation.SuppressLint;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.documentfile.provider.DocumentFile;

import com.google.android.material.snackbar.Snackbar;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.kunfei.basemvplib.BasePresenterImpl;
import com.kunfei.basemvplib.impl.IView;
import com.kunfei.bookshelf.DbHelper;
import com.kunfei.bookshelf.base.observer.MyObserver;
import com.kunfei.bookshelf.bean.RecommendBookListBean;
import com.kunfei.bookshelf.bean.RecommendIndexBean;
import com.kunfei.bookshelf.bean.TxtChapterRuleBean;
import com.kunfei.bookshelf.constant.RxBusTag;
import com.kunfei.bookshelf.help.DocumentHelper;
import com.kunfei.bookshelf.model.BookListManager;
import com.kunfei.bookshelf.model.ReplaceRuleManager;
import com.kunfei.bookshelf.model.TxtChapterRuleManager;
import com.kunfei.bookshelf.presenter.contract.SimpleBookListContract;
import com.kunfei.bookshelf.presenter.contract.TxtChapterRuleContract;
import com.kunfei.bookshelf.utils.NetworkUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.text.TextUtils.isEmpty;

public class SimpleBookListPresenter extends BasePresenterImpl<SimpleBookListContract.View> implements SimpleBookListContract.Presenter {

    @Override
    public void detachView() {
        RxBus.get().unregister(this);
    }

    @Override
    public void queryAll(Boolean needRefresh){

        Observable.create((ObservableOnSubscribe<List<RecommendIndexBean>>) e -> {
            List<RecommendIndexBean> bookShelfList;
            bookShelfList = BookListManager.getAll();

            e.onNext(bookShelfList == null ? new ArrayList<>() : bookShelfList);
            e.onComplete();
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<List<RecommendIndexBean>>() {
                    @Override
                    public void onNext(List<RecommendIndexBean> value) {
                        if (null != value) {
                            mView.refreshAll(value);

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        //mView.refreshError(NetworkUtils.getErrorTip(NetworkUtils.ERROR_CODE_ANALY));
                    }
                });

    }


    @Override
    public void queryBookList(RecommendIndexBean recommendIndexBean){

        Observable<List<RecommendIndexBean>> observable = BookListManager.queryBookList(recommendIndexBean);
        if (observable != null) {
            observable.subscribe(getImportObserver());
        }

    }

    private MyObserver<List<RecommendIndexBean>> getImportObserver() {
        return new MyObserver<List<RecommendIndexBean>>() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onNext(List<RecommendIndexBean> bookSourceBeans) {
                if (bookSourceBeans.size() > 0) {
                    mView.refreshBookList(bookSourceBeans.get(0).getList());
                    // mView.showSnackBar(String.format("导入成功%d个书源", bookSourceBeans.size()), Snackbar.LENGTH_SHORT);
                    // mView.setResult(RESULT_OK);
                } else {
                    //mView.showSnackBar("格式不对", Snackbar.LENGTH_SHORT);
                }
            }

            @Override
            public void onError(Throwable e) {
                //mView.showSnackBar(e.getMessage(), Snackbar.LENGTH_SHORT);
            }
        };
    }

    @Override
    public void attachView(@NonNull IView iView) {
        super.attachView(iView);
        RxBus.get().register(this);
    }

    /////////////////////////////////////////////////


    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(RxBusTag.UPDATE_BOOK_LIST)})
    public void updateRead(Boolean recreate) {
        queryAll(recreate);
    }
}
