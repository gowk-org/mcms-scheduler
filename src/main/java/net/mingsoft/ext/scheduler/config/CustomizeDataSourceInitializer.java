package net.mingsoft.ext.scheduler.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import javax.sql.DataSource;

/**
 * <p>CustomizeDataSourceInitializer class.</p>
 *
 * @author user1
 * @version $Id: $Id
 */
@Configuration
public class CustomizeDataSourceInitializer {

    @Value("classpath:sql/tables_mysql_innodb.sql")
    private Resource functionScript;

    /**
     * <p>dataSourceInitializer.</p>
     *
     * @param dataSource a {@link javax.sql.DataSource} object.
     * @return a {@link org.springframework.jdbc.datasource.init.DataSourceInitializer} object.
     */
    @Bean
    public DataSourceInitializer dataSourceInitializer(final DataSource dataSource) {
        final DataSourceInitializer initializer = new DataSourceInitializer();
        // 设置数据源
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(databasePopulator());
        return initializer;
    }

    private DatabasePopulator databasePopulator() {
        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        //populator.addScripts(functionScript);
        populator.setSeparator("$$");
        return populator;
    }
}
