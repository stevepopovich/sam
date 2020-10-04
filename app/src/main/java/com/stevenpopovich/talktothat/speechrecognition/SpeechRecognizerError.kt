package com.stevenpopovich.talktothat.speechrecognition

val Int.toRecognizerError: String
    get() {
        return when (this) {
            7 -> "ERROR_NO_MATCH"
            8 -> "ERROR_RECOGNIZER_BUSY"
            else -> "UNKNOWN ERROR"
        }
    }
