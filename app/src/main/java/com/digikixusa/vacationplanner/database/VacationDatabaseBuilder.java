package com.digikixusa.vacationplanner.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.digikixusa.vacationplanner.dao.ExcDAO;
import com.digikixusa.vacationplanner.dao.VacationDAO;
import com.digikixusa.vacationplanner.entities.Exc;
import com.digikixusa.vacationplanner.entities.Vacation;


@Database(entities = {Vacation.class, Exc.class}, version = 2, exportSchema = false)
public abstract class VacationDatabaseBuilder extends RoomDatabase {
    public abstract VacationDAO vacationDAO();
    public abstract ExcDAO excDAO();
    private static volatile VacationDatabaseBuilder INSTANCE;

    static VacationDatabaseBuilder getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (VacationDatabaseBuilder.class){
                if(INSTANCE==null){
                    INSTANCE= Room.databaseBuilder(context.getApplicationContext(),VacationDatabaseBuilder.class,
                                    "MyVacationDatabase.db")
                            .fallbackToDestructiveMigration(true)
                            .build();
                }
            }
        }
        return INSTANCE;
    }



}
