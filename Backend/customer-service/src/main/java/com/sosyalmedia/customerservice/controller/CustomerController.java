package com.sosyalmedia.customerservice.controller;

import com.sosyalmedia.customerservice.dto.*;
import com.sosyalmedia.customerservice.entity.Customer;
import com.sosyalmedia.customerservice.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "Customer Management", description = "Müşteri Yönetimi API'leri")
public class CustomerController {

    private final CustomerService customerService;

    @Operation(summary = "Yeni müşteri oluştur", description = "Tüm zorunlu bilgilerle yeni müşteri kaydı oluşturur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Müşteri başarıyla oluşturuldu"),
            @ApiResponse(responseCode = "400", description = "Geçersiz istek"),
            @ApiResponse(responseCode = "409", description = "Müşteri zaten mevcut")
    })
    @PostMapping
    public ResponseEntity<com.sosyalmedia.customerservice.dto.ApiResponse<CustomerResponse>> createCustomer(
            @Valid @RequestBody CustomerRequest request) {
        log.info("REST: Creating customer: {}", request.getCompanyName());
        CustomerResponse response = customerService.createCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(com.sosyalmedia.customerservice.dto.ApiResponse.success("Müşteri başarıyla oluşturuldu", response));
    }

    @Operation(summary = "Tüm müşterileri listele", description = "Sistemdeki tüm aktif müşterileri getirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Başarılı")
    })
    @GetMapping
    public ResponseEntity<com.sosyalmedia.customerservice.dto.ApiResponse<List<CustomerListResponse>>> getAllCustomers() {
        log.info("REST: Getting all customers");
        return ResponseEntity.ok(com.sosyalmedia.customerservice.dto.ApiResponse.success(customerService.getAllCustomers()));
    }

    @Operation(summary = "ID ile müşteri getir", description = "Belirtilen ID'ye sahip müşterinin tüm bilgilerini getirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Başarılı"),
            @ApiResponse(responseCode = "404", description = "Müşteri bulunamadı")
    })
    @GetMapping("/{id}")
    public ResponseEntity<com.sosyalmedia.customerservice.dto.ApiResponse<CustomerResponse>> getCustomerById(
            @Parameter(description = "Müşteri ID", required = true, example = "1")
            @PathVariable Long id) {
        log.info("REST: Getting customer by ID: {}", id);
        return ResponseEntity.ok(com.sosyalmedia.customerservice.dto.ApiResponse.success(customerService.getCustomerById(id)));
    }

    @Operation(summary = "Şirket adına göre müşteri getir")
    @GetMapping("/company/{companyName}")
    public ResponseEntity<com.sosyalmedia.customerservice.dto.ApiResponse<CustomerResponse>> getCustomerByCompanyName(
            @Parameter(description = "Şirket adı", required = true, example = "Cafe Sunshine")
            @PathVariable String companyName) {
        log.info("REST: Getting customer by company name: {}", companyName);
        return ResponseEntity.ok(com.sosyalmedia.customerservice.dto.ApiResponse.success(customerService.getCustomerByCompanyName(companyName)));
    }

    @Operation(summary = "Sektöre göre müşteri listele")
    @GetMapping("/sector/{sector}")
    public ResponseEntity<com.sosyalmedia.customerservice.dto.ApiResponse<List<CustomerListResponse>>> getCustomersBySector(
            @Parameter(description = "Sektör", example = "cafe")
            @PathVariable String sector) {
        log.info("REST: Getting customers by sector: {}", sector);
        return ResponseEntity.ok(com.sosyalmedia.customerservice.dto.ApiResponse.success(customerService.getCustomersBySector(sector)));
    }

    @Operation(summary = "Statüye göre müşteri listele")
    @GetMapping("/status/{status}")
    public ResponseEntity<com.sosyalmedia.customerservice.dto.ApiResponse<List<CustomerListResponse>>> getCustomersByStatus(
            @Parameter(description = "Müşteri statüsü", example = "ACTIVE")
            @PathVariable Customer.CustomerStatus status) {
        log.info("REST: Getting customers by status: {}", status);
        return ResponseEntity.ok(com.sosyalmedia.customerservice.dto.ApiResponse.success(customerService.getCustomersByStatus(status)));
    }

    @Operation(summary = "Silinmiş müşterileri listele", description = "Soft delete ile silinmiş tüm müşterileri getirir")
    @GetMapping("/deleted")
    public ResponseEntity<com.sosyalmedia.customerservice.dto.ApiResponse<List<CustomerResponse>>> getAllDeletedCustomers() {
        log.info("REST: Getting all deleted customers");
        return ResponseEntity.ok(com.sosyalmedia.customerservice.dto.ApiResponse.success(customerService.getAllDeletedCustomers()));
    }

    @Operation(summary = "Müşteri güncelle (PATCH)", description = "Sadece gönderilen alanları günceller")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Başarıyla güncellendi"),
            @ApiResponse(responseCode = "404", description = "Müşteri bulunamadı")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<com.sosyalmedia.customerservice.dto.ApiResponse<CustomerResponse>> patchCustomer(
            @Parameter(description = "Müşteri ID", required = true)
            @PathVariable Long id,
            @RequestBody CustomerUpdateRequest request) {
        log.info("REST: Patching customer ID: {}", id);
        return ResponseEntity.ok(com.sosyalmedia.customerservice.dto.ApiResponse.success("Müşteri başarıyla güncellendi",
                customerService.patchCustomer(id, request)));
    }

    @Operation(summary = "Şirket adına göre güncelle")
    @PatchMapping("/company/{companyName}")
    public ResponseEntity<com.sosyalmedia.customerservice.dto.ApiResponse<CustomerResponse>> patchCustomerByCompanyName(
            @Parameter(description = "Şirket adı")
            @PathVariable String companyName,
            @RequestBody CustomerUpdateRequest request) {
        log.info("REST: Patching customer by company name: {}", companyName);
        return ResponseEntity.ok(com.sosyalmedia.customerservice.dto.ApiResponse.success("Müşteri başarıyla güncellendi",
                customerService.patchCustomerByCompanyName(companyName, request)));
    }

    @Operation(summary = "Müşteriyi sil (Soft Delete)", description = "Müşteriyi pasif hale getirir, veriler saklanır")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Başarıyla silindi"),
            @ApiResponse(responseCode = "404", description = "Müşteri bulunamadı")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<com.sosyalmedia.customerservice.dto.ApiResponse<Void>> softDeleteCustomer(
            @Parameter(description = "Müşteri ID")
            @PathVariable Long id) {
        log.info("REST: Soft deleting customer ID: {}", id);
        customerService.softDeleteCustomer(id);
        return ResponseEntity.ok(com.sosyalmedia.customerservice.dto.ApiResponse.success("Müşteri başarıyla silindi", null));
    }

    @Operation(summary = "Şirket adına göre soft delete")
    @DeleteMapping("/company/{companyName}")
    public ResponseEntity<com.sosyalmedia.customerservice.dto.ApiResponse<Void>> softDeleteCustomerByCompanyName(
            @Parameter(description = "Şirket adı")
            @PathVariable String companyName) {
        log.info("REST: Soft deleting customer by company name: {}", companyName);
        customerService.softDeleteCustomerByCompanyName(companyName);
        return ResponseEntity.ok(com.sosyalmedia.customerservice.dto.ApiResponse.success("Müşteri başarıyla silindi", null));
    }

    @Operation(summary = "Müşteriyi kalıcı sil (Hard Delete)", description = "Müşteri ve tüm ilişkili verileri kalıcı olarak siler")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kalıcı olarak silindi"),
            @ApiResponse(responseCode = "404", description = "Müşteri bulunamadı")
    })
    @DeleteMapping("/{id}/hard")
    public ResponseEntity<com.sosyalmedia.customerservice.dto.ApiResponse<Void>> hardDeleteCustomer(
            @Parameter(description = "Müşteri ID")
            @PathVariable Long id) {
        log.info("REST: Hard deleting customer ID: {}", id);
        customerService.hardDeleteCustomer(id);
        return ResponseEntity.ok(com.sosyalmedia.customerservice.dto.ApiResponse.success("Müşteri kalıcı olarak silindi", null));
    }

    @Operation(summary = "Silinmiş müşteriyi geri yükle", description = "Soft delete ile silinmiş müşteriyi aktif hale getirir")
    @PutMapping("/{id}/restore")
    public ResponseEntity<com.sosyalmedia.customerservice.dto.ApiResponse<Void>> restoreCustomer(
            @Parameter(description = "Müşteri ID")
            @PathVariable Long id) throws Throwable {
        log.info("REST: Restoring customer ID: {}", id);
        customerService.restoreCustomer(id);
        return ResponseEntity.ok(com.sosyalmedia.customerservice.dto.ApiResponse.success("Müşteri geri yüklendi", null));
    }
}