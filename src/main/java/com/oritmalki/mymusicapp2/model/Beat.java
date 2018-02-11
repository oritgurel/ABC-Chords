package com.oritmalki.mymusicapp2.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by Orit on 19.11.2017.
 */
@Entity(tableName = "beat", foreignKeys = @ForeignKey(onDelete = CASCADE, entity = Measure.class, parentColumns = "measure_number", childColumns = "measureNum"))
public class Beat {

//    private int duration;
//    boolean isRest;

    @PrimaryKey
    @ColumnInfo(name = "chord_name")
    @NonNull
    String chordName;


    public Integer measureNum;




    public Beat() {
        this.getChordName();
    }
    @Ignore
    public Beat(String chordName) {
        this.chordName = chordName;


    }

    public String getChordName() {
        return chordName;
    }

    public void setChordName(String chordName) {
        this.chordName = chordName;
    }
}
