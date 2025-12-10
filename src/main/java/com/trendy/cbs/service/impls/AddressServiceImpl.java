package com.trendy.cbs.service.impls;

import com.trendy.cbs.entity.Address;
import com.trendy.cbs.entity.Customer;
import com.trendy.cbs.exception.ResourceNotFoundException;
import com.trendy.cbs.mapper.AddressMapper;
import com.trendy.cbs.payload.dto.AddressDTO;
import com.trendy.cbs.payload.request.AddressRequest;
import com.trendy.cbs.repos.CustomerRepository;
import com.trendy.cbs.repos.AddressRepository;
import com.trendy.cbs.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;
    private final AddressMapper addressMapper;

    /**
     * Creates a new customer address based on the provided request.
     *
     * This method verifies the existence of the customer, checks if the customer already has an address,
     * maps the request to an Address entity, associates it with the customer, saves it to the repository,
     * and returns the corresponding DTO.
     *
     * @param request the AddressRequest containing the customer ID and address details
     * @return the created AddressDTO
     * @throws ResourceNotFoundException() if no customer is found with the provided ID
     * @throws IllegalArgumentException if the customer already has an existing address
     */
    @Override
    public AddressDTO createAddress(Long customerId,AddressRequest request) {

        // verify customer exist
         Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("", customerId) );

        // check if customer already has an address
        if(customer.getAddress() != null){
            throw new IllegalArgumentException("Address already exists");
        }

        // map request to addresses
        Address address = addressMapper.toEntity(request);
        addressRepository.save(address);

        return addressMapper.toDTO(address);
    }


    /**
     * Retrieves all customer addresses from the repository and maps them to DTOs.
     *
     * @return a list of AddressDTO objects representing all customer addresses
     */
    @Override
    public List<AddressDTO> getAllAddress() {
        return addressMapper.toDTOList(addressRepository.findAll());
    }

    /**
     * Retrieves a customer address by its ID.
     *
     * This method fetches the address from the repository using the provided ID.
     * If the address is not found, it throws a ResourceNotFoundException.
     *
     * @param id the ID of the address to retrieve
     * @return the AddressDTO if found, or null if the method is annotated to allow null
     * @throws ResourceNotFoundException if no address is found with the provided ID
     */
    @Override
    public @Nullable AddressDTO getAddressById(Long id) {
        return addressRepository.findById(id)
                .map(addressMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Addresses", id));
    }

    /**
     * Updates an existing customer address based on the provided ID and request.
     *
     * This method fetches the existing address from the repository, copies the updatable properties
     * from the request to the address entity (excluding addressId and customer), saves the changes,
     * and returns the updated AddressDTO.
     *
     * @param id the ID of the address to update
     * @param request the UpdateAddressRequest containing the fields to update
     * @return the updated AddressDTO
     * @throws ResourceNotFoundException if no address is found with the provided ID
     */
    @Override
    public AddressDTO updateAddress(Long id, AddressRequest request) {
        // find existed addresses
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Addresses",id));

        addressMapper.updateEntityFromRequest(request,address);

        return addressMapper.toDTO(addressRepository.save(address));
    }

    /**
     * Deletes a customer address by its ID.
     *
     * This method fetches the address from the repository using the provided ID,
     * deletes it if found, and returns a success message.
     *
     * @param id the ID of the address to delete
     * @return a success message if the deletion is successful, or null if the method is annotated to allow null
     * @throws ResourceNotFoundException if no address is found with the provided ID
     */
    @Override
    public String deleteAddressById(Long id) {
        // find existed addresses
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Addresses",id));

        addressRepository.delete(address);
        return "Address has been deleted successfully!!";
    }


}
