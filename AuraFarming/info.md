# AuraFarming Info

Generated from source inspection on 2026-03-20.

## 1. System Overview

AuraFarming is the project implementation of the Smart AgriNepal Cooperative Management System described in `ap_question.txt`. It is an offline-first JavaFX desktop application that uses:

- Java + JavaFX/FXML for the UI
- object serialization to local `.dat` files for persistence
- a layered structure: `View -> Controller -> Service -> DAO -> File`

The codebase is organized cleanly by responsibility:

- `com.aurafarming` for app startup classes
- `com.aurafarming.model` for domain objects and enums
- `com.aurafarming.dto` for request payload records
- `com.aurafarming.dao` for file persistence
- `com.aurafarming.service` for business logic
- `com.aurafarming.controller` for JavaFX event handling
- `src/main/resources/com/aurafarming/view` for FXML screens
- `src/main/resources/com/aurafarming/css` for styling

## 2. Launch Flow

There are three important startup classes:

| Class | File path | Role |
|---|---|---|
| `AuraFarmingApplication` | `src/main/java/com/aurafarming/AuraFarmingApplication.java` | Real JavaFX `Application` class. Creates runtime folders, initializes `SceneRouter`, opens `login.fxml`. |
| `AuraFarmingLauncher` | `src/main/java/com/aurafarming/AuraFarmingLauncher.java` | Thin launcher used by the JavaFX Maven plugin. Calls `launch(args)`. |
| `AuraFarmingJarBootstrap` | `src/main/java/com/aurafarming/AuraFarmingJarBootstrap.java` | Plain `main()` entry used by the shaded runnable JAR manifest. Forwards to `AuraFarmingLauncher.main(args)`. |

Startup sequence:

1. Shaded JAR entry goes to `AuraFarmingJarBootstrap.main()`.
2. That forwards to `AuraFarmingLauncher.main()`.
3. JavaFX starts `AuraFarmingApplication`.
4. `AuraFarmingApplication.start(Stage)` runs:
   - `FileUtil.ensureAppDirectories()`
   - `SceneRouter.initialize(stage)`
   - `SceneRouter.goTo("/com/aurafarming/view/login.fxml", "AuraFarming - Login")`

So yes, the real app behavior starts in `AuraFarmingApplication`, while `AuraFarmingLauncher` and `AuraFarmingJarBootstrap` exist to support Maven and packaged JAR launching.

## 3. Package Structure

```text
AuraFarming/
+- pom.xml
+- ap_question.txt
+- src/
|  +- main/java/
|  |  +- module-info.java
|  |  \- com/aurafarming/
|  |     +- AuraFarmingApplication.java
|  |     +- AuraFarmingLauncher.java
|  |     +- AuraFarmingJarBootstrap.java
|  |     +- controller/
|  |     +- dao/
|  |     +- dto/
|  |     +- exception/
|  |     +- model/
|  |     +- service/
|  |     \- util/
|  \- main/resources/com/aurafarming/
|     +- view/
|     \- css/
+- data storage/
+- target/
\- mvnw.cmd
```

Observed file counts:

- `model`: 16 files
- `controller`: 12 files
- `service`: 11 files
- `dao`: 9 files
- `view`: 12 FXML files

## 4. Main Packages and What They Do

| Package | Purpose | Main files |
|---|---|---|
| `com.aurafarming` | App startup and bootstrap | `AuraFarmingApplication`, `AuraFarmingLauncher`, `AuraFarmingJarBootstrap` |
| `com.aurafarming.model` | Domain entities and enums used across the whole system | `User`, `Farmer`, `Farm`, `CropPlan`, `MarketPrice`, `WeatherAlert`, `YieldLog`, `AuditLog`, enums |
| `com.aurafarming.dto` | Small immutable input carriers from UI to services | `LoginDTO`, `RegistrationDTO`, `ExportRequestDTO` |
| `com.aurafarming.dao` | File-based persistence layer | `BaseObjectDAO` and entity-specific DAOs |
| `com.aurafarming.service` | Business rules, validation, role filtering, aggregation | `AuthService`, `UserService`, `FarmPlotService`, `CropPlanService`, `ExportService`, etc. |
| `com.aurafarming.controller` | JavaFX event handlers for each screen | `LoginController`, `DashboardController`, `FarmPlotController`, etc. |
| `com.aurafarming.util` | Shared helpers for IDs, paths, scene switching, alerts, constants | `FileUtil`, `SceneRouter`, `IdGenerator`, `Constants`, `AlertUtil` |
| `com.aurafarming.exception` | Custom runtime exceptions | `AuthenticationException`, `ValidationException` |

