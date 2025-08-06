package com.digikixusa.vacationplanner.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.digikixusa.vacationplanner.R;
import com.digikixusa.vacationplanner.database.Repository;
import com.digikixusa.vacationplanner.entities.Exc;
import com.digikixusa.vacationplanner.entities.Vacation;

import java.util.ArrayList;
import java.util.List;


public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.ProductViewHolder> {
    private List<Vacation> mVacations;
    private final Context context;
    private final LayoutInflater mInflater;
    private boolean fromSearchFunction;


    public VacationAdapter(Context context){
        mInflater= LayoutInflater.from(context);
        this.context=context;
        fromSearchFunction = false;
    }
    public VacationAdapter(Context context, List<Vacation> vacList){
        this.mVacations = vacList;
        mInflater= LayoutInflater.from(context);
        this.context = context;
        if(vacList.isEmpty()){
            Toast.makeText(context, "No vacations found.", Toast.LENGTH_LONG).show();
        }
        fromSearchFunction = true;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        private final TextView vacationNameView;
        private final TextView vacationDateView;
        private final TextView vacationNumExcs;
        private final TextView vacationNumExcsCount;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            vacationNameView = itemView.findViewById(R.id.vacationTitle);
            vacationDateView = itemView.findViewById(R.id.vacationDates);
            if (fromSearchFunction) {
                vacationNumExcs = itemView.findViewById(R.id.vacationNumExc);
                vacationNumExcsCount = itemView.findViewById(R.id.vacationNumExcCount);
            }else{
                vacationNumExcs=null;
                vacationNumExcsCount=null;
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    final Vacation current= mVacations.get(position);
                    Intent intent=new Intent(context, VacationDetails.class);
                    intent.putExtra("id",current.getVacationID());
                    intent.putExtra("name",current.getVacationName());
                    intent.putExtra("hotel", current.getHotel());
                    intent.putExtra("startdate", current.getStartDate());
                    intent.putExtra("enddate",current.getEndDate());
                    // no need for the below since on VacationDetails we can see the excs and don't need a count
                    //intent.putExtra("numexcs", current.getNumExcursions());
                    context.startActivity(intent);
                }
            });
        }
    }
    @NonNull
    @Override
    public VacationAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (fromSearchFunction){
            View itemView = mInflater.inflate(R.layout.item_vacation_search, parent, false);
            return new ProductViewHolder(itemView);
        }else {
            View itemView = mInflater.inflate(R.layout.item_vacation, parent, false);
            return new ProductViewHolder(itemView);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull VacationAdapter.ProductViewHolder holder, int position) {
        if(mVacations !=null){

            if (mVacations.isEmpty()){
                holder.vacationNameView.setText("No vacations found.");
                holder.vacationDateView.setText("");
            }else {
                Vacation current = mVacations.get(position);
                String name = current.getVacationName();
                String dates = current.getStartDate() + " to " + current.getEndDate();
                String numExcs = "0";

                holder.vacationNameView.setText(name);
                holder.vacationDateView.setText(dates);
                if (fromSearchFunction){
                    holder.vacationNumExcs.setText("Number of Excursions: ");
                    holder.vacationNumExcsCount.setText(numExcs);
                }
            }
        }
        else{

            holder.vacationNameView.setText("No vacation name");
            holder.vacationDateView.setText(".");
        }
    }

    @Override
    public int getItemCount() {
        if(mVacations !=null) {
            return mVacations.size();
        }
        else return 0;
    }

    public void setProducts(List<Vacation> vacations){
        mVacations = vacations;
        notifyDataSetChanged();
    }




}
