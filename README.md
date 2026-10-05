# Asha Management System

A JavaFX desktop application for managing products, suppliers, categories, and inventory shipments in a MySQL database.

## Features

- Create, update, delete, search, sort, and filter products.
- Create, update, delete, search, and sort categories and manufacturers.
- Record shipments with multiple product entries and keep product quantities synchronized.
- Update or remove shipments and view their product-entry details.
- View product and shipment statistics, including expiry, discounts, and category totals.

## Technologies & Tools

- **JDK 25** — compiles and runs the application.
- **JavaFX 25** — desktop UI, FXML views, controls, and CSS styling.
- **MySQL 8+ / JDBC** — persistent inventory data and SQL reporting.
- **Maven Wrapper** — portable dependency management, builds, tests, and JavaFX launch commands.
- **dotenv-java** — loads local MySQL settings from an untracked `.env` file.

## Data Structures

- JavaFX `ObservableList` collections hold categories, manufacturers, products, shipments, and shipment entries for table binding.
- JavaFX property objects in the model classes expose values to `TableView` columns.
- A temporary observable entry list collects shipment lines before they are committed in one database transaction.

## Prerequisites

- A JDK 25 installation with `JAVA_HOME` set to that JDK.
- MySQL Server 8 or newer and the MySQL command-line client.
- A MySQL account allowed to create and use the `asha` database.

## Getting Started

1. Create your local environment file from the provided template:

   ```powershell
   Copy-Item .env.example .env
   ```

   On macOS or Linux, use `cp .env.example .env`. Edit `.env` and replace the example values with your own local MySQL URL, user, and password. Never commit `.env`.

2. Initialize the database and optional sample data from the repository root:

   ```powershell
   mysql -u root -p -e "source database/schema.sql"
   mysql -u root -p -e "source database/data.sql"
   ```

3. Build and run with the Maven Wrapper:

   ```powershell
   .\mvnw.cmd clean
   .\mvnw.cmd javafx:run
   ```

   On macOS or Linux, use `sh mvnw clean test` and `sh mvnw javafx:run`.

## Project Structure

- `src/main/java/AshaMangmentSystem/` — existing Java models, controllers, database access, and application entry point.
- `src/main/resources/AshaMangmentSystem/` — FXML views, CSS, and UI icons.
- `database/schema.sql` — complete MySQL schema, constraints, foreign keys, and indexes.
- `database/data.sql` — repeatable sample inventory data.
- `.mvn/`, `mvnw`, `mvnw.cmd`, and `pom.xml` — Maven build and wrapper configuration.
