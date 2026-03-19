# AuraFarming System Analysis and Presentation Guide

## 1. Title Page Style Introduction

**Project title:** AuraFarming - Smart AgriNepal Cooperative Management System  
**Project type:** Offline-first JavaFX desktop application for Advanced Programming coursework  
**Primary domain:** Agricultural cooperative management in Nepal

AuraFarming is a desktop-based prototype that models the daily operational needs of an agricultural cooperative. Its purpose is to manage users, farms, plots, crop plans, market prices, weather alerts, yield logs, audit logs, and report exports without depending on a database or internet connection. That design matters because the assignment scenario explicitly targets rural and low-connectivity environments, where reliability and local persistence are more important than cloud integration.

This report is written as both a study guide and a presentation guide. It explains not only what each class and file does, but why it exists in the system, how execution flows through it, and where the implementation matches or does not match the assignment brief.

**Important source note:** the exact file `ITS66704_202601_Group_Assignment.txt` is not present in the workspace. For assignment requirements, this report uses:

- `ap_question.txt` as the main assignment brief
- `ProjectDetailByUser.txt` as additional requirement notes
- the Java source, FXML files, CSS, Maven configuration, and observed data folders as the implementation evidence

Where the code does not confirm a claim from the notes, this report marks that claim as missing, incomplete, or uncertain instead of inventing details.

## 2. Executive Summary

AuraFarming is implemented as a layered JavaFX application with a practical MVC-inspired structure:

- **View:** FXML screens in `src/main/resources/com/aurafarming/view`
- **Controller:** JavaFX controllers in `src/main/java/com/aurafarming/controller`
- **Service:** business logic and authorization in `src/main/java/com/aurafarming/service`
- **DAO:** file-based persistence through object serialization in `src/main/java/com/aurafarming/dao`
- **Model:** domain entities and enums in `src/main/java/com/aurafarming/model`
- **DTO:** lightweight request objects for login, registration, and export

The system starts in the login screen, authenticates against locally serialized user data, stores the current user in a session holder, loads a dashboard with role-based tab visibility, and routes all module actions through controller -> service -> DAO -> file storage.

The implementation satisfies several major assignment constraints:

- desktop application
- offline local persistence
- no SQL database
- object-oriented modeling
- file-based DAO pattern
- JavaFX with FXML

However, some assignment or note-level expectations are only partially implemented or not implemented in source:

- backup and restore are not implemented as dedicated features
- weather alerts are manually entered, not loaded from an external weather data file
- export is CSV-based only, not charts or graphs
- user update, password change, and role change are not exposed as working UI flows
- officer-side farmer management is weaker than the notes describe
- some validation and exception handling are inconsistent across modules

## 3. Assignment Requirement Breakdown

### 3.1 Requirement Summary from `ap_question.txt`

The assignment describes an offline-first Smart AgriNepal Cooperative Management System for a Nepal cooperative operating in Chitwan, Dang, and Rupandehi. It requires:

- role-based access for Admin, Officer, and Farmer
- farmer profile management
- farm and plot management
- crop planning and advisory
- market price management
- weather risk alerts
- reporting and export
- backup, restore, and audit logging
- strong use of OOP concepts
- use of MVC, DAO, DTO, and other suitable patterns
- file-based persistence only
- JavaFX or Swing GUI

### 3.2 Requirement-to-Implementation Matrix

| Assignment requirement | Implemented in project | Evidence from source | Status |
|---|---|---|---|
| Offline desktop application | Yes | JavaFX app entry, FXML views, no network code | Implemented |
| No database, local files only | Yes | `BaseObjectDAO`, `FileUtil`, `.dat` files in `data storage` | Implemented |
| User authentication and role-based access | Yes | `AuthService`, `SessionContext`, `DashboardController` | Implemented |
| Predefined admin account | Yes | `AuthService.ensureAdminSeed()` | Implemented |
| Farmer registration | Yes | `RegistrationController`, `AuthService.register()` | Implemented |
| Officer registration | Yes | `RegistrationController`, `AuthService.register()` | Implemented |
| Admin registration disabled | Yes | `AuthService.validateRegistration()` | Implemented |
| Farmer profile search and basic management | Partial | `FarmerController`, `UserService.search/deactivate/reactivate` | Partially implemented |
| Officer management | Partial | `OfficerController`, `UserService` | Partially implemented |
| User update and password change | Limited | `UserService.updateUser()` exists, but no controller/UI flow uses it | Incomplete |
| Admin role change for users | No working flow found | `User.setRole()` exists, but no service/controller uses it | Missing |
| Farm creation | Yes | `FarmPlotService.createFarm()` | Implemented |
| Plot creation | Yes | `FarmPlotService.createPlot()` | Implemented |
| Farm deletion | Yes | `FarmPlotService.deleteFarm()` cascades plot removal | Implemented |
| Plot edit/delete as its own feature | No dedicated implementation found | No `deletePlot` or plot update method | Missing |
| Crop recommendation score | Yes | `CropPlanService.recommendationScore()` | Implemented |
| Crop plan save | Yes | `CropPlanService.savePlan()` | Implemented |
| Market price save and history | Yes | `MarketPriceService.save()` and `findHistory()` | Implemented |
| District comparison for market price | No explicit compare feature | No compare method found | Missing |
| Weather alert creation and viewing | Yes | `WeatherAlertService`, `WeatherAlertController` | Implemented |
| Load weather data from files | I do not have enough context to confirm this from source, and I do not see file-import logic | No parser/import class found | Missing or uncertain |
| Yield logging | Yes | `YieldLogService`, `YieldLogController` | Implemented |
| Audit logging | Yes | `AuditService`, `AuditLogDAO` | Implemented |
| Backup and restore | No dedicated implementation found | No backup/restore classes or methods found | Missing |
| Reporting and export | Yes, in CSV form | `ExportService`, `ExportController` | Implemented, but limited |
| Export charts, pie charts, graphs | No export logic for charts found | Export writes CSV text lines only | Missing |
| UML diagrams, package diagrams, prototype screenshots | I do not have enough context to confirm this from the provided files | Not found in repository | Uncertain or missing from workspace |
| Testing evidence | No automated test source found | No `src/test` package present | Limited evidence |

### 3.3 Requirements That Shape the Design

The assignment constraints directly explain several design choices:

