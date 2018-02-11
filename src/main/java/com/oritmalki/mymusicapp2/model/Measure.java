package com.oritmalki.mymusicapp2.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by Orit on 28.11.2017.
 */
@Entity(tableName = "measure",
        foreignKeys = @ForeignKey(onDelete = CASCADE, entity = Sheet.class, parentColumns = "id", childColumns = "sheetId"),
        indices = {@Index("measure_number"), @Index(value = {"measure_number", "beats"})})
public class Measure {

    @Embedded
    TimeSignature timeSignature;

//    @Relation(parentColumn = "measureNumber", entityColumn = "chordName", entity = Beat.class)
    public Integer sheetId;

    @PrimaryKey
    @ColumnInfo(name = "measure_number")
    public Integer measureNumber;


    @ColumnInfo(name = "time_signature")

    String timeSig;

    boolean showTimeSig = true;

    List<Beat> beats = new ArrayList<>();

    public Measure() {
        this.getBeats();
    }
    @Ignore
    public Measure(int number, List<Beat> beats, TimeSignature timeSignature, boolean showTimeSig) {
        this.beats = beats;
        this.measureNumber = number;
        this.timeSignature = timeSignature;
        this.showTimeSig = showTimeSig;

        int s = beats.size();
        int x = timeSignature.getNumerator();
        if (timeSignature.numerator == x) {
            s = x;
        }
    }


    public List<Beat> getBeats() {
        return beats;
    }

    public int getNumber() {
        return measureNumber;
    }

    public void setNumber(int number) {
        this.measureNumber = number;
    }

    public void setBeats(List<Beat> beats) {
        this.beats = beats;
    }

    public void addBeat(Beat beat) {

        try {

            if (beats.size() <= timeSignature.getNumerator())
                beats.add(new Beat(" "));
        }
        catch (Exception e) {
            Log.v("BeatsException", "Too many beats.");

        }
    }

    public TimeSignature getTimeSignature() {
        return timeSignature;
    }

    public void setTimeSignature(int numerator, int denominator) {
        this.timeSignature = timeSignature;
    }

    public void removeBeat(int pos) {
        beats.remove(pos);
    }

    public boolean isShowTimeSig() {
        return showTimeSig;
    }

    public void setShowTimeSig(boolean showTimeSig) {
        this.showTimeSig = showTimeSig;
    }

    public void setTimeSignature(TimeSignature timeSignature) {
        this.timeSignature = timeSignature;
    }

    public String getTimeSig() {
        return timeSig;
    }

    public void setTimeSig(String timeSig) {
        this.timeSig = timeSig;
    }
}


