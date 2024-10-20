import model.Request
import model.Response
import util.Constants.HEADER_SPLIT_CONDITION
import util.Constants.HTTP_VERSION
import java.net.Socket

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

        val request = parseRequest(requestLines)
        val response = HttpServer.handle(request)

        val responseData = parseResponse(response)
        println("Answering: $response")
        output.write(responseData.toByteArray())
        output.flush()
        output.close()
    }

    private fun parseResponse(response: Response) =
        buildString {
            append("$HTTP_VERSION ${response.status.code} ${response.status.message}\r\n")
            response.headers.forEach { (key, v) ->
                append("$key: $v\r\n")
            }
            append("\r\n")
            append(response.body)
        }

    private fun parseRequest(requestLines: List<String>): Request {
        val parts = requestLines[0].split(" ")
        val path = parts[1]
        val headers = requestLines.drop(1).takeWhile { it.contains(HEADER_SPLIT_CONDITION) }
            .map { it.split(HEADER_SPLIT_CONDITION) }
            .associate { it[0] to it[1] }

        return Request(path = path, body = null, headers = headers)
    }
}
