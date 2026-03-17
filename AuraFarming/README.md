# AuraFarming (Prototype) - Project Documentation

## 1. Project Introduction
AuraFarming is a JavaFX desktop prototype for offline cooperative agriculture management in Nepal context.
It implements role-based operations for Admin, Officer, and Farmer, with local file persistence using object serialization (no database), and follows MVC + DAO + DTO patterns.

Core goals:
- Provide safe local record management for users, farms, plots, crop plans, market prices, weather alerts, yield logs, and audit logs.
- Support role-based responsibility and visibility.
- Keep the system simple and practical for coursework scope.

## 2. Prototype Credentials
This is a prototype and seeds one admin account automatically.

- Admin Email: admin@control.com
- Admin Password: admin123

Notes:
- Admin is predefined and cannot be created via registration screen.
- Farmer and Officer accounts are created from the registration flow.

## 3. Why This Structure Was Chosen
This repository is organized by architectural responsibility to improve maintainability, testing, and team collaboration:

- Model: domain entities and enums.
- DTO: form/request payloads between UI and services.
- DAO: file-level persistence operations.
- Service: business rules and authorization.
- Controller: JavaFX scene event handling.
- View/CSS: visual layout and styling.

Reason to do so:
- Clear separation of concerns.
- Easy onboarding for new contributors.
- Easier mapping from assignment analysis/design to implementation.
- Safer evolution of features without mixing UI/data logic.

## 4. What ap_question.txt Asked For (Summary)
The file [ap_question.txt](ap_question.txt) contains the assignment scenario and constraints for the Smart AgriNepal Cooperative Management System.
In short, it requires:

- Offline-first desktop application.
- File-based persistence only (no SQL/cloud/network storage).
- Role-based modules (Admin, Officer, Farmer).
- OOP concepts and design patterns (MVC, DAO, DTO).
- Analysis + design deliverables (Part A) and implementation deliverables (Part B).
- Agricultural modules including user management, farm/plot, crop planning, market price, weather alerts, reporting/export, and audit/backup concerns.

## 5. Quick Start (Run/Build)
1. Ensure JDK 21 is available and JAVA_HOME is set.
2. Compile:
   - Windows PowerShell: .\mvnw.cmd -DskipTests compile
3. Run with JavaFX plugin:
   - .\mvnw.cmd javafx:run
4. Build package:
   - .\mvnw.cmd -DskipTests package

Important:
- This project also includes a runnable shaded jar build profile in pom.
- For production-like JavaFX distribution, prefer jlink image/zip from JavaFX plugin.

## 6. Reference Documents
1. [documentation.md](documentation.md)
   - Detailed system definition, module intent, pattern mapping, and assignment alignment.
2. [ProjectDetailByUser.txt](ProjectDetailByUser.txt)
   - User-provided practical role behavior and UI/feature expectations.
3. [execution steps/step1.md](execution%20steps/step1.md) ... [execution steps/step14.md](execution%20steps/step14.md)
   - Incremental implementation guidance from setup to finishing steps.
4. [ap_question.txt](ap_question.txt)
   - Source assignment brief and constraints.

## 7. Package and File Documentation (Numerical Hierarchy)

### 1) Package: src/main/java/com/aurafarming (Application Bootstrap)
1.1 [src/main/java/com/aurafarming/AuraFarmingApplication.java](src/main/java/com/aurafarming/AuraFarmingApplication.java)
- JavaFX application entry; initializes directories and scene router.

1.2 [src/main/java/com/aurafarming/AuraFarmingLauncher.java](src/main/java/com/aurafarming/AuraFarmingLauncher.java)
- Launcher class for JavaFX startup wiring.

1.3 [src/main/java/com/aurafarming/AuraFarmingJarBootstrap.java](src/main/java/com/aurafarming/AuraFarmingJarBootstrap.java)
- Plain main bootstrap for runnable jar manifest entry.

1.4 [src/main/java/module-info.java](src/main/java/module-info.java)
- Java module declarations and exported/opened packages.

