package com.common.commonmodule.ValidService;

import com.common.commonmodule.Valid.ValidPhone;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {

    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$");

    @Override
    public void initialize(ValidPhone constraintAnnotation) {

    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("手机号不能为空").addConstraintViolation();
            return false;
        }
        String phoneNumberStr = phoneNumber;
        return PHONE_NUMBER_PATTERN.matcher(phoneNumberStr).matches();
    }
}
