package com.tixon.sectionedrecyclerview;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tixon.sectionedrecyclerview.databinding.SectionBinding;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by tikhon.osipov on 04.08.2016
 */
public class SectionedRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int SECTION_TYPE = 0;

    private boolean valid = true;
    private RecyclerView.Adapter baseAdapter;
    private SparseArray<Section> sections;

    public SectionedRecyclerAdapter(final RecyclerView.Adapter baseAdapter) {
        this.baseAdapter = baseAdapter;
        this.sections = new SparseArray<>();

        this.baseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                valid = baseAdapter.getItemCount() > 0;
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                valid = baseAdapter.getItemCount() > 0;
                notifyItemRangeChanged(positionStart, itemCount);
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                valid = baseAdapter.getItemCount() > 0;
                notifyItemRangeInserted(positionStart, itemCount);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                valid = baseAdapter.getItemCount() > 0;
                notifyItemRangeRemoved(positionStart, positionStart);
            }
        });
    }

    public void setSections(Section[] sections) {
        this.sections.clear();

        Arrays.sort(sections, new Comparator<Section>() {
            @Override
            public int compare(Section section, Section section1) {
                return (section.firstPosition == section1.firstPosition)?
                        0 : ((section.firstPosition < section1.firstPosition)?
                                -1: 1);
            }
        });
        int offset = 0;
        for(Section section: sections) {
            section.sectionedPosition = section.firstPosition + offset;
            this.sections.append(section.sectionedPosition, section);
            ++offset;
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == SECTION_TYPE) {
            SectionBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.section, parent, false);
            return new SectionViewHolder(binding.getRoot());
        } else return baseAdapter.onCreateViewHolder(parent, viewType -1);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(isSectionHeaderPosition(position)) {
            //((SectionViewHolder) holder).sectionTitle.setText(sections.get(position).getTitle());
            ((SectionViewHolder) holder).binding.setSection(sections.get(position));
        } else {
            baseAdapter.onBindViewHolder(holder, sectionedPositionToPosition(position));
        }
    }

    @Override
    public int getItemCount() {
        return valid && baseAdapter != null && sections != null?
                baseAdapter.getItemCount() + sections.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return isSectionHeaderPosition(position)? SECTION_TYPE:
                baseAdapter.getItemViewType(sectionedPositionToPosition(position))+1;
    }

    @Override
    public long getItemId(int position) {
        return isSectionHeaderPosition(position) ?
                Integer.MAX_VALUE - sections.indexOfKey(position) :
                baseAdapter.getItemId(sectionedPositionToPosition(position));
    }

    private boolean isSectionHeaderPosition(int position) {
        return sections.get(position) != null;
    }

    public int sectionedPositionToPosition(int sectionedPosition) {
        if(isSectionHeaderPosition(sectionedPosition)) return RecyclerView.NO_POSITION;
        int offset = 0;

        for(int i = 0; i < sections.size(); i++) {
            if(sections.valueAt(i).sectionedPosition > sectionedPosition) break;
            --offset;
        }
        return sectionedPosition + offset;
    }

    public int positionToSectionedPosition(int position) {
        int offset = 0;
        for (int i = 0; i < sections.size(); i++) {
            if(sections.valueAt(i).firstPosition > position) break;
            ++offset;
        }
        return position + offset;
    }

    private static class SectionViewHolder extends RecyclerView.ViewHolder {
        SectionBinding binding;

        SectionViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    public static class Section {
        int firstPosition;
        int sectionedPosition;
        CharSequence title;

        public Section(int firstPosition, CharSequence title) {
            this.firstPosition = firstPosition;
            this.title = title;
        }

        public CharSequence getTitle() {
            return title;
        }
    }
}
