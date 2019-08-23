package com.kunfei.bookshelf.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.util.ArrayList;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class RecommendIndexBean {

    /**
     * title : 15年老书虫的推书
     * author : wdise
     * list : [{"name":"新书速递100本","desc":"新书速递100本","url":"http://xxx/aaa.json"},{"name":"在看的书 ","desc":"新书速递100本","url":"http://xxx/bbb.json"},{"name":"故事精彩是第一判断标准","desc":"新书速递100本","url":"http://xxx/aaa.json"},{"name":"白一点的后宫文","desc":"新书速递100本","url":"http://xxx/aaa.json"}]
     */

    @Id
    private Long id ;
    private String title;//标题
    private String author;//作者
    private  String url;//地址

    @Transient
    private List<RecommendBookListBean>  list = new ArrayList<RecommendBookListBean>();



    @Generated(hash = 1628495135)
    public RecommendIndexBean(Long id, String title, String author, String url) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.url = url;
    }

    @Generated(hash = 78736937)
    public RecommendIndexBean() {
    }



    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<RecommendBookListBean> getList() {
        return list;
    }

    public void setList(List<RecommendBookListBean> list) {
        this.list = list;
    }


}
