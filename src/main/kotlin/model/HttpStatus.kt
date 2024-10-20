package model

enum class HttpStatus(val code: String, val message: String) {
    OK("200", "OK"),
    NOT_FOUND("404", "NOT FOUND"),
    BAD_REQUEST("400", "BAD_REQUEST"),
}
