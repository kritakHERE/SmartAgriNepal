# Smart AgriNepal Cooperative Management System (AuraFarming)

## 1. Title Page Style Introduction

**Project Title:** Smart AgriNepal Cooperative Management System (Prototype Name: AuraFarming)  
**Course Context:** ITS66704 Advanced Programming group assignment  
**Application Type:** Offline-first JavaFX desktop application with local file persistence  
**Primary Goal:** Provide a role-based cooperative management tool for farmers, officers, and admin users without relying on internet, databases, or cloud services.

This project is a strong OOP practice case because it combines real domain workflows (registration, farm/plot setup, crop planning, market intelligence, weather alerts, yield tracking, audit logging, and export) with a layered architecture (MVC + Service + DAO + DTO). It is educationally valuable because it forces careful boundaries between user interface, business rules, and persistence while staying within syllabus constraints (file handling and desktop UI).

**Important source note:** the requested assignment file name `ITS66704_202601_Group_Assignment.txt` is not present in this workspace. The requirements analysis in this report is based on `ap_question.txt`, which contains the ITS66704 assignment brief.

---

## 2. Executive Summary

AuraFarming is implemented as a modular Java application using JavaFX/FXML for the user interface and object serialization (`.dat` files) for persistent storage. The implementation follows an MVC-inspired structure:

- **View:** FXML files under `src/main/resources/com/aurafarming/view`
- **Controller:** UI event handlers under `src/main/java/com/aurafarming/controller`
- **Model:** Domain entities and enums under `src/main/java/com/aurafarming/model`
- **DAO:** File-based data access objects under `src/main/java/com/aurafarming/dao`
- **Service:** Business logic and authorization under `src/main/java/com/aurafarming/service`
- **DTO:** Input/request records under `src/main/java/com/aurafarming/dto`

The system supports the core modules requested in the assignment: user management, farmer management, farm/plot management, crop planning, market prices, weather alerts, reporting/export, and audit logging. It clearly demonstrates encapsulation, inheritance, abstraction, polymorphism, collections, exception handling, and local file I/O.

Some assignment expectations are **partially implemented** or **not present** in code (for example: explicit backup/restore workflow, advanced role-edit UI, and deliverables like UML/peer feedback). These are explicitly called out in this report.

---

## 3. Assignment Requirement Breakdown

### 3.1 Requirement source used

- Assignment brief: `ap_question.txt`
- Implementation evidence: Java sources, FXML views, CSS, `pom.xml`, and README documentation.

### 3.2 Requirement mapping summary

| Requirement from assignment | Implemented in project | Evidence and status |
|---|---|---|
| Offline-first desktop application | Yes | JavaFX desktop app with local files only (`pom.xml`, DAOs, `FileUtil`) |
| No database/cloud/network persistence | Yes | DAO persists to local `.dat` via object serialization (`BaseObjectDAO`) |
| OOP concepts: encapsulation, inheritance, abstraction, polymorphism, interface usage | Mostly yes (interface usage limited) | Abstract `User`, subclasses, typed enums, role-based polymorphic behavior |
| MVC, DAO, DTO patterns | Yes | Distinct packages and responsibilities |
| Module 1 User Management | Yes (core operations) | `AuthService`, `UserService`, login/registration and status toggles |
| Module 2 Farmer Profile Management | Yes (search + activate/deactivate) | `FarmerController`, `UserService` |
| Module 3 Farm and Plot Management | Yes | `FarmPlotService`, `FarmPlotController`, `FarmDAO`, `PlotDAO` |
| Module 4 Crop Planning and Advisory | Yes | `CropPlanService.recommendationScore`, crop plan save flow |
| Module 5 Market Price Management | Yes (history + trend hint) | `MarketPriceService`, `MarketPriceController` |
| Module 6 Weather Risk Alerts | Yes | `WeatherAlertService`, `WeatherAlertController` |
| Module 7 Reporting and Export | Yes (CSV export + preview) | `ExportService`, `ExportController` |
| Module 8 Backup, Restore, Audit Logging | Audit yes, backup/restore not confirmed | Audit fully present; backup/restore flow not found in current Java sources |
| Part A UML diagrams | Not in current source tree as implemented artifacts | I do not have enough context to confirm this. |
| Peer feedback documentation | Not verifiable from code | I do not have enough context to confirm this. |

