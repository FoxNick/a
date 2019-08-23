package com.kunfei.bookshelf.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kunfei.bookshelf.R;
import com.kunfei.bookshelf.bean.RecommendIndexBean;
import com.kunfei.bookshelf.utils.EncodingDetect;
import com.kunfei.bookshelf.widget.flowlayout.TagAdapter;

import java.util.ArrayList;

public class SimpleBookListFlowAdapter extends TagAdapter<RecommendIndexBean> {

    private Long selectId ;

public SimpleBookListFlowAdapter() {
        super(new ArrayList<RecommendIndexBean>());
        }

public interface OnItemClickListener{
    void itemClick(View v, RecommendIndexBean recommendIndexBean);
    void settingClick(View v, RecommendIndexBean recommendIndexBean);
}
    private SimpleBookListFlowAdapter.OnItemClickListener onItemClickListener;

    public SimpleBookListFlowAdapter.OnItemClickListener getListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(SimpleBookListFlowAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public View getView(com.kunfei.bookshelf.widget.flowlayout.FlowLayout parent, int position, RecommendIndexBean recommendIndexBean) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simple_book_list,
                parent, false);

        TextView tv = view.findViewById(R.id.tv_text);
        tv.setTag(recommendIndexBean.getId());
        tv.setText(recommendIndexBean.getTitle());

        if(!recommendIndexBean.getTitle().equals("新增书单源")
            && recommendIndexBean.getId().equals(selectId)){
            view.setBackground(parent.getContext().getResources().getDrawable(R.drawable.bg_flow_source_item_selected));
        }




        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != onItemClickListener){
                    if(recommendIndexBean.getTitle().equals("新增书单源")){
                        onItemClickListener.settingClick(v,recommendIndexBean);
                    }else {
                        onItemClickListener.itemClick(v, recommendIndexBean);

                    }
                }
            }
        });



        ImageView iv = view.findViewById(R.id.mBookshelfEdit);
        if(recommendIndexBean.getTitle().equals("新增书单源")){
            iv.setVisibility(View.INVISIBLE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) iv.getLayoutParams();
            params.width = 0;
            iv.setLayoutParams(params);
        }

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != onItemClickListener){
                    onItemClickListener.settingClick(v,recommendIndexBean);

                }
            }
        });


        return view;
    }



    public RecommendIndexBean getItemData(int position){
        return mTagDatas.get(position);
    }

    public int getDataSize(){
        return mTagDatas.size();
    }


    public Long getSelectId() {
        return selectId;
    }

    public void setSelectId(Long selectId) {
        this.selectId = selectId;
    }
}