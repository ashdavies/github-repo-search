package io.ashdavies.check

import com.auth0.jwt.algorithms.Algorithm
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

private const val GOOGLE_AUTH_SCOPE = "https://www.googleapis.com/auth"
private const val GOOGLE_TOKEN_ENDPOINT = "https://accounts.google.com/o/oauth2/token"

private val FIREBASE_CLAIMS_SCOPES = listOf(
    "$GOOGLE_AUTH_SCOPE/cloud-platform",
    "$GOOGLE_AUTH_SCOPE/firebase.database",
    "$GOOGLE_AUTH_SCOPE/firebase.messaging",
    "$GOOGLE_AUTH_SCOPE/identitytoolkit",
    "$GOOGLE_AUTH_SCOPE/userinfo.email",
)

public suspend fun bearerResponse(
    httpClient: HttpClient,
    algorithm: Algorithm,
    accountId: String,
    appId: String,
): BearerResponse {
    val jwt = Jwt.create(algorithm) {
        it.audience = GOOGLE_TOKEN_ENDPOINT
        it.scope = FIREBASE_CLAIMS_SCOPES
        it.issuer = accountId
        it.appId = appId
    }

    return httpClient.post(GOOGLE_TOKEN_ENDPOINT) {
        contentType(ContentType.Application.FormUrlEncoded)
        grantType(JwtBearer)
        assertion(jwt)
    }.body()
}

public data class HttpClientConfig(
    val algorithm: Algorithm,
    val accountId: String,
    val appId: String,
)

@Serializable
public data class BearerResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("token_type") val tokenType: String,
    @SerialName("expires_in") val expiresIn: Int,
)
