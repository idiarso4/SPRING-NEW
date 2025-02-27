Baik, saya akan melanjutkan sesuai urutan pembelajaran yang telah direvisi untuk siswa, dengan penjelasan langkah demi langkah yang disesuaikan untuk pemula. Kita akan mulai dari **Tahap 0: Pengenalan Dasar** hingga **Tahap 7: Pengujian dan Deployment**, dengan fokus pada pemahaman konsep, contoh kode sederhana, dan latihan praktis. Saya akan menyusunnya seperti materi pelajaran yang bisa diajarkan langsung kepada siswa.

---

### **Tahap 0: Pengenalan Dasar**

#### **Tujuan**
- Siswa memahami apa itu Flutter, Spring Boot, dan API.
- Siswa bisa menyiapkan lingkungan pengembangan.

#### **Materi**
1. **Apa itu Flutter?**  
   - Flutter adalah alat dari Google untuk membuat aplikasi Android dan iOS dengan satu kode.
   - Contoh: Aplikasi seperti absensi siswa yang kita buat.

2. **Apa itu Spring Boot?**  
   - Spring Boot adalah alat berbasis Java untuk membuat backend (bagian server).
   - Digunakan untuk menyimpan data dan mengatur logika aplikasi.

3. **Apa itu API?**  
   - API (Application Programming Interface) adalah cara aplikasi frontend (Flutter) dan backend (Spring Boot) berkomunikasi.
   - Contoh: Frontend minta data absensi, backend kirim respons.

4. **Alat yang Dibutuhkan**  
   - Flutter SDK (untuk frontend).
   - Java JDK dan Maven (untuk backend).
   - VS Code atau IntelliJ IDEA (editor kode).
   - Emulator Android atau perangkat fisik.