- Because database usage is forbidden, each DAO writes serialized object lists to `.dat` files.
- Because the system is desktop-based, the UI is built in JavaFX with `.fxml` layouts.
- Because the assignment emphasizes OOP, the user hierarchy is modeled with `User` as an abstract superclass and `Admin`, `Officer`, and `Farmer` as subclasses.
- Because the assignment asks for DAO, MVC, and DTO, the project is organized into packages that separate these concerns clearly.

## 4. System Overview

### 4.1 System Purpose

AuraFarming is intended to help a cooperative manage agricultural operations without a cloud backend. It records who the users are, what farms and plots exist, what crops are planned, how market prices change, what weather risks exist, what yields were harvested, and what actions users performed.

### 4.2 Main User Roles

**Admin**

- full login access
- can see all dashboard metrics
- can view officer and farmer records
- can access full audit log filtering
- can access all export types

**Officer**

- can log in and access operational modules
- can save market prices
- can save weather alerts
- can see all yield logs
- can access export screen
- cannot access officer management tab from the dashboard

**Farmer**

- can register and log in
- can create own farms and plots
- can create crop plans
- can create and view own yield logs
- can view market price history
- can view weather alerts
- can export only plot and crop-plan reports for own user ID

### 4.3 High-Level Architecture

AuraFarming is best described as a **layered MVC-inspired desktop application**:

`FXML View -> Controller -> Service -> DAO -> Serialized File`

This means:

- the user interacts with controls in an FXML view
- the controller reads those controls and calls service methods
- the service applies validation, role logic, or business rules
- the DAO reads or writes serialized lists in local files
- the result returns upward so the controller can update labels, charts, or text areas

### 4.4 How the Program Starts

Startup flow:

1. `AuraFarmingJarBootstrap` forwards to `AuraFarmingLauncher`
2. `AuraFarmingLauncher` launches the JavaFX application
3. `AuraFarmingApplication.start()` ensures the local folders exist
4. `SceneRouter.initialize(stage)` stores the primary stage
5. `SceneRouter.goTo()` opens `login.fxml`

That startup sequence exists to guarantee that storage folders are ready before any DAO tries to read or write data.

### 4.5 Data Movement Through the System

Example data movement for a crop plan:

1. User selects plot, crop, season, and dates in `crop-plan.fxml`
2. `CropPlanController` computes or reuses a recommendation score
3. `CropPlanService.savePlan()` creates a `CropPlan` object
4. `CropPlanDAO` reads the current list of plans from `crop_plans.dat`
5. The new plan is appended to the in-memory list
6. The DAO rewrites the file with the updated list
7. `AuditService` records the action
8. The controller updates the text area for user feedback

## 5. Technology Stack

### 5.1 Java

**What it is:** the main programming language used for the application.  
**Why it is used:** the assignment requires object-oriented programming and desktop development, both of which Java supports well.  
**How it works in this project:** all domain models, services, DAOs, controllers, utilities, and application bootstrap classes are written in Java.

### 5.2 JavaFX

**What it is:** Java's modern desktop UI toolkit.  
**Why it is used:** the assignment asks for a desktop GUI using JavaFX or Swing. JavaFX supports FXML, styling, charts, forms, and scene switching.  
**How it works in this project:** JavaFX powers application startup, screens, controls, charts, dialogs, and navigation.

**Version evidence:** `pom.xml` uses JavaFX `21.0.6`.  
**Important note:** `ProjectDetailByUser.txt` says JavaFX `25.0.0`, but the build file is the stronger implementation evidence, so the confirmed version in source is `21.0.6`.

### 5.3 FXML

**What it is:** an XML-based way to define JavaFX user interfaces declaratively.  
**Why it is used:** it keeps layout separate from controller logic, which supports MVC-style organization.  
**How it works in this project:** each screen such as login, registration, dashboard, farm plots, market price, and export has its own `.fxml` file and corresponding controller class.

### 5.4 CSS

**What it is:** a styling language used by JavaFX to theme controls.  
**Why it is used:** it centralizes visual design instead of hard-coding styles in each screen.  
**How it works in this project:** `app.css` defines shared colors, gradients, button styling, metric cards, chart cards, and form control appearance.

### 5.5 Maven

**What it is:** the build and dependency management tool for the project.  
**Why it is used:** it keeps JavaFX dependencies, compiler settings, and packaging behavior consistent.  
**How it works in this project:** `pom.xml` defines JavaFX dependencies, Java release `21`, the JavaFX Maven plugin, and the Shade plugin for a runnable JAR.

### 5.6 Object Serialization

**What it is:** Java's built-in mechanism for storing entire objects or object graphs in binary form.  
**Why it is used:** the assignment forbids SQL databases and allows file-based persistence. Serialization provides a simple way to persist lists of domain objects.  
**How it works in this project:** each DAO reads and writes a `List<T>` of serialized objects into `.dat` files in the `data storage` directory.

### 5.7 Java Collections

**What it is:** the standard `List`, `Set`, `Map`, and stream APIs.  
**Why it is used:** the app frequently filters, groups, sorts, and rewrites in-memory lists before persisting them.  
**How it works in this project:** user searches, history queries, chart aggregation, export generation, and farm-to-plot mapping all depend on collection processing.

### 5.8 Java Records

**What it is:** a compact Java feature for immutable data carriers.  
**Why it is used:** DTOs do not need mutable behavior; they only package request data.  
**How it works in this project:** `LoginDTO`, `RegistrationDTO`, and `ExportRequestDTO` are records that move form data from controllers into services.

### 5.9 Custom Exceptions

**What it is:** project-defined exception types for domain-specific failures.  
**Why it is used:** they make validation and authentication errors more meaningful than generic exceptions.  
**How it works in this project:** `ValidationException` and `AuthenticationException` communicate business rule failures from services to controllers.

## 6. File-by-File System Analysis

### 6.1 Assignment and Project Definition Files

| File path | Functional description | Technical role | How it fits system understanding |
|---|---|---|---|
| `ap_question.txt` | Assignment brief with scenario, modules, constraints, and grading criteria | Requirement source document | Defines what the system is supposed to achieve |
| `ProjectDetailByUser.txt` | Additional student requirement notes and UI expectations | Supplementary requirement note | Useful for intent, but source code is the final authority |
| `README.md` | Repository overview and running notes | Project documentation | Helps interpret structure but is secondary to code |
| `documentation.md` | Extended design notes | Documentation | Useful background, but some claims are broader than implementation |

### 6.2 Build and Module Files

