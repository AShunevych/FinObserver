package com.ashunevich.finobserver.DashboardAccountPackage;


import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;


import com.ashunevich.finobserver.R;
import com.ashunevich.finobserver.UtilsPackage.CustomSpinnerAdapter;
import com.ashunevich.finobserver.databinding.DashboardNewAccountDialogBinding;

import java.util.ArrayList;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;

public class Dashboard_NewAccountDialog extends DialogFragment {
    private DashboardNewAccountDialogBinding binding;
    ArrayList<Drawable> images;



    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        assert inflater != null;
        binding = DashboardNewAccountDialogBinding.inflate(inflater, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setTextWatcher();
        binding.okButton.setEnabled(false);
        binding.cancelButton.setOnClickListener(view -> onCancel(Objects.requireNonNull(getDialog())));
        binding.okButton.setOnClickListener(view -> onDismiss(Objects.requireNonNull(getDialog())));
        fillSpinner();
        Objects.requireNonNull(getDialog()).setCanceledOnTouchOutside(true);
        return binding.getRoot();
    }

    private void setTextWatcher(){
        binding.newAccountName.addTextChangedListener(watcher);
        binding.newAccountValue.addTextChangedListener(watcher);
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
               enableSubmitIfReady(binding.newAccountName,binding.newAccountValue);
           }
       };


    private void enableSubmitIfReady(EditText text1, EditText text2) {
        binding.okButton.setEnabled(getText(text1).length() > 0 && getText(text2).length() > 0);
    }

    private String getText(EditText text) {
        return text.getText().toString().trim();
    }


    private void postValue() {
        if(!TextUtils.isEmpty(binding.newAccountName.getText().toString())  && !TextUtils.isEmpty(binding.newAccountValue.getText().toString())){
            Bundle result = new Bundle();
            result.putString("nameResult",binding.newAccountName.getText().toString());
            result.putDouble("doubleResult",Double.parseDouble(binding.newAccountValue.getText().toString()));
            result.putString("currencyResult","UAH");
            result.putInt("idResult",DrawablePostion());
            getParentFragmentManager().setFragmentResult("fragmentKey",result);
        }

    }


    private int DrawablePostion(){
        return binding.drawableSpinner.getSelectedItemPosition();
    }


    private void fillSpinner(){
        images = new ArrayList<>();
        images.add(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_wallet_balance,null));
        images.add(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_bank_balance,null));
        images.add(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_other_balance,null));
        CustomSpinnerAdapter mCustomAdapter = new CustomSpinnerAdapter(requireContext(), images);
        binding.drawableSpinner.setAdapter(mCustomAdapter);
    }

    public void onDismiss(@NonNull DialogInterface dialog) {
            postValue();
            super.onDismiss(dialog);
    }

    public void onCancel(@NonNull DialogInterface dialog) {
        dialog.cancel();
        super.onCancel(dialog);
    }



}


