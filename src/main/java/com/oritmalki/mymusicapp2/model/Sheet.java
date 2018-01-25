package com.oritmalki.mymusicapp2.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

/**
 * Created by Orit on 21.12.2017.
 */
@Entity
public class Sheet {

    @PrimaryKey
    public Integer id;

    public String name = "";
    public String title = "";
    public String author = "";


//    @Relation(parentColumn = "id", entityColumn = "measureNumber", entity = Measure.class)

    @Ignore
    public List<Measure> measures;

    @Ignore
    public Sheet(List<Measure> measures) {
    }

    public Sheet() {
        this.getMeasures();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<Measure> getMeasures() {
        return measures;
    }

    public void setMeasures(List<Measure> measures) {
        this.measures = measures;
    }

    @Ignore
    public Sheet(int id, String name, String title, String author, List<Measure> measures) {
        this.id = id;
        this.name = name;
        this.title = title;

        this.author = author;
        this.measures = measures;
    }
}
