package org.solteh.validator;

import org.solteh.entity.Product;
import org.solteh.form.ProductForm;
import org.solteh.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ProductFormValidator implements Validator {
    private final ProductRepository productRepository;

    @Autowired
    public ProductFormValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // This validator only checks for the ProductForm.
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == Product.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProductForm productForm = (ProductForm) target;
// Check the fields of ProductForm.
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "code", "NotEmpty.productForm.code");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty.productForm.name");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "price", "NotEmpty.productForm.price");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "NotEmpty.productForm.description");

        String code = productForm.getCode();
        if (code != null && code.length() > 0) {
            if (code.matches("\\s+")) {
                errors.rejectValue("code", "Pattern.productForm.code");
            } else if (productForm.isNewProduct()) {
                Product product = productRepository.findByCode(code);
                if (product != null) {
                    errors.rejectValue("code", "Duplicate.productForm.code");
                }
            }
        }
    }

}
