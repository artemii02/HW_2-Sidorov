//package com.example.RestaurantModel.configuration;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
//        if (e.getMessage().equals("out of stock")) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Доступное количество блюд закончилось");
//        }
//        // Другие обработки исключений
////        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Произошла ошибка");
//        return null;
//    }
//}