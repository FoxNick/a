package com.kunfei.bookshelf.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kunfei.bookshelf.R;
import com.kunfei.bookshelf.bean.RecommendBookListBean;
import com.kunfei.bookshelf.bean.RecommendIndexBean;

import java.util.ArrayList;
import java.util.List;

public class SimpleBookListAdapter extends RecyclerView.Adapter<SimpleBookListAdapter.MyViewHolder> {

    private Context mContext;
    //private List<String> mData;
    private List<RecommendBookListBean> mData = new ArrayList<RecommendBookListBean>();

    //自定义点击事件和长按事件
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener)
    {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    //构造器
    public SimpleBookListAdapter(Context mContext) {
        this.mContext = mContext;
    }
    //加载布局
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_booklist_list, parent, false));
        return holder;
    }
    //为布局加载数据
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv_name.setText(mData.get(position).getName());
        holder.tv_num.setText(mData.get(position).getNum()+"本");
        holder.tv_author.setText(mData.get(position).getAuthor());

        setClickListener(holder,position);
    }
    //设置点击事件
    private void setClickListener(final MyViewHolder holder, int position) {
        if(mOnItemClickListener!=null){
            holder.tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = holder.getLayoutPosition();//默认是第一个开始
                    mOnItemClickListener.onItemClick(holder.itemView, pos);
                }
            });
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                int pos = holder.getLayoutPosition()-1;//默认是第一个开始
                //mOnItemClickListener.onItemLongClick(holder.itemView, pos);
                return true;//返回true可以屏蔽点击监听的响应
            }
        });
    }

    //总共多少个项
    @Override
    public int getItemCount() {
        return mData.size();
    }
    //初始化布局信息
    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_name;
        TextView tv_num;
        TextView tv_author;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_num = (TextView) itemView.findViewById(R.id.tv_num);
            tv_author = (TextView) itemView.findViewById(R.id.tv_author);
        }
    }

    //添加
    public void addData(int position) {
        // mData.add(position, "Insert One");
        notifyItemInserted(position);
    }
    //删除
    public void removeData(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }


    public synchronized void replaceAll(List<RecommendBookListBean> dataList){
        this.mData.clear();
        this.mData.addAll(dataList);
        notifyDataSetChanged();
    }

    public List<RecommendBookListBean> getDataList() {
        return mData;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        //void onItemLongClick(View view , int position);
    }
}