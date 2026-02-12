package com.example.chroniccare.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;
    
    public String userId;
    public String name;
    public String email;
    public String phone;
    public String dob;
    public String gender;
    public String bloodGroup;
    public String height;
    public String weight;
    public String conditions;
    public String allergies;
    public String emergencyName;
    public String emergencyPhone;
    public String emergencyRelation;
}
