package base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatEditText;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.nweave.provider.R;
import com.nweave.provider.login.presenter.CorporateLoginPresenter;

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

    public void promoteUserPhoneAlert(final CorporateLoginPresenter presenter){
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        final AppCompatEditText edittext = new AppCompatEditText(getContext());
        alert.setMessage(getString(R.string.login_screen_phone_verification_dialog_message));
        alert.setTitle(getString(R.string.login_screen_phone_verification_dialog_title));
        edittext.setPadding(40, 10,40,10);
        edittext.setSupportBackgroundTintList(AppCompatResources.getColorStateList(getContext(), android.R.color.darker_gray));
        edittext.setInputType(InputType.TYPE_CLASS_PHONE);
        edittext.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        alert.setView(edittext);


        alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                presenter.sendOTP(getContext(), edittext.getText().toString());
            }
        });

        alert.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        AlertDialog alertDialog = alert.create();
        alertDialog.show();
        Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negativeButton.setTextColor(getResources().getColor(android.R.color.black));

        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setTextColor(getResources().getColor(android.R.color.black));

    }

    @Override
    public void onStop() {
        super.onStop();
        baseViewBridge.hideProgressDialog();
    }
}
