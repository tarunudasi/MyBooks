package debugbridge.mybooks;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import debugbridge.mybooks.Fragments.Profile;
import debugbridge.mybooks.Fragments.RBooks;
import debugbridge.mybooks.Fragments.SellBooks;
import debugbridge.mybooks.Utility.BottomFloatingBehavior;
import debugbridge.mybooks.Utility.BottomNavigationViewBehavior;
import debugbridge.mybooks.Utility.ImageHelper;

public class MainActivity extends AppCompatActivity{

    private BottomNavigationView navigation;
    private FloatingActionButton button;
    public static final int IMAGE_REQUEST_CODE = 9162;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment fragment = null;
            FragmentManager fragmentManager = getSupportFragmentManager();

            switch (item.getItemId()) {
                case R.id.navigation_home:

                    replaceFragment(new RBooks());
                    return true;

                case R.id.navigation_profile:
                    replaceFragment(new Profile());
                    return true;
            }
            return false;
        }
    };

    private void replaceFragment (Fragment fragment){
        String backStateName =  fragment.getClass().getName();
        FragmentManager manager = getSupportFragmentManager();

        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);
//        fragment not in back stack, create it.
        if (!fragmentPopped && manager.findFragmentByTag(backStateName) == null){
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(R.id.content, fragment, backStateName);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehavior());

        button = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(ImageHelper.getPickImageChooserIntent(MainActivity.this), IMAGE_REQUEST_CODE);
            }
        });

        CoordinatorLayout.LayoutParams layoutParams1 = (CoordinatorLayout.LayoutParams) button.getLayoutParams();
        layoutParams1.setBehavior(new BottomFloatingBehavior());

        //removeTextLabel(navigation, R.id.navigation_home);
        //removeTextLabel(navigation, R.id.navigation_profile);

        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigation.getChildAt(0);

        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams params = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

            // set your height here
            params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, displayMetrics);
            // set your width here
            params.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, displayMetrics);
            iconView.setLayoutParams(params);
        }

    }


    private void removeTextLabel(@NonNull BottomNavigationView bottomNavigationView, @IdRes int menuItemId) {
        View view = bottomNavigationView.findViewById(menuItemId);
        if (view == null) return;
        if (view instanceof MenuView.ItemView) {
            ViewGroup viewGroup = (ViewGroup) view;
            int padding = 0;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View v = viewGroup.getChildAt(i);
                if (v instanceof ViewGroup) {
                    padding = v.getHeight();
                    viewGroup.getChildAt(i).setVisibility(View.GONE);
                    viewGroup.removeViewAt(i);
                }
            }
            viewGroup.setPadding(view.getPaddingLeft(), (viewGroup.getPaddingTop() + padding) / 2, view.getPaddingRight(), view.getPaddingBottom());

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle bundle = new Bundle();
            Uri imageUri = ImageHelper.getPickImageResultUri(this,data);
            bundle.putString("imageUri", imageUri.toString());
            Fragment fragment = new SellBooks();
            fragment.setArguments(bundle);
            String backStateName =  fragment.getClass().getName();
            FragmentManager manager = getSupportFragmentManager();

            boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);
//        fragment not in back stack, create it.
            if (!fragmentPopped && manager.findFragmentByTag(backStateName) == null){
                FragmentTransaction ft = manager.beginTransaction();
                ft.add(R.id.content, fragment, backStateName);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(backStateName);
                ft.commit();
            }

        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0){
            String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
            if (getSupportFragmentManager().findFragmentByTag(tag).getChildFragmentManager().getBackStackEntryCount() > 0){
                getSupportFragmentManager().findFragmentByTag(tag).getChildFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }else if (getSupportFragmentManager().getBackStackEntryCount() > 1){
                super.onBackPressed();
                updateNavigationColor();
            }else if (getSupportFragmentManager().getBackStackEntryCount() == 1){
                finish();
            }
        }
    }

    private void updateNavigationColor(){
        String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
        if (tag.equals(RBooks.class.getName())){
            navigation.setSelectedItemId(R.id.navigation_home);
        }else if (tag.equals(Profile.class.getName())){
            navigation.setSelectedItemId(R.id.navigation_profile);
        }
    }

}
