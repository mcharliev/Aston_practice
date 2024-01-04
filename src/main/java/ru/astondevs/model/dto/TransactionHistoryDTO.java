package ru.astondevs.model.dto;

import java.util.List;

/**
 * Класс, представляющий историю транзакций.
 */
public class TransactionHistoryDTO {

    private String message;
    private List<TransactionDTO> transactionDTOList;

    /**
     * Получает сообщение о статусе истории транзакций.
     *
     * @return Сообщение о статусе, тип {@link String}.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Устанавливает сообщение о статусе истории транзакций.
     *
     * @param message Сообщение о статусе, тип {@link String}.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Получает список транзакций в истории.
     *
     * @return Список транзакций, тип {@link List}<{@link TransactionDTO}>.
     */
    public List<TransactionDTO> getTransactionDTOList() {
        return transactionDTOList;
    }

    /**
     * Устанавливает список транзакций в истории.
     *
     * @param transactionDTOList Список транзакций, тип {@link List}<{@link TransactionDTO}>.
     */
    public void setTransactionDTOList(List<TransactionDTO> transactionDTOList) {
        this.transactionDTOList = transactionDTOList;
    }
}
