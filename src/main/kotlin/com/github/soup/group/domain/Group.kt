package com.github.soup.group.domain

import com.github.soup.common.domain.Core
import com.github.soup.file.domain.File
import com.github.soup.group.infra.http.request.UpdateGroupRequest
import com.github.soup.group.infra.http.response.GroupResponse
import com.github.soup.member.domain.Member
import jakarta.persistence.*
import kr.soupio.soup.group.entities.GroupRecruitmentEnum
import org.hibernate.annotations.ColumnDefault
import java.time.LocalDate

@Entity
@Table(name = "`group`")
class Group(
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var type: GroupTypeEnum,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var content: String,

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "image_id", nullable = true)
    var image: File?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    var manager: Member,

    @Column(nullable = false)
    var isOnline: Boolean,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var scope: GroupScopeEnum,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var recruitment: GroupRecruitmentEnum,

    @Column(nullable = false)
    var startHour: Int,

    @Column(nullable = false)
    var startMinute: Int,

    @Column(nullable = true)
    var endHour: Int? = null,

    @Column(nullable = true)
    var endMinute: Int? = null,

    @ElementCollection
    @CollectionTable()
    var dayOfTheWeek: MutableList<DayOfTheWeek>? = ArrayList(),

    @Column(nullable = true)
    var meetingLink: String? = null,

    @Column(nullable = false)
    @ColumnDefault("0")
    var personnel: Int = 0
) : Core() {

    @Column(nullable = false)
    @ColumnDefault("0")
    var views: Int = 0

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: GroupStatusEnum = GroupStatusEnum.READY

    @Column(nullable = false)
    var startDate: LocalDate = LocalDate.now()

    @Column(nullable = true)
    var endDate: LocalDate? = null

    fun update(request: UpdateGroupRequest): Group {
        name = request.name
        content = request.content
        isOnline = request.online
        scope = request.scope
        startHour = request.startHour
        startMinute = request.startMinute
        endHour = request.endHour
        endMinute = request.endMinute
        dayOfTheWeek = request.dayOfTheWeek
        meetingLink = request.meetingLink
        return this
    }

    fun toResponse(): GroupResponse {
        return GroupResponse(
            id = id.toString(),
            name = name,
            content = content,
            image = image?.toResponse(),
            type = type,
            manager = manager.toResponse(),
            isOnline = isOnline,
            scope = scope,
            recruitment = recruitment,
            startHour = startHour,
            startMinute = startMinute,
            endHour = endHour,
            endMinute = endMinute,
            dayOfTheWeek = dayOfTheWeek,
            personnel = personnel,
            views = views,
            status = status,
            createdAt = createdAt!!,
            updatedAt = updatedAt!!
        )
    }
}
