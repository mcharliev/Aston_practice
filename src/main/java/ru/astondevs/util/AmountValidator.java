package ru.astondevs.util;

import org.springframework.stereotype.Component;
import ru.astondevs.model.dto.AmountDTO;


import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * Компонент для валидации объектов типа {@link AmountDTO}.
 */
@Component
public class AmountValidator implements DTOValidator<AmountDTO> {
    private final Validator validator;

    /**
     * Конструктор класса {@code AmountValidator}.
     * Создает экземпляр валидатора для валидации объектов типа {@link AmountDTO}.
     */
    public AmountValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    /**
     * Выполняет валидацию объекта {@link AmountDTO}.
     *
     * @param amountDTO объект, который нужно валидировать, тип {@link AmountDTO}.
     * @return набор нарушений ограничений валидации
     */
    public Set<ConstraintViolation<AmountDTO>> validate(AmountDTO amountDTO) {
        return validator.validate(amountDTO);
    }
}
