# Quick Refactoring Guide - ChronicCare

## 🎯 Goal
Organize the codebase for better readability, reusability, and maintainability.

---

## 📋 Step-by-Step Refactoring

### Step 1: Create Directory Structure (5 minutes)

```bash
cd /Users/saisubhamsahu/StudioProjects/ChronicCare/app/src/main/java/com/example/chroniccare/

# Create activity directories
mkdir -p activities/auth
mkdir -p activities/main
mkdir -p activities/documents
mkdir -p activities/chat
mkdir -p activities/base

# Create database subdirectories
mkdir -p database/entities
mkdir -p database/dao

# Create other directories
mkdir -p adapters
mkdir -p repositories
mkdir -p services
```

### Step 2: Move Activity Files (10 minutes)

```bash
# Authentication activities
mv LogInPage.java activities/auth/
mv SignUpPage.java activities/auth/

# Main activities
mv MainActivity.java activities/main/
mv HomeActivity.java activities/main/
mv ProfileActivity.java activities/main/
mv MonitorActivity.java activities/main/
mv MedicationsActivity.java activities/main/
mv AddMedications.java activities/main/

# Document activities
mv DocumentsActivity.java activities/documents/

# Chat activities
mv DrGPTActivity.java activities/chat/

# Base activities
mv BaseActivity.java activities/base/
mv BottomNavActivity.java activities/base/
```

### Step 3: Move Database Files (10 minutes)

```bash
# Move entities
mv database/User.java database/entities/
mv database/MedicalDocument.java database/entities/
mv database/ChatMessage.java database/entities/
mv database/ExerciseLog.java database/entities/
mv database/FoodLog.java database/entities/
mv database/Note.java database/entities/
mv database/Reminder.java database/entities/
mv database/NoteWithUserView.java database/entities/

# Move DAOs
mv database/UserDao.java database/dao/
mv database/MedicalDocumentDao.java database/dao/
mv database/ChatMessageDao.java database/dao/
mv database/ExerciseDao.java database/dao/
mv database/FoodDao.java database/dao/
mv database/NoteDao.java database/dao/
mv database/ReminderDao.java database/dao/
mv database/NoteWithUserDao.java database/dao/
```

### Step 4: Update Package Declarations (15 minutes)

After moving files, update package declarations in each file:

**Example for ProfileActivity.java:**
```java
// OLD
package com.example.chroniccare;

// NEW
package com.example.chroniccare.activities.main;
```

**Example for User.java:**
```java
// OLD
package com.example.chroniccare.database;

// NEW
package com.example.chroniccare.database.entities;
```

### Step 5: Update Imports (15 minutes)

Update import statements in all files that reference moved classes.

**Example:**
```java
// OLD
import com.example.chroniccare.database.User;

// NEW
import com.example.chroniccare.database.entities.User;
```

### Step 6: Update AndroidManifest.xml (5 minutes)

Update activity declarations:

```xml
<!-- OLD -->
<activity android:name=".ProfileActivity" />

<!-- NEW -->
<activity android:name=".activities.main.ProfileActivity" />
```

---

## 🔧 Automated Refactoring (Recommended)

### Using Android Studio

1. **Right-click on file** → **Refactor** → **Move**
2. Select destination package
3. Android Studio will:
   - Update package declaration
   - Update all imports
   - Update AndroidManifest.xml

### Benefits
- ✅ No manual import updates
- ✅ No broken references
- ✅ Faster and safer

---

## 📁 Final Structure

```
com.example.chroniccare/
├── activities/
│   ├── auth/
│   │   ├── LogInPage.java
│   │   └── SignUpPage.java
│   ├── main/
│   │   ├── MainActivity.java
│   │   ├── HomeActivity.java
│   │   ├── ProfileActivity.java
│   │   ├── MonitorActivity.java
│   │   ├── MedicationsActivity.java
│   │   └── AddMedications.java
│   ├── documents/
│   │   └── DocumentsActivity.java
│   ├── chat/
│   │   └── DrGPTActivity.java
│   └── base/
│       ├── BaseActivity.java
│       └── BottomNavActivity.java
│
├── api/
│   ├── DrGPTApiService.java
│   ├── RetrofitClient.java
│   └── (models)
│
├── database/
│   ├── entities/
│   │   ├── User.java
│   │   ├── MedicalDocument.java
│   │   └── ...
│   ├── dao/
│   │   ├── UserDao.java
│   │   ├── MedicalDocumentDao.java
│   │   └── ...
│   └── AppDatabase.java
│
├── utils/
│   ├── FirebaseSync.java
│   ├── FirebaseStorageHelper.java
│   └── ProfileImageHelper.java
│
└── (other packages)
```