#### **Langkah Instalasi**
1. **Flutter**  
   - Unduh Flutter dari [flutter.dev](https://flutter.dev).
   - Tambahkan ke PATH di terminal:
     ```bash
     export PATH="$PATH:/path/to/flutter/bin"
     ```
   - Jalankan `flutter doctor` untuk cek instalasi.

2. **Java dan Maven**  
   - Unduh JDK dari [Oracle](https://www.oracle.com/java/).
   - Tambahkan ke PATH:
     ```bash
     export JAVA_HOME=/path/to/jdk
     export PATH=$JAVA_HOME/bin:$PATH
     ```
   - Unduh Maven, tambahkan ke PATH, lalu cek: `mvn -version`.

3. **VS Code**  
   - Install VS Code, tambahkan extension: Flutter, Dart, Thunder Client.

#### **Latihan**
- **Tugas**: Install Flutter dan buat proyek baru:
  ```bash
  flutter create hello_app
  cd hello_app
  flutter run
  ```
- **Hasil**: Aplikasi sederhana dengan teks "Hello World" muncul di emulator.

---

### **Tahap 1: Membuat Aplikasi Sederhana**

#### **Tujuan**
- Siswa mengenal struktur proyek dan komunikasi frontend-backend.

#### **Materi**
1. **Struktur Flutter**  
   - File utama: `lib/main.dart`.
   - Tempat menulis kode UI dan logika.

2. **Struktur Spring Boot**  
   - File utama: `src/main/java/com/attendance/Application.java`.
   - Tempat membuat API.

3. **Komunikasi Sederhana**  
   - Backend kirim teks, frontend tampilkan.

#### **Contoh Kode**
**Backend: Spring Boot**
```java
// src/main/java/com/attendance/Application.java
package com.attendance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

// src/main/java/com/attendance/controller/HelloController.java
package com.attendance.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {
    @GetMapping("/hello")
    public String sayHello() {
        return "Hello from Backend!";
    }
}
```
- Jalankan: `mvn spring-boot:run`.

**Frontend: Flutter**
```dart
// lib/main.dart
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  Future<String> fetchHello() async {
    final response = await http.get(Uri.parse('http://localhost:8080/api/hello'));
    if (response.statusCode == 200) return response.body;
    return 'Error';
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(title: Text('Hello App')),
        body: FutureBuilder<String>(
          future: fetchHello(),
          builder: (context, snapshot) {
            if (snapshot.hasData) return Center(child: Text(snapshot.data!));
            return Center(child: CircularProgressIndicator());
          },
        ),
      ),
    );
  }
}
```
- Jalankan: `flutter run`.

#### **Latihan**
- **Tugas**: Ubah teks di backend menjadi "Selamat Datang, Siswa!" dan pastikan muncul di Flutter.

---

### **Tahap 2: Mengenal Database dan Model**

#### **Tujuan**
- Siswa memahami cara menyimpan data.

#### **Materi**
1. **Database**  
   - Tempat menyimpan data seperti nama siswa dan absensi.
   - Kita pakai PostgreSQL (gratis dan mudah).

2. **Model**  
   - Model adalah struktur data (contoh: `Student` punya nama dan kelas).

#### **Contoh Kode**
**Backend: Tambah Database**
- Tambah dependensi di `pom.xml`:
  ```xml
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
  </dependency>
  <dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.5.0</version>
  </dependency>
  ```
- Konfigurasi `application.properties`:
  ```properties
  spring.datasource.url=jdbc:postgresql://localhost:5432/student_db
  spring.datasource.username=postgres
  spring.datasource.password=your_password
  spring.jpa.hibernate.ddl-auto=update
  ```

**Model: Student**
```java
// src/main/java/com/attendance/model/Student.java
package com.attendance.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Student {
    @Id
    private String id;
    private String name;
    private String className;

    // Getter dan Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
}
```

**Repository**
```java
// src/main/java/com/attendance/repository/StudentRepository.java
package com.attendance.repository;

import com.attendance.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, String> {
}
```

**Controller**
```java
// src/main/java/com/attendance/controller/StudentController.java
package com.attendance.controller;

import com.attendance.model.Student;
import com.attendance.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class StudentController {
    @Autowired
    private StudentRepository studentRepository;

    @PostMapping("/students")
    public Student addStudent(@RequestBody Student student) {
        return studentRepository.save(student);
    }

    @GetMapping("/students/{id}")
    public Student getStudent(@PathVariable String id) {
        return studentRepository.findById(id).orElse(null);
    }
}
```

**Frontend: Tambah Siswa**
```dart
// lib/screens/add_student_screen.dart
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class AddStudentScreen extends StatefulWidget {
  @override
  _AddStudentScreenState createState() => _AddStudentScreenState();
}

class _AddStudentScreenState extends State<AddStudentScreen> {
  final TextEditingController _idController = TextEditingController();
  final TextEditingController _nameController = TextEditingController();
  final TextEditingController _classController = TextEditingController();

  Future<void> addStudent() async {
    final response = await http.post(
      Uri.parse('http://localhost:8080/api/students'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({
        'id': _idController.text,
        'name': _nameController.text,
        'className': _classController.text,
      }),
    );
    if (response.statusCode == 200) {
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text('Siswa ditambahkan')));
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Tambah Siswa')),
      body: Padding(
        padding: EdgeInsets.all(16.0),
        child: Column(
          children: [
            TextField(controller: _idController, decoration: InputDecoration(labelText: 'ID')),
            TextField(controller: _nameController, decoration: InputDecoration(labelText: 'Nama')),
            TextField(controller: _classController, decoration: InputDecoration(labelText: 'Kelas')),
            SizedBox(height: 20),
            ElevatedButton(onPressed: addStudent, child: Text('Simpan')),
          ],
        ),
      ),
    );
  }
}
```

#### **Latihan**
- **Tugas**: Tambahkan siswa dengan ID "001", nama "Budi", kelas "X-A" melalui Flutter.

---

### **Tahap 3: Fitur Absensi Dasar**
Baik, kita lanjutkan ke **Tahap 3: Fitur Absensi Dasar** sesuai urutan pembelajaran untuk siswa. Saya akan menyusun materi ini dengan pendekatan sederhana, cocok untuk pemula, dengan contoh kode dan latihan praktis.

---

### **Tahap 3: Fitur Absensi Dasar**

#### **Tujuan**
- Siswa dapat membuat fitur absensi sederhana (manual, tanpa wajah atau radius).
- Siswa memahami cara mengirim data dari Flutter ke Spring Boot dan menyimpannya di database.

#### **Materi**
1. **Apa itu Absensi?**  
   - Absensi adalah mencatat kehadiran siswa.
   - Kita mulai dengan absensi manual: klik tombol, simpan waktu ke database.

2. **Komponen yang Dibutuhkan**  
   - Model `Attendance` untuk menyimpan data absensi.
   - API di backend untuk menerima data.
   - Layar di Flutter untuk tombol absensi.

#### **Contoh Kode**

**Backend: Spring Boot**

1. **Model: Attendance**  
```java
// src/main/java/com/attendance/model/Attendance.java
package com.attendance.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class Attendance {
    @Id
    private String id;
    @ManyToOne
    private Student student;
    private LocalDateTime timestamp;

    // Getter dan Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
```

2. **Repository: AttendanceRepository**  
```java
// src/main/java/com/attendance/repository/AttendanceRepository.java
package com.attendance.repository;

import com.attendance.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, String> {
}
```

3. **Controller: AttendanceController**  
```java
// src/main/java/com/attendance/controller/AttendanceController.java
package com.attendance.controller;

import com.attendance.model.Attendance;
import com.attendance.model.Student;
import com.attendance.repository.AttendanceRepository;
import com.attendance.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class AttendanceController {
    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private StudentRepository studentRepository;

    @PostMapping("/attendance")
    public Attendance recordAttendance(@RequestBody AttendanceRequest request) {
        Student student = studentRepository.findById(request.getStudentId()).orElseThrow();
        Attendance attendance = new Attendance();
        attendance.setId(UUID.randomUUID().toString());
        attendance.setStudent(student);
        attendance.setTimestamp(LocalDateTime.now());
        return attendanceRepository.save(attendance);
    }
}

class AttendanceRequest {
    private String studentId;

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
}
```

**Frontend: Flutter**

1. **Layar Absensi: `attendance_screen.dart`**  
```dart
// lib/screens/attendance_screen.dart
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class AttendanceScreen extends StatefulWidget {
  @override
  _AttendanceScreenState createState() => _AttendanceScreenState();
}

class _AttendanceScreenState extends State<AttendanceScreen> {
  Future<void> recordAttendance() async {
    try {
      final response = await http.post(
        Uri.parse('http://localhost:8080/api/attendance'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode({'studentId': '001'}), // ID siswa hardcoded untuk latihan
      );
      if (response.statusCode == 200) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Absensi berhasil!')),
        );
      } else {
        throw Exception('Gagal absen');
      }
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Error: $e')),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Absensi Siswa')),
      body: Center(
        child: ElevatedButton(
          onPressed: recordAttendance,
          child: Text('Absen Sekarang'),
        ),
      ),
    );
  }
}
```

2. **Update `main.dart`**  
```dart
// lib/main.dart
import 'package:flutter/material.dart';
import 'screens/attendance_screen.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: AttendanceScreen(),
    );
  }
}
```

#### **Penjelasan**
- **Backend**: Menerima ID siswa, membuat data absensi dengan waktu saat ini, lalu menyimpan ke database.
- **Frontend**: Tombol "Absen Sekarang" mengirim request ke backend dan menampilkan pesan sukses/gagal.

#### **Latihan**
- **Tugas**:  
  1. Tambahkan siswa dengan ID "001" (dari Tahap 2).
  2. Klik tombol absen dan pastikan data tersimpan di database (cek tabel `attendance` di PostgreSQL).
  3. Ubah pesan sukses menjadi "Halo, kamu sudah absen!".

---

### **Tahap 4: Geofencing dan Kamera**

#### **Tujuan**
- Siswa menambahkan fitur absensi dengan validasi lokasi (geofencing) dan kamera untuk wajah.

#### **Materi**
1. **Geofencing**  
   - Memeriksa apakah siswa berada dalam radius tertentu (misalnya, 100 meter dari sekolah).
   - Pakai `geolocator` di Flutter.

2. **Kamera**  
   - Mengambil foto wajah untuk absensi.
   - Pakai `camera` di Flutter.

#### **Contoh Kode**

**Tambah Dependensi di Flutter**
```yaml
// pubspec.yaml
dependencies:
  geolocator: ^10.0.0
  camera: ^0.10.0
```
- Jalankan: `flutter pub get`.

**Backend: Update AttendanceController**
```java
@PostMapping("/attendance")
public Attendance recordAttendance(@RequestBody AttendanceRequest request) {
    double schoolLat = -6.2088; // Koordinat sekolah (contoh: Jakarta)
    double schoolLon = 106.8456;
    double distance = calculateDistance(request.getLatitude(), request.getLongitude(), schoolLat, schoolLon);
    if (distance > 100) throw new RuntimeException("Di luar radius sekolah");

    Student student = studentRepository.findById(request.getStudentId()).orElseThrow();
    Attendance attendance = new Attendance();
    attendance.setId(UUID.randomUUID().toString());
    attendance.setStudent(student);
    attendance.setTimestamp(LocalDateTime.now());
    attendance.setImagePath(request.getImagePath()); // Simpan path gambar
    return attendanceRepository.save(attendance);
}

private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
    double R = 6371000; // Radius bumi dalam meter
    double dLat = Math.toRadians(lat2 - lat1);
    double dLon = Math.toRadians(lon2 - lon1);
    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
               Math.sin(dLon / 2) * Math.sin(dLon / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return R * c;
}

class AttendanceRequest {
    private String studentId;
    private String imagePath;
    private double latitude;
    private double longitude;

    // Getter dan Setter
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
}
```

**Frontend: Update AttendanceScreen**
```dart
// lib/screens/attendance_screen.dart
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:geolocator/geolocator.dart';
import 'package:camera/camera.dart';
import 'dart:convert';
import 'dart:io';

class AttendanceScreen extends StatefulWidget {
  @override
  _AttendanceScreenState createState() => _AttendanceScreenState();
}

class _AttendanceScreenState extends State<AttendanceScreen> {
  CameraController? _controller;
  Position? _position;

  @override
  void initState() {
    super.initState();
    _initializeCamera();
  }

  Future<void> _initializeCamera() async {
    final cameras = await availableCameras();
    _controller = CameraController(cameras[0], ResolutionPreset.medium);
    await _controller!.initialize();
    setState(() {});
  }

  Future<void> _checkLocation() async {
    bool serviceEnabled = await Geolocator.isLocationServiceEnabled();
    if (!serviceEnabled) throw Exception('Aktifkan lokasi');

    LocationPermission permission = await Geolocator.requestPermission();
    if (permission == LocationPermission.denied) throw Exception('Izin lokasi ditolak');

    _position = await Geolocator.getCurrentPosition();
  }

  Future<void> recordAttendance() async {
    try {
      await _checkLocation();
      final image = await _controller!.takePicture();

      final response = await http.post(
        Uri.parse('http://localhost:8080/api/attendance'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode({
          'studentId': '001',
          'imagePath': image.path,
          'latitude': _position!.latitude,
          'longitude': _position!.longitude,
        }),
      );

      if (response.statusCode == 200) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Absensi berhasil!')),
        );
      } else {
        throw Exception('Gagal absen: ${response.body}');
      }
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Error: $e')),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    if (_controller == null || !_controller!.value.isInitialized) {
      return Center(child: CircularProgressIndicator());
    }
    return Scaffold(
      appBar: AppBar(title: Text('Absensi Siswa')),
      body: Column(
        children: [
          CameraPreview(_controller!),
          SizedBox(height: 20),
          ElevatedButton(
            onPressed: recordAttendance,
            child: Text('Absen Sekarang'),
          ),
        ],
      ),
    );
  }

  @override
  void dispose() {
    _controller?.dispose();
    super.dispose();
  }
}
```

#### **Penjelasan**
- **Geofencing**: Cek lokasi siswa dan validasi radius di backend.
- **Kamera**: Ambil foto wajah dan kirim path-nya ke backend.

#### **Latihan**
- **Tugas**:  
  1. Tambahkan teks di layar yang menunjukkan "Di dalam radius" atau "Di luar radius".
  2. Coba absen dari lokasi jauh (ubah koordinat manual) dan periksa pesan error.

---

Baik, kita lanjut ke **Tahap 5: Autentikasi dengan JWT** sesuai urutan pembelajaran untuk siswa. Saya akan menyusun materi ini dengan pendekatan sederhana, fokus pada pemahaman konsep autentikasi, implementasi JWT di backend dan frontend, serta latihan praktis yang mudah diikuti oleh siswa.

---

### **Tahap 5: Autentikasi dengan JWT**

#### **Tujuan**
- Siswa memahami cara mengamankan aplikasi dengan autentikasi.
- Siswa dapat membuat sistem login dan melindungi endpoint API menggunakan JWT (JSON Web Token).

#### **Materi**
1. **Apa itu Autentikasi dan JWT?**  
   - **Autentikasi**: Memastikan hanya pengguna yang sah (misalnya, siswa atau guru) yang bisa masuk ke aplikasi.
   - **JWT**: Token khusus yang diberikan saat login, digunakan untuk membuktikan identitas pengguna di setiap request.

2. **Cara Kerja JWT**  
   - Pengguna login dengan username dan password.
   - Backend memberi token jika login berhasil.
   - Frontend menyimpan token dan mengirimkannya di setiap request ke API yang dilindungi.

3. **Langkah Implementasi**  
   - Tambah login di backend.
   - Lindungi endpoint absensi.
   - Buat layar login di Flutter dan simpan token.

#### **Contoh Kode**

**Backend: Spring Boot**

1. **Tambah Dependensi di `pom.xml`**  
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt</artifactId>
    <version>0.9.1</version>
</dependency>
```

2. **JWT Utility (`JwtUtil.java`)**  
```java
// src/main/java/com/attendance/config/JwtUtil.java
package com.attendance.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "rahasia123"; // Ganti dengan kunci rahasia yang kuat
    private final long EXPIRATION_TIME = 86400000; // 24 jam

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

3. **Security Config (`SecurityConfig.java`)**  
```java
// src/main/java/com/attendance/config/SecurityConfig.java
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
            .requestMatchers("/api/auth/login").permitAll() // Izinkan login
            .anyRequest().authenticated() // Lindungi semua endpoint lain
            .and()
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
```

4. **JWT Filter (`JwtFilter.java`)**  
```java
// src/main/java/com/attendance/config/JwtFilter.java
package com.attendance.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (jwtUtil.validateToken(token)) {
                // Token valid, lanjutkan request
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        chain.doFilter(request, response);
    }
}
```

5. **Auth Controller (`AuthController.java`)**  
```java
// src/main/java/com/attendance/controller/AuthController.java
package com.attendance.controller;

import com.attendance.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        // Hardcoded untuk latihan, nanti ganti dengan database
        if ("siswa123".equals(request.getUsername()) && "password123".equals(request.getPassword())) {
            String token = jwtUtil.generateToken(request.getUsername());
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(401).body("Login gagal");
    }
}

class LoginRequest {
    private String username;
    private String password;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
```

6. **Update AttendanceController**  
- Endpoint `/attendance` sudah dilindungi JWT oleh `JwtFilter`.

**Frontend: Flutter**

1. **Tambah Dependensi di `pubspec.yaml`**  
```yaml
dependencies:
  shared_preferences: ^2.0.15
```
- Jalankan: `flutter pub get`.

2. **Auth Service (`auth_service.dart`)**  
```dart
// lib/services/auth_service.dart
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';
import 'dart:convert';

class AuthService {
  final String baseUrl = 'http://localhost:8080/api';

  Future<bool> login(String username, String password) async {
    try {
      final response = await http.post(
        Uri.parse('$baseUrl/auth/login'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode({'username': username, 'password': password}),
      );
      if (response.statusCode == 200) {
        final token = response.body;
        await _saveToken(token);
        return true;
      }
      return false;
    } catch (e) {
      return false;
    }
  }

  Future<void> _saveToken(String token) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString('jwt_token', token);
  }

  Future<String?> getToken() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getString('jwt_token');
  }
}
```

3. **Login Screen (`login_screen.dart`)**  
```dart
// lib/screens/login_screen.dart
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
      Navigator.pushReplacementNamed(context, '/attendance');
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

4. **Update AttendanceScreen dengan JWT**  
```dart
// lib/screens/attendance_screen.dart
Future<void> recordAttendance() async {
  try {
    final token = await _authService.getToken();
    if (token == null) throw Exception('Silakan login dulu');

    await _checkLocation();
    final image = await _controller!.takePicture();

    final response = await http.post(
      Uri.parse('http://localhost:8080/api/attendance'),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $token',
      },
      body: jsonEncode({
        'studentId': '001',
        'imagePath': image.path,
        'latitude': _position!.latitude,
        'longitude': _position!.longitude,
      }),
    );

    if (response.statusCode == 200) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Absensi berhasil!')),
      );
    } else {
      throw Exception('Gagal absen: ${response.body}');
    }
  } catch (e) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text('Error: $e')),
    );
  }
}
```

5. **Update `main.dart`**  
```dart
// lib/main.dart
import 'package:flutter/material.dart';
import 'screens/login_screen.dart';
import 'screens/attendance_screen.dart';
import 'services/auth_service.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  final AuthService authService = AuthService();
  final token = await authService.getToken();
  runApp(MyApp(initialRoute: token != null ? '/attendance' : '/login'));
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
        '/attendance': (context) => AttendanceScreen(),
      },
    );
  }
}
```

#### **Penjelasan**
- **Backend**: 
  - Membuat endpoint login yang mengembalikan token JWT.
  - Melindungi endpoint `/attendance` dengan `JwtFilter`.
- **Frontend**: 
  - Layar login untuk memasukkan username dan password.
  - Token disimpan di `shared_preferences` dan dikirim di header setiap request.

#### **Latihan**
- **Tugas**:  
  1. Login dengan username "siswa123" dan password "password123".
  2. Coba absen tanpa login (hapus token dari `shared_preferences`) dan lihat pesan error.
  3. Tambahkan teks "Selamat datang, [username]" di layar absensi setelah login.

---

Saya akan lanjutkan ke **Tahap 6: Fitur Tambahan** di bagian berikutnya jika kamu setuju. Lanjutkan?
Baik, kita lanjut ke **Tahap 6: Fitur Tambahan** sesuai urutan pembelajaran untuk siswa. Pada tahap ini, kita akan menambahkan fitur seperti notifikasi, dashboard, dan jadwal kegiatan untuk memperkaya aplikasi absensi. Materi akan disusun sederhana agar siswa bisa mengikuti dengan mudah, lengkap dengan contoh kode dan latihan praktis.

---

### **Tahap 6: Fitur Tambahan**

#### **Tujuan**
- Siswa dapat menambahkan fitur notifikasi untuk memberi tahu pengguna.
- Siswa dapat membuat dashboard sederhana untuk ringkasan data.
- Siswa dapat mengelola jadwal kegiatan akademik.

#### **Materi**
1. **Notifikasi**  
   - Menggunakan Firebase Cloud Messaging (FCM) untuk mengirim pesan seperti "Absensi Berhasil".
   - Notifikasi muncul saat aplikasi dibuka (foreground).

2. **Dashboard**  
   - Menampilkan ringkasan seperti jumlah absensi hari ini.
   - Data diambil dari backend.

3. **Jadwal Kegiatan**  
   - Menyimpan dan menampilkan jadwal seperti kelas atau shalat.

#### **Contoh Kode**

**Backend: Spring Boot**

1. **Tambah Dependensi Firebase di `pom.xml`**  
```xml
<dependency>
    <groupId>com.google.firebase</groupId>
    <artifactId>firebase-admin</artifactId>
    <version>9.1.1</version>
