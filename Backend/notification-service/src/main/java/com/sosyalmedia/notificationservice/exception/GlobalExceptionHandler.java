package com.sosyalmedia.notificationservice.exception;

import com.sosyalmedia.notificationservice.dto.response.ApiResponse;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(PostDeadlineNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handlePostDeadlineNotFound(
            PostDeadlineNotFoundException ex) {
        log.error("❌ Post deadline not found: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(CustomerValidationException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomerValidation(
            CustomerValidationException ex) {
        log.error("❌ Customer validation failed: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(PostDeadlineArchiveNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleArchiveNotFound(
            PostDeadlineArchiveNotFoundException ex) {
        log.error("❌ Archive not found: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomerNotFound(
            CustomerNotFoundException ex) {
        log.error("❌ Customer not found: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(CustomerNotActiveException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomerNotActive(
            CustomerNotActiveException ex) {
        log.error("❌ Customer not active: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage()));
    }

    // ✅ YENİ - Validation Errors (Field bazlı)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        log.error("❌ Validation errors: {}", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Validation hatası", errors));
    }

    // ✅ YENİ - JSON Parse Error (Tarih formatı vb.)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex) {

        log.error("❌ JSON parse error: {}", ex.getMessage());

        String message = "Geçersiz JSON formatı";
        Map<String, String> errorDetails = new HashMap<>();

        // Tarih parse hatası kontrolü
        if (ex.getCause() instanceof DateTimeParseException) {
            DateTimeParseException dateEx = (DateTimeParseException) ex.getCause();
            message = "Geçersiz tarih formatı";
            errorDetails.put("scheduledDate", "Tarih formatı YYYY-MM-DD şeklinde olmalıdır (örn: 2025-11-05)");
            errorDetails.put("receivedValue", dateEx.getParsedString());
        }
        // LocalDate deserialize hatası
        else if (ex.getMessage().contains("LocalDate")) {
            message = "Geçersiz tarih formatı";
            errorDetails.put("scheduledDate", "Tarih formatı YYYY-MM-DD şeklinde olmalıdır (örn: 2025-11-05)");

            // Gönderilen değeri extract et
            String receivedValue = extractInvalidValue(ex.getMessage());
            if (receivedValue != null) {
                errorDetails.put("receivedValue", receivedValue);
            }
        }
        // Enum parse hatası
        else if (ex.getMessage().contains("Enum")) {
            message = "Geçersiz enum değeri";
            errorDetails.put("error", "Geçerli değerlerden birini seçiniz");
        }
        // Genel JSON hatası
        else {
            errorDetails.put("error", "JSON formatı hatalı. Lütfen gönderdiğiniz veriyi kontrol ediniz.");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(message, errorDetails));
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ApiResponse<Void>> handleFeignException(FeignException ex) {
        log.error("❌ Feign client error: {}", ex.getMessage());

        String message = "Dış servis ile iletişim hatası";
        if (ex.status() == 404) {
            message = "İlgili kayıt dış serviste bulunamadı";
        } else if (ex.status() == 503) {
            message = "Dış servis şu anda erişilebilir değil";
        }

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ApiResponse.error(message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
        log.error("❌ Unexpected error: ", ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Beklenmeyen bir hata oluştu: " + ex.getMessage()));
    }

    // ========== HELPER METHOD ==========

    /**
     * Hata mesajından geçersiz değeri extract eder
     */
    private String extractInvalidValue(String errorMessage) {
        try {
            // "invalid-date" gibi değerleri extract et
            int startIndex = errorMessage.indexOf("\"");
            if (startIndex != -1) {
                int endIndex = errorMessage.indexOf("\"", startIndex + 1);
                if (endIndex != -1) {
                    return errorMessage.substring(startIndex + 1, endIndex);
                }
            }
        } catch (Exception e) {
            log.debug("Could not extract invalid value from error message");
        }
        return null;
    }
}