package nweave.com.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import nweave.com.myapplication.R;


/**
 * Created by yassermabrouk on 10/30/16.
 */

public class BaseViewBridge {

    private Activity mActivity;
    public ProgressDialog mProgressDialog;

    public BaseViewBridge(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void showProgressDialog() {
        ProgressBar progressBar = new ProgressBar(mActivity, null,android.R.attr.progressBarStyle);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(mActivity, R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mActivity);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
        mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mProgressDialog.setContentView(progressBar);
    }
    public void hideKeyboard(){
        View view = mActivity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
    public void showToastMessage(String message){
        if(null == message || "".equals(message) ){
            message = mActivity.getString(R.string.common_screen_general_error);
        }
        Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
    }

}
