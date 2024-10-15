import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.runBlocking
import java.net.ServerSocket

private const val SERVER_PORT = 4221

fun main() = runBlocking {
    val serverSocket = ServerSocket(SERVER_PORT)
    println("Server running on port $SERVER_PORT")

    while (true) {
        val clientSocket = withContext(Dispatchers.IO) { serverSocket.accept() }
        launch(Dispatchers.IO) {
            val connectionHandler = ConnectionHandler(clientSocket)
            connectionHandler.handle()
        }
    }
}
