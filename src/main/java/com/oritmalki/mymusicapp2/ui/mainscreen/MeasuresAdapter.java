package com.oritmalki.mymusicapp2.ui.mainscreen;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.DiffUtil.Callback;
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
import java.util.Objects;

/**
 * Created by Orit on 16.12.2017.
 */

public class MeasuresAdapter extends RecyclerView.Adapter<MeasuresAdapter.MeasureHolder> {

   List<? extends Measure> measures = new ArrayList<>();
   List<? extends Beat> beats;
   TimeSignature timeSignature;
   Context context;

    @Nullable
    private final MeasureClickCallback measureClickCallback;


   //New Constructor
   public MeasuresAdapter(@Nullable MeasureClickCallback measureClickCallback) {
       this.measureClickCallback = measureClickCallback;
   }


//new Architecture components accomodation
    public void setMeasuresList(final List<? extends Measure> measuresList) {
       if (this.measures == null) {
           this.measures = measuresList;
           notifyItemRangeInserted(0, measuresList.size());
           notifyItemRangeRemoved(0, measuresList.size());
       }
       else {
           DiffUtil.DiffResult result = DiffUtil.calculateDiff(new Callback() {
               @Override
               public int getOldListSize() {
                   return MeasuresAdapter.this.measures.size();
               }

               @Override
               public int getNewListSize() {
                   return measuresList.size();
               }

               @Override
               public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                   return MeasuresAdapter.this.measures.get(oldItemPosition).getNumber() == measuresList.get(newItemPosition).getNumber();
               }

               @Override
               public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                   Measure measure = measuresList.get(newItemPosition);
                   Measure old = measuresList.get(oldItemPosition);
                   return measure.getNumber() == old.getNumber() && Objects.equals(measure.getBeats(), old.getBeats());
               }
           });
           this.measures = measuresList;
           result.dispatchUpdatesTo(this);
       }
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


    public void addAndBindBeatsAndTimeSig(List<? extends Measure> measures, MeasureHolder measureHolder, int position) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup timeSigView = (ViewGroup)layoutInflater.inflate(R.layout.time_signature_layout, measureHolder.measure, false);


            beats = new ArrayList<>(measures.get(position).getBeats());
            timeSignature = new TimeSignature(measures.get(position).getTimeSignature().getNumerator(), measures.get(position).getTimeSignature().getDenominator());

            TextView numerator = (TextView) timeSigView.findViewById(R.id.numerator);
            numerator.setText(String.valueOf(measures.get(position).getTimeSignature().getNumerator()));
            TextView denomerator = (TextView) timeSigView.findViewById(R.id.denominator);
            denomerator.setText(String.valueOf(measures.get(position).getTimeSignature().getDenominator()));


            for (int i = 1; i < measures.size(); i++) {


                measureHolder.measure.removeAllViews();
                measureHolder.measure.addView(timeSigView, 0);

                if (measures.get(i).getTimeSignature().compare(measures.get(i - 1).getTimeSignature())) {
                    measures.get(i).setShowTimeSig(false);
                }

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
