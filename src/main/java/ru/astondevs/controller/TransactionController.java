package ru.astondevs.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.astondevs.model.dto.AmountDTO;
import ru.astondevs.model.dto.TransactionDTO;
import ru.astondevs.model.dto.TransactionHistoryDTO;
import ru.astondevs.model.entity.Player;
import ru.astondevs.service.TransactionService;


@RestController
@RequestMapping("/transactions")
@Api(tags = "Управление транзакциями")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/credit")
    @ApiOperation(value = "Добавление кредитной транзакции для аутентифицированного игрока")
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "201", description = "Кредитная транзакция успешно создана"),
            @ApiResponse(responseCode  = "400", description = "Неверные входные данные (не прошли валидацию)"),
            @ApiResponse(responseCode  = "401", description = "Учетные данные игрока неверны, или токен JWT неверен или истек"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<TransactionDTO> addCreditTransaction(
            @ApiParam(value = "Аутентифицированный игрок", required = true)
            @RequestAttribute("authenticatedPlayer") Player authenticatedPlayer,
            @ApiParam(value = "Сумма для транзакции", required = true)
            @RequestBody AmountDTO amountDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transactionService.addCreditTransaction(authenticatedPlayer, amountDTO));
    }

    @PostMapping("/debit")
    @ApiOperation(value = "Добавление дебетовой транзакции для аутентифицированного игрока")
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "201", description = "Дебетовая транзакция успешно создана"),
            @ApiResponse(responseCode  = "400", description = "Неверные входные данные (не прошли валидацию)"),
            @ApiResponse(responseCode  = "401", description = "Учетные данные игрока неверны, или токен JWT неверен или истек"),
            @ApiResponse(responseCode  = "409", description = "Недостаточно средств для осуществления транзакции"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<TransactionDTO> addDebitTransaction(
            @ApiParam(value = "Аутентифицированный игрок", required = true)
            @RequestAttribute("authenticatedPlayer") Player authenticatedPlayer,
            @ApiParam(value = "Сумма для транзакции", required = true)
            @RequestBody AmountDTO amountDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transactionService.addDebitTransaction(authenticatedPlayer, amountDTO));
    }

    @GetMapping("/history")
    @ApiOperation(value = "Просмотр истории транзакций аутентифицированного игрока")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "История транзакций успешно получена"),
            @ApiResponse(responseCode  = "401", description = "Учетные данные игрока неверны, или токен JWT неверен или истек"),
            @ApiResponse(responseCode = "404", description = "У игрока нету истории транзакций"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<TransactionHistoryDTO> viewTransactionHistory(
            @ApiParam(value = "Аутентифицированный игрок", required = true)
            @RequestAttribute("authenticatedPlayer") Player authenticatedPlayer) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(transactionService.viewTransactionHistory(authenticatedPlayer));
    }

}
