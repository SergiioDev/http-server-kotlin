package model

data class Response(
    val path: String,
    val status: HttpStatus,
    val body: String?,
    val headers: Map<String, String>
)
