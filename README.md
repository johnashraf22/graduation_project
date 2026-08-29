# OrangeHRM Automation Testing — Graduation Project

Selenium WebDriver + TestNG + Page Object Model automation suite for the
[OrangeHRM Open Source Demo](https://opensource-demo.orangehrmlive.com/web/index.php/auth/login).

## Tech stack
- Java 11, Maven
- Selenium WebDriver 4.x
- TestNG 7.x (parallel execution, DataProvider, IRetryAnalyzer)
- WebDriverManager (auto-downloads the matching ChromeDriver binary)
- Jackson (JSON data-driven testing)
- Log4j2 (step/error logging to `logs/automation.log`)
- Allure Reporting

## Project structure
```
src/test/java/
  base/       BaseTest.java          -> ThreadLocal<WebDriver>, setUp/tearDown
  pages/      LoginPage, DashboardPage, PimPage, AddEmployeePage,
              AdminUsersPage, BasePage (shared explicit-wait helpers)
  utils/      ConfigReader           -> reads config.properties
              JsonDataProviderUtil   -> reads testdata.json for @DataProvider
              RetryAnalyzer/Listener -> retries a failed test up to 2x
  tests/      LoginTests   (TC1-TC3)
              PimTests     (TC4-TC8)
              AdminTests   (TC9)
              UiTests      (TC10-TC11)
src/test/resources/
  config.properties   -> base.url, browser, explicit.wait, headless
  testdata.json       -> usernames/passwords/employee names
  log4j2.xml
  allure.properties
testng.xml            -> parallel="tests", thread-count="3"
```

## Test cases covered
| # | Case | Class |
|---|------|-------|
| 1 | Login with valid credentials | LoginTests |
| 2 | Login with invalid credentials | LoginTests |
| 3 | Login with empty fields | LoginTests |
| 4 | Search for an existing employee | PimTests |
| 5 | Search for a non-existing employee | PimTests |
| 6 | Open Add Employee page | PimTests |
| 7 | Add employee with empty required field | PimTests |
| 8 | End-to-end: add a new employee | PimTests |
| 9 | Admin > Add User page fields | AdminTests |
| 10 | Footer/branding link | UiTests |
| 11 | Sidebar menu UI | UiTests |

## Before you run it
- **Google Chrome** must be installed locally (WebDriverManager handles the driver binary automatically).
- `testdata.json` → `existingEmployee.employeeName` is set to `"Amelia Brown"`, a
  name commonly seeded in the public demo. **The demo resets periodically**, so
  if TC4 fails, open the app manually, note a real employee name from
  PIM > Employee List, and update that value in `testdata.json`.

## How to run
```bash
mvn clean test
```
This runs `testng.xml`, which executes `LoginTests`, `PimTests`, and
`AdminTests`+`UiTests` in parallel (`parallel="tests"`, `thread-count="3"`).

**Important — always run through Maven, not IntelliJ's own "Run" button on a
test class.** The `@Step` annotations (required by the assignment's "meaningful
test steps" Allure requirement) are woven into the compiled bytecode by the
`aspectj-maven-plugin` during `mvn compile`/`mvn test`. If you right-click a
test class and hit Run, IntelliJ may use its own compiler and skip that
weaving step entirely, and your Allure report will be missing step detail (or
tests may not run at all if IntelliJ tries to reuse a stale run configuration).
If you want the green-arrow Run button to work too, enable **Settings >
Build, Execution, Deployment > Build Tools > Maven > Runner > Delegate IDE
build/run actions to Maven**, so IntelliJ hands compilation off to Maven.

To run headless (e.g. in CI), set `headless=true` in `config.properties`.

## Generating the Allure report
```bash
mvn test
allure generate allure-results --clean -o allure-report
allure open allure-report
```

## Logs
Step-level `INFO` logs and `ERROR`s are written to `logs/automation.log`
(and mirrored to the console) via Log4j2.

## Submission
1. Create a **public** GitHub repository named `automation-testing-graduation-project`.
2. Push this project to it, including the generated `allure-report/` folder
   (do **not** commit `allure-results/` — it's already in `.gitignore`).
3. Email the repo link to `nouranabil0216@gmail.com` with:
   - Subject: `GP Task [Your Name] – Online/Offline`
   - Body: the repository link
"# graduation_project" 
