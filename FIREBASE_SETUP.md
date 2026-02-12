# Firebase Realtime Database Setup

## Issue
Profile and document data not saving to Firebase Realtime Database.

## Solution

### 1. Enable Firebase Realtime Database
1. Go to Firebase Console: https://console.firebase.google.com/
2. Select your project: **ChronicCare**
3. Click on **Realtime Database** in the left menu
4. Click **Create Database**
5. Choose location (e.g., us-central1)
6. Start in **Test mode** (for development)

### 2. Set Database Rules
Go to the **Rules** tab and paste this:

```json
{
  "rules": {
    "users": {
      "$uid": {
        ".read": "$uid === auth.uid",
        ".write": "$uid === auth.uid"
      }
    },
    "chats": {
      "$uid": {
        ".read": "$uid === auth.uid",
        ".write": "$uid === auth.uid"
      }
    }
  }
}
```

### 3. For Testing (Open Access - NOT for production)
```json
{
  "rules": {
    ".read": true,
    ".write": true
  }
}
```

⚠️ **Warning**: Open access rules allow anyone to read/write. Use only for testing!

### 4. Verify Database URL
Check that your `google-services.json` contains:
```json
{
  "project_info": {
    "firebase_url": "https://YOUR-PROJECT-ID-default-rtdb.firebaseio.com"
  }
}
```

## Testing

### Check Logs
Run the app and filter logcat for:
```
adb logcat | grep FirebaseSync
```

You should see:
- ✅ `FirebaseSync initialized with userId: [ID]`
- ✅ `Syncing profile for user: [ID]`
- ✅ `Profile synced successfully to Firebase`

### Verify in Firebase Console
1. Go to Realtime Database in Firebase Console
2. You should see data structure:
```
users/
  └── [userId]/
      ├── profile/
      │   ├── name: "..."
      │   ├── email: "..."
      │   └── ...
      └── documents/
          └── [docId]/
              ├── documentName: "..."
              └── ...
```

## Common Issues

### 1. Permission Denied
**Error**: `DatabaseError: Permission denied`
**Fix**: Update database rules to allow authenticated users

### 2. Database Not Created
**Error**: No data appears
**Fix**: Create Realtime Database in Firebase Console first

### 3. Wrong Database URL
**Error**: Connection timeout
**Fix**: Verify `firebase_url` in google-services.json matches console

### 4. Authentication Issue
**Error**: `auth.uid` is null
**Fix**: Ensure user is signed in with Firebase Auth (Google Sign-In)

## Code Changes Made

1. ✅ Added detailed logging to `FirebaseSync.java`
2. ✅ Added Firebase persistence for offline support
3. ✅ Added null checks and error handling
4. ✅ Added logging to ProfileActivity save operations
5. ✅ Verified Firebase Database dependency in build.gradle

## Next Steps

1. Create Realtime Database in Firebase Console
2. Set appropriate rules
3. Run the app and check logs
4. Verify data appears in Firebase Console