### 3.3 Constraints check

- **File-based persistence only:** satisfied.
- **Desktop UI using JavaFX/Swing:** JavaFX with FXML is used.
- **No endpoint/web service requirement:** system uses direct method calls and local data services.
- **DAO redesign for files:** implemented via `BaseObjectDAO<T extends Serializable>`.

---

## 4. System Overview

### 4.1 Business purpose

The system digitalizes cooperative operations that were traditionally manual (paper/spreadsheets), especially for environments with limited connectivity. It helps maintain farmer data safely, guide crop decisions by season, track yields, and improve traceability via audit logs.

### 4.2 User roles

- **Admin**
  - Seeded automatically if missing.
  - Full dashboard and module access.
  - Can manage officer/farmer records, view broader audit/export features.
- **Officer**
  - Operational management role.
  - Can manage farmer-related operations and data modules.
- **Farmer**
  - Limited, self-service mode.
  - Restricted editing on sensitive modules; mostly own-data operations.

### 4.3 High-level architecture

`FXML View` -> `Controller` -> `Service` -> `DAO` -> `Object File (.dat)`

Support services:

- `SessionContext` for in-memory current user session.
- `AuditService` for write-on-action logging.
- `FileUtil` and `Constants` for path/file conventions.

### 4.4 Startup and runtime flow

1. App starts through `AuraFarmingApplication` (or launcher/bootstrap wrappers).
2. `FileUtil.ensureAppDirectories()` creates required local folders.
3. `SceneRouter` initializes JavaFX stage and loads login scene.
4. Login/registration controllers call auth services.
5. After successful login, dashboard scene loads and modules are opened by role.

---

## 5. Technology Stack

### 5.1 Java (JDK 21 target)

- Defined in `pom.xml` (`maven.compiler.release` = 21).
- Provides OOP model, collections, streams, date/time, and serialization APIs.
- Used to implement all layers from UI control to file persistence.

### 5.2 JavaFX + FXML

- Dependencies: `javafx-controls`, `javafx-fxml`.
- FXML defines static UI structure; controllers handle behavior.
- `SceneRouter` centralizes navigation and CSS loading.
- Dashboard integrates JavaFX charts (`PieChart`, `BarChart`) for quick metrics visualization.

### 5.3 Maven build system

- `maven-compiler-plugin` for Java compilation.
- `javafx-maven-plugin` for run packaging pipeline.
- `maven-shade-plugin` produces runnable jar bootstrap entry.

### 5.4 File I/O and object serialization

- `BaseObjectDAO` uses `ObjectInputStream/ObjectOutputStream` to store full lists of domain objects in `.dat` files.
- `FileUtil` ensures directory/file existence before read/write.
- Supports offline persistence with no SQL/cloud dependencies.

### 5.5 Java collections and streams

- Heavy use of `List`, `Set`, and stream filtering/grouping for role filtering, search, and dashboard aggregations.

### 5.6 Exception strategy

- Domain-specific runtime exceptions: `ValidationException`, `AuthenticationException`.
- Controllers commonly catch exceptions and show user-friendly alerts via `AlertUtil`.

---

## 6. File-by-File System Analysis

This section is organized by package. Each file includes functional meaning, not just class syntax.

### 6.1 Application bootstrap and module declaration

#### `src/main/java/module-info.java`

- **Technical role:** Java module declaration for JavaFX requirements and exports.
- **Functional role:** Allows JavaFX to reflectively load controller classes in FXML.

#### `src/main/java/com/aurafarming/AuraFarmingApplication.java`

- **Technical role:** Main JavaFX `Application` subclass.
- **Functional role:** Initializes app directories and opens login scene.

#### `src/main/java/com/aurafarming/AuraFarmingLauncher.java`

- **Technical role:** Thin launcher extending application class.
- **Functional role:** Alternate entry point for plugin/runtime launching.