</dependency>
```

2. **Notifikasi Service (`NotificationService.java`)**  
```java
// src/main/java/com/attendance/service/NotificationService.java
package com.attendance.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;

@Service
public class NotificationService {
    @PostConstruct
    public void initialize() {
        try {
            FileInputStream serviceAccount = new FileInputStream("path/to/your-firebase-adminsdk.json");
            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendNotification(String token, String title, String body) throws Exception {
        Message message = Message.builder()
            .setToken(token) // Token dari frontend
            .setNotification(com.google.firebase.messaging.Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build())
            .build();
        FirebaseMessaging.getInstance().send(message);
    }
}
```

3. **Update AttendanceController untuk Notifikasi**  
```java
@PostMapping("/attendance")
public ResponseEntity<Attendance> recordAttendance(@RequestBody AttendanceRequest request) {
    double schoolLat = -6.2088;
    double schoolLon = 106.8456;
    double distance = calculateDistance(request.getLatitude(), request.getLongitude(), schoolLat, schoolLon);
    if (distance > 100) throw new RuntimeException("Di luar radius sekolah");

    Student student = studentRepository.findById(request.getStudentId()).orElseThrow();
    Attendance attendance = new Attendance();
    attendance.setId(UUID.randomUUID().toString());
    attendance.setStudent(student);
    attendance.setTimestamp(LocalDateTime.now());
    attendance.setImagePath(request.getImagePath());
    attendanceRepository.save(attendance);

    // Kirim notifikasi
    notificationService.sendNotification(request.getFcmToken(), "Absensi", "Kamu berhasil absen!");
    return ResponseEntity.ok(attendance);
}

// Update AttendanceRequest untuk menambahkan fcmToken
class AttendanceRequest {
    private String studentId;
    private String imagePath;
    private double latitude;
    private double longitude;
    private String fcmToken;

    // Getter dan Setter
    public String getFcmToken() { return fcmToken; }
    public void setFcmToken(String fcmToken) { this.fcmToken = fcmToken; }
    // ... lainnya tetap sama
}
```

4. **Model: Schedule (`Schedule.java`)**  
```java
// src/main/java/com/attendance/model/Schedule.java
package com.attendance.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Schedule {
    @Id
    private String id;
    private String activityType;
    private String description;
    private LocalDateTime timestamp;

    // Getter dan Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getActivityType() { return activityType; }
    public void setActivityType(String activityType) { this.activityType = activityType; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
```

5. **Repository: ScheduleRepository**  
```java
// src/main/java/com/attendance/repository/ScheduleRepository.java
package com.attendance.repository;

import com.attendance.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, String> {
}
```

6. **Schedule Controller (`ScheduleController.java`)**  
```java
// src/main/java/com/attendance/controller/ScheduleController.java
package com.attendance.controller;

import com.attendance.model.Schedule;
import com.attendance.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ScheduleController {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @GetMapping("/schedules")
    public List<Schedule> getSchedules() {
        return scheduleRepository.findAll();
    }

    @PostMapping("/schedules")
    public Schedule addSchedule(@RequestBody Schedule schedule) {
        schedule.setId(UUID.randomUUID().toString());
        return scheduleRepository.save(schedule);
    }
}
```

7. **Dashboard Controller (`DashboardController.java`)**  
```java
// src/main/java/com/attendance/controller/DashboardController.java
package com.attendance.controller;

import com.attendance.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DashboardController {
    @Autowired
    private AttendanceRepository attendanceRepository;

    @GetMapping("/dashboard")
    public DashboardData getDashboardData() {
        long attendanceCount = attendanceRepository.count();
        return new DashboardData(attendanceCount);
    }
}

class DashboardData {
    private long attendanceCount;

    public DashboardData(long attendanceCount) {
        this.attendanceCount = attendanceCount;
    }

    public long getAttendanceCount() { return attendanceCount; }
    public void setAttendanceCount(long attendanceCount) { this.attendanceCount = attendanceCount; }
}
```

**Frontend: Flutter**

1. **Tambah Dependensi Firebase di `pubspec.yaml`**  
```yaml
dependencies:
  firebase_core: ^2.4.0
  firebase_messaging: ^14.2.1
```
- Konfigurasi Firebase:
  - Tambahkan `google-services.json` (Android) dan `GoogleService-Info.plist` (iOS) seperti dijelaskan sebelumnya.

2. **Notifikasi Service (`notification_service.dart`)**  
```dart
// lib/services/notification_service.dart
import 'package:firebase_messaging/firebase_messaging.dart';

class NotificationService {
  final FirebaseMessaging _firebaseMessaging = FirebaseMessaging.instance;

  Future<String?> initialize() async {
    await _firebaseMessaging.requestPermission();
    String? token = await _firebaseMessaging.getToken();
    print("FCM Token: $token");

    FirebaseMessaging.onMessage.listen((RemoteMessage message) {
      print('Notifikasi: ${message.notification?.title} - ${message.notification?.body}');
      // Tampilkan di UI jika perlu
    });

    return token;
  }
}
```

3. **Dashboard Screen (`dashboard_screen.dart`)**  
```dart
// lib/screens/dashboard_screen.dart
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'services/auth_service.dart';

class DashboardScreen extends StatefulWidget {
  @override
  _DashboardScreenState createState() => _DashboardScreenState();
}

class _DashboardScreenState extends State<DashboardScreen> {
  final AuthService _authService = AuthService();
  int attendanceCount = 0;

  @override
  void initState() {
    super.initState();
    _fetchDashboardData();
  }

  Future<void> _fetchDashboardData() async {
    final token = await _authService.getToken();
    final response = await http.get(
      Uri.parse('http://localhost:8080/api/dashboard'),
      headers: {'Authorization': 'Bearer $token'},
    );
    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      setState(() {
        attendanceCount = data['attendanceCount'];
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Dashboard')),
      body: Center(
        child: Text('Jumlah Absensi Hari Ini: $attendanceCount', style: TextStyle(fontSize: 20)),
      ),
    );
  }
}
```

4. **Schedule Screen (`schedule_screen.dart`)**  
```dart
// lib/screens/schedule_screen.dart
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'services/auth_service.dart';

class ScheduleScreen extends StatefulWidget {
  @override
  _ScheduleScreenState createState() => _ScheduleScreenState();
}

class _ScheduleScreenState extends State<ScheduleScreen> {
  final AuthService _authService = AuthService();
  List<dynamic> schedules = [];

  @override
  void initState() {
    super.initState();
    _fetchSchedules();
  }

  Future<void> _fetchSchedules() async {
    final token = await _authService.getToken();
    final response = await http.get(
      Uri.parse('http://localhost:8080/api/schedules'),
      headers: {'Authorization': 'Bearer $token'},
    );
    if (response.statusCode == 200) {
      setState(() {
        schedules = jsonDecode(response.body);
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Jadwal')),
      body: ListView.builder(
        itemCount: schedules.length,
        itemBuilder: (context, index) {
          return ListTile(
            title: Text(schedules[index]['description']),
            subtitle: Text(schedules[index]['timestamp']),
          );
        },
      ),
    );
  }
}
```

5. **Update AttendanceScreen untuk Notifikasi**  
```dart
// lib/screens/attendance_screen.dart
Future<void> recordAttendance() async {
  try {
    final token = await _authService.getToken();
    if (token == null) throw Exception('Silakan login dulu');

    await _checkLocation();
    final image = await _controller!.takePicture();
    final fcmToken = await NotificationService().initialize();

    final response = await http.post(
      Uri.parse('http://localhost:8080/api/attendance'),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $token',
      },
      body: jsonEncode({
        'studentId': '001',
        'imagePath': image.path,
        'latitude': _position!.latitude,
        'longitude': _position!.longitude,
        'fcmToken': fcmToken,
      }),
    );

    if (response.statusCode == 200) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Absensi berhasil!')),
      );
    } else {
      throw Exception('Gagal absen: ${response.body}');
    }
  } catch (e) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text('Error: $e')),
    );
  }
}
```

6. **Update `main.dart`**  
```dart
// lib/main.dart
import 'package:flutter/material.dart';
import 'screens/login_screen.dart';
import 'screens/attendance_screen.dart';
import 'screens/dashboard_screen.dart';
import 'screens/schedule_screen.dart';
import 'services/auth_service.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  final AuthService authService = AuthService();
  final token = await authService.getToken();
  runApp(MyApp(initialRoute: token != null ? '/dashboard' : '/login'));
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
        '/attendance': (context) => AttendanceScreen(),
        '/dashboard': (context) => DashboardScreen(),
        '/schedule': (context) => ScheduleScreen(),
      },
    );
  }
}
```

#### **Penjelasan**
- **Notifikasi**: Backend mengirim notifikasi via Firebase saat absensi berhasil.
- **Dashboard**: Menampilkan jumlah absensi dari backend.
- **Jadwal**: Menampilkan daftar jadwal dalam bentuk list.

#### **Latihan**
- **Tugas**:  
  1. Tambahkan jadwal "Matematika - X-A" untuk hari ini di backend via POST `/api/schedules`.
  2. Tambahkan tombol di `DashboardScreen` untuk navigasi ke `AttendanceScreen`.
  3. Ubah pesan notifikasi menjadi "Selamat, absenmu tercatat!".

---

Baik, kita lanjut ke **Tahap 7: Pengujian dan Deployment** sesuai urutan pembelajaran untuk siswa. Pada tahap ini, siswa akan belajar cara menguji aplikasi menggunakan alat seperti Postman atau Thunder Client, serta cara membuild dan mendeploy aplikasi Flutter ke Android. Materi akan disusun sederhana agar mudah dipahami, dengan contoh praktis dan latihan.

---

### **Tahap 7: Pengujian dan Deployment**

#### **Tujuan**
- Siswa dapat menguji API backend untuk memastikan semua fitur berjalan dengan baik.
- Siswa dapat membuild aplikasi Flutter menjadi APK untuk Android.
- Siswa memahami langkah dasar deployment.

#### **Materi**
1. **Pengujian API**  
   - Menggunakan Postman atau Thunder Client untuk tes manual.
   - Memastikan endpoint login, absensi, dashboard, dan jadwal berfungsi.

2. **Build Aplikasi Flutter**  
   - Mengubah kode Flutter menjadi file APK yang bisa diinstall di Android.

3. **Deployment Dasar**  
   - Menjalankan backend di server lokal (untuk latihan).
   - Menginstall APK di perangkat fisik atau emulator.

#### **Contoh Kode dan Langkah-langkah**

**1. Pengujian API**

**Menggunakan Postman**
- **Prasyarat**: Postman terinstal ([Download](https://www.postman.com/downloads/)).
- **Langkah-langkah**:

1. **Jalankan Backend**  
   - Pastikan Spring Boot berjalan:
     ```bash
     cd student-attendance-backend
     mvn spring-boot:run
     ```

2. **Tes Endpoint Login**  
   - **Metode**: POST  
   - **URL**: `http://localhost:8080/api/auth/login`  
   - **Body** (raw JSON):  
     ```json
     {
       "username": "siswa123",
       "password": "password123"
     }
     ```
   - Klik **Send**, simpan token dari respons (contoh: `eyJhbGciOiJIUzI1NiJ9...`).

