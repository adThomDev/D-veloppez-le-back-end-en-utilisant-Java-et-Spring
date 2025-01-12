# Développez le back-end en utilisant Java et Spring

## Table of Contents

- [Description](#description)
- [Technology](#technology)
- [Documentation](#documentation)
- [Features](#features)
- [Installation](#installation)
    - [Project installation](#project-installation)
    - [Database installation](#database-installation)
    - [Database configuration](#database-configuration)
    - [Database Initialization](#database-initialization)

## Description

This project is a Spring Boot backend for a properties rental portal application, complementing an existing Angular
frontend.

It will handle user authentication, secure requests using Json Web Tokens, manage some CRUD operations on rentals, users
and messages, and provide an API documentation with Swagger.

## Technology

- Java language
- Spring framework
- Swagger for API documentation
- MySQL database

## Documentation

You'll find in the "docs" folder at the root of the project the following files :

- An SQL script for database initialization
- A Postman collection for API testing

## Features

- User Authentication: Users (tenants or property owners) must authenticate to access the application beside the login
  page.  
  NB : You can find in the sql scripts a command to directly create in the database an user with login "user1@ocrp3.com"
  and
  password "
  ocrpassword1".
- Documentation : The API is documented with Swagger, you can access it at this address once the application is
  running :
  http://localhost:3001/swagger-ui/index.html

## Installation

### Project installation

Open a terminal at the desired location and clone the repository:

```bash
git clone https://github.com/adThomDev/Developpez-le-back-end-en-utilisant-Java-et-Spring
```

Install dependencies using e.g. Maven:

```bash
mvn install
```

Edit the *application.properties* file to input your own secret key for JWT generation, and set the database
credentials.  
If you don't modifiy the line "server.port=3001", once started the application will be available at :  
http://localhost:3001

Once that and the database configuration (see below) is done, navigate to the project directory and run the
application (example with maven):

```bash
mvn spring-boot:run
```

### Database installation

You can e.g. use [XAMPP](https://www.apachefriends.org/fr/index.html) to install a MySQL database :  
Once XAMPP installed, in the Control Panel start the Apache and MySQL modules.  
You can then access phpMyAdmin to administer the database at http://localhost/phpmyadmin

### Database configuration

You can find the database configuration in the application.properties file, where for example you'll be able to change
its server name and port, and the credentials to access it.

### Database Initialization

To initialize the database's tables (and optionnaly populate them with some data), you'll find SQL scripts in the
docs/sql project'
s folder.



