# **Online library**

Online library is a simple Java Spring Boot application to store and rent books. 

It stores and sells books of following categories:

- NEW

- CLASSIC

- STANDARD

In a java project, these categories are arranged in constants folder as an enum.

# **Functionality And Approach Taken:**
Online library uses MySql and consists of database named “library” which has 3 table named: 
“books”, "users", "rent".

Online library supports advanced CRUD operations for user, book and rent entity.

# **Tools/Framework**

- MYSQL: MYSQL is a open-source relational database management system.
- JPA: Java Persistence API(JPA) is a Java programming interface specification that describes the management of relational data in applications using Java Platform.
- Lombok: Project Lombok is a java library that automatically plugs into an editor and build tools, spicing up java. Getter, Setters, Constructors can be created with annotation without writing the code with the help of Lombok.
- ModelMapper: ModelMapper is a simple, intelligent, object mapping tool. It is used to map the object in this project. 
- Swagger: Swagger is an open-source framework that helps developers design, build, document and consume RESTful Web services.