#### `src/main/java/com/aurafarming/AuraFarmingJarBootstrap.java`

- **Technical role:** Final bootstrap for shaded runnable JAR.
- **Functional role:** Redirects execution to launcher for packaged distribution.

### 6.2 Utility files

#### `src/main/java/com/aurafarming/util/Constants.java`

- **Technical role:** Shared constants (seed admin credentials, directory names, data file names, formatters).
- **Functional role:** Keeps path and naming standards centralized and consistent.

#### `src/main/java/com/aurafarming/util/FileUtil.java`

- **Technical role:** Ensures directories/files exist and resolves data/export paths.
- **Functional role:** Prevents runtime failures when first run has no precreated folders.

#### `src/main/java/com/aurafarming/util/IdGenerator.java`

- **Technical role:** Prefix-based UUID short ID generator.
- **Functional role:** Produces readable IDs like `FRM-XXXXXXXX`, `PLT-XXXXXXXX`, etc.

#### `src/main/java/com/aurafarming/util/SceneRouter.java`

- **Technical role:** Central scene loader with FXML and stylesheet binding.
- **Functional role:** Single navigation gateway used by login/registration/dashboard transitions.

#### `src/main/java/com/aurafarming/util/AlertUtil.java`

- **Technical role:** Wrapper around JavaFX `Alert` dialogs.
- **Functional role:** Standard user feedback for success/error.

### 6.3 Exception files

#### `src/main/java/com/aurafarming/exception/AuthenticationException.java`

- **Technical role:** Runtime exception for authentication failures.
- **Functional role:** Signals invalid login state to controller for UI error display.

#### `src/main/java/com/aurafarming/exception/ValidationException.java`

- **Technical role:** Runtime exception for business/data validation failures.
- **Functional role:** Stops invalid operations and communicates constraints.

### 6.4 Domain model files

#### `src/main/java/com/aurafarming/model/User.java`

- **Technical role:** Abstract base class with serializable identity/auth/audit fields.
- **Functional role:** Common user profile contract across admin/officer/farmer.

#### `src/main/java/com/aurafarming/model/Admin.java`

- **Technical role:** `User` subclass with `privilegeLevel`.
- **Functional role:** Represents system superuser account.

#### `src/main/java/com/aurafarming/model/Officer.java`

- **Technical role:** `User` subclass with assigned districts list.
- **Functional role:** Represents cooperative operator role.

#### `src/main/java/com/aurafarming/model/Farmer.java`

- **Technical role:** `User` subclass with district, phone, and farm ID references.
- **Functional role:** Represents farmer account linked to farming operations.

#### `src/main/java/com/aurafarming/model/Farm.java`

- **Technical role:** Serializable farm aggregate root with area and unit metadata.
- **Functional role:** Connects a farmer to one or more plots.

#### `src/main/java/com/aurafarming/model/Plot.java`

- **Technical role:** Serializable plot entity under a farm.
- **Functional role:** Operational unit for crop planning and yield logging.

#### `src/main/java/com/aurafarming/model/CropPlan.java`

- **Technical role:** Crop scheduling/advisory entity.
- **Functional role:** Records planned crop-season combination with recommendation score.

#### `src/main/java/com/aurafarming/model/MarketPrice.java`

- **Technical role:** District crop price snapshot with date.
- **Functional role:** Supports trend and history visibility for pricing decisions.

#### `src/main/java/com/aurafarming/model/WeatherAlert.java`

- **Technical role:** Alert entity with condition, severity, duration, probability.
- **Functional role:** Captures weather-risk advisories by district.

#### `src/main/java/com/aurafarming/model/YieldLog.java`

- **Technical role:** Estimated vs actual harvest log.
- **Functional role:** Enables productivity tracking and dashboard totals.

#### `src/main/java/com/aurafarming/model/AuditLog.java`

- **Technical role:** Activity log record with actor/action/target/timestamp/details.
- **Functional role:** Provides accountability and traceability for operations.

#### Enums

