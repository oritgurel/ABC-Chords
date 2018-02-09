package com.oritmalki.mymusicapp2.ui.mainscreen;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxItemDecoration;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.oritmalki.mymusicapp2.R;
import com.oritmalki.mymusicapp2.database.DataRepository;
import com.oritmalki.mymusicapp2.model.Measure;
import com.oritmalki.mymusicapp2.viewmodel.MeasureListViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FlexboxLayoutManager layoutManager;
    private DataRepository dataRepository;
    private FloatingActionButton addBut;
    private FloatingActionButton remBut;
    private OnClickListener listener;
    MeasuresAdapter measuresAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MeasureListViewModel viewModel = ViewModelProviders.of(this).get(MeasureListViewModel.class);

        initializeViews(viewModel);

        observeViewModel(viewModel);

    }

    private void observeViewModel(MeasureListViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getMeasures().observe(this, new Observer<List<Measure>>() {
            @Override
            public void onChanged(@Nullable List<Measure> measures) {
                if (measures != null) {
                    measuresAdapter = new MeasuresAdapter(getApplicationContext());
                    measuresAdapter.setMeasuresList(measures, getApplicationContext());
                    recyclerView.setAdapter(measuresAdapter);
                    recyclerView.smoothScrollToPosition(measures.size());
                }
            }
        });
    }

    public void initializeViews(MeasureListViewModel viewModel) {

        listener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.add_fab:
                        addEmptyMeasure(viewModel);
                        break;
                    case R.id.remove_fab:
                        deleteMeasure(viewModel);
                        break;
                }
            }
        };


        addBut = findViewById(R.id.add_fab);
        addBut.setOnClickListener(listener);

        remBut = findViewById(R.id.remove_fab);
        remBut.setOnClickListener(listener);

        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new FlexboxLayoutManager(MainActivity.this);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setAlignItems(AlignItems.STRETCH);


        FlexboxItemDecoration itemDecoration = new FlexboxItemDecoration(MainActivity.this);
        itemDecoration.setDrawable(getApplicationContext().getDrawable(R.drawable.divider));
        itemDecoration.setOrientation(FlexboxItemDecoration.BOTH);
        recyclerView.addItemDecoration(itemDecoration);
        layoutManager.getBaseline();
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.addOnScrollListener(new OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                recyclerView.getScrollState();
//            }
//        });



    }


    public void addEmptyMeasure(MeasureListViewModel viewModel) {
        viewModel.addEmptyMeasure(getApplication());

    }

    public void deleteMeasure(MeasureListViewModel viewModel) {
        viewModel.deleteMeasure(getApplication());

    }

    private final MeasureClickCallback measureClickCallback = new MeasureClickCallback() {
        @Override
        public void onClick(Measure measure) {
//            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
//                ((MainActivity) getActivity()).show(measure);
//            }
        }
    };


}
