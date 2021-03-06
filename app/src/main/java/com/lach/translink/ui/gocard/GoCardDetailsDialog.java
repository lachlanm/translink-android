package com.lach.translink.ui.gocard;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lach.common.data.preference.Preferences;
import com.lach.common.data.preference.PreferencesProvider;
import com.lach.common.ui.dialog.ButterCustomDialogFragment;
import com.lach.translink.TranslinkApplication;
import com.lach.translink.activities.R;
import com.lach.translink.data.gocard.GoCardPreference;

import javax.inject.Inject;

import butterknife.InjectView;

public class GoCardDetailsDialog extends ButterCustomDialogFragment {

    @Inject
    PreferencesProvider preferencesProvider;

    @InjectView(R.id.gocard_number)
    EditText cardNumber;

    @InjectView(R.id.gocard_password_input_layout)
    TextInputLayout cardPasswordInputLayout;

    @InjectView(R.id.gocard_password)
    EditText cardPassword;

    public static GoCardDetailsDialog newInstance() {
        return new GoCardDetailsDialog();
    }

    @Override
    protected void onDialogCreated(Bundle savedInstanceState, AlertDialog dialog) {
        dialog.setTitle("Go-Card credentials");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Save",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Do nothing, this functionality of overridden later in onStart,
                        // this allows us to provide dialog validation.
                    }
                }
        );
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        // If any details are saved, show a clear button.
        Preferences preferences = preferencesProvider.getPreferences();
        String cardNumValue = GoCardPreference.CARD_NUMBER.get(preferences);
        String cardNumPass = GoCardPreference.PASSWORD.get(preferences);
        if (cardNumValue != null || cardNumPass != null) {
            dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Clear",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            clear();
                        }
                    }
            );
        }
    }

    @SuppressLint("InflateParams")
    @Override
    public View getDialogView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.d_gocard_details, null);
    }

    @Override
    public void onDialogInjected(Bundle savedInstanceState) {
        TranslinkApplication application = (TranslinkApplication) getActivity().getApplication();
        application.getCoreComponent().inject(this);

        Preferences preferences = preferencesProvider.getPreferences();
        String cardNumValue = GoCardPreference.CARD_NUMBER.get(preferences);
        String cardNumPass = GoCardPreference.PASSWORD.get(preferences);

        if (cardNumValue != null) {
            cardNumber.setText(cardNumValue);
        }
        if (cardNumPass != null) {
            cardPasswordInputLayout.setHint(getString(R.string.gocard_details_password_unchanged));
        } else {
            cardPasswordInputLayout.setHint(getString(R.string.gocard_details_enter_password));
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        AlertDialog d = (AlertDialog) getDialog();
        if (d != null) {
            Button positiveButton = d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (save()) {
                        dismiss();
                    }
                }
            });
        }
    }

    private boolean save() {
        FragmentActivity activity = getActivity();
        String cardNumber = this.cardNumber.getText().toString();
        String password = cardPassword.getText().toString();

        if (TextUtils.isEmpty(cardNumber) || TextUtils.isEmpty(password)) {
            Toast.makeText(activity, "You must specify a card number and password.", Toast.LENGTH_SHORT).show();
            return false;
        }

        int cardNumberRequiredLength = getResources().getInteger(R.integer.go_card_number_length);
        if (cardNumber.length() != cardNumberRequiredLength) {
            Toast.makeText(activity, "Card number must be " + cardNumberRequiredLength + " characters long.", Toast.LENGTH_SHORT).show();
            return false;
        }

        Preferences preferences = preferencesProvider.getPreferences();
        Preferences.Editor editor = preferences.edit();
        GoCardPreference.CARD_NUMBER.set(editor, cardNumber);

        if (password.length() > 0) {
            GoCardPreference.PASSWORD.set(editor, password);
        }
        editor.apply();

        Toast.makeText(activity, "Go-Card details saved.", Toast.LENGTH_SHORT).show();
        return true;
    }

    private void clear() {
        Preferences preferences = preferencesProvider.getPreferences();
        Preferences.Editor editor = preferences.edit();
        GoCardPreference.CARD_NUMBER.remove(editor);
        GoCardPreference.PASSWORD.remove(editor);
        editor.apply();

        Toast.makeText(getActivity(), "Go-Card details cleared.", Toast.LENGTH_SHORT).show();
    }

}