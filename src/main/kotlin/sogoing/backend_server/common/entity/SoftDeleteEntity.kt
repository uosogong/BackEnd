package com.uoslife.common.entity

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class SoftDeleteEntity(
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    var createdDate: LocalDateTime? = null,
    @LastModifiedDate @Column(name = "updated_at") var updatedDate: LocalDateTime? = null,
    @Column(name = "deleted_at") var deletedDate: LocalDateTime? = null,
)
