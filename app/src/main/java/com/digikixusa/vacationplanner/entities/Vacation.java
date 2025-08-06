package com.example.d424_softwareengineeringcapstone.entities;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@SuppressLint("ParcelCreator")
@Entity(tableName = "vacations")
public class Vacation implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int vacationID;
    private String vacationName;
    private String hotel;
    private String startDate;
    private String endDate;
    private int numExcursions;

    public Vacation(int vacationID, String vacationName, String hotel, String startDate, String endDate, int numExcursions) {
        this.vacationID = vacationID;
        this.vacationName = vacationName;
        this.hotel = hotel;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numExcursions = numExcursions;
    }


    public int getNumExcursions() {
        return numExcursions;
    }

    public void setNumExcursions(int numExcursions) {
        this.numExcursions = numExcursions;
    }
    public String toString(){
        return vacationName;
    }

    public int getVacationID() {
        return vacationID;
    }

    public void setVacationID(int vacationID) {
        this.vacationID = vacationID;
    }

    public String getVacationName() {
        return vacationName;
    }

    public void setVacationName(String vacationName) {
        this.vacationName = vacationName;
    }

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    protected Vacation(Parcel in){
        vacationID = in.readInt();
        vacationName = in.readString();
        hotel = in.readString();
        startDate = in.readString();
        endDate = in.readString();
        numExcursions = in.readInt();
    }

    public static final Creator<Vacation> CREATOR = new Creator<Vacation>(){
        @Override
        public Vacation createFromParcel(Parcel in){
            return new Vacation(in);
        }

        @Override
        public Vacation[] newArray(int size) {
            return new Vacation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(vacationID);
        dest.writeString(vacationName);
        dest.writeString(hotel);
        dest.writeString(startDate);
        dest.writeString(endDate);
        dest.writeInt(numExcursions);
    }
}

