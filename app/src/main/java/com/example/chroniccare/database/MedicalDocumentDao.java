package com.example.chroniccare.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MedicalDocumentDao {
    @Insert
    void insert(MedicalDocument document);
    
    @Delete
    void delete(MedicalDocument document);
    
    @Query("SELECT * FROM medical_documents WHERE userId = :userId ORDER BY uploadDate DESC")
    List<MedicalDocument> getDocumentsByUserId(String userId);
}
