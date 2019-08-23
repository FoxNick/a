//Copyright (c) 2017. 章钦豪. All rights reserved.
package com.kunfei.bookshelf.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.arlib.floatingsearchview.util.Util;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.hwangjr.rxbus.RxBus;
import com.kunfei.basemvplib.BitIntentDataManager;
import com.kunfei.bookshelf.DbHelper;
import com.kunfei.bookshelf.MApplication;
import com.kunfei.bookshelf.R;
import com.kunfei.bookshelf.base.BaseTabActivity;
import com.kunfei.bookshelf.bean.BookShelfBean;
import com.kunfei.bookshelf.bean.MyFindKindGroupBean;
import com.kunfei.bookshelf.bean.SearchHistoryBean;
import com.kunfei.bookshelf.constant.RxBusTag;
import com.kunfei.bookshelf.data.ColorSuggestion;
import com.kunfei.bookshelf.data.DataHelper;
import com.kunfei.bookshelf.help.BookshelfHelp;
import com.kunfei.bookshelf.help.ChapterContentHelp;
import com.kunfei.bookshelf.help.DataBackup;
import com.kunfei.bookshelf.help.ReadBookControl;
import com.kunfei.bookshelf.model.UpLastChapterModel;
import com.kunfei.bookshelf.presenter.MyMainPresenter;
import com.kunfei.bookshelf.presenter.ReadBookPresenter;
import com.kunfei.bookshelf.presenter.contract.MyMainContract;
import com.kunfei.bookshelf.utils.ACache;
import com.kunfei.bookshelf.utils.PermissionUtils;
import com.kunfei.bookshelf.utils.ScreenUtils;
import com.kunfei.bookshelf.utils.StringUtils;
import com.kunfei.bookshelf.utils.theme.ATH;
import com.kunfei.bookshelf.view.adapter.FindFlowAdapter;
import com.kunfei.bookshelf.view.fragment.MyBookListFragment;
import com.kunfei.bookshelf.view.fragment.MyFindBookFragment;
//import com.kunfei.bookshelf.view.fragment.SearchBookFragment;
import com.kunfei.bookshelf.view.fragment.MySearchBookFragment;
import com.kunfei.bookshelf.widget.flowlayout.TagView;
import com.kunfei.bookshelf.widget.modialog.InputDialog;
import com.kunfei.bookshelf.widget.modialog.MoDialogHUD;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.kunfei.bookshelf.utils.NetworkUtils.isNetWorkAvailable;


public class MyMainActivity extends BaseTabActivity<MyMainContract.Presenter> implements  MyMainContract.View, AppBarLayout.OnOffsetChangedListener, MyBookListFragment.CallBackValue, MyFindBookFragment.CallBackValue{

    @BindView(R.id.floating_search_view)
    FloatingSearchView mSearchView;
    @BindView(R.id.appbar)
    AppBarLayout mAppBar;
    //@BindView(R.id.drawer_layout)
    //DrawerLayout drawer;
    //@BindView(R.id.nav_view)
    // NavigationView navigationView;
    private Drawer mDrawer;
    private AccountHeader mAccountHeader;

    CoordinatorLayout aa;
    private Switch swNightTheme;

    private Handler handler = new Handler();


    private final String TAG = "BlankFragment";

    public static final long FIND_SUGGESTION_SIMULATED_DELAY = 250;

    private boolean mIsDarkSearchTheme = false;

    private final static int DRAG_SENSITIVITY = ScreenUtils.dpToPx(32);

    //private String mLastQuery = "";

    private int suggestionCount = 10;

    private boolean viewIsList;//书架样式
    private boolean findIsFlow;//发现样式
    private boolean openBookHiddenFunction;//是否开启书籍隐藏功能

    private static final int BACKUP_RESULT = 11;
    private static final int RESTORE_RESULT = 12;
    private static final int FILE_SELECT_RESULT = 13;
    private final int requestSource = 14;

    private static String[] mTitles = new String[]{"书架", "搜索列表" ,"发现"};

    private int group;
    private boolean resumed = false;
    private MoDialogHUD moDialogHUD;

    private long exitTime = 0;

    private ActionBarDrawerToggle mDrawerToggle;

    //private boolean isChangeTheme = false;


    private String mLastQuery = "";

    final static int COUNTS = 2;// 点击次数
    final static long DURATION = 1000;// 规定有效时间
    long[] mHits = new long[COUNTS];



