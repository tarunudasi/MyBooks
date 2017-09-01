package debugbridge.mybooks.Fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.victor.loading.book.BookLoading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import debugbridge.mybooks.Adapter.SubCategoryRecyclerAdapter;
import debugbridge.mybooks.AppVolley.SingletonVolley;
import debugbridge.mybooks.Model.MainCategory;
import debugbridge.mybooks.Model.SubCategory;
import debugbridge.mybooks.R;
import debugbridge.mybooks.Utility.UrlConstant;
import debugbridge.mybooks.listener.OnClickListener;

public class BooksList extends Fragment {

    private RecyclerView booklist_recyclerview;
    private SubCategoryRecyclerAdapter adapter;
    private List<Object> list;
    private List<MainCategory> mainCategoryList;
    private List<SubCategory> subCategoryList;
    private String id = "";
    private GridLayoutManager gridLayoutManager;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_list, container, false);

        /*((MainActivity)getActivity()).title.setText("Book Categories");
        ((MainActivity)getActivity()).subtitle.setVisibility(View.GONE);
        ((MainActivity)getActivity()).toolbar_image.setVisibility(View.GONE);
*/
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id = bundle.getString("id");
        }

        list = new ArrayList<>();
        mainCategoryList = new ArrayList<>();
        subCategoryList = new ArrayList<>();

        getCategory();

        progressDialog = new ProgressDialog(getContext(), R.style.full_screen_dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(true);
        progressDialog.show();
        progressDialog.getWindow().setContentView(R.layout.progress_dialog_sell_books);
        progressDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        BookLoading bookLoading = (BookLoading) progressDialog.getWindow().findViewById(R.id.book_loading_progress);
        bookLoading.start();

        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                getActivity().onBackPressed();
            }
        });

        adapter = new SubCategoryRecyclerAdapter(list,getContext(), getActivity());

        gridLayoutManager = new GridLayoutManager(getContext(), 4);
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

    private void prepareList(){

        for (int i = 0 ; i < mainCategoryList.size() ; i++){
            list.add(mainCategoryList.get(i));
            for (int j = 0 ; j < subCategoryList.size() ; j++){
                if (subCategoryList.get(j).getCategoryId().trim().equals(mainCategoryList.get(i).getId())){
                    list.add(subCategoryList.get(j));
                    //subCategoryList.remove(j);
                    adapter.notifyDataSetChanged();
                }
            }
        }

        mainCategoryList.clear();
        subCategoryList.clear();

        gridLayoutManager.scrollToPosition(getPosition());
        progressDialog.dismiss();

    }

    private void getCategory(){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlConstant.CATEGORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equals("unsuccessful")){
                            return;
                        }

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("categories");

                            for (int i = 0 ; i < jsonArray.length() ; i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                mainCategoryList.add(new MainCategory(object.getString("id"),object.getString("name"),object.getString("img")));
                                adapter.notifyDataSetChanged();
                            }

                            getSubCategory();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        SingletonVolley.getInstance(getContext()).addToRequestQueue(stringRequest);
    }


    private void getSubCategory(){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlConstant.SUBCATEGORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equals("unsuccessful")){
                            return;
                        }

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("subcategories");

                            for (int i = 0 ; i < jsonArray.length() ; i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                subCategoryList.add(new SubCategory(object.getString("id"),object.getString("category"),object.getString("name"),object.getString("img")));
                                adapter.notifyDataSetChanged();
                            }

                            prepareList();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

        };

        SingletonVolley.getInstance(getContext()).addToRequestQueue(stringRequest);

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

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.getItem(0).setVisible(false);
        menu.getItem(1).setVisible(false);
    }

}
