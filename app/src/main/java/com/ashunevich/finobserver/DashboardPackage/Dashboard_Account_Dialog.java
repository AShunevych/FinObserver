package com.ashunevich.finobserver.DashboardPackage;


import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ashunevich.finobserver.R;
import com.ashunevich.finobserver.UtilsPackage.CustomSpinnerAdapter;
import com.ashunevich.finobserver.databinding.DashboardNewAccountDialogBinding;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;

import static com.ashunevich.finobserver.DashboardPackage.Utils_Dashboard.KEY_DIALOG;
import static com.ashunevich.finobserver.DashboardPackage.Utils_Dashboard.KEY_UPDATE;
import static com.ashunevich.finobserver.DashboardPackage.Utils_Dashboard.enableSubmitIfReady;
import static com.ashunevich.finobserver.UtilsPackage.Utils.getSelectedItemOnSpinnerPosition;

public class Dashboard_Account_Dialog extends DialogFragment {
    private DashboardNewAccountDialogBinding binding;
    ArrayList<Drawable> images;
    int id;
    String currency;
    private String keyType;

    //receive bundle

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        assert inflater != null;
        binding = DashboardNewAccountDialogBinding.inflate(inflater, container, false);
        setTextWatcher();
        binding.okButton.setEnabled(false);
        binding.cancelButton.setOnClickListener(view -> onCancel(Objects.requireNonNull(getDialog())));
        binding.okButton.setOnClickListener(view -> onDismiss(Objects.requireNonNull(getDialog())));
        fillSpinner();

        assert getArguments() != null;
        keyType = getArguments().getString("operationKey");
        if (keyType.matches(KEY_UPDATE)){
            setTextFromBundle();
        }
        Objects.requireNonNull(getDialog()).setCanceledOnTouchOutside(true);
        return binding.getRoot();
    }

    private void setTextWatcher(){
        binding.newAccountName.addTextChangedListener(watcher);
        binding.newAccountValue.addTextChangedListener(watcher);
    }

    private void setTextFromBundle(){
        assert getArguments() != null;
        binding.newAccountName.setText(getArguments().getString("accountName"));
        binding.newAccountValue.setText(String.valueOf(getArguments().getDouble("accountValue")));
       binding.drawableSpinner.setSelection(getArguments().getInt("imageID"));
       id = getArguments().getInt("accountID");
       currency = getArguments().getString("accountCurrency");
    }

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


    private void submitToActivity() {
        if(!TextUtils.isEmpty(binding.newAccountName.getText().toString())
                && !TextUtils.isEmpty(binding.newAccountValue.getText().toString())){
            bundlePackage(keyType);
        }
    }

    private void bundlePackage(String key){
        Bundle result = new Bundle();
        result.putString("accountName",binding.newAccountName.getText().toString());
        result.putDouble("accountValue",Double.parseDouble(binding.newAccountValue.getText().toString()));
        if(currency == null){
            currency = getResources().getString(R.string.UAH);
        }
        result.putString("accountCurrency",currency);
        result.putInt("accountDrawablePos", getSelectedItemOnSpinnerPosition(binding.drawableSpinner));
        if(key.matches(KEY_UPDATE)){
            result.putInt("accountID",id);
        }
        result.putString("operationType",keyType);
        getParentFragmentManager().setFragmentResult(KEY_DIALOG,result);
    }

    private void fillSpinner(){
        images = new ArrayList<>();
        images.add(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_wallet_balance,null));
        images.add(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_bank_balance,null));
        images.add(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_other_balance,null));
        binding.drawableSpinner.setAdapter(new CustomSpinnerAdapter(requireContext(), images));
    }

    public void onDismiss(@NonNull DialogInterface dialog) {
            submitToActivity();
            super.onDismiss(dialog);
    }

    public void onCancel(@NonNull DialogInterface dialog) {
        dialog.cancel();
        super.onCancel(dialog);
    }



}


