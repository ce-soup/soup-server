package com.github.soup.group.participant.application.facade

import com.github.soup.group.participant.infra.http.request.AcceptParticipantRequest
import com.github.soup.group.participant.infra.http.request.CreateParticipantRequest
import com.github.soup.group.participant.infra.http.response.ParticipantResponse

interface ParticipantFacade {
    fun join(memberId: String, request: CreateParticipantRequest): Boolean

    fun participantList(memberId: String, groupId: String): List<ParticipantResponse>

    fun accept(memberId: String, groupId: String, request: AcceptParticipantRequest): Boolean

    fun isRegister(memberId: String, groupId: String): Boolean

    fun isParticipant(memberId: String, groupId: String): Boolean

    fun firstcome(key: String)
}