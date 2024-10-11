import java.net.ServerSocket

private const val SERVER_PORT = 4221

fun main() {
    val serverSocket = ServerSocket(SERVER_PORT)
    println("Server running on port $SERVER_PORT")

    while(true) {
        val clientSocket = serverSocket.accept()

        val connectionHandler = ConnectionHandler(clientSocket)
        connectionHandler.handle()
    }


}