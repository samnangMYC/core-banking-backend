package com.trendy.cbs.service;

import com.trendy.cbs.payload.dto.IdentityDocDTO;
import com.trendy.cbs.payload.request.IdentityDocRequest;
import com.trendy.cbs.payload.request.IdentityDocStatusRequest;
import jakarta.validation.Valid;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface IdentityDocService {
    IdentityDocDTO createIdentityDoc(Jwt jwt, @Valid IdentityDocRequest request);

    List<IdentityDocDTO> getAllIdentityDoc();

    IdentityDocDTO getIdentityDocById(Long id);

    IdentityDocDTO updateIdentityDoc(Long id, @Valid IdentityDocRequest request);

    String deleteIdentityDoc(Long id);

    IdentityDocDTO updateIdentityDocStatus(Long id, @Valid IdentityDocStatusRequest request);
}
