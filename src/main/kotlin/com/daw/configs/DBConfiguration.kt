package com.daw.configs

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.config.JdbiConfig
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.DriverManagerDataSource
import javax.sql.DataSource

@Configuration
class DBConfiguration : JdbiConfig<DBConfiguration>{

        @Bean
        @ConfigurationProperties(prefix = "spring.datasource")
        fun driverManagerDataSource(): DataSource {
            return DriverManagerDataSource()
        }

        @Bean
        fun dataSourceTransactionManager(dataSource: DataSource?): DataSourceTransactionManager {
            val dataSourceTransactionManager = DataSourceTransactionManager()
            dataSourceTransactionManager.dataSource = dataSource
            return dataSourceTransactionManager
        }

        @Bean
        fun jdbi(dataSource: DataSource?): Jdbi {
            return Jdbi.create(dataSource)
                    .installPlugins()
        }

        override fun createCopy(): DBConfiguration {
            return this
        }
}