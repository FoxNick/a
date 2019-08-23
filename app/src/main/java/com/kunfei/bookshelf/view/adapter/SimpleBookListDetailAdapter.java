package com.kunfei.bookshelf.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kunfei.bookshelf.R;
import com.kunfei.bookshelf.bean.RecommendBookBean;
import com.kunfei.bookshelf.bean.RecommendBookListBean;

import java.util.ArrayList;
import java.util.List;

public class SimpleBookListDetailAdapter extends RecyclerView.Adapter<SimpleBookListDetailAdapter.MyViewHolder> {

    private Context mContext;
    //private List<String> mData;
    private List<RecommendBookBean> mData = new ArrayList<RecommendBookBean>();

    //自定义点击事件和长按事件
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener)
    {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    //构造器
    public SimpleBookListDetailAdapter(Context mContext) {
        this.mContext = mContext;
    }
    //加载布局
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_booklist_detail, parent, false));
        return holder;
    }
    //为布局加载数据
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv_book_name.setText(mData.get(position).getTitle());
        holder.tv_book_author.setText("作者："+mData.get(position).getAuthor());
        holder.tv_word_count.setText("字数："+mData.get(position).getWordCount());
        holder.tv_book_comment.setText(mData.get(position).getComment());

        setClickListener(holder,position);
    }
    //设置点击事件
    private void setClickListener(final MyViewHolder holder, int position) {
        if(mOnItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
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
        TextView tv_book_name;
        TextView tv_book_author;
        TextView tv_word_count;
        TextView tv_book_comment;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_book_name = (TextView) itemView.findViewById(R.id.tv_book_name);
            tv_book_author = (TextView) itemView.findViewById(R.id.tv_book_author);
            tv_word_count = (TextView) itemView.findViewById(R.id.tv_word_count);
            tv_book_comment = (TextView) itemView.findViewById(R.id.tv_book_comment);
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


    public synchronized void replaceAll(List<RecommendBookBean> dataList){
        this.mData.clear();
        this.mData.addAll(dataList);
        notifyDataSetChanged();
    }

    public List<RecommendBookBean> getDataList() {
        return mData;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        //void onItemLongClick(View view , int position);
    }
}