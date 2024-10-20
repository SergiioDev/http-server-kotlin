import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import java.net.ServerSocket

private const val SERVER_PORT = 4221
private val logger = LoggerFactory.getLogger("Main")

fun main() = runBlocking {
    val serverSocket = ServerSocket(SERVER_PORT)
    logger.info("Server running on port $SERVER_PORT")

    while (true) {
        val clientSocket = withContext(Dispatchers.IO) { serverSocket.accept() }
        launch(Dispatchers.IO) {
            val connectionHandler = ConnectionHandler(clientSocket)
            connectionHandler.handle()
        }
    }
}
