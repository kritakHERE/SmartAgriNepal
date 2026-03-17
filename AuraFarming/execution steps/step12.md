# Step 12 - Audit Log Scene

## Goal
Implement audit monitoring and filtering.

## Dependencies
- Prior modules should already emit audit events.

## Build Tasks
1. Create `audit-log.fxml` and controller.
2. Right panel shows audit entries.
3. Left controls for filters:
   - user id/email.
   - action type.
   - date duration From/To.
4. Visibility rules:
   - Admin: full access.
   - Officer: login/yield related limited scope.
   - Farmer: no direct audit module.

## Done Criteria
- Append-only audit entries are queryable by role and filter.