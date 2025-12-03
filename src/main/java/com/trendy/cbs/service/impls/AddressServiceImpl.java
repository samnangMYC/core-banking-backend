package com.trendy.cbs.service.impls;

import com.trendy.cbs.entity.Address;
import com.trendy.cbs.entity.User;
import com.trendy.cbs.exception.ResourceNotFoundException;
import com.trendy.cbs.mapper.AddressMapper;
import com.trendy.cbs.payload.dto.AddressDTO;
import com.trendy.cbs.payload.request.AddressRequest;
import com.trendy.cbs.payload.request.UpdateAddressRequest;
import com.trendy.cbs.repos.UserRepository;
import com.trendy.cbs.repos.AddressRepository;
import com.trendy.cbs.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final AddressMapper addressMapper;

    /**
     * Creates a new user address based on the provided request.
     *
     * This method verifies the existence of the user, checks if the user already has an address,
     * maps the request to an Address entity, associates it with the user, saves it to the repository,
     * and returns the corresponding DTO.
     *
     * @param request the AddressRequest containing the user ID and address details
     * @return the created AddressDTO
     * @throws ResourceNotFoundException() if no user is found with the provided ID
     * @throws IllegalArgumentException if the user already has an existing address
     */
    @Override
    public AddressDTO createUserAddress(Long userId,AddressRequest request) {

        // verify user exist
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId) );

        // check if user already has an address
        if(user.getAddress() != null){
            throw new IllegalArgumentException("Address already exists");
        }

        // map request to addresses
        Address address = addressMapper.toEntity(request);
        addressRepository.save(address);

        return addressMapper.toDTO(address);
    }


    /**
     * Retrieves all user addresses from the repository and maps them to DTOs.
     *
     * @return a list of AddressDTO objects representing all user addresses
     */
    @Override
    public List<AddressDTO> getAllUserAddress() {
        return addressMapper.toDTOList(addressRepository.findAll());
    }

    /**
     * Retrieves a user address by its ID.
     *
     * This method fetches the address from the repository using the provided ID.
     * If the address is not found, it throws a ResourceNotFoundException.
     *
     * @param id the ID of the address to retrieve
     * @return the AddressDTO if found, or null if the method is annotated to allow null
     * @throws ResourceNotFoundException if no address is found with the provided ID
     */
    @Override
    public @Nullable AddressDTO getUserAddressById(Long id) {
        return addressRepository.findById(id)
                .map(addressMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Addresses", id));
    }

    /**
     * Updates an existing user address based on the provided ID and request.
     *
     * This method fetches the existing address from the repository, copies the updatable properties
     * from the request to the address entity (excluding addressId and user), saves the changes,
     * and returns the updated AddressDTO.
     *
     * @param id the ID of the address to update
     * @param request the UpdateAddressRequest containing the fields to update
     * @return the updated AddressDTO
     * @throws ResourceNotFoundException if no address is found with the provided ID
     */
    @Override
    public AddressDTO updateUserAddress(Long id, AddressRequest request) {
        // find existed addresses
        addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Addresses",id));

        Address address = addressMapper.toEntity(request);
        address.setAddressId(id);

        return addressMapper.toDTO(addressRepository.save(address));
    }

    /**
     * Deletes a user address by its ID.
     *
     * This method fetches the address from the repository using the provided ID,
     * deletes it if found, and returns a success message.
     *
     * @param id the ID of the address to delete
     * @return a success message if the deletion is successful, or null if the method is annotated to allow null
     * @throws ResourceNotFoundException if no address is found with the provided ID
     */
    @Override
    public String deleteUserAddressById(Long id) {
        // find existed addresses
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Addresses",id));

        addressRepository.delete(address);
        return "Address has been deleted successfully!!";
    }


}