| File path | Functional description | Technical role | Dependencies and behavior |
|---|---|---|---|
| `pom.xml` | Defines how the project is built and packaged | Maven build configuration | Declares JavaFX dependencies, compiler release 21, JavaFX plugin, Shade plugin |
| `src/main/java/module-info.java` | Declares Java module requirements and JavaFX openness | Java module descriptor | Requires JavaFX modules, exports `com.aurafarming` and `com.aurafarming.model`, opens controller package to FXML |

### 6.3 Application Bootstrap Files

| File path | Functional description | Technical role | System flow contribution |
|---|---|---|---|
| `src/main/java/com/aurafarming/AuraFarmingApplication.java` | Starts the application and opens login screen | Main JavaFX `Application` class | Creates required folders and routes first scene |
| `src/main/java/com/aurafarming/AuraFarmingLauncher.java` | Simplifies launch entry for JavaFX plugin | Launcher wrapper | Used by Maven JavaFX run target |
| `src/main/java/com/aurafarming/AuraFarmingJarBootstrap.java` | Entry point for shaded runnable JAR | Bootstrap main class | Allows packaged JAR to start the JavaFX app |

### 6.4 Utility Files

| File path | Functional description | Technical role | System flow contribution |
|---|---|---|---|
| `src/main/java/com/aurafarming/util/Constants.java` | Centralizes file names, admin seed values, and date formats | Constant holder | Prevents repeated literals across the app |
| `src/main/java/com/aurafarming/util/FileUtil.java` | Creates app directories and resolves file paths | File helper | Ensures missing directories and files are created before DAO access |
| `src/main/java/com/aurafarming/util/IdGenerator.java` | Generates short prefixed UUID-based IDs | ID utility | Gives users, farms, plots, logs, and reports stable identifiers |
| `src/main/java/com/aurafarming/util/SceneRouter.java` | Loads FXML screens and applies CSS | Navigation helper | Controls high-level scene switching and shared styling |
| `src/main/java/com/aurafarming/util/AlertUtil.java` | Shows info and error dialogs | UI feedback helper | Used by controllers for user-facing success/failure alerts |

### 6.5 Model Files

| File path | Functional description | Technical role | Why it exists |
|---|---|---|---|
| `src/main/java/com/aurafarming/model/User.java` | Base user record for all roles | Abstract serializable superclass | Unifies shared user state such as ID, name, email, password, role, active flag, creation time, and last login time |
| `src/main/java/com/aurafarming/model/Admin.java` | Represents the predefined administrator | `User` subclass | Captures admin identity and privilege level |
| `src/main/java/com/aurafarming/model/Officer.java` | Represents a cooperative officer | `User` subclass | Supports officer-specific state, currently assigned districts |
| `src/main/java/com/aurafarming/model/Farmer.java` | Represents a farmer user | `User` subclass | Stores district, phone, and farm ownership links |
| `src/main/java/com/aurafarming/model/Farm.java` | Represents a farm owned by a farmer | Serializable entity | Holds district, tag, unit, and area for farm management |
| `src/main/java/com/aurafarming/model/Plot.java` | Represents a subdivision of a farm | Serializable entity | Allows one farm to have multiple usable planting sections |
| `src/main/java/com/aurafarming/model/CropPlan.java` | Represents a crop plan for a plot | Serializable entity | Stores crop type, season, recommendation score, and planned dates |
| `src/main/java/com/aurafarming/model/MarketPrice.java` | Represents a market price entry | Serializable entity | Preserves district-wise crop pricing history |
| `src/main/java/com/aurafarming/model/WeatherAlert.java` | Represents a weather risk alert | Serializable entity | Stores manual risk alerts with severity and probability |
| `src/main/java/com/aurafarming/model/YieldLog.java` | Represents a harvest record | Serializable entity | Tracks estimated and actual yield for later reporting |
| `src/main/java/com/aurafarming/model/AuditLog.java` | Represents a user action log | Serializable entity | Supports traceability and role-based audit review |
| `src/main/java/com/aurafarming/model/Role.java` | Defines system roles | Enum | Enables role-based decisions in services and controllers |
| `src/main/java/com/aurafarming/model/District.java` | Defines supported districts | Enum | Keeps district usage consistent across modules |
| `src/main/java/com/aurafarming/model/Season.java` | Defines agricultural seasons with display labels | Enum | Drives recommendation scoring and presentation-friendly display |
| `src/main/java/com/aurafarming/model/Severity.java` | Defines alert severity levels | Enum | Standardizes weather risk classification |
| `src/main/java/com/aurafarming/model/WeatherCondition.java` | Defines alertable weather conditions | Enum | Standardizes hazard categories |

### 6.6 DTO Files

| File path | Functional description | Technical role | Why it exists |
|---|---|---|---|
| `src/main/java/com/aurafarming/dto/LoginDTO.java` | Packages login email and password | Immutable DTO record | Prevents controllers from passing raw fields separately |
| `src/main/java/com/aurafarming/dto/RegistrationDTO.java` | Packages registration form data | Immutable DTO record | Moves form values into `AuthService` in one object |
| `src/main/java/com/aurafarming/dto/ExportRequestDTO.java` | Packages export parameters | Immutable DTO record | Collects report type, date range, district, farmer, and role values |

**Important implementation note:** `ExportRequestDTO` contains `district` and `role`, but the current `ExportService` does not use those fields for filtering logic.

### 6.7 DAO Files

| File path | Functional description | Technical role | How it fits persistence flow |
|---|---|---|---|
| `src/main/java/com/aurafarming/dao/BaseObjectDAO.java` | Shared read/write logic for serialized lists | Generic DAO base class | Reads lists from files, returns empty lists for empty files, rewrites whole file on save |
| `src/main/java/com/aurafarming/dao/UserDAO.java` | User persistence and lookup | DAO | Supports user list retrieval, save-all, append-save, find by email, and find by ID |
| `src/main/java/com/aurafarming/dao/FarmDAO.java` | Farm persistence | DAO | Stores all farms in `farms.dat` |
| `src/main/java/com/aurafarming/dao/PlotDAO.java` | Plot persistence | DAO | Stores all plots in `plots.dat` |
| `src/main/java/com/aurafarming/dao/CropPlanDAO.java` | Crop plan persistence | DAO | Stores crop plans in `crop_plans.dat` |
| `src/main/java/com/aurafarming/dao/MarketPriceDAO.java` | Market price persistence | DAO | Stores historical market prices in `market_prices.dat` |
| `src/main/java/com/aurafarming/dao/WeatherAlertDAO.java` | Weather alert persistence | DAO | Stores weather alerts in `weather_alerts.dat` |
| `src/main/java/com/aurafarming/dao/YieldLogDAO.java` | Yield log persistence | DAO | Stores harvest entries in `yield_logs.dat` |
| `src/main/java/com/aurafarming/dao/AuditLogDAO.java` | Audit log persistence | DAO | Stores all audit entries in `audit_logs.dat` |

