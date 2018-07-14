package com.oritmalki.mymusicapp2.ui.mainscreen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.oritmalki.mymusicapp2.R;
import com.oritmalki.mymusicapp2.model.Sheet;

import java.util.ArrayList;
import java.util.List;

public class MySheetsFragment extends ListFragment {

    private static List<Sheet> mySheets;
    private Button createNewBtn;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listfragment_mysheets_layout, container, false);
        createNewBtn = view.findViewById(R.id.listfragment_mysheets_create_new_btn);
        createNewBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(CreateNewSheetActivity.getIntent(getContext()));
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        setListShown(true);
//        setEmptyText(getString(R.string.sharedsheets_fragment_empty_message));
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
