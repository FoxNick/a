package com.kunfei.bookshelf.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

@Entity
public class BookSpecStyleBean  {

    @Id(autoincrement = true)
    private Long id;

    private String bookName;//书名
    private String bookAuthor;
    private String styleJson;//样式json
    @Generated(hash = 82595838)
    public BookSpecStyleBean(Long id, String bookName, String bookAuthor,
            String styleJson) {
        this.id = id;
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.styleJson = styleJson;
    }
    @Generated(hash = 1137612453)
    public BookSpecStyleBean() {
    }
    public String getBookName() {
        return this.bookName;
    }
    public void setBookName(String bookName) {
        this.bookName = bookName;
    }
    public String getBookAuthor() {
        return this.bookAuthor;
    }
    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }
    public String getStyleJson() {
        return this.styleJson;
    }
    public void setStyleJson(String styleJson) {
        this.styleJson = styleJson;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }



}