### 6.8 Service Files

| File path | Functional description | Technical role | Key methods and system role |
|---|---|---|---|
| `src/main/java/com/aurafarming/service/SessionContext.java` | Stores the currently logged-in user in memory | Session helper | `getCurrentUser`, `setCurrentUser`, `clear` support role-aware UI and service behavior |
| `src/main/java/com/aurafarming/service/AuthService.java` | Handles registration, login, logout, and admin seeding | Authentication service | `register`, `login`, `logout`, `ensureAdminSeed`, validation logic |
| `src/main/java/com/aurafarming/service/UserService.java` | Handles user listing, searching, updating, activation state changes | User management service | `findByRole`, `search`, `updateUser`, `deactivateUser`, `reactivateUser` |
| `src/main/java/com/aurafarming/service/FarmPlotService.java` | Handles farm and plot CRUD-like operations | Farm/plot service | `getFarmsForUser`, `getPlotsForFarm`, `createFarm`, `createPlot`, `deleteFarm` |
| `src/main/java/com/aurafarming/service/CropPlanService.java` | Handles recommendation scoring and crop plan save | Advisory service | `recommendationScore`, `savePlan`, `findAll` |
| `src/main/java/com/aurafarming/service/MarketPriceService.java` | Handles market price storage, history, and trend hints | Market service | `save`, `findHistory`, `trendHint` |
| `src/main/java/com/aurafarming/service/WeatherAlertService.java` | Handles weather alert creation and retrieval | Weather service | `save`, `findAll` |
| `src/main/java/com/aurafarming/service/YieldLogService.java` | Handles yield storage and visibility rules | Yield service | `save`, `findVisible`, `totalSeason`, `totalYear` |
| `src/main/java/com/aurafarming/service/AuditService.java` | Handles audit write and filtered retrieval | Audit service | `log`, `findAll`, `filter` |
| `src/main/java/com/aurafarming/service/ExportService.java` | Builds previews and writes CSV reports | Export service | `preview`, `export`, private builders per report type |
| `src/main/java/com/aurafarming/service/DashboardService.java` | Aggregates counts and chart data | Dashboard service | total users, yield totals, active-last-24-hours, chart series data |

### 6.9 Controller Files

| File path | Functional description | Technical role | How a developer traces execution |
|---|---|---|---|
| `src/main/java/com/aurafarming/controller/LoginController.java` | Handles login screen events | JavaFX controller | Reads email/password, calls `AuthService.login`, routes to dashboard |
| `src/main/java/com/aurafarming/controller/RegistrationController.java` | Handles registration form | JavaFX controller | Collects form values into `RegistrationDTO`, calls `AuthService.register`, routes back to login |
| `src/main/java/com/aurafarming/controller/DashboardController.java` | Controls main dashboard shell and module loading | JavaFX controller | Checks session, refreshes metrics/charts, hides some tabs by role, loads module FXML files dynamically |
| `src/main/java/com/aurafarming/controller/OfficerController.java` | Displays and searches officer users | JavaFX controller | Uses `UserService` to render officer records and deactivate first search match |
| `src/main/java/com/aurafarming/controller/FarmerController.java` | Displays and manages farmer users | JavaFX controller | Shows full search for admin, restricted view for non-admin, supports deactivate/reactivate |
| `src/main/java/com/aurafarming/controller/FarmPlotController.java` | Handles farm and plot creation UI | JavaFX controller | Builds combo-box lists, creates farms and plots, cascades farm deletion, renders current structure |
| `src/main/java/com/aurafarming/controller/CropPlanController.java` | Handles crop recommendation and plan save | JavaFX controller | Computes score, saves plan, and shows summary text |
| `src/main/java/com/aurafarming/controller/MarketPriceController.java` | Handles market price actions | JavaFX controller | Saves prices, retrieves history, updates trend label |
| `src/main/java/com/aurafarming/controller/WeatherAlertController.java` | Handles weather alert actions | JavaFX controller | Saves alerts and renders current alert list |
| `src/main/java/com/aurafarming/controller/YieldLogController.java` | Handles yield logging screen | JavaFX controller | Loads visible plots, autofills district from selected plot, saves yield, renders visible logs |
| `src/main/java/com/aurafarming/controller/AuditLogController.java` | Handles audit filtering screen | JavaFX controller | Applies date/user/action filters and prints matching logs |
| `src/main/java/com/aurafarming/controller/ExportController.java` | Handles preview and export UI | JavaFX controller | Restricts farmer report choices, builds `ExportRequestDTO`, calls `ExportService` |

### 6.10 View and Style Files

| File path | Functional description | Technical role | UI role in system flow |
|---|---|---|---|
| `src/main/resources/com/aurafarming/view/login.fxml` | Login screen | FXML view | Entry screen for existing users |
| `src/main/resources/com/aurafarming/view/registration.fxml` | Registration screen | FXML view | Entry screen for new farmer/officer users |
| `src/main/resources/com/aurafarming/view/dashboard.fxml` | Main shell with top tabs, cards, and charts | FXML view | Role-based landing page after login |
| `src/main/resources/com/aurafarming/view/officers.fxml` | Officer management screen | FXML view | Basic officer listing, search, and deactivation |
| `src/main/resources/com/aurafarming/view/farmers.fxml` | Farmer management screen | FXML view | Farmer listing and state change actions |
| `src/main/resources/com/aurafarming/view/farm-plots.fxml` | Farm and plot operations screen | FXML view | Farm creation, plot creation, farm deletion |
| `src/main/resources/com/aurafarming/view/crop-plan.fxml` | Crop advisory screen | FXML view | Recommendation and crop plan save flow |
| `src/main/resources/com/aurafarming/view/market-price.fxml` | Market price screen | FXML view | Price entry, trend view, and history |
| `src/main/resources/com/aurafarming/view/weather-alert.fxml` | Weather alert screen | FXML view | Alert entry and viewing |
| `src/main/resources/com/aurafarming/view/yield-log.fxml` | Yield logging screen | FXML view | Harvest log entry and display |
| `src/main/resources/com/aurafarming/view/audit-log.fxml` | Audit log screen | FXML view | Audit filtering and display |
| `src/main/resources/com/aurafarming/view/export.fxml` | Reporting/export screen | FXML view | Preview and file export controls |
| `src/main/resources/com/aurafarming/css/app.css` | Shared application styling | JavaFX CSS | Gives the app a consistent visual identity |