    private List<String> popupMenuItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // 避免从桌面启动程序后，会重新实例化入口类的activity
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }



        if (!MApplication.getInstance().isChangeTheme() && preferences.getBoolean(getString(R.string.pk_default_read), false)) {//第一次运行且设置了自动打开最近阅读
            MApplication.getInstance().setChangeTheme(false);
            startReadActivity();
        }
        MApplication.getInstance().setChangeTheme(false);

        // if (savedInstanceState != null) {
        //    resumed = savedInstanceState.getBoolean("resumed");
        //}
        group = preferences.getInt("bookshelfGroup", 0);
        //super.onCreate(savedInstanceState);

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // outState.putBoolean("resumed", resumed);
    }

    private void startReadActivity() {
        Intent intent = new Intent(this, MyReadBookActivity.class);
        intent.putExtra("openFrom", ReadBookPresenter.OPEN_FROM_APP);
        startActivity(intent);
    }

    @Override
    protected MyMainContract.Presenter initInjector() {
        return new MyMainPresenter();
    }


    @Override
    protected void onCreateActivity() {

        setContentView(R.layout.activity_eink_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {

        mPresenter.querySearchHistory("");

        viewIsList = preferences.getBoolean("bookshelfIsList", false);
        findIsFlow = preferences.getBoolean("findIsFlow", false);
        openBookHiddenFunction = preferences.getBoolean("openBookHiddenFunction", false);

        popupMenuItemList = new ArrayList<>();

        popupMenuItemList.add("复制");
        popupMenuItemList.add("设为固定格言");
        popupMenuItemList.add("设为网络格言");


        flowAdapter = new FindFlowAdapter();

    }

    @Override
    public void reloadSearchHistory(){
        mPresenter.querySearchHistory("");
    }

    @Override
    protected void bindView() {

        super.bindView();


        mAppBar.addOnOffsetChangedListener(this);

        setupSearchBar();//todo 设置搜索框的各种事件，历史等
        //setUpNavigationView();//todo 以后可以自定义图片，和文字，个性化
        initDrawer();
        initTabLayout();
        upGroup(group);
        moDialogHUD = new MoDialogHUD(this);


        //preferences.getBoolean("bookshelfIsList", false);

        //setFloatingSearchViewNightTheme(isNightTheme());

        updateUI();




    }






    private void changeTabSelect(TabLayout.Tab tab) {
        View view = tab.getCustomView();

        TextView txt_title = view.findViewById(R.id.tabtext);

        txt_title.setTextColor(getResources().getColor(R.color.selectTab));

    }

    private void changeTabNormal(TabLayout.Tab tab) {
        View view = tab.getCustomView();

        TextView txt_title = view.findViewById(R.id.tabtext);

        txt_title.setTextColor(getResources().getColor(R.color.noSelectTab));

    }


    public  void kindSearch(String url, String tag, MyFindKindGroupBean findKindGroupBean){
        mVp.setCurrentItem(1);
        mTlIndicator.getTabAt(1).select();

        String ttag = "android:switcher:"+mVp.getId()+":"+mVp.getCurrentItem();

        FragmentManager fm = getSupportFragmentManager();
        //todo
        MySearchBookFragment fragment = (MySearchBookFragment)fm.findFragmentByTag(ttag);
        fragment.kindSearch(url,tag,findKindGroupBean);

    }


    @Override
    public  void authorSearch(String author){
        mVp.setCurrentItem(1);
        mTlIndicator.getTabAt(1).select();

        String ttag = "android:switcher:"+mVp.getId()+":"+mVp.getCurrentItem();

        FragmentManager fm = getSupportFragmentManager();
        MySearchBookFragment fragment = (MySearchBookFragment)fm.findFragmentByTag(ttag);

        mSearchView.setSearchText(author);

        fragment.setSearchKey(author);
        fragment.setSearchAuthor(author);
        fragment.authorSearch();

    }


    @Override
    public  void keyWordSearch(String keyWord){
        mVp.setCurrentItem(1);
        mTlIndicator.getTabAt(1).select();

        String ttag = "android:switcher:"+mVp.getId()+":"+mVp.getCurrentItem();

        FragmentManager fm = getSupportFragmentManager();
        MySearchBookFragment fragment = (MySearchBookFragment)fm.findFragmentByTag(ttag);

        mSearchView.setSearchText(keyWord);

        fragment.setSearchKey(keyWord);
        fragment.toSearch();

    }


    @Override
    protected List<Fragment> createTabFragments() {

        /*
        MyBookListFragment bookListFragment = new MyBookListFragment();
        MySearchBookFragment searchBookFragment = new MySearchBookFragment();
        MyFindBookFragment findBookFragment = new MyFindBookFragment();

    */


        MyBookListFragment bookListFragment = null;
        MySearchBookFragment searchBookFragment = null;
        MyFindBookFragment findBookFragment = null;

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof MyBookListFragment) {
                bookListFragment = (MyBookListFragment) fragment;
            } else if (fragment instanceof MySearchBookFragment) {
                searchBookFragment = (MySearchBookFragment) fragment;
            }else if (fragment instanceof MyFindBookFragment) {
                findBookFragment = (MyFindBookFragment) fragment;
            }
        }
        if (bookListFragment == null)
            bookListFragment = new MyBookListFragment();
        if (searchBookFragment == null)
            searchBookFragment = new MySearchBookFragment();
        if (findBookFragment == null)
            findBookFragment = new MyFindBookFragment();
        return Arrays.asList(bookListFragment, searchBookFragment, findBookFragment);

    }

    private PopupWindow popupWindow;
    private FindFlowAdapter flowAdapter;


    //初始化TabLayout和ViewPager
    private void initTabLayout() {
        //TabLayout使用自定义Item
        for (int i = 0; i < mTlIndicator.getTabCount(); i++) {
            TabLayout.Tab tab = mTlIndicator.getTabAt(i);
            if (tab == null) return;
            if (i == 0) { //设置第一个Item的点击事件(当下标为0时触发)
                tab.setCustomView(tab_icon(mTitles[i], R.drawable.ic_arrow_drop_down));
                View view1 = tab.getCustomView();

                TextView txt_title = view1.findViewById(R.id.tabtext);

                txt_title.setTextColor(getResources().getColor(R.color.selectTab));

                View tabView = (View) Objects.requireNonNull(tab.getCustomView()).getParent();
                tabView.setTag(i);
                tabView.setOnClickListener(view -> {
                    if (tabView.isSelected()) {
                        showBookGroupMenu(view);
                    }
                });
            } else {
                tab.setCustomView(tab_icon(mTitles[i], null));
                View view1 = tab.getCustomView();
                TextView txt_title = view1.findViewById(R.id.tabtext);
                txt_title.setTextColor(getResources().getColor(R.color.noSelectTab));
            }

            View tabView = (View) Objects.requireNonNull(tab.getCustomView()).getParent();
            tabView.setOnLongClickListener(view -> {
                //continuousClick(COUNTS, DURATION);

                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("openBookHiddenFunction", !openBookHiddenFunction);
                editor.apply();
                openBookHiddenFunction = !openBookHiddenFunction;

                String aaa = "只显示未隐藏的书籍。";
                if(!openBookHiddenFunction){
                    aaa = "显示所有书籍。";
                }


                mPresenter.setHiddenMode(openBookHiddenFunction);//触发列表刷新

                Toast.makeText(this, aaa, Toast.LENGTH_SHORT).show();

                return true;
            });


            if (i == 2 ) {
                tabView.setOnClickListener(view -> {
                    if( !preferences.getBoolean("findIsFlow", false) && tabView.isSelected()) {
                        FragmentManager fm = getSupportFragmentManager();
                        MyFindBookFragment ff = (MyFindBookFragment) fm.getFragments().get(2);
                        ff.taggleBookSource();

                        /*
                        final BubbleLayout bubbleLayout = (BubbleLayout) LayoutInflater.from(this).inflate(R.layout.my_pop_kind, null);
                        popupWindow = BubblePopupHelper.create(this, bubbleLayout);
                        int[] location = new int[2];
                        view.getLocationInWindow(location);

                        bubbleLayout.setArrowDirection(ArrowDirection.TOP);


                        //int[] location = new int[2];
                        //mTlIndicator.getLocationInWindow(location);

                        //赋值
                        TagFlowLayout tflFind = bubbleLayout.findViewById(R.id.tfl_find);
                        tflFind.setAdapter(flowAdapter);
                        flowAdapter.replaceAll(ff.getAllGroup());


                        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1]);
                        popupWindow.setHeight(500);
                        */
                    }


                });
            }
        }


        mTlIndicator.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                changeTabSelect(tab);

                //设置右边三点的菜单
                if(tab.getPosition()==0){

                    mSearchView.inflateOverflowMenu(R.menu.menu_main_bookshelf);
                }
                if(tab.getPosition()==1){
                    mSearchView.inflateOverflowMenu(R.menu.menu_main_search);
                }
                if(tab.getPosition()==2){
                    mSearchView.inflateOverflowMenu(R.menu.menu_main_find);
                }

                //setFloatingSearchMenuIcon();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                changeTabNormal(tab);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });
    }


    private void initDrawer() {

        mAccountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withDividerBelowHeader(false)
                .withCompactStyle(true)
                .withSelectionListEnabled(false)
                .build();


        ArrayList<IDrawerItem> drawerItems = new ArrayList<>();
        drawerItems.add(new PrimaryDrawerItem().withIdentifier(1L).withName(R.string.book_source_manage).withIcon(R.drawable.ic_book_source_manage).withSelectable(false));
        drawerItems.add(new PrimaryDrawerItem().withIdentifier(2L).withName(R.string.replace_rule_title).withIcon(R.drawable.ic_find_replace).withSelectable(false));
        drawerItems.add(new PrimaryDrawerItem().withIdentifier(3L).withName(R.string.action_download).withIcon(R.drawable.ic_download_line).withSelectable(false));
        drawerItems.add(new DividerDrawerItem());
        drawerItems.add(new PrimaryDrawerItem().withIdentifier(4L).withName(R.string.setting).withIcon(R.drawable.ic_settings_black_24dp).withSelectable(false));
        drawerItems.add(new DividerDrawerItem());
        drawerItems.add(new PrimaryDrawerItem().withIdentifier(5L).withName(R.string.backup).withIcon(R.drawable.ic_backup).withSelectable(false));
        drawerItems.add(new PrimaryDrawerItem().withIdentifier(6L).withName(R.string.restore).withIcon(R.drawable.ic_restore).withSelectable(false));
        //drawerItems.add(new DividerDrawerItem());
        //drawerItems.add(new PrimaryDrawerItem().withIdentifier(7L).withName(R.string.lab).withIcon(R.drawable.ic__lab).withSelectable(false));
        //drawerItems.add(new PrimaryDrawerItem().withIdentifier(8L).withName(R.string.simple_portal).withIcon(R.drawable.ic_simple_portal).withSelectable(false));


        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(mAccountHeader)
                .withTranslucentStatusBar(true)
                .withDrawerItems(drawerItems)
                .withStickyFooterDivider(false)
                .withStickyFooterShadow(false)
                .withOnDrawerItemClickListener(new DrawerItemClickListener())
                .build();

        mDrawer.getRecyclerView().setVerticalScrollBarEnabled(false);

        mDrawer.deselect();
    }

    private class DrawerItemClickListener implements Drawer.OnDrawerItemClickListener {
        @Override
        public boolean onItemClick(View view, int position, IDrawerItem iDrawerItem) {

           // if (iDrawerItem.getIdentifier() == Constants.DRAWER_NO_ACTION)
          //      return false;

            switch ((int) iDrawerItem.getIdentifier()) {
                case 1:
                    handler.postDelayed(() -> BookSourceActivity.startThis(MyMainActivity.this,requestSource), 200);
                    break;
                case 2:
                    handler.postDelayed(() -> ReplaceRuleActivity.startThis(getContext(),null), 200);
                    break;
                case 3:
                    handler.postDelayed(() -> DownloadActivity.startThis(MyMainActivity.this), 200);
                    break;
                case 4:
                    handler.postDelayed(() -> MySettingActivity.startThis(MyMainActivity.this), 200);
                    break;
                case 5:
                    handler.postDelayed(MyMainActivity.this::backup, 200);
                    break;
                case 6:
                    handler.postDelayed(MyMainActivity.this::restore, 200);
                    break;

            }

            return false;
        }

    }


    private float mStartX;
    private float mStartY;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mStartX = ev.getX();
                mStartY = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                float deltaX = ev.getX() - mStartX;
                float deltaY = Math.abs(ev.getY() - mStartY);

                if (deltaX >= DRAG_SENSITIVITY && deltaY < 0.5 * deltaX && mTlIndicator.getSelectedTabPosition()==0) {
                    if (!mDrawer.isDrawerOpen()) {
                        mDrawer.openDrawer();
                    }
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void continuousClick(int count, long time) {
        //每次点击时，数组向前移动一位
        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
        //为数组最后一位赋值
        mHits[mHits.length - 1] = SystemClock.uptimeMillis();
        if (mHits[0] >= (SystemClock.uptimeMillis() - DURATION)) {
            mHits = new long[COUNTS];//重新初始化数组


            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("openBookHiddenFunction", !openBookHiddenFunction);
            editor.apply();
            openBookHiddenFunction = !openBookHiddenFunction;

            String aaa = "连续点击了2次,开始书籍隐藏模式，长按书籍详情里的书名，隐藏书籍。";
            if(!openBookHiddenFunction){
                aaa = "连续点击了2次,关闭书籍隐藏模式,显示所有书籍。";
            }


            mPresenter.setHiddenMode(openBookHiddenFunction);//触发列表刷新

            Toast.makeText(this, aaa, Toast.LENGTH_LONG).show();
        }
    }




    @Override
    public int getGroup() {
        return group;
    }

    /**
     * 显示分组菜单
     */
    private void showBookGroupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        for (int j = 0; j < getResources().getStringArray(R.array.book_group_array).length; j++) {
            popupMenu.getMenu().add(0, 0, j, getResources().getStringArray(R.array.book_group_array)[j]);
        }

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            upGroup(menuItem.getOrder());
            return true;
        });
        popupMenu.setOnDismissListener(popupMenu1 -> updateTabItemIcon(false));
        popupMenu.show();
        updateTabItemIcon(true);
    }

    private void updateTabItemIcon(boolean showMenu) {
        TabLayout.Tab tab = mTlIndicator.getTabAt(0);
        if (tab == null) return;
        View customView = tab.getCustomView();
        if (customView == null) return;
        ImageView im = customView.findViewById(R.id.tabicon);
        if (showMenu) {
            im.setImageResource(R.drawable.ic_arrow_drop_up);
        } else {
            im.setImageResource(R.drawable.ic_arrow_drop_down);
        }
    }

    private void upGroup(int group) {
        if (this.group != group) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("bookshelfGroup", group);
            editor.apply();
        }
        this.group = group;
        RxBus.get().post(RxBusTag.UPDATE_GROUP, group);
        RxBus.get().post(RxBusTag.REFRESH_BOOK_LIST, false);
        //更换Tab文字
        updateTabItemText(group);

    }




    private void updateTabItemText(int group) {
        TabLayout.Tab tab = mTlIndicator.getTabAt(0);
        if (tab == null) return;
        View customView = tab.getCustomView();
        if (customView == null) return;
        TextView tv = customView.findViewById(R.id.tabtext);
        tv.setText(getResources().getStringArray(R.array.book_group_array)[group]);
    }

    private View tab_icon(String name, Integer iconID) {
        @SuppressLint("InflateParams")
        View tabView = LayoutInflater.from(this).inflate(R.layout.tab_view_icon_right, null);
        TextView tv = tabView.findViewById(R.id.tabtext);
        //tv.setHeight(14);
        tv.setText(name);
        tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
        ImageView im = tabView.findViewById(R.id.tabicon);
        if (iconID != null) {
            im.setVisibility(View.VISIBLE);
            im.setImageResource(iconID);
        } else {
            im.setVisibility(View.GONE);
        }
        return tabView;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // 这个必须要，没有的话进去的默认是个箭头。。正常应该是三横杠的
        if (swNightTheme != null) {
            swNightTheme.setChecked(isNightTheme());
        }
    }

    /**
     * 沉浸状态栏
     */
    @Override
    public void initImmersionBar() {
        super.initImmersionBar();
    }

    @Override
    protected List<String> createTabTitles() {
        return Arrays.asList(mTitles);
    }

    public static void startThis(Context context) {
        context.startActivity(new Intent(context, MyMainActivity.class));
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        mSearchView.setTranslationY(verticalOffset);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "start onResume~~~");



        setFloatingSearchViewNightTheme(isNightTheme());


    }

    private void setFloatingSearchViewNightTheme(Boolean isNightTheme){

        if(isNightTheme) {
            mIsDarkSearchTheme = true;
            mSearchView.setBackgroundColor(Color.parseColor("#282828"));
            mSearchView.setViewTextColor(Color.parseColor("#e9e9e9"));
            mSearchView.setHintTextColor(Color.parseColor("#e9e9e9"));
            mSearchView.setActionMenuOverflowColor(Color.parseColor("#e9e9e9"));
            mSearchView.setMenuItemIconColor(Color.parseColor("#e9e9e9"));
            mSearchView.setLeftActionIconColor(Color.parseColor("#e9e9e9"));
            mSearchView.setClearBtnColor(Color.parseColor("#e9e9e9"));
            mSearchView.setDividerColor(Color.parseColor("#BEBEBE"));
        }
        else{
            mIsDarkSearchTheme = false;
            mSearchView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            mSearchView.setViewTextColor(Color.parseColor("#2c2a2a"));//
            mSearchView.setHintTextColor(Color.parseColor("#363434"));
            mSearchView.setActionMenuOverflowColor(Color.parseColor("#2f2828"));
            mSearchView.setMenuItemIconColor(Color.parseColor("#2f2828"));
            mSearchView.setLeftActionIconColor(Color.parseColor("#2f2828"));
            mSearchView.setClearBtnColor(Color.parseColor("#2f2828"));
            mSearchView.setDividerColor(Color.parseColor("#2f2828"));
        }
    }






    /**
     * 备份
     */
    private void backup() {
        PermissionUtils.checkMorePermissions(this, MApplication.PerList, new PermissionUtils.PermissionCheckCallback() {
            @Override
            public void onHasPermission() {
                AlertDialog alertDialog = new AlertDialog.Builder(MyMainActivity.this)
                        .setTitle(R.string.backup_confirmation)
                        .setMessage(R.string.backup_message)
                        .setPositiveButton(R.string.ok, (dialog, which) -> mPresenter.backupData())
                        .setNegativeButton(R.string.cancel, null)
                        .show();
                ATH.setAlertDialogTint(alertDialog);
            }

            @Override
            public void onUserHasAlreadyTurnedDown(String... permission) {
                MyMainActivity.this.toast(R.string.backup_permission);
            }

            @Override
            public void onAlreadyTurnedDownAndNoAsk(String... permission) {
                MyMainActivity.this.toast(R.string.backup_permission);
                PermissionUtils.requestMorePermissions(MyMainActivity.this, permission, BACKUP_RESULT);
            }
        });
    }

    /**
     * 恢复
     */
    private void restore() {
        PermissionUtils.checkMorePermissions(this, MApplication.PerList, new PermissionUtils.PermissionCheckCallback() {
            @Override
            public void onHasPermission() {
                AlertDialog alertDialog = new AlertDialog.Builder(MyMainActivity.this)
                        .setTitle(R.string.restore_confirmation)
                        .setMessage(R.string.restore_message)
                        .setPositiveButton(R.string.ok, (dialog, which) -> mPresenter.restoreData())
                        .setNegativeButton(R.string.cancel, null)
                        .show();
                ATH.setAlertDialogTint(alertDialog);
            }

            @Override
            public void onUserHasAlreadyTurnedDown(String... permission) {
                MyMainActivity.this.toast(R.string.restore_permission);
            }

            @Override
            public void onAlreadyTurnedDownAndNoAsk(String... permission) {
                PermissionUtils.requestMorePermissions(MyMainActivity.this, permission, RESTORE_RESULT);
            }
        });
    }


    private void setupSearchBar() {

        //左边按钮点击
        mSearchView.setOnLeftMenuClickListener(new FloatingSearchView.OnLeftMenuClickListener() {
            @Override
            public void onMenuOpened() {
                //打开抽屉侧滑菜单
                if (mDrawer.isDrawerOpen())
                    mDrawer.closeDrawer();
                else
                    mDrawer.openDrawer();

                mSearchView.closeMenu(true);

            }

            @Override
            public void onMenuClosed() {
                //Toast.makeText(MainActivity.this, "ddddd", Toast.LENGTH_SHORT).show();
            }
        });

        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {

            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {



                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mSearchView.swapSuggestions(DataHelper.getHistory(getContext(), suggestionCount));
                } else {

                    //this shows the top left circular progress
                    //you can call it where ever you want, but
                    //it makes sense to do it when loading something in
                    //the background.
                    mSearchView.showProgress();

                    //simulates a query call to a data source
                    //with a new query.
                    DataHelper.findSuggestions(getContext(), newQuery, 5,
                            FIND_SUGGESTION_SIMULATED_DELAY, new DataHelper.OnFindSuggestionsListener() {

                                @Override
                                public void onResults(List<ColorSuggestion> results) {

                                    //this will swap the data and
                                    //render the collapse/expand animations as necessary
                                    mSearchView.swapSuggestions(results);

                                    //let the users know that the background
                                    //process has completed
                                    mSearchView.hideProgress();
                                }
                            });
                }


                Log.d(TAG, "onSearchTextChanged()");
            }
        });

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {

                mLastQuery = searchSuggestion.getBody();

                //mSearchView.setSearchText(mLastQuery);
                mSearchView.clearFocus();  //可以收起键盘
                mSearchView.clearSuggestions();

                ColorSuggestion colorSuggestion = (ColorSuggestion) searchSuggestion;

                if(colorSuggestion.ismIsShelf()){

                    // BookShelfBean bookShelfBean = bookShelfAdapter.getBooks().get(index);
                    BookShelfBean bookShelfBean = BookshelfHelp.getBook(colorSuggestion.getmNoteUrl());

                    bookShelfBean.setHasUpdate(false);
                    DbHelper.getDaoSession().getBookShelfBeanDao().insertOrReplace(bookShelfBean);
                    Intent intent = new Intent(getContext(), MyReadBookActivity.class);
                    intent.putExtra("openFrom", ReadBookPresenter.OPEN_FROM_APP);
                    String key = String.valueOf(System.currentTimeMillis());
                    intent.putExtra("bookKey", key);
                    try {
                        BitIntentDataManager.getInstance().putData(key, bookShelfBean.clone());
                    } catch (Exception e) {
                        BitIntentDataManager.getInstance().putData(key, bookShelfBean);
                        e.printStackTrace();
                    }
                    startActivityByAnim(intent, android.R.anim.fade_in, android.R.anim.fade_out);

                }else if(colorSuggestion.ismIsFind()){

                    mVp.setCurrentItem(2);
                    mTlIndicator.getTabAt(2).select();

                    String ttag = "android:switcher:"+mVp.getId()+":"+mVp.getCurrentItem();

                    FragmentManager fm = getSupportFragmentManager();
                    MyFindBookFragment fragment = (MyFindBookFragment)fm.findFragmentByTag(ttag);
                    for(int p = 0 ;p < fragment.getTflFind().getChildCount(); p++){

                        TagView tv = (TagView)fragment.getTflFind().getChildAt(p);
                        if(tv.findViewWithTag(colorSuggestion.getmTag())!=null){
                            fragment.newShowBookSource();//先显示源，再点击
                            tv.findViewWithTag(colorSuggestion.getmTag()).performClick();
                        }
                    }


                }else{
                    mVp.setCurrentItem(1);
                    mTlIndicator.getTabAt(1).select();

                    String ttag = "android:switcher:"+mVp.getId()+":"+mVp.getCurrentItem();

                    FragmentManager fm = getSupportFragmentManager();
                    MySearchBookFragment fragment = (MySearchBookFragment)fm.findFragmentByTag(ttag);
                    fragment.setSearchKey(colorSuggestion.getBody().trim());
                    fragment.toSearch();
                }




                /*
                DataHelper.findColors(getContext(), colorSuggestion.getBody(),
                        new DataHelper.OnFindColorsListener() {

                            @Override
                            public void onResults(List<ColorWrapper> results) {
                                //show search results
                            }

                        });
                        */
                Log.d(TAG, "onSuggestionClicked()");

                //mLastQuery = searchSuggestion.getBody();
            }

            @Override
            public void onSearchAction(String query) {

                mLastQuery = query;


                mVp.setCurrentItem(1);
                mTlIndicator.getTabAt(1).select();

                String ttag = "android:switcher:"+mVp.getId()+":"+mVp.getCurrentItem();

                FragmentManager fm = getSupportFragmentManager();
                MySearchBookFragment fragment = (MySearchBookFragment)fm.findFragmentByTag(ttag);
                fragment.setSearchKey(query.trim());
                fragment.toSearch();

                Log.d(TAG, "onSearchAction()");


            }

            @Override
            public void onSuggestionDeleteClicked(SearchSuggestion searchSuggestion) {


                mPresenter.cleanSearchHistory(searchSuggestion.getBody());


            }
        });

        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                //show suggestions when search bar gains focus (typically history suggestions)
                mSearchView.swapSuggestions(DataHelper.getHistory(getContext(), suggestionCount));

                Log.d(TAG, "onFocus()");
            }

            @Override
            public void onFocusCleared() {

                //set the title of the bar so that when focus is returned a new query begins
                //mSearchView.setSearchBarTitle(getString(R.string.app_name));
                mSearchView.setSearchBarTitle(mLastQuery);

                //you can also set setSearchText(...) to make keep the query there when not focused and when focus returns
                // mSearchView.setSearchText(searchSuggestion.getBody());

                Log.d(TAG, "onFocusCleared()");
            }
        });


        //handle menu clicks the same way as you would
        //in a regular activity
        mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {

                SharedPreferences.Editor editor = preferences.edit();
                int id = item.getItemId();
                switch (id) {
                    case R.id.action_manage_source:
                        //mPresenter.cleanSearchHistory();
                        handler.postDelayed(() -> BookSourceActivity.startThis(MyMainActivity.this,requestSource), 200);
                        break;
                    case R.id.action_clear_history:
                        mPresenter.cleanSearchHistory();
                        break;
                    case R.id.action_add_local:
                        PermissionUtils.checkMorePermissions(getContext(), MApplication.PerList, new PermissionUtils.PermissionCheckCallback() {
                            @Override
                            public void onHasPermission() {
                                startActivity(new Intent(MyMainActivity.this, ImportBookActivity.class));
                            }

                            @Override
                            public void onUserHasAlreadyTurnedDown(String... permission) {
                                MyMainActivity.this.toast(R.string.import_per);
                            }

                            @Override
                            public void onAlreadyTurnedDownAndNoAsk(String... permission) {
                                MyMainActivity.this.toast(R.string.please_grant_storage_permission);
                                PermissionUtils.requestMorePermissions(MyMainActivity.this, permission, FILE_SELECT_RESULT);
                            }
                        });
                        break;
                    case R.id.action_add_url:
                        InputDialog.builder(getContext())
                                .setTitle(getString(R.string.add_book_url))
                                .setCallback(inputText -> {
                                    inputText = StringUtils.trim(inputText);
                                    mPresenter.addBookUrl(inputText);
                                }).show();
                        break;
                    case R.id.action_download_all:
                        if (!isNetWorkAvailable())
                            toast(R.string.network_connection_unavailable);
                        else
                            RxBus.get().post(RxBusTag.DOWNLOAD_ALL, 10000);
                        break;
                    case R.id.action_list_grid:
                        editor.putBoolean("bookshelfIsList", !viewIsList);
                        editor.apply();
                        MApplication.getInstance().setChangeTheme(true);
                        recreate();
                        break;
                    case R.id.action_flow_leftright:
                        editor.putBoolean("findIsFlow", !findIsFlow);
                        editor.apply();
                        MApplication.getInstance().setChangeTheme(true);
                        recreate();
                        break;
                    case R.id.action_clear_find_cache:

                        MyFindBookFragment  findBookFragment = (MyFindBookFragment) mFragmentList.get(2);

                        ACache.get(getContext(), "findCache").clear();
                        toast("清除发现缓存成功！");
                        // if (findBookFragment != null) {
                        //      findBookFragment.
                        //  }
                        break;
                    case R.id.action_clear_cache:
                        new AlertDialog.Builder(getContext())
                                .setTitle(R.string.clear_content)
                                .setMessage("是否同时删除已下载的书籍目录？")
                                .setPositiveButton("是", (dialog, which) -> BookshelfHelp.clearCaches(true))
                                .setNegativeButton("否", (dialogInterface, i) -> BookshelfHelp.clearCaches(false))
                                .show();
                        break;
                    case R.id.action_clearBookshelf:
                        new AlertDialog.Builder(getContext())
                                .setTitle(R.string.clear_bookshelf)
                                .setMessage(R.string.clear_bookshelf_s)
                                .setPositiveButton(R.string.ok, (dialog, which) -> mPresenter.clearBookshelf())
                                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
                                })
                                .show();
                        break;
                    //case R.id.action_change_icon:
                    //    LauncherIcon.Change();
                    //     break;
                    case android.R.id.home:

                        break;
                }

                //just print action
                //Toast.makeText(getContext().getApplicationContext(), item.getTitle(),
                //        Toast.LENGTH_SHORT).show();


            }


        });

        //use this listener to listen to menu clicks when app:floatingSearch_leftAction="showHome"
        mSearchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
            @Override
            public void onHomeClicked() {

                Log.d(TAG, "onHomeClicked()");
            }
        });

        /*
         * Here you have access to the left icon and the text of a given suggestion
         * item after as it is bound to the suggestion list. You can utilize this
         * callback to change some properties of the left icon and the text. For example, you
         * can load the left icon images using your favorite image loading library, or change text color.
         *
         *
         * Important:
         * Keep in mind that the suggestion list is a RecyclerView, so views are reused for different
         * items in the list.
         */
        mSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(View suggestionView, ImageView leftIcon,
                                         TextView textView, SearchSuggestion item, int itemPosition) {
                ColorSuggestion colorSuggestion = (ColorSuggestion) item;

                String textColor = mIsDarkSearchTheme ? "#ffffff" : "#000000";
                String textLight = mIsDarkSearchTheme ? "#bfbfbf" : "#787878";

                leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                        R.drawable.ic_history_black_24dp, null));

                if (colorSuggestion.ismIsShelf()) {

                    leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.book, null));

                }else if (colorSuggestion.ismIsFind()) {
                    leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.navigation, null));
                }
                Util.setIconColor(leftIcon, Color.parseColor(textColor));
                leftIcon.setAlpha(.36f);
                //    leftIcon.setAlpha(0.0f);
                //    leftIcon.setImageDrawable(null);
                //}

                textView.setTextColor(Color.parseColor(textColor));
                String text = colorSuggestion.getBody()
                        .replaceFirst(mSearchView.getQuery(),
                                "<font color=\"" + textLight + "\">" + mSearchView.getQuery() + "</font>");
                textView.setText(Html.fromHtml(text));
            }

        });
    }

    @Override
    public void querySearchHistorySuccess(List<SearchHistoryBean> searchHistoryBeanList){

        List<ColorSuggestion> suggestionList = new ArrayList<>();
        ColorSuggestion colorSuggestion;

        //mSearchView.setSearchHint(getString((R.string.app_name)));

        if(searchHistoryBeanList!=null) {//清空浏览记录后要执行查询历史记录，让历史界面上为空
            for (int i = 0; i < searchHistoryBeanList.size(); i++) {
                colorSuggestion = new ColorSuggestion(searchHistoryBeanList.get(i).getContent());
                colorSuggestion.setIsHistory(true);
                suggestionList.add(colorSuggestion);
                if (suggestionList.size() == suggestionCount) {
                    break;
                }
            }
        }
        DataHelper.setsColorSuggestions(suggestionList);

    }

    @Override
    public void deleteSearchHistorySuccess(List<SearchHistoryBean> searchHistoryBeanList){

        List<ColorSuggestion> suggestionList = new ArrayList<>();
        ColorSuggestion colorSuggestion;

        //mSearchView.setSearchHint(getString((R.string.app_name)));

        if(searchHistoryBeanList!=null) {//清空浏览记录后要执行查询历史记录，让历史界面上为空
            for (int i = 0; i < searchHistoryBeanList.size(); i++) {
                colorSuggestion = new ColorSuggestion(searchHistoryBeanList.get(i).getContent());
                colorSuggestion.setIsHistory(true);
                suggestionList.add(colorSuggestion);
                if (suggestionList.size() == suggestionCount) {
                    break;
                }
            }
        }
        DataHelper.setsColorSuggestions(suggestionList);

        mSearchView.swapSuggestions(DataHelper.getHistory(getContext(), suggestionCount));
    }


    @Override
    protected void onDestroy() {
        UpLastChapterModel.destroy();
        super.onDestroy();
    }

    @Override
    public void dismissHUD() {
        moDialogHUD.dismiss();
    }

    public void onRestore(String msg) {
        moDialogHUD.showLoading(msg);
    }


    @Override
    public void recreate(){


        super.recreate();
    }

    @Override
    public void updateUITitle(String logo_title){

        /*
        TextView logoTitle = navigationView.getHeaderView(0).findViewById(R.id.logo_title);

        logoTitle.setText(logo_title);

*/

    }
    @Override
    public void updateUI(){

        /*
        String logo_path = preferences.getString(getResources().getString(R.string.pk_logo_path), "");

        GifImageView logoPath =  navigationView.getHeaderView(0).findViewById(R.id.logo_path);

        File f=new File(logo_path);

        if(f.exists()){
            //logoPath.setImageURI(Uri.parse(logo_path));

            try {

                if(logo_path.toLowerCase().endsWith(".gif")) {
                    GifDrawable gifDrawable = new GifDrawable(f);

                    logoPath.setImageDrawable(gifDrawable);
                }else{
                    logoPath.setImageURI(Uri.parse(logo_path));
                }
            }catch (IOException e) {
                e.printStackTrace();
            }


        }else{

            logoPath.setImageResource(R.drawable.ebook);
        }


        String logo_title = preferences.getString(getResources().getString(R.string.pk_logo_title), "");
        String logo_title_align = preferences.getString(getResources().getString(R.string.pk_logo_title_align), "0");

        TextView logoTitle = navigationView.getHeaderView(0).findViewById(R.id.logo_title);

        if(!TextUtils.isEmpty(logo_title)) {
            logoTitle.setText(logo_title);
        }else{
            logoTitle.setText(R.string.read_summary);
        }




        switch (logo_title_align) {
            case "0":
                logoTitle.setGravity(Gravity.LEFT);
                break;
            case "1":
                logoTitle.setGravity(Gravity.CENTER);
                break;

            case "2":
                logoTitle.setGravity(Gravity.RIGHT);
                break;

        }
*/


    }


    /**
     * 新版本运行
     */
    private void versionUpRun() {
        if (preferences.getInt("versionCode", 0) != MApplication.getVersionCode()) {
            //保存版本号
            preferences.edit()
                    .putInt("versionCode", MApplication.getVersionCode())
                    .apply();
            //更新日志
            moDialogHUD.showAssetMarkdown("updateLog.md");
        }
    }


    @Override
    protected void firstRequest() {
        if (!isRecreate) {
            versionUpRun();
            requestPermission();
            handler.postDelayed(this::preloadReader, 200);
        }

        handler.postDelayed(() -> UpLastChapterModel.getInstance().startUpdate(), 60 * 1000);

    }

    /**
     * 获取权限
     */
    private void requestPermission() {
        List<String> per = PermissionUtils.checkMorePermissions(this, MApplication.PerList);
        if (per.size() > 0) {
            toast(R.string.get_storage_per);
            PermissionUtils.requestMorePermissions(this, per, MApplication.RESULT__PERMS);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Boolean mo = moDialogHUD.onKeyDown(keyCode, event);
        if (mo) {
            return true;
        } else if (mTlIndicator.getSelectedTabPosition() != 0) {
            Objects.requireNonNull(mTlIndicator.getTabAt(0)).select();
            return true;
        } else {
            if (keyCode == KeyEvent.KEYCODE_BACK ) {
                if (mDrawer.isDrawerOpen()) {
                    mDrawer.closeDrawer();
                    return true;
                }
                exit();
                return true;
            }
            return super.onKeyDown(keyCode, event);
        }
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            if (getCurrentFocus() != null) {
                showSnackBar(mSearchView, "再按一次退出程序");
            }
            exitTime = System.currentTimeMillis();
        } else {
            MApplication.getInstance().setChangeTheme(false);
            DataBackup.getInstance().autoSave();
            finish();
        }
    }


    private void preloadReader() {
        AsyncTask.execute(() -> {
            ReadBookControl.getInstance();
            ChapterContentHelp.getInstance();
        });
    }

    @Override
    public boolean isRecreate() {
        return isRecreate;
    }

    @Override
    public void changeTabName(String name) {
        TabLayout.Tab tab = mTlIndicator.getTabAt(2);
        if (tab == null) return;
        View customView = tab.getCustomView();
        if (customView == null) return;
        TextView tv = customView.findViewById(R.id.tabtext);
        tv.setText(name);
        ImageView im = customView.findViewById(R.id.tabicon);
        im.setImageResource(R.drawable.ic_arrow_drop_down);
        im.setVisibility(View.VISIBLE);

    }
}
