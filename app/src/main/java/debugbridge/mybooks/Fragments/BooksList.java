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

import debugbridge.mybooks.Adapter.SubCategoryRecyclerAdapter;
import debugbridge.mybooks.Model.MainCategory;
import debugbridge.mybooks.Model.SubCategory;
import debugbridge.mybooks.R;

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

        list.add(new MainCategory("1","Competitive","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("1","Quantitative","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("2","Reasoning","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("3","GA","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));
        list.add(new SubCategory("4","Banking","https://www.smartadvantage.com/wp-content/uploads/2013/07/CCA-Books.png"));


        list.add(new MainCategory("2","Diploma","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTiJyqBNbAQQHyJBDvjxoyR7_VdUB1T0VoOYlI9376gPj2Wsi2R"));
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



        list.add(new MainCategory("3","Engineering","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQfmOAo253JOBjjEKJMr435Td_99E4_bWjdK55t9MdO186sBD0hkw"));
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

        list.add(new MainCategory("4","High School","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSqKQv3m3dwigJ_QBNH1GRu-o5LkkZN-W5CThsElFvIhxGravSu8Q"));
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

//        list.add(new MainCategory("5","Medical","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT9bZ2tM96hdWqurCH96_7KmFJpO9N6sMrwPkqx4O6Rv6zydNmG"));


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
