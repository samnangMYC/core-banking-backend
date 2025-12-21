package com.trendy.cbs.service.validation;

import com.trendy.cbs.entity.Customer;
import com.trendy.cbs.entity.IdentityDoc;
import com.trendy.cbs.enums.CustomerStatus;
import com.trendy.cbs.enums.CustomerVerification;
import com.trendy.cbs.enums.DocStatus;
import com.trendy.cbs.enums.ErrorCode;
import com.trendy.cbs.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class CustomerValidationService {

    /**
     * Performs complete validation for a customer and their identity document.
     *
     * <p>This method acts as a single entry point for all customer
     * eligibility checks and delegates validation to specialized
     * internal methods.</p>
     *
     * @param customer the customer entity to be validated
     * @param identityDoc the customer's identity document (nullable)
     *
     * @throws BusinessException if any validation rule fails
     */
    public void validateCustomer(Customer customer, IdentityDoc identityDoc) {
        validateCustomerVerification(customer);
        validateCustomerStatus(customer);
        validateIdentityDocument(identityDoc);
    }

    /**
     * Validates the customer's verification status.
     *
     * @param customer the customer entity to be validated
     *
     * @throws BusinessException if the customer is not verified
     */
    public void validateCustomerVerification(Customer customer) {
        if (!CustomerVerification.VERIFIED.equals(customer.getVerification())) {
            throw new BusinessException(
                    "Customer is not verified",
                    ErrorCode.CUSTOMER_NOT_VERIFIED,
                    HttpStatus.NOT_FOUND.value()
            );
        }
    }

    /**
     * Validates the customer's status.
     *
     * @param customer the customer entity to be validated
     *
     * @throws BusinessException if the customer is not active
     */
    public void validateCustomerStatus(Customer customer) {
        if (!CustomerStatus.ACTIVE.equals(customer.getStatus())) {
            throw new BusinessException(
                    "Customer is not active",
                    ErrorCode.CUSTOMER_NOT_ACTIVE,
                    HttpStatus.CONFLICT.value()
            );
        }
    }

    /**
     * Validates the customer's identity document status.
     *
     * <p>If the identity document is {@code null}, this validation
     * is skipped.</p>
     *
     * @param identityDoc the identity document (nullable)
     *
     * @throws BusinessException if the document exists but is not verified
     */
    public void validateIdentityDocument(IdentityDoc identityDoc) {
        if (identityDoc != null && !DocStatus.VERIFIED.equals(identityDoc.getDocStatus())) {
            throw new BusinessException(
                    "Identity document is not verified or was rejected",
                    ErrorCode.IDENTITY_NOT_COMPLETED,
                    HttpStatus.BAD_REQUEST.value()
            );
        }
    }
}