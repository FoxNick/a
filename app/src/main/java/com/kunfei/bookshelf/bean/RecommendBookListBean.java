package com.kunfei.bookshelf.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class RecommendBookListBean {

    /**
     * name : 新书速递100本
     * desc : 新书速递100本
     * url : http://xxx/aaa.json
     */

    private Long id;
    private String name;//书单名称
    private String author;//书单描述
    private String url;//书单地址
    private int num;//书本数量



    @Generated(hash = 1332384894)
    public RecommendBookListBean(Long id, String name, String author, String url,
            int num) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.url = url;
        this.num = num;
    }

    @Generated(hash = 1288298382)
    public RecommendBookListBean() {
    }



    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
