package dev.sd.aisqldemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Demo application entry point.
 * Exposes the ai-sql starter on the configured port and persists
 * conversations to a PostgreSQL database.
 */
@SpringBootApplication
public class AiSqlDemoApplication {

    /**
     * Boots the Spring application context.
     *
     * @param args standard CLI arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(AiSqlDemoApplication.class, args);
    }
}
