package debugbridge.mybooks;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import debugbridge.mybooks.Fragments.Profile;
import debugbridge.mybooks.Fragments.RBooks;
import debugbridge.mybooks.Fragments.SellBooks;
import debugbridge.mybooks.Utility.BottomFloatingBehavior;
import debugbridge.mybooks.Utility.BottomNavigationViewBehavior;
import debugbridge.mybooks.Utility.ImageHelper;

;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navigation;
    private FloatingActionButton button;
    public static final int IMAGE_REQUEST_CODE = 9162;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            //updateToolbarText(item.getTitle());
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    replaceFragment(new RBooks());
                    return true;

//                case R.id.navigation_sell:
//                    replaceFragment(new SellBooks());
//                    return true;

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

        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);
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

            boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);
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
        }/*else if (tag.equals(SellBooks.class.getName())){
            navigation.setSelectedItemId(R.id.navigation_sell);
        }*/else {
            navigation.setSelectedItemId(R.id.navigation_profile);
        }
    }

}
