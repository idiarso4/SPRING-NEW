Baik, saya akan memperdalam logika geofencing untuk absensi berbasis radius dan menyempurnakan struktur aplikasi dengan menambahkan elemen yang mungkin masih kurang berdasarkan analisis saya. Saya akan memastikan semua fitur terintegrasi dengan baik dan menambahkan beberapa elemen penting yang mungkin terlewat, seperti:
- **Notifikasi**: Untuk memberi tahu siswa, guru, dan guru BK tentang status absensi, perizinan, atau laporan.
- **Dashboard**: Untuk ringkasan data bagi guru, siswa, dan admin.
- **Manajemen Jadwal**: Untuk mengatur jadwal kegiatan akademik, ekstrakurikuler, dan shalat.

Berikut adalah struktur yang diperbarui dan logika geofencing yang lebih rinci.

---

### **Backend (Java Spring Boot)**  
```
student-attendance-backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── attendance/
│   │   │           ├── config/           # Konfigurasi aplikasi
│   │   │           │   ├── SecurityConfig.java
│   │   │           │   ├── MapConfig.java
│   │   │           │   └── NotificationConfig.java  # Konfigurasi notifikasi
│   │   │           ├── controller/       # REST API endpoints
│   │   │           │   ├── StudentController.java
│   │   │           │   ├── TeacherController.java
│   │   │           │   ├── CounselorController.java
│   │   │           │   ├── AttendanceController.java
│   │   │           │   ├── ClassActivityController.java
│   │   │           │   ├── ExtracurricularController.java
│   │   │           │   ├── PrayerController.java
│   │   │           │   ├── CounselingController.java
│   │   │           │   ├── DamageReportController.java
│   │   │           │   ├── InternshipController.java
│   │   │           │   ├── PermissionController.java
│   │   │           │   ├── MapController.java
│   │   │           │   ├── InteractionController.java
│   │   │           │   ├── NotificationController.java  # Notifikasi
│   │   │           │   ├── DashboardController.java   # Dashboard
│   │   │           │   └── ScheduleController.java    # Manajemen jadwal
│   │   │           ├── model/           # Entity classes
│   │   │           │   ├── Student.java
│   │   │           │   ├── Teacher.java
│   │   │           │   ├── Counselor.java
│   │   │           │   ├── Attendance.java
│   │   │           │   ├── ClassActivity.java
│   │   │           │   ├── Extracurricular.java
│   │   │           │   ├── PrayerAttendance.java
│   │   │           │   ├── CounselingSession.java
│   │   │           │   ├── DamageReport.java
│   │   │           │   ├── InternshipJournal.java
│   │   │           │   ├── InternshipPhoto.java
│   │   │           │   ├── Permission.java
│   │   │           │   ├── Location.java
│   │   │           │   ├── Interaction.java
│   │   │           │   ├── Notification.java    # Model notifikasi
│   │   │           │   ├── Dashboard.java       # Data ringkasan
│   │   │           │   └── Schedule.java        # Jadwal kegiatan
│   │   │           ├── repository/      # JPA repositories
│   │   │           │   ├── StudentRepository.java
│   │   │           │   ├── TeacherRepository.java
│   │   │           │   ├── CounselorRepository.java
│   │   │           │   ├── AttendanceRepository.java
│   │   │           │   ├── ClassActivityRepository.java
│   │   │           │   ├── ExtracurricularRepository.java
│   │   │           │   ├── PrayerAttendanceRepository.java
│   │   │           │   ├── CounselingSessionRepository.java
│   │   │           │   ├── DamageReportRepository.java
│   │   │           │   ├── InternshipJournalRepository.java
│   │   │           │   ├── InternshipPhotoRepository.java
│   │   │           │   ├── PermissionRepository.java
│   │   │           │   ├── LocationRepository.java
│   │   │           │   ├── InteractionRepository.java
│   │   │           │   ├── NotificationRepository.java
│   │   │           │   ├── DashboardRepository.java
│   │   │           │   └── ScheduleRepository.java
│   │   │           ├── service/         # Business logic
│   │   │           │   ├── StudentService.java
│   │   │           │   ├── TeacherService.java
│   │   │           │   ├── CounselorService.java
│   │   │           │   ├── AttendanceService.java
│   │   │           │   ├── ClassActivityService.java
│   │   │           │   ├── ExtracurricularService.java
│   │   │           │   ├── PrayerService.java
│   │   │           │   ├── CounselingService.java
│   │   │           │   ├── DamageReportService.java
│   │   │           │   ├── InternshipService.java
│   │   │           │   ├── PermissionService.java
│   │   │           │   ├── MapService.java       # Logika geofencing
│   │   │           │   ├── InteractionService.java
│   │   │           │   ├── NotificationService.java
│   │   │           │   ├── DashboardService.java
│   │   │           │   └── ScheduleService.java
│   │   │           └── Application.java # Main application class
│   │   └── resources/
│   │       ├── static/                  # Static files
│   │       ├── templates/              # Template files (opsional)
│   │       └── application.properties  # Konfigurasi database, cloud storage
│   └── test/                          # Unit dan integration tests
├── pom.xml                            # Dependencies Maven
└── README.md
```

