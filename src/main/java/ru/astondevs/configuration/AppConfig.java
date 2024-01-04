package ru.astondevs.configuration;

import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import ru.astondevs.util.JwtUtil;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Конфигурационный класс для настройки приложения.
 */
@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "ru.astondevs")
@PropertySources({
        @PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
})
public class AppConfig {

    @Value("${database.driver}")
    private String dbDriver;

    @Value("${database.url}")
    private String dbUrl;

    @Value("${database.username}")
    private String dbUsername;

    @Value("${database.password}")
    private String dbPassword;

    @Value("${database.changeLogLog}")
    private String liquibaseChangeLog;

    @Value("${database.liquibaseSchema}")
    private String liquibaseSchema;

    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    @Value("${database.defaultSchema}")
    private String defaultSchema;

    /**
     * Создает и настраивает источник данных для базы данных.
     *
     * @return Объект источника данных, тип {@link DataSource}.
     */
    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(dbDriver);
        dataSource.setJdbcUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);
        return dataSource;
    }

    /**
     * Создает и настраивает Spring Liquibase для управления миграциями базы данных.
     *
     * @param dataSource Объект источника данных, тип {@link DataSource}.
     * @return Объект Spring Liquibase, тип {@link SpringLiquibase}.
     */
    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE SCHEMA IF NOT EXISTS migration;");
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось создать схему migration", e);
        }
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog(liquibaseChangeLog);
        liquibase.setDefaultSchema(defaultSchema);
        liquibase.setLiquibaseSchema(liquibaseSchema);
        return liquibase;
    }

    /**
     * Создает и настраивает утилиту для работы с JSON Web Tokens (JWT).
     *
     * @return Объект утилиты JWT, тип {@link JwtUtil}.
     */
    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil(jwtSecretKey);
    }

    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

}
