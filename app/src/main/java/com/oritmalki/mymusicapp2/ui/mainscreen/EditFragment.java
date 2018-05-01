package com.oritmalki.mymusicapp2.ui.mainscreen;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.oritmalki.mymusicapp2.R;
import com.oritmalki.mymusicapp2.model.Measure;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditFragment extends Fragment {


    @BindView(R.id.add_chord) Button addChordButt;

    @BindView(R.id.c) RadioButton c_root;
    @BindView(R.id.d) RadioButton d_root;
    @BindView(R.id.e) RadioButton e_root;
    @BindView(R.id.f) RadioButton f_root;
    @BindView(R.id.g) RadioButton g_root;
    @BindView(R.id.a) RadioButton a_root;
    @BindView(R.id.b) RadioButton b_root;
    @BindView(R.id.root_flat) RadioButton root_flat;
    @BindView(R.id.root_sharp) RadioButton root_sharp;
    @BindView(R.id.root_natural) RadioButton root_natural;

    @BindView(R.id.major) Button major;
    @BindView(R.id.minor) Button minor;
    @BindView(R.id.aug) Button aug;
    @BindView(R.id.dim) Button dim;
    @BindView(R.id.sus) Button sus;

    @BindView(R.id.ten1_flat) Button ten1_flat;
    @BindView(R.id.ten1_sharp) Button ten1_sharp;
    @BindView(R.id.ten1_natural) Button ten1_natural;

    @BindView(R.id._2) Button _2;
    @BindView(R.id._3) Button _3;
    @BindView(R.id._4) Button _4;
    @BindView(R.id._5) Button _5;
    @BindView(R.id._6) Button _6;
    @BindView(R.id._7) Button _7;
    @BindView(R.id.maj_7) Button maj_7;
    @BindView(R.id.diminished_7) Button diminished_7;
    @BindView(R.id.half_diminished) Button half_diminished;

    @BindView(R.id.ten2_flat) Button ten2_flat;
    @BindView(R.id.ten2_sharp) Button ten2_sharp;
    @BindView(R.id.ten2_natural) Button ten2_natural;

    @BindView(R.id._9) Button _9;
    @BindView(R.id._11) Button _11;
    @BindView(R.id._13) Button _13;


    @BindViews({R.id.c, R.id.d, R.id.e, R.id.f, R.id.g, R.id.a, R.id.b}) RadioButton[] rootButtonsGroup;
    @BindViews({R.id.root_flat, R.id.root_sharp, R.id.root_natural}) RadioButton[] rootAccButtonsGroup;



    private static final String ARG_MEASURE = "args_measure";
    public static String ARG_BEAT_POSITION = "args_beat_position";

    Measure measure;
    int beatPosition;


    private OnFragmentInteractionListener mListener;

    public EditFragment() {
        // Required empty public constructor
    }


    public static EditFragment newInstance(Measure measure, int beatPosition) {
        EditFragment fragment = new EditFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MEASURE, measure);
        args.putInt(ARG_BEAT_POSITION, beatPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.measure = (Measure) getArguments().getSerializable(ARG_MEASURE);
            this.beatPosition = getArguments().getInt(ARG_BEAT_POSITION);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit, container, false);
        TextView measureNumEditorView = view.findViewById(R.id.measure_num_tv);
        TextView beatNumEditorView = view.findViewById(R.id.beat_num);

        ButterKnife.bind(this, view);

        addChordButt.setOnClickListener(EditFragment.this::onButtonPressed);

        for (RadioButton rootButton : rootButtonsGroup) {
            //null??
            rootButton.setOnClickListener(EditFragment.this::onButtonPressed);
        }

        measureNumEditorView.setText("M: " + String.valueOf(measure.getNumber()));
        beatNumEditorView.setText("B: " + String.valueOf(beatPosition + 1));






        // Inflate the layout for this fragment
        return view;
    }

    // hook method into UI event
    public void onButtonPressed(View view) {
        if (mListener != null) {
            mListener.onFragmentInteraction(view);

            }
        }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(View view);

    }

}
