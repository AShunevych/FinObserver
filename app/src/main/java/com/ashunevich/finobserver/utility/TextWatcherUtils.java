package com.ashunevich.finobserver.utility;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class TextWatcherUtils {
    private TextWatcherInstance instance;

    public TextWatcherUtils setCallback(TextWatcherInstance callback) {
        this.instance = callback;
        return this;
    }

    public TextWatcherUtils setAfterTextChangedWatcher(final EditText editText){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                instance.afterTextChanged(editable);
            }
        });
        return this;
    }


}
