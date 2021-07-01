package com.ashunevich.finobserver.dashboard;


import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ashunevich.finobserver.R;
import com.ashunevich.finobserver.databinding.DashboardNewaccountBinding;
import com.ashunevich.finobserver.utils.CustomSpinnerAdapter;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import static com.ashunevich.finobserver.dashboard.DashboardUtils.DIALOG_STATIC;
import static com.ashunevich.finobserver.dashboard.DashboardUtils.KEY_CANCEL;
import static com.ashunevich.finobserver.dashboard.DashboardUtils.KEY_UPDATE;
import static com.ashunevich.finobserver.dashboard.DashboardUtils.enableSubmitIfReady;
import static com.ashunevich.finobserver.dashboard.DashboardUtils.stringFromTextView;
import static com.ashunevich.finobserver.dashboard.DashboardUtils.doubleFromTextView;
import static com.ashunevich.finobserver.utils.Utils.getSelectedItemOnSpinnerPosition;

public class AccountDialog extends DialogFragment {
    private DashboardNewaccountBinding binding;
    private int id;
    private String currency,keyType;
    private TypedArray typedImagesArrays;


    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        assert inflater != null;
        binding = DashboardNewaccountBinding.inflate(inflater, container, false);
        initTextWatchers();
        initUIStatus();
        initClickListeners();
        initSpinner();
        initKeyType();

        return binding.getRoot();
    }

    //init methods
    private void initTextWatchers(){
        binding.newAccountName.addTextChangedListener(watcher);
        binding.newAccountValue.addTextChangedListener(watcher);
    }

    private void initSpinner(){
        typedImagesArrays = getResources().obtainTypedArray(R.array.Icons);
        binding.drawableSpinner.setAdapter(new CustomSpinnerAdapter(AccountDialog.this, typedImagesArrays));
    }

    private void initTextFromBundle(){
        assert getArguments() != null;
        binding.newAccountName.setText(getArguments().getString("accountName"));
        binding.newAccountValue.setText(String.valueOf(getArguments().getDouble("accountValue")));
        binding.drawableSpinner.setSelection(getArguments().getInt("imageID"));
        id = getArguments().getInt("accountID");
        currency = getArguments().getString("accountCurrency");
    }

    private void initKeyType(){
        assert getArguments() != null;
        keyType = getArguments().getString("operationKey");
        if (keyType.matches(KEY_UPDATE)){
            initTextFromBundle();
        }
    }

    private void initUIStatus(){
        binding.okButton.setEnabled(false);
        Objects.requireNonNull(getDialog()).setCanceledOnTouchOutside(true);
    }

    private void initClickListeners(){
        binding.cancelButton.setOnClickListener(view -> onCancel(Objects.requireNonNull(getDialog())));
        binding.okButton.setOnClickListener(view -> onDismiss(Objects.requireNonNull(getDialog())));
    }

    //Utils
    TextWatcher watcher = new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void afterTextChanged(Editable editable) {
               enableSubmitIfReady(binding.okButton,binding.newAccountName,binding.newAccountValue);
           }
       };

    //submit result
    private void submitToActivity() {
        if(!TextUtils.isEmpty(stringFromTextView (binding.newAccountName))
                && !TextUtils.isEmpty(stringFromTextView (binding.newAccountValue))){
            submitPositiveResult(keyType);
        }
    }

    private void submitPositiveResult(String key){
        Bundle result = new Bundle();
        result.putString("accountName", stringFromTextView (binding.newAccountName));
        result.putDouble("accountValue", doubleFromTextView (binding.newAccountValue));
        if(currency == null){
            currency = getResources().getString(R.string.UAH);
        }
        result.putString("accountCurrency",currency);
        result.putString("accountImageName",resourceNameAt(getSelectedItemOnSpinnerPosition(binding.drawableSpinner)));
        if(key.matches(KEY_UPDATE)){
            result.putInt("accountID",id);
        }
        result.putString("operationType",keyType);
        getParentFragmentManager().setFragmentResult(DIALOG_STATIC,result);
    }

    private void submitNegativeResult(){
        Bundle result = new Bundle();
        result.putString("operationType", KEY_CANCEL);
        getParentFragmentManager().setFragmentResult(DIALOG_STATIC,result);
    }

    //Dialog
    public void onDismiss(@NonNull DialogInterface dialog) {
        submitToActivity();
        super.onDismiss(dialog);
    }

    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        submitNegativeResult();
        dialog.cancel();
    }

    private String resourceNameAt(int pos){
        return  getResources().getResourceEntryName(typedImagesArrays.getResourceId (pos,0));
    }



}