### **Frontend (Flutter)**  
```
student_attendance_frontend/
├── android/                          # Konfigurasi Android
├── ios/                             # Konfigurasi iOS
├── lib/
│   ├── models/                     # Data models
│   │   ├── student.dart
│   │   ├── teacher.dart
│   │   ├── counselor.dart
│   │   ├── attendance.dart
│   │   ├── class_activity.dart
│   │   ├── extracurricular.dart
│   │   ├── prayer_attendance.dart
│   │   ├── counseling_session.dart
│   │   ├── damage_report.dart
│   │   ├── internship_journal.dart
│   │   ├── internship_photo.dart
│   │   ├── permission.dart
│   │   ├── location.dart
│   │   ├── interaction.dart
│   │   ├── notification.dart     # Model notifikasi
│   │   ├── dashboard.dart        # Data ringkasan
│   │   └── schedule.dart         # Jadwal kegiatan
│   ├── screens/                   # UI screens
│   │   ├── home_screen.dart
│   │   ├── login_screen.dart
│   │   ├── student_list_screen.dart
│   │   ├── teacher_list_screen.dart
│   │   ├── counselor_list_screen.dart
│   │   ├── attendance_screen.dart
│   │   ├── class_activity_screen.dart
│   │   ├── extracurricular_screen.dart
│   │   ├── prayer_screen.dart
│   │   ├── counseling_screen.dart
│   │   ├── damage_report_screen.dart
│   │   ├── internship_screen.dart
│   │   ├── permission_screen.dart
│   │   ├── map_screen.dart
│   │   ├── interaction_screen.dart
│   │   ├── notification_screen.dart  # Layar notifikasi
│   │   ├── dashboard_screen.dart     # Dashboard
│   │   └── schedule_screen.dart      # Jadwal
│   ├── services/                  # API services
│   │   ├── api_service.dart
│   │   ├── auth_service.dart
│   │   ├── attendance_service.dart
│   │   ├── class_activity_service.dart
│   │   ├── extracurricular_service.dart
│   │   ├── prayer_service.dart
│   │   ├── counseling_service.dart
│   │   ├── damage_report_service.dart
│   │   ├── internship_service.dart
│   │   ├── permission_service.dart
│   │   ├── map_service.dart
│   │   ├── interaction_service.dart
│   │   ├── notification_service.dart
│   │   ├── dashboard_service.dart
│   │   └── schedule_service.dart
│   ├── widgets/                   # Reusable widgets
│   │   ├── student_card.dart
│   │   ├── teacher_card.dart
│   │   ├── counselor_card.dart
│   │   ├── attendance_button.dart
│   │   ├── activity_card.dart
│   │   ├── damage_report_form.dart
│   │   ├── journal_entry.dart
│   │   ├── photo_upload.dart
│   │   ├── permission_form.dart
│   │   ├── notification_tile.dart  # Widget notifikasi
│   │   ├── dashboard_card.dart     # Widget ringkasan
│   │   ├── schedule_card.dart      # Widget jadwal
│   │   └── map_widget.dart
│   ├── utils/                     # Helper functions
│   │   ├── constants.dart
│   │   └── location_utils.dart   # Logika geofencing
│   └── main.dart                  # Entry point aplikasi
├── test/                         # Unit dan widget tests
├── pubspec.yaml                  # Dependencies Flutter
└── README.md
```

