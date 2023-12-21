package ru.astondevs.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import ru.astondevs.model.dto.AmountDTO;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

/**
 * Сервис для обработки HTTP-запросов и извлечения суммы из запроса.
 * Этот сервис предоставляет метод для извлечения суммы из HTTP-запроса и валидации этой суммы.
 */
public class RequestService {

    private final ObjectMapper mapper = new ObjectMapper();
    private final Validator validator;

    /**
     * Создает экземпляр класса RequestService и инициализирует валидатор.
     */
    public RequestService() {
        this.validator = initValidator();
    }

    /**
     * Извлекает сумму из HTTP-запроса и выполняет её валидацию.
     *
     * @param req  объект HttpServletRequest, содержащий HTTP-запрос
     * @param resp объект HttpServletResponse, используется для отправки HTTP-ответа
     * @return {@link Optional} объект с суммой, если сумма корректна, иначе пустой {@link Optional}
     * @throws IOException если возникла ошибка при чтении данных из запроса
     */
    public Optional<BigDecimal> getAmountFromRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            AmountDTO amountDTO = mapper.readValue(req.getReader(), AmountDTO.class);
            Set<ConstraintViolation<AmountDTO>> violations = validator.validate(amountDTO);
            if (!violations.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\": \"Некорректное значение суммы\"}");
                return Optional.empty();
            }
            return Optional.of(amountDTO.getAmount());
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Ошибка чтения данных\"}");
            return Optional.empty();
        }
    }

    /**
     * Инициализирует валидатор для проверки суммы.
     *
     * @return объект валидатора для проверки суммы
     */
    private Validator initValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        return factory.getValidator();
    }
}
