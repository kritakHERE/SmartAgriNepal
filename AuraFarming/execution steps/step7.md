# Step 7 - Farm and Plot Scene

## Goal
Implement `farm-plots.fxml` for farmer-owned farm and plot CRUD.

## Dependencies
- Step 6 completed.

## Build Tasks
1. Add district dropdown and farm create controls.
2. Add measurement unit and quantity fields.
3. Create plot generation and naming convention `Ffn_Ppn`.
4. Enable edit/delete for selected farm/plot.
5. Enforce ownership (farmer can edit own records only).
6. Officer/Admin support for assisted updates.

## Validation Checklist
- Farm creation requires district and quantity.
- Plot code uniqueness per farmer.
- Delete confirmation required.

## Done Criteria
- Farm/plot lifecycle management works with file persistence.