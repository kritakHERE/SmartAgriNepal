# Step 2 - User Domain and Authentication Backbone

## Goal
Build core user models and file-backed authentication support before any UI scene logic.

## Dependencies
- Step 1 completed.

## Build Tasks
1. Create base `User` class (encapsulation, getters/setters).
2. Create `Farmer`, `Officer`, `Admin` subclasses (inheritance).
3. Create `UserDAO` with CRUD and find-by-email/id.
4. Create `AuthService`:
   - register user.
   - login validation.
   - role lookup.
5. Implement duplicate email prevention and password confirm logic.
6. Add exceptions for invalid login and validation failure.

## Key Files (Planned)
- model: User, Farmer, Officer, Admin.
- dao: UserDAO.
- dto: UserDTO / RegistrationDTO.
- service: AuthService, UserService.

## Manual Tests
- Register farmer/officer succeeds.
- Duplicate email register fails.
- Invalid password login fails.
- Predefined admin login succeeds.

## Done Criteria
- All user auth operations work from service layer without UI.