### 6.11 Functional Observations by File Group

These are important for presentation because they separate implemented behavior from intended behavior:

- The controllers mostly render results in `TextArea` controls, not tables.
- The module screens are mainly single-column `VBox` layouts with horizontal rows of controls, not true left-panel/right-panel master-detail layouts.
- `DashboardController` provides charts on screen, but `ExportService` does not export charts.
- `UserService.updateUser()` exists, but no controller currently drives a full update workflow.

## 7. Logical Flow of the System

### 7.1 End-to-End Startup and Authentication Flow

1. The program launches through the bootstrap classes.
2. The app creates `data storage` and `export results` if needed.
3. The login screen opens.
4. On login, `AuthService` loads users from `users.dat`.
5. If the default admin account does not exist yet, it is seeded automatically.
6. Credentials are validated.
7. `lastLoginAt` is updated and persisted.
8. `SessionContext` stores the logged-in user.
9. An audit log is written.
10. The dashboard opens with role-aware tab visibility.

### 7.2 Registration Flow

1. User enters full name, email, passwords, role, district, and phone.
2. `RegistrationController` packages values into `RegistrationDTO`.
3. `AuthService.validateRegistration()` checks required fields, matching passwords, basic email format, role validity, and farmer district requirement.
4. `UserDAO.findByEmail()` prevents duplicates.
5. A `Farmer` or `Officer` object is created.
6. The new user is stored in `users.dat`.
7. A registration audit entry is written.
8. The user is redirected back to login.

### 7.3 Dashboard Flow

1. `DashboardController.initialize()` checks whether a session exists.
2. `DashboardService` calculates:
   - total active farmers
   - total active officers
   - current season yield
   - current year yield
   - active users in last 24 hours
3. Chart datasets are built from user, farm, and yield files.
4. A default module is opened depending on role:
   - Farmer -> crop plan
   - Officer -> yield log
   - Admin -> farmers

### 7.4 Farm and Plot Flow

1. User opens farm and plot screen.
2. `FarmPlotController` loads district, unit, and tag options.
3. Existing farms visible to the current user are loaded.
4. Creating a farm calls `FarmPlotService.createFarm()`.
5. Creating a plot calls `FarmPlotService.createPlot()`.
6. Deleting a farm removes the farm and all plots under that farm.
7. The output area redraws the current farm -> plot structure.

### 7.5 Crop Planning Flow

1. The current user's visible plots are loaded into the plot combo box.
2. The user selects crop, season, start date, and expected harvest date.
3. `onRecommend()` calls `CropPlanService.recommendationScore()`.
4. The score is shown as Good, Moderate, or Poor.
5. Saving creates a `CropPlan`, persists it, and writes an audit log.

### 7.6 Market Price Flow

1. User selects district and crop.
2. Officer/Admin can enter price per kg and save.
3. `MarketPriceService.save()` stores the record with today's date.
4. `findHistory()` retrieves matching historical records.
5. `trendHint()` compares the latest two prices for the same district and crop.
6. The controller prints history lines into the output area.

### 7.7 Weather Alert Flow

1. User selects district, weather condition, severity, duration, and probability.
2. Officer/Admin can save the alert.
3. `WeatherAlertService.save()` persists the alert and writes an audit entry.
4. The alert list is sorted newest first and printed in the output area.

### 7.8 Yield Log Flow

1. The controller loads plots available to the current user.
2. When a plot is selected, the district is inferred from the parent farm.
3. User enters estimated and actual kilograms and a harvest date.
4. `YieldLogService.save()` persists the log and writes an audit entry.
5. `YieldLogService.findVisible()` controls visibility:
   - Admin/Officer see all logs
   - Farmer sees only own logs
6. Dashboard totals later aggregate these logs by season and year.

### 7.9 Audit Flow

1. Services call `AuditService.log()` after major actions.
2. Logs are stored as serialized `AuditLog` objects.
3. `AuditLogController` filters logs by actor keyword, action, and date range.
4. For non-admin viewers, `AuditService.filter()` only exposes actions containing `login` or `yield`.

### 7.10 Export Flow

1. User selects report type and optional date range/farmer ID.
2. `ExportController` builds `ExportRequestDTO`.
3. `ExportService.preview()` or `export()` validates role-based export restrictions.
4. The service generates CSV lines for the chosen report.
5. On export, the lines are written to `export results/<report>_<timestamp>.csv`.
6. An export audit record is written.

## 8. Design Pattern and Principle Analysis

### 8.1 Overall Architectural Style

The project is not a strict textbook MVC system. It is more accurate to call it **MVC with additional service and DAO layers**.

That matters because:

- the **view** is still separate in FXML
- the **controller** still handles UI events
- the **model** still represents domain state
- but core business logic is intentionally pushed into services
- and storage is intentionally pushed into DAOs

This is a good coursework design because it separates responsibilities more clearly than a controller-heavy desktop app.

### 8.2 MVC in This Project

**Model**

- Implemented by classes in `com.aurafarming.model`
- Examples: `User`, `Farmer`, `Farm`, `CropPlan`, `MarketPrice`
- These classes store the system's persistent state

**View**

- Implemented by FXML files in `src/main/resources/com/aurafarming/view`
- Examples: `login.fxml`, `dashboard.fxml`, `market-price.fxml`
- These files define forms, labels, buttons, charts, and layout

**Controller**

- Implemented by classes in `com.aurafarming.controller`
- Examples: `LoginController`, `DashboardController`, `YieldLogController`
- Controllers read user input, call services, and update UI output

**How user actions move through MVC layers**

Example: login

1. User types credentials in `login.fxml`
2. `LoginController.onLogin()` fires
3. `AuthService.login()` validates credentials
4. `UserDAO` loads user data from `users.dat`
5. On success, `SceneRouter` opens the dashboard

**Conclusion**

