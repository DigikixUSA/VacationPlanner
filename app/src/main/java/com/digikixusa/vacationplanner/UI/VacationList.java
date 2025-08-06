package com.digikixusa.vacationplanner.UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.digikixusa.vacationplanner.R;
import com.digikixusa.vacationplanner.database.Repository;
import com.digikixusa.vacationplanner.entities.Exc;
import com.digikixusa.vacationplanner.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class VacationList extends AppCompatActivity {

    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FloatingActionButton fabutton = findViewById(R.id.floatingActionButton);
        fabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VacationList.this, VacationDetails.class);
                startActivity(intent);
            }
        });
        FloatingActionButton fabuttonSearch = findViewById(R.id.floatingActionButtonSearch);
        fabuttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText input = new EditText(VacationList.this);
                input.setHint("Search vacations...");
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                new AlertDialog.Builder(VacationList.this)
                        .setTitle("Search Vacations")
                        .setView(input)
                        .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String query = input.getText().toString().trim();
                                if (!query.isEmpty()) {
                                    performSearch(query);
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

        RecyclerView recyclerView=findViewById(R.id.recyclerView);
        repository = new Repository(getApplication());
        List<Vacation> allVacations =repository.getAllVacations();
        final VacationAdapter vacationAdapter =new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdapter.setProducts(allVacations);
        vacationAdapter.notifyDataSetChanged();
    }

    private void performSearch(final String query) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                repository = new Repository(getApplication());
                List<Vacation> results = repository.searchVacations(query);

                // Pass results to new activity on the main thread
                runOnUiThread(() -> {
                    Intent intent = new Intent(VacationList.this, SearchResults.class);
                    intent.putParcelableArrayListExtra("search_results", (ArrayList<? extends Parcelable>) new ArrayList<Vacation>(results));
                    startActivity(intent);
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_list, menu);
        return true;
    }

    @Override
    protected void onResume(){
        super.onResume();
        List<Vacation> allVacations =repository.getAllVacations();
        RecyclerView recyclerView=findViewById(R.id.recyclerView);
        final VacationAdapter vacationAdapter =new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdapter.setProducts(allVacations);
        vacationAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mitem) {
        if (mitem.getItemId() == R.id.sample) {
            repository = new Repository(getApplication());
            //Toast.makeText(VacationList.this,"put in sample data",Toast.LENGTH_LONG).show();
            Vacation vacation = new Vacation(1, "Bermuda Trip", "Hilton Bermuda","02/22/2026", "02/30/2026", 2);
            repository.insert(vacation);
            Exc exc = new Exc(1, "Snorkeling", "02/25/2026", 1);
            repository.insert(exc);
            exc = new Exc(2, "Bus Tour", "02/28/2026", 1);
            repository.insert(exc);

            vacation = new Vacation(2, "Florida Spring Break", "Wyndham Daytona Beach","03/12/2026","03/26/2026", 0);
            repository.insert(vacation);

            List<Vacation> allVacations =repository.getAllVacations();
            RecyclerView recyclerView=findViewById(R.id.recyclerView);
            final VacationAdapter vacationAdapter =new VacationAdapter(this);
            recyclerView.setAdapter(vacationAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            vacationAdapter.setProducts(allVacations);
            vacationAdapter.notifyDataSetChanged();
            return true;
        }
        if (mitem.getItemId() == android.R.id.home) {
            this.finish();
            //use intent to save data here if needed
            return true;
        }
        if (mitem.getItemId() == R.id.clearall){
            repository = new Repository(getApplication());
            repository.deleteAllExcursions();
            repository.deleteAllVacations();
            //System.out.println("***** repository size after="+Integer.toString(repository.getAllVacations().size()));
            this.finish();
            return true;
        }
        return true;
    }


}