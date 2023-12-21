package ru.astondevs.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.astondevs.exception.NotEnoughMoneyException;
import ru.astondevs.model.entity.Player;
import ru.astondevs.model.entity.Transaction;
import ru.astondevs.repository.TransactionRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;


public class TransactionServiceTest {

    private TransactionRepository transactionRepository;
    private PlayerService playerService;
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionRepository = Mockito.mock(TransactionRepository.class);
        playerService = Mockito.mock(PlayerServiceImpl.class);
        transactionService = new TransactionServiceImpl(transactionRepository, playerService);
    }

    @Test
    public void testAddDebitTransaction() {
        Player player = new Player();
        player.setUsername("testPlayer");
        player.setBalance(new BigDecimal("1000"));
        Mockito.when(playerService.findPlayerById(player.getId())).thenReturn(player);
        transactionService.addDebitTransaction(player, new BigDecimal("500.00"));
        Player updatedPlayer = playerService.findPlayerById(player.getId());
        assertEquals(new BigDecimal("500.00"), updatedPlayer.getBalance());
    }

    @Test
    public void testAddCreditTransaction() {
        Player player = new Player();
        player.setUsername("testPlayer");
        player.setBalance(new BigDecimal("1000"));
        Mockito.when(playerService.findPlayerById(player.getId())).thenReturn(player);
        transactionService.addCreditTransaction(player, new BigDecimal("500.00"));
        Player updatedPlayer = playerService.findPlayerById(player.getId());
        assertEquals(new BigDecimal("1500.00"), updatedPlayer.getBalance());
    }

    @Test
    public void testViewTransactionHistory() {
        Player player = new Player();
        player.setUsername("testPlayer");
        player.setBalance(new BigDecimal("1000"));
        List<Transaction> transactionsList = Arrays.asList(new Transaction(), new Transaction());
        Mockito.when(transactionRepository.getAllTransactionsByPlayerId(player.getId())).thenReturn(transactionsList);
        List<Transaction> transactions = transactionRepository.getAllTransactionsByPlayerId(player.getId());
        assertEquals(2, transactions.size());
    }

    @Test
    public void testAddDebitTransactionThrowsException() {
        Player player = new Player();
        player.setUsername("testPlayer");
        player.setBalance(new BigDecimal("1000"));
        assertThrows(NotEnoughMoneyException.class, () -> {
            transactionService.addDebitTransaction(player, new BigDecimal("1500.00"));
        });
    }

    @Test
    public void testAddDebitTransactionSavesTransaction() {
        Player player = new Player();
        player.setUsername("testPlayer");
        player.setBalance(new BigDecimal("1000"));
        Transaction transaction = new Transaction();
        Mockito.when(transactionRepository.addTransaction(any(Transaction.class), eq(player.getId())))
                .thenReturn(transaction);
        Transaction savedTransaction = transactionService.addDebitTransaction(player, new BigDecimal("500.00"));
        assertEquals(transaction, savedTransaction);
    }
}