The design is **partially MVC and strongly layer-separated**, not minimal MVC. That is a positive design choice for maintainability.

### 8.3 DAO Pattern in This Project

The DAO pattern is clearly implemented and is important because the assignment forbids database usage.

**Purpose of DAO here**

- isolate file persistence from UI and business logic
- provide reusable data access per entity
- allow services to work with objects instead of raw file streams

**How DAO is used here**

- `BaseObjectDAO<T>` contains shared serialization logic
- each entity DAO wraps one file and exposes domain-specific read/write methods
- services call DAOs, not the controllers

**What problem it solves**

Without DAOs, controllers or services would need to open files directly, duplicate serialization logic, and mix storage details with business logic.

### 8.4 DTO Pattern in This Project

The DTO pattern is present, but lightly used.

**Purpose of DTO here**

- package user input cleanly
- reduce long parameter lists
- separate raw form input from entity construction

**Examples**

- `LoginDTO` for login form input
- `RegistrationDTO` for registration data
- `ExportRequestDTO` for export options

**Difference from DAO**

- DAO handles persistence
- DTO carries data between layers

**Project-specific note**

DTO usage is strongest in authentication and export flows. Other modules still pass primitive values directly from controllers to services.

### 8.5 Inheritance

Inheritance is clearly implemented through:

- `User` -> `Admin`
- `User` -> `Officer`
- `User` -> `Farmer`

**Why it exists**

All users share identity and authentication fields, but different roles may have their own extra state.

**Why it matters in flow**

Authentication and session handling can work with a `User` reference, while role-specific data still exists in the underlying subclass object.

### 8.6 Polymorphism

Polymorphism is present, but mostly in a practical rather than advanced form.

**Concrete project use**

- `UserDAO` stores a `List<User>` that may contain `Admin`, `Officer`, or `Farmer`
- `SessionContext` and many services accept a `User`
- role-specific behavior is selected at runtime using the logged-in user's role

**Important nuance**

This project uses **role-based conditional polymorphism** more than overridden behavior. In other words, it relies more on `if (user.getRole() == ...)` than on subclass method overriding.

### 8.7 Abstraction

Abstraction is implemented through `abstract class User`.

**Why it matters**

The project communicates that a generic user exists conceptually, but direct users are always specific concrete roles.

This is a good match for the problem domain.

### 8.8 Encapsulation

Encapsulation is used consistently in the model layer:

- fields are private
- state is accessed through getters and setters
- services mediate many operations instead of letting controllers mutate files directly

This protects the domain structure and keeps responsibilities clearer.

### 8.9 Composition

Composition is used heavily:

- controllers contain service objects
- services contain DAO objects
- `DashboardService` composes several data sources to calculate metrics

This matters because most real behavior in the system happens through collaboration between objects rather than inheritance alone.

### 8.10 Interfaces

The source does not define custom business interfaces such as `UserRepository` or `Exportable`.

What is confirmed:

- serializable model classes implement Java's `Serializable` interface

What is not confirmed:

- I do not have enough context to confirm any deliberate custom interface-based design beyond `Serializable`, because none appears in the source files.

## 9. Constraint Handling

### 9.1 Constraints Satisfied

| Constraint | How the project handles it |
|---|---|
| No database | Uses object serialization to `.dat` files |
| Offline-first | No network or web service dependencies in source |
| Desktop GUI | Built with JavaFX and FXML |
| OOP requirement | Uses classes, inheritance, abstraction, encapsulation, enums, services, DAOs |
| DAO redesign for local files | `BaseObjectDAO` and entity-specific DAOs handle local persistence |
| MVC/DTO usage | Present in layered form |

### 9.2 Constraints Only Partially Satisfied

| Assignment or note expectation | Current state |
|---|---|
| Backup and restore | No dedicated implementation found |
| Weather data loaded from files | Alerts are manually entered and stored, not imported from a weather file |
| Full user management with credential changes and role changes | Only deactivate/reactivate and generic update service exist; no full UI workflow |
| Reporting and export with charts/graphs | Dashboard shows charts, but export writes CSV only |
| Officer farmer-management depth from notes | Current officer behavior is weaker than described |

### 9.3 Field-Level and Workflow Validation

Confirmed validation rules:

- registration requires non-blank name, email, password, and confirmation
- passwords must match
- email must contain `@` and `.`
- admin cannot be registered from the registration screen
- farmer registration requires district
- farm creation requires district, unit, and positive area
- plot creation requires selected farm and positive area
- farmer export is restricted to plot and crop-plan reports only
- farmer export cannot target another farmer's ID

### 9.4 Missing or Inconsistent Validation

These are important to mention honestly in presentation:

- no password strength rule
- no validation that crop plan harvest date is after start date
- no validation that yield actual/estimated values are non-negative inside `YieldLogService`
- no validation that weather probability is within 0-100
- no validation that market price is positive inside `MarketPriceService`
- no validation that selected farmer ID actually owns the chosen plot when admin/officer saves crop plans or yield logs

## 10. Implementation Walkthrough

### 10.1 Authentication and Session Management

The system begins with user identity because every later module depends on role. `AuthService` is therefore one of the most important classes in the project.

Internally it:

- ensures a default admin exists
- reads users from file
- validates credentials
- checks active status
- updates last login time
- stores the logged-in user in `SessionContext`
- writes audit records for success, failure, and logout

This flow demonstrates how the system centralizes authentication rather than scattering it across screens.

### 10.2 Dashboard Aggregation

`DashboardService` does not store data itself. Instead, it reads multiple persisted lists and calculates:

- counts of active farmers and officers
- yield totals for the current year and a quarter-based "season"
- users active within the last 24 hours
- chart-ready summaries by role and district

This class exists to keep summary logic out of the controller.

### 10.3 User Management

User management is split between authentication and administrative operations:

- `AuthService` handles creation and login
- `UserService` handles search, update, deactivate, and reactivate
- `OfficerController` and `FarmerController` present the UI

Technically, user deactivation is a **soft delete**, because the user object remains in storage and only `active` changes. That is an important design decision because it preserves auditability and allows reactivation.

### 10.4 Farm and Plot Management

Farm and plot management models one-to-many ownership:

- one farmer can have many farms
- one farm can have many plots

`FarmPlotService` uses this relationship to decide what the current user can see. A farmer sees only own farms and plots. Admin and officer see all farms and plots.

