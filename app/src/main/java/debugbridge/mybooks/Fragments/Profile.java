package debugbridge.mybooks.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import debugbridge.mybooks.Activities.Login_screen;
import debugbridge.mybooks.MainActivity;
import debugbridge.mybooks.R;


public class Profile extends Fragment {
    TextView name,phone,address,mybooks,termncondition,logout;
    ImageView imageView;
    Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        name = (TextView) view.findViewById(R.id.name);
        phone = (TextView) view.findViewById(R.id.phone);
        address = (TextView) view.findViewById(R.id.address);
        imageView = (ImageView) view.findViewById(R.id.profile_image_view);

        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getRandomColor();

        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .withBorder(4) /* thickness in px */
                .endConfig()
                .buildRound(name.getText().toString().substring(0,1).toUpperCase(), color);

        imageView.setImageDrawable(drawable);

        /******On click Edit profile view*************/

        /*editprofile.setOnClickListener(new View.OnClickListener() {
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
});*/

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


    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Profile");
        ((MainActivity)getActivity()).getSupportActionBar().setSubtitle(null);

        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_back_arrow);
        upArrow.setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
        ((MainActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(upArrow);

        for (int i = 0 ; i < menu.size(); i++){
            menu.getItem(i).setVisible(false);
        }
    }
}
