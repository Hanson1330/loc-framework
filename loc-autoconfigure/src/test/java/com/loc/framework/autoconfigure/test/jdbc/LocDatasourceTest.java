package com.loc.framework.autoconfigure.test.jdbc;

import static org.assertj.core.api.Assertions.assertThat;

import com.loc.framework.autoconfigure.jdbc.LocDataSourceAutoConfiguration;
import com.zaxxer.hikari.HikariDataSource;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.sql.DataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.problem.Problem;

/**
 * Created on 2017/12/26.
 */
@RunWith(SpringRunner.class)
@WebMvcTest
@TestPropertySource(properties = {
    "loc.dataSource.first.username = root",
    "loc.dataSource.first.password = ",
    "loc.dataSource.first.jdbcUrl = jdbc:log4jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false",
    "loc.dataSource.first.jdbcPool.autoCommit = false",
    "loc.dataSource.first.jdbcPool.maximumPoolSize = 30",
    "log4jdbc.debug.stack.prefix = com.loc.framework.autoconfigure.test.springmvc",
    "log4jdbc.sqltiming.warn.threshold = 300",
    "log4jdbc.sqltiming.error.threshold = 2000",
})
@DirtiesContext
public class LocDatasourceTest {

  @Autowired
  private DataSource dataSource;


  @Autowired
  private Environment environment;


  @Test
  public void testDataSource() throws Exception {
    assertThat(dataSource).isNotNull();
    HikariDataSource hikariDataSource = dataSource.unwrap(HikariDataSource.class);
    assertThat(hikariDataSource.getUsername()).isEqualTo("root");
    assertThat(hikariDataSource.getPassword()).isEqualTo("");
    assertThat(hikariDataSource.getJdbcUrl()).isEqualTo(
        "jdbc:log4jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false");

    assertThat(hikariDataSource.isAutoCommit()).isEqualTo(false);
    assertThat(hikariDataSource.getConnectionTimeout()).isEqualTo(30000);
    assertThat(hikariDataSource.getIdleTimeout()).isEqualTo(600000);
    assertThat(hikariDataSource.getMaxLifetime()).isEqualTo(1800000);
    assertThat(hikariDataSource.getMaximumPoolSize()).isEqualTo(30);
    assertThat(hikariDataSource.getMinimumIdle()).isEqualTo(10);
    assertThat(hikariDataSource.getInitializationFailTimeout()).isEqualTo(1);
    assertThat(hikariDataSource.isIsolateInternalQueries()).isEqualTo(false);
    assertThat(hikariDataSource.isReadOnly()).isEqualTo(false);
    assertThat(hikariDataSource.isRegisterMbeans()).isEqualTo(false);
    assertThat(hikariDataSource.getDriverClassName()).isEqualTo(null);
    assertThat(hikariDataSource.getValidationTimeout()).isEqualTo(5000);
    assertThat(hikariDataSource.getLeakDetectionThreshold()).isEqualTo(0);
  }

  @Test
  public void testLog4jdbc() {
    assertThat(environment).isNotNull();

    assertThat(this.environment.getProperty("log4jdbc.debug.stack.prefix"))
        .isEqualTo("com.loc.framework.autoconfigure.test.springmvc");
    assertThat(this.environment.getProperty("log4jdbc.sqltiming.warn.threshold")).isEqualTo("300");
    assertThat(this.environment.getProperty("log4jdbc.sqltiming.error.threshold"))
        .isEqualTo("2000");
  }

  @MinimalWebConfiguration
  @RestController
  @Validated
  public static class JdbcController {


    @GetMapping(value = "/jdbc/get")
    public Problem jdbcGet() {
      return Problem.builder().build();
    }
  }

  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  @Configuration
  @Import({
      ServletWebServerFactoryAutoConfiguration.class,
      JacksonAutoConfiguration.class,
      LocDataSourceAutoConfiguration.class
  })
  protected @interface MinimalWebConfiguration {

  }
}
