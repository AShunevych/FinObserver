package com.ashunevich.finobserver.DashboardAccountPackage;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class Dashboard_RecyclerViewSwipeAdapter extends ItemTouchHelper.SimpleCallback {
    private Dashboard_RecyclevrViewAdapter mAdapter;

    public Dashboard_RecyclerViewSwipeAdapter(Dashboard_RecyclevrViewAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        mAdapter = adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        mAdapter.deleteItem(position);
    }
}