## 5. Assignment Modules From `ap_question.txt` Mapped to Code

This is the most direct mapping from the assignment modules to implementation.

| Assignment module | Main models | Main service/controller/DAO classes | Data file(s) | Status |
|---|---|---|---|---|
| Module 1: User Management | `User`, `Admin`, `Officer`, `Farmer`, `Role` | `AuthService`, `UserService`, `LoginController`, `RegistrationController`, `UserDAO` | `users.dat` | Implemented |
| Module 2: Farmer Profile Management | `Farmer`, `User` | `UserService`, `FarmerController`, `UserDAO`, `AuditService` | `users.dat`, `audit_logs.dat` | Implemented as search + activate/deactivate/reactivate |
| Module 3: Farm and Plot Management | `Farm`, `Plot`, `Farmer`, `District` | `FarmPlotService`, `FarmPlotController`, `FarmDAO`, `PlotDAO` | `farms.dat`, `plots.dat` | Implemented |
| Module 4: Crop Planning and Advisory | `CropPlan`, `Season`, `Plot`, `Farmer` | `CropPlanService`, `CropPlanController`, `CropPlanDAO` | `crop_plans.dat` | Implemented |
| Module 5: Market Price Management | `MarketPrice`, `District` | `MarketPriceService`, `MarketPriceController`, `MarketPriceDAO` | `market_prices.dat` | Implemented |
| Module 6: Weather Risk Alerts | `WeatherAlert`, `WeatherCondition`, `Severity`, `District` | `WeatherAlertService`, `WeatherAlertController`, `WeatherAlertDAO` | `weather_alerts.dat` | Implemented |
| Module 7: Reporting and Export | `CropPlan`, `MarketPrice`, `YieldLog`, `AuditLog`, `Farm`, `Plot` | `ExportService`, `ExportController` | writes CSV files to `export results/` | Implemented |
| Module 8: Backup, Restore, and Audit Logging | `AuditLog` | `AuditService`, `AuditLogController`, `AuditLogDAO` | `audit_logs.dat` | Audit logging implemented, dedicated backup/restore workflow not found |

Important note:

- `ap_question.txt` asks for backup/restore, but the current codebase clearly implements audit logging only. There is no dedicated backup/restore service/controller class in `src/main/java`.

## 6. Domain Model Implementation Map

All entity models are in `src/main/java/com/aurafarming/model`. Most data entities implement `Serializable` so lists of them can be written to `.dat` files by the DAO layer.

### 6.1 User and Role Models

| Class | File path | What the class does | Contribution to assignment modules |
|---|---|---|---|
| `User` | `src/main/java/com/aurafarming/model/User.java` | Abstract base user class. Stores `userId`, `fullName`, `email`, `password`, `role`, `active`, `createdAt`, `lastLoginAt`. | Foundation for Module 1 and Module 2. Also supports audit attribution and dashboard metrics. |
| `Admin` | `src/main/java/com/aurafarming/model/Admin.java` | Extends `User`. Adds `privilegeLevel`. Used for the seeded system admin account. | Module 1 access control and overall system administration. |
| `Officer` | `src/main/java/com/aurafarming/model/Officer.java` | Extends `User`. Adds `assignedDistricts`. | Module 1 role handling and operational actions across Modules 5, 6, 7, 8. |
| `Farmer` | `src/main/java/com/aurafarming/model/Farmer.java` | Extends `User`. Adds `district`, `phone`, and `farmIds`. | Core user type for Modules 2, 3, 4, 7. Represents the farm owner/operator. |
| `Role` | `src/main/java/com/aurafarming/model/Role.java` | Enum with `ADMIN`, `OFFICER`, `FARMER`. | Used everywhere for role-based visibility, restrictions, and audit labeling. |
| `District` | `src/main/java/com/aurafarming/model/District.java` | Enum with `CHITWAN`, `DANG`, `RUPANDEHI`. | Shared geography across farmer, farm, market, weather, yield, and dashboard modules. |

### 6.2 Farm, Plot, and Planning Models

