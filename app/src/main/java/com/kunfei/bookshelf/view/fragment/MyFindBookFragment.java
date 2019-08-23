package com.kunfei.bookshelf.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.kunfei.basemvplib.BitIntentDataManager;
import com.kunfei.bookshelf.R;
import com.kunfei.bookshelf.base.MBaseFragment;
import com.kunfei.bookshelf.bean.FindKindBean;
import com.kunfei.bookshelf.bean.MyFindKindGroupBean;
import com.kunfei.bookshelf.bean.SearchBookBean;
import com.kunfei.bookshelf.presenter.BookDetailPresenter;
import com.kunfei.bookshelf.presenter.MyFindBookPresenter;
import com.kunfei.bookshelf.presenter.contract.MyFindBookContract;
import com.kunfei.bookshelf.view.activity.BookDetailActivity;
import com.kunfei.bookshelf.view.activity.MyMainActivity;
import com.kunfei.bookshelf.view.adapter.FindFlowAdapter;
import com.kunfei.bookshelf.view.adapter.FindSecondAdapter;
import com.kunfei.bookshelf.view.adapter.KindBookAdapter;
import com.kunfei.bookshelf.view.adapter.MyFindLeftAdapter;
import com.kunfei.bookshelf.widget.flowlayout.TagFlowLayout;
import com.kunfei.bookshelf.widget.modialog.MoDialogHUD;
import com.kunfei.bookshelf.widget.recycler.refresh.OnLoadMoreListener;
import com.kunfei.bookshelf.widget.recycler.refresh.RefreshRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyFindBookFragment extends MBaseFragment<MyFindBookContract.Presenter> implements MyFindBookContract.View {
    @BindView(R.id.ll_content)
    FrameLayout llContent;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.rl_empty_view)
    RelativeLayout rlEmptyView;

    @BindView(R.id.tfl_find)
    TagFlowLayout tflFind;

    @BindView(R.id.rv_find_left)
    RecyclerView rvFindLeft;
    private MyFindLeftAdapter findLeftAdapter;

    @BindView(R.id.rv_find_right)
    RefreshRecyclerView rvFindRight;

    @BindView(R.id.mHomeNavigation)
    LinearLayout mHomeNavigation;

    @BindView(R.id.ns_scroll)
    NestedScrollView nsScroll;



    private boolean isRecreate;

    private CallBackValue callBackValue;

    private Unbinder unbinder;
    private FindFlowAdapter flowAdapter;
    private FindSecondAdapter findSecondAdapter;

    private MoDialogHUD moProgressHUD;

    private Activity mActivity;

    private MyFindKindGroupBean selectedKindGroupBean;

    private KindBookAdapter kindBookAdapter;

    //private Animation bookSourceIn;
    //private Animation bookSourceOut;

    List<MyFindKindGroupBean> AllGroup;


    @Override
    public int createLayoutId() {
        return R.layout.my_fragment_book_find;
    }

    @Override
    protected MyFindBookContract.Presenter initInjector() {
        return new MyFindBookPresenter();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callBackValue = (MyFindBookFragment.CallBackValue) getActivity();

        mActivity = getActivity();
    }

    @Override
    protected void initData() {

        isRecreate = callBackValue != null && callBackValue.isRecreate();



        //bookSourceIn = AnimationUtils.loadAnimation(this.getContext(), R.anim.anim_readbook_top_in);
        //bookSourceOut = AnimationUtils.loadAnimation(this.getContext(), R.anim.anim_readbook_top_out);

    }
    @Override
    protected void bindView() {
        super.bindView();
        unbinder = ButterKnife.bind(this, view);



        initOldView();
        initNewView();

        if(!preferences.getBoolean("findIsFlow", false)) {
            mHomeNavigation.setVisibility(View.VISIBLE);
        }else{
            refreshLayout.setVisibility(View.VISIBLE);
        }


    }



    public TagFlowLayout getTflFind() {
        return tflFind;
    }

    public void initOldView() {


        flowAdapter = new FindFlowAdapter();
        findSecondAdapter = new FindSecondAdapter();
        tflFind.setAdapter(flowAdapter);

        moProgressHUD = new MoDialogHUD(this.getContext());

        flowAdapter.setOnItemClickListener(new FindFlowAdapter.OnItemClickListener() {
            @Override
            public void itemClick(View v,MyFindKindGroupBean findKindGroupBean) {

                selectedKindGroupBean = findKindGroupBean;

                mPresenter.getSecondFind(findKindGroupBean);
                if(!preferences.getBoolean("findIsFlow", false)){

                    findLeftAdapter.upShowIndex(0);
                    findLeftAdapter.clear();
                    kindBookAdapter.clear();
                    taggleBookSource();
                    callBackValue.changeTabName(findKindGroupBean.getGroupName());

                }

            }
        });

        findSecondAdapter.setOnItemClickListener(new FindSecondAdapter.OnItemClickListener() {
            @Override
            public void itemClick(View v, FindKindBean findKindBean) {
                //Toast.makeText(rootView.getContext(), findKindBean.getKindUrl(), Toast.LENGTH_SHORT).show();
                moProgressHUD.dismiss();
                //todo
                ((MyMainActivity)mActivity).kindSearch(findKindBean.getKindUrl(),findKindBean.getTag(),selectedKindGroupBean);


            }
        });
    }

    public void initNewView(){
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        refreshLayout.setOnRefreshListener(() -> {
            mPresenter.initData();
            refreshLayout.setRefreshing(false);
        });



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(),RecyclerView.VERTICAL,false);
        rvFindLeft.setLayoutManager(linearLayoutManager);
        findLeftAdapter = new MyFindLeftAdapter(getActivity(),pos -> {
            //分类点击操作在这里进行


            kindBookAdapter.clear();
            String url = findLeftAdapter.getItem(pos).getKindUrl();
            String tag = findLeftAdapter.getItem(pos).getTag();
            startRefreshAnim();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mPresenter.initKindPage(url, tag);
                    mPresenter.toKindSearch();
                }
            }, 300);

        });

        rvFindLeft.setAdapter(findLeftAdapter);


        //rvFindRight.addItemDecoration(new SimpleDividerDecoration(10,R.color.colorTextSecondary));

        //发现列表
        rvFindRight.setBaseRefreshListener(() -> {
            mPresenter.initPage();
            mPresenter.toKindSearch();
            startRefreshAnim();
        });
        rvFindRight.setLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void startLoadMore() {
                mPresenter.toKindSearch();
            }

            @Override
            public void loadMoreErrorTryAgain() {
                mPresenter.toKindSearch();
            }
        });

        kindBookAdapter = new KindBookAdapter(mActivity);


        rvFindRight.setRefreshRecyclerViewAdapter(kindBookAdapter, new LinearLayoutManager(mActivity));

        rvFindRight.setRefreshRecyclerViewAdapter(kindBookAdapter, new LinearLayoutManager(mActivity));

        View viewRefreshError = LayoutInflater.from(mActivity).inflate(R.layout.view_searchbook_refresh_error, null);
        viewRefreshError.findViewById(R.id.tv_refresh_again).setOnClickListener(v -> {
            kindBookAdapter.replaceAll(null);
            //刷新失败 ，重试
            mPresenter.initPage();
            mPresenter.toKindSearch();
            startRefreshAnim();
        });

        kindBookAdapter.setItemClickListener(new KindBookAdapter.OnItemClickListener() {
            @Override
            public void clickItem(View animView, int position, SearchBookBean searchBookBean) {
                String dataKey = String.valueOf(System.currentTimeMillis());
                Intent intent = new Intent(getActivity(), BookDetailActivity.class);
                intent.putExtra("openFrom", BookDetailPresenter.FROM_SEARCH);
                intent.putExtra("data_key", dataKey);
                BitIntentDataManager.getInstance().putData(dataKey, kindBookAdapter.getItem(position));

                startActivityByAnim(intent, android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }




    public void startRefreshAnim() {
        rvFindRight.startRefresh();
    }


    /**
     * 首次逻辑操作
     */
    @Override
    protected void firstRequest() {
        super.firstRequest();
        mPresenter.initData();

    }

    @Override
    public synchronized void updateUI(List<MyFindKindGroupBean> group) {
        if (rlEmptyView == null) return;
        updateFlowUI(group);


    }


    public void newShowBookSource(){
        refreshLayout.setVisibility( View.VISIBLE );
        mHomeNavigation.setVisibility( View.GONE);

            /*
            if(tflFind.getVisibility()==View.VISIBLE) {
                tflFind.startAnimation(bookSourceIn);
            }else{
                tflFind.startAnimation(bookSourceOut);
            }*/

        //nsScroll.fullScroll(NestedScrollView.FOCUS_UP);


    }

    public void taggleBookSource(){
        refreshLayout.setVisibility(refreshLayout.getVisibility()==View.VISIBLE ? View.GONE : View.VISIBLE );
        mHomeNavigation.setVisibility(mHomeNavigation.getVisibility()==View.VISIBLE ? View.GONE : View.VISIBLE );

            /*
            if(tflFind.getVisibility()==View.VISIBLE) {
                tflFind.startAnimation(bookSourceIn);
            }else{
                tflFind.startAnimation(bookSourceOut);
            }*/

        //nsScroll.fullScroll(NestedScrollView.FOCUS_UP);


    }

    @Override
    public synchronized void ShowSecond(List<FindKindBean> list,String GroupName) {
        if (list == null) return;
        //flow模式
        if(preferences.getBoolean("findIsFlow", false)) {
            moProgressHUD.showKindList(GroupName, list, findSecondAdapter);
        }else {



            findLeftAdapter.setData(list);
            //mPresenter.

            //tflFind.setVisibility(View.INVISIBLE);

            if (list.size() > 0) {

                String url = list.get(0).getKindUrl();
                String tag = list.get(0).getTag();
                startRefreshAnim();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPresenter.initKindPage(url, tag);
                        mPresenter.toKindSearch();
                    }
                }, 300);
            }

        }


    }

    public MyFindKindGroupBean getSelectedKindGroupBean() {
        return selectedKindGroupBean;
    }

    public void setSelectedKindGroupBean(MyFindKindGroupBean selectedKindGroupBean) {
        this.selectedKindGroupBean = selectedKindGroupBean;
    }

    public List<MyFindKindGroupBean> getAllGroup(){
        return AllGroup;
    }

    public  void updateFlowUI(List<MyFindKindGroupBean> group) {
        if (group.size() > 0) {

            AllGroup = group;

            flowAdapter.replaceAll(group);

           if(!preferences.getBoolean("findIsFlow", false)) {
               //有分类的书源记载完毕后，再默认记载第一个书院下的分类
               mPresenter.getSecondFind(group.get(0));
               callBackValue.changeTabName(group.get(0).getGroupName());
           }

            //书源分类加载完毕后，默认加载第一个分类下的书

            rlEmptyView.setVisibility(View.GONE);
        } else {
            flowAdapter.clearAll();
            tvEmpty.setText("没有发现，可以在书源里添加。");
            rlEmptyView.setVisibility(View.VISIBLE);
        }
    }



    //1. 第一次加载数据
    @Override
    public void refreshKindBook(List<SearchBookBean> books) {

        //mInloading = false;

        kindBookAdapter.replaceAll(books);
    }


    //2.第一次加载数后，判断是否结束
    @Override
    public void refreshKindFinish(Boolean isAll) {
        rvFindRight.finishRefresh(isAll, true);
    }

    //3.第一次加载数后，再次加载更多的数据
    @Override
    public void loadMoreKindBook(List<SearchBookBean> books) {

        if (books.size() <= 0) {//m
            loadMoreKindFinish(true);
            return;
        }
        for (SearchBookBean searchBook : kindBookAdapter.getSearchBooks()) {
            if (books.get(0).getName().equals( searchBook.getName()) && books.get(0).getAuthor().equals( searchBook.getAuthor())) {
                loadMoreKindFinish(true);
                return;
            }
        }
        kindBookAdapter.addAll(books);
        loadMoreKindFinish(false);

    }


    //4.结束
    @Override
    public void loadMoreKindFinish(Boolean isAll) {

        rvFindRight.finishLoadMore(isAll, true);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }



    public interface CallBackValue {
        boolean isRecreate();

        void changeTabName(String name);
    }




}
