SUSUNAN **diagram alur (flowchart)** untuk logika absensi dengan geofencing, **diagram relasi antar tabel (ERD)** untuk database, dan **contoh konsumsi API** untuk komunikasi antara frontend dan backend. Karena ini dalam format teks, saya akan menggunakan deskripsi dan representasi sederhana yang mudah dipahami, lalu kamu bisa menggambarkannya lebih lanjut dengan alat seperti Draw.io atau Lucidchart.

---

### **1. Diagram Alur (Flowchart) - Absensi Wajah + Geofencing**
Berikut adalah alur logika absensi siswa dengan validasi wajah dan radius:

```
[Mulai]
   |
[Login Siswa]
   |
[Cek Izin Lokasi]
   | Tidak
   +----> [Tampilkan Pesan: "Aktifkan Lokasi"] ----> [Kembali]
   | Ya
[Ambil Lokasi Siswa]
   |
[Hitung Jarak dari Sekolah]
   | Jarak > Radius
   +----> [Tampilkan Pesan: "Di Luar Radius"] ----> [Kembali]
   | Jarak <= Radius
[Aktifkan Kamera]
   |
[Ambil Foto Wajah]
   |
[Kirim Foto dan Lokasi ke Backend]
   |
[Backend: Validasi Wajah]
   | Gagal
   +----> [Tampilkan Pesan: "Wajah Tidak Cocok"] ----> [Kembali]
   | Berhasil
[Backend: Simpan Data Absensi]
   |
[Kirim Notifikasi: "Absensi Berhasil"]
   |
[Selesai]
```

**Penjelasan:**  
- Siswa login, lalu aplikasi memeriksa lokasi dan memastikan dalam radius sekolah (misalnya, 100 meter).  
- Jika lokasi valid, kamera diaktifkan untuk mengambil foto wajah.  
- Data dikirim ke backend untuk validasi wajah dan penyimpanan absensi.  
- Notifikasi dikirim jika berhasil.

---

### **2. Diagram Relasi Antar Tabel (ERD)**  
Berikut adalah representasi relasi antar tabel dalam database:

```
[Student]                  [Teacher]                [Counselor]
| - id (PK)               | - id (PK)              | - id (PK)
| - name                  | - name                | - name
| - class                 | - subject             | - schedule
| - email                 | - email               |
                          |
[Attendance]              [ClassActivity]         [Extracurricular]
| - id (PK)              | - id (PK)             | - id (PK)
| - student_id (FK)      | - teacher_id (FK)     | - student_id (FK)
| - timestamp            | - student_id (FK)     | - name
| - image_path           | - subject             | - timestamp
| - latitude             | - timestamp           |
| - longitude            |                       |
                          |
[PrayerAttendance]       [CounselingSession]     [DamageReport]
| - id (PK)              | - id (PK)             | - id (PK)
| - student_id (FK)      | - student_id (FK)     | - student_id (FK)
| - timestamp            | - counselor_id (FK)   | - item_name
|                         | - timestamp           | - description
|                         | - notes               | - status
|
[InternshipJournal]      [InternshipPhoto]       [Permission]
| - id (PK)              | - id (PK)             | - id (PK)
| - student_id (FK)      | - journal_id (FK)     | - student_id (FK)
| - date                 | - photo_url           | - reason
| - description          |                       | - status
| - status               |                       | - timestamp
|
[Interaction]            [Notification]          [Schedule]
| - id (PK)              | - id (PK)             | - id (PK)
| - student_id (FK)      | - user_id (FK)        | - activity_type
| - teacher_id (FK)      | - title               | - timestamp
| - message              | - body                | - description
| - timestamp            | - timestamp           |
|
[Location]
| - id (PK)
| - latitude
| - longitude
| - name
```

**Relasi:**  
- `Student` 1:N `Attendance`, `ClassActivity`, `Extracurricular`, `PrayerAttendance`, `CounselingSession`, `DamageReport`, `InternshipJournal`, `Permission`, `Interaction`.  
- `Teacher` 1:N `ClassActivity`, `Interaction`.  
- `Counselor` 1:N `CounselingSession`.  
- `InternshipJournal` 1:N `InternshipPhoto`.  
- `Notification` terkait dengan `user_id` (bisa student, teacher, atau counselor).  
- `Schedule` untuk jadwal semua kegiatan.

**Catatan:**  
- Foreign Key (FK) digunakan untuk menghubungkan tabel.  
- `Location` bisa digunakan untuk menyimpan koordinat sekolah atau zona absensi.

---

### **3. Konsumsi API**

Berikut adalah contoh spesifikasi API dan cara frontend mengonsumsinya.

#### **API Spesifikasi (Backend)**  
**1. POST /api/attendance**  
- **Deskripsi**: Mengirim data absensi wajah dan lokasi.  
- **Request Body**:  
  ```json
  {
    "studentId": "123",
    "image": "base64_encoded_image",
    "latitude": -6.2088,
    "longitude": 106.8456
  }
  ```  
- **Response**:  
  ```json
  {
    "status": "success",
    "message": "Absensi berhasil",
    "attendanceId": "456"
  }
  ```  
  atau  
  ```json
  {
    "status": "error",
    "message": "Di luar radius atau wajah tidak cocok"
  }
  ```

**2. GET /api/schedule**  
- **Deskripsi**: Mengambil jadwal kegiatan.  
- **Response**:  
  ```json
  [
    {
      "id": "789",
      "activityType": "class",
      "timestamp": "2025-02-28T08:00:00",
      "description": "Matematika - Kelas X-A"
    }
  ]
  ```

**3. POST /api/damage-report**  
- **Deskripsi**: Melaporkan kerusakan barang.  
- **Request Body**:  
  ```json
  {
    "studentId": "123",
    "itemName": "Proyektor",
    "description": "Lensa pecah",
    "timestamp": "2025-02-27T10:00:00"
  }
  ```  