| Class | File path | What the class does | Contribution to assignment modules |
|---|---|---|---|
| `Farm` | `src/main/java/com/aurafarming/model/Farm.java` | Represents a farm owned by a farmer. Stores `farmId`, `farmerId`, `district`, `farmTag`, `measurementUnit`, `totalArea`. | Main entity for Module 3. Also feeds Module 4 crop planning and Module 7 export. |
| `Plot` | `src/main/java/com/aurafarming/model/Plot.java` | Represents a plot under a farm. Stores `plotId`, `farmId`, `plotCode`, `area`. | Module 3 child entity. Also needed by crop plans and yield logs. |
| `CropPlan` | `src/main/java/com/aurafarming/model/CropPlan.java` | Stores a crop plan for one plot: `planId`, `farmerId`, `plotId`, `cropType`, `season`, `recommendationScore`, `startDate`, `expectedHarvestDate`. | Core entity for Module 4 and export/reporting in Module 7. |
| `Season` | `src/main/java/com/aurafarming/model/Season.java` | Enum with display names like `Basant (Spring)` and `Barkha (Monsoon)`. | Used by crop advisory logic in Module 4. |

### 6.3 Market, Weather, Yield, and Audit Models

| Class | File path | What the class does | Contribution to assignment modules |
|---|---|---|---|
| `MarketPrice` | `src/main/java/com/aurafarming/model/MarketPrice.java` | Stores district-wise crop price history with `priceId`, `district`, `cropType`, `pricePerKg`, `date`. | Main entity for Module 5 and market exports in Module 7. |
| `WeatherAlert` | `src/main/java/com/aurafarming/model/WeatherAlert.java` | Stores manual weather risk records with `alertId`, `district`, `condition`, `severity`, `durationDays`, `probabilityPercent`, `createdDate`. | Main entity for Module 6. |
| `WeatherCondition` | `src/main/java/com/aurafarming/model/WeatherCondition.java` | Enum for alert types: `RAIN`, `STORM`, `HAIL`, `FROST`, `HEATWAVE`, `FLOOD_RISK`. | Shared vocabulary for Module 6. |
| `Severity` | `src/main/java/com/aurafarming/model/Severity.java` | Enum for `LOW`, `MEDIUM`, `HIGH`, `CRITICAL`. | Risk classification for Module 6. |
| `YieldLog` | `src/main/java/com/aurafarming/model/YieldLog.java` | Stores harvest records: `yieldId`, `farmerId`, `plotId`, `district`, `cropType`, `estimatedKg`, `actualKg`, `harvestDate`. | Supports operational yield tracking and dashboard calculations. Useful for reporting even though not named as a separate module in `ap_question.txt`. |
| `AuditLog` | `src/main/java/com/aurafarming/model/AuditLog.java` | Stores action trace entries: `logId`, `actorId`, `actorRole`, `action`, `targetType`, `targetId`, `timestamp`, `details`. | Core entity for Module 8 audit logging and audit exports. |

## 7. How Those Models Are Actually Used

The model classes are mostly data holders. Their behavior is provided by services and controllers around them:

- `User` hierarchy is used by `AuthService`, `UserService`, `DashboardService`, `AuditService`, and `SessionContext`.
- `Farm` and `Plot` are created/loaded by `FarmPlotService` and shown in `FarmPlotController`.
- `CropPlan` is scored and saved by `CropPlanService`, then driven by `CropPlanController`.
- `MarketPrice` is written, queried, and trend-analyzed by `MarketPriceService`.
- `WeatherAlert` is written and sorted by `WeatherAlertService`.
- `YieldLog` is saved and aggregated by `YieldLogService` and `DashboardService`.
- `AuditLog` is appended by almost every service through `AuditService`.

In other words:

- `model` classes define the data shape
- `service` classes define the business meaning
- `dao` classes define file persistence
- `controller` classes define how the UI triggers the logic

## 8. DAO Layer and Data Files

The DAO layer is built around `BaseObjectDAO<T extends Serializable>`.

| DAO class | File path | Model persisted | File used |
|---|---|---|---|
| `BaseObjectDAO` | `src/main/java/com/aurafarming/dao/BaseObjectDAO.java` | Generic base | Generic read/write support |
| `UserDAO` | `src/main/java/com/aurafarming/dao/UserDAO.java` | `User` list | `users.dat` |
| `FarmDAO` | `src/main/java/com/aurafarming/dao/FarmDAO.java` | `Farm` list | `farms.dat` |
| `PlotDAO` | `src/main/java/com/aurafarming/dao/PlotDAO.java` | `Plot` list | `plots.dat` |
| `CropPlanDAO` | `src/main/java/com/aurafarming/dao/CropPlanDAO.java` | `CropPlan` list | `crop_plans.dat` |
| `MarketPriceDAO` | `src/main/java/com/aurafarming/dao/MarketPriceDAO.java` | `MarketPrice` list | `market_prices.dat` |
| `WeatherAlertDAO` | `src/main/java/com/aurafarming/dao/WeatherAlertDAO.java` | `WeatherAlert` list | `weather_alerts.dat` |
| `YieldLogDAO` | `src/main/java/com/aurafarming/dao/YieldLogDAO.java` | `YieldLog` list | `yield_logs.dat` |
| `AuditLogDAO` | `src/main/java/com/aurafarming/dao/AuditLogDAO.java` | `AuditLog` list | `audit_logs.dat` |

