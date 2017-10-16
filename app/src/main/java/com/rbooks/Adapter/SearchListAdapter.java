package com.rbooks.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.rbooks.Model.Search;
import com.rbooks.R;



public class SearchListAdapter extends BaseAdapter {

    // Declare Variables

    Context mContext;
    LayoutInflater inflater;
    private List<Search> SearchList = null;
    private ArrayList<Search> arraylist;

    public SearchListAdapter(Context context, List<Search> SearchList) {
        mContext = context;
        this.SearchList = SearchList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(SearchList);
    }

    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        return SearchList.size();
    }

    @Override
    public Search getItem(int position) {
        return SearchList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.search_item, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.item);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(SearchList.get(position).getSearch());
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        SearchList.clear();
        if (charText.length() == 0) {
            SearchList.addAll(arraylist);
        }
        else
        {
            for (Search wp : arraylist)
            {
                if (wp.getSearch().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    SearchList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }


}