package com.kunfei.bookshelf.view.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.hwangjr.rxbus.RxBus;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.kunfei.basemvplib.impl.IPresenter;
import com.kunfei.bookshelf.DbHelper;
import com.kunfei.bookshelf.MApplication;
import com.kunfei.bookshelf.R;
import com.kunfei.bookshelf.base.MBaseActivity;
import com.kunfei.bookshelf.bean.BookSpecStyleBean;
import com.kunfei.bookshelf.constant.RxBusTag;
import com.kunfei.bookshelf.dao.BookSpecStyleBeanDao;
import com.kunfei.bookshelf.help.ReadBookControl;
import com.kunfei.bookshelf.utils.BitmapUtil;
import com.kunfei.bookshelf.utils.FileUtils;
import com.kunfei.bookshelf.utils.GsonUtils;
import com.kunfei.bookshelf.utils.PermissionUtils;
import com.kunfei.bookshelf.utils.bar.ImmersionBar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReadStyleActivity extends MBaseActivity implements ColorPickerDialogListener {
    private final int ResultSelectBg = 103;
    private final int SELECT_TEXT_COLOR = 201;
    private final int SELECT_BG_COLOR = 301;

    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tvSelectTextColor)
    TextView tvSelectTextColor;
    @BindView(R.id.tvSelectBgColor)
    TextView tvSelectBgColor;
    @BindView(R.id.tvSelectBgImage)
    TextView tvSelectBgImage;
    @BindView(R.id.tvDefault)
    TextView tvDefault;
    @BindView(R.id.sw_darkStatusIcon)
    Switch swDarkStatusIcon;

    private ReadBookControl readBookControl = ReadBookControl.getInstance();
    private int textDrawableIndex;
    private boolean isBSS;
    private String bookName;
    private String bookAuthor;
    private int textColor;
    private int bgColor;
    private Drawable bgDrawable;
    private int bgCustom;
    private boolean darkStatusIcon;
    private String bgPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * P层绑定   若无则返回null;
     */
    @Override
    protected IPresenter initInjector() {
        return null;
    }

    /**
     * 布局载入  setContentView()
     */
    @Override
    protected void onCreateActivity() {
        setContentView(R.layout.activity_read_style);
        ButterKnife.bind(this);
        llContent.setPadding(0, ImmersionBar.getStatusBarHeight(this), 0, 0);
        this.setSupportActionBar(toolbar);
        setupActionBar();
        setTextKind(readBookControl);
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isImmersionBarEnabled()) {
            mImmersionBar.statusBarDarkFont(false);
        } else if (darkStatusIcon) {
            mImmersionBar.statusBarDarkFont(true);
        } else {
            mImmersionBar.statusBarDarkFont(false);
        }
        mImmersionBar.init();
    }

    /**
     * 数据初始化
     */
    @Override
    protected void initData() {
        Intent intent = getIntent();
        textDrawableIndex = intent.getIntExtra("index", 1);
        isBSS =  intent.getBooleanExtra("isBSS",false);
        bookName =  intent.getStringExtra("bookName");
        bookAuthor =  intent.getStringExtra("bookAuthor");
        bgCustom = readBookControl.getBgCustom(textDrawableIndex);
        textColor = readBookControl.getTextColor(textDrawableIndex);
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        bgDrawable = readBookControl.getBgDrawable(textDrawableIndex, getContext(), width, height);
        bgColor = readBookControl.getBgColor(textDrawableIndex);
        darkStatusIcon = readBookControl.getDarkStatusIcon(textDrawableIndex);
        bgPath = readBookControl.getBgPath(textDrawableIndex);
        upText();
        upBg();
    }

    /**
     * 事件触发绑定
     */
    @Override
    protected void bindEvent() {
        swDarkStatusIcon.setChecked(darkStatusIcon);
        swDarkStatusIcon.setOnCheckedChangeListener((compoundButton, b) -> {
            darkStatusIcon = b;
            initImmersionBar();
        });
        //选择文字颜色
        tvSelectTextColor.setOnClickListener(view ->
                ColorPickerDialog.newBuilder()
                        .setColor(textColor)
                        .setShowAlphaSlider(false)
                        .setDialogType(ColorPickerDialog.TYPE_CUSTOM)
                        .setDialogId(SELECT_TEXT_COLOR)
                        .show(ReadStyleActivity.this));
        //选择背景颜色
        tvSelectBgColor.setOnClickListener(view ->
                ColorPickerDialog.newBuilder()
                        .setColor(bgColor)
                        .setShowAlphaSlider(false)
                        .setDialogType(ColorPickerDialog.TYPE_CUSTOM)
                        .setDialogId(SELECT_BG_COLOR)
                        .show(ReadStyleActivity.this));
        //选择背景图片
        tvSelectBgImage.setOnClickListener(view ->
                PermissionUtils
                        .checkMorePermissions(ReadStyleActivity.this,
                                MApplication.PerList,
                                new PermissionUtils.PermissionCheckCallback() {
                                    @Override
                                    public void onHasPermission() {
                                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                                        intent.setType("image/*");
                                        startActivityForResult(intent, ResultSelectBg);
                                    }

                                    @Override
                                    public void onUserHasAlreadyTurnedDown(String... permission) {
                                        ReadStyleActivity.this.toast(R.string.bg_image_per);
                                    }

                                    @Override
                                    public void onAlreadyTurnedDownAndNoAsk(String... permission) {
                                        PermissionUtils.requestMorePermissions(ReadStyleActivity.this, MApplication.PerList, MApplication.RESULT__PERMS);
                                    }
                                }));
        //恢复默认
        tvDefault.setOnClickListener(view -> {
            bgCustom = 0;
            textColor = readBookControl.getDefaultTextColor(textDrawableIndex);
            bgDrawable = readBookControl.getDefaultBgDrawable(textDrawableIndex, this);
            upText();
            upBg();
        });
    }

    //设置ToolBar
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.read_style);
        }
    }

    // 添加菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_read_style_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //菜单
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_save:
                saveStyle();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 保存配置
     */
    private void saveStyle() {

        if(isBSS){
            Map<String,String> bss = readBookControl.getBookSpecStyle();
            bss.put("textColor"+textDrawableIndex,textColor+"");
            bss.put("bgCustom"+textDrawableIndex,bgCustom+"");
            bss.put("bgColor"+textDrawableIndex,bgColor+"");
            bss.put("darkStatusIcon"+textDrawableIndex,darkStatusIcon+"");
            if (bgCustom == 2) {
                bss.put("bgPath"+textDrawableIndex,bgPath);

            }

            readBookControl.setBookSpecStyle(bss);//存入内存
            //changeProListener.upBookSpecStyle(bss);//存入数据库
//            RxBus.get().post(RxBusTag.UPDATE_BOOK_SPEC_STYLE, bss);
            saveBookSpecStyle(bss);

        }else {

            readBookControl.setTextColor(textDrawableIndex, textColor);
            readBookControl.setBgCustom(textDrawableIndex, bgCustom);
            readBookControl.setBgColor(textDrawableIndex, bgColor);
            readBookControl.setDarkStatusIcon(textDrawableIndex, darkStatusIcon);
            if (bgCustom == 2) {
                readBookControl.setBgPath(textDrawableIndex, bgPath);
            }
        }

        readBookControl.initTextDrawableIndex();
        RxBus.get().post(RxBusTag.UPDATE_READ, false);
        finish();
    }

    private void setTextKind(ReadBookControl readBookControl) {
        tvContent.setTextSize(readBookControl.getTextSize());
    }


    public BookSpecStyleBean getBookSpecStyle(String bookName,String bookAuthor) {

        List<BookSpecStyleBean> list = DbHelper.getDaoSession().getBookSpecStyleBeanDao().queryBuilder()
                .where(BookSpecStyleBeanDao.Properties.BookName.eq(bookName))
                .where(BookSpecStyleBeanDao.Properties.BookAuthor.eq(bookAuthor))
                .limit(1)
                .list();

        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public void saveBookSpecStyle( Map<String,String> bookSpecStyle) {

        //查询是否存在特定样式
        BookSpecStyleBean m = getBookSpecStyle(bookName, bookAuthor);

        if(bookSpecStyle!=null) {
            if (m != null) {//存在则修改

                m.setStyleJson(GsonUtils.toJsonWithSerializeNulls(bookSpecStyle));
                DbHelper.getDaoSession().getBookSpecStyleBeanDao().update(m);

            } else {

                BookSpecStyleBean bbs = new BookSpecStyleBean();
                bbs.setBookName(bookName);//书名
                bbs.setBookAuthor(bookAuthor);//作者
                bbs.setStyleJson(GsonUtils.toJsonWithSerializeNulls(bookSpecStyle));


                DbHelper.getDaoSession().getBookSpecStyleBeanDao().insertOrReplace(bbs);
            }
        }else{
            //取消特定样式
            DbHelper.getDaoSession().getBookSpecStyleBeanDao().delete(m);
        }




    }

    private void upText() {
        tvContent.setTextColor(textColor);
    }

    private void upBg() {
        llContent.setBackground(bgDrawable);
    }

    /**
     * 自定义背景
     */
    public void setCustomBg(Uri uri) {
        try {
            bgPath = FileUtils.getPath(this, uri);
            Resources resources = this.getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            int width = dm.widthPixels;
            int height = dm.heightPixels;
            Bitmap bitmap = BitmapUtil.getFitSampleBitmap(bgPath, width, height);
            bgCustom = 2;
            bgDrawable = new BitmapDrawable(getResources(), bitmap);
            upBg();
        } catch (Exception e) {
            e.printStackTrace();
            toast(e.getMessage(), ERROR);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ResultSelectBg) {
            if (resultCode == RESULT_OK && null != data) {
                setCustomBg(data.getData());
            }
        }
    }

    /**
     * Callback that is invoked when a color is selected from the color picker dialog.
     *
     * @param dialogId The dialog id used to create the dialog instance.
     * @param color    The selected color
     */
    @Override
    public void onColorSelected(int dialogId, int color) {
        switch (dialogId) {
            case SELECT_TEXT_COLOR:
                textColor = color;
                upText();
                break;
            case SELECT_BG_COLOR:
                bgCustom = 1;
                bgColor = color;
                bgDrawable = new ColorDrawable(bgColor);
                upBg();
        }
    }

    /**
     * Callback that is invoked when the color picker dialog was dismissed.
     *
     * @param dialogId The dialog id used to create the dialog instance.
     */
    @Override
    public void onDialogDismissed(int dialogId) {

    }
}
