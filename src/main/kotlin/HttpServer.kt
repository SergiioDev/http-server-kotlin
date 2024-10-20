import model.*
import util.Constants.ECHO_PATH_REGEX
import util.Constants.PATH_SEPARATOR
import util.Constants.ROOT_PATH
import util.Constants.USER_AGENT_PATH

object HttpServer {
    fun handle(request: Request): Response {
        return when {
            request.path == ROOT_PATH -> { handleRoot(request) }
            ECHO_PATH_REGEX.matches(request.path)-> { handleEchoPath(request) }
            request.path == USER_AGENT_PATH -> { handleUserAgentPath(request) }
            else -> handleNotFound(request)
        }
    }

    private fun handleNotFound(
        request: Request,
    ) = Response(
        path = request.path,
        status = HttpStatus.NOT_FOUND,
        body = null,
        headers = emptyMap()
    )

    private fun handleUserAgentPath(
        request: Request,
    ): Response {
        val responseHeaders = mutableMapOf<String, String>()
        val userAgent = request.headers[Header.USER_AGENT.value]?.trim(' ') ?: return Response(
            path = request.path,
            status = HttpStatus.BAD_REQUEST,
            body = null,
            headers = responseHeaders
        )

        responseHeaders[Header.CONTENT_LENGTH.value] = userAgent.length.toString()
        responseHeaders[Header.CONTENT_TYPE.value] = ContentType.TEXT_PLAIN.value
        return Response(
            path = request.path,
            status = HttpStatus.OK,
            body = userAgent,
            headers = responseHeaders
        )
    }

    private fun handleEchoPath(
        request: Request,
    ): Response {
        val responseHeaders = mutableMapOf<String, String>()
        val responseBody = request.path.split(PATH_SEPARATOR)[2]

        responseHeaders[Header.CONTENT_LENGTH.value] = responseBody.length.toString()
        responseHeaders[Header.CONTENT_TYPE.value] = ContentType.TEXT_PLAIN.value
        return Response(
            path = request.path,
            status = HttpStatus.OK,
            body = responseBody,
            headers = responseHeaders
        )
    }

    private fun handleRoot(request: Request) = Response(
        path = request.path,
        status = HttpStatus.OK,
        body = null,
        headers = emptyMap()
    )

}
