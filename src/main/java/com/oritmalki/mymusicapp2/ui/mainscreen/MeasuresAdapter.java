package com.oritmalki.mymusicapp2.ui.mainscreen;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.LayoutDirection;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.oritmalki.mymusicapp2.R;
import com.oritmalki.mymusicapp2.model.Beat;
import com.oritmalki.mymusicapp2.model.Measure;
import com.oritmalki.mymusicapp2.model.TimeSignature;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Orit on 16.12.2017.
 */

public class MeasuresAdapter extends RecyclerView.Adapter<MeasuresAdapter.MeasureHolder> {

   List<Measure> measures = new ArrayList<>();
   List<Beat> beats;
   TimeSignature timeSignature;
   Context context;

    public MeasuresAdapter(List<Measure> measures, Context context) {
        this.measures = measures;
        this.context = context;

    }

    @Override
    public MeasureHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.measure_view, parent, false);
        return new MeasureHolder(viewGroup);

    }

    @Override
    public void onBindViewHolder(MeasureHolder holder, int position) {

        MeasureHolder measureHolder = (MeasureHolder) holder;
        measures.get(position);
        holder.measure.removeAllViews();
        addAndBindBeatsAndTimeSig(measures, measureHolder, position);
        if (measures.get(position).isShowTimeSig() == false) {
            holder.measure.getChildAt(0).setVisibility(View.GONE);

        }
    }


    @Override
    public int getItemCount() {
        return measures.size();
    }


    public void addAndBindBeatsAndTimeSig(List<Measure> measures, MeasureHolder measureHolder, int position) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup timeSigView = (ViewGroup)layoutInflater.inflate(R.layout.time_signature_layout, measureHolder.measure, false);

// to function add timeSignature

//        for (Measure measure : measures) {
            //Problem: remembers only last beat list and passes it to "beats"
            beats = new ArrayList<>(measures.get(position).getBeats());
            timeSignature = new TimeSignature(measures.get(position).getTimeSignature().getNumerator(), measures.get(position).getTimeSignature().getDenominator());

            //also here at the timeSig
            TextView numerator = (TextView) timeSigView.findViewById(R.id.numerator);
            numerator.setText(String.valueOf(measures.get(position).getTimeSignature().getNumerator()));
            TextView denomerator = (TextView) timeSigView.findViewById(R.id.denominator);
            denomerator.setText(String.valueOf(measures.get(position).getTimeSignature().getDenominator()));


            for (int i = 1; i < measures.size(); i++) {

                if (measures.get(i).getTimeSignature().compare(measures.get(i - 1).getTimeSignature())) {
                    measures.get(i).setShowTimeSig(false);
                }
                measureHolder.measure.removeAllViews();
                measureHolder.measure.addView(timeSigView, 0);

                for (Beat beat : beats) {

                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(10, 20, 10, 20);
                    lp.setLayoutDirection(LayoutDirection.LTR);
                    lp.weight = 1.0f;
                    TextView newBeat = new TextView(context);
                    newBeat.setLayoutParams(lp);
                    newBeat.setSingleLine(true);


                    newBeat.setText(beat.getChordName());
                    measureHolder.measure.addView(newBeat);

                }


            }



//to function add beats


    }

    static class MeasureHolder extends RecyclerView.ViewHolder {
        LinearLayout measure;


        public MeasureHolder(View itemView) {
            super(itemView);
            measure = itemView.findViewById(R.id.measure);



            ViewGroup.LayoutParams lp =
                    (ViewGroup.LayoutParams) measure.getLayoutParams();
            if (lp instanceof FlexboxLayoutManager.LayoutParams) {
                FlexboxLayoutManager.LayoutParams flmp = (FlexboxLayoutManager.LayoutParams) lp;
                flmp.setFlexGrow(1.0f);
            }

        }
    }


}
