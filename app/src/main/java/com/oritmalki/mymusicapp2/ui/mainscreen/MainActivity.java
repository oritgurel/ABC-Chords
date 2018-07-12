package com.oritmalki.mymusicapp2.ui.mainscreen;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxItemDecoration;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.oritmalki.mymusicapp2.R;
import com.oritmalki.mymusicapp2.database.MeasureRepository;
import com.oritmalki.mymusicapp2.model.Beat;
import com.oritmalki.mymusicapp2.model.Measure;
import com.oritmalki.mymusicapp2.ui.mainscreen.EditFragment.OnFragmentInteractionListener;
import com.oritmalki.mymusicapp2.ui.mainscreen.MeasuresAdapter.MeasureHolder;
import com.oritmalki.mymusicapp2.utils.StringQueueArray;
import com.oritmalki.mymusicapp2.viewmodel.MeasureListViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    private RecyclerView recyclerView;
    private FlexboxLayoutManager layoutManager;
    private MeasureRepository measureRepository;
    private FloatingActionButton addBut;
    private FloatingActionButton remBut;
    private OnClickListener listener;
    private MeasuresAdapter measuresAdapter;
    private StringBuilder sb = new StringBuilder();
    private String chord;
    private MeasureListViewModel viewModel;
    private List<Beat> beatsForInsersion;
    private EditFragment editFragment;
    StringQueueArray usedQueue;
    SharedPreferences preferences;
    Editor editor;
    private RadioButton[] usedStackBtns;
    private Measure currentMeasure;
    int currentBeatPosition;
    private View currentBeatView;
    private FrameLayout editFragmentContainer;
    private AppBarLayout appBarLayout;

    private AtomicBoolean isLoadingNewMeasure;

    private boolean isAccSelected = false;

    public final static String IS_C_ROOT_PRESSED = "IS_C_ROOT_PRESSED";
    public final static String IS_D_ROOT_PRESSED = "IS_D_ROOT_PRESSED";
    public final static String IS_E_ROOT_PRESSED = "IS_E_ROOT_PRESSED";
    public final static String IS_F_ROOT_PRESSED = "IS_F_ROOT_PRESSED";
    public final static String IS_G_ROOT_PRESSED = "IS_G_ROOT_PRESSED";
    public final static String IS_A_ROOT_PRESSED = "IS_A_ROOT_PRESSED";
    public final static String IS_B_ROOT_PRESSED = "IS_B_ROOT_PRESSED";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSharedPrefs();

        isLoadingNewMeasure = new AtomicBoolean(false);
        usedQueue = new StringQueueArray(6);


        final MeasureListViewModel viewModel = ViewModelProviders.of(this).get(MeasureListViewModel.class);
        this.viewModel = viewModel;

        initializeViews(viewModel);

        observeViewModel(viewModel);

        setupActionbar();

    }

    private void setupActionbar() {
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setIcon(R.drawable.ic_chevron_left_black_24dp);
        actionBar.setIcon(R.drawable.ic_chevron_right_black_24dp);
        actionBar.setIcon(R.drawable.ic_eraser);
        TextView titleTV = findViewById(R.id.sheet_title_tv);
        TextView authorTV = findViewById(R.id.sheet_author_tv);
        appBarLayout = findViewById(R.id.main_appbar);
    }

    private void initUsedStackUi() {
        usedStackBtns = new RadioButton[6];
        usedStackBtns[0] = editFragment.getView().findViewById(R.id.last_used_1);
        usedStackBtns[0].setOnClickListener(this::onFragmentInteraction);
        usedStackBtns[1] = editFragment.getView().findViewById(R.id.last_used_2);
        usedStackBtns[2] = editFragment.getView().findViewById(R.id.last_used_3);
        usedStackBtns[3] = editFragment.getView().findViewById(R.id.last_used_4);
        usedStackBtns[4] = editFragment.getView().findViewById(R.id.last_used_5);
        usedStackBtns[5] = editFragment.getView().findViewById(R.id.last_used_6);
    }

    private void observeViewModel(MeasureListViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getMeasures().observe(this, new Observer<List<Measure>>() {
            @Override
            public void onChanged(@Nullable List<Measure> measures) {
                if (measures != null && measures.size() != 0) {

                    if (measuresAdapter == null) {

                        measuresAdapter = new MeasuresAdapter(getApplicationContext(), beatClickCallback);
                        recyclerView.setAdapter(measuresAdapter);

                    }
                    measuresAdapter.setMeasuresList(measures, getApplicationContext());

//                    recyclerView.smoothScrollToPosition(measures.size()-1);
                    Log.d("ADD_MEASURE", "updated view");

                }
            }
        });
    }

    public void showEditFragment(Measure measure, int currentBeatPosition) {

        editFragment = EditFragment.newInstance(measure, currentBeatPosition);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        if (!(getSupportFragmentManager().getBackStackEntryCount() == 1)) {

        transaction.addToBackStack(editFragment.getClass().getSimpleName());
        editFragmentContainer.setVisibility(View.VISIBLE);
        transaction.replace(R.id.edit_fragment_container, editFragment, null).commit();
        recyclerView.smoothScrollToPosition(measure.getNumber());
    }
//    }

    public void initializeViews(MeasureListViewModel viewModel) {

        listener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.add_fab:
                        addMeasure();
                        break;
                    case R.id.remove_fab:
                        deleteMeasure(viewModel);
                        break;
                }
            }
        };


        addBut = findViewById(R.id.add_fab);
        addBut.setOnClickListener(listener);

        editFragmentContainer = findViewById(R.id.edit_fragment_container);

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

    }

    private void addMeasure() {
        if (!isLoadingNewMeasure.get()) {
            Log.d("ADD_MEASURE", "called addEmptyMeasure(viewModel) from activity");
            addEmptyMeasure(viewModel);
        }
    }

    //interaction with adapter
    private final BeatClickCallback beatClickCallback = new BeatClickCallback() {

        @Override
        public void onBeatClicked(Measure measure, View beatView, int beatPosition) {
            appBarLayout.setExpanded(false);
            currentMeasure = measure;
            currentBeatPosition = beatPosition;
            currentBeatView = beatView;

            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                showEditFragment(measure, currentBeatPosition);

            }
        }

    };

    //DAO methods
    public void addEmptyMeasure(MeasureListViewModel viewModel) {
        viewModel.addEmptyMeasure(getApplication(), isLoadingNewMeasure);

    }

    public void updateMeasure(MeasureListViewModel viewModel, Measure measure) {

        viewModel.updateMeasure(getApplication(), measure);
    }

    public void deleteMeasure(MeasureListViewModel viewModel) {
        viewModel.deleteMeasure(getApplication());

    }

    //when button pressed in editFragment
    @Override
    public void onFragmentInteraction(View view) {
        initUsedStackUi();

        switch (view.getId()) {

            case R.id.c:
            case R.id.d:
            case R.id.e:
            case R.id.f:
            case R.id.g:
            case R.id.a:
            case R.id.b:

            case R.id._2:
            case R.id._3:
            case R.id._4:
            case R.id._5:
            case R.id._6:

            case R.id._9:
            case R.id._11:
            case R.id._13:
                onRootSelected(view);
                addChord();
                break;
            //accidentals adding
            case R.id.root_flat:
            case R.id.root_sharp:
            case R.id.root_natural:

            case R.id.ten1_flat:
            case R.id.ten1_sharp:
            case R.id.ten1_natural:

            case R.id.ten2_flat:
            case R.id.ten2_sharp:
            case R.id.ten2_natural:

            case R.id._7:
            case R.id.diminished_7:
            case R.id.maj_7:
            case R.id.half_diminished:

            case R.id.minor:
            case R.id.aug:
            case R.id.dim:
            case R.id.sus:

                onAccSelected(view);
                addChord();
                break;

            case R.id.next_beat_butt:
                editNext();
                break;
            case R.id.prev_beat_butt:
                editPrev();
                break;
            case R.id.eraser_butt:
                eraseBeat();
                break;

        }
    }

    private void addChord() {

        if (chord == null) {
            return;
        }

        beatsForInsersion = new ArrayList<>(currentMeasure.getBeats());


            beatsForInsersion.get(currentBeatPosition).setChordName(chord);

        currentMeasure.setBeats(beatsForInsersion);

        //update database
        updateMeasure(viewModel, currentMeasure);
        updateUsedStackButtons();
    }

    private void editNext() {

        View nextBeatView;
        int measurePosition = currentMeasure.getNumber()-1;
        //TODO handle when first measure
        if (measurePosition == -1) {
            measurePosition = 0;
        }

            nextBeatView = ((MeasureHolder) recyclerView.findViewHolderForAdapterPosition(measurePosition)).measure.getChildAt(currentBeatPosition + 2);
            //if beat is not last in measure
            if (nextBeatView != null) {
                nextBeatView.performClick();
            } else {
                //if last beat of measure then go to first beat of next measure
                nextBeatView = ((MeasureHolder) recyclerView.findViewHolderForAdapterPosition(measurePosition + 1)).measure.getChildAt(1);
                nextBeatView.performClick();
            }
        }

    private void editPrev() {

        View prevBeatView;
        int measurePosition = currentMeasure.getNumber()-1;
        //if in first measure
        if (measurePosition == -1) {
            measurePosition = 0;
        }

        prevBeatView = ((MeasureHolder) recyclerView.findViewHolderForAdapterPosition(measurePosition)).measure.getChildAt(currentBeatPosition);
        //if beat is not first in measure and not a hidden time sig view
        if (prevBeatView != null && !(prevBeatView instanceof LinearLayout)) {
            prevBeatView.performClick();
        } else {
            //if first beat of first measure
            if (measurePosition == 0) {
                return;
            }
            //if first beat of measure then go to last beat of previous measure
            prevBeatView = ((MeasureHolder) recyclerView.findViewHolderForAdapterPosition(measurePosition - 1)).measure.getChildAt(currentMeasure.getBeats().size());
            prevBeatView.performClick();
        }
    }

    private void eraseBeat() {
        chord = "";
        addChord();
    }



    private void onAccSelected(View view) {


        TextView preview = findViewById(R.id.preview_select_tv);
        if (chord != null && chord.length() != 0) {
            //string builder usage
            sb.append(chord);
            RadioButton rb = (RadioButton) view;
            if (rb.isChecked()) {
                sb.append((String) rb.getText());
                chord = sb.toString();
                sb.replace(0, chord.length(), "");
                preview.setText(chord);
            }
        }
    }

    private void updateUsedStackButtons() {

        if (!usedQueue.isEmpty(usedQueue)) {
            //if chord already exists in last used list, don't insert it and return.
            if (usedQueue.getIndexOf(chord) != -1) {
                return;
            }

            //if list is not yet full, fill in the buttons. (buttons and list array has same size).
            if (!usedQueue.isFull(usedQueue)) {
                usedQueue.enqueue(chord);
                usedStackBtns[usedQueue.getIndexOf(chord)].setText(chord);
                return;
            }
        }

        //else, if list is full
        usedQueue.dequeue();
        usedQueue.enqueue(chord);
        for (int i = 0; i < usedStackBtns.length; i++) {
            usedStackBtns[i].setText(usedQueue.getArray()[i]);
        }
    }


    //selecting chord root on edit fragment
    public void onRootSelected(View view) {
        TextView preview = findViewById(R.id.preview_select_tv);
        RadioButton rb = (RadioButton) view;

        if (rb.isChecked()) {
            String text = (String) (rb.getText());
            chord = text;
            preview.setText(chord);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (editFragmentContainer.getVisibility() == View.VISIBLE) {
            editFragmentContainer.setVisibility(View.GONE);
        }
    }

    public void initSharedPrefs() {
        final SharedPreferences preferences = getApplicationContext().getSharedPreferences("myApp", MODE_PRIVATE);
        this.preferences = preferences;
        Editor editor = preferences.edit();
        this.editor = editor;

        editor.putBoolean(IS_C_ROOT_PRESSED, false).commit();
        editor.putBoolean(IS_D_ROOT_PRESSED, false).commit();
        editor.putBoolean(IS_E_ROOT_PRESSED, false).commit();
        editor.putBoolean(IS_F_ROOT_PRESSED, false).commit();
        editor.putBoolean(IS_G_ROOT_PRESSED, false).commit();
        editor.putBoolean(IS_A_ROOT_PRESSED, false).commit();
        editor.putBoolean(IS_B_ROOT_PRESSED, false).commit();


    }


}
