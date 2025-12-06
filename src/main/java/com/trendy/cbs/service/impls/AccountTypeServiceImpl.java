package com.trendy.cbs.service.impls;

import com.trendy.cbs.entity.AccountType;
import com.trendy.cbs.exception.DuplicationResource;
import com.trendy.cbs.exception.ResourceNotFoundException;
import com.trendy.cbs.mapper.AccountTypeMapper;
import com.trendy.cbs.payload.dto.AccountTypeDTO;
import com.trendy.cbs.payload.request.AccountTypeRequest;
import com.trendy.cbs.repos.AccountTypeRepository;
import com.trendy.cbs.service.AccountTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountTypeServiceImpl implements AccountTypeService {

    private final AccountTypeRepository accountTypeRepository;
    private final AccountTypeMapper accountTypeMapper;

    /**
     * Implements the creation of a new account type.
     *
     * This method checks if an account type with the given code already exists in the repository. If it does,
     * a DuplicationResource exception is thrown. Otherwise, it maps the request to an entity, saves it to the
     * repository, and maps the saved entity back to a DTO for return.
     *
     * @param request The request object containing the details for the new account type.
     * @return The created AccountTypeDTO.
     * @throws DuplicationResource If an account type with the same code already exists.
     */
    @Override
    public AccountTypeDTO createAccountType(AccountTypeRequest request) {
        Optional<AccountType> accountType = accountTypeRepository.findByCode(request.getCode());

        if(accountType.isPresent()) {
            throw new DuplicationResource("Account Type already exists!!");
        }

        return accountTypeMapper.toDTO(accountTypeRepository.save(accountTypeMapper.toEntity(request)));
    }

    /**
     * Retrieves all account types.
     *
     * This method overrides the interface to fetch all account types from the repository and maps them
     * to a list of DTOs for return.
     *
     * @return A list of AccountTypeDTO objects representing all account types.
     */
    @Override
    public List<AccountTypeDTO> getAllAccountTypes() {

        return accountTypeMapper.toListDTO(accountTypeRepository.findAll());
    }

    /**
     * Retrieves an account type by its ID.
     *
     * This method overrides the interface to find an account type by the given ID in the repository.
     * If found, it maps the entity to a DTO; otherwise, it throws a ResourceNotFoundException.
     *
     * @param id The ID of the account type to retrieve.
     * @return The AccountTypeDTO corresponding to the given ID.
     * @throws ResourceNotFoundException If no account type is found with the specified ID.
     */
    @Override
    public AccountTypeDTO getAccountTypeById(Long id) {

        return accountTypeRepository.findById(id)
                .map(accountTypeMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Account type",id));
    }

    /**
     * Updates an existing account type.
     *
     * This method overrides the interface to update an account type by first retrieving it from the
     * repository using the provided ID. If not found, it throws a ResourceNotFoundException. It then
     * updates the entity with details from the request, saves the changes, and returns the updated DTO.
     *
     * @param id The ID of the account type to update.
     * @param request The request object containing the updated details for the account type.
     * @return The updated AccountTypeDTO.
     * @throws ResourceNotFoundException If no account type is found with the specified ID.
     */
    @Override
    public AccountTypeDTO updateAccountType(Long id, AccountTypeRequest request) {
        AccountType accountType = accountTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account type",id));

        accountTypeMapper.updateEntityFromRequest(request,accountType);

        return accountTypeMapper.toDTO(accountTypeRepository.save(accountType));
    }

    /**
     * Deletes an account type by its ID.
     *
     * This method overrides the interface to delete an account type by first retrieving it from the
     * repository using the provided ID. If not found, it throws a ResourceNotFoundException. It then
     * deletes the entity and returns a success message.
     *
     * @param id The ID of the account type to delete.
     * @return A success message indicating the deletion.
     * @throws ResourceNotFoundException If no account type is found with the specified ID.
     */
    @Override
    public String deleteAccountTypeById(Long id) {
        AccountType accountType = accountTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account type",id));

        accountTypeRepository.delete(accountType);
        return "IdentityDoc successfully deleted with id " + id;
    }
}
