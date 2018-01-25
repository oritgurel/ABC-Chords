package com.oritmalki.mymusicapp2;

import java.util.List;

/**
 * Created by Orit on 24.12.2017.
 */

public class Measures {
    private List<Measure> measures;

    public Measures(List<Measure> measures) {
        this.measures = measures;
    }


    public List<Measure> getMeasures() {
        return measures;
    }

    public void setMeasures(List<Measure> measures) {
        this.measures = measures;
    }
}