- `Role.java`: Admin/Officer/Farmer role typing.
- `District.java`: Chitwan/Dang/Rupandehi as constrained geography set.
- `Season.java`: Nepali season labels displayed in UI.
- `Severity.java`: Alert severity scale.
- `WeatherCondition.java`: Controlled weather categories.

### 6.5 DTO files

#### `src/main/java/com/aurafarming/dto/LoginDTO.java`

- **Technical role:** Record with `email`, `password`.
- **Functional role:** Safe login payload from form to auth service.

#### `src/main/java/com/aurafarming/dto/RegistrationDTO.java`

- **Technical role:** Record for registration fields and role context.
- **Functional role:** Prevents controller from directly mutating domain entities.

#### `src/main/java/com/aurafarming/dto/ExportRequestDTO.java`

- **Technical role:** Export filter criteria record.
- **Functional role:** Encapsulates report type and filter window for export service.

### 6.6 DAO files

#### `src/main/java/com/aurafarming/dao/BaseObjectDAO.java`

- **Technical role:** Generic serialization DAO base for `List<T>` read/write.
- **Functional role:** Shared persistence engine reused by all concrete DAOs.
- **File handling behavior:**
  - Empty file => returns empty list.
  - EOF => returns empty list.
  - I/O/class errors => wraps in runtime exception.

#### Entity DAOs

- `UserDAO.java`: find/save users; search by email and ID.
- `FarmDAO.java`: persist farms list.
- `PlotDAO.java`: persist plots list.
- `CropPlanDAO.java`: persist crop plans list.
- `MarketPriceDAO.java`: persist market prices list.
- `WeatherAlertDAO.java`: persist alerts list.
- `YieldLogDAO.java`: persist yield logs list.
- `AuditLogDAO.java`: persist audit logs list.

Each concrete DAO only defines file name and exposes typed `findAll/saveAll` methods, delegating actual stream operations to `BaseObjectDAO`.

### 6.7 Service files

#### `src/main/java/com/aurafarming/service/SessionContext.java`

- **Technical role:** Static in-memory current-user holder.
- **Functional role:** Session state for role checks and ownership checks.

#### `src/main/java/com/aurafarming/service/AuditService.java`

- **Technical role:** Audit log writer/filter.
- **Functional role:** Records key actions and supports filtered viewing by date/action/user.

#### `src/main/java/com/aurafarming/service/AuthService.java`

- **Technical role:** Registration, login, logout, admin seed creation.
- **Functional role:** Entry gate and identity lifecycle manager.
- **Key behaviors:**
  - blocks admin registration from public form,
  - validates email/password fields,
  - sets session on login,
  - writes login success/fail and register/logout audit events.

#### `src/main/java/com/aurafarming/service/UserService.java`

- **Technical role:** Role-based user retrieval and status updates.
- **Functional role:** User management used by officer/farmer/admin screens.

#### `src/main/java/com/aurafarming/service/FarmPlotService.java`

- **Technical role:** Farm/plot business rules and ownership checks.
- **Functional role:** Manages one-to-many farm-plot structure and cascaded plot deletion when farm deleted.

#### `src/main/java/com/aurafarming/service/CropPlanService.java`

- **Technical role:** Rule-based scoring + crop plan persistence.
- **Functional role:** Implements basic advisory logic required by assignment.

#### `src/main/java/com/aurafarming/service/MarketPriceService.java`

- **Technical role:** Save/history/trend logic for market entries.
- **Functional role:** Converts raw price logs into decision hints (up/down/stable/no trend).

#### `src/main/java/com/aurafarming/service/WeatherAlertService.java`

- **Technical role:** Alert persistence and sorting.
- **Functional role:** Supports weather risk communication module.

#### `src/main/java/com/aurafarming/service/YieldLogService.java`

- **Technical role:** Yield persistence, visibility filtering, seasonal/year aggregation.
- **Functional role:** Enables harvest tracking and dashboard KPI calculations.

#### `src/main/java/com/aurafarming/service/DashboardService.java`

- **Technical role:** Aggregation service over users/farms/yields.
- **Functional role:** Supplies dashboard cards and chart data.

