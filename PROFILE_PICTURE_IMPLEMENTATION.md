# Profile Picture Consistency Implementation

## Summary
Implemented a consistent profile picture loading system across all activities in ChronicCare app. The profile picture now loads the user's Google profile photo by default and persists across all activities when navigating.

## Changes Made

### 1. Created ProfileImageHelper Utility Class
**File:** `app/src/main/java/com/example/chroniccare/utils/ProfileImageHelper.java`

- Centralized profile image loading logic
- Handles both custom uploaded photos and Google profile photos
- Automatically falls back to default profile icon if no photo is available
- Uses Picasso for efficient image loading with caching

### 2. Updated Activities

#### HomeActivity
- Replaced `loadUserPhoto()` method with `ProfileImageHelper.loadProfileImage()`
- Added profile image reload in `onResume()` to ensure consistency when returning to activity
- Profile image click navigates to ProfileActivity

#### DrGPTActivity
- Added profile image initialization in `onCreate()`
- Added profile image reload in `onResume()`
- Profile image click navigates to ProfileActivity

#### MonitorActivity
- Added profile image initialization in `onCreate()`
- Added profile image reload in `onResume()`
- Profile image click navigates to ProfileActivity

#### FitHubActivity
- Added profile image initialization in `onCreate()`
- Added profile image reload in `onResume()`
- Profile image click navigates to ProfileActivity

#### MedicationsActivity
- Added profile image to layout
- Added profile image initialization in `onCreate()`
- Added profile image reload in `onResume()`
- Profile image click navigates to ProfileActivity

#### ProfileActivity
- Updated to use `ProfileImageHelper` for consistency
- Maintains existing functionality for changing profile photo

### 3. Updated Layout
**File:** `app/src/main/res/layout/activity_medications.xml`

- Added profile image to the header section
- Positioned at top-right corner consistent with other activities
- 60dp x 60dp size with circular border

## Key Features

1. **Consistency**: All activities now use the same profile loading logic
2. **Persistence**: Profile picture doesn't disappear when navigating between activities
3. **Priority Order**:
   - Custom uploaded photo (from SharedPreferences)
   - Google account profile photo
   - Default profile icon
4. **Automatic Refresh**: Profile image reloads in `onResume()` to reflect any changes
5. **Click Navigation**: Clicking profile image in any activity navigates to ProfileActivity

## Technical Details

- Uses SharedPreferences to store custom photo URI
- Uses Picasso library for image loading with placeholder and error handling
- Supports both HTTP URLs (Google photos) and local URIs (custom uploads)
- Implements proper error handling and fallback mechanisms
