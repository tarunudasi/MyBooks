package com.rbooks.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.rbooks.Model.Search;
import com.rbooks.R;
import com.rbooks.listener.OnClickListener;

public class SearchAdapterTitle extends RecyclerView.Adapter {

    private List<Search> list;
    private Context context;
    private OnClickListener onClickListener;
    private final int BOOKS = 0;

    public SearchAdapterTitle(List<Search> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        switch (viewType) {
            case BOOKS:
                View v1 = inflater.inflate(R.layout.search_item, parent, false);
                viewHolder = new MyViewHolder(v1);
                break;
            default:
                View v = inflater.inflate(R.layout.search_item, parent, false);
                viewHolder = new MyViewHolder(v);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.textView.setText(list.get(position).getSearch());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position) instanceof Search) {
            return BOOKS;
        }
        return -1;
    }

    public void setOnItemClick(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onClickListener != null){
                onClickListener.onItemClick(getAdapterPosition(), itemView);
            }
        }
    }
}
