// Place your Spring DSL code here
import org.springframework.jdbc.core.JdbcTemplate

beans = {
    jdbcTemplate(JdbcTemplate) {
        dataSource = ref('dataSource')
    }
}
