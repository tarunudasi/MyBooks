package com.rbooks.Adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import com.rbooks.Model.Books;
import com.rbooks.R;
import com.rbooks.listener.OnClickListener;

public class MyBooksRecyclerAdapter extends RecyclerView.Adapter{

    private List<Object> list;
    private Context context;
    private OnClickListener onClickListener;
    private int lastPosition;
    private FragmentActivity activityCompat;

    public MyBooksRecyclerAdapter(List<Object> list, Context context, FragmentActivity activityCompat) {
        this.list = list;
        this.context = context;
        this.activityCompat = activityCompat;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.mybooks_recycler_view_item, parent, false);
        viewHolder = new MyBookViewHolder(v);
        return viewHolder;
    }

    public void setOnItemClick(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    private void setAnimation(View viewToAnimate, int position) {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animation = ObjectAnimator.ofFloat(viewToAnimate,"translationY", position > lastPosition ? 200 : -200, 0);
        animation.setDuration(1000);
        animatorSet.playTogether(animation);
        animatorSet.start();
        lastPosition = position;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activityCompat.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        holder.itemView.getLayoutParams().width = displayMetrics.widthPixels / 2;
        holder.itemView.getLayoutParams().height = displayMetrics.heightPixels / 3;

        Books viewBooks = (Books) list.get(position);

        ((MyBookViewHolder) holder).title_my_books_recycler.setText(viewBooks.getName());
        ((MyBookViewHolder) holder).price_my_books_recycler.setText(viewBooks.getAmount() + "/-");

        if (viewBooks.getVerify().equals("0")){
            ((MyBookViewHolder) holder).my_books_not_verified_image.setVisibility(View.VISIBLE);
            ((MyBookViewHolder) holder).my_books_not_verified_image.setImageResource(R.drawable.not_approved);
        }else if (viewBooks.getVerify().equals("1")){
            ((MyBookViewHolder) holder).my_books_not_verified_image.setVisibility(View.VISIBLE);
            ((MyBookViewHolder) holder).my_books_not_verified_image.setImageResource(R.drawable.verfied);
        }else if (viewBooks.getVerify().equals("2")){
            ((MyBookViewHolder) holder).my_books_not_verified_image.setVisibility(View.VISIBLE);
            ((MyBookViewHolder) holder).my_books_not_verified_image.setImageResource(R.drawable.reject);
        }else {
            ((MyBookViewHolder) holder).my_books_not_verified_image.setVisibility(View.GONE);
        }


        if (viewBooks.getVerify().equals("1") && viewBooks.getSold().equals("1")) {
            ((MyBookViewHolder) holder).my_books_not_verified_image.setImageResource(R.drawable.sold_out);
            ((MyBookViewHolder) holder).my_books_not_verified_image.setVisibility(View.VISIBLE);
        }

        Picasso.with(context)
                .load(viewBooks.getImg().replace(" ","%20"))
                .placeholder(R.drawable.placeholder)
                .into(((MyBookViewHolder) holder).image_my_books_recycler);

        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class MyBookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView image_my_books_recycler,my_books_not_verified_image;
        private TextView title_my_books_recycler, price_my_books_recycler;

        public MyBookViewHolder(View itemView) {
            super(itemView);
            image_my_books_recycler = (ImageView) itemView.findViewById(R.id.image_my_books_recycler);
            title_my_books_recycler = (TextView) itemView.findViewById(R.id.title_my_books_recycler);
            price_my_books_recycler = (TextView) itemView.findViewById(R.id.price_my_books_recycler);
            my_books_not_verified_image = (ImageView) itemView.findViewById(R.id.my_books_not_verified_image);
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