#### `src/main/java/com/aurafarming/service/ExportService.java`

- **Technical role:** Generates CSV content and writes export files.
- **Functional role:** Produces shareable reports with role-aware restrictions.
- **Supported report types:** `audit`, `yield`, `market`, `crop-plan`, `plot`.

### 6.8 Controller files

#### `src/main/java/com/aurafarming/controller/LoginController.java`

- Handles login form and route to dashboard.
- On error shows alert.

#### `src/main/java/com/aurafarming/controller/RegistrationController.java`

- Initializes role/district options.
- Submits registration DTO.
- Returns to login after success.

#### `src/main/java/com/aurafarming/controller/DashboardController.java`

- Loads metrics and charts.
- Applies role-based tab visibility.
- Hosts module injection area (`moduleContainer`).
- Handles module switching and logout.

#### `src/main/java/com/aurafarming/controller/OfficerController.java`

- Officer search/deactivation UI.
- Renders officer text list.

#### `src/main/java/com/aurafarming/controller/FarmerController.java`

- Admin or self-limited farmer search/reactivation/deactivation operations.
- Hides search controls when current user is farmer.

#### `src/main/java/com/aurafarming/controller/FarmPlotController.java`

- Creates farms and plots from user inputs.
- Updates dependent combo options (farm -> plots).
- Displays farm and plot hierarchy in output area.

#### `src/main/java/com/aurafarming/controller/CropPlanController.java`

- Supports recommendation scoring and plan save.
- For farmer role, hides optional farmer ID override field.

#### `src/main/java/com/aurafarming/controller/MarketPriceController.java`

- Saves market prices (disabled for farmer role).
- Displays history and trend label.

#### `src/main/java/com/aurafarming/controller/WeatherAlertController.java`

- Saves alerts (disabled for farmer role).
- Displays alert stream.

#### `src/main/java/com/aurafarming/controller/YieldLogController.java`

- Saves harvest records.
- Auto-sets district from selected plot’s farm when possible.
- Shows visible logs according to role.

#### `src/main/java/com/aurafarming/controller/AuditLogController.java`

- Applies filters and displays audit rows.

#### `src/main/java/com/aurafarming/controller/ExportController.java`

- Builds export request DTO.
- Provides preview and write-to-file actions.
- Restricts report options for farmer role.

### 6.9 View files (FXML)

- `login.fxml`: email/password login and registration redirect.
- `registration.fxml`: registration fields including role/district.
- `dashboard.fxml`: top module tabs, KPI cards, and charts.
- `officers.fxml`: officer search/deactivate view.
- `farmers.fxml`: farmer management view.
- `farm-plots.fxml`: farm and plot operations.
- `crop-plan.fxml`: crop advisory and save plan flow.
- `market-price.fxml`: market entry/history/trend view.
- `weather-alert.fxml`: alert creation/view.
- `yield-log.fxml`: yield logging view.
- `audit-log.fxml`: audit filter panel and output.
- `export.fxml`: report preview/export panel.

### 6.10 Styling file

#### `src/main/resources/com/aurafarming/css/app.css`

- Provides visual consistency with gradient background, cards, button styles, and chart framing.
- Functional effect: makes dashboards/modules visually structured for presentation and usability.

---

## 7. Logical Flow of the System

### 7.1 End-to-end flow

1. User starts app.
2. Login or registration page appears.
3. Auth service validates credentials or registration data.
4. Session context stores logged-in user.
5. Dashboard loads and filters UI by role.
6. User selects module tab.
7. Controller receives action and forwards to service.
8. Service performs validation and role checks.
9. DAO reads/writes local serialized file data.
10. Service writes audit entry for critical actions.
11. Controller updates output area / labels / charts.
12. Export module optionally writes CSV output file.

### 7.2 Example trace: crop planning

- UI action: click Recommend -> `CropPlanController.onRecommend()`.
- Service call: `CropPlanService.recommendationScore(crop, season)`.
- Result shown as score badge.
- Save action -> `CropPlanService.savePlan(...)`.
- DAO write -> `CropPlanDAO.saveAll(...)` -> `crop_plans.dat`.
- Audit -> `AuditService.log(..., "SAVE_CROP_PLAN", ...)`.

