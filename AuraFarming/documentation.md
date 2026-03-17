# AuraFarming System Documentation

## 1. System Definition (Brief)
AuraFarming is an offline-first desktop system for agriculture cooperative management in Nepal context. It manages farmer/officer/admin operations for registration, login, farm and plot handling, crop planning, market price updates, weather alerts, yield tracking, audit logs, and report export.

The system is designed for Java OOP coursework (2nd year level) and strictly uses local file-based persistence (text/binary/object files), with JavaFX + FXML UI and MVC + DAO + DTO patterns.

## 2. System Components

### 2.1 Actors
- Admin (predefined account): full privilege access.
- Officer: manages farmer-related operational modules, cannot manage officers.
- Farmer: self-service operations with restricted read/write permissions.

### 2.2 Architectural Components
- View layer (`.fxml`): screens built in Scene Builder/JavaFX.
- Controller layer: event handling and UI logic.
- Service layer: business rules and validation orchestration.
- DAO layer: file CRUD operations.
- DTO layer: structured data transfer between UI, services, and DAO.
- Model layer: domain objects and enums.
- Data storage: local files in `data storage`.
- Export results: generated text/CSV summaries in `export results`.

## 3. How the System Works
1. User opens application.
2. New users go to Registration; existing users use Login.
3. Authentication validates credentials from local user storage.
4. Role-based Dashboard loads and displays metrics cards.
5. User navigates top tabs (module scenes).
6. Each scene uses left panel controls and right panel result display (except dashboard cards).
7. CRUD operations are executed through Controller -> Service -> DAO -> File.
8. Critical actions append entries to audit log.
9. Export scene generates report files from existing logs/history.

## 4. Component Interaction (System Thinking View)

### 4.1 Interaction Chain
`FXML View -> Controller -> Service -> DAO -> File Storage`

### 4.2 Example: Market Price Save (Officer/Admin)
1. MarketPriceController receives district/crop/price input.
2. Service validates role + input range + duplicate/date rule.
3. MarketPriceDAO writes new record to file.
4. AuditService appends action metadata.
5. View refreshes right panel history list.

### 4.3 Example: Farmer Yield Log
1. Farmer selects farm/plot and enters estimated + actual yield.
2. Service validates ownership and numeric fields.
3. YieldLogDAO saves yield entry.
4. DashboardService recalculates seasonal/year totals.

## 5. Scope and Syllabus Boundaries

### 5.1 In Scope
- Generic types and simple casting.
- Exception handling (try-catch-finally, custom exception classes).
- Enums.
- ArrayList collections.
- File handling (text/object/binary only).
- CRUD operations.
- Encapsulation, inheritance, abstraction, polymorphism.
- JavaFX with FXML and controller classes.
- MVC, DAO, DTO patterns.

### 5.2 Out of Scope
- SQL databases.
- Cloud storage/services.
- Network APIs/web services.
- Advanced ML prediction engines.
- Complex external chart systems beyond JavaFX/basic export.

## 6. Functional Modules

### Module 1: User Management
- Registration for Farmer/Officer.
- Login for all users.
- Admin can view/edit users and change roles.
- Officer can manage farmer credentials only.
- Predefined Admin account: `admin@control.com / admin123`.

### Module 2: Farmer Profile Management
- Search farmer by name/email/id.
- View and update farmer profile.
- Safe delete with confirmation + audit entry.

### Module 3: Farm and Plot Management
- Farmer creates farm under district.
- Farm has multiple plots.
- Plot naming pattern: `Ffn_Ppn`.
- Edit/delete farm and plot with validation.

### Module 4: Crop Planning and Advisory
- Select district, farm-plot, crop, season, expected duration/date.
- Rule-based recommendation score (season-crop match/mismatch).
- Save crop plan history.

### Module 5: Market Price Management
- District + crop + price per kg.
- Current price lookup.
- District comparison.
- History view by date range.
- Farmers have view-only permission.

### Module 6: Weather Risk Alerts
- Condition type, severity, duration days, probability percent.
- Officer/Admin create and update alerts.
- Farmer view-only alert feed.