3. **Tes Endpoint Absensi**  
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
       "studentId": "001",
       "imagePath": "dummy/path.jpg",
       "latitude": -6.2088,
       "longitude": 106.8456,
       "fcmToken": "dummy_fcm_token"
     }
     ```
   - **Tes**: Klik **Send**, pastikan respons:
     ```json
     {
       "id": "some-uuid",
       "student": { "id": "001", "name": "Budi", "className": "X-A" },
       "timestamp": "2025-02-27T10:00:00",
       "imagePath": "dummy/path.jpg"
     }
     ```

4. **Tes Endpoint Dashboard**  
   - **Metode**: GET  
   - **URL**: `http://localhost:8080/api/dashboard`  
   - **Headers**:  
     ```
     Authorization: Bearer <paste_token_here>
     ```
   - **Tes**: Pastikan respons menunjukkan jumlah absensi.

5. **Skrip Tes di Postman**  
   - Tambahkan di tab **Tests** untuk `/api/attendance`:  
     ```javascript
     pm.test("Absensi berhasil", function () {
         pm.response.to.have.status(200);
         var jsonData = pm.response.json();
         pm.expect(jsonData.id).to.exist;
         pm.expect(jsonData.student.id).to.eql("001");
     });
     ```

**Menggunakan Thunder Client (Alternatif di VS Code)**  
- **Prasyarat**: Install Thunder Client di VS Code (Extensions > "Thunder Client").  
- **Langkah-langkah**:  
  1. Buka Thunder Client di sidebar VS Code.  
  2. Ulangi langkah Postman dengan antarmuka Thunder Client (URL, Headers, Body sama).  
  3. Tambah skrip tes di tab **Test**:  
     ```javascript
     tc.test("Absensi berhasil", function() {
         tc.response.status.expect(200);
         tc.response.body.id.expect.to.exist;
     });
     ```

