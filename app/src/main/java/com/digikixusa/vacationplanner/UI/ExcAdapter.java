package com.example.d424_softwareengineeringcapstone.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d424_softwareengineeringcapstone.R;
import com.example.d424_softwareengineeringcapstone.entities.Exc;

import java.util.List;

public class ExcAdapter extends RecyclerView.Adapter<ExcAdapter.ExcViewHolder> {
    private List<Exc> mExcs;
    private final Context context;
    private final LayoutInflater mInflater;
    class ExcViewHolder extends RecyclerView.ViewHolder {


        private final TextView excItemName;
        private final TextView excItemDate;

        private ExcViewHolder(View itemView) {
            super(itemView);
            excItemName = itemView.findViewById(R.id.textExcName);
            excItemDate = itemView.findViewById(R.id.textExcDate);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final Exc current = mExcs.get(position);
                    Intent intent = new Intent(context, ExcDetails.class);
                    intent.putExtra("id", current.getExcID());
                    intent.putExtra("name", current.getExcName());
                    intent.putExtra("date", current.getDate());
                    intent.putExtra("vacaID", current.getVacationID());
                    context.startActivity(intent);
                }
            });
        }
    }
    public ExcAdapter(Context context){
        mInflater=LayoutInflater.from(context);
        this.context=context;
    }
    @Override
    public ExcViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView=mInflater.inflate(R.layout.item_excursion,parent,false);
        return new ExcViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull ExcViewHolder holder, int position){
        if(mExcs !=null){
            Exc current= mExcs.get(position);
            String name=current.getExcName();
            int vacaID=current.getVacationID();
            holder.excItemName.setText(name);
            holder.excItemDate.setText(current.getDate().toString());
        }
        else{
            holder.excItemName.setText("No excursion name");
            holder.excItemDate.setText("No date");
        }
    }
    public void setExcs(List<Exc> excs){
        mExcs = excs;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount(){
        if(mExcs !=null) {
            return mExcs.size();
        }
        else return 0;
    }

}
