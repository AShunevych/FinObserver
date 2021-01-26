package com.ashunevich.finobserver.UtilsPackage;

import com.ashunevich.finobserver.TransactionsPackage.Transaction_Item;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

public class TransactionDiffUtilCallback extends DiffUtil.Callback {

    ArrayList<Transaction_Item> oldList;

    public TransactionDiffUtilCallback(ArrayList<Transaction_Item> oldList, ArrayList<Transaction_Item> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    ArrayList<Transaction_Item> newList;

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getItemIID() == newList.get(newItemPosition).getItemIID();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        //you can return particular field for changed item.
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
