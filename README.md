# Train management system
**Developers:**
- Camilo Andrés Serrano Jimenez ([@camilio77](https://github.com/camilio77))
- Juan Sebastian Gamboa ([@CrashNight005](https://github.com/CrashNight005))

### **Project description**

This is the repository for the data structure class 11307 project with the name "Train management system for passengers traveling", the main purpose of the project is to automatize all the management of trains, passengers, routes, employees, luggages and tickets of the UPB transport organization, this is due the great amount of work that the administrators and employees of the organization need to get the organization data working properly manually, so for that reason in this system we offer a technological solution for them.

### **Requirements**

- **Passenger management:** Register, Login and Logout.
- **Security:** Roles and sessions management (Passenger, Employee, Administrator).
- **Navigation:** Dynamic menu for every user in session.
- **Profile:** Get and Update user information and password.
- **Core CRUD:** Complete management for Trains, Users, Luggages, Wagons, Routes, Stations and Tickets (Create, Delete, Update and Get).
- **Advanced filters:** Search and filters for Routes.
- **Quality:** Error handling and user input validation.
- **Documentation and tests:** JavaDoc and JUnit.

### **Class diagram**

In the following diagram is defined the organization of the project and classes that solve the problem.

![DiagramaDeClases](C:\Users\camil\Downloads\proyecto integrador lenin 3r semestre tren\DiagramaDeClases.jpg)

### **Database**

All the data of the system is stored in a PostgreSQL online database in [supabase.com](https://www.supabase.com) because of the facility to use Java with an SQL type database, specially a PostgreSQL one.

### **How to run the program**

To run the system you need to first clone the project with the following command:

```
git clone https://github.com/camilio77/train_management_system.git
cd train_management_system
code .
```

Then in your IDE you need to go to the maven dependency, javaFx and then you execute run.
