package ru.astondevs.service.impl;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.astondevs.aop.annotations.Loggable;
import ru.astondevs.exception.AuthenticationException;
import ru.astondevs.exception.PlayerAlreadyExistException;
import ru.astondevs.exception.PlayerNotFoundException;
import ru.astondevs.exception.ValidationException;
import ru.astondevs.model.dto.LoginResponseDTO;
import ru.astondevs.model.dto.PlayerBalanceDTO;
import ru.astondevs.model.dto.PlayerDTO;
import ru.astondevs.model.dto.RegisterPlayerDTO;
import ru.astondevs.model.entity.Player;
import ru.astondevs.model.mapper.PlayerMapper;
import ru.astondevs.repository.PlayerRepository;
import ru.astondevs.service.PlayerService;
import ru.astondevs.util.DTOValidator;
import ru.astondevs.util.JwtUtil;


import javax.validation.ConstraintViolation;
import java.util.Optional;
import java.util.Set;

/**
 * Сервис для управления данными игрока.
 * Этот сервис предоставляет методы для выполнения основных операций, таких как поиск, обновление, регистрация и вход.
 */
@Service
@Loggable
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final DTOValidator<RegisterPlayerDTO> registerPlayerValidator;
    private final JwtUtil jwtUtil;

    /**
     * Конструктор с внедрением зависимостей.
     *
     * @param playerRepository          Репозиторий игроков, тип {@link PlayerRepository}.
     * @param registerPlayerDTOValidator Валидатор данных регистрации игрока, тип {@link DTOValidator}.
     * @param jwtUtil                   Утилита для работы с JWT-токенами, тип {@link JwtUtil}.
     */
    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository, DTOValidator<RegisterPlayerDTO> registerPlayerDTOValidator, JwtUtil jwtUtil) {
        this.playerRepository = playerRepository;
        this.registerPlayerValidator = registerPlayerDTOValidator;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Находит игрока по его идентификатору.
     *
     * @param id Идентификатор игрока, тип {@link Long}.
     * @return Объект игрока, тип {@link Player}.
     * @throws PlayerNotFoundException Если игрок с заданным идентификатором не найден.
     */
    @Override
    public Player findPlayerById(Long id) {
        return playerRepository.findPlayerById(id).orElseThrow(PlayerNotFoundException::new);
    }

    /**
     * Обновляет информацию об игроке.
     *
     * @param updatedPlayer Обновленный объект игрока, тип {@link Player}.
     */
    @Override
    public void updatePlayer(Player updatedPlayer) {
        playerRepository.updatePlayer(updatedPlayer);
    }

    /**
     * Регистрирует нового игрока.
     *
     * @param registerPlayerDTO Данные для регистрации игрока, тип {@link RegisterPlayerDTO}.
     * @return Объект игрока, зарегистрированного в системе, тип {@link PlayerDTO}.
     */
    @Override
    public PlayerDTO registerNewPlayer(RegisterPlayerDTO registerPlayerDTO) {
        validate(registerPlayerDTO);
        Optional<Player> existingPlayer = playerRepository.findPlayerByUsername(registerPlayerDTO.getUsername());
        if (existingPlayer.isPresent()) {
            throw new PlayerAlreadyExistException();
        }
        Player player = new Player(registerPlayerDTO.getUsername(), registerPlayerDTO.getPassword());
        playerRepository.addPlayer(player);
        return PlayerMapper.INSTANCE.toDTO(player);
    }

    /**
     * Находит игрока по его имени пользователя.
     *
     * @param username Имя пользователя игрока, тип {@link String}.
     * @return Объект игрока, тип {@link Optional} {@link Player}.
     */
    @Override
    public Optional<Player> findPlayerByUsername(String username) {
        return playerRepository.findPlayerByUsername(username);
    }

    /**
     * Аутентифицирует игрока и генерирует JWT-токен.
     *
     * @param registerPlayerDTO Данные для аутентификации игрока, тип {@link RegisterPlayerDTO}.
     * @return Объект {@link LoginResponseDTO} с данными игрока и JWT-токеном.
     */
    @Override
    public LoginResponseDTO authenticateAndGenerateToken(RegisterPlayerDTO registerPlayerDTO) {
        Player player = authenticatePlayer(registerPlayerDTO.getUsername(),
                registerPlayerDTO.getPassword()).orElseThrow(AuthenticationException::new);
        String token = jwtUtil.generateToken(player.getUsername());
        PlayerDTO playerDTO = PlayerMapper.INSTANCE.toDTO(player);
        return new LoginResponseDTO(playerDTO, token);
    }

    /**
     * Получает информацию о балансе игрока.
     *
     * @param player Объект игрока, тип {@link Player}.
     * @return Объект {@link PlayerBalanceDTO} с информацией о балансе игрока.
     */
    @Override
    public PlayerBalanceDTO getPlayerBalanceInfo(Player player) {
        return new PlayerBalanceDTO(player.getUsername(), player.getBalance());
    }

    /**
     * Проверяет JWT-токен и возвращает игрока, если токен действителен.
     *
     * @param token JWT-токен, тип {@link String}.
     * @return Объект игрока, тип {@link Optional} {@link Player}.
     */
    @Override
    public Optional<Player> validateTokenAndGetPlayer(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return Optional.empty();
        }
        token = token.substring(7);
        try {
            String username = jwtUtil.extractUsername(token);
            if (!jwtUtil.validateToken(token, username)) {
                return Optional.empty();
            }
            return findPlayerByUsername(username);
        } catch (SignatureException | ExpiredJwtException ex) {
            return Optional.empty();
        }
    }

    /**
     * Приватный метод для аутентификации игрока по имени пользователя и паролю.
     *
     * @param username Имя пользователя для аутентификации, тип {@link String}.
     * @param password Пароль для аутентификации, тип {@link String}.
     * @return Объект игрока, если аутентификация успешна, тип {@link Optional}{@code <}{@link Player}{@code >}.
     */
    private Optional<Player> authenticatePlayer(String username, String password) {
        return playerRepository.findPlayerByUsername(username)
                .filter(player -> player.getPassword().equals(password));
    }

    /**
     * Приватный метод для валидации данных нового игрока.
     *
     * @param registerPlayerDTO Объект с данными нового игрока, тип {@link RegisterPlayerDTO}.
     * @throws ValidationException Если данные нового игрока не проходят валидацию.
     */
    private void validate(RegisterPlayerDTO registerPlayerDTO) {
        Set<ConstraintViolation<RegisterPlayerDTO>> violations = registerPlayerValidator.validate(registerPlayerDTO);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<RegisterPlayerDTO> violation : violations) {
                sb.append(violation.getMessage()).append(". ");
            }
            throw new ValidationException(sb.toString(), violations);
        }
    }
}

