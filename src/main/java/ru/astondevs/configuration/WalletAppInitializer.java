package ru.astondevs.configuration;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import ru.astondevs.aop.annotations.Loggable;
import ru.astondevs.connection.DatabaseConnectionManager;
import ru.astondevs.liquibase.LiquibaseMigrationRunner;

/**
 * Класс реализует интерфейс {@code ServletContextListener}, что позволяет
 * выполнять определенные действия при инициализации и уничтожении контекста сервлета.
 * При инициализации производится подключение к базе данных и запускаются миграции Liquibase.
 * @see ServletContextListener
 */
@Loggable
public class WalletAppInitializer implements ServletContextListener {


    /**
     * Метод, вызывающийся при инициализации контекста сервлета.
     * В данном методе создается менеджер подключений к базе данных и
     * запускаются миграции с использованием Liquibase.
     * @param sce информация о событии инициализации контекста
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        DatabaseConnectionManager connectionManager = new DatabaseConnectionManager();
        LiquibaseMigrationRunner migrationRunner = new LiquibaseMigrationRunner(connectionManager);
        migrationRunner.runMigrations();
    }
}
