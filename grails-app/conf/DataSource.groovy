dataSource {
    pooled = true
    //dbCreate = "update"
    url = "jdbc:mysql://localhost:3306/stats_info"
    driverClassName = "com.mysql.jdbc.Driver"
    username = "root"
    password = "root"
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory' // Hibernate 3
//    cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory' // Hibernate 4
    singleSession = true // configure OSIV singleSession mode
}

// environment specific settings
environments {
    development {
        dataSource {
            dbCreate = "update" // one of 'create', 'create-drop', 'update', 'validate', ''
//            url = "jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE"
            dialect = org.hibernate.dialect.MySql5InnoDbDialect
            pooled = true
//			dbCreate = "update" // one of 'create', 'create-drop', 'update', 'validate', ''
            url = "jdbc:mysql://localhost:3306/stats_info?autoreconnect=true&useUnicode=yes&characterEncoding=UTF-8"
            driverClassName = "com.mysql.jdbc.Driver"
            username = "root"
            password = "root"

            properties {
                maxActive = 50
                maxIdle = 25
                minIdle = 5
                initialSize = 5
                minEvictableIdleTimeMillis = 60000
                timeBetweenEvictionRunsMillis = 60000
                maxWait = 10000
            }
        }
    }
    test {
        dataSource {

        }
    }
    production {
        dataSource {
            dbCreate = "update"
            url = "jdbc:mysql://localhost:3306/stats_info?autoreconnect=true&useUnicode=yes&characterEncoding=UTF-8"
            properties {
               // See http://grails.org/doc/latest/guide/conf.html#dataSource for documentation
                validationQuery = "SELECT 1"
                testOnBorrow = true
                testWhileIdle = true
                testOnReturn = false
                maxActive = 50
                maxIdle = 25
                minIdle = 5
                initialSize = 5
                minEvictableIdleTimeMillis = 1800000
                timeBetweenEvictionRunsMillis = 1800000
                maxWait = 10000
            }
        }
    }
}
