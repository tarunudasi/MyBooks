package debugbridge.mybooks.Fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import debugbridge.mybooks.Activities.Login_screen;
import debugbridge.mybooks.R;


public class Profile extends Fragment {
    TextView name,phone,address,editprofile,mybooks,termncondition,logout;
    Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        name = (TextView) view.findViewById(R.id.name);
        phone = name = (TextView) view.findViewById(R.id.phone);
        address = (TextView) view.findViewById(R.id.address);
        editprofile = (TextView) view.findViewById(R.id.edit_profile);

        /******On click Edit profile view*************/

        editprofile.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        View popupView = getActivity().getLayoutInflater().inflate(R.layout.edit_custom_dialog_box, null);

        PopupWindow popupWindow = new PopupWindow(popupView,
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);

        // If you need the PopupWindow to dismiss when when touched outside
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        EditText naME=(EditText)popupView.findViewById(R.id.input_name);
        int location[] = new int[2];
        popupWindow.showAtLocation(view, Gravity.CENTER,
                location[0], location[1] + view.getHeight());


    }
});

        mybooks = (TextView) view.findViewById(R.id.my_books);



        /**on click my books***/


        mybooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        termncondition = (TextView) view.findViewById(R.id.terms_condition);
        /**** on click terms and condition **/


        termncondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View popupView = getActivity().getLayoutInflater().inflate(R.layout.termsncondition, null);

                PopupWindow popupWindow = new PopupWindow(popupView,
                        WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                popupWindow.setFocusable(true);

                // If you need the PopupWindow to dismiss when when touched outside
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                TextView termsncondition=(TextView)popupView.findViewById(R.id.termscondi);

                int location[] = new int[2];
                popupWindow.showAtLocation(view, Gravity.CENTER,
                        location[0], location[1] + view.getHeight());


            }
        });
        logout = (TextView) view.findViewById(R.id.log_out);

        /******* on click logout */
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity().getApplicationContext(), Login_screen.class);
                startActivity(intent);
                getActivity().finish();

            }
        });









        return view;


}


    public void getDetails() {


    }
}
