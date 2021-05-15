package com.github.xsi640.core

import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.*
import javax.persistence.*

@EntityListeners(AuditingEntityListener::class)
@MappedSuperclass
abstract class AuditableEntity(
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", nullable = true)
    var createDate: Date? = null,
    @CreatedBy
    @Column(name = "creator", nullable = true)
    var creator: String = "",
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_mod_date", nullable = true)
    var lastModDate: Date? = null,
    @LastModifiedBy
    @Column(name = "last_modifier", nullable = true)
    var lastModifier: String = ""
)