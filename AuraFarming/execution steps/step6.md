# Step 6 - Officer and Farmer Management Scenes

## Goal
Implement searchable user management for officers and farmers with role restrictions.

## Dependencies
- Steps 2, 5 completed.

## Build Tasks
1. Build `officers.fxml` + `OfficerController`.
2. Build `farmers.fxml` + `FarmerController`.
3. Add search fields by id/name/email.
4. Add update flow with confirmation popup.
5. Hide passwords on right-side listing.
6. Restrict permissions:
   - Admin: manage officers + farmers.
   - Officer: manage farmers only.
   - Farmer: self-edit only where allowed.
7. Log updates/deletes in audit file.

## Done Criteria
- Role-safe user management scenes are functional.