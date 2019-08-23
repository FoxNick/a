package com.kunfei.bookshelf.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hwangjr.rxbus.RxBus;
import com.kunfei.basemvplib.BitIntentDataManager;
import com.kunfei.basemvplib.impl.IPresenter;
import com.kunfei.bookshelf.R;
import com.kunfei.bookshelf.base.MBaseSimpleActivity;
import com.kunfei.bookshelf.base.observer.MyObserver;
import com.kunfei.bookshelf.bean.RecommendBookBean;
import com.kunfei.bookshelf.bean.RecommendBookListBean;
import com.kunfei.bookshelf.bean.RecommendIndexBean;
import com.kunfei.bookshelf.constant.RxBusTag;
import com.kunfei.bookshelf.model.BookListManager;
import com.kunfei.bookshelf.service.UpdateBookListService;
import com.kunfei.bookshelf.utils.theme.ThemeStore;
import com.kunfei.bookshelf.view.adapter.SimpleBookListAdapter;
import com.kunfei.bookshelf.view.adapter.SimpleBookListDetailAdapter;
import com.kunfei.bookshelf.widget.BoldTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

import static android.text.TextUtils.isEmpty;

public class SimpleBookListDetailActivity extends MBaseSimpleActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.btv_save)
    BoldTextView btvSave;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.recycler_view_list)
    RecyclerView recycler_view_list;

    private SimpleBookListDetailAdapter adapter;



    public RecommendBookListBean  recommendBookListBean;


    public static void startThis(Activity activity) {
        Intent intent = new Intent(activity, SimpleBookListDetailActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
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
        getWindow().getDecorView().setBackgroundColor(ThemeStore.backgroundColor(this));
        setContentView(R.layout.a_simple_book_list_detail);
        ButterKnife.bind(this);
        this.setSupportActionBar(toolbar);
        setupActionBar();
    }

    /**
     * 数据初始化
     */
    @Override
    protected void initData() {

        Intent intent = getIntent();

        String bookKey = intent.getStringExtra("bookKey");
        if (!isEmpty(bookKey)) {
            recommendBookListBean = (RecommendBookListBean) BitIntentDataManager.getInstance().getData(bookKey);
            tvTitle.setText(recommendBookListBean.getName());
        }

        Observable<List<RecommendBookBean>> observable = BookListManager.queryBookListDetail(recommendBookListBean);
        if (observable != null) {
            observable.subscribe(getImportObserver());
        }

    }

    private MyObserver<List<RecommendBookBean>> getImportObserver() {
        return new MyObserver<List<RecommendBookBean>>() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onNext(List<RecommendBookBean> bookSourceBeans) {
                if (bookSourceBeans.size() > 0) {
                    adapter.replaceAll(bookSourceBeans);
                   // mView.refreshBookList(bookSourceBeans.get(0).getList());
                    // mView.showSnackBar(String.format("导入成功%d个书源", bookSourceBeans.size()), Snackbar.LENGTH_SHORT);
                    // mView.setResult(RESULT_OK);
                } else {
                    //mView.showSnackBar("格式不对", Snackbar.LENGTH_SHORT);
                }
            }

            @Override
            public void onError(Throwable e) {
                //mView.showSnackBar(e.getMessage(), Snackbar.LENGTH_SHORT);
            }
        };
    }

    @Override
    protected void bindView() {


        //设置为垂直的样式
        recycler_view_list.setLayoutManager(new LinearLayoutManager(this));
        //使用的是系统默认的分割线
        //recycler_view_list.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        //设置适配器
        recycler_view_list.setAdapter(adapter=new SimpleBookListDetailAdapter(this));
        recycler_view_list.setNestedScrollingEnabled(false);
        //设置默认动画
        recycler_view_list.setItemAnimator(new DefaultItemAnimator());

        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        adapter.setOnItemClickListener(new SimpleBookListDetailAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                view.startAnimation(animation);
                // Toast.makeText(mContext, position + " click",
                //        Toast.LENGTH_SHORT).show();

                RecommendBookBean recommendBookBean = adapter.getDataList().get(position);
                Intent intent = new Intent(getContext(), MyMainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                String key = String.valueOf(System.currentTimeMillis());
                intent.putExtra("recommendBook", recommendBookBean.getTitle());

                startActivityByAnim(intent, android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });
    }


    @Override
    protected void bindEvent(){
        btvSave.setOnClickListener(v -> {



            finish();
        });


    }

    //菜单
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    //设置ToolBar
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }
    }






}
