package nweave.com.base;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Window;

import nweave.com.myapplication.R;

/**
 * Created by yassermabrouk on 11/26/16.
 */

public class BaseAppCompatActivity extends AppCompatActivity {

    private BaseViewBridge baseViewBridge;
    protected Fragment curFragment;

    protected void hideKeyboard() {
        baseViewBridge.hideKeyboard();
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
        baseViewBridge = new BaseViewBridge(this);

    }

    protected void showProgressDialog() {
        baseViewBridge.showProgressDialog();
    }

    protected void hideProgressDialog() {
        baseViewBridge.hideProgressDialog();
    }

    protected void showToastMessage(String message) {
        baseViewBridge.showToastMessage(message);
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    protected void showFragment(Fragment fragment, boolean addToBackStack) {
        try {
            curFragment = fragment;
            String fragmentTag = fragment.getClass().getName();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_layout, fragment, fragmentTag);
            if (addToBackStack) {
                fragmentTransaction.addToBackStack(fragmentTag);
            }
            fragmentTransaction.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected void replaceFragment(Fragment fragment, boolean addToBackStack) {
        try {
            curFragment = fragment;
            String fragmentTag = fragment.getClass().getName();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_layout, fragment, fragmentTag);
            if (addToBackStack) {
                fragmentTransaction.addToBackStack(fragmentTag);
            }
            fragmentTransaction.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void startActivityTransition(Intent intent){
        // Check if we're running on Android 5.0 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Apply activity transition

            getWindow().setAllowEnterTransitionOverlap(true);

            // set an exit transition

//            getWindow().setEnterTransition(new Slide());
            getWindow().setExitTransition(new Explode());

            startActivity(intent,
                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle());

        } else {
            // Swap without transition
            startActivity(intent);
        }
    }

}
