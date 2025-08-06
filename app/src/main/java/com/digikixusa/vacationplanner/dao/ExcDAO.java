package com.example.d424_softwareengineeringcapstone.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.d424_softwareengineeringcapstone.entities.Exc;
import com.example.d424_softwareengineeringcapstone.entities.Vacation;

import java.util.List;

@Dao
public interface ExcDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Exc exc);

    @Update
    void update(Exc exc);

    @Delete
    void delete(Exc exc);

    @Query("SELECT * FROM EXCURSIONS ORDER BY excID ASC")
    List<Exc> getAllExcs();

    @Query("SELECT * FROM EXCURSIONS  WHERE vacationID=:vaca ORDER BY excID ASC")
    List<Exc> getAssociatedExcs(int vaca);

    @Query("DELETE FROM EXCURSIONS")
    void deleteAllExcursions();

    @Query("SELECT * FROM EXCURSIONS WHERE EXCNAME LIKE '%' || :query || '%'")
    List<Exc> searchExcursions(String query);

}

