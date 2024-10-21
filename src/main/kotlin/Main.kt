import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import java.net.ServerSocket

private const val SERVER_PORT = 4221
private val logger = LoggerFactory.getLogger("Main")

fun main(args: Array<String>) = runBlocking {
    val serverSocket = ServerSocket(SERVER_PORT)
    logger.info("Server running on port $SERVER_PORT")

    val fileDirectory = if( args.size >= 2 && args[0] == "--directory") {
        args[1]
    } else {
        null
    }

    while (true) {
        val clientSocket = withContext(Dispatchers.IO) { serverSocket.accept() }
        launch(Dispatchers.IO) {
            val connectionHandler = ConnectionHandler(clientSocket = clientSocket, fileDirectory = fileDirectory)
            connectionHandler.handle()
        }
    }
}
