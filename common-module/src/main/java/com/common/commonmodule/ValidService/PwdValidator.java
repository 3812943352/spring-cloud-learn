package com.common.commonmodule.ValidService;

import com.common.commonmodule.Valid.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PwdValidator implements ConstraintValidator<ValidPassword, String> {

    private static final Pattern PASSWORD_COMPLEXITY_PATTERN = Pattern.compile(
            "^.{6,10}$"
    );

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            // 设置自定义的错误信息
            context.disableDefaultConstraintViolation(); // 禁用默认的错误信息
            context.buildConstraintViolationWithTemplate("密码不能为空").addConstraintViolation();
            return false;
        }
        return PASSWORD_COMPLEXITY_PATTERN.matcher(password).matches();
    }

}
