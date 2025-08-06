package com.example.d424_softwareengineeringcapstone.entities;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "excursions")
public class Exc {
    @PrimaryKey(autoGenerate = true)
    private int excID;
    private String excName;
    private String date;
    private int vacationID;

    public Exc(int excID, String excName, String date, int vacationID) {
        this.excID = excID;
        this.excName = excName;
        this.date = date;
        this.vacationID = vacationID;
    }

    public int getExcID() {
        return excID;
    }

    public void setExcID(int excID) {
        this.excID = excID;
    }

    public String getExcName() {
        return excName;
    }

    public void setExcName(String excName) {
        this.excName = excName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getVacationID() {
        return vacationID;
    }

    public void setVacationID(int vacationID) {
        this.vacationID = vacationID;
    }

}

