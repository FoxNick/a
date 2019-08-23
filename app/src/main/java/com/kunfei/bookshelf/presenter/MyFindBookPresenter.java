//Copyright (c) 2017. 章钦豪. All rights reserved.
package com.kunfei.bookshelf.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.kunfei.basemvplib.BasePresenterImpl;
import com.kunfei.basemvplib.impl.IView;
import com.kunfei.bookshelf.DbHelper;
import com.kunfei.bookshelf.base.observer.MyObserver;
import com.kunfei.bookshelf.base.observer.MySingleObserver;
import com.kunfei.bookshelf.bean.BookShelfBean;
import com.kunfei.bookshelf.bean.FindKindGroupBean;
import com.kunfei.bookshelf.bean.MyFindKindGroupBean;
import com.kunfei.bookshelf.bean.SearchBookBean;
import com.kunfei.bookshelf.constant.RxBusTag;
import com.kunfei.bookshelf.help.BookshelfHelp;
import com.kunfei.bookshelf.model.SearchBookModel;
import com.kunfei.bookshelf.model.WebBookModel;
import com.kunfei.bookshelf.model.analyzeRule.AnalyzeRule;
import com.kunfei.bookshelf.presenter.contract.MyFindBookContract;
import com.kunfei.bookshelf.utils.ACache;
import com.kunfei.bookshelf.utils.RxUtils;
import com.kunfei.bookshelf.MApplication;
import com.kunfei.bookshelf.bean.BookSourceBean;
import com.kunfei.bookshelf.bean.FindKindBean;
import com.kunfei.bookshelf.bean.FindKindGroupBean;
import com.kunfei.bookshelf.model.BookSourceManager;
import com.kunfei.bookshelf.utils.RxUtils;
import java.util.ArrayList;
import java.util.List;

import javax.script.SimpleBindings;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.kunfei.bookshelf.constant.AppConstant.SCRIPT_ENGINE;

public class MyFindBookPresenter extends BasePresenterImpl<MyFindBookContract.View> implements MyFindBookContract.Presenter {
    private Disposable disposable;
    private AnalyzeRule analyzeRule;


    //
    private SearchBookModel searchBookModel;
    private List<BookShelfBean> bookShelfS = new ArrayList<>();   //用来比对搜索的书籍是否已经添加进书架
    private int page = 1;

    private String url = "";

    private  String tag = "";

    private Boolean kindHasMore = true;

