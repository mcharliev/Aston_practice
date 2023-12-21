package ru.astondevs.connection;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


/**
 * Класс для управления подключениями к базе данных.
 * <p>
 * Он предоставляет методы для установления соединения с базой данных на основе свойств,
 * загруженных из файла properties.
 * </p>
 */
public class DatabaseConnectionManager {

    private final Properties properties;

    public DatabaseConnectionManager() {
        this.properties = loadProperties();
    }

    public DatabaseConnectionManager(String url, String username, String password, String driver) {
        this.properties = new Properties();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("test-application.properties")) {
            if (input != null) {
                this.properties.load(input);
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при загрузке тестовых свойств", e);
        }
        properties.setProperty("url", url);
        properties.setProperty("username", username);
        properties.setProperty("password", password);
        properties.setProperty("driver", driver);
    }

    /**
     * Загружает свойства из файла для настройки подключения к базе данных.
     *
     * @return Properties, содержащий параметры для подключения.
     * @throws RuntimeException если происходит ошибка при чтении файла свойств.
     */
    private Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new FileNotFoundException("Файл 'application.properties' не найден по данному пути");
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при загрузке свойств", e);
        }
        return props;
    }

    /**
     * Устанавливает соединение с базой данных на основе свойств, загруженных из файла.
     *
     * @return Connection - объект соединения с базой данных.
     * @throws ClassNotFoundException если драйвер базы данных не найден.
     * @throws SQLException           если происходит ошибка при установке соединения с базой данных.
     */
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        String driver = properties.getProperty("driver");
        String url = properties.getProperty("url");
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");

        Class.forName(driver);

        return DriverManager.getConnection(url, username, password);
    }

    public Properties getProperties() {
        return properties;
    }
}

