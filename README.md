# Siemens Java Internship

This is a Spring Boot CRUD application implemented as part of the Siemens internship challenge.  

---

## ✅ Features

- Full CRUD operations for `Item` entities
- Validation using `@Valid`, `@NotBlank`, `@Email`
- Asynchronous processing with `CompletableFuture`
- Thread-safe logic and exception handling
- Proper HTTP status codes (200, 201, 400, 404, 204)
- Unit, integration and performance tests with JUnit 5
- Manual testing via Postman (screenshots available)

---

## 🛠 Technologies Used

- Java 17
- Spring Boot
- Spring Data JPA
- JUnit 5
- Postman

---

## 🚀 How to Run

### ✅ With IntelliJ (recommended)

1. Open project in IntelliJ IDEA
2. Locate the main class: InternshipApplication.java
3. Right-click → **Run 'InternshipApplication'**
4. Wait until console shows: Tomcat started on port(s): 8080
5. 5. Open in browser or Postman: http://localhost:8080/api/items

## 🔗 API Endpoints

| Method | Endpoint             | Description                       |
| ------ | -------------------- | --------------------------------- |
| GET    | `/api/items`         | List all items                    |
| POST   | `/api/items`         | Create new item (with validation) |
| GET    | `/api/items/{id}`    | Get item by ID                    |
| PUT    | `/api/items/{id}`    | Update existing item              |
| DELETE | `/api/items/{id}`    | Delete item                       |
| GET    | `/api/items/process` | Asynchronously process all items  |


## 📸 Postman Screenshots

### 🔹 Create New Item
![Post Item](Siemens/Screnshoots/post.png)

### 🔹 Get All Items
![Get Items](Siemens/Screnshoots/get.png)

### 🔹 Get Item By ID
![Get Items](Siemens/Screnshoots/GetId.png)

### 🔹 Process Items
![Get Items](Siemens/Screnshoots/process.png)

### 🔹 Update Item
![Get Items](Siemens/Screnshoots/put.png)

### 🔹 Delete Item
![Get Items](Siemens/Screnshoots/delete.png)

### 🔹 After Delete Item
![Get Items](Siemens/Screnshoots/After_delete.png)

---

👤 Author
- Submitted by: Dorin Pastinaru
- GitHub: https://github.com/Dorin23