Core behavior of `BaseObjectDAO`:

- reads a whole serialized `List<T>` from a file
- returns an empty list if the file is empty
- rewrites the full list on save

This is how the assignment's "file-based persistence only" rule is implemented.

## 9. Service Layer and Core Responsibilities

| Service | File path | Core functionality |
|---|---|---|
| `AuthService` | `src/main/java/com/aurafarming/service/AuthService.java` | Admin seed creation, registration, login, logout, user validation |
| `UserService` | `src/main/java/com/aurafarming/service/UserService.java` | Search users by role, update user, deactivate/reactivate |
| `FarmPlotService` | `src/main/java/com/aurafarming/service/FarmPlotService.java` | Farm creation, plot creation, role-based farm filtering, delete farm with plot cascade |
| `CropPlanService` | `src/main/java/com/aurafarming/service/CropPlanService.java` | Rule-based crop recommendation scoring and plan save |
| `MarketPriceService` | `src/main/java/com/aurafarming/service/MarketPriceService.java` | Save prices, filter history, compute up/down/stable trend hint |
| `WeatherAlertService` | `src/main/java/com/aurafarming/service/WeatherAlertService.java` | Save alerts and return them sorted by created date |
| `YieldLogService` | `src/main/java/com/aurafarming/service/YieldLogService.java` | Save yield logs, role-based visibility, seasonal/year totals |
| `AuditService` | `src/main/java/com/aurafarming/service/AuditService.java` | Append audit entries and filter them by user/action/date/role |
| `DashboardService` | `src/main/java/com/aurafarming/service/DashboardService.java` | Dashboard counts, active user metrics, chart aggregates |
| `ExportService` | `src/main/java/com/aurafarming/service/ExportService.java` | Preview/export CSV reports with role restrictions |
| `SessionContext` | `src/main/java/com/aurafarming/service/SessionContext.java` | In-memory current user holder |

## 10. Controller and Screen Wiring

FXML screens map directly to controller classes:

| FXML view | Controller | What it handles |
|---|---|---|
| `login.fxml` | `LoginController` | Login flow and switch to registration |
| `registration.fxml` | `RegistrationController` | Farmer/officer registration |
| `dashboard.fxml` | `DashboardController` | Main dashboard, module tab switching, charts, logout |
| `officers.fxml` | `OfficerController` | Officer listing/search/deactivate |
| `farmers.fxml` | `FarmerController` | Farmer listing/search/activate/deactivate |
| `farm-plots.fxml` | `FarmPlotController` | Farm and plot creation/listing/delete |
| `crop-plan.fxml` | `CropPlanController` | Crop recommendation and save plan |
| `market-price.fxml` | `MarketPriceController` | Save price, view history, show trend |
| `weather-alert.fxml` | `WeatherAlertController` | Save weather alert and list all alerts |
| `yield-log.fxml` | `YieldLogController` | Save yield record and display visible logs |
| `audit-log.fxml` | `AuditLogController` | Filter and show audit logs |
| `export.fxml` | `ExportController` | Preview and export CSV reports |

`DashboardController` is the main shell after login. It loads module screens dynamically into `moduleContainer` with `FXMLLoader`.

## 11. Utility and Support Classes

| Class | File path | Why it matters |
|---|---|---|
| `Constants` | `src/main/java/com/aurafarming/util/Constants.java` | Central file names, admin seed credentials, date formatters |
| `FileUtil` | `src/main/java/com/aurafarming/util/FileUtil.java` | Creates data/export folders and resolves runtime paths |
| `IdGenerator` | `src/main/java/com/aurafarming/util/IdGenerator.java` | Generates prefixed IDs like `ADM-`, `OFF-`, `FRM-`, `PLT-`, `PLN-`, `LOG-` |
| `SceneRouter` | `src/main/java/com/aurafarming/util/SceneRouter.java` | Global scene switching and CSS loading |
| `AlertUtil` | `src/main/java/com/aurafarming/util/AlertUtil.java` | Standard information/error dialogs |
| `AuthenticationException` | `src/main/java/com/aurafarming/exception/AuthenticationException.java` | Signals login/auth problems |
| `ValidationException` | `src/main/java/com/aurafarming/exception/ValidationException.java` | Signals invalid input or invalid business actions |

## 12. Persistence and Runtime Folder Behavior