---

### **Logika Geofencing (Absensi Wajah + Radius)**

#### **Frontend (Flutter): `attendance_screen.dart`**
```dart
import 'package:flutter/material.dart';
import 'package:camera/camera.dart';
import 'package:geolocator/geolocator.dart';
import 'package:latlong2/latlong.dart';
import 'services/attendance_service.dart';

class AttendanceScreen extends StatefulWidget {
  @override
  _AttendanceScreenState createState() => _AttendanceScreenState();
}

class _AttendanceScreenState extends State<AttendanceScreen> {
  CameraController? _controller;
  Position? _position;
  final double allowedRadius = 100.0; // Radius dalam meter
  final LatLng schoolLocation = LatLng(-6.2088, 106.8456); // Koordinat sekolah
  final AttendanceService _attendanceService = AttendanceService();

  @override
  void initState() {
    super.initState();
    _initializeCamera();
    _checkLocation();
  }

  Future<void> _initializeCamera() async {
    final cameras = await availableCameras();
    _controller = CameraController(cameras[0], ResolutionPreset.medium);
    await _controller!.initialize();
    setState(() {});
  }

  Future<bool> _checkLocation() async {
    bool serviceEnabled = await Geolocator.isLocationServiceEnabled();
    if (!serviceEnabled) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Aktifkan layanan lokasi!')),
      );
      return false;
    }

    LocationPermission permission = await Geolocator.checkPermission();
    if (permission == LocationPermission.denied) {
      permission = await Geolocator.requestPermission();
      if (permission == LocationPermission.denied) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Izin lokasi ditolak!')),
        );
        return false;
      }
    }

    _position = await Geolocator.getCurrentPosition(
      desiredAccuracy: LocationAccuracy.high,
    );
    final Distance distance = Distance();
    double distanceInMeters = distance(
      LatLng(_position!.latitude, _position!.longitude),
      schoolLocation,
    );

    if (distanceInMeters > allowedRadius) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Anda di luar radius sekolah!')),
      );
      return false;
    }
    return true;
  }

  Future<void> _submitAttendance() async {
    if (await _checkLocation()) {
      final image = await _controller!.takePicture();
      bool success = await _attendanceService.submitAttendance(
        image.path,
        _position!.latitude,
        _position!.longitude,
      );
      if (success) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Absensi berhasil!')),
        );
      } else {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Absensi gagal, coba lagi!')),
        );
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    if (_controller == null || !_controller!.value.isInitialized) {
      return Center(child: CircularProgressIndicator());
    }
    return Scaffold(
      appBar: AppBar(title: Text('Absen Wajah')),
      body: Column(
        children: [
          CameraPreview(_controller!),
          SizedBox(height: 20),
          Text(_position != null
              ? 'Lokasi: ${_position!.latitude}, ${_position!.longitude}'
              : 'Memeriksa lokasi...'),
        ],
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _submitAttendance,
        child: Icon(Icons.camera),
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

#### **Backend: `AttendanceService.java`**
```java
@Service
public class AttendanceService {
    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private MapService mapService;

