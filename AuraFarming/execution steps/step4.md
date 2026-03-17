# Step 4 - Login Scene and Role Routing

## Goal
Implement `login.fxml` and role-based redirection to dashboard.

## Dependencies
- Step 3 completed.

## Build Tasks
1. Create `login.fxml` with email/password.
2. Create `LoginController`.
3. Integrate AuthService login.
4. Route to dashboard with role context.
5. Add audit entries for login success/failure.

## Validation Checklist
- Wrong credentials show user-friendly message.
- Admin lands in full dashboard.
- Officer/Farmer land in restricted dashboard.

## Done Criteria
- Login works and role session context is available.