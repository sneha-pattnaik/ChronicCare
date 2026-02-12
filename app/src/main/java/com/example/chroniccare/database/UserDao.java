package com.example.chroniccare.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);
    
    @Update
    void update(User user);
    
    @Query("SELECT * FROM users WHERE userId = :userId LIMIT 1")
    User getUserByUserId(String userId);
}
