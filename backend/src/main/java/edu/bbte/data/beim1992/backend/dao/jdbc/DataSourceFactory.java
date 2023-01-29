package edu.bbte.data.beim1992.backend.dao.jdbc;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("jdbc")
public class DataSourceFactory {
    @Value("${jdbc.url:localhost}")
    private String url;
    @Value("${jdbc.poolSize:4}")
    private Integer poolSize;

    @Bean
    public DataSource getDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setMaximumPoolSize(poolSize);

        return dataSource;
    }
}
