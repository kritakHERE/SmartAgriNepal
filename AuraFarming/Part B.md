# Part B – Development Report

## 1. Creativity and Innovation

AuraFarming demonstrates creativity and innovation by extending the base assignment brief into a more polished offline-first desktop solution for agricultural cooperative management in Nepal. Beyond simply implementing CRUD forms, the project emphasizes practical usability, visual clarity, and role-sensitive workflows.

### 1.1 Additional Value Added in the Implementation
- **Offline-first architecture**: The system is designed to work fully without internet connectivity, which directly fits the rural Nepal agricultural scenario.
- **Layered architecture**: The application is organized using a clear `View -> Controller -> Service -> DAO -> File` flow, making the system easier to maintain and extend.
- **Role-aware dashboards**: Different users see different functions and access levels based on whether they are Admin, Officer, or Farmer.
- **Aesthetic JavaFX interface**: The interface uses JavaFX FXML and CSS styling to create a cleaner and more modern desktop experience than a plain form-based system.
- **Report export support**: The system can generate exportable output files for audit history, crop plans, market prices, yields, and farm/plot summaries.
- **Recommendation-oriented crop planning**: Crop planning is not just data entry; it includes a rule-based recommendation score to support better seasonal planting decisions.
- **Audit-driven accountability**: Important actions are logged to support traceability and cooperative accountability.

### 1.2 UI and User Experience Innovation
The application uses JavaFX and FXML to separate presentation from logic while supporting a more structured and attractive interface. Dashboard tabs, metric cards, forms, result areas, and CSS styling help users navigate the system more easily. This is especially useful for non-technical cooperative staff who need a simple but informative interface.

### 1.3 Innovation Within Assignment Boundaries
The assignment restricts storage to local files only and disallows SQL or web services. Within those boundaries, AuraFarming still introduces meaningful enhancements such as role-based visibility, export previews, recommendation scoring, chart-style dashboard summaries, and consistent scene-based navigation. These features improve user experience without violating the coursework scope.

## 2. Patterns and Object-Oriented Concepts

AuraFarming strongly applies both object-oriented programming principles and software design patterns. The implementation is structured so that business rules, persistence, UI logic, and data models remain separated.

### 2.1 Object-Oriented Concepts Used

#### Encapsulation
Encapsulation is applied throughout the model layer. Domain classes such as `User`, `Farmer`, `Farm`, `Plot`, `CropPlan`, `MarketPrice`, `WeatherAlert`, `YieldLog`, and `AuditLog` keep their fields private and expose access through constructors, getters, and setters. This protects internal state and keeps updates controlled.

#### Inheritance
Inheritance is visible in the user hierarchy:
- `User` acts as the common base class.
- `Admin`, `Officer`, and `Farmer` extend `User`.

This reduces duplication and supports shared behavior such as user identity, login credentials, role, and status while allowing specialized role-based behavior.

#### Abstraction
Abstraction is used by representing shared concepts through common parent classes and reusable components. For example, `User` abstracts the common attributes of all users. DAO classes also abstract file persistence responsibilities so that controllers do not directly handle serialization logic.

#### Polymorphism
Polymorphism appears in role-based handling, where different subclasses of `User` are processed through shared references while behavior differs according to role. The system also uses role checks in services and controllers to change access permissions and visible features dynamically.

#### Interfaces / Reusable Contracts
The project mainly uses abstracted layered design rather than many explicit custom interfaces, but it still demonstrates interface-oriented thinking through DTOs, DAO reuse, and controller-service separation. Reusable utility and DAO structures act as common contracts for consistent behavior.

### 2.2 Design Patterns Used

#### MVC (Model-View-Controller)
AuraFarming follows an MVC-inspired structure:
- **Model**: classes in `src/main/java/com/aurafarming/model`
- **View**: FXML files in `src/main/resources/com/aurafarming/view`
- **Controller**: JavaFX controllers in `src/main/java/com/aurafarming/controller`

This pattern helps keep UI layout separate from application logic.

#### DAO (Data Access Object)
The assignment requires file-based persistence instead of a database. AuraFarming implements the DAO pattern through classes such as:
- `UserDAO`
- `FarmDAO`
- `PlotDAO`
- `CropPlanDAO`
- `MarketPriceDAO`
- `WeatherAlertDAO`
- `YieldLogDAO`
- `AuditLogDAO`

These DAOs isolate all file read/write behavior from the controllers and services. A reusable `BaseObjectDAO` supports shared serialization logic.

#### DTO (Data Transfer Object)
DTOs are used to pass structured form or request data without exposing domain objects directly to the UI. The project includes:
- `LoginDTO`
- `RegistrationDTO`
- `ExportRequestDTO`

This keeps controller code cleaner and avoids mixing raw form controls with business logic.

