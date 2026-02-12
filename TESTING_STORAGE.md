# Testing Firebase Storage Upload

## Issue
You're seeing `content://` URIs instead of Firebase Storage URLs.

## Possible Causes
1. **Old documents** - Documents uploaded before Firebase Storage integration
2. **Upload not completing** - Check if progress dialog shows and completes
3. **Storage rules** - Permission denied errors

## Testing Steps

### 1. Clear Old Documents (Optional)
Old documents have local URIs. To test fresh:
```bash
# Clear app data
adb shell pm clear com.example.chroniccare
```
Then login again.

### 2. Upload New Document
1. Open app → Profile
2. Click "Upload Document"
3. Select document type
4. Choose a file
5. **Watch for progress dialog** (0-100%)
6. Wait for "uploaded to cloud successfully" message

### 3. Check Logs
Run this while uploading:
```bash
adb logcat | grep -E "ProfileActivity|FirebaseStorageHelper"
```

**Expected logs:**
```
ProfileActivity: Starting upload for URI: content://...
ProfileActivity: File name: userid_20260212_114500.pdf
FirebaseStorageHelper: Uploading file to: users/userid/documents/...
ProfileActivity: Upload progress: 25%
ProfileActivity: Upload progress: 50%
ProfileActivity: Upload progress: 75%
ProfileActivity: Upload progress: 100%
FirebaseStorageHelper: ✅ File uploaded successfully: https://firebasestorage.googleapis.com/...
ProfileActivity: ✅ Upload successful! Download URL: https://...
ProfileActivity: Saving to local DB with URL: https://...
ProfileActivity: ✅ Saved to local database
ProfileActivity: ✅ Synced to Firestore
```

**If you see errors:**
```
❌ Upload failed: Permission denied
❌ Upload failed: Storage bucket not configured
❌ Upload failed: Network error
```

### 4. Verify in Firebase Console

**Storage:**
1. Go to: https://console.firebase.google.com/project/chroniccare-287b7/storage
2. Navigate to: `users/{userId}/documents/`
3. You should see your uploaded file
4. Click on it to see the download URL

**Firestore:**
1. Go to: https://console.firebase.google.com/project/chroniccare-287b7/firestore
2. Navigate to: `users/{userId}/documents/`
3. Click on a document
4. Check `downloadUrl` field - should start with `https://firebasestorage.googleapis.com/`

### 5. Check in App
1. Go to Profile → View Documents
2. Click on the newly uploaded document
3. It should open in browser with Firebase Storage URL

## Troubleshooting

### Problem: Still seeing `content://` URIs
**Solution:** These are old documents. Upload a NEW document to test.

### Problem: Progress dialog doesn't show
**Solution:** Upload function not being called. Check button click listener.

### Problem: Upload fails immediately
**Check:**
1. Internet connection
2. Firebase Storage is enabled in console
3. Storage rules allow write access

### Problem: Permission Denied
**Solution:** Update Storage Rules:
```
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /{allPaths=**} {
      allow read, write: if true;  // For testing only
    }
  }
}
```

### Problem: Network Error
**Check:**
1. Device has internet
2. Firebase project is active
3. Billing is enabled (if required)

## Quick Test Command

Run this in terminal while uploading:
```bash
adb logcat -c && adb logcat | grep -E "ProfileActivity|FirebaseStorageHelper|FirebaseSync"
```

This will:
1. Clear old logs
2. Show only relevant logs
3. Help you see exactly what's happening

## Expected Result

After uploading a NEW document, you should see:
- ✅ Progress dialog (0-100%)
- ✅ Success message
- ✅ File in Firebase Storage console
- ✅ Metadata in Firestore with `downloadUrl`
- ✅ Document opens from cloud URL when clicked

## Note About Old Documents

Documents uploaded BEFORE Firebase Storage integration will still have `content://` URIs. This is normal. Only NEW uploads will use Firebase Storage.

To migrate old documents, you would need to:
1. Read old documents from local DB
2. Upload each to Firebase Storage
3. Update DB with new URLs

(This can be implemented if needed)

## Build Status
✅ **BUILD SUCCESSFUL**

Try uploading a NEW document and check the logs! 🚀
