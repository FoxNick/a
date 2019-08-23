package com.kunfei.bookshelf.model;

import com.kunfei.bookshelf.DbHelper;
import com.kunfei.bookshelf.MApplication;
import com.kunfei.bookshelf.base.BaseModelImpl;
import com.kunfei.bookshelf.bean.BookSourceBean;
import com.kunfei.bookshelf.bean.RecommendBookBean;
import com.kunfei.bookshelf.bean.RecommendBookListBean;
import com.kunfei.bookshelf.bean.RecommendIndexBean;
import com.kunfei.bookshelf.bean.TxtChapterRuleBean;
import com.kunfei.bookshelf.dao.RecommendBookListBeanDao;
import com.kunfei.bookshelf.dao.TxtChapterRuleBeanDao;
import com.kunfei.bookshelf.model.analyzeRule.AnalyzeHeaders;
import com.kunfei.bookshelf.model.impl.IHttpGetApi;
import com.kunfei.bookshelf.utils.GsonUtils;
import com.kunfei.bookshelf.utils.IOUtils;
import com.kunfei.bookshelf.utils.NetworkUtils;
import com.kunfei.bookshelf.utils.RxUtils;
import com.kunfei.bookshelf.utils.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class BookListManager {

    public static List<TxtChapterRuleBean> getAll1() {
        List<TxtChapterRuleBean> beans = DbHelper.getDaoSession().getTxtChapterRuleBeanDao().loadAll();
        if (beans.isEmpty()) {
            return getDefault();
        }
        return beans;
    }

    public static List<RecommendIndexBean> getAll() {
        List<RecommendIndexBean> beans = DbHelper.getDaoSession().getRecommendIndexBeanDao().loadAll();

        return beans;
    }


    public static List<RecommendBookListBean> queryBookList11(Long id) {
        List<RecommendBookListBean> beans = DbHelper.getDaoSession().getRecommendBookListBeanDao()
                .queryBuilder()
                 .where(RecommendBookListBeanDao.Properties.Id.eq(id))
                .build().list();

        return beans;
    }


    public static Observable<List<RecommendIndexBean>>  queryBookList(RecommendIndexBean recommendIndexBean) {


        String string = recommendIndexBean.getUrl();
        if (StringUtils.isTrimEmpty(string)) return null;
        string = string.trim();
        if (NetworkUtils.isIPv4Address(string)) {
            string = String.format("http://%s:65501", string);
        }
        if (NetworkUtils.isUrl(string)) {
            return BaseModelImpl.getInstance().getRetrofitString(StringUtils.getBaseUrl(string), "utf-8")
                    .create(IHttpGetApi.class)
                    .get(string, AnalyzeHeaders.getMap(null))
                    .flatMap(rsp -> importBookListFromJson(recommendIndexBean,rsp.body()))
                    .compose(RxUtils::toSimpleSingle);
        }
        return Observable.error(new Exception("不是Json或Url格式"));


        //return beans;
    }


    public static Observable<List<RecommendBookBean>>  queryBookListDetail(RecommendBookListBean recommendBookListBean) {


        String string = recommendBookListBean.getUrl();
        if (StringUtils.isTrimEmpty(string)) return null;
        string = string.trim();
        if (NetworkUtils.isIPv4Address(string)) {
            string = String.format("http://%s:65501", string);
        }
        if (NetworkUtils.isUrl(string)) {
            return BaseModelImpl.getInstance().getRetrofitString(StringUtils.getBaseUrl(string), "utf-8")
                    .create(IHttpGetApi.class)
                    .get(string, AnalyzeHeaders.getMap(null))
                    .flatMap(rsp -> importBookListDetailFromJson(recommendBookListBean,rsp.body()))
                    .compose(RxUtils::toSimpleSingle);
        }
        return Observable.error(new Exception("不是Json或Url格式"));


        //return beans;
    }


    private static Observable<List<RecommendBookBean>> importBookListDetailFromJson(RecommendBookListBean recommendBookListBean,String json) {
        return Observable.create(e -> {
           // List<RecommendIndexBean> recommendIndexBeanList = new ArrayList<>();

            if (StringUtils.isJsonArray(json)) {
                try {
                    List<RecommendBookBean> list = GsonUtils.parseJArray(json, RecommendBookBean.class);

                    // recommendIndexBean.setTitle(recommendIndexBeanNew.getTitle());
                    //recommendBookListBean.setAuthor(recommendIndexBeanNew.getAuthor());
                    //recommendBookListBean.setList(recommendIndexBeanNew.getList());
                    // save(recommendIndexBean);
                    //saveBookList(recommendIndexBean);
                    //recommendIndexBeanList.add(recommendIndexBean);
                    e.onNext(list);
                    e.onComplete();
                    return;
                } catch (Exception ignored) {
                }
            }
            e.onError(new Throwable("格式不对"));
        });
    }


    public static List<TxtChapterRuleBean> getDefault() {
        String json = null;
        try {
            InputStream inputStream = MApplication.getInstance().getAssets().open("txtChapterRule.json");
            json = IOUtils.toString(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<TxtChapterRuleBean> ruleBeanList = GsonUtils.parseJArray(json, TxtChapterRuleBean.class);
        if (ruleBeanList != null) {
            DbHelper.getDaoSession().getTxtChapterRuleBeanDao().insertOrReplaceInTx(ruleBeanList);
            return ruleBeanList;
        }
        return new ArrayList<>();
    }


    public static void del(RecommendIndexBean recommendIndexBean) {
        DbHelper.getDaoSession().getRecommendIndexBeanDao().delete(recommendIndexBean);
    }

    public static Observable<List<RecommendIndexBean>> importBookList(RecommendIndexBean recommendIndexBean) {
        String string = recommendIndexBean.getUrl();
        if (StringUtils.isTrimEmpty(string)) return null;
        string = string.trim();
        if (NetworkUtils.isIPv4Address(string)) {
            string = String.format("http://%s:65501", string);
        }
        if (NetworkUtils.isUrl(string)) {
            return BaseModelImpl.getInstance().getRetrofitString(StringUtils.getBaseUrl(string), "utf-8")
                    .create(IHttpGetApi.class)
                    .get(string, AnalyzeHeaders.getMap(null))
                    .flatMap(rsp -> importBookListFromJson(recommendIndexBean,rsp.body()))
                    .compose(RxUtils::toSimpleSingle);
        }
        return Observable.error(new Exception("不是Json或Url格式"));
    }


    private static Observable<List<RecommendIndexBean>> importBookListFromJson(RecommendIndexBean recommendIndexBean,String json) {
        return Observable.create(e -> {
            List<RecommendIndexBean> recommendIndexBeanList = new ArrayList<>();

            if (StringUtils.isJsonObject(json)) {
                try {
                    RecommendIndexBean recommendIndexBeanNew = GsonUtils.parseJObject(json, RecommendIndexBean.class);

                   // recommendIndexBean.setTitle(recommendIndexBeanNew.getTitle());
                    recommendIndexBean.setAuthor(recommendIndexBeanNew.getAuthor());
                    recommendIndexBean.setList(recommendIndexBeanNew.getList());
                   // save(recommendIndexBean);
                    //saveBookList(recommendIndexBean);
                    recommendIndexBeanList.add(recommendIndexBean);
                    e.onNext(recommendIndexBeanList);
                    e.onComplete();
                    return;
                } catch (Exception ignored) {
                }
            }
            e.onError(new Throwable("格式不对"));
        });
    }


    public static void save(TxtChapterRuleBean txtChapterRuleBean) {
        if (txtChapterRuleBean.getSerialNumber() == null) {
            txtChapterRuleBean.setSerialNumber((int) DbHelper.getDaoSession().getTxtChapterRuleBeanDao().queryBuilder().count());
        }
        DbHelper.getDaoSession().getTxtChapterRuleBeanDao().insertOrReplace(txtChapterRuleBean);
    }

    public static void save(RecommendIndexBean recommendIndexBean) {

        DbHelper.getDaoSession().getRecommendIndexBeanDao().insertOrReplace(recommendIndexBean);
    }

    public static void saveBookList(RecommendIndexBean recommendIndexBean) {

       for( com.kunfei.bookshelf.bean.RecommendBookListBean  recommendBookListBean  :recommendIndexBean.getList()){

           recommendBookListBean.setId(recommendIndexBean.getId());
            DbHelper.getDaoSession().getRecommendBookListBeanDao().insertOrReplace(recommendBookListBean);
        }

    }

    public static void save(List<TxtChapterRuleBean> txtChapterRuleBeans) {
        DbHelper.getDaoSession().getTxtChapterRuleBeanDao().insertOrReplaceInTx(txtChapterRuleBeans);
    }
}
