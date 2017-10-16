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
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import com.rbooks.Model.MainCategory;
import com.rbooks.Model.SubCategory;
import com.rbooks.R;
import com.rbooks.listener.OnClickListener;

public class SubCategoryRecyclerAdapter extends RecyclerView.Adapter{

    private OnClickListener onClickListener;
    private List<Object> list;
    private Context context;
    private final int TITLE = 0, CATEGORY = 1;
    private int lastPosition;
    private FragmentActivity activityCompat;

    public SubCategoryRecyclerAdapter(List<Object> list, Context context, FragmentActivity activityCompat) {
        this.list = list;
        this.context = context;
        this.activityCompat = activityCompat;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        switch (viewType) {
            case CATEGORY:
                View v1 = inflater.inflate(R.layout.subcategory_recyclerview_item, parent, false);
                viewHolder = new SubCategoryItemsViewHolder(v1);
                break;
            case TITLE:
                View v2 = inflater.inflate(R.layout.category_title_recyclerview_item, parent, false);
                viewHolder = new SubCategoryTitlesViewHolder(v2);
                break;
            default:
                View v = inflater.inflate(R.layout.subcategory_recyclerview_item, parent, false);
                viewHolder = new SubCategoryItemsViewHolder(v);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activityCompat.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        if (holder.getItemViewType() == CATEGORY) {
            SubCategory subCategory = (SubCategory) list.get(position);
            SubCategoryItemsViewHolder categoryViewHolder = (SubCategoryItemsViewHolder) holder;
            holder.itemView.getLayoutParams().width = displayMetrics.widthPixels / 4;
            categoryViewHolder.subcategory_recycler_view.setText(subCategory.getTitle());
            Picasso.with(context)
                    .load(subCategory.getImg().replace(" ","%20"))
                    .placeholder(R.drawable.placeholder)
                    .into(categoryViewHolder.subcategory_image_recycler_view);
        }else if (holder.getItemViewType() == TITLE) {
            MainCategory mainCategory = (MainCategory) list.get(position);
            SubCategoryTitlesViewHolder sliderViewHolder = (SubCategoryTitlesViewHolder) holder;
            sliderViewHolder.subcategory_title_recycler_view.setText(mainCategory.getName());
        }
        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position) instanceof MainCategory) {
            return TITLE;
        }else if (list.get(position) instanceof SubCategory){
            return CATEGORY;
        }
        return -1;
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
    public int getItemCount() {
        return list.size();
    }

    public void setOnItemClick(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    private class SubCategoryItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView subcategory_recycler_view;
        private CircleImageView subcategory_image_recycler_view;

        public SubCategoryItemsViewHolder(View itemView) {
            super(itemView);
            subcategory_recycler_view = (TextView) itemView.findViewById(R.id.subcategory_recycler_view);
            subcategory_image_recycler_view = (CircleImageView) itemView.findViewById(R.id.subcategory_image_recycler_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onClickListener != null){
                onClickListener.onItemClick(getAdapterPosition(), itemView);
            }
        }
    }

    private class SubCategoryTitlesViewHolder extends RecyclerView.ViewHolder{

        private TextView subcategory_title_recycler_view;

        public SubCategoryTitlesViewHolder(View itemView) {
            super(itemView);
            subcategory_title_recycler_view = (TextView) itemView.findViewById(R.id.subcategory_title_recycler_view);
        }
    }

}
