# Step 13 - Export Scene

## Goal
Implement export controls for reports and histories.

## Dependencies
- Steps 9, 11, 12 completed.

## Build Tasks
1. Create `export.fxml` and `ExportController`.
2. Add export choices:
   - audit logs.
   - yield logs.
   - market price history.
   - crop plan summary.
3. Add filter controls:
   - date range.
   - district/farmer/role where applicable.
4. Generate text/CSV files in `export results` folder.
5. Enforce role restrictions on exportable data.

## Done Criteria
- Filtered exports are generated with proper naming and location.