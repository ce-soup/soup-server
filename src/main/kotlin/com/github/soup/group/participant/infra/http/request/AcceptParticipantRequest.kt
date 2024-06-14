package com.github.soup.group.participant.infra.http.request

import jakarta.validation.constraints.NotNull

data class AcceptParticipantRequest(
    @NotNull
    val memberIdList: List<String>
)