#### Service Layer Pattern
Although not listed explicitly in the assignment, the system effectively uses a service layer. Classes such as `AuthService`, `FarmPlotService`, `CropPlanService`, `MarketPriceService`, `WeatherAlertService`, `YieldLogService`, `AuditService`, and `ExportService` contain validations, authorization logic, and business rules. This improves maintainability and keeps controllers focused on UI interaction.

### 2.3 Where the Patterns and OOP Concepts Are Implemented
- **Authentication and role access**: `AuthService`, `SessionContext`, `DashboardController`
- **Farmer and officer management**: `FarmerController`, `OfficerController`, `UserService`
- **Farm and plot module**: `FarmPlotController`, `FarmPlotService`, `FarmDAO`, `PlotDAO`
- **Crop planning and advisory**: `CropPlanController`, `CropPlanService`, `CropPlanDAO`
- **Market price management**: `MarketPriceController`, `MarketPriceService`, `MarketPriceDAO`
- **Weather alerts**: `WeatherAlertController`, `WeatherAlertService`, `WeatherAlertDAO`
- **Yield logging**: `YieldLogController`, `YieldLogService`, `YieldLogDAO`
- **Audit logging and export**: `AuditLogController`, `AuditService`, `ExportController`, `ExportService`, `AuditLogDAO`

## 3. Mapping Design to Implementation

The implementation reflects the analysis and design direction described in Part A. The use case expectations and design class responsibilities are translated into concrete packages, classes, screens, and workflows.

### 3.1 Use Case to Implementation Mapping

#### User Registration and Login
The use cases for registration and login are implemented through:
- `registration.fxml` and `RegistrationController`
- `login.fxml` and `LoginController`
- `AuthService`
- `UserDAO`

Users enter registration details through the interface, DTOs carry the data to the service layer, and validated user objects are stored locally through the DAO layer.

#### Role-Based Dashboard Access
The dashboard use case is implemented through:
- `dashboard.fxml`
- `DashboardController`
- `SessionContext`
- `DashboardService`

After successful login, the current session user is stored and the dashboard loads different tabs and summaries based on the role.

#### Farmer Profile Management
The farmer management use case is implemented through:
- `farmers.fxml`
- `FarmerController`
- `UserService`

This supports search and basic status-based management of farmer records.

#### Farm and Plot Management
The design for managing one farmer with multiple farms and plots is implemented through:
- `farm-plots.fxml`
- `FarmPlotController`
- `FarmPlotService`
- `FarmDAO` and `PlotDAO`
- `Farm` and `Plot` models

This accurately reflects the one-to-many relationship planned in the design.

#### Crop Planning and Advisory
The crop planning use case is implemented through:
- `crop-plan.fxml`
- `CropPlanController`
- `CropPlanService`
- `CropPlanDAO`
- `CropPlan` model

The service computes recommendation scores and persists crop planning records for later viewing and export.

#### Market Price Management
The market price design is implemented through:
- `market-price.fxml`
- `MarketPriceController`
- `MarketPriceService`
- `MarketPriceDAO`
- `MarketPrice` model

This allows officer/admin users to save district-based crop prices and review historical price records.

#### Weather Risk Alerts
The weather alert module is implemented through:
- `weather-alert.fxml`
- `WeatherAlertController`
- `WeatherAlertService`
- `WeatherAlertDAO`
- `WeatherAlert` model

It supports alert creation and viewing, with role-based permissions on who may create or only view alerts.

#### Yield Logging
The yield log module extends the original core scope with practical operational value. It is implemented through:
- `yield-log.fxml`
- `YieldLogController`
- `YieldLogService`
- `YieldLogDAO`
- `YieldLog` model

This supports tracking estimated and actual crop yield per plot.

#### Reporting and Export
The reporting use case is implemented through:
- `export.fxml`
- `ExportController`
- `ExportService`
- `ExportRequestDTO`

Exports are written into the local `export results` folder, which matches the offline desktop requirement.

#### Audit Logging
The accountability requirement is implemented through:
- `AuditService`
- `AuditLogDAO`
- `AuditLogController`
- `AuditLog` model

Important actions such as save, update, delete, export, and login-related operations are recorded.

### 3.2 Design Class Diagram to Source Code Translation
The design class diagram from Part A is translated into implementation through clear package grouping:
- `model` package for domain entities and enums
- `dto` package for request/transfer objects
- `dao` package for persistence classes
- `service` package for business logic
- `controller` package for JavaFX event handling
- `view` package for FXML screens
- `util` package for shared helper classes

This package structure closely follows the designed architecture and makes the mapping between design and implementation easy to explain.

### 3.3 Accuracy of the Mapping
Overall, the mapping from design to implementation is strong because the project preserves the same domain concepts, module separation, and object responsibilities described in the analysis documents. The source code shows a deliberate effort to keep models, persistence, logic, and UI independent.

At the same time, some assignment-level requirements appear only partially implemented in the current source base:
- backup and restore are not implemented as dedicated modules
- weather alerts are entered manually rather than imported from weather files
- some user management functions are partial or limited in UI flow
- reporting focuses on file export rather than advanced visualization

