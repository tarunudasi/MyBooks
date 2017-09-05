package debugbridge.mybooks.Fragments;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import debugbridge.mybooks.Activities.Login_screen;
import debugbridge.mybooks.MainActivity;
import debugbridge.mybooks.R;
import debugbridge.mybooks.SharedPrefs.UserData;


public class Profile extends Fragment {
    TextView name, phone, mybooks, termncondition, logout;
    ImageView imageView;

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
        imageView = (ImageView) view.findViewById(R.id.profile_image_view);

        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getRandomColor();

        name.setText(UserData.getInstance(getContext()).getUser().getName());

        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .withBorder(4) /* thickness in px */
                .endConfig()
                .buildRound(name.getText().toString().substring(0,1).toUpperCase(), color);

        imageView.setImageDrawable(drawable);
        phone.setText(UserData.getInstance(getContext()).getUser().getMobile());

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
        mybooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        termncondition = (TextView) view.findViewById(R.id.terms_condition);

        /*termncondition.setOnClickListener(new View.OnClickListener() {
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
        });*/

        logout = (TextView) view.findViewById(R.id.log_out);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(), Login_screen.class));
                UserData.getInstance(getContext()).setLogout();
                getActivity().overridePendingTransition(R.anim.right_enter, R.anim.slide_out);
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
