import model.HttpStatus
import model.Response
import java.net.Socket

private const val ROOT_PATH = "/"
private val ECHO_PATH_REGEX = "/echo/.*".toRegex()

class ConnectionHandler(clientSocket: Socket) {
    private val input = clientSocket.getInputStream().bufferedReader()
    private val output = clientSocket.getOutputStream()

    fun handle(){
        println("Handling request...")
        var line = input.readLine()
        val requestLines = mutableListOf<String>()

        while(line.isNotBlank()) {
            requestLines.add(line)
            line = input.readLine()
        }

        val response = parseRequest(requestLines)

        val responseData = buildString {
            append("HTTP/1.1 ${response.status.code} ${response.status.message}\r\n")
            response.headers.forEach{ (key, v) ->
                append("$key: $v\r\n")
            }
            append("\r\n")
            append(response.body)
        }
        println("Answering: $response")
        output.write(responseData.toByteArray())
        output.flush()
        output.close()
    }

    private fun parseRequest(requestLines: List<String>): Response {
        val parts = requestLines[0].split(" ")
        val path = parts[1]

        val headers = mutableMapOf<String, String>()
        return when {
            path == ROOT_PATH -> {
                Response(
                    path = path,
                    status = HttpStatus.OK,
                    body = null,
                    headers = headers
                )
            }
            ECHO_PATH_REGEX.matches(path)-> {
                val responseBody = path.split("/")[2]
                headers["Content-Length"] = responseBody.length.toString()
                headers["Content-Type"] = "text/plain"
                Response(
                    path = path,
                    status = HttpStatus.OK,
                    body = responseBody,
                    headers = headers
                )
            }
            else -> Response(
                path = path,
                status = HttpStatus.NOT_FOUND,
                body = null,
                headers = headers
            )
        }
    }
}
