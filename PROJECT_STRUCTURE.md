# ChronicCare - Project Structure & Architecture

## 📁 Current Project Structure

```
app/src/main/java/com/example/chroniccare/
│
├── 📂 activities/              # UI Activities (to be created)
│   ├── auth/                   # Authentication related
│   │   ├── LogInPage.java
│   │   └── SignUpPage.java
│   │
│   ├── main/                   # Main app screens
│   │   ├── MainActivity.java
│   │   ├── HomeActivity.java
│   │   ├── ProfileActivity.java
│   │   ├── MonitorActivity.java
│   │   └── MedicationsActivity.java
│   │
│   ├── documents/              # Document management
│   │   ├── DocumentsActivity.java
│   │   └── DocumentViewerActivity.java
│   │
│   ├── chat/                   # Dr.GPT Chat
│   │   └── DrGPTActivity.java
│   │
│   └── base/                   # Base classes
│       ├── BaseActivity.java
│       └── BottomNavActivity.java
│
├── 📂 api/                     # API & Network (✅ exists)
│   ├── DrGPTApiService.java
│   ├── RetrofitClient.java
│   ├── ChatRequest.java
│   ├── ChatResponse.java
│   ├── ChatHistoryResponse.java
│   ├── StatusResponse.java
│   └── BookingSlotsResponse.java
│
├── 📂 database/                # Local Database (✅ exists)
│   ├── entities/               # Database entities (to organize)
│   │   ├── User.java
│   │   ├── MedicalDocument.java
│   │   ├── ChatMessage.java
│   │   ├── ExerciseLog.java
│   │   ├── FoodLog.java
│   │   ├── Note.java
│   │   └── Reminder.java
│   │
│   ├── dao/                    # Data Access Objects (to organize)
│   │   ├── UserDao.java
│   │   ├── MedicalDocumentDao.java
│   │   ├── ChatMessageDao.java
│   │   ├── ExerciseDao.java
│   │   ├── FoodDao.java
│   │   ├── NoteDao.java
│   │   └── ReminderDao.java
│   │
│   └── AppDatabase.java       # Main database class
│
├── 📂 utils/                   # Utility Classes (✅ exists)
│   ├── FirebaseSync.java
│   ├── FirebaseStorageHelper.java
│   └── ProfileImageHelper.java
│
├── 📂 adapters/                # RecyclerView Adapters (to be created)
│   ├── DocumentsAdapter.java
│   ├── MedicationsAdapter.java
│   └── ChatAdapter.java
│
├── 📂 models/                  # Data Models (✅ exists)
│   └── (domain models if needed)
│
├── 📂 services/                # Background Services
│   ├── AlarmService.java
│   └── NotificationService.java
│
└── 📂 receivers/               # Broadcast Receivers
    └── AlarmReceiver.java
```

---

## 🎯 Recommended Refactoring Plan

### Phase 1: Organize Activities (Priority: HIGH)
Move all activity files to proper subdirectories:

```bash
activities/
├── auth/
│   ├── LogInPage.java
│   └── SignUpPage.java
├── main/
│   ├── MainActivity.java
│   ├── HomeActivity.java
│   ├── ProfileActivity.java
│   ├── MonitorActivity.java
│   └── MedicationsActivity.java
├── documents/
│   └── DocumentsActivity.java
├── chat/
│   └── DrGPTActivity.java
└── base/
    ├── BaseActivity.java
    └── BottomNavActivity.java
```

### Phase 2: Organize Database (Priority: MEDIUM)
Separate entities and DAOs:

```bash
database/
├── entities/
│   ├── User.java
│   ├── MedicalDocument.java
│   └── ...
├── dao/
│   ├── UserDao.java
│   ├── MedicalDocumentDao.java
│   └── ...
└── AppDatabase.java
```

### Phase 3: Create Adapters (Priority: MEDIUM)
Extract RecyclerView logic from activities.

### Phase 4: Add Repositories (Priority: LOW)
Create repository pattern for data management.

---

## 📋 File Organization Guidelines

### 1. Activities
- **Purpose**: Handle UI and user interactions
- **Max Lines**: 300-400 lines
- **Responsibilities**: 
  - Initialize views
  - Handle user input
  - Navigate between screens
  - Delegate business logic to utils/repositories

### 2. Utils
- **Purpose**: Reusable helper functions
- **Max Lines**: 200-300 lines
- **Responsibilities**:
  - Firebase operations
  - Image loading
  - Date formatting
  - Validation

### 3. Database
- **Entities**: Data models (50-100 lines each)
- **DAOs**: Database queries (100-150 lines each)
- **AppDatabase**: Central database class

### 4. API
- **Services**: API interface definitions
- **Models**: Request/Response classes
- **Client**: Retrofit configuration

---

## 🔧 Code Quality Standards

### Naming Conventions
```java
// Activities
public class ProfileActivity extends BaseActivity { }

// Utils
public class FirebaseSync { }

// Database Entities
@Entity(tableName = "users")
public class User { }

// DAOs
@Dao
public interface UserDao { }

// API Services
public interface DrGPTApiService { }
```

### Package Naming
```
com.example.chroniccare.activities.main
com.example.chroniccare.database.entities
com.example.chroniccare.utils
```

### File Size Limits
- ✅ **Good**: < 300 lines
- ⚠️ **Refactor**: 300-500 lines
- ❌ **Must Split**: > 500 lines

---

## 🚀 Quick Refactoring Commands