### 7.3 Example trace: market price history

- UI action: click View History.
- Controller calls `MarketPriceService.findHistory(from,to,crop)`.
- Service filters, sorts by date desc, returns list.
- Controller renders rows and updates trend via `trendHint(...)`.

---

## 8. Design Pattern and Principle Analysis

### 8.1 MVC analysis

- **Model:** domain entities (`User`, `Farm`, `CropPlan`, etc.) and enums.
- **View:** FXML files define forms, controls, and scene layout.
- **Controller:** receives user events, invokes services, updates view controls.

**Assessment:** implementation is **MVC-inspired and mostly clean**. Controllers still contain some lightweight view formatting and minor input conversion (`parseDouble`, `parseInt`), but core business logic is in services.

### 8.2 DAO analysis

The DAO layer is explicit and file-focused, exactly matching assignment constraints.

- Generic DAO base removes duplication.
- Each domain DAO binds to one file (`users.dat`, `farms.dat`, etc.).
- Persistence is list-level serialization (rewrite file on save).

### 8.3 DTO analysis

DTOs are used in flows where input grouping matters:

- `LoginDTO`
- `RegistrationDTO`
- `ExportRequestDTO`

They decouple raw form data from service internals and support clean method signatures.

### 8.4 Encapsulation

- Domain fields are private with getters/setters.
- Controllers do not directly manipulate file streams.
- DAOs hide serialization details from services/controllers.

### 8.5 Inheritance

- `User` is abstract base class.
- `Admin`, `Officer`, and `Farmer` inherit shared identity/auth behavior.

### 8.6 Abstraction

- Abstract class `User` defines a generalized user contract.
- Generic abstract `BaseObjectDAO<T>` abstracts file serialization mechanics.

### 8.7 Polymorphism

- Services/controllers handle `User` references and branch by runtime role.
- Example: role-based behavior in `DashboardController`, `FarmPlotService`, `YieldLogService`, `ExportService`.

### 8.8 Composition

- Services compose DAOs and other services (for example `ExportService` composes audit, farm/plot, market, yield, crop-plan services).

### 8.9 Interface usage

No custom Java interface types are defined in current source files. The assignment mentions interface concept coverage, but in this codebase the abstraction emphasis is through abstract class and generic base class instead.

---

## 9. Constraint Handling

### 9.1 Syllabus and architecture constraints

- File persistence only: satisfied.
- Desktop GUI: satisfied via JavaFX + FXML.
- DAO/DTO/MVC usage: satisfied.
- Role-based access: present and enforced in multiple modules.

### 9.2 Data and workflow constraints seen in implementation

- Email must contain `@` and `.`.
- Password and confirm password must match.
- Admin role cannot be self-registered.
- Farmer registration requires district.
- Farm area and plot area must be positive.
- Farmer cannot create farm for other user IDs.
- Farmer cannot export unauthorized report types.
- Farmer cannot save market prices or weather alerts (UI disables save).

### 9.3 Constraints from assignment that are not fully evidenced

- Full backup/restore workflow: not found in Java source methods.
- Explicit static prototype/peer feedback/UML deliverables: not derivable from code alone.

For these, **I do not have enough context to confirm this**.

---

## 10. Implementation Walkthrough

### 10.1 Authentication and session

- App seeds default admin (`admin@control.com` / `admin123`) if missing.
- Login checks email/password and active status.
- Last login timestamp is updated on success.
- Session user is set in `SessionContext`.

### 10.2 Role-based dashboard

- Dashboard computes KPIs:
  - active farmers
  - active officers
  - seasonal yield total
  - yearly yield total
  - active users in last 24 hours
- Charts:
  - user composition pie
  - users by district bar
  - harvest by district bar

### 10.3 User management flows

- Officer/farmer screens call `UserService` for search and status changes.
- Farmer role sees restricted controls and self-scope behavior.

### 10.4 Farm and plot flows