- **Response**:  
  ```json
  {
    "status": "success",
    "message": "Laporan diterima",
    "reportId": "101"
  }
  ```

#### **Konsumsi API di Frontend (Flutter)**  
**1. Submit Absensi**  
```dart
import 'package:http/http.dart' as http;
import 'dart:convert';

class AttendanceService {
  final String baseUrl = 'http://your-backend-url/api';

  Future<bool> submitAttendance(String imagePath, double latitude, double longitude) async {
    final uri = Uri.parse('$baseUrl/attendance');
    final imageBytes = await File(imagePath).readAsBytes();
    String base64Image = base64Encode(imageBytes);

    final response = await http.post(
      uri,
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({
        'studentId': '123', // Ganti dengan ID siswa dari auth
        'image': base64Image,
        'latitude': latitude,
        'longitude': longitude,
      }),
    );

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      return data['status'] == 'success';
    }
    return false;
  }
}
```

**2. Ambil Jadwal**  
```dart
class ScheduleService {
  final String baseUrl = 'http://your-backend-url/api';

  Future<List<Schedule>> getSchedules() async {
    final uri = Uri.parse('$baseUrl/schedule');
    final response = await http.get(uri);

    if (response.statusCode == 200) {
      final List<dynamic> data = jsonDecode(response.body);
      return data.map((json) => Schedule.fromJson(json)).toList();
    }
    throw Exception('Gagal mengambil jadwal');
  }
}

class Schedule {
  final String id;
  final String activityType;
  final DateTime timestamp;
  final String description;

  Schedule({required this.id, required this.activityType, required this.timestamp, required this.description});

  factory Schedule.fromJson(Map<String, dynamic> json) {
    return Schedule(
      id: json['id'],
      activityType: json['activityType'],
      timestamp: DateTime.parse(json['timestamp']),
      description: json['description'],
    );
  }
}
```

**3. Laporan Kerusakan**  
```dart
class DamageReportService {
  final String baseUrl = 'http://your-backend-url/api';

  Future<bool> submitDamageReport(String itemName, String description) async {
    final uri = Uri.parse('$baseUrl/damage-report');
    final response = await http.post(
      uri,
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({
        'studentId': '123', // Ganti dengan ID siswa dari auth
        'itemName': itemName,
        'description': description,
        'timestamp': DateTime.now().toIso8601String(),
      }),
    );

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      return data['status'] == 'success';
    }
    return false;
  }
}
```

---

### **Catatan Tambahan**
1. **Keamanan API**: Tambahkan autentikasi (misalnya, JWT) pada setiap endpoint untuk memastikan hanya pengguna terotorisasi yang bisa mengakses.  
2. **Error Handling**: Pastikan frontend menangani error seperti koneksi gagal atau respons tidak valid.  
3. **Optimasi**: Gunakan pagination untuk endpoint seperti `/api/schedule` jika datanya banyak.

===========================
Berikut adalah **checklist untuk roadmap pengembangan aplikasi** serta **step-by-step instalasi** untuk backend (Java Spring Boot) dan frontend (Flutter). Checklist ini mencakup tahapan pengembangan fitur yang telah kita bahas sebelumnya, sedangkan langkah instalasi akan memandu kamu untuk menyiapkan lingkungan pengembangan.

---

### **Checklist untuk Roadmap Pengembangan Aplikasi**

#### **Tahap 1: Perencanaan dan Setup Awal**
- [ ] Tentukan kebutuhan fungsional (sudah dilakukan: absensi wajah + radius, PKL, perizinan, dll.).
- [ ] Desain struktur folder backend dan frontend (sudah dibuat).
- [ ] Buat diagram alur dan ERD (sudah dibuat).
- [ ] Siapkan lingkungan pengembangan (IDE, database, tools).

#### **Tahap 2: Pengembangan Backend**
- [ ] Setup proyek Spring Boot dengan Maven.
- [ ] Implementasi model dan repository untuk:
  - [ ] Student, Teacher, Counselor
  - [ ] Attendance (wajah + radius), ClassActivity, Extracurricular, PrayerAttendance
  - [ ] CounselingSession, DamageReport, InternshipJournal, InternshipPhoto
  - [ ] Permission, Interaction, Notification, Schedule, Dashboard
- [ ] Implementasi service:
  - [ ] Logika geofencing di `MapService`.
  - [ ] Validasi wajah di `AttendanceService`.
  - [ ] Notifikasi di `NotificationService`.
- [ ] Implementasi controller untuk semua endpoint API.
- [ ] Integrasi dengan database (misalnya, PostgreSQL atau MySQL).
- [ ] Integrasi dengan penyimpanan cloud (misalnya, AWS S3 untuk foto PKL).
- [ ] Integrasi Firebase untuk notifikasi.

#### **Tahap 3: Pengembangan Frontend**
- [ ] Setup proyek Flutter.
- [ ] Implementasi model dan service untuk semua entitas.
- [ ] Desain UI untuk layar:
  - [ ] Login, Dashboard, Schedule
  - [ ] Attendance (wajah + radius), ClassActivity, Extracurricular, Prayer
  - [ ] Counseling, DamageReport, Internship, Permission, Interaction, Notification
- [ ] Integrasi kamera untuk absensi wajah.
- [ ] Integrasi geolocator untuk geofencing.
- [ ] Integrasi Firebase Messaging untuk notifikasi.
- [ ] Konsumsi semua API dari backend.

#### **Tahap 4: Integrasi dan Pengujian**
- [ ] Hubungkan frontend dengan backend via API.
- [ ] Uji fitur:
  - [ ] Absensi wajah + radius
  - [ ] Jurnal PKL dan upload foto
  - [ ] Perizinan dan status WFO/WFA
  - [ ] Absensi kelas, ekstrakurikuler, shalat
  - [ ] Bimbingan konseling dan laporan kerusakan
  - [ ] Notifikasi dan jadwal
