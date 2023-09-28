# Appointment Scheduler

## Author

Craig Bodo

craigmbodo@gmail.com

## Description

Appointment Scheduler allows a fictional company to keep track of customer appointments. This program was designed and
built for a Software II class project. It uses a Java backend, JavaFX for the GUI, and a MySQL database, using JDBC to
connect to and query the database.

## About the Project

Appointment Scheduler uses a simple interface to allow a fictional company to manage its client schedules.
A major feature of this program is its attention to the current user's timezone. Because the company is
based on the East Coast of the United States, all appointments entered into the database need to adhere
to the company's open hours within that timezone. Therefore, form validation is used to ensure all appointments
fall within the correct hours.

This program was intended to be a simple, clutter-free experience for the company's users. The main window features
two tables: Schedule and Customers.

The Schedule table features three tabs: Weekly, Monthly, and Reports. The Weekly tab lists all appointments scheduled for
the current week. The Monthly tab lists all appoints scheduled for the current month. The Reports tab displays two
separate reports for the company's records: Total Number of Appointments by Month and Type, and Average Appoint Length
for Each Customer. Under the Weekly and Monthly tabs, the user can Add Edit or Delete appointments using the buttons
below the table. The View All button opens a modal window that displays all appointments within the database.
Currently, the Weekly/Monthly views only display appointments for the current week/month. Future iterations of this
project may implement functionality to allow a user to choose specific weeks/months or a custom range.

The Customer table shows a list of the company's customers. The View Schedule allows the user to see all appointments
in the database for the selected customer. The user can Add, Edit, or Delete customers using the buttons below the
table.

The program also features a useful alert that lets the user know if an appointment is scheduled to begins within
15 minutes of logging in. Currently, the alert only happens once upon login, but future iterations of the project may
allow for a more customizable and automated alert.

Additionally, the program logs all login attempts in a text file named 'login_activity' located in the root directory.

## Instructions

Since this program is not hosted on any server, running it on your local machine will require some setting up.

### Setting Up The Database:

1. Open a MySQL connection on 'localhost:3306'.
2. Create a database named 'client_schedule'. NOTE: You may need to update the username and password located on lines 37
and 41 in 'src/main/java/com/cbodo/schedule/util/JDBC.java', if your MySQL username and password do not match.
3. Run the scripts in MySQL Workbench to create the tables and populate with example data, in the following order:
    >'client_schedule_db_dml.sql'

    >'client_schedule_db_ddl.sql'

### Running The Application:

Enter the following into the command line:

```bash
cd scheduler/out/artifacts/scheduler_jar
java -jar scheduler.jar
```

### Using the Application

Once the application loads, you will be prompted to log in. Use either of the following username/password combinations:

>**username:** *test*\
>**password:** *test*

>**username:** *admin*\
>**password:** *admin*

## IDE, SDK, and Libraries

IntelliJ IDEA 2023.2 (Community Edition)

JDK 17.0.1

JavaFX-SDK-17.0.6

## MySQL Connector Driver Version

mysql-connector-java-8.0.26