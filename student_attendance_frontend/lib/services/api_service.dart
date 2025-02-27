import 'dart:convert';
import 'package:http/http.dart' as http;
import '../models/student.dart';
import '../models/attendance.dart';
import '../models/notification.dart';

class ApiService {
    final String baseUrl = 'http://localhost:8080';  // Adjust the base URL if needed

    Future<List<Student>> fetchStudents() async {
        final response = await http.get(Uri.parse('
            $baseUrl/students'
        ));

        if (response.statusCode == 200) {
            List jsonResponse = json.decode(response.body);
            return jsonResponse.map((student) => Student.fromJson(student)).toList();
        } else {
            throw Exception('Failed to load students');
        }
    }

    Future<Student> createStudent(Student student) async {
        final response = await http.post(
            Uri.parse('$baseUrl/students'),
            headers: <String, String>{
                'Content-Type': 'application/json; charset=UTF-8',
            },
            body: jsonEncode(student.toJson()),
        );

        if (response.statusCode == 201) {
            return Student.fromJson(json.decode(response.body));
        } else {
            throw Exception('Failed to create student');
        }
    }

    Future<List<Attendance>> fetchAttendance() async {
        final response = await http.get(Uri.parse('$baseUrl/attendance'));

        if (response.statusCode == 200) {
            List jsonResponse = json.decode(response.body);
            return jsonResponse.map((attendance) => Attendance.fromJson(attendance)).toList();
        } else {
            throw Exception('Failed to load attendance');
        }
    }

    Future<List<Notification>> fetchNotifications() async {
        final response = await http.get(Uri.parse('$baseUrl/notifications'));

        if (response.statusCode == 200) {
            List jsonResponse = json.decode(response.body);
            return jsonResponse.map((notification) => Notification.fromJson(notification)).toList();
        } else {
            throw Exception('Failed to load notifications');
        }
    }
}
