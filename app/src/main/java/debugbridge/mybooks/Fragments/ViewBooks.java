package debugbridge.mybooks.Fragments;


import android.content.Intent;
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

import debugbridge.mybooks.Activities.BookDescription;
import debugbridge.mybooks.Adapter.ViewBooksRecyclerAdapter;
import debugbridge.mybooks.MainActivity;
import debugbridge.mybooks.Model.BookLists;
import debugbridge.mybooks.R;
import debugbridge.mybooks.listener.OnClickListener;

public class ViewBooks extends Fragment{

    private RecyclerView recyclerView;
    private ViewBooksRecyclerAdapter adapter;
    private List<Object> list;
    private String id = "", title = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragments_view_books, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id = bundle.getString("id");
            title = bundle.getString("title");
        }

        ((MainActivity)getActivity()).updateToolbarText(title);

        /*((MainActivity)getActivity()).title.setText(title);
        ((MainActivity)getActivity()).subtitle.setVisibility(View.GONE);
        ((MainActivity)getActivity()).toolbar_image.setVisibility(View.GONE);

*/
        list = new ArrayList<>();

        list.add(new BookLists("1","Title of the book","250","Book is Good","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png","Mr. X", "1234567890"));
        list.add(new BookLists("1","Title of the book","250","Book is Good","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png","Mr. Y", "345636546"));
        list.add(new BookLists("1","Title of the book","250","Book is Good","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png","Mr. Z", "45674567456"));
        list.add(new BookLists("1","Title of the book","250","Book is Good","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png","Mr. A", "564645634"));
        list.add(new BookLists("1","Title of the book","250","Book is Good","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png","Mr. B", "45646456456"));
        list.add(new BookLists("1","Title of the book","250","Book is Good","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png","Mr. C", "54646456456"));
        list.add(new BookLists("1","Title of the book","250","Book is Good","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png","Mr. D", "456456456456"));
        list.add(new BookLists("1","Title of the book","250","Book is Good","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png","Mr. E", "456456456456"));

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        adapter = new ViewBooksRecyclerAdapter(list, getContext(), getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.view_books_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClick(new OnClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                BookLists bookLists = (BookLists) list.get(position);
                Intent intent = new Intent(getActivity(), BookDescription.class);
                intent.putExtra("data", bookLists);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_enter,R.anim.slide_out);
            }
        });


        return view;
    }



}
