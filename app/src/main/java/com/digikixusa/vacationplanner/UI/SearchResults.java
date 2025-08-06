package com.digikixusa.vacationplanner.UI;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.digikixusa.vacationplanner.R;
import com.digikixusa.vacationplanner.entities.Vacation;

import java.util.ArrayList;

public class SearchResults extends AppCompatActivity {

    private RecyclerView recyclerView;
    private VacationAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_results);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerViewSearchResults);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<Vacation> results = getIntent().getParcelableArrayListExtra("search_results");
        if (results == null) results = new ArrayList<>();

        adapter = new VacationAdapter(this,results);
        recyclerView.setAdapter(adapter);

    }
}