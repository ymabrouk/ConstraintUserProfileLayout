package nweave.com.myapplication;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import butterknife.ButterKnife;
import nweave.com.base.BaseAppCompatActivity;
import nweave.com.myapplication.retrofit.RetroPhoto;

public class NewsFeedActivity extends BaseAppCompatActivity  implements FeedFragment.OnListFragmentInteractionListener {

    @Override
    public boolean onSupportNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);
        ButterKnife.bind(this);

        if(savedInstanceState != null){
            savedInstanceState.getString("dsadasdads");
        }


        FragmentManager fm = getSupportFragmentManager();
        FeedFragment mStateFragment = (FeedFragment) fm.findFragmentByTag(FeedFragment.class.getName());
        if (mStateFragment == null) {
            mStateFragment = FeedFragment.newInstance(1);
            showFragment(mStateFragment, false);
        }
        else{
            replaceFragment(mStateFragment, false);
        }

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                int stackHeight = getSupportFragmentManager().getBackStackEntryCount();
                if (stackHeight > 0) { // if we have something on the stack (doesn't include the current shown fragment)
                    getSupportActionBar().setHomeButtonEnabled(true);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                } else {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    getSupportActionBar().setHomeButtonEnabled(false);
                }
            }

        });

    }

    @Override
    public void onListFragmentInteraction(FeedRecyclerViewAdapter.FeedViewHolder viewHolder, RetroPhoto item) {
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        // adding some parameters to bundle.
//        outState.putString("dsadasdas", "");
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onBackPressed() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
            return;
        }
        super.onBackPressed();
    }
}
