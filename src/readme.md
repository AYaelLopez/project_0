# Loan Management System - README

## Overview

This project implements a RESTful back-end application for a Loan Management System. Users can register, log in, manage their profiles, and apply for loans. Manager users have additional privileges to view and manage all loan applications.

## Technologies Used

* Java 17
* Javalin (for REST APIs)
* JDBC (for database connectivity)
* PostgreSQL (database)
* JUnit 5 (unit testing)
* Logback (logging)
* Postman (for API testing)

## Setup and Running the Application

### Prerequisites

* Java 17 or later
* Maven
* PostgreSQL database
* Postman or a similar API testing tool

### Database Setup

1.  **Create a PostgreSQL database:**
    * Create a new database for the loan management system.
2.  **Create tables:**
    * Execute the following SQL scripts to create the `users` and `loans` tables. Replace the placeholders with your desired constraints.
    * users table:
    ```DROP TABLE IF EXISTS Roles CASCADE;
DROP TABLE IF EXISTS Users CASCADE;
DROP TABLE IF EXISTS Loans CASCADE;
DROP TABLE IF EXISTS Account CASCADE;


CREATE TABLE Roles (
roleid INTEGER PRIMARY KEY NOT NULL UNIQUE,
role VARCHAR(20) NOT NULL);

CREATE TABLE Users (
userid SERIAL PRIMARY KEY NOT NULL,
name VARCHAR(20) NOT NULL,
last_name VARCHAR(20) NOT NULL,
role INTEGER NOT NULL,
account_id INTEGER NOT NULL);

CREATE TABLE Loans (
loan_aplication SERIAL NOT NULL,
applicant INTEGER NOT NULL,
amount_request DOUBLE PRECISION NOT NULL,
status VARCHAR(20) NOT NULL);

CREATE TABLE Account (
account_id SERIAL PRIMARY KEY NOT NULL,
username VARCHAR(20) NOT NULL UNIQUE,
password VARCHAR(20) NOT NULL);

ALTER TABLE Users ADD CONSTRAINT Users_role_Roles_roleid FOREIGN KEY (role) REFERENCES Roles(roleid);
ALTER TABLE Users ADD CONSTRAINT Users_account_id_Account_account_id FOREIGN KEY (account_id) REFERENCES Account(account_id);
ALTER TABLE Loans ADD CONSTRAINT Loans_applicant_Users_userid FOREIGN KEY (applicant) REFERENCES Users(userid);



insert into roles (roleid, role) values (1, 'regular'), (2, 'manager');


    ```
3. **Configure database connection:**
    * Update the database connection details in your application's configuration (e.g., in the `App.java` file or a separate configuration file). You will need to change the database url, username and password.


### Running Unit Tests

To run the unit tests, execute the following command:

```bash
mvn test

API Endpoints
Authentication
 * POST /auth/register: Register a new user.
 * POST /auth/login: Log in a user.
 * POST /auth/logout: Log out the current session.
User Management
 * GET /users/{id}: Get user info (user can only see their own, or manager can see any user).
 * PUT /users/{id}: Update user profile (only if it’s the same user or a manager).
Loan Management
 * POST /loans: Create a loan (logged-in user).
 * GET /loans: View all loans (manager only) or just the user’s loans (regular user).
 * GET /loans/{loanId}: View a specific loan (owner or manager).
 * PUT /loans/{loanId}: Update the loan (owner or manager).
 * PUT /loans/{loanId}/approve: Approve a loan (manager only).
 * PUT /loans/{loanId}/reject: Reject a loan (manager only).
Postman Collection
A Postman collection is provided to demonstrate the functionality of the API. Import the collection into Postman to test the endpoints. The postman collection will test registration, login, logout, loan creation, and manager actions.
Logging
Logback is used for logging. Logs are written to the console and/or a file, depending on the logback.xml configuration.
 * INFO: Major actions (e.g., user registration, loan creation).
 * WARN: Suspicious or invalid actions (e.g., unauthorized access).
 * ERROR: Exceptions or critical failures (e.g., database errors).
Service Layer Unit Tests
The service layer includes unit tests to validate business logic. JUnit 5 is used for testing. Tests cover scenarios such as loan creation with invalid data and role-based access control for loan approval.

