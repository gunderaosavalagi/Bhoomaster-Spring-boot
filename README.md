# Bhoomaster-Spring-boot

Bhoomaster is a web application designed to streamline property management for builders and users. It enables builders to post multiple properties and users to browse and book properties with an intuitive interface. The application also includes admin functionality for overseeing activities.

## Features

- **Registration and Login**:
  - Single login page for all users.
  - Separate registration pages for users and builders.
  - Email-based OTP verification upon registration.
  - Form validation for both registration and login.
  
- **Home Pages**:
  - User Home: Users can browse and book properties posted by builders.
  - Builder Home: Builders can manage and post multiple properties.
  - Admin Login: Admins can log in to oversee the platform.

- **Property Booking**:
  - Users can book properties listed by builders.
  - Booking data is stored in the database for future use.
  - Future implementation: Email notifications for bookings.

- **Profiles**:
  - User and builder profiles are planned for future implementation.

## Technologies Used

### Frontend
- HTML
- CSS
- JavaScript

### Backend
- Java
- Spring Boot

### Database
- MySQL

## Project Structure

### Java Packages
- **Controller**:
  - `BuilderController.java`
  - `UserController.java`
  - `AdminController.java`
  - `ContactController.java`
- **DTO**:
  - `Builder.java`
  - `User.java`
  - `Property.java`
  - `Booking.java`
- **Helper**:
  - `AES.java`
  - `MyEmailSender.java`
  - `MessageRemover.java`
- **Repository**:
  - `UserRepository.java`
  - `BuilderRepository.java`
  - `PropertyRepository.java`
  - `BookingRepository.java`
- **Service**:
  - `UserService.java`
  - `BuilderService.java`
  - `AdminService.java`
- **Main Application**:
  - `BhoomasterApplication.java`

### Resources
- **Static Templates**:
  - `home.html`
  - `login.html`
  - `user-register.html`
  - `builder-register.html`
  - `user-home.html`
  - `builder-home.html`
  - `otp-template.html`
  - `post-property.html`
  - `admin-login.html`
  - `privacy-policy.html`
  - `terms.html`

## Installation

1. Navigate to the project directory.
2. Configure the MySQL database in `application.properties`.
3. Run the application:
   ```bash
   ./mvnw spring-boot:run
