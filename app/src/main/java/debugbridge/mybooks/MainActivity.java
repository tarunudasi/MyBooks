package debugbridge.mybooks;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import debugbridge.mybooks.Fragments.Profile;
import debugbridge.mybooks.Fragments.RBooks;
import debugbridge.mybooks.Fragments.SellBooks;
import debugbridge.mybooks.Utility.BottomNavigationViewBehavior;

;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navigation;

    public TextView title, subtitle;
    public ImageView toolbar_image;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            updateToolbarText(item.getTitle());
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    replaceFragment(new RBooks());
                    return true;

                case R.id.navigation_sell:
                    replaceFragment(new SellBooks());
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_back_arrow);
        upArrow.setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehavior());

        //handleIntent(getIntent());

        /*title = (TextView) findViewById(R.id.toolbar_title);
        subtitle = (TextView) findViewById(R.id.toolbar_subtitle);
        toolbar_image = (ImageView) findViewById(R.id.toolbar_location_image);
        */
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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

    public void updateToolbarText(CharSequence text) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(text);
        }
    }

    private void updateNavigationColor(){
        String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
        if (tag.equals(RBooks.class.getName())){
            navigation.setSelectedItemId(R.id.navigation_home);
        }else if (tag.equals(SellBooks.class.getName())){
            navigation.setSelectedItemId(R.id.navigation_sell);
        }else {
            navigation.setSelectedItemId(R.id.navigation_profile);
        }
    }

}
