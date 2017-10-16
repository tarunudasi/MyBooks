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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.rbooks.Model.Books;
import com.rbooks.R;
import com.rbooks.listener.OnClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ViewBooksRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> list;
    private Context context;
    private OnClickListener onClickListener;
    private final int BOOKS = 1, PROGRESS = 0, NO_INTERNET = 2, ADMOB = 3;
    private int lastPosition;
    private FragmentActivity activityCompat;

    public ViewBooksRecyclerAdapter(List<Object> list, Context context, FragmentActivity activityCompat) {
        this.list = list;
        this.context = context;
        this.activityCompat = activityCompat;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        switch (viewType) {
            case BOOKS:
                View v1 = inflater.inflate(R.layout.view_books_recycler_view_items, parent, false);
                viewHolder = new ViewBooksViewHolder(v1);
                break;
            case PROGRESS:
                View v2 = inflater.inflate(R.layout.progress_loading_items, parent, false);
                viewHolder = new ProgressViewHolder(v2);
                break;
            case NO_INTERNET:
                View v3 = inflater.inflate(R.layout.no_internet_item, parent, false);
                viewHolder = new NoInternetViewHolder(v3);
                break;
            case ADMOB:
                View v4 = inflater.inflate(R.layout.admob_item, parent, false);
                viewHolder = new AdMobViewHolder(v4);
                break;
            default:
                View v = inflater.inflate(R.layout.view_books_recycler_view_items, parent, false);
                viewHolder = new ViewBooksViewHolder(v);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activityCompat.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        if (holder.getItemViewType() == BOOKS) {
            Books viewBooks = (Books) list.get(position);
            ViewBooksViewHolder viewBooksViewHolder = (ViewBooksViewHolder) holder;
            holder.itemView.getLayoutParams().width = displayMetrics.widthPixels / 2;
            holder.itemView.getLayoutParams().height = displayMetrics.heightPixels / 3;
            viewBooksViewHolder.title_view_books_recycler.setText(viewBooks.getName());
            viewBooksViewHolder.price_view_books_recycler.setText(viewBooks.getAmount() + "/-");
            Picasso.with(context)
                    .load(viewBooks.getImg().replace(" ","%20"))
                    .placeholder(R.drawable.placeholder)
                    .into(viewBooksViewHolder.image_view_books_recycler);
        }

        //setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemViewType(int position) {
        //return list.get(position) != null ? BOOKS : PROGRESS;

        if (list.get(position) == null){
            return PROGRESS;
        }else if (list.get(position) instanceof Books){
            return BOOKS;
        }else if (list.get(position).equals("No Internet")){
            return NO_INTERNET;
        }else if (list.get(position).equals("AdMob")){
            return ADMOB;
        }else
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

    private class ViewBooksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView image_view_books_recycler;
        private TextView title_view_books_recycler, price_view_books_recycler;

        public ViewBooksViewHolder(View itemView) {
            super(itemView);
            image_view_books_recycler = (ImageView) itemView.findViewById(R.id.image_view_books_recycler);
            title_view_books_recycler = (TextView) itemView.findViewById(R.id.title_view_books_recycler);
            price_view_books_recycler = (TextView) itemView.findViewById(R.id.price_view_books_recycler);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onClickListener != null){
                onClickListener.onItemClick(getAdapterPosition(), itemView);
            }
        }
    }

    private class ProgressViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;

        public ProgressViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_item);
        }
    }


    private class NoInternetViewHolder extends RecyclerView.ViewHolder {
        private Button retry_internet;

        public NoInternetViewHolder(View itemView) {
            super(itemView);
            retry_internet = (Button) itemView.findViewById(R.id.retry_internet);
            retry_internet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onItemClick(getAdapterPosition(), v);
                }
            });
        }
    }

    private class AdMobViewHolder extends RecyclerView.ViewHolder {
        private AdView adViewItem;
        private AdRequest adRequest;

        public AdMobViewHolder(View itemView) {
            super(itemView);
            adViewItem = (AdView) itemView.findViewById(R.id.adViewItem);

            adRequest = new AdRequest.Builder()
                    .build();

            adViewItem.loadAd(adRequest);

        }
    }

}
