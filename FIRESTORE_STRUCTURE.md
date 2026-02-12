# Firestore Database Structure - ChronicCare

## Complete Hierarchical Structure

```
users/ (collection)
  └── {userId}/ (document)
      │
      ├── profile/ (subcollection)
      │   ├── personalInfo/ (document)
      │   │   ├── name: string
      │   │   ├── email: string
      │   │   ├── phone: string
      │   │   ├── dob: string
      │   │   ├── gender: string
      │   │   └── bloodGroup: string
      │   │
      │   ├── medicalInfo/ (document)
      │   │   ├── height: string
      │   │   ├── weight: string
      │   │   ├── conditions: string
      │   │   └── allergies: string
      │   │
      │   └── emergencyContact/ (document)
      │       ├── name: string
      │       ├── phone: string
      │       └── relation: string
      │
      ├── medications/ (subcollection)
      │   └── {medicationId}/ (document)
      │       ├── medName: string
      │       ├── dose: string
      │       ├── timing: map
      │       └── ...
      │
      ├── documents/ (subcollection)
      │   └── {documentId}/ (document)
      │       ├── documentName: string
      │       ├── documentType: string
      │       ├── documentUri: string
      │       ├── uploadDate: timestamp
      │       └── fileSize: number
      │
      ├── drGptChats/ (subcollection)
      │   └── {sessionId}/ (document)
      │       └── messages/ (subcollection)
      │           └── {messageId}/ (document)
      │               ├── role: string ("user" | "assistant")
      │               ├── content: string
      │               └── timestamp: number
      │
      ├── exercises/ (subcollection)
      │   └── {exerciseId}/ (document)
      │       └── (exercise data)
      │
      └── foods/ (subcollection)
          └── {foodId}/ (document)
              └── (food data)
```

## Collection Details

### 1. Profile Collection
**Path:** `users/{userId}/profile/`

#### personalInfo Document
- Contains basic user information
- Fields: name, email, phone, dob, gender, bloodGroup

#### medicalInfo Document
- Contains health-related information
- Fields: height, weight, conditions, allergies

#### emergencyContact Document
- Contains emergency contact details
- Fields: name, phone, relation

### 2. Medications Collection
**Path:** `users/{userId}/medications/`
- Already implemented by AddMedications activity
- Each document represents one medication
- Auto-generated document IDs

### 3. Documents Collection
**Path:** `users/{userId}/documents/`
- Medical documents (lab reports, prescriptions, etc.)
- Each document contains metadata about uploaded files
- Document ID is timestamp-based

### 4. Dr.GPT Chats Collection
**Path:** `users/{userId}/drGptChats/{sessionId}/messages/`
- Organized by session ID
- Each message is a separate document
- Supports multiple chat sessions per user

### 5. Exercises Collection
**Path:** `users/{userId}/exercises/`
- Ready for future implementation
- Will store exercise logs

### 6. Foods Collection
**Path:** `users/{userId}/foods/`
- Ready for future implementation
- Will store food/nutrition logs

## Benefits of This Structure

### ✅ Organized & Scalable
- Clear separation of concerns
- Easy to query specific data types
- Supports future expansion

### ✅ User-Centric
- All data linked to userId
- Easy to fetch all user data
- Simple to implement user deletion

### ✅ Efficient Queries
- Can query specific subcollections
- No need to fetch entire user document
- Better performance

### ✅ Security Rules Ready
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /users/{userId}/{document=**} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
  }
}
```

## Example Queries

### Get Personal Info
```java
db.collection("users").document(userId)
  .collection("profile").document("personalInfo")
  .get();
```

### Get All Medications
```java
db.collection("users").document(userId)
  .collection("medications")
  .get();
```

### Get Chat Messages
```java
db.collection("users").document(userId)
  .collection("drGptChats").document(sessionId)
  .collection("messages")
  .orderBy("timestamp")
  .get();
```

### Get All Documents
```java
db.collection("users").document(userId)
  .collection("documents")
  .get();
```

## Migration Notes

### Old Structure (Flat)
```
users/{userId}
  ├── name
  ├── email
  ├── phone
  └── ...
```

### New Structure (Organized)
```
users/{userId}/profile/personalInfo
  ├── name
  ├── email
  └── phone
```

## Implementation Status

✅ **Profile** - Implemented with 3 documents (personalInfo, medicalInfo, emergencyContact)
✅ **Medications** - Already working (existing implementation)
✅ **Documents** - Implemented with proper structure
✅ **Dr.GPT Chats** - Implemented with session-based organization
⏳ **Exercises** - Structure ready, awaiting implementation
⏳ **Foods** - Structure ready, awaiting implementation

## Build Status
✅ **BUILD SUCCESSFUL**

All data now saves with proper hierarchical structure! 🎉
