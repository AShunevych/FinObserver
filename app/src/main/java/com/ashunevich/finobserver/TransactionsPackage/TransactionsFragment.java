package com.ashunevich.finobserver.TransactionsPackage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ashunevich.finobserver.databinding.TransactionsFragmentBinding;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TransactionsFragment extends Fragment {
    private TransactionsFragmentBinding binding;

    public TransactionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = TransactionsFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }
}