`deleteFarm()` also removes child plots. This is effectively a manual cascade delete implemented in service logic.

### 10.5 Crop Planning and Advisory

The crop plan module combines domain data with rule-based scoring.

The advisory score is not machine learning. It is a simple deterministic rule:

- rice scores highest in monsoon
- wheat and mustard score highest in winter
- maize scores highest in spring
- vegetables score highest in summer
- millet scores highest in autumn

This matters in presentation because it shows how the system provides advisory support while staying within the assignment scope of basic rule-based analytics.

### 10.6 Market Price Management

Market prices are stored as historical entries, not as a single mutable "latest price" row.

That design choice matters because it supports:

- date-range filtering
- trend comparison between the last two entries for a district and crop
- exporting historical records later

The implementation is simple but logically correct for a history-driven module.

### 10.7 Weather Risk Alerts

The weather module creates alerts manually and stores them with:

- district
- weather condition
- severity
- duration in days
- probability percentage
- created date

This module is operationally useful, but it does not currently read from an external weather data file. That difference should be stated clearly if asked.

### 10.8 Yield Logging

Yield logging is important because it supports both operational history and dashboard metrics.

The module records:

- farmer ID
- plot ID
- district
- crop type
- estimated kilograms
- actual kilograms
- harvest date

The dashboard later reuses this same file for seasonal and yearly totals. This is a good example of module reuse through shared persisted data.

### 10.9 Audit Logging

Audit logging is cross-cutting. It is not a separate end-user workflow only; it supports accountability across the whole system.

Each important save, login, logout, export, activation change, or similar action can add an `AuditLog` entry. This means the audit module exists to answer the question: "who did what, to which target, and when?"

### 10.10 Export and Reporting

`ExportService` demonstrates how the system converts domain objects into presentation data. It supports:

- audit CSV
- yield CSV
- market CSV
- crop-plan CSV
- plot CSV

Farmers are intentionally restricted so they cannot export broader cooperative data. That is a concrete example of role-based authorization implemented in the service layer, not just hidden in the UI.

## 11. Important Code Excerpts

### 11.1 Application Startup

This excerpt shows how the project guarantees local folders exist before loading the first screen.

```java
public void start(Stage stage) {
    FileUtil.ensureAppDirectories();
    SceneRouter.initialize(stage);
    SceneRouter.goTo("/com/aurafarming/view/login.fxml", "AuraFarming - Login");
}
```

Why it matters:

- prevents file path failures later
- centralizes first-screen routing

### 11.2 Generic File-Based DAO Logic

This excerpt is the core of local persistence.

```java
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
```

Why it matters:

- proves the app uses object serialization
- shows how empty-file startup is handled safely

### 11.3 Crop Recommendation Rule

This excerpt is the advisory engine.

```java
return switch (season) {
    case MONSOON -> crop.contains("rice") ? 90 : 45;
    case WINTER -> crop.contains("wheat") || crop.contains("mustard") ? 88 : 40;
    case SPRING -> crop.contains("maize") ? 82 : 55;
    case SUMMER -> crop.contains("vegetable") ? 80 : 50;
    case AUTUMN -> crop.contains("millet") ? 78 : 52;
};
```

Why it matters:

- shows rule-based analytics as requested by assignment
- explains how "recommendation score" is produced

### 11.4 Role-Based Yield Visibility

This excerpt demonstrates authorization in the service layer.

```java
public List<YieldLog> findVisible(User user) {
    if (user.getRole() == Role.ADMIN || user.getRole() == Role.OFFICER) {
        return yieldLogDAO.findAll();
    }
    return yieldLogDAO.findAll().stream()
            .filter(y -> y.getFarmerId().equalsIgnoreCase(user.getUserId()))
            .collect(Collectors.toList());
}
```

Why it matters:

- admins and officers can review the cooperative's harvest data
- farmers are restricted to their own records

### 11.5 Role-Restricted Export

This excerpt shows export enforcement beyond UI hiding.

```java
if (actor.getRole() == Role.FARMER) {
    if (!requestedType.equals("plot") && !requestedType.equals("crop-plan")) {
        throw new ValidationException("Farmers can export only Plot and Crop Plan reports.");
    }
    if (effectiveFarmerId != null && !effectiveFarmerId.isBlank()
            && !effectiveFarmerId.equalsIgnoreCase(actor.getUserId())) {
        throw new ValidationException("Farmers can export only their own records.");
    }
}
```

Why it matters:

- proves security is enforced in business logic, not only in interface visibility

## 12. Common Questions for Presentation

### 12.1 What does this project do?

Answer outline: It is an offline JavaFX desktop system for a Nepal agricultural cooperative. It manages users, farms, plots, crop plans, market prices, weather alerts, yield logs, audit logs, and CSV exports using local file persistence instead of a database.

### 12.2 Why did you use JavaFX instead of a console app?

Answer outline: The assignment explicitly asks for a desktop GUI using JavaFX or Swing. JavaFX was chosen because it supports FXML, charts, CSS styling, and cleaner separation of view and controller logic.

### 12.3 Why did you not use a database?

Answer outline: The assignment forbids database usage. To comply, the project uses object serialization through DAOs that read and write `.dat` files locally.

### 12.4 Is this a real MVC project?

Answer outline: It is MVC-inspired with extra layers. The FXML files are the views, the controller classes handle events, the model classes store domain state, and business logic is pushed into services while persistence is pushed into DAOs.

### 12.5 Where is the DAO pattern used?

Answer outline: Every major persisted entity has a DAO, and all DAOs inherit shared serialization logic from `BaseObjectDAO`. That redesign is necessary because this assignment uses files instead of SQL tables.

### 12.6 Where is the DTO pattern used?

Answer outline: DTOs are used for login, registration, and export requests. They package form input cleanly before services create or process domain objects.

### 12.7 How is inheritance used?

Answer outline: `User` is the abstract base class, and `Admin`, `Officer`, and `Farmer` extend it. Shared user state is centralized, while role-specific state stays in subclasses.

### 12.8 How is polymorphism used?

Answer outline: Many services and DAOs work with `User` references that can actually be `Admin`, `Officer`, or `Farmer` at runtime. The code then applies role-aware behavior based on the current object's role.

### 12.9 How does the crop advisory work?

Answer outline: It is a rule-based scoring system, not AI. The service compares crop type and season and returns a score, then the controller translates that into a badge like Good, Moderate, or Poor.

### 12.10 How is data persisted?

