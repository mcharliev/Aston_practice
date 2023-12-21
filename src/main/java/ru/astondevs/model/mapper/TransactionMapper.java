package ru.astondevs.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.astondevs.model.dto.TransactionDTO;
import ru.astondevs.model.entity.Transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Интерфейс маппера для конвертации объектов между {@link Transaction} и {@link TransactionDTO}.
 * Использует библиотеку MapStruct для автоматического создания реализации на основе аннотаций.
 */
@Mapper
public interface TransactionMapper {

    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    /**
     * Конвертирует сущность {@link Transaction} в DTO {@link TransactionDTO}.
     *
     * @param entity сущность транзакции
     * @return объект DTO {@link TransactionDTO}
     */
    @Mapping(target = "localDateTime", source = "entity.localDateTime", qualifiedByName = "formatLocalDateTime")
    TransactionDTO toDTO(Transaction entity);

    /**
     * Преобразует {@link LocalDateTime} в строку с заданным форматом "yyyy-MM-dd HH:mm:ss".
     *
     * @param localDateTime объект LocalDateTime для форматирования
     * @return отформатированная дата и время в виде строки {@link String}
     */
    @Named("formatLocalDateTime")
    default String formatLocalDateTime(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(formatter);
    }
}
