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

public class MyBooks extends Fragment {

    private RecyclerView category_recycler_view;
    private List<Object> list;
    private CategoryRecyclerAdapter categoryRecyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mybooks,container,false);

        List<String> img = new ArrayList<>();
        img.add("http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        img.add("http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        img.add("http://cdn3.nflximg.net/images/3093/2043093.jpg");
        img.add("http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");

        list = new ArrayList<>();
        list.add(new Slidder(img));
        list.add(new MainCategory("1","Competitive","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new MainCategory("2","Diploma","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTiJyqBNbAQQHyJBDvjxoyR7_VdUB1T0VoOYlI9376gPj2Wsi2R"));
        list.add(new MainCategory("3","Engineering","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQfmOAo253JOBjjEKJMr435Td_99E4_bWjdK55t9MdO186sBD0hkw"));
        list.add(new MainCategory("4","High School","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSqKQv3m3dwigJ_QBNH1GRu-o5LkkZN-W5CThsElFvIhxGravSu8Q"));
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
                fragmentTransaction.addToBackStack(MyBooks.class.getName());
                fragmentTransaction.commit();
            }
        });

        return view;
    }

}
