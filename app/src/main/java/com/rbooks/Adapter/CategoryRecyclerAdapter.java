package com.rbooks.Adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.rbooks.Model.MainCategory;
import com.rbooks.Model.Slidder;
import com.rbooks.R;
import com.rbooks.listener.OnClickListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter{

    List<Object> list;
    Context context;
    OnClickListener onClickListener;
    private final int CATEGORY = 0, HEADER = 1, ADMOB = 2;
    private int lastPosition;
    private FragmentActivity activityCompat;

    public CategoryRecyclerAdapter(List<Object> list, Context context, FragmentActivity activityCompat) {
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
                View v1 = inflater.inflate(R.layout.category_recycler_view_items, parent, false);
                viewHolder = new CategoryViewHolder(v1);
                break;
            case HEADER:
                View v2 = inflater.inflate(R.layout.slidder_item, parent, false);
                viewHolder = new SliderViewHolder(v2);
                break;
            case ADMOB:
                View v3 = inflater.inflate(R.layout.large_admob_item, parent, false);
                viewHolder = new AdMobViewHolder(v3);
                break;
            default:
                View v = inflater.inflate(R.layout.category_recycler_view_items, parent, false);
                viewHolder = new CategoryViewHolder(v);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        activityCompat.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        if (holder.getItemViewType() == CATEGORY){
            MainCategory category = (MainCategory) list.get(position);
            CategoryViewHolder categoryViewHolder = (CategoryViewHolder) holder;
            categoryViewHolder.itemView.getLayoutParams().height = displayMetrics.heightPixels / 3;
            categoryViewHolder.category_text_rv.setText(category.getName());
            Picasso.with(context)
                    .load(category.getImg().replace(" ","%20"))
                    .placeholder(R.drawable.placeholder)
                    .into(categoryViewHolder.category_image_rv);
        }else if (holder.getItemViewType() == HEADER){

            Slidder slidder = (Slidder) list.get(position);
            SliderViewHolder sliderViewHolder = (SliderViewHolder) holder;

            HashMap<String,String> url_maps = new HashMap<>();

            for (int i = 0 ; i < slidder.getList().size() ; i++){
                url_maps.put(i+1+"",slidder.getList().get(i).replace(" ","%20"));
            }

            for(String name : url_maps.keySet()){
                DefaultSliderView textSliderView = new DefaultSliderView(context);
                // initialize a SliderLayout
                textSliderView
                        .description(name)
                        .image(url_maps.get(name))
                        .setScaleType(BaseSliderView.ScaleType.Fit);

                //add your extra information
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle()
                        .putString("extra",name);
                sliderViewHolder.mDemoSlider.addSlider(textSliderView);
            }
            sliderViewHolder.mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Stack);
            sliderViewHolder.mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            sliderViewHolder.mDemoSlider.setCustomAnimation(new DescriptionAnimation());
            sliderViewHolder.mDemoSlider.setDuration(4000);
        }
        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return list.size();
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
    public int getItemViewType(int position) {
        if (list.get(position) instanceof MainCategory) {
            return CATEGORY;
        }else if (list.get(position) instanceof Slidder){
            return HEADER;
        }else if (list.get(position).equals("AdMob")){
            return ADMOB;
        }
        return -1;
    }

    public void setOnItemClick(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    private class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView category_image_rv;
        private TextView category_text_rv;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            category_image_rv = (ImageView) itemView.findViewById(R.id.category_image_rv);
            category_text_rv = (TextView) itemView.findViewById(R.id.category_text_rv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onClickListener != null){
                onClickListener.onItemClick(getAdapterPosition(),v);
            }
        }
    }


    private class SliderViewHolder extends RecyclerView.ViewHolder{

        private SliderLayout mDemoSlider;

        public SliderViewHolder(View itemView) {
            super(itemView);
            mDemoSlider = (SliderLayout)itemView.findViewById(R.id.slider);
        }
    }

    private class AdMobViewHolder extends RecyclerView.ViewHolder {
        private AdView adViewItem;
        private AdRequest adRequest;

        public AdMobViewHolder(View itemView) {
            super(itemView);
            adViewItem = (AdView) itemView.findViewById(R.id.adViewItemLarge);

            adRequest = new AdRequest.Builder()
                    .build();

            adViewItem.loadAd(adRequest);

        }
    }

}
