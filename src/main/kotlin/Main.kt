import java.net.ServerSocket

private const val SERVER_PORT = 4221

fun main() {
    val serverSocket = ServerSocket(SERVER_PORT)
    println("Server running on port $SERVER_PORT")

    val client = serverSocket.accept()
    val input = client.getInputStream()
    val output = client.getOutputStream()

        input.bufferedReader().use {
            val request = it.readLine()
            val requestParts = request.split(" ")
            val path = requestParts[1]
            if (path == "/") {
                output.write("HTTP/1.1 200 OK\r\n\r\n".toByteArray())
            } else {
                output.write("HTTP/1.1 404 Not Found\r\n\r\n".toByteArray())
            }
            //output.flush()
            //output.close()

    }




}
