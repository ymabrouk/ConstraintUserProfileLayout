package nweave.com.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
/**
 * Created by yassermabrouk on 10/27/16.
 */

public class BaseFragment extends Fragment {


    private BaseViewBridge baseViewBridge;

    protected void showProgressDialog() {
        baseViewBridge.showProgressDialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseViewBridge = new BaseViewBridge(getActivity());
    }

    protected void hideKeyboard(){
        baseViewBridge.hideKeyboard();
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
        baseViewBridge.hideProgressDialog();
    }
}
