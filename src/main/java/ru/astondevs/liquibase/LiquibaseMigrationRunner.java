package ru.astondevs.liquibase;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.astondevs.aop.annotations.Loggable;
import ru.astondevs.connection.DatabaseConnectionManager;
import ru.astondevs.exception.MigrationException;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;


/**
 * Класс для запуска миграций базы данных с использованием Liquibase.
 * <p>
 * Предоставляет функциональность для выполнения миграций на основе файла changelog.xml в директории liquibase.
 * </p>
 */
@Loggable
public class LiquibaseMigrationRunner {

    // Логгер для записи информации о выполнении миграций
    private final Logger log = LoggerFactory.getLogger(LiquibaseMigrationRunner.class);

    // Менеджер соединений с базой данных
    private final DatabaseConnectionManager connectionManager;
    private final Properties properties;
    public LiquibaseMigrationRunner(DatabaseConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.properties = connectionManager.getProperties();
    }

    /**
     * Запускает миграции на базе данных с использованием Liquibase.
     * <p>
     * Метод создает соединение с базой данных, устанавливает соответствующие параметры для Liquibase и
     * выполняет миграции, определенные в файле changelog.xml.
     * </p>
     *
     * @throws MigrationException если произошла ошибка при выполнении миграции.
     */
    public void runMigrations() {
        String changeLog = properties.getProperty("change.log");
        String liquibaseSchema = properties.getProperty("liquibase.schema");
        try (Connection connection = connectionManager.getConnection();
             Statement stmt = connection.createStatement()
        ) {
            //создаю схему migration для служебных таблиц liquibase
            stmt.execute("CREATE SCHEMA IF NOT EXISTS migration;");
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            //задаю схему в которой будут хранится служебные таблицы liquibase
            database.setLiquibaseSchemaName(liquibaseSchema);
            Liquibase liquibase = new Liquibase(changeLog, new ClassLoaderResourceAccessor(), database);
            liquibase.update();
        } catch (Exception e) {
            log.error("Ошибка при выполнении миграций", e);
            throw new MigrationException("Ошибка: миграции не выполнены!");
        }
    }
}