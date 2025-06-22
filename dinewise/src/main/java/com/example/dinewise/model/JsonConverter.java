// package com.example.dinewise.model;

// import java.util.ArrayList;
// import java.util.List;

// import com.fasterxml.jackson.core.JsonProcessingException;
// import com.fasterxml.jackson.databind.ObjectMapper;

// import aj.org.objectweb.asm.TypeReference;
// import io.jsonwebtoken.io.IOException;
// import jakarta.persistence.AttributeConverter;
// import jakarta.persistence.Converter;

// @Converter
// public class JsonConverter implements AttributeConverter<List<String>, String> {
//     private final ObjectMapper objectMapper = new ObjectMapper();

//     @Override
//     public String convertToDatabaseColumn(List<String> attribute) {
//         try {
//             return objectMapper.writeValueAsString(attribute);
//         } catch (JsonProcessingException e) {
//             return "[]";
//         }
//     }

   
//     @Override
//     public List<String> convertToEntityAttribute(String dbData) {
//         try {
//             return objectMapper.readValue(dbData, new TypeReference<>() {});
//         } catch (JsonProcessingException e) {
//             return new ArrayList<>();
//         }
//     }
// }


package com.example.dinewise.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Converter
public class JsonConverter implements AttributeConverter<List<String>, String> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        try {
            return mapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            // fallback to empty JSON array
            return "[]";
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        try {
            return mapper.readValue(dbData, new TypeReference<List<String>>() {});
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
}
