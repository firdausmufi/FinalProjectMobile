package com.daus.catering.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.daus.catering.database.DatabaseModel;

import java.util.List;

@Dao
public interface DatabaseDao {

    @Query("SELECT * FROM tbl_catering where uid != 1")
    LiveData<List<DatabaseModel>> getAllOrder();

    @Query("SELECT * FROM tbl_catering where username= :username AND password= :password")
    LiveData<List<DatabaseModel>> getUserByName(String username, String password);

    //untuk insert data
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertData(DatabaseModel... modelDatabases);

    @Query("UPDATE tbl_catering SET nama_menu= :nama_menu, jml_items= :jml_items, harga= :harga WHERE uid = :uid")
    void updateData(String nama_menu, int jml_items, int harga, int uid);

    @Query("DELETE FROM tbl_catering WHERE uid= :uid")
    void deleteSingleData(int uid);
}