### 2) Package: src/main/java/com/aurafarming/util
2.1 [src/main/java/com/aurafarming/util/Constants.java](src/main/java/com/aurafarming/util/Constants.java)
- Global constants: admin seed credentials, file names, date formatters.

2.2 [src/main/java/com/aurafarming/util/FileUtil.java](src/main/java/com/aurafarming/util/FileUtil.java)
- Creates/locates data and export directories/files.

2.3 [src/main/java/com/aurafarming/util/IdGenerator.java](src/main/java/com/aurafarming/util/IdGenerator.java)
- Generates IDs for users and domain records.

2.4 [src/main/java/com/aurafarming/util/SceneRouter.java](src/main/java/com/aurafarming/util/SceneRouter.java)
- Central scene navigation and stylesheet loading.

2.5 [src/main/java/com/aurafarming/util/AlertUtil.java](src/main/java/com/aurafarming/util/AlertUtil.java)
- Standard JavaFX alert popups for info/error flows.

### 3) Package: src/main/java/com/aurafarming/model
3.1 [src/main/java/com/aurafarming/model/User.java](src/main/java/com/aurafarming/model/User.java)
- Base abstract user model for all roles.

3.2 [src/main/java/com/aurafarming/model/Admin.java](src/main/java/com/aurafarming/model/Admin.java)
- Admin subtype.

3.3 [src/main/java/com/aurafarming/model/Officer.java](src/main/java/com/aurafarming/model/Officer.java)
- Officer subtype.

3.4 [src/main/java/com/aurafarming/model/Farmer.java](src/main/java/com/aurafarming/model/Farmer.java)
- Farmer subtype with district/phone data.

3.5 [src/main/java/com/aurafarming/model/Farm.java](src/main/java/com/aurafarming/model/Farm.java)
- Farm record, district/unit/area/tag and farmer link.

3.6 [src/main/java/com/aurafarming/model/Plot.java](src/main/java/com/aurafarming/model/Plot.java)
- Plot record belonging to a farm.

3.7 [src/main/java/com/aurafarming/model/CropPlan.java](src/main/java/com/aurafarming/model/CropPlan.java)
- Crop planning record by season/plot/farmer.

3.8 [src/main/java/com/aurafarming/model/MarketPrice.java](src/main/java/com/aurafarming/model/MarketPrice.java)
- District crop price history entries.

3.9 [src/main/java/com/aurafarming/model/WeatherAlert.java](src/main/java/com/aurafarming/model/WeatherAlert.java)
- Weather risk alert entries.

3.10 [src/main/java/com/aurafarming/model/YieldLog.java](src/main/java/com/aurafarming/model/YieldLog.java)
- Harvest estimated vs actual logging.

3.11 [src/main/java/com/aurafarming/model/AuditLog.java](src/main/java/com/aurafarming/model/AuditLog.java)
- Append-style activity audit record.

3.12 [src/main/java/com/aurafarming/model/Role.java](src/main/java/com/aurafarming/model/Role.java)
- Roles enum: ADMIN, OFFICER, FARMER.

3.13 [src/main/java/com/aurafarming/model/District.java](src/main/java/com/aurafarming/model/District.java)
- District enum for cooperative coverage.

3.14 [src/main/java/com/aurafarming/model/Season.java](src/main/java/com/aurafarming/model/Season.java)
- Nepali season display labels with English in brackets.

3.15 [src/main/java/com/aurafarming/model/Severity.java](src/main/java/com/aurafarming/model/Severity.java)
- Severity enum for alerts.

3.16 [src/main/java/com/aurafarming/model/WeatherCondition.java](src/main/java/com/aurafarming/model/WeatherCondition.java)
- Weather condition enum.

### 4) Package: src/main/java/com/aurafarming/dto
4.1 [src/main/java/com/aurafarming/dto/LoginDTO.java](src/main/java/com/aurafarming/dto/LoginDTO.java)
- Login input transfer object.

4.2 [src/main/java/com/aurafarming/dto/RegistrationDTO.java](src/main/java/com/aurafarming/dto/RegistrationDTO.java)
- Registration input transfer object.

4.3 [src/main/java/com/aurafarming/dto/ExportRequestDTO.java](src/main/java/com/aurafarming/dto/ExportRequestDTO.java)
- Export filter/request payload.

