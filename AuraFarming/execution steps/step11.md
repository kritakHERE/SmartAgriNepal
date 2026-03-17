# Step 11 - Yield Log Scene

## Goal
Implement `yield-log.fxml` with estimated vs actual harvest tracking.

## Dependencies
- Step 8 completed.

## Build Tasks
1. Inputs: farmer, district, farm-plot, crop, estimated kg, actual kg, date.
2. Role-based list behavior:
   - Farmer: own logs only.
   - Officer/Admin: all logs.
3. Add search filters (district, farmer, date range).
4. Update dashboard totals from yield data.

## Done Criteria
- Yield logs support CRUD and role-based visibility correctly.