### Module 7: Reporting and Export
- Export audit logs, yield logs, market history, crop plan summaries.
- Filter by date range and basic criteria.
- Output in text/CSV files to export folder.

### Module 8: Backup/Restore and Audit Logging
- Append-only audit trail (login/logout/create/update/delete/export).
- Basic backup and restore of data files.
- Admin full audit visibility; officer limited view.

## 7. Role-Based Access Matrix
| Feature | Admin | Officer | Farmer |
|---|---|---|---|
| Registration/Login | Yes | Yes | Yes |
| Dashboard | Full | Operational | Personal/limited |
| Officer Management | Yes | No | No |
| Farmer Management | Yes | Yes | Self-only |
| Market Price Edit | Yes | Yes | No |
| Market Price View | Yes | Yes | Yes |
| Weather Alert Edit | Yes | Yes | No |
| Weather Alert View | Yes | Yes | Yes |
| Farm/Plot CRUD | Yes | Assist/manage | Own only |
| Crop Plan CRUD | Yes | Assist/manage | Own only |
| Yield Log View | All | All | Own only |
| Yield Log Edit | Yes | Yes | Own only |
| Audit Log View | Full | Limited | No |
| Export Reports | Full | Limited | Limited/self |
| Change User Role | Yes | No | No |

## 8. Data/Object Model (Domain)

### 8.1 Core Domain Classes
- `User` (abstract/base): userId, fullName, email, password, role, status.
- `Farmer` extends `User`: district, phone, farmIds.
- `Officer` extends `User`: assignedDistricts.
- `Admin` extends `User`: privilegeLevel.
- `Farm`: farmId, farmerId, district, measurementUnit, totalArea.
- `Plot`: plotId, farmId, plotCode, area.
- `CropPlan`: planId, farmerId, plotId, cropType, season, recommendationScore, startDate, expectedHarvestDate.
- `MarketPrice`: priceId, district, cropType, pricePerKg, date.
- `WeatherAlert`: alertId, district, condition, severity, durationDays, probabilityPercent, createdDate.
- `YieldLog`: yieldId, farmerId, plotId, cropType, estimatedKg, actualKg, harvestDate.
- `AuditLog`: logId, actorId, actorRole, action, targetType, targetId, timestamp.

### 8.2 Enums
- `Role {ADMIN, OFFICER, FARMER}`
- `District {CHITWAN, DANG, RUPANDEHI}`
- `Season {SPRING, SUMMER, MONSOON, AUTUMN, WINTER}`
- `WeatherCondition {RAIN, STORM, HAIL, FROST, HEATWAVE, FLOOD_RISK}`
- `Severity {LOW, MEDIUM, HIGH, CRITICAL}`

### 8.3 Key Relationships
- One `Farmer` -> many `Farm`.
- One `Farm` -> many `Plot`.
- One `Plot` -> many `CropPlan` and `YieldLog`.
- Many users create `AuditLog` entries.

## 9. Pattern Mapping (MVC, DAO, DTO)

### 9.1 MVC
- Model: domain classes and enums.
- View: `.fxml` files.
- Controller: JavaFX controller classes for scene events and navigation.

### 9.2 DAO (File-based redesign)
- Each entity has DAO for local file CRUD.
- A shared BaseDAO handles reusable read/write logic.
- Audit log is append-only write style.

### 9.3 DTO
- DTOs pass filtered/validated data between scene forms and services.
- Keeps controller clean and prevents direct entity mutation from UI.

## 10. Package Tree Hierarchy and File Plan

### 10.1 Parent Package
- `aurafarming`

### 10.2 Planned Structure
- `aurafarming/model` (nouns + enums)
- `aurafarming/dao`
- `aurafarming/dto`
- `aurafarming/service`
- `aurafarming/controller`
- `aurafarming/view` (`.fxml` views)
- `aurafarming/data storage` (local files)
- `aurafarming/export results` (output files)

