package ru.astondevs.model.enums;

/**
 * Перечисление, представляющее типы действий игрока.
 */
public enum PlayerActionType {
    REGISTRATION("REGISTRATION"),
    AUTHENTICATION("AUTHENTICATION"),
    BALANCE_CHECK("BALANCE_CHECK"),
    DEBIT_TRANSACTION_SUCCESS("DEBIT_TRANSACTION_SUCCESS"),
    CREDIT_TRANSACTION_SUCCESS("CREDIT_TRANSACTION_SUCCESS"),
    VIEW_TRANSACTION_HISTORY("VIEW_TRANSACTION_HISTORY");

    private final String action;

    /**
     * Создает экземпляр перечисления с указанным типом действия.
     *
     * @param action Тип действия, тип {@link String}.
     */
    PlayerActionType(String action) {
        this.action = action;
    }

    /**
     * Получает тип действия.
     *
     * @return Тип действия, тип {@link String}.
     */
    public String getAction() {
        return action;
    }

    /**
     * Переопределение метода toString() для получения типа действия в виде строки.
     *
     * @return Тип действия в виде строки.
     */
    @Override
    public String toString() {
        return action;
    }
}