### 5) Package: src/main/java/com/aurafarming/dao
5.1 [src/main/java/com/aurafarming/dao/BaseObjectDAO.java](src/main/java/com/aurafarming/dao/BaseObjectDAO.java)
- Generic object-serialization read/write base.

5.2 [src/main/java/com/aurafarming/dao/UserDAO.java](src/main/java/com/aurafarming/dao/UserDAO.java)
- User persistence and lookup.

5.3 [src/main/java/com/aurafarming/dao/FarmDAO.java](src/main/java/com/aurafarming/dao/FarmDAO.java)
- Farm persistence.

5.4 [src/main/java/com/aurafarming/dao/PlotDAO.java](src/main/java/com/aurafarming/dao/PlotDAO.java)
- Plot persistence.

5.5 [src/main/java/com/aurafarming/dao/CropPlanDAO.java](src/main/java/com/aurafarming/dao/CropPlanDAO.java)
- Crop plan persistence.

5.6 [src/main/java/com/aurafarming/dao/MarketPriceDAO.java](src/main/java/com/aurafarming/dao/MarketPriceDAO.java)
- Market price persistence.

5.7 [src/main/java/com/aurafarming/dao/WeatherAlertDAO.java](src/main/java/com/aurafarming/dao/WeatherAlertDAO.java)
- Weather alert persistence.

5.8 [src/main/java/com/aurafarming/dao/YieldLogDAO.java](src/main/java/com/aurafarming/dao/YieldLogDAO.java)
- Yield log persistence.

5.9 [src/main/java/com/aurafarming/dao/AuditLogDAO.java](src/main/java/com/aurafarming/dao/AuditLogDAO.java)
- Audit log persistence.

### 6) Package: src/main/java/com/aurafarming/service
6.1 [src/main/java/com/aurafarming/service/AuthService.java](src/main/java/com/aurafarming/service/AuthService.java)
- Registration/login/logout, admin seed, auth validations.

6.2 [src/main/java/com/aurafarming/service/SessionContext.java](src/main/java/com/aurafarming/service/SessionContext.java)
- In-memory current-user session holder.

6.3 [src/main/java/com/aurafarming/service/UserService.java](src/main/java/com/aurafarming/service/UserService.java)
- User search/update/deactivate/reactivate operations.

6.4 [src/main/java/com/aurafarming/service/FarmPlotService.java](src/main/java/com/aurafarming/service/FarmPlotService.java)
- Farm/plot CRUD, ownership filtering, dropdown support.

6.5 [src/main/java/com/aurafarming/service/CropPlanService.java](src/main/java/com/aurafarming/service/CropPlanService.java)
- Crop recommendation scoring and crop plan persistence.

6.6 [src/main/java/com/aurafarming/service/MarketPriceService.java](src/main/java/com/aurafarming/service/MarketPriceService.java)
- Price save/history/compare operations.

6.7 [src/main/java/com/aurafarming/service/WeatherAlertService.java](src/main/java/com/aurafarming/service/WeatherAlertService.java)
- Alert creation/filtering based on role.

6.8 [src/main/java/com/aurafarming/service/YieldLogService.java](src/main/java/com/aurafarming/service/YieldLogService.java)
- Yield save/visibility and seasonal/year totals.

6.9 [src/main/java/com/aurafarming/service/AuditService.java](src/main/java/com/aurafarming/service/AuditService.java)
- Activity logging and filtered retrieval.

6.10 [src/main/java/com/aurafarming/service/ExportService.java](src/main/java/com/aurafarming/service/ExportService.java)
- Preview/export generation with role restrictions.

6.11 [src/main/java/com/aurafarming/service/DashboardService.java](src/main/java/com/aurafarming/service/DashboardService.java)
- Dashboard metrics and chart aggregations.

### 7) Package: src/main/java/com/aurafarming/controller
7.1 [src/main/java/com/aurafarming/controller/RegistrationController.java](src/main/java/com/aurafarming/controller/RegistrationController.java)
- Registration form controller.

