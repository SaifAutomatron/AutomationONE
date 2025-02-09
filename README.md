==> AutomationONE Test Automation Framework

=> Overview

AutomationONE is a versatile test automation framework designed for automating UI, API, and Mobile testing. The framework integrates robust utilities for handling data, interacting with databases, performing Unix commands, and generating detailed reports through **ExtentReports**. It is built using Java, Selenium WebDriver, TestNG, Appium, and various other libraries for different utilities.

=> Features

- **UI Testing**: Automate browser-based testing using Selenium WebDriver.
- **Mobile Testing**: Supports mobile application testing on both Android and iOS using Appium.
- **Data Management**: Support for test data stored in both Excel and JSON formats.
- **Database Interaction**: Execute SQL queries and retrieve Couchbase NoSQL data.
- **Unix Utilities**: Connect to Unix servers to execute commands and transfer files.
- **Detailed Reporting**: Generate beautiful HTML reports using ExtentReports with integrated screenshots and logs.
- **Reusable Utilities**: Common utilities for encryption, email sending, and more.
- **Appium Server Management**: Start and stop Appium server programmatically.
- **Gestures and Actions**: Perform touch gestures like tapping, scrolling, and drag-and-drop for mobile apps.

=> Folder Structure

```
AutomationONE/
│
├── Utilities/
│   ├── Common/
│   │   ├── EncryptionUtils.java
│   │   ├── JavaUtilities.java
│   │   ├── PathConst.java
│   │   ├── SendMail.java
│   │   ├── WebElementUtils.java
│   │   └── AppiumUtilities.java
│   ├── Database/
│   │   ├── CouchBase.java
│   │   └── DataBase.java
│   ├── Excel/
│   │   ├── EmailData.java
│   │   ├── EnvironmentData.java
│   │   ├── ReferData.java
│   │   └── TestData.java
│   ├── Json/
│   │   └── TestDataJson.java
│   ├── Listener/
│   │   ├── ExtentListener.java
│   │   └── ExtentReporterCls.java
│   ├── Unix/
│   │   ├── UnixUtils.java
│   └── UnixTest/
│       └── LoginExtent.java
├── UITest.xml
```

=> Prerequisites

1. **Java 11 or higher**
2. **Maven** for managing dependencies
3. **Selenium WebDriver 4.x**
4. **TestNG** for managing test execution
5. **ExtentReports** for report generation
6. **Appium Java Client 8.x**
7. **Couchbase** and **JDBC** drivers for database interaction
8. **JSch** library for Unix command execution

=> Setup Instructions

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/SaifAutomatron/AutomationONE.git
   cd AutomationONE
   ```

2. **Install Dependencies**:
   Ensure that Maven is installed. Run the following command to install dependencies:
   ```bash
   mvn clean install
   ```

3. **Configure Test Data**:
   - Ensure that test data files (e.g., Excel sheets, JSON files) are placed in the appropriate paths defined in the `EnvironmentData.java` file.
   - Set up environment-specific details (like Couchbase host, database credentials) in the `environment.properties` file.

=> How to Execute Tests

1. **Test Execution**:
   The framework uses **TestNG** for running the tests. You can execute tests via the **UITest.xml** file, which defines the tests to run:
   ```bash
   mvn test -DsuiteXmlFile=UITest.xml
   ```

2. **Reports**:
   After the tests complete, the framework will generate an HTML report using **ExtentReports**. You can find the report at:
   ```
   /target/ExtentReports/Report.html
   ```

=> Key Components

1. **Utilities.Common**
   - **EncryptionUtils**: Provides encryption and decryption functionality using the DESede algorithm.
   - **JavaUtilities**: Contains utility methods for handling Selenium WebDriver interactions, such as switching frames, handling alerts, and taking screenshots.
   - **SendMail**: Sends emails with test results or logs.

2. **Utilities.Database**
   - **CouchBase**: Handles Couchbase NoSQL database operations (retrieving and creating documents).
   - **DataBase**: Executes SQL queries and exports table data to Excel using Apache POI.

3. **Utilities.Excel**
   - **EmailData, EnvironmentData, TestData**: Manage test data from Excel sheets, with the ability to retrieve specific data points for use during test execution.

4. **Utilities.Json**
   - **TestDataJson**: Handles test data stored in JSON files, converting the data into a `HashMap` for easy access during tests.

5. **Utilities.Unix**
   - **UnixUtils**: Provides methods for connecting to Unix servers, executing commands, and transferring files using SSH and SFTP.

6. **Utilities.Appium**
   - **AppiumUtilities**: Includes methods for:
     - Starting and stopping the Appium server programmatically.
     - Initializing Android and iOS drivers.
     - Performing touch actions like tapping, scrolling, and drag-and-drop.
     - Capturing screenshots and locking/unlocking devices.

7. **Reporting**
   - **ExtentListener**: Integrates **ExtentReports** into the framework for generating detailed HTML reports with logs, screenshots, and structured JSON/XML outputs.

=> Customization

- You can modify the **UITest.xml** file to enable or disable specific tests or change test execution priorities.
- The framework is designed to be extensible. You can add new utilities or modify existing ones to suit your testing needs.

=> Contributing

If you'd like to contribute to this project, feel free to open an issue or submit a pull request.

**Work in Progress**
- Full integration of Appium with Browserstack utilities.
- Enhanced API testing capabilities.
- Addition of advanced mobile gestures.
