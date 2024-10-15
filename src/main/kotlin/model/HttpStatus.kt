package model

enum class HttpStatus(val code: String, val message: String) {
    OK("200", "OK"),
    NOT_FOUND("404", "NOT FOUND"),
}
