# Step 3 - Registration Scene Package

## Goal
Implement first scene: `registration.fxml` with full controller-to-service integration.

## Dependencies
- Step 2 completed (AuthService ready).

## Build Tasks
1. Create `registration.fxml` form fields:
   - full name, email, password, confirm password, role dropdown.
2. Add buttons:
   - Register, Login Instead.
3. Create `RegistrationController`:
   - validate fields.
   - call AuthService register.
   - show success/error alerts.
4. Route `Login Instead` to login scene.
5. Ensure admin role is not selectable here.

## Validation Checklist
- Empty form cannot submit.
- Password mismatch blocked.
- Role limited to Farmer/Officer.
- Successful register writes user file.

## Done Criteria
- Registration scene fully functional and navigable.