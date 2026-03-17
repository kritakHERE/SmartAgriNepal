# Step 1 - Foundation Setup

## Goal
Set up package hierarchy, module configuration, shared constants/enums, file utility, and predefined admin seed strategy.

## Why First
All later models/scenes depend on package structure, enums, and shared file paths.

## Build Tasks
1. Standardize root package to `aurafarming`.
2. Prepare folder/package skeleton:
   - model, dao, dto, service, controller, view, util, exception.
3. Define enums:
   - Role, District, Season, WeatherCondition, Severity.
4. Define constants:
   - default admin email/password, file names, date formats.
5. Create file utility for safe file create/read/write checks.
6. Configure module exports/opens for JavaFX controllers.

## Key Files (Planned)
- `src/main/java/.../module-info.java`
- `src/main/java/.../util/Constants.java`
- `src/main/java/.../util/FileUtil.java`
- enum files in model package.

## Validation Checklist
- Project builds with empty skeleton.
- Data directories are created automatically if missing.
- Admin seed constants available to AuthService.

## Done Criteria
- Common dependencies compile and are import-ready.