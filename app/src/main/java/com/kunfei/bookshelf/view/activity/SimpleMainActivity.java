package com.kunfei.bookshelf.view.activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.kunfei.basemvplib.impl.IPresenter;
import com.kunfei.bookshelf.R;
import com.kunfei.bookshelf.base.MBaseActivity;
import com.kunfei.bookshelf.base.MBaseSimpleActivity;
import com.kunfei.bookshelf.utils.bar.BarHide;
import com.kunfei.bookshelf.utils.bar.ImmersionBar;
import com.kunfei.bookshelf.view.fragment.SimpleBookDiscoveryFragment;
import com.kunfei.bookshelf.view.fragment.SimpleBookListFragment;
import com.kunfei.bookshelf.view.fragment.MyBookListFragment;
import com.kunfei.bookshelf.view.fragment.MySearchBookFragment;
import com.kunfei.bookshelf.view.fragment.SimpleBookShelfFragment;
import com.kunfei.bookshelf.widget.RippleView;
import com.kunfei.bookshelf.widget.StateImageView;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SimpleMainActivity extends MBaseSimpleActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.pv_bookshelf)
    RippleView pvBookshelf;
    @BindView(R.id.pv_discover)
    RippleView pvDiscover;
    @BindView(R.id.pv_profile)
    RippleView pvProfile;

    @BindView(R.id.siv_bookshelf)
    StateImageView sivBookshelf;
    @BindView(R.id.siv_discover)
    StateImageView sivDiscover;
    @BindView(R.id.siv_profile)
    StateImageView sivProfile;

    @BindView(R.id.vwNavigationBar)
    View vwNavigationBar;

    @BindView(R.id.mHomeViewPager)
    ViewPager mHomeViewPager;


    protected List<Fragment> mFragmentList;

    /**************Adapter***************/
    protected MainFragmentPageAdapter mainFragmentPageAdapter;


    public static void startThis(Context context) {
        context.startActivity(new Intent(context, SimpleMainActivity.class));
    }



    @Override
    protected IPresenter initInjector() {
        return null;
    }

    @Override
    protected void onCreateActivity() {
        setContentView(R.layout.activity_simple_main);
        ButterKnife.bind(this);

    }

    @Override
    protected void initData() {


    }

    /**
     * 事件触发绑定
     */
    @Override
    protected void bindEvent() {
        pvBookshelf.setOnClickListener(view -> {
            sivBookshelf.setChecked(true);
            sivDiscover.setChecked(false);
            sivProfile.setChecked(false);
            mHomeViewPager.setCurrentItem(0, false);
        });

        pvDiscover.setOnClickListener(view -> {
            sivBookshelf.setChecked(false);
            sivDiscover.setChecked(true);
            sivProfile.setChecked(false);
            mHomeViewPager.setCurrentItem(1, false);
        });

        pvProfile.setOnClickListener(view -> {
            sivBookshelf.setChecked(false);
            sivDiscover.setChecked(false);
            sivProfile.setChecked(true);
            mHomeViewPager.setCurrentItem(2, false);
        });
    }

    /**
     * 控件绑定
     */
    @Override
    protected void bindView() {
        mFragmentList = createTabFragments();
        mainFragmentPageAdapter = new MainFragmentPageAdapter(getSupportFragmentManager());
        mHomeViewPager.setAdapter(mainFragmentPageAdapter);
        mHomeViewPager.setOffscreenPageLimit(3);
        mHomeViewPager.addOnPageChangeListener((ViewPager.OnPageChangeListener)this);
        mHomeViewPager.setCurrentItem(0, false);
        sivBookshelf.setChecked(true);

    }

    protected List<Fragment> createTabFragments() {
        SimpleBookShelfFragment bookShelfFragment = null;
        SimpleBookListFragment bookListFragment = null;
        //SimpleBookDiscoveryFragment bookDiscoveryFragment = null;

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof SimpleBookShelfFragment) {
                bookShelfFragment = (SimpleBookShelfFragment) fragment;
            } else if (fragment instanceof SimpleBookListFragment) {
                bookListFragment = (SimpleBookListFragment) fragment;
            }else if (fragment instanceof SimpleBookDiscoveryFragment) {
             //   bookDiscoveryFragment = (SimpleBookDiscoveryFragment) fragment;
            }
        }
        if (bookListFragment == null)
            bookShelfFragment = new SimpleBookShelfFragment();
        if (bookListFragment == null)
            bookListFragment = new SimpleBookListFragment();
       // if (bookDiscoveryFragment == null)
            //bookDiscoveryFragment = new SimpleBookDiscoveryFragment();
        return Arrays.asList(bookShelfFragment, bookListFragment);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                sivBookshelf.setChecked(true);
                sivDiscover.setChecked(false);
                sivProfile.setChecked(false);
                break;
            case 1:
                sivBookshelf.setChecked(false);
                sivDiscover.setChecked(true);
                sivProfile.setChecked(false);
                break;
            case 2:
                sivBookshelf.setChecked(false);
                sivDiscover.setChecked(false);
                sivProfile.setChecked(true);
                break;

        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            initImmersionBar();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();


        initImmersionBar();

    }


    /******************inner class*****************/
    public class MainFragmentPageAdapter extends FragmentPagerAdapter {

        MainFragmentPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

       // @Override
        //public CharSequence getPageTitle(int position) {
          //  return mTitleList.get(position);
        //}

    }

}
