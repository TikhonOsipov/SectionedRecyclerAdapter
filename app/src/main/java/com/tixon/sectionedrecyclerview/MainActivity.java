package com.tixon.sectionedrecyclerview;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;

import com.tixon.sectionedrecyclerview.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    SimpleRecyclerAdapter simpleRecyclerAdapter;
    SectionedRecyclerAdapter sectionedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);

        List<String> items = new ArrayList<>();
        items.add("0");
        items.add("1");
        items.add("2");
        items.add("3");
        items.add("4");
        items.add("5");
        items.add("6");
        items.add("7");
        items.add("8");
        items.add("9");

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        simpleRecyclerAdapter = new SimpleRecyclerAdapter(items); //here sectionedAdapter is null yet

        List<SectionedRecyclerAdapter.Section> sections = new ArrayList<>();
        sections.add(new SectionedRecyclerAdapter.Section(2, "Section 1"));
        sections.add(new SectionedRecyclerAdapter.Section(4, "Section 2"));
        sections.add(new SectionedRecyclerAdapter.Section(6, "Section 3"));
        sections.add(new SectionedRecyclerAdapter.Section(8, "Section 4"));

        SectionedRecyclerAdapter.Section[] dummy = new SectionedRecyclerAdapter.Section[sections.size()];
        sectionedAdapter = new SectionedRecyclerAdapter(simpleRecyclerAdapter);
        sectionedAdapter.setSections(sections.toArray(dummy));

        simpleRecyclerAdapter.setSectionedAdapterReference(sectionedAdapter);

        binding.recyclerView.setAdapter(sectionedAdapter);

        /*//SectionedRecyclerAdapter adapter = new SectionedRecyclerAdapter(items);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        //binding.recyclerView.setAdapter(adapter);*/
    }
}
