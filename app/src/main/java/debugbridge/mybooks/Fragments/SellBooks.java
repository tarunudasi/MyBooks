package debugbridge.mybooks.Fragments;

import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import debugbridge.mybooks.MainActivity;
import debugbridge.mybooks.R;

public class SellBooks extends Fragment implements AdapterView.OnItemSelectedListener{


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sell_books,container,false);
        Spinner category=(Spinner)view.findViewById(R.id.category);
        EditText description=(EditText)view.findViewById(R.id.description) ;
        ImageView sell_book=(ImageView)view.findViewById(R.id.bookimage);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Competitive");
        categories.add("Diploma");
        categories.add("Engineering");
        categories.add("High School");
        categories.add("Medical");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        category.setAdapter(dataAdapter);



        return view;


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
