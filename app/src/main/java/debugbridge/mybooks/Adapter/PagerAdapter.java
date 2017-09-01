package debugbridge.mybooks.Adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

import debugbridge.mybooks.Fragments.AuthorTab;
import debugbridge.mybooks.Fragments.PublicationTab;
import debugbridge.mybooks.Fragments.TitleTab;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    private final SparseArray<WeakReference<Fragment>> instantiatedFragments = new SparseArray<>();

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                TitleTab tab1 = new TitleTab();
                return tab1;
            case 1:
                AuthorTab tab2 = new AuthorTab();
                return tab2;
            case 2:
                PublicationTab tab3 = new PublicationTab();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final Fragment fragment = (Fragment) super.instantiateItem(container, position);
        instantiatedFragments.put(position, new WeakReference<>(fragment));
        return fragment;
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        instantiatedFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    @Nullable
    public Fragment getFragment(final int position) {
        final WeakReference<Fragment> wr = instantiatedFragments.get(position);
        if (wr != null) {
            return wr.get();
        } else {
            return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}