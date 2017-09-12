package debugbridge.mybooks.Activities;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import debugbridge.mybooks.Adapter.PagerAdapter;
import debugbridge.mybooks.Fragments.AuthorTab;
import debugbridge.mybooks.Fragments.PublicationTab;
import debugbridge.mybooks.Fragments.TitleTab;
import debugbridge.mybooks.MyReceiver.ConnectivityReceiver;
import debugbridge.mybooks.R;

public class SearchableActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private PagerAdapter pagerAdapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0.0f);

        tabLayout = (TabLayout) findViewById(R.id.search_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Book Name"));
        tabLayout.addTab(tabLayout.newTab().setText("Author"));
        tabLayout.addTab(tabLayout.newTab().setText("Publication"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        final ViewPager viewPager = (ViewPager) findViewById(R.id.search_viewpager);
        pagerAdapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        searchView.onActionViewExpanded();

        searchView.setOnQueryTextListener(this);

        searchView.setBackgroundColor(Color.WHITE);

        SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setHintTextColor(Color.DKGRAY);
        searchAutoComplete.setTextColor(Color.BLACK);

        ImageView searchCloseIcon = (ImageView)searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        searchCloseIcon.setImageResource(R.drawable.ic_search_clear);

        ImageView searchIcon = (ImageView)searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
        searchIcon.setImageResource(R.drawable.ic_action_search);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        overridePendingTransition(R.anim.right_enter, R.anim.slide_out);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        //dataFromActivityToFragment.sendData(getApplicationContext(),newText);
        //adapter.filter(text);
        int position = tabLayout.getSelectedTabPosition();
        Fragment fragment = pagerAdapter.getFragment(tabLayout.getSelectedTabPosition());

        if (fragment != null) {
            switch (position) {
                case 0:
                    ((TitleTab) fragment).search(newText);
                    break;
                case 1:
                    ((AuthorTab) fragment).search(newText);
                    break;
                case 2:
                    ((PublicationTab) fragment).search(newText);
                    break;
            }
        }

        if (!ConnectivityReceiver.isConnected()){
            final Dialog alert = new Dialog(this, R.style.full_screen_dialog);
            alert.setContentView(R.layout.internet_customize_alert);
            alert.setCancelable(true);
            alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    onBackPressed();
                }
            });
            alert.setCanceledOnTouchOutside(false);
            alert.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT);
            Button button = (Button) alert.getWindow().findViewById(R.id.try_again_internet_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ConnectivityReceiver.isConnected()){
                        alert.dismiss();
                    }
                }
            });

            alert.show();
        }

        return false;
    }

}
