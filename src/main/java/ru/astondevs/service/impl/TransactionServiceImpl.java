package ru.astondevs.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.astondevs.aop.annotations.Loggable;
import ru.astondevs.exception.NoTransactionsFoundException;
import ru.astondevs.exception.NotEnoughMoneyException;
import ru.astondevs.exception.ValidationException;
import ru.astondevs.model.dto.AmountDTO;
import ru.astondevs.model.dto.TransactionDTO;
import ru.astondevs.model.dto.TransactionHistoryDTO;
import ru.astondevs.model.entity.Player;
import ru.astondevs.model.entity.Transaction;
import ru.astondevs.model.enums.TransactionType;
import ru.astondevs.model.mapper.TransactionMapper;
import ru.astondevs.repository.TransactionRepository;
import ru.astondevs.service.PlayerService;
import ru.astondevs.service.TransactionService;
import ru.astondevs.util.DTOValidator;


import javax.validation.ConstraintViolation;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Сервис для обработки транзакций.
 * Этот сервис предоставляет методы для добавления дебетовых и кредитных транзакций,
 * а также для просмотра истории транзакций игрока.
 */
@Service
@Loggable
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    private final PlayerService playerService;
    private final DTOValidator<AmountDTO> amountValidator;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  PlayerService playerService,
                                  DTOValidator<AmountDTO> amountValidator) {
        this.transactionRepository = transactionRepository;
        this.playerService = playerService;
        this.amountValidator = amountValidator;
    }

    /**
     * Добавляет дебетовую транзакцию для игрока.
     *
     * @param player    Объект игрока, тип {@link Player}.
     * @param amountDTO Объект с информацией о сумме транзакции, тип {@link AmountDTO}.
     * @return Объект с информацией о созданной транзакции, тип {@link TransactionDTO}.
     */
    public TransactionDTO addDebitTransaction(Player player, AmountDTO amountDTO) {
        validateAmount(amountDTO);
        checkSufficientBalance(player, amountDTO.getAmount());
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setType(TransactionType.DEBIT);
        transaction.setAmount(amountDTO.getAmount());
        transaction.setLocalDateTime(LocalDateTime.now());
        transactionRepository.addTransaction(transaction, player.getId());
        BigDecimal newBalance = player.getBalance().subtract(amountDTO.getAmount());
        player.setBalance(newBalance);
        player.setTransaction(transaction);
        playerService.updatePlayer(player);
        return TransactionMapper.INSTANCE.toDTO(transaction);
    }

    /**
     * Добавляет кредитную транзакцию для игрока.
     *
     * @param player    Объект игрока, тип {@link Player}.
     * @param amountDTO Объект с информацией о сумме транзакции, тип {@link AmountDTO}.
     * @return Объект с информацией о созданной транзакции, тип {@link TransactionDTO}.
     */
    public TransactionDTO addCreditTransaction(Player player, AmountDTO amountDTO) {
        validateAmount(amountDTO);
        Transaction transaction = new Transaction();
        transaction.setType(TransactionType.CREDIT);
        transaction.setAmount(amountDTO.getAmount());
        transaction.setLocalDateTime(LocalDateTime.now());
        transactionRepository.addTransaction(transaction, player.getId());
        BigDecimal newBalance = player.getBalance().add(amountDTO.getAmount());
        player.setBalance(newBalance);
        player.setTransaction(transaction);
        playerService.updatePlayer(player);
        return TransactionMapper.INSTANCE.toDTO(transaction);
    }

    /**
     * Просматривает историю транзакций игрока.
     *
     * @param player Объект игрока, тип {@link Player}.
     * @return Объект с информацией об истории транзакций игрока, тип {@link TransactionHistoryDTO}.
     * @throws NoTransactionsFoundException Если не найдено ни одной транзакции для игрока.
     */
    public TransactionHistoryDTO viewTransactionHistory(Player player) {
        List<Transaction> allTransactionsByPlayerId
                = transactionRepository.getAllTransactionsByPlayerId(player.getId());
        if (allTransactionsByPlayerId.isEmpty()) {
            throw new NoTransactionsFoundException();
        }
        List<TransactionDTO> transactionDTOList = allTransactionsByPlayerId.stream()
                .map(TransactionMapper.INSTANCE::toDTO)
                .toList();
        TransactionHistoryDTO transactionHistoryDTO = new TransactionHistoryDTO();
        transactionHistoryDTO.setMessage("История транзакций игрока " + player.getUsername());
        transactionHistoryDTO.setTransactionDTOList(transactionDTOList);
        return transactionHistoryDTO;
    }

    /**
     * Приватный метод для валидации суммы транзакции.
     *
     * @param amountDTO Объект с информацией о сумме транзакции, тип {@link AmountDTO}.
     * @throws ValidationException Если сумма транзакции не проходит валидацию.
     */
    private void validateAmount(AmountDTO amountDTO) {
        Set<ConstraintViolation<AmountDTO>> violations = amountValidator.validate(amountDTO);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<AmountDTO> violation : violations) {
                sb.append(violation.getMessage()).append(". ");
            }
            throw new ValidationException(sb.toString(), violations);
        }
    }

    /**
     * Приватный метод для проверки достаточности средств на счете игрока.
     *
     * @param player Объект игрока, тип {@link Player}.
     * @param amount Сумма транзакции, тип {@link BigDecimal}.
     * @throws NotEnoughMoneyException Если у игрока недостаточно средств для проведения транзакции.
     */
    private void checkSufficientBalance(Player player, BigDecimal amount) {
        if (amount.compareTo(player.getBalance()) > 0) {
            throw new NotEnoughMoneyException();
        }
    }
}









