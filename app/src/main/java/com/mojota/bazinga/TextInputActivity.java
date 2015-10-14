package com.mojota.bazinga;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by mojota on 15-10-14.
 */
public class TextInputActivity extends ToolBarActivity {

    private TextInputLayout mTiUsername;
    private TextInputLayout mTiPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_textinput);
        mTiUsername = (TextInputLayout) findViewById(R.id.ti_username);
        mTiPassword = (TextInputLayout) findViewById(R.id.ti_password);
        EditText edUsername = mTiUsername.getEditText();
        EditText edPassword = mTiPassword.getEditText();

        edUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 2){
                    mTiUsername.setError(getString(R.string.str_username_error));
                    mTiUsername.setErrorEnabled(true);
                } else {
                    mTiUsername.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() > 2) {
                    mTiPassword.setError(getString(R.string.str_password_error));
                    mTiPassword.setErrorEnabled(true);
                } else {
                    mTiPassword.setErrorEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
