package seongju.remaapi.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@MapperScan(
        basePackages = "seongju.remaapi.dao"
)
public class MybatisDbConfig {
    @Bean(name="oracleDatasource")
    @ConfigurationProperties(prefix = "spring.datasource.db")
    public DataSource oracleDatasource(){
        return DataSourceBuilder.create()
                .type(HikariDataSource.class).build();
    }
    @Bean(name="oracleSqlSessionFactory")
    public SqlSessionFactory oracleOneSqlSessionFactory(
            @Qualifier(value = "oracleDatasource")
                    DataSource oracleDatasource
    )throws Exception{
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setVfs(SpringBootVFS.class);
        bean.setTypeAliasesPackage("seongju.remaapi.vo");
        bean.setDataSource(oracleDatasource);
        PathMatchingResourcePatternResolver resolver =
                new PathMatchingResourcePatternResolver();
        //oracle mapper 설정
        bean.setMapperLocations(resolver.getResources(
                "classpath:mybatis/mapper/*.xml"));
        return bean.getObject();
    }

    @Bean(name="oracleSqlSessionTemplate")
    public SqlSessionTemplate oracleOneSqlSessionTemplate(
            @Qualifier(value = "oracleSqlSessionFactory")
                    SqlSessionFactory oracleSqlSessionFactory
    ) throws Exception{
        return new SqlSessionTemplate(oracleSqlSessionFactory);
    }

}

