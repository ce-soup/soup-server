package com.github.soup.group.participant.infra.http.request

import jakarta.validation.constraints.NotEmpty

data class CreateParticipantRequest(

    @NotEmpty
    val groupId: String,

    val message: String
)