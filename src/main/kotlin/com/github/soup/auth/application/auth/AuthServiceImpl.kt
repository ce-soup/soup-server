package com.github.soup.auth.application.auth

import com.github.soup.auth.application.oauth.OAuthService
import com.github.soup.auth.application.token.TokenService
import com.github.soup.auth.domain.auth.Auth
import com.github.soup.auth.domain.auth.AuthRepository
import com.github.soup.auth.exceptions.AlreadyExistingAuthException
import com.github.soup.auth.exceptions.InvalidTokenException
import com.github.soup.auth.exceptions.NotFoundAuthException
import com.github.soup.auth.infra.http.request.ReIssueRequest
import com.github.soup.auth.infra.http.request.SignInRequest
import com.github.soup.auth.infra.http.request.SignUpRequest
import com.github.soup.auth.infra.http.response.TokenResponse
import com.github.soup.file.application.facade.FileFacade
import com.github.soup.file.domain.FileType
import com.github.soup.member.domain.Member
import com.github.soup.member.domain.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AuthServiceImpl(
    private val tokenService: TokenService,
    private val oAuthService: OAuthService,
    private val authRepository: AuthRepository,
    private val memberRepository: MemberRepository,
    private val fileFacade: FileFacade
) : AuthService {
    @Transactional
    override fun login(request: SignInRequest): TokenResponse {
        val auth = authRepository.getByAuthTypeAndClientId(
            request.type,
            oAuthService.getClientId(
                request.type,
                request.token
            )
        ) ?: throw NotFoundAuthException()

        return tokenService.create(auth).toResponse()
    }

    @Transactional
    override fun create(request: SignUpRequest): TokenResponse {
        val clientId = oAuthService.getClientId(request.type, request.token)
        if (authRepository.getByAuthTypeAndClientId(request.type, clientId) != null) {
            throw AlreadyExistingAuthException()
        }

        val member = memberRepository.save(
            Member(
                name = request.name,
                nickname = request.nickname,
                sex = request.sex,
                bio = request.bio
            )
        )

        if (request.profileImage != null) {
            member.profileImage = fileFacade.upload(
                memberId = member.id!!,
                type = FileType.PROFILE,
                image = request.profileImage
            )
        }

        val auth = authRepository.save(
            Auth(
                member = member,
                type = request.type,
                clientId = clientId,
            )
        )

        return tokenService.create(auth).toResponse()
    }

    @Transactional
    override fun reissue(request: ReIssueRequest): TokenResponse {
        if (!tokenService.validation(request.refreshToken)) {
            throw InvalidTokenException()
        }

        val auth = authRepository.getByMemberId(
            tokenService.parse(request.refreshToken)
        ) ?: throw NotFoundAuthException()

        return tokenService.create(auth).toResponse()
    }

    @Transactional
    override fun logout(memberId: String): Boolean {
        val auth = authRepository.getByMemberId(memberId) ?: throw NotFoundAuthException()

        return tokenService.remove(auth).and(true)
    }
}