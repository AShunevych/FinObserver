package com.ashunevich.finobserver.DashboardAccountPackage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ashunevich.finobserver.AccountPackage.Account_Item;
import com.ashunevich.finobserver.R;
import com.ashunevich.finobserver.databinding.DashboardAccountItemBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Dashboard_RecyclevrViewAdapter extends RecyclerView.Adapter<Dashboard_RecyclevrViewAdapter.MyViewHolder>  {
    private ArrayList<Account_Item> pad_list;
    Account_Item thisItem;

    public Dashboard_RecyclevrViewAdapter(ArrayList<Account_Item> data){
        this.pad_list = data;
    }
    //This method inflates view present in the RecyclerView
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(DashboardAccountItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    //Binding the data using get() method of POJO object
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Account_Item item  = pad_list.get(position);
        holder.binding.accountIco.setImageDrawable(item.getImage());
        holder.binding.accountType.setText(item.getAccountType());
        holder.binding.accountValue.setText(String.valueOf(item.getAccountValue()));
        holder.binding.accountCurrency.setText(item.getAccountCurrency());
            /*
        holder.binding.deleteAccount.setOnClickListener(view -> {
           // Log.d("Value at this pos",String.valueOf(thisItem.getAccountValue()));
            deleteItem(position);
        });

             */



        ///когда отнимается через интерефес отправляется новое значение в фрагнмент
    }

    protected void deleteItem (int position){
        pad_list.remove(position);
        notifyItemRemoved(position);
       // notifyItemRangeChanged(position,pad_list.size());
    }





    //Setting the arraylist
    public void setListContent(ArrayList <Account_Item> pad_list) {
        this.pad_list = pad_list;
    }

    @Override
    public int getItemCount() {
        return pad_list.size();
    }

    //View holder class, where all view components are defined
    static class MyViewHolder extends RecyclerView.ViewHolder {
        private final DashboardAccountItemBinding binding;
        public MyViewHolder(DashboardAccountItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
//TODO  (1.6) Update item when accountValue change


    protected String summAllItemsValue(RecyclerView viewRec){
        double totalPrice = 0;
        for (int i = 0; i<viewRec.getChildCount(); i++)
        {
            RecyclerView.ViewHolder holder = viewRec.getChildViewHolder(viewRec.getChildAt(i));
            TextView view = holder.itemView.findViewById(R.id.accountValue);
            totalPrice += Double.parseDouble(view.getText().toString());
        }

        return String.valueOf(totalPrice);
    }
}