- [ ] Uji performa (beban pengguna, latency API).
- [ ] Uji keamanan (autentikasi, enkripsi data).

#### **Tahap 5: Deployment**
- [ ] Deploy backend ke server (misalnya, AWS EC2, Heroku).
- [ ] Build aplikasi Flutter untuk Android/iOS.
- [ ] Publikasikan APK ke Google Play Store atau IPA ke App Store.
- [ ] Siapkan monitoring (log, error tracking).

#### **Tahap 6: Maintenance**
- [ ] Pantau bug dan feedback pengguna.
- [ ] Tambahkan fitur baru sesuai kebutuhan.
- [ ] Perbarui dependensi secara berkala.

---

### **Step-by-Step Instalasi**

#### **1. Instalasi Backend (Java Spring Boot)**

**Prasyarat:**
- Java JDK 17 atau lebih baru
- Maven 3.6+
- IDE (IntelliJ IDEA atau Eclipse)
- Database (PostgreSQL/MySQL)
- Git

**Langkah-langkah:**
1. **Instal Java JDK**
   - Unduh JDK dari [Oracle](https://www.oracle.com/java/technologies/javase-downloads.html) atau gunakan OpenJDK.
   - Setel variabel lingkungan:
     ```bash
     export JAVA_HOME=/path/to/jdk
     export PATH=$JAVA_HOME/bin:$PATH
     ```
   - Verifikasi: `java -version`.

2. **Instal Maven**
   - Unduh dari [Maven](https://maven.apache.org/download.cgi).
   - Ekstrak dan tambahkan ke PATH:
     ```bash
     export PATH=/path/to/maven/bin:$PATH
     ```
   - Verifikasi: `mvn -version`.

3. **Instal Database (PostgreSQL)**
   - Unduh dari [PostgreSQL](https://www.postgresql.org/download/).
   - Instal dan buat database:
     ```bash
     psql -U postgres
     CREATE DATABASE student_attendance;
     ```
   - Catat username dan password.

4. **Clone dan Setup Proyek**
   - Clone repository (jika sudah ada) atau buat proyek baru:
     ```bash
     mvn archetype:generate -DgroupId=com.attendance -DartifactId=student-attendance-backend -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
     cd student-attendance-backend
     ```
   - Edit `pom.xml` untuk menambahkan dependensi (contoh):
     ```xml
     <dependencies>
       <dependency>
         <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-starter-web</artifactId>
         <version>3.0.0</version>
       </dependency>
       <dependency>
         <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-starter-data-jpa</artifactId>
         <version>3.0.0</version>
       </dependency>
       <dependency>
         <groupId>org.postgresql</groupId>
         <artifactId>postgresql</artifactId>
         <version>42.5.0</version>
       </dependency>
     </dependencies>
     ```

5. **Konfigurasi Database**
   - Edit `src/main/resources/application.properties`:
     ```properties
     spring.datasource.url=jdbc:postgresql://localhost:5432/student_attendance
     spring.datasource.username=postgres
     spring.datasource.password=your_password
     spring.jpa.hibernate.ddl-auto=update
     ```

6. **Jalankan Aplikasi**
   - Dari terminal:
     ```bash
     mvn spring-boot:run
     ```
   - Atau buka di IDE dan jalankan `Application.java`.

---

#### **2. Instalasi Frontend (Flutter)**

**Prasyarat:**
- Flutter SDK 3.0+
- Dart (termasuk dalam Flutter)
- Android Studio atau VS Code
- Emulator Android atau perangkat fisik
- Git

**Langkah-langkah:**
1. **Instal Flutter**
   - Unduh dari [Flutter](https://flutter.dev/docs/get-started/install).
   - Ekstrak dan tambahkan ke PATH:
     ```bash
     export PATH=/path/to/flutter/bin:$PATH
     ```
   - Verifikasi: `flutter doctor`.

2. **Setup Emulator atau Perangkat**
   - Instal Android Studio, buka AVD Manager, dan buat emulator.
   - Atau sambungkan perangkat fisik via USB dengan USB Debugging aktif.

3. **Buat Proyek Flutter**
   - Jalankan:
     ```bash
     flutter create student_attendance_frontend
     cd student_attendance_frontend
     ```
   - Edit `pubspec.yaml` untuk menambahkan dependensi:
     ```yaml
     dependencies:
       flutter:
         sdk: flutter
       flutter_map: ^6.1.0
       latlong2: ^0.9.0
       geolocator: ^10.0.0
       camera: ^0.10.0
       image_picker: ^1.0.0
       http: ^1.0.0
       firebase_messaging: ^14.0.0
     ```

4. **Instal Dependensi**
   - Jalankan:
     ```bash
     flutter pub get
     ```

5. **Integrasi Firebase (Opsional untuk Notifikasi)**
   - Buat proyek di [Firebase Console](https://console.firebase.google.com/).
   - Tambahkan aplikasi Android/iOS dan unduh `google-services.json` (Android) atau `GoogleService-Info.plist` (iOS).
   - Letakkan file di:
     - Android: `android/app/`
     - iOS: `ios/Runner/`
   - Tambahkan konfigurasi di `android/build.gradle` dan `android/app/build.gradle`.

6. **Jalankan Aplikasi**
   - Pastikan emulator berjalan atau perangkat tersambung.
   - Jalankan:
     ```bash
     flutter run
     ```

---

### **Tips Tambahan**
- **Backend**: Gunakan Postman untuk menguji API setelah backend berjalan.
- **Frontend**: Gunakan `flutter analyze` dan `flutter test` untuk memeriksa kode.
- **Version Control**: Inisialisasi Git di kedua proyek:
  ```bash
  git init
  git add .
  git commit -m "Initial commit"
  ```
Baik, saya akan menambahkan detail untuk **konfigurasi Firebase** (untuk notifikasi di frontend Flutter) dan **pengujian API** (menggunakan Postman untuk backend). Ini akan melengkapi langkah-langkah instalasi yang telah dibuat sebelumnya.

---

### **Detail Tambahan untuk Instalasi**

#### **1. Konfigurasi Firebase untuk Notifikasi di Frontend (Flutter)**

Firebase digunakan untuk mengirim notifikasi push ke aplikasi Flutter, seperti "Absensi Berhasil" atau "Laporan Kerusakan Diterima". Berikut langkah-langkahnya:

**Prasyarat:**
- Akun Google dan akses ke [Firebase Console](https://console.firebase.google.com/).
- Proyek Flutter sudah dibuat (`student_attendance_frontend`).

**Langkah-langkah:**

1. **Buat Proyek Firebase**
   - Buka Firebase Console, klik **Add Project**, dan beri nama (misalnya, `student-attendance`).
   - Aktifkan **Cloud Messaging** di menu **Cloud Messaging** (bagian kiri).

2. **Tambahkan Aplikasi Android**
   - Klik ikon Android di halaman proyek Firebase.
   - Masukkan package name (dari `android/app/src/main/AndroidManifest.xml`), misalnya `com.attendance.student`.
   - Unduh file `google-services.json`.
   - Letakkan file di `student_attendance_frontend/android/app/`.

3. **Tambahkan Aplikasi iOS (Opsional)**
   - Klik ikon iOS di Firebase Console.
   - Masukkan Bundle ID (dari `ios/Runner/Info.plist`), misalnya `com.attendance.student`.
   - Unduh file `GoogleService-Info.plist`.
   - Letakkan file di `student_attendance_frontend/ios/Runner/` via Xcode.

4. **Konfigurasi File Gradle (Android)**
   - Edit `android/build.gradle`:
     ```gradle
     buildscript {
       dependencies {
         classpath 'com.google.gms:google-services:4.3.15'
       }
     }
     ```
   - Edit `android/app/build.gradle`:
     ```gradle
     apply plugin: 'com.google.gms.google-services'
     dependencies {
       implementation 'com.google.firebase:firebase-messaging:23.0.0'
     }
     ```

5. **Tambahkan Dependensi di Flutter**
   - Edit `pubspec.yaml`:
     ```yaml
     dependencies:
       firebase_core: ^2.4.0
       firebase_messaging: ^14.2.1
     ```
   - Jalankan:
     ```bash
     flutter pub get
     ```

6. **Inisialisasi Firebase di Aplikasi**
   - Edit `lib/main.dart`:
     ```dart
     import 'package:firebase_core/firebase_core.dart';
     import 'package:firebase_messaging/firebase_messaging.dart';

     Future<void> _firebaseMessagingBackgroundHandler(RemoteMessage message) async {
       print("Background message: ${message.notification?.title}");
     }

     void main() async {
       WidgetsFlutterBinding.ensureInitialized();
       await Firebase.initializeApp();
       FirebaseMessaging.onBackgroundMessage(_firebaseMessagingBackgroundHandler);
       runApp(MyApp());
     }
     ```

7. **Konfigurasi Notifikasi di Foreground**
   - Tambahkan di `lib/services/notification_service.dart`:
     ```dart
     import 'package:firebase_messaging/firebase_messaging.dart';

     class NotificationService {
       final FirebaseMessaging _firebaseMessaging = FirebaseMessaging.instance;

       Future<void> initialize() async {
         await _firebaseMessaging.requestPermission();
         String? token = await _firebaseMessaging.getToken();
         print("FCM Token: $token"); // Simpan token ini untuk dikirim ke backend

         FirebaseMessaging.onMessage.listen((RemoteMessage message) {
           print('Foreground message: ${message.notification?.title}');
           // Tampilkan notifikasi di UI
         });

         FirebaseMessaging.onMessageOpenedApp.listen((RemoteMessage message) {
           print('Message clicked: ${message.notification?.title}');
           // Navigasi ke layar tertentu
         });
       }
     }
     ```
   - Panggil `NotificationService().initialize()` di `initState` layar utama.

8. **Integrasi dengan Backend**
   - Backend perlu mengirim notifikasi via Firebase Admin SDK. Tambahkan di `pom.xml`:
     ```xml
     <dependency>
       <groupId>com.google.firebase</groupId>
       <artifactId>firebase-admin</artifactId>
       <version>9.1.1</version>
     </dependency>
     ```
   - Contoh di `NotificationService.java`:
     ```java
     import com.google.firebase.messaging.FirebaseMessaging;
     import com.google.firebase.messaging.Message;

     @Service
     public class NotificationService {
         public void sendNotification(String token, String title, String body) throws Exception {
             Message message = Message.builder()
                 .setToken(token) // Token dari frontend
                 .setNotification(com.google.firebase.messaging.Notification.builder()
                     .setTitle(title)
                     .setBody(body)
                     .build())
                 .build();
             String response = FirebaseMessaging.getInstance().send(message);
             System.out.println("Notifikasi terkirim: " + response);
         }
     }
     ```
   - Panggil fungsi ini dari controller, misalnya setelah absensi berhasil.

9. **Uji Notifikasi**
   - Jalankan aplikasi di emulator/perangkat:
     ```bash
     flutter run
     ```
   - Kirim notifikasi tes dari Firebase Console (Cloud Messaging > New Notification).

---

#### **2. Pengujian API Menggunakan Postman**

Postman digunakan untuk menguji endpoint API backend setelah proyek Spring Boot berjalan.

**Prasyarat:**
- Postman terinstal ([Download Postman](https://www.postman.com/downloads/)).
- Backend berjalan di `localhost:8080` (default port Spring Boot).

**Langkah-langkah:**

1. **Instal Postman**
   - Unduh dan instal dari situs resmi.
   - Buka aplikasi Postman.

2. **Jalankan Backend**
   - Pastikan aplikasi Spring Boot aktif:
     ```bash
     cd student-attendance-backend
     mvn spring-boot:run
     ```

3. **Buat Request Baru**
   - Klik **New** > **HTTP Request** di Postman.
   - Pilih metode (misalnya, `POST`) dan masukkan URL (contoh: `http://localhost:8080/api/attendance`).

4. **Uji Endpoint Absensi**
   - **Metode**: POST
   - **URL**: `http://localhost:8080/api/attendance`
   - **Headers**: 
     ```
     Content-Type: application/json
     ```
   - **Body** (raw JSON):
     ```json
     {
       "studentId": "123",
       "image": "base64_encoded_image_string",
       "latitude": -6.2088,
       "longitude": 106.8456
     }
     ```
   - Klik **Send** dan periksa respons:
     ```json
     {
       "status": "success",
       "message": "Absensi berhasil",
       "attendanceId": "456"
     }
     ```

5. **Uji Endpoint Jadwal**
   - **Metode**: GET
   - **URL**: `http://localhost:8080/api/schedule`
   - Klik **Send** dan periksa respons:
     ```json
     [
       {
         "id": "789",
         "activityType": "class",
         "timestamp": "2025-02-28T08:00:00",
         "description": "Matematika - Kelas X-A"
       }
     ]
     ```

6. **Uji Endpoint Laporan Kerusakan**
   - **Metode**: POST
   - **URL**: `http://localhost:8080/api/damage-report`
   - **Headers**: 
     ```
     Content-Type: application/json
     ```
   - **Body** (raw JSON):
     ```json
     {
       "studentId": "123",
       "itemName": "Proyektor",
       "description": "Lensa pecah",
       "timestamp": "2025-02-27T10:00:00"
     }
     ```
   - Klik **Send** dan periksa respons:
     ```json
     {
       "status": "success",
       "message": "Laporan diterima",
       "reportId": "101"
     }
     ```

7. **Tambahkan Autentikasi (Opsional)**
   - Jika menggunakan JWT, tambahkan header:
     ```
     Authorization: Bearer <your_jwt_token>
     ```
   - Uji endpoint yang memerlukan autentikasi setelah login.

8. **Simpan dan Otomatisasi**
   - Simpan request ke koleksi (misalnya, `Student Attendance API`).
   - Buat environment di Postman (misalnya, `localhost`) dengan variabel seperti `base_url: http://localhost:8080`.
   - Gunakan runner untuk mengotomatisasi pengujian banyak endpoint.

---

### **Tips Tambahan**
- **Firebase**: Simpan FCM token di backend untuk setiap pengguna agar notifikasi dapat dikirim secara spesifik.
- **Postman**: Gunakan fitur **Tests** di Postman untuk memvalidasi respons otomatis (contoh: `pm.test("Status is success", () => { pm.expect(pm.response.json().status).to.eql("success"); });`).
- **Logging**: Tambahkan log di backend (`log.info`) untuk memantau respons API selama pengujian.


Baik, saya akan menambahkan **implementasi JWT untuk autentikasi API** di backend Spring Boot, **pengujian menggunakan Postman** dengan skrip tes, dan alternatif **pengujian dengan Thunder Client** (plugin VS Code). Berikut detailnya:

---

### **1. Implementasi JWT untuk Autentikasi API (Backend Spring Boot)**

JWT (JSON Web Token) digunakan untuk mengamankan endpoint API agar hanya pengguna terotentikasi yang bisa mengaksesnya.

#### **Langkah-langkah:**

1. **Tambahkan Dependensi di `pom.xml`**
   ```xml
   <dependencies>
     <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-security</artifactId>
       <version>3.0.0</version>
     </dependency>
     <dependency>
       <groupId>io.jsonwebtoken</groupId>
       <artifactId>jjwt</artifactId>
       <version>0.9.1</version>
     </dependency>
   </dependencies>
   ```

2. **Konfigurasi Security (`SecurityConfig.java`)**
   ```java
   package com.attendance.config;

   import org.springframework.context.annotation.Bean;
   import org.springframework.context.annotation.Configuration;
   import org.springframework.security.config.annotation.web.builders.HttpSecurity;
   import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
   import org.springframework.security.config.http.SessionCreationPolicy;
   import org.springframework.security.web.SecurityFilterChain;
   import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

   @Configuration
   @EnableWebSecurity
   public class SecurityConfig {

       @Bean
       public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {
           http
               .csrf().disable()
               .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
               .and()
               .authorizeHttpRequests()
               .requestMatchers("/api/auth/**").permitAll() // Izinkan endpoint login
               .anyRequest().authenticated()
               .and()
               .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
           return http.build();
       }
   }
   ```

3. **Buat JWT Utility (`JwtUtil.java`)**
   ```java
   package com.attendance.config;

   import io.jsonwebtoken.Claims;
   import io.jsonwebtoken.Jwts;
   import io.jsonwebtoken.SignatureAlgorithm;
   import org.springframework.stereotype.Component;

   import java.util.Date;

   @Component
   public class JwtUtil {
       private final String SECRET_KEY = "your-secret-key"; // Ganti dengan kunci rahasia yang kuat
       private final long EXPIRATION_TIME = 86400000; // 24 jam dalam milidetik

       public String generateToken(String username) {
           return Jwts.builder()
               .setSubject(username)
               .setIssuedAt(new Date())
               .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
               .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
               .compact();
       }

       public String extractUsername(String token) {
           return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
       }

       public boolean validateToken(String token) {
           try {
               Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
               return true;
           } catch (Exception e) {
               return false;
           }
       }
   }
   ```

4. **Buat Filter JWT (`JwtFilter.java`)**
   ```java
   package com.attendance.config;

   import jakarta.servlet.FilterChain;
   import jakarta.servlet.ServletException;
   import jakarta.servlet.http.HttpServletRequest;
   import jakarta.servlet.http.HttpServletResponse;
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
   import org.springframework.security.core.context.SecurityContextHolder;
   import org.springframework.security.core.userdetails.UserDetails;
   import org.springframework.security.core.userdetails.UserDetailsService;
   import org.springframework.stereotype.Component;
   import org.springframework.web.filter.OncePerRequestFilter;

   import java.io.IOException;

   @Component
   public class JwtFilter extends OncePerRequestFilter {

       @Autowired
       private JwtUtil jwtUtil;

       @Autowired
       private UserDetailsService userDetailsService;

       @Override
       protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
               throws ServletException, IOException {
           String header = request.getHeader("Authorization");
           if (header != null && header.startsWith("Bearer ")) {
               String token = header.substring(7);
               if (jwtUtil.validateToken(token)) {
                   String username = jwtUtil.extractUsername(token);
                   UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                   UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                       userDetails, null, userDetails.getAuthorities());
                   SecurityContextHolder.getContext().setAuthentication(auth);
               }
           }
           chain.doFilter(request, response);
       }
   }
   ```

5. **Buat Endpoint Login (`AuthController.java`)**
   ```java
   package com.attendance.controller;

   import com.attendance.config.JwtUtil;
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.http.ResponseEntity;
   import org.springframework.security.authentication.AuthenticationManager;
   import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
   import org.springframework.web.bind.annotation.*;

   @RestController
   @RequestMapping("/api/auth")
   public class AuthController {

       @Autowired
       private AuthenticationManager authenticationManager;

       @Autowired
       private JwtUtil jwtUtil;

       @PostMapping("/login")
       public ResponseEntity<String> login(@RequestBody LoginRequest request) {
           authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
           );
           String token = jwtUtil.generateToken(request.getUsername());
           return ResponseEntity.ok(token);
       }
   }

   class LoginRequest {
       private String username;
       private String password;

       // Getter dan Setter
       public String getUsername() { return username; }
       public void setUsername(String username) { this.username = username; }
       public String getPassword() { return password; }
       public void setPassword(String password) { this.password = password; }
   }
   ```

6. **Konfigurasi UserDetailsService**
   - Implementasikan `UserDetailsService` untuk memuat pengguna dari database:
     ```java
     package com.attendance.service;

     import org.springframework.security.core.userdetails.User;
     import org.springframework.security.core.userdetails.UserDetails;
     import org.springframework.security.core.userdetails.UserDetailsService;
     import org.springframework.security.core.userdetails.UsernameNotFoundException;
     import org.springframework.stereotype.Service;

     @Service
     public class CustomUserDetailsService implements UserDetailsService {
         @Override
         public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
             // Ganti dengan logika database
             if ("admin".equals(username)) {
                 return User.withUsername("admin")
                     .password("{noop}password") // Ganti dengan hash password
                     .roles("USER")
                     .build();
             }
             throw new UsernameNotFoundException("User not found");
         }
     }
     ```

7. **Tambahkan di `application.properties`**
   ```properties
   jwt.secret=your-secret-key
   ```

---

### **2. Pengujian API Menggunakan Postman**

#### **Langkah-langkah:**

1. **Login untuk Mendapatkan Token**
   - **Metode**: POST
   - **URL**: `http://localhost:8080/api/auth/login`
   - **Body** (raw JSON):
     ```json
     {
       "username": "admin",
       "password": "password"
     }
     ```
   - **Respons**:
     ```json
     "eyJhbGciOiJIUzI1NiJ9..." // Token JWT
     ```
   - Simpan token ini.

2. **Uji Endpoint Absensi dengan JWT**
   - **Metode**: POST
   - **URL**: `http://localhost:8080/api/attendance`
   - **Headers**:
     ```
     Content-Type: application/json
     Authorization: Bearer <paste_token_here>
     ```
   - **Body**:
     ```json
     {
       "studentId": "123",
       "image": "base64_encoded_image",
       "latitude": -6.2088,
       "longitude": 106.8456
     }
     ```
   - **Skrip Tes di Tab "Tests"**:
     ```javascript
     pm.test("Status is success", function () {
         pm.response.to.have.status(200);
         var jsonData = pm.response.json();
         pm.expect(jsonData.status).to.eql("success");
         pm.expect(jsonData.attendanceId).to.exist;
     });
     ```
   - Klik **Send** dan periksa hasil.

3. **Uji Endpoint Jadwal**
   - **Metode**: GET
   - **URL**: `http://localhost:8080/api/schedule`
   - **Headers**:
     ```
     Authorization: Bearer <paste_token_here>
     ```
   - **Skrip Tes**:
     ```javascript
     pm.test("Response is array", function () {
         pm.response.to.have.status(200);
         var jsonData = pm.response.json();
         pm.expect(jsonData).to.be.an("array");
         pm.expect(jsonData[0].id).to.exist;
     });
     ```

4. **Otomatisasi**
   - Simpan semua request ke koleksi `Student Attendance API`.
   - Gunakan **Collection Runner** untuk menjalankan semua tes sekaligus.

---

### **3. Alternatif: Pengujian dengan Thunder Client (VS Code)**

Thunder Client adalah plugin ringan di VS Code untuk menguji API, sebagai alternatif Postman.

#### **Langkah-langkah:**

1. **Instal Thunder Client**
   - Buka VS Code, masuk ke **Extensions** (Ctrl+Shift+X).
   - Cari "Thunder Client" dan klik **Install**.

2. **Buat Request Baru**
   - Klik ikon Thunder Client di sidebar kiri VS Code.
   - Klik **New Request**.

3. **Uji Endpoint Login**
   - **Metode**: POST
   - **URL**: `http://localhost:8080/api/auth/login`
   - **Body** (JSON):
     ```json
     {
       "username": "admin",
       "password": "password"
     }
     ```
   - Klik **Send**, simpan token dari respons.

4. **Uji Endpoint Absensi**
   - **Metode**: POST
   - **URL**: `http://localhost:8080/api/attendance`
   - **Headers**:
     - Klik tab **Headers**, tambahkan:
       ```
       Key: Authorization, Value: Bearer <paste_token_here>
       Key: Content-Type, Value: application/json
       ```
   - **Body** (JSON):
     ```json
     {
       "studentId": "123",
       "image": "base64_encoded_image",
       "latitude": -6.2088,
       "longitude": 106.8456
     }
     ```
   - Klik **Send**, periksa respons.

5. **Simpan dan Kelola**
   - Klik **Save** untuk menyimpan request ke koleksi.
   - Gunakan **Collections** di Thunder Client untuk mengelompokkan semua request.

6. **Skrip Tes (Opsional)**
   - Thunder Client mendukung skrip sederhana di tab **Test**:
     ```javascript
     tc.test("Status is success", function() {
         tc.response.status.expect(200);
         tc.response.body.status.expect("success");
     });
     ```

#### **Keunggulan Thunder Client vs Postman**
- Ringan, terintegrasi langsung di VS Code.
- Cocok untuk proyek kecil hingga menengah.
- Kekurangan: Fitur otomatisasi dan kolaborasi lebih terbatas dibanding Postman.

---

### **Catatan Tambahan**
- **JWT di Frontend**: Simpan token di penyimpanan lokal (contoh: `shared_preferences` di Flutter) dan kirim di header setiap request.
- **Pengujian**: Uji kasus gagal (token salah, radius di luar batas) untuk memastikan error handling berfungsi.

Baik, saya akan menambahkan **cara menyimpan token JWT di Flutter** menggunakan `shared_preferences` untuk frontend dan **contoh skrip tes lebih kompleks** untuk Postman dan Thunder Client. Ini akan melengkapi implementasi autentikasi dan pengujian API yang sudah kita bahas.

---

### **1. Menyimpan Token JWT di Flutter (Frontend)**

Kita akan menggunakan package `shared_preferences` untuk menyimpan token JWT di penyimpanan lokal perangkat, sehingga token dapat digunakan di setiap request API yang memerlukan autentikasi.

#### **Langkah-langkah:**

1. **Tambahkan Dependensi di `pubspec.yaml`**
   ```yaml
   dependencies:
     shared_preferences: ^2.0.15
   ```
   - Jalankan:
     ```bash
     flutter pub get
     ```

2. **Buat Service untuk Auth (`auth_service.dart`)**
   ```dart
   import 'package:http/http.dart' as http;
   import 'package:shared_preferences/shared_preferences.dart';
   import 'dart:convert';

   class AuthService {
     final String baseUrl = 'http://your-backend-url/api';

     // Login dan simpan token
     Future<bool> login(String username, String password) async {
       final uri = Uri.parse('$baseUrl/auth/login');
       final response = await http.post(
         uri,
         headers: {'Content-Type': 'application/json'},
         body: jsonEncode({
           'username': username,
           'password': password,
         }),
       );

       if (response.statusCode == 200) {
         final token = response.body; // Token JWT dari backend
         await _saveToken(token);
         return true;
       }
       return false;
     }

     // Simpan token ke SharedPreferences
     Future<void> _saveToken(String token) async {
       final prefs = await SharedPreferences.getInstance();
       await prefs.setString('jwt_token', token);
     }

     // Ambil token dari SharedPreferences
     Future<String?> getToken() async {
       final prefs = await SharedPreferences.getInstance();
       return prefs.getString('jwt_token');
     }

     // Logout dan hapus token
     Future<void> logout() async {
       final prefs = await SharedPreferences.getInstance();
       await prefs.remove('jwt_token');
     }

     // Contoh penggunaan token di request
     Future<bool> submitAttendance(String imagePath, double latitude, double longitude) async {
       final token = await getToken();
       if (token == null) return false;

       final uri = Uri.parse('$baseUrl/attendance');
       final imageBytes = await File(imagePath).readAsBytes();
       String base64Image = base64Encode(imageBytes);

       final response = await http.post(
         uri,
         headers: {
           'Content-Type': 'application/json',
           'Authorization': 'Bearer $token',
         },
         body: jsonEncode({
           'studentId': '123', // Ganti dengan ID siswa dari auth
           'image': base64Image,
           'latitude': latitude,
           'longitude': longitude,
         }),
       );

       if (response.statusCode == 200) {
         final data = jsonDecode(response.body);
         return data['status'] == 'success';
       }
       return false;
     }
   }
   ```

3. **Integrasi di Layar Login**
   - Edit `login_screen.dart`:
     ```dart
     import 'package:flutter/material.dart';
     import 'services/auth_service.dart';

     class LoginScreen extends StatefulWidget {
       @override
       _LoginScreenState createState() => _LoginScreenState();
     }

     class _LoginScreenState extends State<LoginScreen> {
       final AuthService _authService = AuthService();
       final TextEditingController _usernameController = TextEditingController();
       final TextEditingController _passwordController = TextEditingController();

       Future<void> _login() async {
         bool success = await _authService.login(
           _usernameController.text,
           _passwordController.text,
         );
         if (success) {
           Navigator.pushReplacementNamed(context, '/home');
         } else {
           ScaffoldMessenger.of(context).showSnackBar(
             SnackBar(content: Text('Login gagal')),
           );
         }
       }

       @override
       Widget build(BuildContext context) {
         return Scaffold(
           appBar: AppBar(title: Text('Login')),
           body: Padding(
             padding: EdgeInsets.all(16.0),
             child: Column(
               children: [
                 TextField(
                   controller: _usernameController,
                   decoration: InputDecoration(labelText: 'Username'),
                 ),
                 TextField(
                   controller: _passwordController,
                   decoration: InputDecoration(labelText: 'Password'),
                   obscureText: true,
                 ),
                 SizedBox(height: 20),
                 ElevatedButton(
                   onPressed: _login,
                   child: Text('Login'),
                 ),
               ],
             ),
           ),
         );
       }
     }
     ```

4. **Gunakan Token di Main App**
   - Edit `main.dart` untuk cek token saat aplikasi dibuka:
     ```dart
     import 'package:flutter/material.dart';
     import 'services/auth_service.dart';

     void main() async {
       WidgetsFlutterBinding.ensureInitialized();
       final AuthService authService = AuthService();
       final token = await authService.getToken();
       runApp(MyApp(initialRoute: token != null ? '/home' : '/login'));
     }

     class MyApp extends StatelessWidget {
       final String initialRoute;

       MyApp({required this.initialRoute});

       @override
       Widget build(BuildContext context) {
         return MaterialApp(
           initialRoute: initialRoute,
           routes: {
             '/login': (context) => LoginScreen(),
             '/home': (context) => HomeScreen(),
           },
         );
       }
     }
     ```

---

### **2. Contoh Skrip Tes Lebih Kompleks**

#### **Postman**
Berikut adalah skrip tes kompleks untuk endpoint `/api/attendance`:

```javascript
// Tes 1: Status code dan struktur respons
pm.test("Response status is 200 and structure is valid", function () {
    pm.response.to.have.status(200);
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.be.an("object");
    pm.expect(jsonData.status).to.eql("success");
    pm.expect(jsonData.message).to.be.a("string");
    pm.expect(jsonData.attendanceId).to.be.a("string");
});

// Tes 2: Validasi waktu respons
pm.test("Response time is under 2 seconds", function () {
    pm.expect(pm.response.responseTime).to.be.below(2000);
});

// Tes 3: Simpan attendanceId ke variabel environment
pm.test("Save attendanceId to environment", function () {
    var jsonData = pm.response.json();
    pm.environment.set("lastAttendanceId", jsonData.attendanceId);
});

// Tes 4: Uji kasus gagal (token salah)
if (pm.request.headers.get("Authorization") === "Bearer invalid_token") {
    pm.test("Response should be 401 for invalid token", function () {
        pm.response.to.have.status(401);
        var jsonData = pm.response.json();
        pm.expect(jsonData.status).to.eql("error");
        pm.expect(jsonData.message).to.include("token");
    });
}
```

**Cara Menggunakan:**
- Tambahkan skrip ini di tab **Tests** pada request POST `/api/attendance`.
- Uji dengan token valid, lalu ubah header `Authorization` ke `Bearer invalid_token` untuk tes kasus gagal.

#### **Thunder Client**
Skrip tes di Thunder Client lebih sederhana, tetapi berikut contoh kompleks:

```javascript
// Tes untuk endpoint /api/attendance
tc.test("Status is 200 and success", function() {
    tc.response.status.expect(200);
    tc.response.body.status.expect("success");
    tc.response.body.attendanceId.expect.to.exist;
});

tc.test("Response time under 2s", function() {
    tc.response.time.expect.to.be.below(2000);
});

tc.test("Save attendanceId", function() {
    tc.env.set("attendanceId", tc.response.body.attendanceId);
});

if (tc.request.headers.Authorization === "Bearer invalid_token") {
    tc.test("Invalid token returns 401", function() {
        tc.response.status.expect(401);
        tc.response.body.status.expect("error");
    });
}
```

**Cara Menggunakan:**
- Tambahkan skrip ini di tab **Test** pada request di Thunder Client.
- Jalankan request dengan token valid dan invalid untuk menguji kedua skenario.

---

### **Catatan Tambahan**
- **Flutter**: Tambahkan penanganan error di `AuthService` untuk kasus token kadaluarsa (refresh token jika perlu).
- **Postman**: Gunakan **Pre-request Script** untuk mengambil token secara otomatis sebelum menjalankan request lain:
  ```javascript
  pm.sendRequest({
      url: 'http://localhost:8080/api/auth/login',
      method: 'POST',
      header: 'Content-Type:application/json',
      body: {
          mode: 'raw',
          raw: JSON.stringify({ username: "admin", password: "password" })
      }
  }, function (err, res) {
      pm.environment.set("jwt_token", res.text());
  });
  ```
- **Thunder Client**: Simpan token di environment variable Thunder Client untuk digunakan di semua request.


===========
catatan:Untuk diajarkan kepada siswa, penjelasan yang telah kita buat sudah cukup komprehensif, mencakup struktur aplikasi, logika geofencing, JWT autentikasi, konsumsi API, serta pengujian. Namun, jika tujuannya adalah untuk siswa (misalnya dalam konteks pelajaran pemrograman atau proyek sekolah), ada beberapa aspek yang mungkin masih kurang atau perlu disesuaikan agar lebih mudah dipahami dan terstruktur. Berikut analisis serta saran penyempurnaan:

Aspek yang Mungkin Kurang untuk Siswa
Penjelasan Konsep Dasar
Siswa mungkin belum familiar dengan konsep seperti REST API, JWT, atau geofencing. Penjelasan awal tentang "apa itu" dan "mengapa digunakan" bisa membantu.
Urutan Belajar yang Bertahap
Urutan saat ini langsung masuk ke implementasi kompleks (misalnya JWT dan geofencing). Siswa mungkin membutuhkan langkah-langkah yang lebih bertahap, dari dasar hingga kompleks.
Contoh Sederhana Sebelum Kompleks
Sebelum masuk ke aplikasi penuh, contoh kecil (misalnya aplikasi "Hello World" dengan Flutter dan Spring Boot) bisa menjadi pengantar.
Dokumentasi Visual
Siswa sering lebih mudah memahami dengan diagram atau flowchart yang dijelaskan langkah demi langkah, bukan hanya teks.
Latihan Praktis
Tidak ada tugas atau latihan spesifik untuk siswa coba sendiri, yang penting untuk pembelajaran aktif.
Penanganan Error untuk Pemula
Penanganan error di kode masih minimal. Siswa perlu tahu cara debug dan menangani kesalahan umum.
=================