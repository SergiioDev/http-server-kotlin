package model

enum class Header(val value: String) {
    CONTENT_LENGTH("Content-Length"),
    CONTENT_TYPE("Content-Type"),
    USER_AGENT("User-Agent")
}
