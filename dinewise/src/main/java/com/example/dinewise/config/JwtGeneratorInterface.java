package com.example.dinewise.config;
import java.util.Map;

// import com.example.dinewise.model.MessManager;
import com.example.dinewise.model.Student;
// public interface JwtGeneratorInterface {

//     Map<String, String> generateToken(Student student);
//     // Map<String, String> generateTokenForMessManager(MessManager messManager);
// }

public interface JwtGeneratorInterface {
    Map<String, String> generateToken(Student student);
    Map<String, String> generateToken(String subject, String role); // ✅ for both
}
