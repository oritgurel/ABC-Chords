package com.oritmalki.mymusicapp2.ui.mainscreen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;

import com.oritmalki.mymusicapp2.model.Sheet;

import java.util.ArrayList;
import java.util.List;

public class MySheetsFragment extends ListFragment {

    private static List<Sheet> mySheets;

    public MySheetsFragment() {

    }

    public static MySheetsFragment getInstance() {
        return new MySheetsFragment();
    }

    public static void setData(List<Sheet> sheets) {
        if (mySheets == null) {
            mySheets = new ArrayList<>(sheets);
        }
        if (!mySheets.isEmpty()) {
            mySheets.clear();
        }
        mySheets.addAll(sheets);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayAdapter<Sheet> adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, mySheets);
        if (mySheets != null) {
            setListAdapter(adapter);
        }
    }
}
