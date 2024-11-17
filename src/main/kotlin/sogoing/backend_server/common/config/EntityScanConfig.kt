package sogoing.backend_server.common.config

import jakarta.annotation.PostConstruct
import jakarta.persistence.EntityManagerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EntityScan("sogoing.backend_server.app")
@EnableJpaRepositories("sogoing.backend_server.app")
class EntityScanConfig {

    @Autowired
    private lateinit var entityManagerFactory: EntityManagerFactory

    @PostConstruct
    fun printEntities() {
        val metamodel = entityManagerFactory.metamodel
        val managedTypes = metamodel.managedTypes

        println("Entities that are being managed by JPA:")
        for (entityType in managedTypes) {
            println(entityType.javaType.name)
        }
    }
}
