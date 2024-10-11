import java.net.Socket

private const val ROOT_PATH = "/"

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
            append("HTTP/1.1 ${response.status.code} ${response.status.message}\r\n\r\n")
        }
        println("Answering: $response")
        output.write(responseData.toByteArray())
        output.flush()
        output.close()
    }

    private fun parseRequest(requestLines: List<String>): Response {
        val parts = requestLines[0].split(" ")
        val statusCode = if(parts[1] == ROOT_PATH) {
            HttpStatus.OK
        } else {
            HttpStatus.NOT_FOUND
        }
        return Response(
            method = parts[0],
            path = parts[1],
            status = statusCode,
            body = null,
        )
    }

}


enum class HttpStatus(val code: String, val message: String) {
    OK("200", "OK"),
    NOT_FOUND("404", "NOT FOUND"),
}

data class Response(
    val method: String,
    val path: String,
    val status: HttpStatus,
    val body: String?,
)
