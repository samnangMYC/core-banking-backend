package com.trendy.cbs.service.impls;

import com.trendy.cbs.entity.IdentityDoc;
import com.trendy.cbs.enums.DocStatus;
import com.trendy.cbs.exception.ResourceNotFoundException;
import com.trendy.cbs.mapper.IdentityDocMapper;
import com.trendy.cbs.payload.dto.IdentityDocDTO;
import com.trendy.cbs.payload.request.IdentityDocRequest;
import com.trendy.cbs.repos.IdentityDocRepository;
import com.trendy.cbs.repos.UserRepository;
import com.trendy.cbs.service.IdentityDocService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IdentityDocServiceImpl implements IdentityDocService {

    private final IdentityDocRepository identityDocRepository;
    private final UserRepository userRepository;
    private final IdentityDocMapper identityDocMapper;


    /**
     * Creates a new identity document entity from the given request.
     * *
     * This method fetches the associated user, maps the request to an entity,
     * sets the initial status to ON_REVIEW, saves the entity, and returns the DTO.
     *
     * @param request the request object containing identity document details
     * @return the DTO representation of the created identity document
     * @throws ResourceNotFoundException() if the user with the specified ID is not found
     */
    @Override
    public IdentityDocDTO createIdentityDoc(Long userId,IdentityDocRequest request) {

        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User",userId));


        IdentityDoc identityDoc= identityDocMapper.toEntity(request);

        identityDoc.setVerifiedDate(null);
        identityDoc.setDocStatus(DocStatus.ON_REVIEW);

        identityDocRepository.save(identityDoc);

        return identityDocMapper.toDTO(identityDoc);
    }

    /**
     * Retrieves a list of all identity documents from the repository.
     *
     * @return a list of DTOs representing all identity documents
     */
    @Override
    public List<IdentityDocDTO> getAllIdentityDoc() {
        return identityDocMapper.toDTO(identityDocRepository.findAll());
    }

    /**
     * Retrieves an IdentityDocDTO by its unique identifier.
     *
     * @param id the unique identifier of the IdentityDoc
     * @return the IdentityDocDTO if found
     * @throws ResourceNotFoundException if no IdentityDoc is found with the given id
     */
    @Override
    public IdentityDocDTO getIdentityDocById(Long id) {
        return identityDocRepository.findById(id)
                .map(identityDocMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("IdentityDoc",id));

    }

    /**
     * Retrieves an IdentityDocDTO by its unique identifier.
     *
     * @param id the unique identifier of the IdentityDoc
     * @return the IdentityDocDTO if found
     * @throws ResourceNotFoundException if no IdentityDoc is found with the given id
     */
    @Override
    public IdentityDocDTO updateIdentityDoc(Long id, IdentityDocRequest request) {

        identityDocRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("IdentityDoc",id));

        IdentityDoc identityDoc = identityDocMapper.toEntity(request);
        identityDoc.setDocId(id);
        identityDoc.setDocStatus(DocStatus.ON_REVIEW);

        return identityDocMapper.toDTO(identityDocRepository.save(identityDoc));
    }

    /**
     * Deletes an IdentityDoc by its unique identifier and returns a confirmation message.
     *
     * @param id the unique identifier of the IdentityDoc to delete
     * @return a string message confirming the deletion
     * @throws ResourceNotFoundException if no IdentityDoc is found with the given id
     */
    @Override
    public String deleteIdentityDoc(Long id) {
        IdentityDoc doc = identityDocRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("IdentityDoc", id));

        identityDocRepository.delete(doc);
        return "IdentityDoc deleted with id " + id;
    }



}
