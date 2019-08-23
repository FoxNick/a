package com.kunfei.bookshelf.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.kunfei.basemvplib.BitIntentDataManager;
import com.kunfei.bookshelf.R;
import com.kunfei.bookshelf.base.MBaseFragment;
import com.kunfei.bookshelf.bean.RecommendBookListBean;
import com.kunfei.bookshelf.bean.RecommendIndexBean;
import com.kunfei.bookshelf.presenter.ReadBookPresenter;
import com.kunfei.bookshelf.presenter.SimpleBookListPresenter;
import com.kunfei.bookshelf.presenter.contract.SimpleBookListContract;
import com.kunfei.bookshelf.view.activity.MyReadBookActivity;
import com.kunfei.bookshelf.view.activity.SimpleBookListDetailActivity;
import com.kunfei.bookshelf.view.activity.SimpleBookListEditActivity;
import com.kunfei.bookshelf.view.adapter.BaseAdapter;
import com.kunfei.bookshelf.view.adapter.SimpleBookListAdapter;
import com.kunfei.bookshelf.view.adapter.SimpleBookListFlowAdapter;
import com.kunfei.bookshelf.widget.flowlayout.TagFlowLayout;
import com.kunfei.bookshelf.widget.recycler.SpacesItemDecoration1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SimpleBookListFragment   extends MBaseFragment<SimpleBookListContract.Presenter> implements SimpleBookListContract.View  {

    @BindView(R.id.recycler_view)
     TagFlowLayout recycler_view;
    @BindView(R.id.mBookshelfTitle)
     TextView mBookshelfTitle;
    @BindView(R.id.recycler_view_list)
     RecyclerView recycler_view_list;
    private Context mContext;
    private SimpleBookListAdapter adapter;
    private SimpleBookListFlowAdapter flowAdapter;

    @BindView(R.id.loadingLayout)
    RelativeLayout loadingLayout;

    @BindView(R.id.loadingTitle)
    TextView loadingTitle;

    private Unbinder unbinder;


    @Override
    public int createLayoutId() {
        return R.layout.f_simple_booklist;
    }

    @Override
    protected SimpleBookListContract.Presenter initInjector() {
        return new SimpleBookListPresenter();
    }


    public void taggleIndex(){
        recycler_view.setVisibility(recycler_view.getVisibility()==View.VISIBLE ? View.GONE : View.VISIBLE );
    }

    @Override
    protected void bindView() {

        mContext=this.getContext();

        super.bindView();

        unbinder = ButterKnife.bind(this, view);

        mBookshelfTitle.setOnClickListener(v -> {
            taggleIndex();
        });
        //设置为垂直的样式
        //recycler_view.setLayoutManager(new LinearLayoutManager(mContext));
        //使用的是系统默认的分割线
        //recycler_view.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));

        //  StaggeredGridLayoutManager llm = new StageredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        //llm.setAutoMeasureEnabled(true);

        //设置为垂直3列
        //recycler_view.setLayoutManager(llm);
        ////item间隔16
        //recycler_view.addItemDecoration(new SpacesItemDecoration1(16));


        flowAdapter = new SimpleBookListFlowAdapter();
        recycler_view.setAdapter(flowAdapter);



        flowAdapter.setOnItemClickListener(new SimpleBookListFlowAdapter.OnItemClickListener() {
            @Override
            public void itemClick(View v, RecommendIndexBean recommendIndexBean) {
                //SimpleBookListEditActivity.startThis(getActivity());

                taggleIndex();

                loadingLayout.setVisibility(View.VISIBLE);
                //頂部變化
                mBookshelfTitle.setText(recommendIndexBean.getTitle());
                //列表變化
                mPresenter.queryBookList(recommendIndexBean);

                flowAdapter.setSelectId(recommendIndexBean.getId());
                flowAdapter.notifyDataChanged();

               // v.setBackground(getContext().getResources().getDrawable(R.drawable.bg_flow_source_item_selected));

            }

            @Override
            public void settingClick(View v, RecommendIndexBean recommendIndexBean) {

                Intent intent = new Intent(getActivity(), SimpleBookListEditActivity.class);
                String key = String.valueOf(System.currentTimeMillis());
                intent.putExtra("bookKey", key);
                try {
                    BitIntentDataManager.getInstance().putData(key, recommendIndexBean);
                } catch (Exception e) {
                    BitIntentDataManager.getInstance().putData(key, recommendIndexBean);
                    e.printStackTrace();
                }
                startActivityByAnim(intent, android.R.anim.fade_in, android.R.anim.fade_out);
            }

        });

        //recycler_view.setNestedScrollingEnabled(false);


        //设置适配器
        //recycler_view.setAdapter(adapter=new SimpleBookListAdapter(mContext,mData));
        //设置默认动画
        //recycler_view.setItemAnimator(new DefaultItemAnimator());
        //播放点击动画
        final Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.alpha);




        //-----------------------书单列表--------------------------------------------

        //设置为垂直的样式
        recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext));
        //使用的是系统默认的分割线
        //recycler_view_list.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        //设置适配器
        recycler_view_list.setAdapter(adapter=new SimpleBookListAdapter(mContext));
        recycler_view_list.setNestedScrollingEnabled(false);
        //设置默认动画
        recycler_view_list.setItemAnimator(new DefaultItemAnimator());
        //播放点击动画

        //final Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.alpha);
        adapter.setOnItemClickListener(new SimpleBookListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                view.startAnimation(animation);
               // Toast.makeText(mContext, position + " click",
                //        Toast.LENGTH_SHORT).show();

                RecommendBookListBean recommendIndexBean = adapter.getDataList().get(position);
                Intent intent = new Intent(getActivity(), SimpleBookListDetailActivity.class);
                String key = String.valueOf(System.currentTimeMillis());
                intent.putExtra("bookKey", key);
                try {
                    BitIntentDataManager.getInstance().putData(key, recommendIndexBean);
                } catch (Exception e) {
                    BitIntentDataManager.getInstance().putData(key, recommendIndexBean);
                    e.printStackTrace();
                }
                startActivityByAnim(intent, android.R.anim.fade_in, android.R.anim.fade_out);

            }


        });

    }


    @Override
    protected void firstRequest() {
        mPresenter.queryAll(false);
    }

    @Override
    public void  refreshAll(List<RecommendIndexBean> recommendIndexBean){

        RecommendIndexBean aa = new RecommendIndexBean();
        aa.setTitle("新增书单源");
        recommendIndexBean.add(aa);

        flowAdapter.replaceAll(recommendIndexBean);
    }


    @Override
    public void  refreshBookList(List<RecommendBookListBean> list){
        loadingLayout.setVisibility(View.INVISIBLE);
        adapter.replaceAll(list);
    }

}