Answer outline: DAOs read and write whole lists of serializable objects to local `.dat` files. Missing files are created automatically, and empty files return empty lists.

### 12.11 What happens if a file is missing or empty?

Answer outline: `FileUtil` creates missing files and folders. `BaseObjectDAO` returns an empty list for empty files or EOF. Corrupted or incompatible files currently throw a runtime exception instead of being automatically repaired.

### 12.12 How are exceptions handled?

Answer outline: The project uses `ValidationException` and `AuthenticationException` for business errors. Some controllers catch exceptions and show alerts, but validation and error handling are not fully consistent in every module.

### 12.13 What are the strongest design decisions in this project?

Answer outline: clear package separation, file-based DAO design, abstract user hierarchy, centralized session handling, and dashboard aggregation through a service layer.

### 12.14 What parts are incomplete or weaker than the assignment ideal?

Answer outline: backup/restore is missing, weather import from files is not implemented, chart export is missing, full user-edit flows are incomplete, and some officer capabilities described in notes are only partially present in code.

### 12.15 How would you improve this system next?

Answer outline: add stronger validation, add plot update/delete operations, implement backup/restore, implement full user update and role-change flows, add tests, hash passwords, and improve export filtering by district and crop.

## 13. Glossary

**Abstraction** - Representing a general concept while hiding unnecessary details. In this project, `User` is an abstract base user concept.

**Audit log** - A historical record of actions taken in the system, including who did what and when.

**Cascade delete** - When deleting one record also deletes related child records. Here, deleting a farm also deletes its plots.

**Controller** - The class that handles UI events and coordinates with services.

**CSV** - Comma-separated values file format used here for exports.

**DAO (Data Access Object)** - A class dedicated to reading and writing persisted data. In this project, DAOs work with local files.

**DTO (Data Transfer Object)** - A small data carrier used to move form data between layers.

**Encapsulation** - Keeping object fields private and controlling access through methods.

**Enum** - A fixed set of constants. Examples here include `Role`, `District`, `Season`, `Severity`, and `WeatherCondition`.

**FXML** - XML markup used to define JavaFX screen layouts.

**Inheritance** - Creating specialized subclasses from a shared parent class. Example: `Farmer` extends `User`.

**JavaFX** - Java's GUI framework used to build the desktop interface.

**MVC** - Model-View-Controller pattern. Here it is implemented with extra service and DAO layers.

**Object serialization** - Storing Java objects directly in binary form for later loading.

**Polymorphism** - Treating different subclass objects through a shared parent type, such as using `User` references for admins, officers, and farmers.

**Role-based access control** - Restricting system actions depending on the current user's role.

**Service layer** - A layer that contains business rules, validation, authorization, and workflow logic.

**Soft delete** - Marking a record inactive instead of physically removing it.

## 14. Appendix

### 14.1 Evidence Basis Used for This Report

This report is grounded in:

- `ap_question.txt`
- `ProjectDetailByUser.txt`
- `pom.xml`
- all source files under `src/main/java`
- all FXML and CSS files under `src/main/resources`
- the observed contents of `data storage` and `export results`

### 14.2 Observed Runtime Data Files

At the time of inspection, the `data storage` directory contained:

- `audit_logs.dat`
- `farms.dat`
- `plots.dat`
- `users.dat`
- `yield_logs.dat`

Files defined in `Constants.java` but not yet present at inspection time:

- `crop_plans.dat`
- `market_prices.dat`
- `weather_alerts.dat`

That absence is understandable because files are created lazily when a DAO first resolves them.

### 14.3 File Handling Analysis

**What files are read or written**

- user records -> `users.dat`
- farms -> `farms.dat`
- plots -> `plots.dat`
- crop plans -> `crop_plans.dat`
- market prices -> `market_prices.dat`
- weather alerts -> `weather_alerts.dat`
- yield logs -> `yield_logs.dat`
- audit logs -> `audit_logs.dat`
- exports -> timestamped CSV files in `export results`

**Format used**

- binary serialized Java object lists for `.dat` files
- plain CSV text for exported reports

**How persistence is ensured**

- each save reads current list, mutates in memory, and writes back whole list
- models declare `serialVersionUID`
- directories and files are created on demand

**What happens on file problems**

- missing file -> created automatically
- empty file -> treated as empty list
- EOF while reading -> treated as empty list
- corrupted or incompatible file -> runtime exception

### 14.4 Exception Handling Analysis

**Where exceptions may occur**

- invalid login credentials
- invalid registration data
- invalid numeric parsing in farm, price, weather, and yield forms
- file read/write errors
- scene loading failures

**How they are handled**

- login, registration, preview, and export controllers catch exceptions and show alerts
- some other modules do not locally catch parsing errors
- DAO file failures become runtime exceptions

**Stability conclusion**

The system has a basic exception strategy, but it is not uniformly defensive across all UI actions. Invalid numeric input is a likely live-demo risk.

### 14.5 Presentation Script Outline

Suggested 5-part speaking flow:

1. Start with the problem: rural cooperatives need offline record management without SQL or internet.
2. Explain the architecture: JavaFX UI, controller layer, service layer, DAO layer, serialized files.
3. Walk through one role-based scenario: login -> dashboard -> farm/plot -> crop plan -> yield -> export.
4. Highlight software engineering concepts: MVC, DAO, DTO, inheritance, abstraction, encapsulation, role-based control.
5. End with honest evaluation: strong offline modular structure, but backup/restore and some advanced management features are still incomplete.

### 14.6 Mapping Notes to Source Reality

These differences are useful to state proactively if questioned:

| Note or expectation | What source code actually shows |
|---|---|
| Officer can fully manage farmers | Officer can access some operational modules, but farmer management UI behavior is limited |
| Full left/right split scene layout | Most module screens are simple VBox screens with control rows and text output |
| Charts, pie charts, graphs export | Dashboard displays charts, but export produces CSV files only |
| Weather data loaded from files | Alerts are manually entered and stored locally |
| Backup and restore module | Not implemented in source |
| JavaFX 25.0.0 | `pom.xml` confirms JavaFX 21.0.6 |

### 14.7 Verification Notes

- Source inspection completed across assignment notes, Java classes, FXML views, CSS, and runtime folders.
- Build verification was attempted with `.\mvnw.cmd -DskipTests compile`, but it could not run because `JAVA_HOME` is not set in the environment.
- No automated test suite was found in the repository.