### Create Directory Structure
```bash
cd app/src/main/java/com/example/chroniccare/

# Create activity directories
mkdir -p activities/{auth,main,documents,chat,base}

# Create database subdirectories
mkdir -p database/{entities,dao}

# Create adapter directory
mkdir -p adapters

# Create repository directory
mkdir -p repositories
```

### Move Files (Example)
```bash
# Move activities
mv ProfileActivity.java activities/main/
mv LogInPage.java activities/auth/
mv DrGPTActivity.java activities/chat/

# Move database entities
mv database/User.java database/entities/
mv database/UserDao.java database/dao/
```

---

## 📚 Architecture Patterns Used

### 1. **MVVM-like Pattern**
```
Activity → ViewModel/Utils → Repository → Database/API
```

### 2. **Repository Pattern** (Recommended)
```java
public class DocumentRepository {
    private MedicalDocumentDao localDao;
    private FirebaseSync firebaseSync;
    
    public void saveDocument(MedicalDocument doc) {
        // Save to local DB
        localDao.insert(doc);
        // Sync to cloud
        firebaseSync.syncDocument(doc);
    }
}
```

### 3. **Singleton Pattern**
- AppDatabase
- RetrofitClient
- FirebaseSync instances

---

## 🎨 UI Organization

### Layout Files
```
res/layout/
├── activities/
│   ├── activity_main.xml
│   ├── activity_profile.xml
│   └── activity_dr_gpt.xml
├── fragments/
│   └── (if using fragments)
├── items/
│   ├── item_document.xml
│   └── item_medication.xml
└── dialogs/
    └── bottom_sheet_upload_document.xml
```

---

## 🔐 Security Best Practices

### 1. API Keys
- ✅ Store in `local.properties`
- ❌ Never commit to git

### 2. Firebase Rules
- ✅ User-specific access
- ❌ Open access in production

### 3. Data Validation
- ✅ Validate all user inputs
- ✅ Sanitize before database insert

---

## 📊 Performance Guidelines

### 1. Database Operations
```java
// ✅ Good: Use background thread
Executors.newSingleThreadExecutor().execute(() -> {
    db.userDao().insert(user);
});

// ❌ Bad: Main thread
db.userDao().insert(user); // Crashes!
```

### 2. Image Loading
```java
// ✅ Good: Use Picasso/Glide
Picasso.get().load(url).into(imageView);

// ❌ Bad: Manual loading
// Heavy operation on main thread
```

### 3. Memory Management
```java
// ✅ Good: Cleanup in onDestroy
@Override
protected void onDestroy() {
    super.onDestroy();
    if (executorService != null) {
        executorService.shutdown();
    }
}
```

---

## 🧪 Testing Structure

```
app/src/test/java/com/example/chroniccare/
├── database/
│   └── UserDaoTest.java
├── utils/
│   └── FirebaseSyncTest.java
└── api/
    └── DrGPTApiTest.java
```

---

## 📝 Documentation Standards

### Class Documentation
```java
/**
 * Manages user profile data and synchronization with Firebase.
 * 
 * Features:
 * - Load profile from local database
 * - Sync profile to Firestore
 * - Upload profile picture to Firebase Storage
 * 
 * @author ChronicCare Team
 * @version 1.0
 */
public class ProfileActivity extends BaseActivity {
    // ...
}
```

### Method Documentation
```java
/**
 * Uploads document to Firebase Storage and syncs metadata to Firestore.
 * 
 * @param uri Local file URI
 * @param docType Type of document (Lab Report, Prescription, etc.)
 * @param docName User-provided document name
 */
private void uploadDocument(Uri uri, String docType, String docName) {
    // ...
}
```

---

## 🔄 Git Workflow

### Branch Structure
```
main
├── develop
├── feature/profile-sync
├── feature/document-upload
└── bugfix/chat-crash
```

### Commit Messages
```
✅ Good:
feat: Add Firebase Storage integration for documents
fix: Resolve null pointer in ProfileActivity
refactor: Organize activities into subdirectories

❌ Bad:
updated files
fixed bug
changes
```

---

## 📦 Dependencies Management

### Current Dependencies
```kotlin
// Firebase
implementation("com.google.firebase:firebase-firestore:26.0.2")
implementation("com.google.firebase:firebase-storage:20.3.0")
implementation("com.google.firebase:firebase-auth:22.1.1")

// Room Database
implementation("androidx.room:room-runtime:2.8.2")
annotationProcessor("androidx.room:room-compiler:2.8.2")

// Networking
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")

// Image Loading
implementation("com.squareup.picasso:picasso:2.8")
implementation("de.hdodenhof:circleimageview:3.1.0")
```

---

## 🎯 Next Steps

### Immediate (This Week)
1. ✅ Create directory structure
2. ✅ Move activities to subdirectories
3. ✅ Organize database entities/DAOs
4. ✅ Update import statements

### Short Term (This Month)
1. Create adapter classes
2. Implement repository pattern
3. Add unit tests
4. Improve error handling

### Long Term (Next Quarter)
1. Migrate to Kotlin
2. Implement MVVM with ViewModels
3. Add Dependency Injection (Hilt)
4. Implement offline-first architecture

---

## 📞 Support & Resources

### Documentation
- Firebase: https://firebase.google.com/docs
- Room: https://developer.android.com/training/data-storage/room
- Retrofit: https://square.github.io/retrofit/

### Code Style
- Follow Android Kotlin Style Guide
- Use Android Studio auto-formatting
- Enable code inspections

---

**Last Updated**: February 12, 2026
**Version**: 1.0
**Maintainers**: ChronicCare Development Team
