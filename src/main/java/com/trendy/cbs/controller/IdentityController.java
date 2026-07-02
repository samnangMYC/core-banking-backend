package com.trendy.cbs.controller;

import com.trendy.cbs.payload.dto.IdentityDocDTO;
import com.trendy.cbs.payload.request.IdentityDocRequest;
import com.trendy.cbs.payload.request.IdentityDocStatusRequest;
import com.trendy.cbs.service.IdentityDocService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.jspecify.annotations.NonNull;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/customer/me/identity-doc")

@RequiredArgsConstructor
public class IdentityController {

    private final IdentityDocService identityDocService;

    @PreAuthorize("hasAnyRole('CUSTOMER','TELLER','MANAGER','SYSTEM_ADMIN')")
    @PostMapping
    public ResponseEntity<IdentityDocDTO> createIdentityDoc(@AuthenticationPrincipal Jwt jwt,
                                                            @Valid @RequestBody IdentityDocRequest request){
        log.info("Creating identity doc{}", request.toString());
        return ResponseEntity.ok(identityDocService.createIdentityDoc(jwt,request));
    }

    @PreAuthorize("hasAnyRole('TELLER','MANAGER','SYSTEM_ADMIN')")
    @GetMapping
    public ResponseEntity<List<IdentityDocDTO>> getAllIdentityDoc(){
        log.info("Fetching all identity doc..");
        return ResponseEntity.ok(identityDocService.getAllIdentityDoc());
    }

    @GetMapping("/{id}")
    public ResponseEntity<IdentityDocDTO> getIdentityDocById(@PathVariable Long id){
        log.info("Fetching identity doc by id{}", id.toString());
        return ResponseEntity.ok(identityDocService.getIdentityDocById(id));
    }

    @PreAuthorize("hasAnyRole('CUSTOMER','TELLER','MANAGER','SYSTEM_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<IdentityDocDTO> updateIdentityDoc(@PathVariable Long id,
                                                            @Valid @RequestBody IdentityDocRequest request){
        log.info("Updating identity doc{}", request.toString());
        return ResponseEntity.ok(identityDocService.updateIdentityDoc(id,request));
    }

    @PreAuthorize("hasAnyRole('CUSTOMER','TELLER','MANAGER','SYSTEM_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteIdentityDoc(@PathVariable Long id){
        log.info("Deleting identity doc{}", id.toString());
        return ResponseEntity.ok(identityDocService.deleteIdentityDoc(id));
    }

    @PatchMapping("{id}")
    public ResponseEntity<IdentityDocDTO> updateIdentityDocStatus(@PathVariable Long id,
                                                                  @Valid @RequestBody IdentityDocStatusRequest request){
        log.info("Updating status of identity doc{}", request.toString());
        return ResponseEntity.ok(identityDocService.updateIdentityDocStatus(id,request));

    }


}
