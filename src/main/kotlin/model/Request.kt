package model

data class Request(
    val path: String,
    val body: String?,
    val headers: Map<String, String>
)
