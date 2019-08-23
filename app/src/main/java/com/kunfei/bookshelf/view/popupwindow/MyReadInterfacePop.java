//Copyright (c) 2017. 章钦豪. All rights reserved.
package com.kunfei.bookshelf.view.popupwindow;

import android.content.Context;
import android.content.Intent;

import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.kunfei.bookshelf.MApplication;
import com.kunfei.bookshelf.R;
import com.kunfei.bookshelf.help.ReadBookControl;
import com.kunfei.bookshelf.utils.PermissionUtils;
import com.kunfei.bookshelf.utils.theme.ATH;
import com.kunfei.bookshelf.view.activity.MyReadBookActivity;
import com.kunfei.bookshelf.view.activity.ReadStyleActivity;
import com.kunfei.bookshelf.widget.CustomRoundAngleImageView;
import com.kunfei.bookshelf.widget.font.FontSelector;
import com.kunfei.bookshelf.widget.my_page.animation.PageAnimation;
import com.kunfei.bookshelf.widget.number.NumberPickerDialog;
import com.kunfei.bookshelf.widget.style.StyleSelector;
import com.kunfei.bookshelf.widget.views.ATECheckBox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyReadInterfacePop extends FrameLayout {

    //字体大小，字体
    @BindView(R.id.fl_smaller)
    FrameLayout flSmaller;
    @BindView(R.id.tv_dur_textsize)
    TextView tvTextSize;
    @BindView(R.id.fl_bigger)
    FrameLayout flBigger;

    //加粗
    @BindView(R.id.fl_bold_smaller)
    FrameLayout flBoldSmaller;
    @BindView(R.id.tv_dur_bold_size)
    TextView tvBoldSize;
    @BindView(R.id.fl_bold_bigger)
    FrameLayout flBoldBigger;
    //@BindView(R.id.fl_bold)
    //FrameLayout  flBold;
    //@BindView(R.id.tv_bold)
    //TextView  tvBold;
    @BindView(R.id.fl_moreFont)
    FrameLayout flMoreFont;

    //样式相关
    @BindView(R.id.fl_space1)
    FrameLayout flSpace1;
    @BindView(R.id.fl_space2)
    FrameLayout flSpace2;
    @BindView(R.id.fl_space3)
    FrameLayout flSpace3;
    @BindView(R.id.fl_space_none)
    FrameLayout flSpaceNone;
    @BindView(R.id.fl_indent)
    FrameLayout flIndent;
    @BindView(R.id.fl_moreStyle)
    FrameLayout flMoreStyle;

    //配色相关
    @BindView(R.id.fl_bg_0)
    RelativeLayout flBg0;
    @BindView(R.id.tv_0)
    TextView tv0;
    @BindView(R.id.civ_0)
    CustomRoundAngleImageView civ0;
    @BindView(R.id.fl_bg_1)
    RelativeLayout flBg1;
    @BindView(R.id.tv_1)
    TextView tv1;
    @BindView(R.id.civ_1)
    CustomRoundAngleImageView civ1;
    @BindView(R.id.fl_bg_2)
    RelativeLayout flBg2;
    @BindView(R.id.tv_2)
    TextView tv2;
    @BindView(R.id.civ_2)
    CustomRoundAngleImageView civ2;
    @BindView(R.id.fl_bg_3)
    RelativeLayout flBg3;
    @BindView(R.id.tv_3)
    TextView tv3;
    @BindView(R.id.civ_3)
    CustomRoundAngleImageView civ3;
    @BindView(R.id.fl_moreColor)
    FrameLayout flMoreColor;

    //翻页相关
    @BindView(R.id.fl_page1)
    FrameLayout flPage1;
    @BindView(R.id.fl_page2)
    FrameLayout flPage2;
    @BindView(R.id.fl_page3)
    FrameLayout flPage3;
    @BindView(R.id.fl_morePage)
    FrameLayout flMorePage;

    //
    @BindView(R.id.fl_chapter_top)
    FrameLayout flChapterTop;
    @BindView(R.id.fl_chapter_buttom)
    FrameLayout flChapterButtom;
    @BindView(R.id.fl_chapter_size)
    FrameLayout flChapterSize;
    @BindView(R.id.fl_chapter_font)
    FrameLayout flChapterFont;

    @BindView(R.id.tv_chapter_buttom)
    TextView tvChapterButtom;
   @BindView(R.id.tv_chapter_top)
   TextView tvChapterTop;
    @BindView(R.id.tv_chapter_size)
    TextView tvChapterSize;

    //样式应用范围
    @BindView(R.id.cb_this_book)
    ATECheckBox cbThisBook;
    //@BindView(R.id.cb_this_source)
    //ATECheckBox cbThisSource;
    //@BindView(R.id.cb_this_app)
    //ATECheckBox cbThisAPP;

    private String[] numbers = {"0.0","0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9", "1.0", "1.1", "1.2", "1.3", "1.4", "1.5", "1.6", "1.7", "1.8", "1.9", "2.0"};

    private MyReadBookActivity activity;
    private ReadBookControl readBookControl = ReadBookControl.getInstance();
    private OnChangeProListener changeProListener;

    public MyReadInterfacePop(Context context) {
        super(context);
        init(context);
    }

    public MyReadInterfacePop(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyReadInterfacePop(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_pop_read_interface, null);
        addView(view);
        ButterKnife.bind(this, view);
        view.setOnClickListener(null);
    }

    public void setListener(MyReadBookActivity readBookActivity, @NonNull OnChangeProListener changeProListener) {
        this.activity = readBookActivity;
        this.changeProListener = changeProListener;
        initData();
        bindEvent();
    }

    private void initData() {
        tvTextSize.setText(String.valueOf(readBookControl.getTextSize()));
        setBg();
        updateBg(readBookControl.getTextDrawableIndex());
        updateBoldText(readBookControl.getBoldSize());
        updateStyleDefault(readBookControl.getStyleDefault());
        updatePageMode(readBookControl.getPageMode());
        updateChapterStyle();
        updateStyleArea(readBookControl.getBookSpecStyle());
    }

    private void bindEvent() {

        flSmaller.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int c = readBookControl.getTextSize()-1;

                if(cbThisBook.isChecked()) {
                    Map<String,String> bss = readBookControl.getBookSpecStyle();
                    bss.put("textSize",c+"");
                    readBookControl.setBookSpecStyle(bss);//存入内存
                    changeProListener.upBookSpecStyle(bss);//存入数据库
                }else{
                    readBookControl.setTextSize(c);
                }

                changeProListener.upTextSize();
                tvTextSize.setText(String.valueOf(c));
            }
        });

        tvTextSize.setOnClickListener(new OnClickListener() {
              @Override
              public void onClick(View v) {

                  NumberPickerDialog npd = new NumberPickerDialog(getContext());
                  npd.setTitle("设置字体大小")
                          .setMaxValue((int) 50)
                          .setMinValue((int) 1)
                          .setValue((int) readBookControl.getTextSize())
                          .setListener(new NumberPickerDialog.OnClickListener(){
                              @Override
                              public void setNumber(int c) {
                                  tvTextSize.setText(String.valueOf(c));

                                  if(cbThisBook.isChecked()) {
                                      Map<String,String> bss = readBookControl.getBookSpecStyle();
                                      bss.put("textSize",c+"");
                                      readBookControl.setBookSpecStyle(bss);//存入内存
                                      changeProListener.upBookSpecStyle(bss);//存入数据库
                                  }else{
                                      readBookControl.setTextSize(c);
                                  }

                                  if (changeProListener != null) {
                                      changeProListener.upTextSize();
                                  }
                              }
                          })
                          .create()
                          .show();

              }
        });

        flBigger.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int c = readBookControl.getTextSize()+1;
                if(cbThisBook.isChecked()) {
                    Map<String,String> bss = readBookControl.getBookSpecStyle();
                    bss.put("textSize",c+"");
                    readBookControl.setBookSpecStyle(bss);//存入内存
                    changeProListener.upBookSpecStyle(bss);//存入数据库
                }else{
                    readBookControl.setTextSize(c);
                }
                changeProListener.upTextSize();
                tvTextSize.setText(String.valueOf(c));
            }
        });

        flBoldSmaller.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                float c = readBookControl.getBoldSize()-0.1f;
                if(c<0)  c = 0.0f;

                if(cbThisBook.isChecked()) {
                    Map<String,String> bss = readBookControl.getBookSpecStyle();
                    bss.put("boldSize", c+"");
                    readBookControl.setBookSpecStyle(bss);//存入内存
                    changeProListener.upBookSpecStyle(bss);//存入数据库
                }else {
                    readBookControl.setBoldSize(c);
                }

               updateBoldText(c);
                changeProListener.refresh();

            }
        });


        tvBoldSize.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                NumberPickerDialog npd = new NumberPickerDialog(getContext());

                npd.setDisplayedValues(null);
                npd.setTitle("设置字体粗细")
                        .setMaxValue((int) numbers.length-1)
                        .setDisplayedValues(numbers)
                        .setMinValue((int) 0)
                        .setValue((int) (readBookControl.getBoldSize()*10))
                        .setListener(new NumberPickerDialog.OnClickListener(){
                            @Override
                            public void setNumber(int c) {
                                tvBoldSize.setText(String.valueOf(c/10f));

                                if(cbThisBook.isChecked()) {
                                    Map<String,String> bss = readBookControl.getBookSpecStyle();
                                    bss.put("boldSize",(c/10f)+"");
                                    readBookControl.setBookSpecStyle(bss);//存入内存
                                    changeProListener.upBookSpecStyle(bss);//存入数据库
                                }else{
                                    readBookControl.setBoldSize(c/10f);
                                }

                                if (changeProListener != null) {
                                    changeProListener.refresh();
                                }
                            }
                        })
                        .create()
                        .show();

            }
        });


        flBoldBigger.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                float c = readBookControl.getBoldSize()+0.1f;
                if(cbThisBook.isChecked()) {
                    Map<String,String> bss = readBookControl.getBookSpecStyle();
                    bss.put("boldSize", c+"");
                    readBookControl.setBookSpecStyle(bss);//存入内存
                    changeProListener.upBookSpecStyle(bss);//存入数据库
                }else {
                    readBookControl.setBoldSize(c);
                }
                updateBoldText(c);
                changeProListener.refresh();
            }
        });

        /*
        flBold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readBookControl.setTextBold(!readBookControl.getTextBold());
                updateBoldText(readBookControl.getTextBold());
                changeProListener.refresh();
            }
        });
        */

        //选择字体
        flMoreFont.setOnClickListener(view -> {
            List<String> per = PermissionUtils.checkMorePermissions(activity, MApplication.PerList);
            if (per.isEmpty()) {
                new FontSelector(activity, readBookControl.getFontPath())
                        .setListener(new FontSelector.OnThisListener() {
                            @Override
                            public void setDefault() {
                                clearFontPath();
                            }

                            @Override
                            public void setFontPath(String fontPath) {
                                setReadFonts(fontPath);
                            }
                        })
                        .create()
                        .show();
            } else {
                Toast.makeText(activity, "本软件需要存储权限来存储备份书籍信息", Toast.LENGTH_SHORT).show();
                PermissionUtils.requestMorePermissions(activity, per, MApplication.RESULT__PERMS);
            }
        });

        //长按清除字体
        flMoreFont.setOnLongClickListener(view -> {
            clearFontPath();
            activity.toast(R.string.clear_font);
            return true;
        });

        //选择標題字体
        flChapterFont.setOnClickListener(view -> {
            List<String> per = PermissionUtils.checkMorePermissions(activity, MApplication.PerList);
            if (per.isEmpty()) {
                new FontSelector(activity, readBookControl.getChapterFontPath())
                        .setListener(new FontSelector.OnThisListener() {
                            @Override
                            public void setDefault() {
                                //clearFontPath();
                                clearChapterFontPath();
                            }

                            @Override
                            public void setFontPath(String fontPath) {

                                //setReadFonts(fontPath);
                                setChapterReadFonts(fontPath);
                            }
                        })
                        .create()
                        .show();
            } else {
                Toast.makeText(activity, "本软件需要存储权限来存储备份书籍信息", Toast.LENGTH_SHORT).show();
                PermissionUtils.requestMorePermissions(activity, per, MApplication.RESULT__PERMS);
            }
        });

        //长按清除字体
        flChapterFont.setOnLongClickListener(view -> {
            clearChapterFontPath();
            activity.toast(R.string.clear_font);
            return true;
        });

        //默认间距1
        flSpace1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cbThisBook.isChecked()) {
                    Map<String,String> bss = readBookControl.getBookSpecStyle();
                    bss.put("styleDefault", 1+"");
                    bss.put("lineMultiplier", 20+"");
                    bss.put("paragraphSize", 40+"");
                    bss.put("paddingTop", 10+"");
                    bss.put("paddingBottom", 10+"");
                    bss.put("paddingLeft", 20+"");
                    bss.put("paddingRight", 20+"");
                    readBookControl.setBookSpecStyle(bss);//存入内存
                    changeProListener.upBookSpecStyle(bss);//存入数据库
                }else {
                    readBookControl.setStyleDefault(1);
                    readBookControl.setLineMultiplier(20);
                    readBookControl.setParagraphSize(40);
                    readBookControl.setPaddingTop(10);
                    readBookControl.setPaddingBottom(10);
                    readBookControl.setPaddingLeft(20);
                    readBookControl.setPaddingRight(20);
                }
                updateStyleDefault(1);
                changeProListener.upTextSizeAndMargin();
            }
        });
        //默认间距2
        flSpace2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cbThisBook.isChecked()) {
                    Map<String,String> bss = readBookControl.getBookSpecStyle();
                    bss.put("styleDefault", 2+"");
                    bss.put("lineMultiplier", 30+"");
                    bss.put("paragraphSize", 60+"");
                    bss.put("paddingTop", 10+"");
                    bss.put("paddingBottom", 10+"");
                    bss.put("paddingLeft", 20+"");
                    bss.put("paddingRight", 20+"");
                    readBookControl.setBookSpecStyle(bss);//存入内存
                    changeProListener.upBookSpecStyle(bss);//存入数据库
                }else {
                    readBookControl.setStyleDefault(2);
                    readBookControl.setLineMultiplier(30);
                    readBookControl.setParagraphSize(60);
                    readBookControl.setPaddingTop(10);
                    readBookControl.setPaddingBottom(10);
                    readBookControl.setPaddingLeft(20);
                    readBookControl.setPaddingRight(20);

                }
                updateStyleDefault(2);
                changeProListener.upTextSizeAndMargin();
            }
        });

        //默认间距3
        flSpace3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cbThisBook.isChecked()) {
                    Map<String,String> bss = readBookControl.getBookSpecStyle();
                    bss.put("styleDefault", 3+"");
                    bss.put("lineMultiplier", 40+"");
                    bss.put("paragraphSize", 80+"");
                    bss.put("paddingTop", 10+"");
                    bss.put("paddingBottom", 10+"");
                    bss.put("paddingLeft", 20+"");
                    bss.put("paddingRight", 20+"");
                    readBookControl.setBookSpecStyle(bss);//存入内存
                    changeProListener.upBookSpecStyle(bss);//存入数据库
                }else {
                    readBookControl.setStyleDefault(3);
                    readBookControl.setLineMultiplier(40);
                    readBookControl.setParagraphSize(80);
                    readBookControl.setPaddingTop(10);
                    readBookControl.setPaddingBottom(10);
                    readBookControl.setPaddingLeft(20);
                    readBookControl.setPaddingRight(20);

                }

                updateStyleDefault(3);
                changeProListener.upTextSizeAndMargin();
            }
        });

        //默认间距4
        flSpaceNone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cbThisBook.isChecked()) {
                    Map<String,String> bss = readBookControl.getBookSpecStyle();
                    bss.put("styleDefault", 4+"");
                    bss.put("lineMultiplier", 0+"");
                    bss.put("paragraphSize", 0+"");
                    bss.put("paddingTop", 0+"");
                    bss.put("paddingBottom", 0+"");
                    bss.put("paddingLeft", 20+"");
                    bss.put("paddingRight", 20+"");
                    readBookControl.setBookSpecStyle(bss);//存入内存
                    changeProListener.upBookSpecStyle(bss);//存入数据库
                }else {
                    readBookControl.setStyleDefault(4);
                    readBookControl.setLineMultiplier(0);
                    readBookControl.setParagraphSize(0);
                    readBookControl.setPaddingTop(0);
                    readBookControl.setPaddingBottom(0);
                    readBookControl.setPaddingLeft(20);
                    readBookControl.setPaddingRight(20);

                }
                updateStyleDefault(4);
                changeProListener.upTextSizeAndMargin();
            }
        });

        //缩进
        flIndent.setOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(activity, R.style.alertDialogThemeRead)
                    .setTitle(activity.getString(R.string.indent))
                    .setSingleChoiceItems(activity.getResources().getStringArray(R.array.indent),
                            readBookControl.getIndent(),
                            (dialogInterface, i) -> {
                                if(cbThisBook.isChecked()) {
                                    Map<String,String> bss = readBookControl.getBookSpecStyle();
                                    bss.put("indent",i+"");
                                    readBookControl.setBookSpecStyle(bss);//存入内存
                                    changeProListener.upBookSpecStyle(bss);//存入数据库
                                }else {
                                    readBookControl.setIndent(i);
                                }

                                changeProListener.refresh();
                                dialogInterface.dismiss();
                            })
                    .create();
            dialog.show();
            //ATH.setAlertDialogTint(dialog);
        });






        //自定义样式
        flMoreStyle.setOnClickListener(view -> {
            new StyleSelector(activity, readBookControl.getFontPath())
                    .setListener(new StyleSelector.OnThisListener() {
                        @Override
                        public void setLineMultiplier(float number) {
                            if(cbThisBook.isChecked()) {
                                Map<String,String> bss = readBookControl.getBookSpecStyle();
                                bss.put("styleDefault",5+"");
                                bss.put("lineMultiplier",number+"");
                                readBookControl.setBookSpecStyle(bss);//存入内存
                                changeProListener.upBookSpecStyle(bss);//存入数据库
                            }else {
                                readBookControl.setStyleDefault(5);
                                readBookControl.setLineMultiplier(number);
                            }
                            updateStyleDefault(5);
                            changeProListener.upTextSize();
                        }

                        @Override
                        public void setParagraphSize(float number) {
                            if(cbThisBook.isChecked()) {
                                Map<String,String> bss = readBookControl.getBookSpecStyle();
                                bss.put("styleDefault",5+"");
                                bss.put("paragraphSize",number+"");
                                readBookControl.setBookSpecStyle(bss);//存入内存
                                changeProListener.upBookSpecStyle(bss);//存入数据库
                            }else {
                                readBookControl.setStyleDefault(5);
                                readBookControl.setParagraphSize(number);
                            }
                            updateStyleDefault(5);
                            changeProListener.upTextSize();
                        }

                        @Override
                        public void setPaddingTop(int number) {
                            if(cbThisBook.isChecked()) {
                                Map<String,String> bss = readBookControl.getBookSpecStyle();
                                bss.put("styleDefault",5+"");
                                bss.put("paddingTop",number+"");
                                readBookControl.setBookSpecStyle(bss);//存入内存
                                changeProListener.upBookSpecStyle(bss);//存入数据库
                            }else {
                                readBookControl.setStyleDefault(5);
                                readBookControl.setPaddingTop(number);
                            }
                            updateStyleDefault(5);
                            changeProListener.upMargin();
                        }

                        @Override
                        public void setPaddingBottom(int number) {
                            if(cbThisBook.isChecked()) {
                                Map<String,String> bss = readBookControl.getBookSpecStyle();
                                bss.put("styleDefault",5+"");
                                bss.put("paddingBottom",number+"");
                                readBookControl.setBookSpecStyle(bss);//存入内存
                                changeProListener.upBookSpecStyle(bss);//存入数据库
                            }else {
                                readBookControl.setStyleDefault(5);
                                readBookControl.setPaddingBottom(number);
                            }
                            updateStyleDefault(5);
                            changeProListener.upMargin();
                        }

                        @Override
                        public void setPaddingLeft(int number) {
                            if(cbThisBook.isChecked()) {
                                Map<String,String> bss = readBookControl.getBookSpecStyle();
                                bss.put("styleDefault",5+"");
                                bss.put("paddingLeft",number+"");
                                readBookControl.setBookSpecStyle(bss);//存入内存
                                changeProListener.upBookSpecStyle(bss);//存入数据库
                            }else {
                                readBookControl.setStyleDefault(5);
                                readBookControl.setPaddingLeft(number);
                            }
                            updateStyleDefault(5);
                            changeProListener.upMargin();
                        }

                        @Override
                        public void setPaddingRight(int number) {
                            if(cbThisBook.isChecked()) {
                                Map<String,String> bss = readBookControl.getBookSpecStyle();
                                bss.put("styleDefault",5+"");
                                bss.put("paddingRight",number+"");
                                readBookControl.setBookSpecStyle(bss);//存入内存
                                changeProListener.upBookSpecStyle(bss);//存入数据库
                            }else {
                                readBookControl.setStyleDefault(5);
                                readBookControl.setPaddingRight(number);
                            }
                            updateStyleDefault(5);
                            changeProListener.upMargin();
                        }

                        @Override
                        public void setTipPaddingTop(int number) {
                            if(cbThisBook.isChecked()) {
                                Map<String,String> bss = readBookControl.getBookSpecStyle();
                                bss.put("styleDefault",5+"");
                                bss.put("tipPaddingTop",number+"");
                                readBookControl.setBookSpecStyle(bss);//存入内存
                                changeProListener.upBookSpecStyle(bss);//存入数据库
                            }else {
                                readBookControl.setStyleDefault(5);
                                readBookControl.setTipPaddingTop(number);
                            }
                            updateStyleDefault(5);
                            changeProListener.upMargin();
                        }

                        @Override
                        public void setTipPaddingBottom(int number) {
                            if(cbThisBook.isChecked()) {
                                Map<String,String> bss = readBookControl.getBookSpecStyle();
                                bss.put("styleDefault",5+"");
                                bss.put("tipPaddingBottom",number+"");
                                readBookControl.setBookSpecStyle(bss);//存入内存
                                changeProListener.upBookSpecStyle(bss);//存入数据库
                            }else {
                                readBookControl.setStyleDefault(5);
                                readBookControl.setTipPaddingBottom(number);
                            }
                            updateStyleDefault(5);
                            changeProListener.upMargin();
                        }

                        @Override
                        public void setTipPaddingLeft(int number) {
                            if(cbThisBook.isChecked()) {
                                Map<String,String> bss = readBookControl.getBookSpecStyle();
                                bss.put("styleDefault",5+"");
                                bss.put("tipPaddingLeft",number+"");
                                readBookControl.setBookSpecStyle(bss);//存入内存
                                changeProListener.upBookSpecStyle(bss);//存入数据库
                            }else {
                                readBookControl.setStyleDefault(5);
                                readBookControl.setTipPaddingLeft(number);
                            }
                            updateStyleDefault(5);
                            changeProListener.upMargin();
                        }

                        @Override
                        public void setTipPaddingRight(int number) {
                            if(cbThisBook.isChecked()) {
                                Map<String,String> bss = readBookControl.getBookSpecStyle();
                                bss.put("styleDefault",5+"");
                                bss.put("tipPaddingRight",number+"");
                                readBookControl.setBookSpecStyle(bss);//存入内存
                                changeProListener.upBookSpecStyle(bss);//存入数据库
                            }else {
                                readBookControl.setStyleDefault(5);
                                readBookControl.setTipPaddingRight(number);
                            }
                            updateStyleDefault(5);
                            changeProListener.upMargin();
                        }
                        

                    })
                    .create()
                    .show();

        });


        //背景选择
        flBg0.setOnClickListener(v -> {
            updateBg(0);
            changeNumber(0);
            changeProListener.bgChange();
        });
        flBg1.setOnClickListener(v -> {
            updateBg(1);
            changeNumber(1);
            changeProListener.bgChange();
        });
        flBg2.setOnClickListener(v -> {
            updateBg(2);
            changeNumber(2);
            changeProListener.bgChange();
        });
        flBg3.setOnClickListener(v -> {
            updateBg(3);
            changeNumber(3);
            changeProListener.bgChange();
        });
        //自定颜色
        flMoreColor.setOnClickListener(view -> {
            Intent intent = new Intent(activity, ReadStyleActivity.class);
            intent.putExtra("index", readBookControl.getTextDrawableIndex());
            intent.putExtra("isBSS", cbThisBook.isChecked());
            intent.putExtra("bookName",activity.getBookName());
            intent.putExtra("bookAuthor",activity.getBookAuthor());
            activity.startActivity(intent);
        });

        //翻页1
        flPage1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cbThisBook.isChecked()) {
                    Map<String,String> bss = readBookControl.getBookSpecStyle();
                    bss.put("pageMode",0+"");
                    readBookControl.setBookSpecStyle(bss);//存入内存
                    changeProListener.upBookSpecStyle(bss);//存入数据库
                }else {
                    readBookControl.setPageMode(0);
                }

                updatePageMode(0);
                changeProListener.upPageMode();
            }
        });

        //翻页2
        flPage2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbThisBook.isChecked()) {
                    Map<String,String> bss = readBookControl.getBookSpecStyle();
                    bss.put("pageMode",1+"");
                    readBookControl.setBookSpecStyle(bss);//存入内存
                    changeProListener.upBookSpecStyle(bss);//存入数据库
                }else {
                    readBookControl.setPageMode(1);
                }
                updatePageMode(1);
                changeProListener.upPageMode();
            }
        });

        //翻页3
        flPage3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbThisBook.isChecked()) {
                    Map<String,String> bss = readBookControl.getBookSpecStyle();
                    bss.put("pageMode",2+"");
                    readBookControl.setBookSpecStyle(bss);//存入内存
                    changeProListener.upBookSpecStyle(bss);//存入数据库
                }else {
                    readBookControl.setPageMode(2);
                }
                updatePageMode(2);
                changeProListener.upPageMode();
            }
        });


        //自定义翻页
        flMorePage.setOnClickListener(view -> {
            AlertDialog dialog = new AlertDialog.Builder(activity, R.style.alertDialogThemeRead)
                    .setTitle(activity.getString(R.string.page_mode))
                    .setSingleChoiceItems(PageAnimation.Mode.getAllPageMode(), readBookControl.getPageMode(), (dialogInterface, i) -> {
                        if(cbThisBook.isChecked()) {
                            Map<String,String> bss = readBookControl.getBookSpecStyle();
                            bss.put("pageMode",i+"");
                            readBookControl.setBookSpecStyle(bss);//存入内存
                            changeProListener.upBookSpecStyle(bss);//存入数据库
                        }else {
                            readBookControl.setPageMode(i);
                        }
                        updatePageMode(i);
                        changeProListener.upPageMode();
                        dialogInterface.dismiss();
                    })
                    .create();
            dialog.show();
        });



        //
        flChapterTop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                NumberPickerDialog npd = new NumberPickerDialog(getContext());

                npd.setDisplayedValues(null);
                npd.setTitle("设置章节名上边距")
                        .setMaxValue(300)
                        .setMinValue((int) 0)
                        .setValue((int)readBookControl.getFirstPageMarginTop())
                        .setListener(new NumberPickerDialog.OnClickListener(){
                            @Override
                            public void setNumber(int c) {
                                if(cbThisBook.isChecked()) {
                                    Map<String,String> bss = readBookControl.getBookSpecStyle();
                                    bss.put("firstPageMarginTop",c+"");
                                    readBookControl.setBookSpecStyle(bss);//存入内存
                                    changeProListener.upBookSpecStyle(bss);//存入数据库
                                }else{
                                    readBookControl.setFirstPageMarginTop(c);
                                }

                                updateChapterStyle();

                                if (changeProListener != null) {
                                    changeProListener.refresh();
                                }
                            }
                        })
                        .create()
                        .show();

            }
        });


        flChapterButtom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                NumberPickerDialog npd = new NumberPickerDialog(getContext());

                npd.setDisplayedValues(null);
                npd.setTitle("设置章节名下边距")
                        .setMaxValue(300)
                        .setMinValue((int) 0)
                        .setValue((int)readBookControl.getFirstPageMarginButtom())
                        .setListener(new NumberPickerDialog.OnClickListener(){
                            @Override
                            public void setNumber(int c) {
                                if(cbThisBook.isChecked()) {
                                    Map<String,String> bss = readBookControl.getBookSpecStyle();
                                    bss.put("firstPageMarginButtom",c+"");
                                    readBookControl.setBookSpecStyle(bss);//存入内存
                                    changeProListener.upBookSpecStyle(bss);//存入数据库
                                }else{
                                    readBookControl.setFirstPageMarginButtom(c);
                                }

                                updateChapterStyle();

                                if (changeProListener != null) {
                                    changeProListener.refresh();
                                }
                            }
                        })
                        .create()
                        .show();

            }
        });


        flChapterSize.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                NumberPickerDialog npd = new NumberPickerDialog(getContext());

                npd.setDisplayedValues(null);
                npd.setTitle("设置章节名字体大小")
                        .setMaxValue(100)
                        .setMinValue((int) 0)
                        .setValue((int)readBookControl.getTitleSize())
                        .setListener(new NumberPickerDialog.OnClickListener(){
                            @Override
                            public void setNumber(int c) {
                                if(cbThisBook.isChecked()) {
                                    Map<String,String> bss = readBookControl.getBookSpecStyle();
                                    bss.put("titleSize",c+"");
                                    readBookControl.setBookSpecStyle(bss);//存入内存
                                    changeProListener.upBookSpecStyle(bss);//存入数据库
                                }else{
                                    readBookControl.setTitleSize(c);
                                }

                                updateChapterStyle();

                                if (changeProListener != null) {
                                    changeProListener.refresh();
                                }
                            }
                        })
                        .create()
                        .show();

            }
        });

        cbThisBook.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    //将当前字体大小，粗细，字体，边距，缩进，文字颜色，背景颜色，图片，翻页，单独存在一个map中
                    HashMap bss = combineAllStyle();
                    readBookControl.setBookSpecStyle(bss);//存入内存
                    changeProListener.upBookSpecStyle(bss);//存入具体书里

                }else{
                    //清除
                    readBookControl.setBookSpecStyle(null);//存入内存
                    changeProListener.upBookSpecStyle(null);//存入具体书里
                    changeProListener.refresh();
                }
            }
        });
    }


    public void changeNumber(int indx) {

        if(cbThisBook.isChecked()) {
            Map<String,String> bss = readBookControl.getBookSpecStyle();

            if (readBookControl.getIsNightTheme()) {
                bss.put("textDrawableIndexNight", indx+"");
            }else{
                bss.put("textDrawableIndex", indx+"");
            }
            readBookControl.setBookSpecStyle(bss);//存入内存
            readBookControl.setTextDrawableIndex1(indx);
            changeProListener.upBookSpecStyle(bss);//存入数据库
        }else {
            readBookControl.setTextDrawableIndex(indx);
        }
    }






    //设置字体
    public void setReadFonts(String path) {


        if(cbThisBook.isChecked()) {
            Map<String,String> bss = readBookControl.getBookSpecStyle();
            bss.put("fontPath", path);
            readBookControl.setBookSpecStyle(bss);//存入内存
            changeProListener.upBookSpecStyle(bss);//存入数据库
        }else {
            readBookControl.setReadBookFont(path);
        }

        changeProListener.refresh();
    }

    //设置字体
    public void setChapterReadFonts(String path) {


        if(cbThisBook.isChecked()) {
            Map<String,String> bss = readBookControl.getBookSpecStyle();
            bss.put("chapterFontPath", path);
            readBookControl.setBookSpecStyle(bss);//存入内存
            changeProListener.upBookSpecStyle(bss);//存入数据库
        }else {
            readBookControl.setChapterBookFont(path);
        }

        changeProListener.refresh();
    }

    //清除字体
    private void clearFontPath() {
        if(cbThisBook.isChecked()) {
            Map<String,String> bss = readBookControl.getBookSpecStyle();
            bss.put("fontPath", null);
            readBookControl.setBookSpecStyle(bss);//存入内存
            changeProListener.upBookSpecStyle(bss);//存入数据库
        }else {
            readBookControl.setReadBookFont(null);
        }

        changeProListener.refresh();
    }


    private void clearChapterFontPath() {
        if(cbThisBook.isChecked()) {
            Map<String,String> bss = readBookControl.getBookSpecStyle();
            bss.put("chapterFontPath", null);
            readBookControl.setBookSpecStyle(bss);//存入内存
            changeProListener.upBookSpecStyle(bss);//存入数据库
        }else {
            readBookControl.setChapterBookFont(null);
        }

        changeProListener.refresh();
    }

    private void updateBoldText(float boldSize) {
        //tvBold.setSelected(isBold);
        java.text.DecimalFormat myformat=new java.text.DecimalFormat("0.0");
        tvBoldSize.setText(myformat.format(boldSize));
    }

    private void updateStyleDefault(int styleDefault) {
        switch (styleDefault) {
            case 1:
                flSpace1.setSelected(true);
                flSpace2.setSelected(false);
                flSpace3.setSelected(false);
                flSpaceNone.setSelected(false);
                flMoreStyle.setSelected(false);
                break;
            case 2:
                flSpace1.setSelected(false);
                flSpace2.setSelected(true);
                flSpace3.setSelected(false);
                flSpaceNone.setSelected(false);
                flMoreStyle.setSelected(false);
                break;
            case 3:
                flSpace1.setSelected(false);
                flSpace2.setSelected(false);
                flSpace3.setSelected(true);
                flSpaceNone.setSelected(false);
                flMoreStyle.setSelected(false);
                break;
            case 4:
                flSpace1.setSelected(false);
                flSpace2.setSelected(false);
                flSpace3.setSelected(false);
                flSpaceNone.setSelected(true);
                flMoreStyle.setSelected(false);
                break;
            case 5:
                flSpace1.setSelected(false);
                flSpace2.setSelected(false);
                flSpace3.setSelected(false);
                flSpaceNone.setSelected(false);
                flMoreStyle.setSelected(true);
                break;
        }

        //tvBold.setSelected(styleDefault);
    }

    private void updateStyleArea(Map<String,String> m) {
        if(m!=null){
            Log.d("使用特有样式","updateStyleArea");
            cbThisBook.setChecked(true);
        }
    }

    private  void updateChapterStyle(){
        java.text.DecimalFormat myformat=new java.text.DecimalFormat("0");

        tvChapterTop.setText("上("+myformat.format(readBookControl.getFirstPageMarginTop())+")");
        tvChapterButtom.setText("下("+myformat.format(readBookControl.getFirstPageMarginButtom())+")");
        tvChapterSize.setText("字("+myformat.format(readBookControl.getTitleSize())+")");
    }

    private void updatePageMode(int pageMode) {
        switch (pageMode) {
            case 0:
                flPage1.setSelected(true);
                flPage2.setSelected(false);
                flPage3.setSelected(false);
                flMorePage.setSelected(false);
                break;
            case 1:
                flPage1.setSelected(false);
                flPage2.setSelected(true);
                flPage3.setSelected(false);
                flMorePage.setSelected(false);
                break;
            case 2:
                flPage1.setSelected(false);
                flPage2.setSelected(false);
                flPage3.setSelected(true);
                flMorePage.setSelected(false);
                break;
            case 3:
                flPage1.setSelected(false);
                flPage2.setSelected(false);
                flPage3.setSelected(false);
                flMorePage.setSelected(true);
                break;
        }
    }

    public void setBg() {
        tv0.setTextColor(readBookControl.getTextColor(0));
        tv1.setTextColor(readBookControl.getTextColor(1));
        tv2.setTextColor(readBookControl.getTextColor(2));
        tv3.setTextColor(readBookControl.getTextColor(3));

        civ0.setImageDrawable(readBookControl.getBgDrawable(0, activity, 100, 180));
        civ1.setImageDrawable(readBookControl.getBgDrawable(1, activity, 100, 180));
        civ2.setImageDrawable(readBookControl.getBgDrawable(2, activity, 100, 180));
        civ3.setImageDrawable(readBookControl.getBgDrawable(3, activity, 100, 180));
    }

    private void updateBg(int index) {

        switch (index) {
            case 0:
                flBg0.setSelected(true);
                flBg1.setSelected(false);
                flBg2.setSelected(false);
                flBg3.setSelected(false);
                break;
            case 1:
                flBg0.setSelected(false);
                flBg1.setSelected(true);
                flBg2.setSelected(false);
                flBg3.setSelected(false);
                break;
            case 2:
                flBg0.setSelected(false);
                flBg1.setSelected(false);
                flBg2.setSelected(true);
                flBg3.setSelected(false);
                break;
            case 3:
                flBg0.setSelected(false);
                flBg1.setSelected(false);
                flBg2.setSelected(false);
                flBg3.setSelected(true);
                break;

        }


        /*
        flBg0.setBorderColor(activity.getResources().getColor(R.color.tv_text_default));
        flBg2.setBorderColor(activity.getResources().getColor(R.color.tv_text_default));
        flBg2.setBorderColor(activity.getResources().getColor(R.color.tv_text_default));
        flBg2.setBorderColor(activity.getResources().getColor(R.color.tv_text_default));
        switch (index) {
            case 0:
                civBgWhite.setBorderColor(Color.parseColor("#F3B63F"));
                break;
            case 1:
                civBgYellow.setBorderColor(Color.parseColor("#F3B63F"));
                break;
            case 2:
                civBgGreen.setBorderColor(Color.parseColor("#F3B63F"));
                break;
            case 3:
                civBgBlue.setBorderColor(Color.parseColor("#F3B63F"));
                break;
            case 4:
                civBgBlack.setBorderColor(Color.parseColor("#F3B63F"));
                break;
        }
        */



    }

    private  HashMap combineAllStyle(){
        HashMap bss = new HashMap();

        bss.put("textSize",readBookControl.getTextSize()+"");
        bss.put("boldSize",readBookControl.getBoldSize()+"");
        bss.put("fontPath",readBookControl.getFontPath()+"");

        bss.put("indent",readBookControl.getIndent()+"");
        bss.put("styleDefault",readBookControl.getStyleDefault()+"");
        bss.put("lineMultiplier",readBookControl.getLineMultiplier()+"");
        bss.put("paragraphSize",readBookControl.getParagraphSize()+"");
        bss.put("paddingTop",readBookControl.getPaddingTop()+"");
        bss.put("paddingBottom",readBookControl.getPaddingBottom()+"");
        bss.put("paddingLeft",readBookControl.getPaddingLeft()+"");
        bss.put("paddingRight",readBookControl.getPaddingRight()+"");
        bss.put("tipPaddingTop",readBookControl.getTipPaddingTop()+"");
        bss.put("tipPaddingBottom",readBookControl.getTipPaddingBottom()+"");
        bss.put("tipPaddingLeft",readBookControl.getTipPaddingLeft()+"");
        bss.put("tipPaddingRight",readBookControl.getTipPaddingRight()+"");


        for(int ii =0;ii<4;ii++){
            bss.put("textColor"+ii,readBookControl.getTextColor(ii)+"");
            bss.put("bgCustom"+ii,readBookControl.getBgCustom(ii)+"");
            bss.put("bgColor"+ii,readBookControl.getBgColor(ii)+"");
            bss.put("darkStatusIcon"+ii,readBookControl.getDarkStatusIcon(ii)+"");
            bss.put("bgPath"+ii,readBookControl.getBgPath(ii)+"");
        }



        bss.put("firstPageMarginTop",readBookControl.getFirstPageMarginTop()+"");
        bss.put("firstPageMarginButtom",readBookControl.getFirstPageMarginButtom()+"");
        bss.put("titleSize",readBookControl.getTitleSize()+"");
        bss.put("chapterFontPath",readBookControl.getChapterFontPath()+"");



        return  bss;
    }

    public interface OnChangeProListener {

        void upBookSpecStyle(Map<String,String> m);

        void upPageMode();

        void upTextSize();

        void upMargin();

        void bgChange();

        void refresh();

        void  upTextSizeAndMargin();
    }
}