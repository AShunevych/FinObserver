package com.ashunevich.finobserver.DashboardAccountPackage;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

class Dashboard_DiffUtil extends DiffUtil.Callback {

    List<Dashboard_Account> oldList;

    public Dashboard_DiffUtil(List<Dashboard_Account> oldList, List<Dashboard_Account>newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    List<Dashboard_Account> newList;

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
        return oldList.get(oldItemPosition).getAccountID() == newList.get(newItemPosition).getAccountID();
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
