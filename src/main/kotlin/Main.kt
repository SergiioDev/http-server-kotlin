import java.net.ServerSocket;

private const val SERVER_PORT = 4221

fun main() {
    val serverSocket = ServerSocket(SERVER_PORT)
    println("Server running on port $SERVER_PORT")

    val client = serverSocket.accept()
    client.getOutputStream().write("HTTP/1.1 200 OK\r\n\r\n".toByteArray())
    println("accepted new connection")
}
