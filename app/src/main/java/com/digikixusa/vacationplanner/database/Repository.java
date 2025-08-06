package com.example.d424_softwareengineeringcapstone.database;

import android.app.Application;

import com.example.d424_softwareengineeringcapstone.dao.ExcDAO;
import com.example.d424_softwareengineeringcapstone.dao.VacationDAO;
import com.example.d424_softwareengineeringcapstone.entities.Exc;
import com.example.d424_softwareengineeringcapstone.entities.Vacation;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Repository {
    private ExcDAO mExcDAO;
    private VacationDAO mVacationDAO;

    private List<Vacation> mAllVacations;
    private List<Exc> mAllExcs;

    private static int NUMBER_OF_THREADS=4;
    static final ExecutorService databaseExecutor= Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application){
        VacationDatabaseBuilder db=VacationDatabaseBuilder.getDatabase(application);
        mExcDAO =db.excDAO();
        mVacationDAO =db.vacationDAO();
    }

    public List<Vacation> getAllVacations(){
        databaseExecutor.execute(()->{
            mAllVacations = mVacationDAO.getAllVacations();
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return mAllVacations;
    }
    public void insert(Vacation vacation){
        databaseExecutor.execute(()->{
            vacation.setNumExcursions(0);//set to 0 since insert is only on new
            mVacationDAO.insert(vacation);
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void update(Vacation vacation){
        databaseExecutor.execute(()->{
            vacation.setNumExcursions(mExcDAO.getAssociatedExcs(vacation.getVacationID()).size());
            mVacationDAO.update(vacation);
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void delete(Vacation vacation){
        databaseExecutor.execute(()->{
            mVacationDAO.delete(vacation);
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void deleteAllVacations(){
        databaseExecutor.execute(() -> {
            mVacationDAO.deleteAllVacations();
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public List<Vacation> searchVacations(String query){
        databaseExecutor.execute(() -> {
            mAllVacations =  mVacationDAO.searchVacations(query);
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllVacations;
    }


    public List<Exc> getAllExcs(){
        databaseExecutor.execute(()->{
            mAllExcs = mExcDAO.getAllExcs();
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllExcs;
    }
    public List<Exc> getAssociatedExcs(int vacationID){
        databaseExecutor.execute(()->{
            mAllExcs = mExcDAO.getAssociatedExcs(vacationID);
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllExcs;
    }
    public void insert(Exc exc){
        databaseExecutor.execute(()->{
            mExcDAO.insert(exc);
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void update(Exc exc){
        databaseExecutor.execute(()->{
            mExcDAO.update(exc);
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void delete(Exc exc){
        databaseExecutor.execute(()->{
            mExcDAO.delete(exc);
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void deleteAllExcursions(){
        databaseExecutor.execute(() -> {
            mExcDAO.deleteAllExcursions();
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public List<Exc> searchExcursions(String query){
        databaseExecutor.execute(() -> {
            mAllExcs = mExcDAO.searchExcursions(query);
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllExcs;
    }




}