#### **2. Build Aplikasi Flutter**

**Langkah-langkah**  
1. **Update Konfigurasi**  
   - Edit `android/app/build.gradle`:  
     ```gradle
     android {
         defaultConfig {
             applicationId "com.attendance.student"
             minSdkVersion 21
             targetSdkVersion 33
             versionCode 1
             versionName "1.0"
         }
     }
     ```
   - Pastikan `android/app/src/main/AndroidManifest.xml` punya izin:  
     ```xml
     <uses-permission android:name="android.permission.INTERNET"/>
     <uses-permission android:name="android.permission.CAMERA"/>
     <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
     ```

2. **Build APK**  
   - Jalankan di terminal:
     ```bash
     cd student_attendance_frontend
     flutter build apk --release
     ```
   - Jika sukses, APK ada di: `build/app/outputs/flutter-apk/app-release.apk`.

3. **Install APK**  
   - Pindahkan APK ke perangkat Android via USB.
   - Aktifkan "Install from Unknown Sources" di pengaturan.
   - Klik file APK untuk install.

#### **3. Deployment Dasar**

**Backend di Lokal**  
- **Langkah**:  
  1. Pastikan backend berjalan di `localhost:8080`.
  2. Ubah `baseUrl` di Flutter ke IP lokal (misalnya, `http://192.168.1.100:8080`) jika diakses dari perangkat fisik di jaringan yang sama.
  - Edit `auth_service.dart`:  
    ```dart
    final String baseUrl = 'http://192.168.1.100:8080/api';
    ```
  - Jalankan ulang backend: `mvn spring-boot:run`.