    private static final double ALLOWED_RADIUS = 100.0; // Radius dalam meter
    private static final double SCHOOL_LAT = -6.2088;
    private static final double SCHOOL_LON = 106.8456;

    public boolean submitAttendance(String imagePath, double latitude, double longitude) {
        // Validasi radius
        double distance = mapService.calculateDistance(latitude, longitude, SCHOOL_LAT, SCHOOL_LON);
        if (distance > ALLOWED_RADIUS) {
            return false;
        }

        // Validasi wajah (misalnya dengan API eksternal)
        boolean faceVerified = verifyFace(imagePath);
        if (!faceVerified) {
            return false;
        }

        // Simpan data absensi
        Attendance attendance = new Attendance();
        attendance.setImagePath(imagePath);
        attendance.setLatitude(latitude);
        attendance.setLongitude(longitude);
        attendance.setTimestamp(LocalDateTime.now());
        attendanceRepository.save(attendance);
        return true;
    }

    private boolean verifyFace(String imagePath) {
        // Logika verifikasi wajah (contoh: panggil API eksternal)
        return true; // Placeholder
    }
}
```

#### **Backend: `MapService.java` (Geofencing)**
```java
@Service
public class MapService {
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371000; // Radius bumi dalam meter
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Jarak dalam meter
    }
}
```

---

### **Penyempurnaan Menu dan Fitur Tambahan**

1. **Notifikasi**  
   - **Model**: `Notification` menyimpan pesan seperti "Absen berhasil", "Izin disetujui", atau "Barang rusak dilaporkan".  
   - **Frontend**: `notification_screen.dart` menampilkan daftar notifikasi.  
   - **Backend**: `NotificationService.java` mengirim notifikasi (bisa via Firebase Cloud Messaging).

2. **Dashboard**  
   - **Model**: `Dashboard` menyimpan data ringkasan (kehadiran, jumlah izin, laporan kerusakan).  
   - **Frontend**: `dashboard_screen.dart` untuk visualisasi data.  
   - **Backend**: `DashboardController.java` menyediakan data agregat.

3. **Manajemen Jadwal**  
   - **Model**: `Schedule` mencakup jadwal kelas, ekstrakurikuler, dan shalat.  
   - **Frontend**: `schedule_screen.dart` untuk menampilkan jadwal.  
   - **Backend**: `ScheduleController.java` untuk CRUD jadwal.

---

### **Dependensi Tambahan**

#### **Backend (pom.xml)**  
```xml
<dependencies>
    <!-- Firebase untuk notifikasi -->
    <dependency>
        <groupId>com.google.firebase</groupId>
        <artifactId>firebase-admin</artifactId>
        <version>9.1.1</version>
    </dependency>
</dependencies>
```

#### **Frontend (pubspec.yaml)**  
```yaml
dependencies:
  flutter_map: ^6.1.0
  latlong2: ^0.9.0
  geolocator: ^10.0.0
  camera: ^0.10.0
  image_picker: ^1.0.0
  http: ^1.0.0
  firebase_messaging: ^14.0.0  # Untuk notifikasi
```

---

### **Contoh Notifikasi (`notification_service.dart`)**  
```dart
import 'package:firebase_messaging/firebase_messaging.dart';

class NotificationService {
  final FirebaseMessaging _firebaseMessaging = FirebaseMessaging.instance;

  Future<void> initialize() async {
    await _firebaseMessaging.requestPermission();
    FirebaseMessaging.onMessage.listen((RemoteMessage message) {
      print('Notifikasi: ${message.notification?.title}');
    });
  }

  Future<void> sendNotification(String title, String body) async {
    // Panggil API backend untuk mengirim notifikasi
  }
}
```

---

Apakah ada fitur lain yang ingin kamu tambahkan atau bagian tertentu yang ingin saya kembangkan lebih lanjut? Struktur ini sudah cukup komprehensif, tapi saya bisa menyesuaikan lebih jauh sesuai kebutuhanmu!