package nweave.com.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import nweave.com.base.BaseAppCompatActivity;
import nweave.com.base.Constants;
import nweave.com.myapplication.R;

public class MainListActivity extends BaseAppCompatActivity implements ItemFragment.OnListFragmentInteractionListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    public void onListFragmentInteraction(DummyItem item) {
//        showToastMessage(item.getTitle());
         String title = item.getTitle();
        switch (item.getTitle()){
            case Constants.ANIMATED_USER_PROFILE:
                startActivityTransition(new Intent(this, UserProfileActivity.class));
                break;
            case Constants.FRAGMENT_TRANSITIONS_WITH_SHARED_ELEMENTS:
                startActivityTransition(new Intent(this, NewsFeedActivity.class));
                break;
        }

    }
}
