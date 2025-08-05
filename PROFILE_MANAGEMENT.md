# Profile Management Features

This document describes the profile management functionality that has been added to the frontend application to work with your Spring Boot backend.

## Overview

The profile management system allows users to:
- View their profile information
- Update their profile details (username, email, password)
- Enable/disable their account
- Delete their account (soft delete)

## API Endpoints

The frontend communicates with the following backend endpoints:

### Get Profile
- **URL**: `GET /api/users/me`
- **Headers**: `Authorization: Bearer {token}`
- **Response**: User profile data

### Update Profile
- **URL**: `PUT /api/users/me`
- **Headers**: `Authorization: Bearer {token}`
- **Body**: `{ "username": "...", "email": "...", "password": "..." }`
- **Response**: Success message

### Delete Account
- **URL**: `DELETE /api/users/me`
- **Headers**: `Authorization: Bearer {token}`
- **Response**: Success message

### Disable Account
- **URL**: `PUT /api/users/me/disable`
- **Headers**: `Authorization: Bearer {token}`
- **Response**: Success message

### Enable Account
- **URL**: `PUT /api/users/me/enable`
- **Headers**: `Authorization: Bearer {token}`
- **Response**: Success message

## Frontend Components

### 1. API Functions (`src/api/auth.js`)

Added the following functions to handle profile management:

```javascript
// Get user profile
api.getProfile(token)

// Update user profile
api.updateProfile(updateData, token)

// Delete user account
api.deleteAccount(token)

// Disable user account
api.disableAccount(token)

// Enable user account
api.enableAccount(token)
```

### 2. Profile Page (`src/pages/ProfilePage.jsx`)

A comprehensive profile management page that includes:
- Profile information display
- Edit mode for updating profile
- Account status management (enable/disable)
- Account deletion with confirmation
- Real-time notifications
- Loading states

### 3. Navigation Integration

- Added `/profile` route to `App.js`
- Added "Profile Settings" button in `DashboardPage.jsx` that navigates to the profile page

## Features

### Profile Viewing
- Displays username, email, account status, and creation date
- Shows account status with color-coded badges (Active/Disabled)

### Profile Editing
- Toggle between view and edit modes
- Form validation for required fields
- Only sends changed fields to the backend
- Password field is optional (leave blank to keep current password)

### Account Management
- **Enable/Disable**: Toggle account status with confirmation
- **Delete Account**: Soft delete with confirmation dialog
- **Status Indicators**: Visual feedback for account status

### User Experience
- **Loading States**: Shows spinners during API calls
- **Notifications**: Success/error messages with auto-dismiss
- **Responsive Design**: Works on desktop and mobile
- **Confirmation Dialogs**: Prevents accidental account changes

## Usage

1. **Access Profile Page**: Click "Profile Settings" in the dashboard
2. **View Profile**: See current profile information
3. **Edit Profile**: Click "Edit Profile" button
4. **Update Information**: Modify username, email, or password
5. **Save Changes**: Click "Save Changes" to update
6. **Account Actions**: Use enable/disable/delete buttons as needed

## Error Handling

The system includes comprehensive error handling:
- Network errors
- API validation errors
- Authentication errors
- User-friendly error messages

## Security Features

- Token-based authentication for all requests
- Confirmation dialogs for destructive actions
- Input validation and sanitization
- Secure password handling

## Styling

The profile page uses the existing design system:
- Glass morphism effects
- Consistent color scheme
- Responsive grid layout
- Modern button styles
- Smooth animations

## Testing

To test the profile management features:

1. Start your Spring Boot backend on `localhost:8080`
2. Start the React frontend
3. Log in with valid credentials
4. Navigate to Profile Settings
5. Test each feature:
   - View profile information
   - Edit and save changes
   - Enable/disable account
   - Delete account (be careful!)

## Notes

- All profile operations require a valid JWT token
- Account deletion is a soft delete (can be restored by admin)
- Password updates are optional
- The system automatically refreshes profile data after updates 