    private long startThisSearchTime;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    public MyFindBookPresenter() {
        Observable.create((ObservableOnSubscribe<List<BookShelfBean>>) e -> {
            List<BookShelfBean> booAll = BookshelfHelp.getAllBook();
            e.onNext(booAll == null ? new ArrayList<>() : booAll);
            e.onComplete();
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<List<BookShelfBean>>() {
                    @Override
                    public void onNext(List<BookShelfBean> value) {
                        bookShelfS.addAll(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });

        //搜索监听
        SearchBookModel.OnSearchListener onSearchListener = new SearchBookModel.OnSearchListener() {
            @Override
            public void refreshSearchBook() {
                //mView.refreshSearchBook();
            }

            @Override
            public void refreshFinish(Boolean value) {
                //mView.refreshFinish(value);
            }

            @Override
            public void loadMoreFinish(Boolean value) {

                //mView.loadMoreFinish(value);
            }


            @Override
            public void loadMoreSearchBook(List<SearchBookBean> value) {
               // mView.loadMoreSearchBook(value);
            }

            @Override
            public void searchBookError(Throwable throwable) {

                //mView.searchBookError(throwable);
            }

            @Override
            public int getItemCount() {
                return 100;
                //return mView.getSearchBookAdapter().getItemCount();
            }
        };
        //搜索引擎初始化
        searchBookModel = new SearchBookModel(onSearchListener);
    }

    @Override
    public void initPage() {
        searchBookModel.setPage(0);
    }

    @Override
    public void initKindPage(String url, String tag) {
        this.page = 1;
        this.url = url;
        this.tag = tag;
        this.startThisSearchTime = System.currentTimeMillis();
    }

    @Override
    public void toKindSearch(){

        kindHasMore = true;
        Observable.create((ObservableOnSubscribe<List<BookShelfBean>>) e -> {
            List<BookShelfBean> temp = DbHelper.getDaoSession().getBookShelfBeanDao().queryBuilder().list();
            if (temp == null)
                temp = new ArrayList<>();
            e.onNext(temp);
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<List<BookShelfBean>>() {
                    @Override
                    public void onNext(List<BookShelfBean> value) {
                        bookShelfS.addAll(value);
                        final long tempTime = startThisSearchTime;
                        kindSearch(url,tag,tempTime);
                        // mView.startRefreshAnim();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }
                });
    }


    public  void  kindSearch(String url, String tag,long searchTime) {

        if(kindHasMore) {

            WebBookModel.getInstance().findBook(url, page, tag)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new MyObserver<List<SearchBookBean>>() {
                        @Override
                        public void onNext(List<SearchBookBean> value) {
                            if (searchTime == startThisSearchTime) {

                                for (SearchBookBean temp : value) {
                                    for (BookShelfBean bookShelfBean : bookShelfS) {
                                        if (temp.getNoteUrl().equals(bookShelfBean.getNoteUrl())) {
                                            temp.setIsCurrentSource(true);
                                            break;
                                        }
                                    }
                                }

                                if (page == 1) {
                                    mView.refreshKindBook(value);
                                    mView.refreshKindFinish(value.size() <= 0);
                                } else {
                                    mView.loadMoreKindBook(value);
                                }
                                page++;
                            }
                        }


                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            //mView.searchBookError();
                            kindHasMore = false;
                            kindSearch(url, tag, searchTime);
                        }
                    });
        }else {//到头了

            mView.refreshKindFinish(true);
            mView.loadMoreKindFinish(true);
        }
    }

    @Override
    public void initData() {
        if (disposable != null) return;

        Single.create((SingleOnSubscribe<List<MyFindKindGroupBean>>) e -> {
            boolean showAllFind = MApplication.getInstance().getConfigPreferences().getBoolean("showAllFind", true);
            List<MyFindKindGroupBean> group = new ArrayList<>();

            List<BookSourceBean> sourceBeans = new ArrayList<>(showAllFind ? BookSourceManager.getAllBookSourceBySerialNumber() : BookSourceManager.getSelectedBookSourceBySerialNumber());
            for (BookSourceBean sourceBean : sourceBeans) {
                try {

                    if (!TextUtils.isEmpty(sourceBean.getRuleFindUrl())) {

                        MyFindKindGroupBean groupBean = new MyFindKindGroupBean();
                        groupBean.setGroupName(sourceBean.getBookSourceName());
                        groupBean.setGroupTag(sourceBean.getBookSourceUrl());
                        group.add(groupBean);


                    }
                } catch (Exception exception) {
                    sourceBean.addGroup("发现规则语法错误");
                    BookSourceManager.addBookSource(sourceBean);
                }
            }
            e.onSuccess(group);
        })
                .compose(RxUtils::toSimpleSingle)
                .subscribe(new MySingleObserver<List<MyFindKindGroupBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onSuccess(List<MyFindKindGroupBean> recyclerViewData) {

                        disposable.dispose();
                        disposable = null;
                        mView.updateUI(recyclerViewData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(mView.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        disposable.dispose();
                        disposable = null;
                    }


                });
    }


    @Override
    public void getSecondFind(MyFindKindGroupBean findKindGroupBean) {
        if (disposable != null) return;

        Single.create((SingleOnSubscribe<List<FindKindBean>>) e -> {

            List<FindKindBean> list = new ArrayList<FindKindBean>();
            ACache aCache = ACache.get(mView.getContext(), "findCache");

            BookSourceBean sourceBean = BookSourceManager.getBookSourceByUrl(findKindGroupBean.getGroupTag());

            try {
                //读取发现规则
                boolean isJsAndCache = sourceBean.getRuleFindUrl().startsWith("<js>");
                String findRule;//这个字符串有可能是js生成的
                String[] kindA;//第一级
                if (isJsAndCache) {
                    findRule = aCache.getAsString(sourceBean.getBookSourceUrl());
                    if (TextUtils.isEmpty(findRule)) {
                        String jsStr = sourceBean.getRuleFindUrl().substring(4, sourceBean.getRuleFindUrl().lastIndexOf("<"));
                        findRule = evalJS(jsStr, sourceBean.getBookSourceUrl()).toString();
                    } else {
                        isJsAndCache = false;
                    }
                } else {
                    findRule = sourceBean.getRuleFindUrl();
                }

                kindA = findRule.split("(&&|\n)+");
                for (String kindB : kindA) {
                    if (kindB.trim().isEmpty()) continue;
                    String kind[] = kindB.split("::");
                    FindKindBean findKindBean = new FindKindBean();
                    findKindBean.setGroup(sourceBean.getBookSourceName());
                    findKindBean.setTag(sourceBean.getBookSourceUrl());
                    findKindBean.setKindName(kind[0]);
                    findKindBean.setKindUrl(kind[1]);
                    list.add(findKindBean);
                }

                if (isJsAndCache) {
                    aCache.put(sourceBean.getBookSourceUrl(), findRule);
                }
            }catch (Exception exception) {
                sourceBean.addGroup("发现规则语法错误");
                BookSourceManager.addBookSource(sourceBean);
            }

            e.onSuccess(list);
        })
                .compose(RxUtils::toSimpleSingle)
                .subscribe(new MySingleObserver<List<FindKindBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onSuccess(List<FindKindBean> list) {
                        //mView.updateUI(recyclerViewData);
                        mView.ShowSecond(list,findKindGroupBean.getGroupName());
                        disposable.dispose();
                        disposable = null;
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(mView.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        disposable.dispose();
                        disposable = null;
                    }


                });
    }


    /**
     * 执行JS
     */
    private Object evalJS(String jsStr, String baseUrl) throws Exception {
        SimpleBindings bindings = new SimpleBindings();
        bindings.put("java", getAnalyzeRule());
        bindings.put("baseUrl", baseUrl);
        return SCRIPT_ENGINE.eval(jsStr, bindings);
    }

    private AnalyzeRule getAnalyzeRule() {
        if (analyzeRule == null) {
            analyzeRule = new AnalyzeRule(null);
        }
        return analyzeRule;
    }

    @Override
    public void attachView(@NonNull IView iView) {
        super.attachView(iView);
        RxBus.get().register(this);
    }

    @Override
    public void detachView() {
        RxBus.get().unregister(this);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD,
            tags = {@Tag(RxBusTag.UPDATE_BOOK_SOURCE)})
    public void hadAddOrRemoveBook(Object object) {
        initData();
    }
}