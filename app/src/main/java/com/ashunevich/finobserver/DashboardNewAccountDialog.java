package com.ashunevich.finobserver;


import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.ashunevich.finobserver.databinding.DashboardCreateAccountDialogBinding;
import com.ashunevich.finobserver.adapters.ImageViewSpinnerAdapter;
import com.ashunevich.finobserver.utility.Constants;
import com.ashunevich.finobserver.utility.TextWatcherUtils;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import static com.ashunevich.finobserver.utility.Utils.enableSubmitIfReady;
import static com.ashunevich.finobserver.utility.Utils.stringFromTextView;
import static com.ashunevich.finobserver.utility.Utils.doubleFromTextView;
import static com.ashunevich.finobserver.utility.Utils.getSelectedItemOnSpinnerPosition;
import static com.ashunevich.finobserver.utility.ViewUtils.uIDisableView;

public class DashboardNewAccountDialog extends DialogFragment {
    private DashboardCreateAccountDialogBinding binding;
    private int id;
    private String currency,keyType;
    private TypedArray typedImagesArrays;


    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        assert inflater != null;
        binding = DashboardCreateAccountDialogBinding.inflate(inflater, container, false);
        setAttributes();
        initTextWatchers();
        initUIStatus();
        initClickListeners();
        initSpinner();
        initKeyType();

        return binding.getRoot();
    }


    public void setAttributes() {
        setStyle (STYLE_NORMAL, R.style.RelativeDialog);
        Window window = Objects.requireNonNull (getDialog ()).getWindow();
        assert window != null;

        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        window.setAttributes(layoutParams);
    }

    //init methods
    private void initTextWatchers(){
        new TextWatcherUtils ().
                setAfterTextChangedWatcher (binding.newAccountName).
                setCallback (editable ->  enableSubmitIfReady(binding.okButton,binding.newAccountName,binding.newAccountValue));

        new TextWatcherUtils ().
                setAfterTextChangedWatcher (binding.newAccountValue).
                setCallback (editable ->  enableSubmitIfReady(binding.okButton,binding.newAccountName,binding.newAccountValue));
    }

    private void initSpinner(){
        typedImagesArrays = getResources().obtainTypedArray(R.array.Icons);
        binding.drawableSpinner.setAdapter(new ImageViewSpinnerAdapter (DashboardNewAccountDialog.this, typedImagesArrays));
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
        if (keyType.matches(Constants.KEY_UPDATE)){
            initTextFromBundle();
        }
    }

    private void initUIStatus(){
        uIDisableView(binding.okButton);
        Objects.requireNonNull(getDialog()).setCanceledOnTouchOutside(true);
    }

    private void initClickListeners(){
        binding.cancelButton.setOnClickListener(view -> onCancel(Objects.requireNonNull(getDialog())));
        binding.okButton.setOnClickListener(view -> onDismiss(Objects.requireNonNull(getDialog())));
    }

    //Utils
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
        if(key.matches(Constants.KEY_UPDATE)){
            result.putInt("accountID",id);
        }
        result.putString("operationType",keyType);
        getParentFragmentManager().setFragmentResult(Constants.DIALOG_STATIC,result);
    }

    private void submitNegativeResult(){
        Bundle result = new Bundle();
        result.putString("operationType", Constants.KEY_CANCEL);
        getParentFragmentManager().setFragmentResult(Constants.DIALOG_STATIC,result);
    }

    //Dialog
    public void onDismiss(@NonNull DialogInterface dialog) {
        submitToActivity();
        super.onDismiss(dialog);
    }

    public void onCancel(@NonNull DialogInterface dialog) {
        submitNegativeResult();
        super.onDismiss(dialog);
    }

    private String resourceNameAt(int pos){
        return  getResources().getResourceEntryName(typedImagesArrays.getResourceId (pos,0));
    }



}