7.2 [src/main/java/com/aurafarming/controller/LoginController.java](src/main/java/com/aurafarming/controller/LoginController.java)
- Login controller and dashboard redirection.

7.3 [src/main/java/com/aurafarming/controller/DashboardController.java](src/main/java/com/aurafarming/controller/DashboardController.java)
- Main shell tabs, role-based landing, metrics/charts refresh.

7.4 [src/main/java/com/aurafarming/controller/OfficerController.java](src/main/java/com/aurafarming/controller/OfficerController.java)
- Officer management module actions.

7.5 [src/main/java/com/aurafarming/controller/FarmerController.java](src/main/java/com/aurafarming/controller/FarmerController.java)
- Farmer management module actions with role limits.

7.6 [src/main/java/com/aurafarming/controller/FarmPlotController.java](src/main/java/com/aurafarming/controller/FarmPlotController.java)
- Farm and plot create/list/delete with combo selections.

7.7 [src/main/java/com/aurafarming/controller/CropPlanController.java](src/main/java/com/aurafarming/controller/CropPlanController.java)
- Crop planning form, recommendation score, save.

7.8 [src/main/java/com/aurafarming/controller/MarketPriceController.java](src/main/java/com/aurafarming/controller/MarketPriceController.java)
- Price save/view/compare/history actions.

7.9 [src/main/java/com/aurafarming/controller/WeatherAlertController.java](src/main/java/com/aurafarming/controller/WeatherAlertController.java)
- Weather alert create/list actions.

7.10 [src/main/java/com/aurafarming/controller/YieldLogController.java](src/main/java/com/aurafarming/controller/YieldLogController.java)
- Yield logging and role-filtered display.

7.11 [src/main/java/com/aurafarming/controller/AuditLogController.java](src/main/java/com/aurafarming/controller/AuditLogController.java)
- Audit filtering and display.

7.12 [src/main/java/com/aurafarming/controller/ExportController.java](src/main/java/com/aurafarming/controller/ExportController.java)
- Export preview and file export interactions.

### 8) Package: src/main/java/com/aurafarming/exception
8.1 [src/main/java/com/aurafarming/exception/AuthenticationException.java](src/main/java/com/aurafarming/exception/AuthenticationException.java)
- Authentication failure exception.

8.2 [src/main/java/com/aurafarming/exception/ValidationException.java](src/main/java/com/aurafarming/exception/ValidationException.java)
- Input/business rule validation exception.

### 9) Package: src/main/resources/com/aurafarming/view (FXML Screens)
9.1 [src/main/resources/com/aurafarming/view/login.fxml](src/main/resources/com/aurafarming/view/login.fxml)
- Login screen.

9.2 [src/main/resources/com/aurafarming/view/registration.fxml](src/main/resources/com/aurafarming/view/registration.fxml)
- Registration screen.

9.3 [src/main/resources/com/aurafarming/view/dashboard.fxml](src/main/resources/com/aurafarming/view/dashboard.fxml)
- Main dashboard shell with top navigation and charts.

9.4 [src/main/resources/com/aurafarming/view/officers.fxml](src/main/resources/com/aurafarming/view/officers.fxml)
- Officer management scene.

9.5 [src/main/resources/com/aurafarming/view/farmers.fxml](src/main/resources/com/aurafarming/view/farmers.fxml)
- Farmer management scene.

9.6 [src/main/resources/com/aurafarming/view/farm-plots.fxml](src/main/resources/com/aurafarming/view/farm-plots.fxml)
- Farm and plot operations scene.

9.7 [src/main/resources/com/aurafarming/view/crop-plan.fxml](src/main/resources/com/aurafarming/view/crop-plan.fxml)
- Crop planning scene.

9.8 [src/main/resources/com/aurafarming/view/market-price.fxml](src/main/resources/com/aurafarming/view/market-price.fxml)
- Market price scene.

9.9 [src/main/resources/com/aurafarming/view/weather-alert.fxml](src/main/resources/com/aurafarming/view/weather-alert.fxml)
- Weather alert scene.

9.10 [src/main/resources/com/aurafarming/view/yield-log.fxml](src/main/resources/com/aurafarming/view/yield-log.fxml)
- Yield logging scene.

