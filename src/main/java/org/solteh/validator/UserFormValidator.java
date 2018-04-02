package org.solteh.validator;

import org.solteh.form.UserForm;
import org.springframework.stereotype.Component;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserFormValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return UserForm.class == clazz;
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty.customerForm.name");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty.customerForm.email");
    }
}
