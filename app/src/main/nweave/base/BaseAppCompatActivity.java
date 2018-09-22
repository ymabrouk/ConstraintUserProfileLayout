package base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.nweave.provider.R;

/**
 * Created by yassermabrouk on 11/26/16.
 */

public class BaseAppCompatActivity extends AppCompatActivity {

    protected FirebaseAuth mAuth;
    private BaseViewBridge baseViewBridge;
    protected Fragment curFragment;

    protected void hideKeyboard(){
        baseViewBridge.hideKeyboard();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        baseViewBridge = new BaseViewBridge(this);

    }

    protected void showProgressDialog() {
        baseViewBridge.showProgressDialog();
    }

    protected void hideProgressDialog() {
       baseViewBridge.hideProgressDialog();
    }

    protected void showToastMessage(String message){
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

    protected void showFragment(Fragment fragment) {
        try {
            curFragment = fragment;
            String fragmentTag = fragment.getClass().getName();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_layout, fragment, fragmentTag).addToBackStack(fragmentTag).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
