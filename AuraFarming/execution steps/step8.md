# Step 8 - Crop Planning Scene

## Goal
Implement `crop-plan.fxml` with season-based recommendation score.

## Dependencies
- Step 7 completed.

## Build Tasks
1. Inputs: district, farm-plot, crop, season, dates, expected duration.
2. Add recommendation button to compute score.
3. Add crop plan save/update/delete controls.
4. Use enum-based rules for simple advisory:
   - match -> high score.
   - mismatch -> low score.
5. Show recommendation result on left-bottom result area.

## Done Criteria
- Crop plans are saved and linked to plot/farmer correctly.