package sogoing.backend_server.common.entity

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import java.time.LocalDateTime
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity(
    @CreatedDate @Column(name = "created_date", updatable = false) var createdDate: LocalDateTime,
    @LastModifiedDate @Column(name = "updated_date") var updatedDate: LocalDateTime? = null,
)