`Constants.java` defines:

- `DATA_DIR = Path.of("data storage")`
- `EXPORT_DIR = Path.of("export results")`

This means the actual data/output location is relative to the process working directory.

That explains why this repository currently shows both:

- `AuraFarming/data storage` and `AuraFarming/export results`
- top-level `data storage` and `export results`

If the app is started from different working directories, it creates those folders there.

Recommended practice:

- run the app from the `AuraFarming` project root so all runtime files stay under `AuraFarming/data storage` and `AuraFarming/export results`

## 13. Version Details

Confirmed from `pom.xml`, `module-info.java`, and local environment:

| Item | Value |
|---|---|
| Group ID | `com.aurafarming` |
| Artifact ID | `AuraFarming` |
| Project version | `1.0-SNAPSHOT` |
| Java source/target release | `21` |
| JavaFX controls | `21.0.6` |
| JavaFX FXML | `21.0.6` |
| JUnit | `5.12.1` |
| Maven compiler plugin | `3.13.0` |
| Maven shade plugin | `3.6.0` |
| JavaFX Maven plugin | `0.0.8` |
| Java module name | `com.aurafarming` |
| Shaded JAR main class | `com.aurafarming.AuraFarmingJarBootstrap` |
| JavaFX Maven run main class | `com.aurafarming/com.aurafarming.AuraFarmingLauncher` |
| Maven wrapper distribution | Apache Maven `3.8.5` |

Packaged artifacts found in `target/`:

- `target/AuraFarming-1.0-SNAPSHOT.jar`
- `target/AuraFarming-1.0-SNAPSHOT-runnable.jar`

## 14. Observed System Info

From the inspected machine/environment:

| Item | Value |
|---|---|
| OS | `Microsoft Windows [Version 10.0.26200.8037]` |
| Shell used for inspection | PowerShell |
| Java runtime found | `21.0.10` LTS |
| Java command path | `C:\Program Files\Common Files\Oracle\Java\javapath\java.exe` |
| Actual JDK folder found | `C:\Program Files\Java\jdk-21.0.10` |
| Workspace root inspected | `C:\Users\asus\OneDrive\OneDrive - MSFT\Desktop\AP - Smart agriFarming\AuraFarming` |

Important environment note:

- `java -version` works
- `mvnw.cmd` requires `JAVA_HOME` to be set

## 15. How To Run the App

### Option A: Run from IDE

Use either of these classes:

- `com.aurafarming.AuraFarmingApplication`
- `com.aurafarming.AuraFarmingLauncher`

`AuraFarmingApplication` is the real JavaFX app class. `AuraFarmingLauncher` is just a thin launcher wrapper.

### Option B: Run with Maven Wrapper

From the `AuraFarming` folder:

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-21.0.10'
.\mvnw.cmd javafx:run
```

Why `JAVA_HOME` matters:

- in the inspected environment, `mvnw.cmd` failed until `JAVA_HOME` was set

### Option C: Run the Packaged Runnable JAR

From the `AuraFarming` folder:

```powershell
java -jar .\target\AuraFarming-1.0-SNAPSHOT-runnable.jar
```

This JAR is configured to start from `AuraFarmingJarBootstrap`.

## 16. Default Credentials and Deployment Notes

Default seeded admin account:

- Email: `admin@control.com`
- Password: `admin123`

Deployment notes:

1. Launch from the `AuraFarming` directory, not the repository root, if you want data files to stay inside the project folder.
2. The first Maven wrapper run may need to download Maven 3.8.5 if it is not already cached.
3. Local data is stored in `.dat` files, not in a database.
4. Exported reports are CSV files created in `export results/`.

## 17. What Is Fully Implemented vs. Partial

Clearly implemented:

- authentication and seeded admin
- role-based dashboard
- farmer/officer/farm/plot/crop-plan/market/weather/yield/audit/export modules
- file-based DAO persistence
- JavaFX screen navigation

Partially implemented or not found as dedicated code:

- backup/restore workflow not found
- dedicated plot update/delete workflow not found
- farmer profile management is mainly search and status toggling, not a full profile editor UI
- weather alerts are manually created in-app, not imported from an external weather file

## 18. Short Architecture Summary

If someone asks "how does the whole system work?" the shortest correct answer is:

1. `AuraFarmingApplication` starts JavaFX and opens the login screen.
2. Controllers receive form actions from FXML screens.
3. Services apply rules, role checks, and business logic.
4. DAOs serialize model lists to `.dat` files in local storage.
5. `AuditService` records important actions.
6. `DashboardController` acts as the post-login shell and loads the rest of the modules.
