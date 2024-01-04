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
import ru.astondevs.model.dto.LoginResponseDTO;
import ru.astondevs.model.dto.PlayerBalanceDTO;
import ru.astondevs.model.dto.PlayerDTO;
import ru.astondevs.model.dto.RegisterPlayerDTO;
import ru.astondevs.model.entity.Player;
import ru.astondevs.service.PlayerService;


@RestController
@RequestMapping("/players")
@Api(tags = "Управление игроками")
public class PlayerController {

    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping("/register")
    @ApiOperation(value = "Регистрация нового игрока")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Игрок успешно создан"),
            @ApiResponse(responseCode = "409", description = "Игрок с таким именем уже существует"),
            @ApiResponse(responseCode = "400", description = "Неверные входные данные (не прошли валидацию)"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<PlayerDTO> register(
            @ApiParam(value = "Детали регистрации игрока", required = true)
            @RequestBody RegisterPlayerDTO registerPlayerDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(playerService.registerNewPlayer(registerPlayerDTO));
    }

    @PostMapping("/login")
    @ApiOperation(value = "Аутентификация игрока и генерация токена")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Игрок успешно аутентифицирован"),
            @ApiResponse(responseCode = "401", description = "Учетные данные игрока неверны, или токен JWT неверен или истек"),
            @ApiResponse(responseCode = "400", description = "Неверные входные данные (не прошли валидацию)"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<LoginResponseDTO> login(
            @ApiParam(value = "Данные для входа игрока", required = true)
            @RequestBody RegisterPlayerDTO registerPlayerDTO) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(playerService.authenticateAndGenerateToken(registerPlayerDTO));
    }

    @GetMapping("/balance")
    @ApiOperation(value = "Получение баланса аутентифицированного игрока")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Баланс игрока успешно получен"),
            @ApiResponse(responseCode = "401", description = "Учетные данные игрока неверны, или токен JWT неверен или истек"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<PlayerBalanceDTO> getBalance(
            @ApiParam(value = "Аутентифицированный игрок", required = true)
            @RequestAttribute("authenticatedPlayer") Player authenticatedPlayer) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(playerService.getPlayerBalanceInfo(authenticatedPlayer));
    }
}
