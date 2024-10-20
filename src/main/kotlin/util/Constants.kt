package util

object Constants {
    const val HEADER_SPLIT_CONDITION = ":"
    const val HTTP_VERSION = "HTTP/1.1"
    const val ROOT_PATH = "/"
    val ECHO_PATH_REGEX = "/echo/.*".toRegex()
    const val USER_AGENT_PATH = "/user-agent"
    const val PATH_SEPARATOR = "/"
}
