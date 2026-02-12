package com.example.chroniccare.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "medical_documents")
public class MedicalDocument {
    @PrimaryKey(autoGenerate = true)
    public int id;
    
    public String userId;
    public String documentName;
    public String documentType;
    public String documentUri;
    public long uploadDate;
    public long fileSize;
}
