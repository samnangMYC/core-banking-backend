package com.trendy.cbs.service.impls;

import com.trendy.cbs.entity.Customer;
import com.trendy.cbs.entity.CustomerProfile;
import com.trendy.cbs.enums.CustomerStatus;
import com.trendy.cbs.exception.DuplicationResource;
import com.trendy.cbs.exception.ResourceNotFoundException;
import com.trendy.cbs.mapper.CustomerMapper;
import com.trendy.cbs.payload.dto.CustomerDTO;
import com.trendy.cbs.payload.dto.CustomerWithProfile;
import com.trendy.cbs.payload.request.CustomerRequest;
import com.trendy.cbs.payload.request.CustomerStatusRequest;
import com.trendy.cbs.repos.CustomerProfileRepository;
import com.trendy.cbs.repos.CustomerRepository;
import com.trendy.cbs.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerProfileRepository customerProfileRepository;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerDTO createNewCustomer(CustomerRequest customerRequest) {

        // Check phone number exists
        if (customerProfileRepository.existsByPhoneNumber(customerRequest.getPhoneNumber())) {
            throw new DuplicationResource("Phone number already exists");
        }

        // convert req to customer
        Customer customer = customerMapper.toCustomer(customerRequest);

        // assign active to status
        customer.setStatus(CustomerStatus.PENDING_VERIFICATION);

        // initialize CustomerProfile by filter from req using mapstruct "toProfile"
        CustomerProfile profile = customerMapper.toProfile(customerRequest);
        customer.setProfile(profile);
        profile.setCustomer(customer);

        // save to customerEntity
        Customer savedCustomer = customerRepository.save(customer);
        customerProfileRepository.save(profile);

        return customerMapper.toDto(savedCustomer,profile);

    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        // fetch list of customer entity
        List<Customer> customers = customerRepository.findAll();

        // use customerList for mapping to CustomerWithProfile
        List<CustomerWithProfile> customerWithProfile = customers.stream()
                .map(customer -> {
                    CustomerWithProfile uwp = new CustomerWithProfile();
                    uwp.setCustomer(customer);
                    uwp.setProfile(customer.getProfile());
                    return uwp;
                })
                .toList();

        // map to CustomerDTO List by mapstruct "toDtoList"
        return customerMapper.toDtoList(customerWithProfile);
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(Long id) {
        // validate ensure customer exists
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer",id));

        // get customer profile in customer entity
        CustomerProfile profile = customer.getProfile();

        // map to CustomerDTO
        CustomerDTO customerDTO = customerMapper.toDto(customer, profile);

        return Optional.of(customerDTO);
    }


    @Override
    public CustomerDTO updateCustomer(Long id, CustomerRequest customerRequest) {

        // validate ensure customer exists
        Customer existCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer",id));

        // Fetch existing profile (assuming Customer has a getProfile() method)
        CustomerProfile existingProfile = existCustomer.getProfile();

        if (existingProfile == null) {
            // Optionally create a new profile if none exists
            existingProfile = new CustomerProfile();
            existCustomer.setProfile(existingProfile);
        }

        // Copy properties from request to existing profile (ignores nulls if using ignoreProperties or custom logic)
        BeanUtils.copyProperties(customerRequest, existingProfile);

        // Save the updated profile (cascades if configured in JPA)
        customerProfileRepository.save(existingProfile);

        // Return DTO
        return customerMapper.toDto(existCustomer, existingProfile);

    }

    @Override
    public CustomerDTO updateCustomerStatus(Long id, CustomerStatusRequest customerStatusRequest) {
        // validate ensure customer exists
        Customer existCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer",id));

        existCustomer.setStatus(customerStatusRequest.getStatus());

        System.out.println("Customer status: " + customerStatusRequest.getStatus().toString());

        // save to db
        customerRepository.save(existCustomer);

        return customerMapper.toDto(existCustomer, existCustomer.getProfile());
    }


}
