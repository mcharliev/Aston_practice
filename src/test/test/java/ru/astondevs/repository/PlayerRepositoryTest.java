package ru.astondevs.repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.astondevs.connection.DatabaseConnectionManager;
import ru.astondevs.liquibase.LiquibaseMigrationRunner;
import ru.astondevs.model.entity.Player;
import ru.astondevs.repository.PlayerRepository;
import ru.astondevs.repository.PlayerRepositoryImpl;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class PlayerRepositoryTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    private PlayerRepository repository;

    @BeforeEach
    void setUp() {
        String jdbcUrl = postgres.getJdbcUrl();
        String username = postgres.getUsername();
        String password = postgres.getPassword();
        String driver = postgres.getDriverClassName();

        DatabaseConnectionManager connectionManager = new DatabaseConnectionManager(jdbcUrl, username, password,driver);

        LiquibaseMigrationRunner migrationRunner = new LiquibaseMigrationRunner(connectionManager);
        migrationRunner.runMigrations();

        repository = new PlayerRepositoryImpl(connectionManager);
    }

    @Test
    void testAddPlayer() {
        Player player = new Player();
        player.setId(1L);
        player.setUsername("testUser");
        player.setPassword("testPassword");
        player.setBalance(new BigDecimal("100.00"));

        repository.addPlayer(player);

        assertNotNull(player.getId());
    }

    @Test
    void testFindPlayerByUsername() {
        Player player = new Player();
        player.setUsername("userByUsername");
        player.setPassword("passwordByUsername");
        player.setBalance(new BigDecimal("200.00"));

        repository.addPlayer(player);

        Optional<Player> foundPlayerOpt = repository.findPlayerByUsername(player.getUsername());
        assertTrue(foundPlayerOpt.isPresent());

        Player foundPlayer = foundPlayerOpt.get();
        assertEquals(player.getUsername(), foundPlayer.getUsername());
        assertEquals(player.getPassword(), foundPlayer.getPassword());
        assertEquals(player.getBalance(), foundPlayer.getBalance());
    }
    @Test
    void testFindNonExistentPlayerByUsername() {
        Optional<Player> notFoundPlayerOpt = repository.findPlayerByUsername("nonExistentUsername");
        assertFalse(notFoundPlayerOpt.isPresent());
    }

    @Test
    void testUpdatePlayer() {
        Player player = new Player();
        player.setUsername("testUsername");
        player.setPassword("testPassword");
        player.setBalance(new BigDecimal("300.00"));

        repository.addPlayer(player);

        Optional<Player> initialPlayerOpt = repository.findPlayerById(player.getId());
        assertTrue(initialPlayerOpt.isPresent());
        assertEquals(new BigDecimal("300.00"), initialPlayerOpt.get().getBalance());

        player.setBalance(new BigDecimal("400.00"));
        repository.updatePlayer(player);

        Optional<Player> updatedPlayerOpt = repository.findPlayerById(player.getId());
        assertTrue(updatedPlayerOpt.isPresent());
        assertEquals(new BigDecimal("400.00"), updatedPlayerOpt.get().getBalance());
    }
    @Test
    void testFindPlayerById() {
        Player player = new Player();
        player.setId(1L);
        player.setUsername("testUser");
        player.setPassword("testPassword");
        player.setBalance(new BigDecimal("100.00"));

        repository.addPlayer(player);

        Optional<Player> foundPlayerOpt = repository.findPlayerById(player.getId());
        assertTrue(foundPlayerOpt.isPresent());

        Player foundPlayer = foundPlayerOpt.get();
        assertEquals(player.getUsername(), foundPlayer.getUsername());
        assertEquals(player.getPassword(), foundPlayer.getPassword());
        assertEquals(player.getBalance(), foundPlayer.getBalance());
    }
}
