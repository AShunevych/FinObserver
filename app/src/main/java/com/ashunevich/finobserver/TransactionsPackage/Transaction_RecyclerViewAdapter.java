package com.ashunevich.finobserver.TransactionsPackage;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ashunevich.finobserver.databinding.TransactionItemBinding;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class Transaction_RecyclerViewAdapter extends RecyclerView.Adapter<Transaction_RecyclerViewAdapter.MyViewHolder>  {
    private ArrayList<Transaction_Item> pad_list;
    //   private PassBalanceValue mListerner;
    public Transaction_RecyclerViewAdapter(ArrayList<Transaction_Item> data){
        this.pad_list = data;
    }
    //This method inflates view present in the RecyclerView
    @NonNull
    @Override
    public Transaction_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Transaction_RecyclerViewAdapter.MyViewHolder(TransactionItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    //Binding the data using get() method of POJO object
    @Override
    public void onBindViewHolder(final Transaction_RecyclerViewAdapter.MyViewHolder holder, int position) {
        final Transaction_Item item  = pad_list.get(position);
        holder.binding.transactionID.setText(String.valueOf(item.getItemIID()));
        holder.binding.transactionTypeImage.setImageDrawable(item.getImage());
        holder.binding.transactionValue.setText(item.getTransactionValue());
        holder.binding.transactionCurrency.setText(String.valueOf(item.getTransactionCurrency()));
        holder.binding.tranactionAccountFrom.setText(item.getTransactionAccount());
        holder.binding.transactionCategory.setText(item.getTransactionCategory());
        holder.binding.transactionDate.setText(item.getTransactionDate());
    }


    //Setting the arraylist
    public void setListContent(ArrayList <Transaction_Item> pad_list) {
        this.pad_list = pad_list;
    }

    @Override
    public int getItemCount() {
        return pad_list.size();
    }

    //View holder class, where all view components are defined
    static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TransactionItemBinding binding;
        public MyViewHolder(TransactionItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void updateItemList(ArrayList<Transaction_Item> items){
        final Transaction_DiffUtilCallback diffCallback = new Transaction_DiffUtilCallback(this.pad_list, items);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.pad_list.clear();
        this.pad_list.addAll(items);
        diffResult.dispatchUpdatesTo(this);
    }

/*
    public interface PassBalanceValue {
        void passData(Double value);
    }
*/
}

