# Step 5 - Dashboard Scene and Metrics

## Goal
Build dashboard cards and top-tab navigation shell.

## Dependencies
- Step 4 completed.

## Build Tasks
1. Create `dashboard.fxml` with card-style summary boxes:
   - total farmers.
   - total officers.
   - total yield this season.
   - total yield this year.
   - active users in last 24h.
2. Add top navigation tabs and active-tab highlight.
3. Create `DashboardController` and `DashboardService`.
4. Enforce role visibility (hide restricted tabs).

## Validation Checklist
- Card metrics update from data files.
- Tab highlight updates on scene switch.
- Restricted tabs hidden/disabled by role.

## Done Criteria
- Dashboard is the central role-aware entry scene.