- Farm creation requires district, unit, area.
- Plot creation requires selected farm and positive area.
- Plot code includes farm-based pattern for readability.
- Farm deletion cascades to linked plots.

### 10.5 Crop advisory and planning

- Simple deterministic scoring function maps crop-season match quality.
- Score is saved with plan for traceability.

### 10.6 Market and weather modules

- Market module saves district crop price with current date.
- Trend compares latest two entries for same district/crop.
- Weather module stores condition, severity, duration, and probability.

### 10.7 Yield module

- Captures estimated and actual yield with harvest date.
- Visibility filtered by role (farmer sees own records).
- Dashboard seasonal/year totals consume this data.

### 10.8 Audit and export

- Services append audit events for key operations.
- Export module supports preview and CSV write.
- Farmer export permissions are constrained to own applicable report types.

---

## 11. Important Code Excerpts

### 11.1 Generic DAO and file serialization

```java
public abstract class BaseObjectDAO<T extends Serializable> {
    protected List<T> readAllInternal() {
        Path path = FileUtil.resolveDataFile(fileName);
        File file = path.toFile();
        if (file.length() == 0) {
            return new ArrayList<>();
        }
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = inputStream.readObject();
            if (obj instanceof List<?>) {
                return (List<T>) obj;
            }
            return new ArrayList<>();
        } catch (EOFException ignored) {
            return new ArrayList<>();
        }
    }
}
```

**Why this matters:** it implements offline persistence in a reusable, strongly typed form.

### 11.2 OOP inheritance and abstraction

```java
public abstract class User implements Serializable {
    private String userId;
    private String fullName;
    private String email;
    private String password;
    private Role role;
    private boolean active;
}

public class Farmer extends User {
    private District district;
    private String phone;
}
```

**Why this matters:** common state is centralized while role-specific attributes extend behavior.

### 11.3 Rule-based crop recommendation

```java
public int recommendationScore(String cropType, Season season) {
    return switch (season) {
        case MONSOON -> crop.contains("rice") ? 90 : 45;
        case WINTER -> crop.contains("wheat") || crop.contains("mustard") ? 88 : 40;
        case SPRING -> crop.contains("maize") ? 82 : 55;
        case SUMMER -> crop.contains("vegetable") ? 80 : 50;
        case AUTUMN -> crop.contains("millet") ? 78 : 52;
    };
}
```

**Why this matters:** demonstrates business rules implementation rather than pure CRUD.

### 11.4 Role-based export restriction

```java
if (actor.getRole() == Role.FARMER) {
    if (!requestedType.equals("plot") && !requestedType.equals("crop-plan")) {
        throw new ValidationException("Farmers can export only Plot and Crop Plan reports.");
    }
}
```

**Why this matters:** enforces least-privilege data access in a local application context.

---

## 12. Common Questions for Presentation (Viva Guide)

1. **Why did you choose JavaFX + FXML instead of console-only?**  
   Because the assignment asks for a desktop UI and user-centric interaction. FXML separates layout from logic, making screen maintenance easier.

2. **How is MVC implemented here?**  
   FXML views define UI; controllers receive events; models hold domain state; services/DAOs process business and persistence. It is MVC-inspired with an added service layer.

3. **How did you implement DAO without database?**  
   Every entity has a DAO backed by local serialized files. `BaseObjectDAO` reads/writes typed object lists using streams.

4. **What is the purpose of DTOs if models already exist?**  
   DTOs carry raw request data from UI to service cleanly and reduce direct model coupling to form internals.

5. **Where do you show inheritance and polymorphism?**  
   `User` abstract base class with `Admin/Officer/Farmer` subclasses; role-based logic executes via runtime user type/role.

6. **How do you prevent invalid registration?**  
   Registration validation checks required fields, email format, password confirmation, role restrictions, and district requirement for farmers.

7. **How do you handle missing files on first run?**  
   `FileUtil` creates data/export directories and creates data files when resolving paths.

8. **How do you guarantee audit traceability?**  
   Critical actions call `AuditService.log(...)`, storing actor, action, target, timestamp, and details.

