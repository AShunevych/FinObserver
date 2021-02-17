package com.ashunevich.finobserver.TransactionsPackage;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ashunevich.finobserver.R;
import com.ashunevich.finobserver.databinding.TransactionItemBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

class RecyclerView_Adapter extends RecyclerView.Adapter<RecyclerView_Adapter.MyViewHolder>  {
    private List<Transaction_Item> pad_list;
    Context context;
    //   private PassBalanceValue mListerner;
    public RecyclerView_Adapter(List<Transaction_Item> data){
        this.pad_list = data;
    }
    //This method inflates view present in the RecyclerView
    @NonNull
    @Override
    public RecyclerView_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new RecyclerView_Adapter.MyViewHolder(TransactionItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    //Binding the data using get() method of POJO object
    @Override
    public void onBindViewHolder(final RecyclerView_Adapter.MyViewHolder holder, int position) {
        final Transaction_Item item  = pad_list.get(position);
      holder.binding.transactionTypeImage.setImageDrawable(getTypeImage(item.getImageInt()));
         holder.binding.transactionValue.setText(String.valueOf(item.getTransactionValue()));
        holder.binding.transactionCurrency.setText(item.getTransactionCurrency());
        holder.binding.tranactionAccountFrom.setText(item.getTransactionAccount());
        holder.binding.transactionCategory.setText(item.getTransactionCategory());
        holder.binding.transactionDate.setText(item.getTransactionDate());
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

    private Drawable getTypeImage(int i) {

        if(i == 0){
            return ContextCompat.getDrawable(context, R.drawable.ic_arrow_drop_up);
        }
        else if (i==1){
            return ContextCompat.getDrawable(context, R.drawable.ic_arrow_drop_down);
        }
        else{
            return ContextCompat.getDrawable(context, R.drawable.ic_transfer);
        }
    }

    //Setting the arraylist
    public void updateItemList(List<Transaction_Item> items){
        final RecyclerView_DiffUtil diffCallback = new RecyclerView_DiffUtil(this.pad_list, items);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.pad_list.clear();
        this.pad_list.addAll(items);
        diffResult.dispatchUpdatesTo(this);
    }


}

