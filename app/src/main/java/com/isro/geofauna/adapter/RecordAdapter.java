package com.isro.geofauna.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.isro.geofauna.R;
import com.isro.geofauna.data.Geofauna;

import java.util.List;


public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder>{

    private final LayoutInflater mInflater;
    private List<Geofauna> mGeofauna; // Cached copy of words
    private Context context;

    public RecordAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.card_record_info, parent, false);
        return new RecordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        if (mGeofauna != null) {
            Geofauna current = mGeofauna.get(position);

            holder.uniqueID.setText(current.getUniqueSurveyId());
            holder.serialNo.setText(current.getSerialNo());
            holder.locality.setText(current.getLocality());
            holder.date.setText(current.getDate());

        } else {
            // Covers the case of data not being ready yet.
            Log.i("RecyclerView", "Data Missing");
        }
    }

    @Override
    public int getItemCount() {
        if (mGeofauna != null)

            return mGeofauna.size();
        else return 0;
    }

    public void setmGeofauna(List<Geofauna> geofaunas){
        mGeofauna = geofaunas;
        notifyDataSetChanged();
    }

    public class RecordViewHolder extends RecyclerView.ViewHolder {
        private final TextView uniqueID;
        private final TextView serialNo;
        private final TextView locality;
        private final TextView date;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);

            uniqueID = (TextView) itemView.findViewById(R.id.unique_id_holder);
            serialNo = (TextView) itemView.findViewById(R.id.serial_no_holder_tv);
            locality = (TextView) itemView.findViewById(R.id.locality_holder_tv);
            date = (TextView) itemView.findViewById(R.id.date_holder_tv);

        }
    }
}
