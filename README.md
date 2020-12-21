# HR-App-RESTful

* Simple REST Application for HR database
* Project is initialized with "Spring Initialzr"
* Spring-Boot Project
* Connected with MySQL DB
* Pre-requisite: Java15, MySQL
* Includes Unit Test for each request in Employee Controller (JUnit4, Mock MVC, Mockito)
* "hrapp" db includes existing datas
* If you want to generate new database, you should change following line in "/src/main/resources/application.properties" (this file also includes default settings for MySQL DB)
 - current: "spring.jpa.hibernate.ddl-auto=update"
 - changed: "spring.jpa.hibernate.ddl-auto=create"

* To run the application the main function is under "src/main/java/com.hrapp.HRAPPREST/HrappRestApplication"
* After starting the application, you can access to the data with the following URLs:
  - "/localhost:8080/employees" it returns all employees in the db
  - "/localhost:8080/employees/{id}" it returns employee by given id
  - "/localhost:8080/" Welcome Screen
* You can use Postman to try other CRUD operations (POST/DELETE/PUT)
  - "/localhost:8080/employees" (POST)
  - "/localhost:8080/employees/{id}" (PUT)
  - "/localhost:8080/employees/{id}" (DELETE)
  
* If there is no employee by given id, it returns "EmployeeNotFoundException" with (404)HttpStatus.NOT_FOUND).
