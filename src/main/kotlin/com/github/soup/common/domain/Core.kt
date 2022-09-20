package com.github.soup.common.domain

import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

@MappedSuperclass
@EntityListeners(value = [AuditingEntityListener::class])
open class Core {
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(columnDefinition = "CHAR(36)")
	var id: String? = null

	@CreatedDate
	@Column(updatable = false)
	var createdAt: LocalDateTime? = null

	@LastModifiedDate
	var updatedAt: LocalDateTime? = null
}
