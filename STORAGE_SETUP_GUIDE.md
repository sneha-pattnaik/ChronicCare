# Firebase Storage Setup - ChronicCare

## Your Firebase Storage Bucket
```
gs://chroniccare-287b7.firebasestorage.app
```

## ✅ Configuration Status
Your `google-services.json` is already configured with the correct storage bucket. No code changes needed!

## Quick Setup Steps

### 1. Enable Firebase Storage (if not already enabled)
1. Go to: https://console.firebase.google.com/project/chroniccare-287b7/storage
2. If you see "Get Started", click it
3. Choose location (e.g., us-central1)
4. Click "Done"

### 2. Set Storage Rules (for testing)
Go to the **Rules** tab and paste:

```
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /users/{userId}/{allPaths=**} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
  }
}
```

**For quick testing (open access):**
```
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /{allPaths=**} {
      allow read, write: if true;
    }
  }
}
```

⚠️ **Note**: Open rules expire after 30 days and allow anyone to upload/download.

### 3. Test Upload
1. Run the app
2. Go to Profile → Upload Document
3. Select a file
4. Watch the progress bar
5. Check Firebase Console → Storage
6. You should see: `users/{userId}/documents/{filename}`

## File Structure in Storage

```
chroniccare-287b7.firebasestorage.app/
  └── users/
      └── {userId}/
          └── documents/
              ├── userid_20260212_113000.pdf
              ├── userid_20260212_114500.jpg
              └── ...
```

## Firestore Metadata

```
Firestore Database:
  users/{userId}/documents/{docId}
    ├── documentName: "Lab Report"
    ├── documentType: "Lab Report"
    ├── downloadUrl: "https://firebasestorage.googleapis.com/v0/b/chroniccare-287b7.firebasestorage.app/o/users%2F..."
    ├── fileName: "userid_20260212_113000.pdf"
    ├── uploadDate: 1234567890
    └── fileSize: 524288
```

## Verify Everything Works

### Check Upload Success
```bash
adb logcat | grep -E "FirebaseStorageHelper|ProfileActivity"
```

Expected logs:
```
FirebaseStorageHelper: Uploading file to: users/{userId}/documents/{filename}
FirebaseStorageHelper: ✅ File uploaded successfully: https://...
ProfileActivity: ✅ Document synced to Firestore
```

### Check in Firebase Console

**Storage:**
1. Go to Storage tab
2. Navigate to `users/{userId}/documents/`
3. Files should be visible
4. Click file to see download URL

**Firestore:**
1. Go to Firestore Database tab
2. Navigate to `users/{userId}/documents/`
3. Documents should have `downloadUrl` field

## Troubleshooting

### Error: "Permission Denied"
**Solution**: Update Storage Rules to allow authenticated users

### Error: "Storage bucket not configured"
**Solution**: Already configured in your `google-services.json` ✅

### Error: "Upload failed"
**Solution**: 
1. Check internet connection
2. Verify Firebase Storage is enabled
3. Check storage rules

### Files not appearing
**Solution**:
1. Check logcat for errors
2. Verify user is signed in
3. Check Firebase Console for uploaded files

## Storage Quotas (Free Tier)

- **Storage**: 5 GB
- **Downloads**: 1 GB/day
- **Uploads**: 20,000/day

Upgrade to Blaze plan for more.

## Security Best Practices

### Production Rules (use after testing):
```
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /users/{userId}/{allPaths=**} {
      // Only authenticated users can access their own files
      allow read, write: if request.auth != null && request.auth.uid == userId;
      
      // Limit file size to 10MB
      allow write: if request.resource.size < 10 * 1024 * 1024;
      
      // Only allow specific file types
      allow write: if request.resource.contentType.matches('image/.*') 
                   || request.resource.contentType == 'application/pdf';
    }
  }
}
```

## Ready to Test! 🚀

Your Firebase Storage is configured and ready. Just:
1. Enable Storage in Firebase Console
2. Set rules
3. Upload a document in the app
4. Check Firebase Console to verify

Everything is set up correctly! ✅
