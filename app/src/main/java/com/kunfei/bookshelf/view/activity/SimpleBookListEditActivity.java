package com.kunfei.bookshelf.view.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hwangjr.rxbus.RxBus;
import com.kunfei.basemvplib.BitIntentDataManager;
import com.kunfei.basemvplib.impl.IPresenter;
import com.kunfei.bookshelf.R;
import com.kunfei.bookshelf.base.MBaseActivity;
import com.kunfei.bookshelf.base.MBaseSimpleActivity;
import com.kunfei.bookshelf.bean.DownloadBookBean;
import com.kunfei.bookshelf.bean.RecommendIndexBean;
import com.kunfei.bookshelf.constant.RxBusTag;
import com.kunfei.bookshelf.model.BookListManager;
import com.kunfei.bookshelf.service.CheckSourceService;
import com.kunfei.bookshelf.service.DownloadService;
import com.kunfei.bookshelf.service.UpdateBookListService;
import com.kunfei.bookshelf.utils.theme.ThemeStore;
import com.kunfei.bookshelf.view.adapter.DownloadAdapter;
import com.kunfei.bookshelf.widget.BoldTextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.text.TextUtils.isEmpty;
import static com.kunfei.bookshelf.service.DownloadService.addDownloadAction;
import static com.kunfei.bookshelf.service.DownloadService.finishDownloadAction;
import static com.kunfei.bookshelf.service.DownloadService.obtainDownloadListAction;
import static com.kunfei.bookshelf.service.DownloadService.progressDownloadAction;
import static com.kunfei.bookshelf.service.DownloadService.removeDownloadAction;

public class SimpleBookListEditActivity extends MBaseSimpleActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.btv_save)
    BoldTextView btvSave;
    @BindView(R.id. btv_del)
    BoldTextView btvDel;


    @BindView(R.id.et_url)
    AppCompatEditText etUrl;
    @BindView(R.id.et_title)
    AppCompatEditText etTitle;


    public RecommendIndexBean  recommendIndexBean;

    //private DownloadAdapter adapter;
   // private DownloadReceiver receiver;

    public static void startThis(Activity activity) {
        Intent intent = new Intent(activity, SimpleBookListEditActivity.class);
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
        setContentView(R.layout.a_simple_book_list_edit);
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
            recommendIndexBean = (RecommendIndexBean) BitIntentDataManager.getInstance().getData(bookKey);
        }

    }

    @Override
    protected void bindView() {

        if(recommendIndexBean.getTitle().equals("新增书单源")
                &&isEmpty(recommendIndexBean.getUrl())
        ) {


        }else{
            etUrl.setText(recommendIndexBean.getUrl());
            etTitle.setText(recommendIndexBean.getTitle());
            btvDel.setVisibility(View.VISIBLE);
        }



    }


    @Override
    protected void bindEvent(){
        btvSave.setOnClickListener(v -> {

            RecommendIndexBean recommendIndexBean = new RecommendIndexBean();
            Long id = System.currentTimeMillis();
            recommendIndexBean.setId(id);
            recommendIndexBean.setTitle(etTitle.getText().toString());
            recommendIndexBean.setUrl(etUrl.getText().toString());
            BookListManager.save(recommendIndexBean);
            RxBus.get().post(RxBusTag.UPDATE_BOOK_LIST, false);

            //List<RecommendIndexBean> recommendIndexBeanList = new ArrayList<RecommendIndexBean>();
            //recommendIndexBeanList.add(recommendIndexBean);
            //UpdateBookListService.start(getContext(), recommendIndexBeanList);

            finish();
        });

        btvDel.setOnClickListener(v -> {

            BookListManager.del(recommendIndexBean);
            RxBus.get().post(RxBusTag.UPDATE_BOOK_LIST, false);
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
