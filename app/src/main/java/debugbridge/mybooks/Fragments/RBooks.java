package debugbridge.mybooks.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import debugbridge.mybooks.Adapter.CategoryRecyclerAdapter;
import debugbridge.mybooks.Model.MainCategory;
import debugbridge.mybooks.Model.Slidder;
import debugbridge.mybooks.R;
import debugbridge.mybooks.listener.OnClickListener;

public class RBooks extends Fragment {

    private RecyclerView category_recycler_view;
    private List<Object> list;
    private CategoryRecyclerAdapter categoryRecyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mybooks,container,false);

        List<String> img = new ArrayList<>();
        img.add("https://16815-presscdn-0-13-pagely.netdna-ssl.com/wp-content/uploads/2015/10/students-in-grp.151119.jpg");
        img.add("https://i0.wp.com/rbms.info/wp-content/uploads/2014/05/special-collections-books-700x300.jpg?fitu003d700%2C300");
        img.add("http://www.arch.rpi.edu/wp-content/uploads/2011/09/Library-Photos-3544_OPT.jpg");
        img.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSwnUoUq0ERQxpd7Xs7Osj62uG04PfCvhaFykl9h1_2jbgWDxaRWw");

        list = new ArrayList<>();
        list.add(new Slidder(img));
        list.add(new MainCategory("1","Competitive","http://kaplonoverseas.com/wordpress/wp-content/uploads/2015/11/Top-10-Most-Difficult-Exams-in-the-World-506x250.jpg"));
        list.add(new MainCategory("2","Diploma","https://www.ccny.cuny.edu/sites/default/files/styles/top_slider/public/Diploma%20Image_0.jpg?itok=MF9pnjWk"));
        list.add(new MainCategory("3","Engineering","http://salearnership.co.za/wp-content/uploads/2016/06/engineering.jpg"));
        list.add(new MainCategory("4","High School","https://hhsvoyager.org/wp-content/uploads/2017/05/635907199239163531550951598_highschool-index.jpg"));
        list.add(new MainCategory("5","Medical","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT9bZ2tM96hdWqurCH96_7KmFJpO9N6sMrwPkqx4O6Rv6zydNmG"));

        categoryRecyclerAdapter = new CategoryRecyclerAdapter(list,getContext());

        category_recycler_view = (RecyclerView) view.findViewById(R.id.category_recycler_view);
        category_recycler_view.setHasFixedSize(true);
        category_recycler_view.setItemAnimator(new DefaultItemAnimator());
        category_recycler_view.setAdapter(categoryRecyclerAdapter);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        category_recycler_view.setLayoutManager(mLayoutManager);
        categoryRecyclerAdapter.setOnItemClick(new OnClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                MainCategory mainCategory = (MainCategory) list.get(position);
                Fragment fragment = new BooksList();
                Bundle bundle = new Bundle();
                bundle.putString("id", mainCategory.getId());
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.add(R.id.home_content, fragment);
                fragmentTransaction.addToBackStack(RBooks.class.getName());
                fragmentTransaction.commit();
            }
        });

        return view;
    }

}