9. **How do role permissions work?**  
   Role checks exist in dashboard tab visibility, service authorization checks, and controller-level disabling of restricted actions.

10. **What happens if corrupted serialized data exists?**  
    DAO read throws runtime exception wrapping the cause. Current implementation does not include automated repair/recovery logic.

11. **Does the project implement backup/restore?**  
    Audit logging is implemented. Explicit backup/restore operations are not visible in the current Java source; I do not have enough context to confirm a separate implementation.

12. **How does this design support future extension?**  
    Clear package layering, centralized constants, generic DAO base, and modular controllers/services allow adding modules with low coupling.

---

## 13. Glossary

- **Abstraction:** Hiding lower-level details behind generalized contracts (for example abstract `User`, generic `BaseObjectDAO`).
- **Active user in 24h:** User whose `lastLoginAt` is within last 24 hours.
- **Audit log:** Immutable-style record of actions for accountability.
- **DAO (Data Access Object):** Class that isolates data read/write operations from business logic.
- **DTO (Data Transfer Object):** Lightweight data container used to pass input between layers.
- **Encapsulation:** Keeping object state private and exposing controlled access methods.
- **FXML:** XML markup format used by JavaFX to define UI layout.
- **Inheritance:** Child class reuses and extends parent class behavior (`Farmer extends User`).
- **Local serialization:** Saving Java objects directly into binary files.
- **Module container:** Dashboard region where feature screens are dynamically loaded.
- **MVC:** Separation of Model, View, Controller responsibilities.
- **Offline-first:** Application can operate without network dependency.
- **Polymorphism:** Same reference type (`User`) exhibits different behavior by runtime role.
- **Role-based access control:** Permissions differ by admin/officer/farmer role.
- **Seasonal yield total:** Sum of yields in current quarter-like seasonal window used by service.
- **Service layer:** Middle layer for rules, validation, and orchestration between controllers and DAOs.

---

## 14. Appendix

### 14.1 Assignment requirement vs implementation status table

| Area | Status | Notes |
|---|---|---|
| Core modular structure | Implemented | User/farmer/farm-crop/market/weather/yield/audit/export modules present |
| File-only persistence | Implemented | `.dat` object serialization in local folders |
| MVC + DAO + DTO | Implemented | Strongly represented in package structure |
| OOP concepts | Implemented | Encapsulation, inheritance, abstraction, polymorphism clearly used |
| Backup/restore | Uncertain/likely missing in current code | No explicit backup/restore service/controller found |
| UML/business artifacts | Not confirmable from code | I do not have enough context to confirm this |

### 14.2 Data files used by module

- `users.dat`
- `farms.dat`
- `plots.dat`
- `crop_plans.dat`
- `market_prices.dat`
- `weather_alerts.dat`
- `yield_logs.dat`
- `audit_logs.dat`

These are resolved under the local directory `data storage`.

### 14.3 Presentation speaking flow (recommended)

1. Problem context and constraints (offline, no DB).  
2. Architecture diagram explanation (MVC + Service + DAO + files).  
3. Role-based flow demo (login -> dashboard -> module).  
4. One full use case walkthrough (farm -> plot -> crop plan -> yield).  
5. Data persistence and audit reliability discussion.  
6. OOP/design pattern mapping with concrete class examples.  
7. Gap analysis and future improvements (backup/restore, stronger validation, tests).

### 14.4 Future enhancement ideas (still syllabus-friendly)

- Add explicit backup and restore service using file copy/versioned snapshots.
- Add stronger numeric/date validation with user-friendly errors at controller boundary.
- Add soft delete policies consistently for sensitive records.
- Add unit tests for services (auth, recommendation, export filters).
- Add hash-based password storage for better security.

---

## Final Quality Checklist

- Requirement coverage aligned to assignment source file: yes.  
- Start-to-finish logic flow explained: yes.  
- Major components explained technically and functionally: yes.  
- MVC/DAO/DTO/OOP/file/exception handling addressed: yes.  
- Missing/uncertain items explicitly marked without guessing: yes.  
- Glossary and appendix included: yes.  
- Presentation-ready Q&A section included: yes.
