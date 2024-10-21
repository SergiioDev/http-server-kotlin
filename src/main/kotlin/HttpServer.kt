import model.*
import util.Constants.ECHO_PATH_REGEX
import util.Constants.FILE_PATH_REGEX
import util.Constants.PATH_SEPARATOR
import util.Constants.ROOT_PATH
import util.Constants.USER_AGENT_PATH
import java.io.File

object HttpServer {
    fun handle(request: Request, fileDirectory: String?): Response {
        return when {
            request.path == ROOT_PATH -> { handleRoot(request) }
            ECHO_PATH_REGEX.matches(request.path) -> { handleEchoPath(request) }
            request.path == USER_AGENT_PATH -> { handleUserAgentPath(request) }
            FILE_PATH_REGEX.matches(request.path) -> { getFileHandler(request = request, fileDirectory!!) }

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
        val userAgent = request.headers[Header.USER_AGENT.value]?.trim(' ') ?: return Response(
            path = request.path,
            status = HttpStatus.BAD_REQUEST,
            body = null,
            headers = emptyMap()
        )

        val headers = mapOf(
            Header.CONTENT_LENGTH.value to userAgent.length.toString(),
            Header.CONTENT_TYPE.value to ContentType.TEXT_PLAIN.value
        )

        return Response(
            path = request.path,
            status = HttpStatus.OK,
            body = userAgent,
            headers = headers
        )
    }

    private fun handleEchoPath(
        request: Request,
    ): Response {
        val responseBody = request.path.split(PATH_SEPARATOR)[2]
        val headers = mapOf(
            Header.CONTENT_LENGTH.value to responseBody.length.toString(),
            Header.CONTENT_TYPE.value to ContentType.TEXT_PLAIN.value
        )

        return Response(
            path = request.path,
            status = HttpStatus.OK,
            body = responseBody,
            headers = headers
        )
    }

    private fun getFileHandler(request: Request, fileDirectory: String): Response {
        val fileName = request.path.split(PATH_SEPARATOR)[2]
        val file = File("/$fileDirectory/$fileName")
        if(!file.exists()) {
            return Response(
                path = request.path,
                status = HttpStatus.NOT_FOUND,
                body = null,
                headers = emptyMap(),
            )
        }

        val responseBody = file.readText()
        val headers = mapOf(
            Header.CONTENT_TYPE.value to ContentType.OCTET_STREAM.value,
            Header.CONTENT_LENGTH.value to responseBody.length.toString()
        )

        return Response(
            path = request.path,
            status = HttpStatus.OK,
            body = file.readText(),
            headers = headers,
        )

    }

    private fun handleRoot(request: Request) = Response(
        path = request.path,
        status = HttpStatus.OK,
        body = null,
        headers = emptyMap()
    )

}