9.11 [src/main/resources/com/aurafarming/view/audit-log.fxml](src/main/resources/com/aurafarming/view/audit-log.fxml)
- Audit log scene.

9.12 [src/main/resources/com/aurafarming/view/export.fxml](src/main/resources/com/aurafarming/view/export.fxml)
- Report preview/export scene.

### 10) Package: src/main/resources/com/aurafarming/css
10.1 [src/main/resources/com/aurafarming/css/app.css](src/main/resources/com/aurafarming/css/app.css)
- Global visual style definitions for JavaFX scenes.

### 11) Folder: execution steps (Implementation Guidance)
11.1 [execution steps/step1.md](execution%20steps/step1.md)
- Initial setup guidance.

11.2 [execution steps/step2.md](execution%20steps/step2.md)
- Core model/scaffold progression.

11.3 [execution steps/step3.md](execution%20steps/step3.md)
- Additional architecture progression.

11.4 [execution steps/step4.md](execution%20steps/step4.md)
- Module implementation phase.

11.5 [execution steps/step5.md](execution%20steps/step5.md)
- Module implementation phase.

11.6 [execution steps/step6.md](execution%20steps/step6.md)
- Module implementation phase.

11.7 [execution steps/step7.md](execution%20steps/step7.md)
- Module implementation phase.

11.8 [execution steps/step8.md](execution%20steps/step8.md)
- Module implementation phase.

11.9 [execution steps/step9.md](execution%20steps/step9.md)
- Module implementation phase.

11.10 [execution steps/step10.md](execution%20steps/step10.md)
- Integration/polish phase.

11.11 [execution steps/step11.md](execution%20steps/step11.md)
- Integration/polish phase.

11.12 [execution steps/step12.md](execution%20steps/step12.md)
- Integration/polish phase.

11.13 [execution steps/step13.md](execution%20steps/step13.md)
- Integration/polish phase.

11.14 [execution steps/step14.md](execution%20steps/step14.md)
- Finalization and completion guidance.

### 12) Root and Build Support Files
12.1 [pom.xml](pom.xml)
- Maven build configuration, dependencies, JavaFX plugin, packaging.

12.2 [mvnw](mvnw)
- Maven wrapper launcher for Unix-like shells.

12.3 [mvnw.cmd](mvnw.cmd)
- Maven wrapper launcher for Windows.

12.4 [.mvn/wrapper/maven-wrapper.properties](.mvn/wrapper/maven-wrapper.properties)
- Maven wrapper settings.

12.5 [.mvn/wrapper/maven-wrapper.jar](.mvn/wrapper/maven-wrapper.jar)
- Maven wrapper runtime binary.

12.6 [documentation.md](documentation.md)
- Main detailed design and implementation notes.

12.7 [ProjectDetailByUser.txt](ProjectDetailByUser.txt)
- Requirements refinement and role behavior expectations.

12.8 [ap_question.txt](ap_question.txt)
- Assignment statement and grading context.

12.9 [AuraFarming.iml](AuraFarming.iml)
- IntelliJ module metadata.

## 8. Data and Output Folders
- data storage/
  - Object-serialized application data files (users, farms, plots, plans, prices, alerts, yields, audit).
- export results/
  - Generated report files (CSV/text exports).

These folders are created/used at runtime by utility services.

## 9. Responsibility Summary (Prototype)
- Admin: full access, user/officer/farmer management, full audit visibility, broad export capability.
- Officer: farmer operational support, market/weather updates, operational exports (restricted vs admin).
- Farmer: own farm/plot/crop/yield operations, view-only on restricted modules, export limited to own scope.

## 10. Notes for New Contributors
1. Read [documentation.md](documentation.md) first for architecture and scope.
2. Read [execution steps/step1.md](execution%20steps/step1.md) to [execution steps/step14.md](execution%20steps/step14.md) for phased build understanding.
3. Keep changes within MVC + DAO + DTO boundaries.
4. Preserve file-based persistence rule (no SQL/cloud APIs).
5. Validate with Maven compile/package before pushing.
