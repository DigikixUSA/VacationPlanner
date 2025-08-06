package com.digikixusa.vacationplanner.UI;

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

import com.digikixusa.vacationplanner.R;
import com.digikixusa.vacationplanner.database.Repository;
import com.digikixusa.vacationplanner.entities.Exc;
import com.digikixusa.vacationplanner.entities.Vacation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ExcDetails extends AppCompatActivity {

    String name;
    String date;
    int excID;
    int vacaID;
    String vacTitle;
    String vacTitleDates;
    EditText editName;
    TextView editDate;
    TextView vacTitleTextView;//not named editVac... because these are not edited but generated
    TextView vacTitleDatesTextView;
    Repository repository;
    DatePickerDialog.OnDateSetListener startDate;
    String myDateFormat = "MM/dd/yyyy";
    final Calendar myCalendarStart = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_exc_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        repository = new Repository(getApplication());
        name = getIntent().getStringExtra("name");
        editName = findViewById(R.id.excName);
        editName.setText(name);
        date = getIntent().getStringExtra("date");
        editDate = findViewById(R.id.excDate);
        editDate.setText(date);
        excID = getIntent().getIntExtra("id", -1);
        vacaID = getIntent().getIntExtra("vacaID", -1);

        vacTitle = "During vacation: ";
        //find which vacation this is associated with and extract its info
        List<Vacation> vacs = repository.getAllVacations();
        for (Vacation v : vacs) {
            if (v.getVacationID() == vacaID) {
                vacTitle += v.getVacationName();
                vacTitleDates = "From "+v.getStartDate()+" to "+v.getEndDate();
                break; //found, so break loop
            }
        }
        vacTitleTextView = findViewById(R.id.vacTitle);
        vacTitleTextView.setText(vacTitle);
        vacTitleDatesTextView = findViewById(R.id.vacTitleDates);
        vacTitleDatesTextView.setText(vacTitleDates);

        SimpleDateFormat sdf = new SimpleDateFormat(myDateFormat, Locale.US);



        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date;
                String info = editDate.getText().toString();
                if (info.equals("")) info = "02/10/2024";
                try {
                    myCalendarStart.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(ExcDetails.this, startDate, myCalendarStart
                        .get(Calendar.YEAR), myCalendarStart.get(Calendar.MONTH),
                        myCalendarStart.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        startDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendarStart.set(Calendar.YEAR, year);
                myCalendarStart.set(Calendar.MONTH, month);
                myCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelStart();
            }
        };
    }

    private void updateLabelStart() {

        SimpleDateFormat sdf = new SimpleDateFormat(myDateFormat, Locale.US);
        editDate.setText(sdf.format(myCalendarStart.getTime()));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_exc_details, menu);
        return true;
    }

    private boolean validateDatesAndDifference(String date1, String date2, String date3) {
        int dateDiff1, dateDiff2;
        Date sdate = null;
        Date edate = null;
        Date excdate = null;
        SimpleDateFormat sdf = new SimpleDateFormat(myDateFormat);
        try {
            sdate = sdf.parse(date1);
        } catch (ParseException e) {
            System.out.println("Error parsing date:" + e.toString());
            Toast.makeText(ExcDetails.this, "Invalid start date.", Toast.LENGTH_LONG).show();
        }
        try {
            edate = sdf.parse(date2);
        } catch (ParseException e) {
            System.out.println("Error parsing date:" + e.toString());
            Toast.makeText(ExcDetails.this, "Invalid end date.", Toast.LENGTH_LONG).show();
        }
        try {
            excdate = sdf.parse(date3);
        } catch (ParseException e) {
            System.out.println("Error parsing date:" + e.toString());
            Toast.makeText(ExcDetails.this, "Invalid excursion date.", Toast.LENGTH_LONG).show();
        }
        dateDiff1 = -9999;//set to impossible values to flag logic error
        dateDiff2 = -9999;
        if ((sdate != null) && (edate != null) && (excdate != null)) {
            dateDiff1 = sdate.compareTo(excdate);
            dateDiff2 = edate.compareTo(excdate);
            if (dateDiff1 <= 0 && dateDiff2 >= 0) {
                //date selected is good
                return true;
            } else {
                Toast.makeText(ExcDetails.this, "Excursion date must be between Vacation dates of "+sdf.format(sdate)+" and "+sdf.format(edate), Toast.LENGTH_LONG).show();
            }
        }

        System.out.println("Error with excursion date. Vacation start:" + sdate.toString() + " Vacation end:" + edate.toString() + " Selected excursion date:" + excdate.toString());
        return false;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        if (item.getItemId() == R.id.excsave) {
            Exc exc;
            List<Vacation> vacs = repository.getAllVacations();

            for (Vacation v : vacs) {
                if (v.getVacationID() == vacaID) {
                    String date1 = v.getStartDate();
                    String date2 = v.getEndDate();
                    if (validateDatesAndDifference(date1, date2, editDate.getText().toString())) {
                        //ok to save
                        if (excID == -1) {
                            if (repository.getAllExcs().size() == 0)
                                excID = 1;
                            else
                                excID = repository.getAllExcs().get(repository.getAllExcs().size() - 1).getExcID() + 1;
                            exc = new Exc(excID, editName.getText().toString(), editDate.getText().toString(), vacaID);
                            System.out.println("**** saving excursion #"+excID+" into vacation#"+vacaID);
                            repository.insert(exc);
                            Toast.makeText(ExcDetails.this, "Excursion " + editName.getText().toString() + " saved!", Toast.LENGTH_LONG).show();
                            ExcDetails.this.finish();
                        } else {
                            exc = new Exc(excID, editName.getText().toString(), editDate.getText().toString(), vacaID);
                            System.out.println("**** updating excursion #"+excID+" into vacation#"+vacaID);
                            repository.update(exc);
                            Toast.makeText(ExcDetails.this, "Excursion " + editName.getText().toString() + " updated!", Toast.LENGTH_LONG).show();
                            ExcDetails.this.finish();
                        }
                    }
                }
            }
        }


        if (item.getItemId() == R.id.notifyexc){

            String startDate = editDate.getText().toString();

            SimpleDateFormat sdf = new SimpleDateFormat(myDateFormat, Locale.US);
            Date myStartDate = null;

            try{
                myStartDate = sdf.parse(startDate);
            }catch (ParseException e){
                e.printStackTrace();
            }

            Long trigger = myStartDate.getTime();

            Intent intent = new Intent(ExcDetails.this, MyReceiver.class);
            intent.putExtra("key", "Excursion "+editName.getText().toString()+" is starting on "+editDate.getText().toString());
            PendingIntent sender=PendingIntent.getBroadcast(ExcDetails.this, ++MainActivity.numAlert, intent,PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);

            return true;
        }

        if (item.getItemId() == R.id.excdelete) {
            for (Exc c : repository.getAllExcs()) {
                if (c.getExcID() == excID) {

                    Toast.makeText(ExcDetails.this, c.getExcName() + " was deleted.", Toast.LENGTH_LONG).show();
                    repository.delete(c);
                    ExcDetails.this.finish();
                }
            }
            return true;
        }
        return true;
    }

}
