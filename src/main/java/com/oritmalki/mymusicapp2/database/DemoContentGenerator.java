package com.oritmalki.mymusicapp2.database;

import android.content.Context;

import com.oritmalki.mymusicapp2.model.Beat;
import com.oritmalki.mymusicapp2.model.Measure;
import com.oritmalki.mymusicapp2.model.TimeSignature;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Orit on 28.1.2018.
 */

public class DemoContentGenerator {
    Context context;


    public static List<Measure> generateDemoContent() {

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
//            Toast.makeText(context, "Too many beats for time signature", Toast.LENGTH_LONG).show();
        }

        return demoMeasureList;
    }
}
