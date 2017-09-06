package debugbridge.mybooks.Fragments;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import debugbridge.mybooks.Activities.Login;
import debugbridge.mybooks.MainActivity;
import debugbridge.mybooks.R;
import debugbridge.mybooks.SharedPrefs.UserData;


public class Profile extends Fragment{
    private TextView name, phone, change_number;
    private LinearLayout log_out, terms_condition, my_books, change_password;
    private ImageView imageView;

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
        change_number = (TextView) view.findViewById(R.id.change_number);
        change_password = (LinearLayout) view.findViewById(R.id.change_password);

        change_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.right_enter, R.anim.slide_out);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.add(R.id.profile_content, new ChangeMobile());
                fragmentTransaction.addToBackStack(Profile.class.getName());
                fragmentTransaction.commit();
            }
        });

        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.right_enter, R.anim.slide_out);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.add(R.id.profile_content, new ChangePassword());
                fragmentTransaction.addToBackStack(Profile.class.getName());
                fragmentTransaction.commit();

            }
        });

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

        my_books = (LinearLayout) view.findViewById(R.id.my_books);
        my_books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        terms_condition = (LinearLayout) view.findViewById(R.id.terms_condition);

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

        log_out = (LinearLayout) view.findViewById(R.id.log_out);
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(), Login.class));
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

        if (phone != null)
            phone.setText(UserData.getInstance(getContext()).getUser().getMobile());

        InputMethodManager inputMethodManager = (InputMethodManager) getActivity()
                .getSystemService(android.content.Context.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(
                getActivity().getCurrentFocus()
                        .getWindowToken(), 0);

    }

}
