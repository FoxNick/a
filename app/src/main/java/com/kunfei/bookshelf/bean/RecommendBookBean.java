package com.kunfei.bookshelf.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class RecommendBookBean {


    /**
     * title : 第一本书
     * comment : 老作者阿斯大法  。
     * author : 作者
     * star : 5
     * wordCount : 5万字
     * chapterNum : 1200章
     * status : 太监
     */

    @Id
    private Long id = System.currentTimeMillis();
    private String title;//名称
    private String comment;//说明
    private String author;//作者
    private String star;//星级
    private String wordCount;//字数
    private String chapterNum;//章节数
    private String status;//状态，太监，连载，完结

    @Generated(hash = 571999590)
    public RecommendBookBean(Long id, String title, String comment, String author,
            String star, String wordCount, String chapterNum, String status) {
        this.id = id;
        this.title = title;
        this.comment = comment;
        this.author = author;
        this.star = star;
        this.wordCount = wordCount;
        this.chapterNum = chapterNum;
        this.status = status;
    }

    @Generated(hash = 1239541091)
    public RecommendBookBean() {
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getWordCount() {
        return wordCount;
    }

    public void setWordCount(String wordCount) {
        this.wordCount = wordCount;
    }

    public String getChapterNum() {
        return chapterNum;
    }

    public void setChapterNum(String chapterNum) {
        this.chapterNum = chapterNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
