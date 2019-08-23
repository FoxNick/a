package com.kunfei.bookshelf.model.task;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.google.android.material.snackbar.Snackbar;
import com.kunfei.bookshelf.DbHelper;
import com.kunfei.bookshelf.base.observer.MyObserver;
import com.kunfei.bookshelf.bean.BookSourceBean;
import com.kunfei.bookshelf.bean.CookieBean;
import com.kunfei.bookshelf.bean.RecommendIndexBean;
import com.kunfei.bookshelf.bean.SearchBookBean;
import com.kunfei.bookshelf.model.BookListManager;
import com.kunfei.bookshelf.model.WebBookModel;
import com.kunfei.bookshelf.model.analyzeRule.AnalyzeRule;
import com.kunfei.bookshelf.service.CheckSourceService;
import com.kunfei.bookshelf.service.UpdateBookListService;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.script.SimpleBindings;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.kunfei.bookshelf.constant.AppConstant.SCRIPT_ENGINE;

public class UpdateBookListTask {

    private RecommendIndexBean recommendIndexBean;
    private Scheduler scheduler;
    private UpdateBookListService.UpdateBookListListener updateBookListListener;

    public UpdateBookListTask(RecommendIndexBean recommendIndexBean, Scheduler scheduler, UpdateBookListService.UpdateBookListListener updateBookListListener) {
        this.recommendIndexBean = recommendIndexBean;
        this.scheduler = scheduler;
        this.updateBookListListener = updateBookListListener;
    }

    public void startCheck() {
        if (!TextUtils.isEmpty(recommendIndexBean.getUrl())) {

            Observable<List<RecommendIndexBean>> observable = BookListManager.importBookList(recommendIndexBean);
            if (observable != null) {
                observable.subscribe(getImportObserver());
            }
        }
    }


    private MyObserver<List<RecommendIndexBean>> getImportObserver() {
        return new MyObserver<List<RecommendIndexBean>>() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onNext(List<RecommendIndexBean> bookSourceBeans) {
                if (bookSourceBeans.size() > 0) {
                    //mView.refreshBookSource();
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




}
