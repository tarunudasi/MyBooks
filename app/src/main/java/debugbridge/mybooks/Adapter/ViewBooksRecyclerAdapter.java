package debugbridge.mybooks.Adapter;

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

import debugbridge.mybooks.Model.Viewbooks;
import debugbridge.mybooks.R;
import debugbridge.mybooks.listener.OnClickListener;

public class ViewBooksRecyclerAdapter extends RecyclerView.Adapter{

    private List<Object> list;
    private Context context;
    private OnClickListener onClickListener;
    private final int BOOKS = 0;
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
            default:
                View v = inflater.inflate(R.layout.view_books_recycler_view_items, parent, false);
                viewHolder = new ViewBooksViewHolder(v);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activityCompat.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        if (holder.getItemViewType() == BOOKS) {
            Viewbooks viewBooks = (Viewbooks) list.get(position);
            ViewBooksViewHolder viewBooksViewHolder = (ViewBooksViewHolder) holder;
            holder.itemView.getLayoutParams().width = displayMetrics.widthPixels / 2;
            viewBooksViewHolder.title_view_books_recycler.setText(viewBooks.getTitle());
            viewBooksViewHolder.price_view_books_recycler.setText(viewBooks.getCost() + "/-");
            Picasso.with(context)
                    .load(viewBooks.getImg().replace(" ","%20"))
                    .placeholder(R.drawable.placeholder)
                    .into(viewBooksViewHolder.image_view_books_recycler);
        }
        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position) instanceof Viewbooks) {
            return BOOKS;
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

}
