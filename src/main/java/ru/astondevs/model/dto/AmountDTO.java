package ru.astondevs.model.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Класс, представляющий собой объект передачи данных (DTO) для суммы.
 */
public class AmountDTO {
    @NotNull(message = "Сумма должна быть введена")
    @DecimalMin(value = "0.0", inclusive = false, message = "Сумма должна быть больше 0")
    @ApiModelProperty(notes = "Сумма транзакции", example = "100")
    private BigDecimal amount;

    /**
     * Получить значение суммы.
     * @return сумма в виде объекта {@link BigDecimal}
     */
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
