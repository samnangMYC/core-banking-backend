package com.trendy.cbs.controller;

import com.trendy.cbs.payload.dto.IdentityDocDTO;
import com.trendy.cbs.payload.request.IdentityDocRequest;
import com.trendy.cbs.service.IdentityDocService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/users/me/identity-doc")
@RequiredArgsConstructor
public class IdentityController {

    private final IdentityDocService identityDocService;

    /**
     * Creates a new identity document based on the provided request.
     *
     * @param request the request containing details for the new identity document
     * @return a ResponseEntity containing the created IdentityDocDTO with HTTP status OK
     */
    @PostMapping
    public ResponseEntity<IdentityDocDTO> createIdentityDoc(@RequestHeader("X-User-Id") Long userId,
                                                            @Valid @RequestBody IdentityDocRequest request){
        log.info("Creating identity doc{}", request.toString());
        return ResponseEntity.ok(identityDocService.createIdentityDoc(userId,request));
    }

    /**
     * Retrieves a list of all identity documents.
     *
     * @return a ResponseEntity containing a list of all IdentityDocDTOs with HTTP status OK
     */
    @GetMapping
    public ResponseEntity<List<IdentityDocDTO>> getAllIdentityDoc(){
        log.info("Fetching all identity doc..");
        return ResponseEntity.ok(identityDocService.getAllIdentityDoc());
    }

    /**
     * Retrieves an IdentityDoc by its unique identifier.
     *
     * @param id the unique identifier of the IdentityDoc to fetch
     * @return ResponseEntity containing the IdentityDocDTO if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<IdentityDocDTO> getIdentityDocById(@PathVariable Long id){
        log.info("Fetching identity doc by id{}", id.toString());
        return ResponseEntity.ok(identityDocService.getIdentityDocById(id));
    }

    /**
     * Updates an existing IdentityDoc with the provided request data.
     *
     * @param id the unique identifier of the IdentityDoc to update
     * @param request the IdentityDocRequest containing updated details (validated)
     * @return ResponseEntity containing the updated IdentityDocDTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<IdentityDocDTO> updateIdentityDoc(@PathVariable Long id,
                                                            @Valid @RequestBody IdentityDocRequest request){
        log.info("Updating identity doc{}", request.toString());
        return ResponseEntity.ok(identityDocService.updateIdentityDoc(id,request));
    }

    /**
     * Deletes an IdentityDoc by its unique identifier.
     *
     * @param id the unique identifier of the IdentityDoc to delete
     * @return ResponseEntity containing a confirmation message upon successful deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteIdentityDoc(@PathVariable Long id){
        log.info("Deleting identity doc{}", id.toString());
        return ResponseEntity.ok(identityDocService.deleteIdentityDoc(id));
    }



}