These points should be acknowledged honestly in the final academic submission.

## 4. Screenshots and Testing Evidence

This section should contain screenshots from the running application to prove that each core module works correctly. The screenshots should be inserted by the team during final report preparation.

### 4.1 Recommended Screenshot List
1. **Login screen** – show successful login path.
2. **Registration screen** – show Farmer or Officer registration.
3. **Dashboard** – show role-based tabs and summary cards.
4. **Farmer management** – show search and record listing.
5. **Farm and plot management** – show farm creation, plot creation, and list refresh.
6. **Crop planning** – show recommendation score and saved crop plan.
7. **Market price management** – show price entry and historical list.
8. **Weather alert module** – show alert creation and alert display.
9. **Yield log module** – show yield entry and history.
10. **Audit log module** – show filtered audit records.
11. **Export/report screen** – show preview and generated export file.
12. **Generated files/folders** – show `.dat` storage files and exported report output.

### 4.2 Suggested Testing Coverage to Demonstrate
- Registration with valid and invalid inputs
- Login with correct and incorrect credentials
- Role-based access differences between Admin, Officer, and Farmer
- Farm and plot creation with ownership restrictions
- Crop planning with recommendation/advisory logic
- Market price saving and retrieval by date/history
- Weather alert visibility and restricted edit rights
- Yield logging and farmer-specific filtering
- Export file generation and audit trail creation
- Error handling for empty fields or invalid data

### 4.3 Evidence Note
The current repository contains the implementation and generated data/export folders, but screenshots themselves are not embedded in source code. They should be captured from the working application and inserted into the final PDF report.

## 5. Additional Information for Instructor

### 5.1 Versions
Based on the implementation files currently available:
- **Java JDK**: Java 21 (`maven.compiler.release` is set to `21` in `pom.xml`)
- **JavaFX libraries**: `javafx-controls 21.0.6` and `javafx-fxml 21.0.6`
- **JavaFX Maven plugin**: `0.0.8`
- **JUnit**: `5.12.1` declared in Maven for test support

If Scene Builder was used to design the FXML screens, its exact version should be added by the team if required by the instructor, because it is not explicitly stated in the source repository.

### 5.2 Libraries Used
The project uses very limited external libraries, which keeps the system aligned with the assignment scope.

#### Confirmed libraries from `pom.xml`
- `org.openjfx:javafx-controls:21.0.6`
- `org.openjfx:javafx-fxml:21.0.6`
- `org.junit.jupiter:junit-jupiter-api:5.12.1`
- `org.junit.jupiter:junit-jupiter-engine:5.12.1`

No database libraries, web frameworks, or cloud dependencies are used. This supports the assignment rule that the system must remain offline and file-based.

### 5.3 Application Deployment Requirements
To run the application successfully, the following deployment conditions should be stated clearly:
- Java 21 JDK must be installed.
- Maven should be available if the project is run using Maven commands.
- The application depends on JavaFX runtime through Maven configuration.
- Local folders such as `data storage` and `export results` must be available or creatable by the application.
- The predefined Admin account is documented as `admin@control.com / admin123`.
- Users should load the application from the project root so relative storage/export paths work correctly.
- The system is intended to run fully offline.

### 5.4 Modules and Features Working / Not Fully Working

#### Working / Implemented Modules
- User registration and login
- Role-based access control
- Farmer management (basic search and status management)
- Officer listing/management support
- Farm and plot creation with linked ownership
- Crop planning with recommendation scoring
- Market price management with historical retrieval
- Weather alert creation and viewing
- Yield logging
- Audit logging
- Reporting/export to local files
- Dashboard navigation and role-based module visibility

#### Partially Implemented or Limited Features
- Farmer and officer management are available, but some advanced edit flows are limited.
- User update, password change, and role reassignment are not fully exposed through working UI paths.
- Reporting is stronger in file export than in advanced visualization.

#### Not Fully Implemented Against the Brief
- Dedicated backup and restore functionality is not present as a completed module.
- Weather data file loading is not clearly implemented; alerts appear to be manually entered.
- Some rubric expectations may require stronger final testing evidence and screenshot documentation.

## 6. Conclusion

AuraFarming successfully implements the main goals of Part B by delivering an offline desktop application based on Java OOP principles, JavaFX user interfaces, file-based persistence, and clear layered architecture. The project demonstrates strong use of MVC, DAO, DTO, encapsulation, inheritance, abstraction, and polymorphism while solving a realistic agricultural cooperative management problem.

The implementation is strongest in authentication, farm and plot handling, crop planning, market pricing, weather alerts, yield logging, audit logging, and export generation. A few assignment items remain partial, especially backup/restore and some advanced management flows, but the system as a whole clearly reflects the analysis and design work completed in Part A.

For final report submission, the team should add:
- actual screenshots from the running system
- group cover page details
- any exact deployment notes used during demonstration
- an honest note on features that are partial or pending
