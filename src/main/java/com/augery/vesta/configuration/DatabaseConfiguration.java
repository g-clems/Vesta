package com.augery.vesta.configuration;

import jakarta.persistence.EntityManager;
import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.TransactionManager;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
public class DatabaseConfiguration {

    @Bean
    TransactionManager transactionManager() {
        return new JpaTransactionManager();
    }

    @Bean
    DataSource dataSource(BotConfiguration.ConfigModel config) {
        BasicDataSource basicDataSource = new BasicDataSource();

        basicDataSource.setDriverClassName("org.postgresql.Driver");
        basicDataSource.setUrl("jdbc:" + config.dbHost() + ":" + config.dbPort() + "/" + config.dbName());
        basicDataSource.setUsername(config.dbUser());
        basicDataSource.setPassword(config.dbPassword());

        return basicDataSource;
    }

    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();

        factoryBean.setDataSource(dataSource);
        factoryBean.setJpaDialect(new HibernateJpaDialect());
        factoryBean.setPersistenceProvider(new HibernatePersistenceProvider());
        factoryBean.setPackagesToScan("com.augery.vesta", "com.augery.vesta.*");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabasePlatform("org.hibernate.dialect.PostgreSQLDialect");
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setShowSql(true);

        factoryBean.setJpaVendorAdapter(vendorAdapter);

        return factoryBean;
    }

    @Bean
    EntityManager entityManager(LocalContainerEntityManagerFactoryBean factoryBean) {
        return Objects.requireNonNull(factoryBean.getObject()).createEntityManager();
    }
}
