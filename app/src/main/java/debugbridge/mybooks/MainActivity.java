package debugbridge.mybooks;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import debugbridge.mybooks.Fragments.MyBooks;
import debugbridge.mybooks.Fragments.Profile;
import debugbridge.mybooks.Fragments.SellBooks;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            updateToolbarText(item.getTitle());
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    replaceFragment(new MyBooks());
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);
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

    private void updateToolbarText(CharSequence text) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(text);
        }
    }

    private void updateNavigationColor(){
        String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
        if (tag.equals(MyBooks.class.getName())){
            navigation.setSelectedItemId(R.id.navigation_home);
        }else if (tag.equals(SellBooks.class.getName())){
            navigation.setSelectedItemId(R.id.navigation_sell);
        }else {
            navigation.setSelectedItemId(R.id.navigation_profile);
        }
    }

}
