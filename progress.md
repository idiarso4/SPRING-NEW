# Progress Report for "We Are" Application

## Completed Tasks

### Backend (Java Spring Boot)
- Project structure created.
- Main application class (`Application.java`) implemented.
- Database configuration (`application.properties`) set up for H2.
- Models created:
  - `Student`
  - `Attendance`
  - `Notification`
- Repositories created:
  - `StudentRepository`
  - `AttendanceRepository`
  - `NotificationRepository`
- Services created:
  - `StudentService`
  - `AttendanceService`
  - `NotificationService`
- Controllers created:
  - `StudentController`
  - `AttendanceController`
  - `NotificationController`
- Unit tests created for:
  - Services
  - Controllers

### Frontend (Flutter)
- HTTP client service (`api_service.dart`) implemented to handle API requests.

## Next Steps
1. Create models in the frontend for `Student`, `Attendance`, and `Notification`.
2. Develop UI screens for displaying students, attendance, and notifications.
3. Connect UI to the API using the methods from `ApiService`.
