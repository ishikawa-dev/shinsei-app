package com.example.demo.validator;

import java.time.LocalDate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import com.example.demo.form.CommuteTransportForm;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, CommuteTransportForm> {

    @Override
    public boolean isValid(CommuteTransportForm commuteTransportForm, ConstraintValidatorContext context) {
        LocalDate from = commuteTransportForm.getValidFrom();
        LocalDate until = commuteTransportForm.getValidUntil();

        // 両方入力されているときのみ比較
        if (from != null && until != null && !from.isBefore(until)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("終了日は開始日より後の日付を指定してください")
                   .addPropertyNode("validUntil") // ここで validUntil に紐づける！
                   .addConstraintViolation();
            return false;
        }

        return true;
    }
}
