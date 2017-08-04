package debugbridge.mybooks.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import debugbridge.mybooks.Adapter.ViewBooksRecyclerAdapter;
import debugbridge.mybooks.Model.Viewbooks;
import debugbridge.mybooks.R;

public class ViewBooks extends Fragment{

    private RecyclerView recyclerView;
    private ViewBooksRecyclerAdapter adapter;
    private List<Object> list;
    private String id = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragments_view_books, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id = bundle.getString("id");
        }

        list = new ArrayList<>();

        list.add(new Viewbooks("1","Title of the book","250","Book is Good","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new Viewbooks("1","Title of the book","250","Book is Good","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new Viewbooks("1","Title of the book","250","Book is Good","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new Viewbooks("1","Title of the book","250","Book is Good","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new Viewbooks("1","Title of the book","250","Book is Good","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new Viewbooks("1","Title of the book","250","Book is Good","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new Viewbooks("1","Title of the book","250","Book is Good","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new Viewbooks("1","Title of the book","250","Book is Good","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        adapter = new ViewBooksRecyclerAdapter(list, getContext(), getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.view_books_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);



        return view;
    }
}
