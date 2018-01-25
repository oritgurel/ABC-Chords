package com.oritmalki.mymusicapp2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxItemDecoration;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FlexboxLayoutManager layoutManager;
    private DataRepository dataRepository;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();

        

    }

    public void initializeViews() {

        dataRepository = new DataRepository(this);
        dataRepository.addMeasures(demoContent());

        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new FlexboxLayoutManager(MainActivity.this);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setAlignItems(AlignItems.STRETCH);

        FlexboxItemDecoration itemDecoration = new FlexboxItemDecoration(MainActivity.this);
        itemDecoration.setDrawable(getApplicationContext().getDrawable(R.drawable.divider));
        itemDecoration.setOrientation(FlexboxItemDecoration.BOTH);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setLayoutManager(layoutManager);
        MeasuresAdapter measuresAdapter = new MeasuresAdapter(dataRepository.getAllMeasures(), MainActivity.this);
        recyclerView.setAdapter(measuresAdapter);

    }

    public List<Measure> demoContent() {

        List<Beat> beats = new ArrayList<>();
        beats.add(new Beat("Cm"));
        beats.add(new Beat("Gm"));
        beats.add(new Beat("Abmaj7"));



        List<Beat> beats1 = new ArrayList<>();
        beats1.add(new Beat("Bm"));
        beats1.add(new Beat("F#m"));
        beats1.add(new Beat("Gmaj7"));
        beats1.add(new Beat("A6"));

        List<Measure> demoMeasureList = new ArrayList<>();

        try {

            for (int i = 1; i < 10; i++) {
                demoMeasureList.add(new Measure(i, new ArrayList<Beat>(beats), new TimeSignature(3, 4), true));
            }
            for (int i = 10; i < 15; i++) {
                demoMeasureList.add(new Measure(i, new ArrayList<Beat>(beats1), new TimeSignature(4, 4), true));
            }

        }

        catch (Exception e) {
            Toast.makeText(this, "Too many beats for time signature", Toast.LENGTH_LONG).show();
        }

        return demoMeasureList;
    }


}
