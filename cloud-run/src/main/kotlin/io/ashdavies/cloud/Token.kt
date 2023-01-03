package io.ashdavies.cloud

import io.ashdavies.check.AppCheckRequest
import io.ashdavies.check.appCheck
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.header
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post
import io.ktor.server.routing.put

internal fun Routing.token(client: HttpClient) {
    post("/token") {
        val appCheck = firebaseApp.appCheck(client)
        val appCheckRequest = call.receive<AppCheckRequest>()
        val appCheckToken = appCheck.createToken(
            appId = appCheckRequest.appId,
        )

        call.respond(
            status = HttpStatusCode.OK,
            message = appCheckToken,
        )
    }

    put("/verify") {
        val appCheck = firebaseApp.appCheck(client)
        val appCheckToken = requireNotNull(call.request.header("X-Firebase-AppCheck")) {
            "Request is missing app check token header"
        }

        call.respond(
            message = appCheck.verifyToken(appCheckToken),
            status = HttpStatusCode.OK,
        )
    }
}
