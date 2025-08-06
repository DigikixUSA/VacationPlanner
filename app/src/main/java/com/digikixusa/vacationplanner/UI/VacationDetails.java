package com.example.d424_softwareengineeringcapstone.UI;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.d424_softwareengineeringcapstone.R;
import com.example.d424_softwareengineeringcapstone.database.Repository;
import com.example.d424_softwareengineeringcapstone.entities.Exc;
import com.example.d424_softwareengineeringcapstone.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class VacationDetails extends AppCompatActivity {
    String name;
    String hotel;
    String startDate;
    String endDate;

    int vacationID;
    EditText editName;
    EditText editHotel;
    TextView editStartDate;
    TextView editEndDate;
    Repository repository;
    Vacation currentVacation;
    int numExcs;
    String myDateFormat = "MM/dd/yyyy";
    DatePickerDialog.OnDateSetListener startDateListener;
    DatePickerDialog.OnDateSetListener endDateListener;
    final Calendar myCalendar = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat(myDateFormat, Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editName=findViewById(R.id.titletext);
        editHotel=findViewById(R.id.hoteltext);
        editStartDate=findViewById(R.id.startdate);
        editEndDate=findViewById(R.id.enddate);
        vacationID = getIntent().getIntExtra("id", -1);
        name = getIntent().getStringExtra("name");
        hotel = getIntent().getStringExtra("hotel");
        startDate = getIntent().getStringExtra("startdate");
        endDate = getIntent().getStringExtra("enddate");
        editName.setText(name);
        editHotel.setText(hotel);
        editStartDate.setText(startDate);
        editEndDate.setText(endDate);
        FloatingActionButton fabutton = findViewById(R.id.floatingActionButton2);
        fabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(VacationDetails.this, ExcDetails.class);
                intent.putExtra("vacaID", vacationID);
                startActivity(intent);
            }
        });
        editStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date;
                String info = editStartDate.getText().toString();
                if (info.equals("")) info = "04/04/2024";//set default if nothing
                try {
                    myCalendar.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(VacationDetails.this, startDateListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        editEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date;
                String info = editEndDate.getText().toString();
                if (info.equals("")) info = "04/04/2024";//set default if nothing
                try {
                    myCalendar.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(VacationDetails.this, endDateListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        startDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelStart();

            }
        };
        endDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelEnd();
            }
        };
        RecyclerView recyclerView = findViewById(R.id.excRecyclerView);
        repository = new Repository(getApplication());
        final ExcAdapter excAdapter = new ExcAdapter(this);
        recyclerView.setAdapter(excAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Exc> filteredExcs = new ArrayList<>();
        for (Exc c : repository.getAllExcs()){
            if (c.getVacationID() == vacationID) filteredExcs.add(c);
        }
        excAdapter.setExcs(filteredExcs);
    }

    private void updateLabelStart() {
        //SimpleDateFormat sdf = new SimpleDateFormat(myDateFormat, Locale.US);
        editStartDate.setText(sdf.format(myCalendar.getTime()));
    }
    private void updateLabelEnd() {
        editEndDate.setText(sdf.format(myCalendar.getTime()));
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_vacation_details, menu);
        return true;
    }

    private int validateDatesAndDifference(String date1, String date2){
        int dateDiff;
        Date sdate=null;
        Date edate=null;
        SimpleDateFormat sdf = new SimpleDateFormat(myDateFormat);
        try {
            sdate = sdf.parse(date1);
        } catch (ParseException e) {
            System.out.println("Error parsing date:"+e.toString());
            Toast.makeText(VacationDetails.this, "Invalid date.", Toast.LENGTH_LONG).show();
        }
        try {
            edate = sdf.parse(date2);
        } catch (ParseException e) {
            System.out.println("Error parsing date:"+e.toString());
            Toast.makeText(VacationDetails.this, "Invalid date.", Toast.LENGTH_LONG).show();
        }
        dateDiff=-99;
        if ((sdate!=null) && (edate!=null)) dateDiff = sdate.compareTo(edate);
        return dateDiff;
    }

    public boolean onOptionsItemSelected(MenuItem item){

        if(item.getItemId()==android.R.id.home){
            this.finish();
            return true;
        }
        if(item.getItemId()==R.id.vacationsave){
            Vacation vacation;
            // if vacationID == -1, that means this is a new one
            if(vacationID ==-1){
                //if no vacations in repository, set first to id of 1
                if (repository.getAllVacations().size() == 0) {
                    vacationID = 1;
                }
                else {
                    //else if at least one exists, get the last one, and increment its id by one.
                    // e.g., .size() is 3, index is 2 for last, last id is 4, give this one 5
                    vacationID = repository.getAllVacations().get(repository.getAllVacations().size() - 1).getVacationID() +1;
                }


                if (validateDatesAndDifference(editStartDate.getText().toString(),editEndDate.getText().toString())<=0) {
                    //give exc count as 0 since this is new, with the vacationID determined above, and the details that have been entered and validated
                    vacation = new Vacation(vacationID, editName.getText().toString(), editHotel.getText().toString(), editStartDate.getText().toString(), editEndDate.getText().toString(),0);
                    repository.insert(vacation);
                    Toast.makeText(VacationDetails.this, "Vacation "+editName.getText().toString()+" saved!", Toast.LENGTH_LONG).show();
                    this.finish();
                }else{
                    Toast.makeText(VacationDetails.this, "Start date must be before end date.", Toast.LENGTH_LONG).show();
                }
            }
            else{
                if (validateDatesAndDifference(editStartDate.getText().toString(),editEndDate.getText().toString())<=0) {
                    // ok to set number of excs here to 0 since in repository.update() the count is automatically counted at that point
                    vacation = new Vacation(vacationID, editName.getText().toString(), editHotel.getText().toString(), editStartDate.getText().toString(), editEndDate.getText().toString(),0);
                    repository.update(vacation);
                    Toast.makeText(VacationDetails.this, "Vacation "+editName.getText().toString()+" updated!", Toast.LENGTH_LONG).show();
                    this.finish();
                }else{
                    Toast.makeText(VacationDetails.this, "Start date must be before end date.", Toast.LENGTH_LONG).show();
                }

            }
        }
        if (item.getItemId() == R.id.vacationdelete) {
            for (Vacation v : repository.getAllVacations()) {
                if (v.getVacationID() == vacationID) {
                    currentVacation = v;
                }
            }
            numExcs = 0;//numExcs = number of excursions *this* product has
            for (Exc c : repository.getAllExcs()) {
                if (c.getVacationID() == vacationID) {
                    numExcs++;
                }
            }
            if (numExcs == 0) {//This product has none, so OK to delete
                repository.delete(currentVacation);
                Toast.makeText(VacationDetails.this, currentVacation.getVacationName() + " was deleted.", Toast.LENGTH_SHORT).show();
                VacationDetails.this.finish();
            } else {//No, it has some so you cannot delete.
                Toast.makeText(VacationDetails.this, "Can't delete a vacation with excursions.", Toast.LENGTH_SHORT).show();
            }

        }

        if (item.getItemId() == R.id.share){
            Intent sentIntent = new Intent();
            sentIntent.setAction(Intent.ACTION_SEND);
            //all details
            sentIntent.putExtra(Intent.EXTRA_TEXT, editName.getText().toString());
            String vacDetailsString = " at hotel " + editHotel.getText().toString()+ ", beginning "+editStartDate.getText().toString()+
                    " through "+editEndDate.getText().toString()+".";
            System.out.println("***"+vacDetailsString);
            for (Exc c : repository.getAllExcs()) {
                System.out.println(c.getExcName()+" id="+c.getVacationID()+" vid="+vacationID);
                if (c.getVacationID() == vacationID) {
                    vacDetailsString = vacDetailsString + " Excursion: "+c.getExcName()+" on "+c.getDate();
                    System.out.println("vacDetailsString now="+vacDetailsString);
                }
            }
            System.out.println("intent to send text: "+editName.getText().toString()+vacDetailsString);
            sentIntent.putExtra(Intent.EXTRA_TITLE, vacDetailsString);
            sentIntent.setType("text/plain");
            Intent shareIntent=Intent.createChooser(sentIntent, null);
            startActivity(shareIntent);
            return true;
        }

        if (item.getItemId() == R.id.notify){

            String vacStartDate = editStartDate.getText().toString();
            String vacEndDate = editEndDate.getText().toString();

            SimpleDateFormat sdf = new SimpleDateFormat(myDateFormat, Locale.US);
            Date myStartDate = null;
            Date myEndDate = null;
            try{
                myStartDate = sdf.parse(vacStartDate);
            }catch (ParseException e){
                e.printStackTrace();
            }
            try{
                myEndDate = sdf.parse(vacEndDate);
            }catch (ParseException e){
                e.printStackTrace();
            }
            Long trigger1 = myStartDate.getTime();
            Long trigger2 = myEndDate.getTime();

            Intent intent1 = new Intent(VacationDetails.this, MyReceiver.class);
            intent1.putExtra("key", editName.getText().toString()+" is starting on "+editStartDate.getText().toString());
            PendingIntent sender1=PendingIntent.getBroadcast(VacationDetails.this, ++MainActivity.numAlert, intent1,PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger1, sender1);

            Intent intent2 = new Intent(VacationDetails.this, MyReceiver.class);
            intent2.putExtra("key", editName.getText().toString()+" is ending on "+editEndDate.getText().toString());
            PendingIntent sender2=PendingIntent.getBroadcast(VacationDetails.this, ++MainActivity.numAlert, intent2,PendingIntent.FLAG_IMMUTABLE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger2, sender2);
            return true;
        }
        return true;
    }



    @Override
    protected void onResume(){
        super.onResume();
        RecyclerView recyclerView = findViewById(R.id.excRecyclerView);
        final ExcAdapter excAdapter = new ExcAdapter(this);
        recyclerView.setAdapter(excAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Exc> filteredExcs = new ArrayList<>();
        for (Exc c : repository.getAllExcs()){
            if (c.getVacationID() == vacationID) filteredExcs.add(c);
        }
        excAdapter.setExcs(filteredExcs);

    }



}