---

## ✅ Verification Checklist

After refactoring:

- [ ] All files compile without errors
- [ ] No import errors
- [ ] AndroidManifest.xml updated
- [ ] App runs successfully
- [ ] All features work as before
- [ ] No runtime crashes

---

## 🚨 Common Issues & Solutions

### Issue 1: Import Errors
**Solution**: Use Android Studio's "Optimize Imports" (Ctrl+Alt+O / Cmd+Option+O)

### Issue 2: Activity Not Found
**Solution**: Check AndroidManifest.xml has correct package paths

### Issue 3: Database Errors
**Solution**: Update AppDatabase.java imports for entities and DAOs

### Issue 4: Build Fails
**Solution**: Clean and rebuild project
```bash
./gradlew clean build
```

---

## 🎨 Code Style After Refactoring

### Activity Example
```java
package com.example.chroniccare.activities.main;

import com.example.chroniccare.activities.base.BaseActivity;
import com.example.chroniccare.database.entities.User;
import com.example.chroniccare.database.dao.UserDao;
import com.example.chroniccare.utils.FirebaseSync;

public class ProfileActivity extends BaseActivity {
    // Clean, organized code
}
```

### Database Example
```java
package com.example.chroniccare.database;

import com.example.chroniccare.database.entities.*;
import com.example.chroniccare.database.dao.*;

@Database(entities = {User.class, MedicalDocument.class, ...})
public abstract class AppDatabase extends RoomDatabase {
    // ...
}
```

---

## 📊 Before vs After

### Before (Flat Structure)
```
chroniccare/
├── ProfileActivity.java (500 lines)
├── DocumentsActivity.java (400 lines)
├── DrGPTActivity.java (350 lines)
├── User.java
├── UserDao.java
└── ... (50+ files in one directory)
```

### After (Organized Structure)
```
chroniccare/
├── activities/
│   ├── main/ProfileActivity.java (300 lines)
│   └── documents/DocumentsActivity.java (250 lines)
├── database/
│   ├── entities/User.java
│   └── dao/UserDao.java
└── utils/
    └── FirebaseSync.java
```

**Benefits:**
- ✅ Easy to find files
- ✅ Clear separation of concerns
- ✅ Better code organization
- ✅ Easier to maintain
- ✅ Scalable structure

---

## 🚀 Next Level Refactoring (Optional)

### 1. Extract Large Methods
```java
// Before: 100-line method
private void saveProfile() {
    // 100 lines of code
}

// After: Multiple small methods
private void saveProfile() {
    validateInput();
    createUserObject();
    saveToDatabase();
    syncToFirebase();
}
```

### 2. Create Repository Classes
```java
public class UserRepository {
    private UserDao userDao;
    private FirebaseSync firebaseSync;
    
    public void saveUser(User user) {
        userDao.insert(user);
        firebaseSync.syncProfile(user);
    }
}
```

### 3. Use ViewModels (MVVM)
```java
public class ProfileViewModel extends ViewModel {
    private UserRepository repository;
    private LiveData<User> user;
    
    public void saveProfile(User user) {
        repository.saveUser(user);
    }
}
```

---

## 📝 Documentation After Refactoring

Update these files:
- [ ] README.md - Update project structure section
- [ ] CONTRIBUTING.md - Add coding guidelines
- [ ] ARCHITECTURE.md - Document architecture decisions

---

## ⏱️ Time Estimate

- **Manual Refactoring**: 2-3 hours
- **Using Android Studio**: 30-45 minutes
- **Testing**: 30 minutes
- **Total**: 1-4 hours

---

## 🎯 Success Criteria

✅ All files organized in logical directories
✅ No compilation errors
✅ App runs without crashes
✅ All features work correctly
✅ Code is more readable
✅ Easy to find specific files
✅ Ready for team collaboration

---

**Recommendation**: Use Android Studio's refactoring tools for safety and speed!

**Last Updated**: February 12, 2026
