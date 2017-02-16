package com.tixon.sectionedrecyclerview;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tixon.sectionedrecyclerview.databinding.ListItemBinding;

import java.util.List;

/**
 * Created by tikhon.osipov on 16.02.2017
 */

public class SimpleRecyclerAdapter extends RecyclerView
        .Adapter<SimpleRecyclerAdapter.SimpleViewHolder> {
    private List<String> data;
    private SectionedRecyclerAdapter sectionedAdapter;

    public SimpleRecyclerAdapter(List<String> data) {
        this.data = data;
    }

    public void setSectionedAdapterReference(SectionedRecyclerAdapter sectionedAdapter) {
        this.sectionedAdapter = sectionedAdapter;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item, parent, false);
        return new SimpleViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        holder.binding.text.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data == null? 0: data.size();
    }

    class SimpleViewHolder extends RecyclerView.ViewHolder {
        ListItemBinding binding;

        SimpleViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            binding.clickField.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.w("myLogs", getClass().getSimpleName() + " (simple): clickedPosition: " +
                            sectionedAdapter.sectionedPositionToPosition(getAdapterPosition()));
                }
            });
        }
    }
}