### 10.3 Planned Class Inventory
- Model classes: User, Farmer, Officer, Admin, Farm, Plot, CropPlan, MarketPrice, WeatherAlert, YieldLog, AuditLog.
- DAO classes: BaseDAO, UserDAO, FarmerDAO, OfficerDAO, FarmDAO, PlotDAO, CropPlanDAO, MarketPriceDAO, WeatherAlertDAO, YieldLogDAO, AuditLogDAO.
- DTO classes: UserDTO, FarmerDTO, OfficerDTO, FarmPlotDTO, CropPlanDTO, MarketPriceDTO, WeatherAlertDTO, YieldLogDTO, ExportRequestDTO.
- Service classes: AuthService, UserService, FarmerService, OfficerService, FarmPlotService, CropPlanService, MarketPriceService, WeatherAlertService, YieldLogService, AuditService, DashboardService, ExportService.
- Controller classes: RegistrationController, LoginController, DashboardController, OfficerController, FarmerController, MarketPriceController, WeatherAlertController, AuditLogController, YieldLogController, ExportController, FarmPlotController, CropPlanController.
- View files: registration.fxml, login.fxml, dashboard.fxml, officers.fxml, farmers.fxml, market-price.fxml, weather-alert.fxml, audit-log.fxml, yield-log.fxml, export.fxml, farm-plots.fxml, crop-plan.fxml.

## 11. UI/Scene Design Guidelines

### 11.1 Navigation and Layout
- Top tab-like menu for module navigation with active highlight.
- Dashboard uses metric cards/boxes.
- Other modules use split design: left controls, right results.

### 11.2 Container Preference (Student comfort)
- Primary container: `VBox`.
- Optional brief use:
  - `HBox`: horizontal grouping (example: date range From/To fields).
  - `GridPane`: simple form alignment when fields grow.

### 11.3 Styling Features (Within Scope)
- Use JavaFX CSS only.
- Consistent color theme and spacing.
- Card-style containers using `styleClass`.
- Hover and active tab styling.
- Simple typography consistency.

## 12. Validation and Exception Handling Strategy
- Required field checks before save.
- Email format and password confirmation checks.
- Duplicate user detection by email.
- Role authorization checks before restricted actions.
- Numeric/date range validation for prices and yields.
- File-not-found and parse exceptions handled with friendly alerts.
- Invalid operation logs written to audit for traceability.

## 13. CRUD and Persistence Strategy
- Use ArrayList in memory during scene interaction.
- DAO synchronizes list changes to file.
- CRUD semantics:
  - Create: append new validated object.
  - Read: load/filter records by role and criteria.
  - Update: find by id then rewrite file.
  - Delete: soft delete preferred for user-related records (status flag), hard delete for temporary/non-sensitive records if justified.

## 14. Rubric and Deliverable Alignment

### Part A Alignment
- Work delegation and modules section included.
- OO concepts and pattern mapping included.
- Data attributes and methods baseline provided.
- Package hierarchy prepared for UML conversion.
- Static prototype flow and scene structure documented.

### Part B Alignment
- Core functionality roadmap scene-by-scene.
- DAO/file persistence clearly planned.
- UI aesthetics through JavaFX CSS.
- Creativity through safe, relevant enhancements.
- File organization and naming structure defined.

## 15. Additional Creative Features (Safe, Non-Advanced)

### 15.1 Feature Ideas
- Crop-season recommendation badge (Good/Moderate/Poor) from enum-based rules.
- Price trend hint (up/down vs previous saved price in same district-crop).
- Dashboard quick insights (today alerts count, top crop by entries).
- Friendly empty-state labels in result panel.

### 15.2 Implementation Strategy
- Keep all logic rule-based in services.
- Reuse existing DAO reads; no external libraries needed.
- Display results using labels/tables/cards in FXML.

### 15.3 Planned Step for Creative Features
- Dedicated phase in execution steps after core modules are stable.
- Add-on only after all required CRUD modules pass manual testing.

## 16. Bottom-Up Development Principle
Build from dependencies upward:
1. Enums/constants/utils ->
2. Base model (`User`) and core models ->
3. DAO base and DAOs ->
4. DTOs ->
5. Services ->
6. Controllers ->
7. FXML scenes ->
8. Integration, validation, export, polish.

This prevents scene/controller code from depending on incomplete models/DAO classes.
