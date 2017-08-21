package debugbridge.mybooks.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import debugbridge.mybooks.Adapter.SubCategoryRecyclerAdapter;
import debugbridge.mybooks.Model.MainCategory;
import debugbridge.mybooks.Model.SubCategory;
import debugbridge.mybooks.R;
import debugbridge.mybooks.listener.OnClickListener;

public class BooksList extends Fragment {

    private RecyclerView booklist_recyclerview;
    private SubCategoryRecyclerAdapter adapter;
    private List<Object> list;
    private String id = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_list, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id = bundle.getString("id");
        }

        list = new ArrayList<>();

        list.add(new MainCategory("1","Competitive","http://kaplonoverseas.com/wordpress/wp-content/uploads/2015/11/Top-10-Most-Difficult-Exams-in-the-World-506x250.jpg"));
        list.add(new SubCategory("1","Quantitative","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("2","Reasoning","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("3","GA","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("4","Banking","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));


        list.add(new MainCategory("2","Diploma","https://www.ccny.cuny.edu/sites/default/files/styles/top_slider/public/Diploma%20Image_0.jpg?itok=MF9pnjWk"));
        list.add(new SubCategory("1","Quantitative","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("2","Reasoning","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("3","GA","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("4","Banking","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("1","Quantitative","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("2","Reasoning","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("3","GA","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("4","Banking","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("1","Quantitative","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("2","Reasoning","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("3","GA","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("4","Banking","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("1","Quantitative","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("2","Reasoning","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("3","GA","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("4","Banking","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("1","Quantitative","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("2","Reasoning","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("3","GA","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("4","Banking","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("1","Quantitative","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("2","Reasoning","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("3","GA","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("4","Banking","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));



        list.add(new MainCategory("3","Engineering","http://salearnership.co.za/wp-content/uploads/2016/06/engineering.jpg"));
        list.add(new SubCategory("1","Quantitative","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("2","Reasoning","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("3","GA","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("4","Banking","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("1","Quantitative","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("2","Reasoning","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("3","GA","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("4","Banking","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("1","Quantitative","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("2","Reasoning","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("3","GA","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("4","Banking","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("1","Quantitative","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("2","Reasoning","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("3","GA","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("4","Banking","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));

        list.add(new MainCategory("4","High School","https://hhsvoyager.org/wp-content/uploads/2017/05/635907199239163531550951598_highschool-index.jpg"));
        list.add(new SubCategory("1","Quantitative","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("2","Reasoning","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("3","GA","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("4","Banking","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("1","Quantitative","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("2","Reasoning","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("3","GA","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("4","Banking","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("1","Quantitative","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("2","Reasoning","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("3","GA","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("4","Banking","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("1","Quantitative","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("2","Reasoning","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("3","GA","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("4","Banking","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));

        list.add(new MainCategory("5","Medical","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT9bZ2tM96hdWqurCH96_7KmFJpO9N6sMrwPkqx4O6Rv6zydNmG"));
        list.add(new SubCategory("1","Quantitative","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("2","Reasoning","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("3","GA","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("4","Banking","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("1","Quantitative","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("2","Reasoning","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("3","GA","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("4","Banking","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("1","Quantitative","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));


        adapter = new SubCategoryRecyclerAdapter(list,getContext(), getActivity());

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch(adapter.getItemViewType(position)){
                    case 0:
                        return 4;
                    case 1:
                        return 1;
                    default:
                        return -1;
                }
            }
        });

        gridLayoutManager.scrollToPosition(getPosition());
        booklist_recyclerview = (RecyclerView) view.findViewById(R.id.booklist_recyclerview);
        booklist_recyclerview.setLayoutManager(gridLayoutManager);
        booklist_recyclerview.setAdapter(adapter);
        booklist_recyclerview.setHasFixedSize(true);
        booklist_recyclerview.setItemAnimator(new DefaultItemAnimator());
        adapter.setOnItemClick(new OnClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                SubCategory subCategory = (SubCategory) list.get(position);
                Fragment fragment = new ViewBooks();
                Bundle bundle = new Bundle();
                bundle.putString("id", subCategory.getId());
                bundle.putString("title", subCategory.getTitle());
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

    private int getPosition(){
        for (int i = 0; i < list.size() ; i++){
            if(list.get(i) instanceof MainCategory){
                if (id.equals(((MainCategory) list.get(i)).getId()))
                    return i;
            }
        }
        return 0;
    }

}
