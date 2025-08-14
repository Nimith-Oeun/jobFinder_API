# Spring Boot Refresh Token with JWT example

Build JWT Refresh Token in the Java Spring Boot Application. You can know how to expire the JWT, then renew the Access Token with Refresh Token.

The instruction can be found at:
[Spring Boot Refresh Token with JWT example](https://bezkoder.com/spring-boot-refresh-token-jwt/)

## User Registration, User Login and Authorization process.
The diagram shows flow of how we implement User Registration, User Login and Authorization process.

<img width="734" height="546" alt="spring-boot-spring-security-jwt-authentication-flow" src="https://github.com/user-attachments/assets/ea4010a2-e390-4556-923a-c561029d137c" />


And this is for Refresh Token:

<img width="700" height="480" alt="spring-boot-refresh-token-jwt-example-flow" src="https://github.com/user-attachments/assets/ad2de8fb-840d-4d26-93df-0ca1847c7e81" />


## Spring Boot Server Architecture with Spring Security
You can have an overview of our Spring Boot Server with the diagram below:

<img width="700" height="480" alt="spring-boot-jwt-authentication-spring-security-architecture" src="https://github.com/user-attachments/assets/b24a1074-e479-4f3d-8c1f-78d1e47d3d89" />


## Configure Spring Datasource, JPA, App properties
Open `src/main/resources/application.properties`

```properties
spring.datasource.url= jdbc:mysql://localhost:3306/testdb?useSSL=false
spring.datasource.username= root
spring.datasource.password= 123456

spring.jpa.properties.hibernate.dialect= org.hibernate.dialect.MySQL5InnoDBDialect
spring.jpa.hibernate.ddl-auto= update

# App Properties
bezkoder.app.jwtSecret= bezKoderSecretKey
bezkoder.app.jwtExpirationMs= 3600000
bezkoder.app.jwtRefreshExpirationMs= 86400000
```

## Run Spring Boot application
```
mvn spring-boot:run
```