**Frontend di Perangkat**  
- Install APK di perangkat dan tes semua fitur (login, absensi, dashboard, jadwal).

#### **Penjelasan**
- **Pengujian**: Memastikan setiap bagian aplikasi (login, absensi, dll.) bekerja sebelum digunakan.
- **Build**: Mengubah kode menjadi aplikasi yang bisa diinstall.
- **Deployment**: Menjalankan aplikasi di dunia nyata (lokal untuk latihan).

#### **Latihan**
- **Tugas**:  
  1. Uji endpoint `/api/schedules` dengan Postman atau Thunder Client, tambahkan jadwal "Shalat Dzuhur" untuk hari ini.
  2. Build APK dan install di perangkat Android atau emulator.
  3. Coba login dan absen dari APK, pastikan notifikasi muncul.

---

### **Penutup Urutan Pembelajaran**
Dengan menyelesaikan **Tahap 0 hingga Tahap 7**, siswa telah:
- Memahami dasar Flutter dan Spring Boot.
- Membuat aplikasi sederhana dengan komunikasi API.
- Mengelola data di database.
- Mengimplementasikan absensi dengan geofencing dan kamera.
- Mengamankan aplikasi dengan JWT.
- Menambahkan fitur notifikasi, dashboard, dan jadwal.
- Menguji dan mendeploy aplikasi.

#### **Saran Tambahan untuk Siswa**
- Eksplorasi: Tambahkan fitur lain seperti perizinan atau laporan kerusakan (dari tahap sebelumnya).
- Praktik: Ulangi latihan dengan data sendiri (misalnya, nama siswa lain).
- Dokumentasi: Tulis langkah-langkah yang sudah dipelajari dalam catatan pribadi.

