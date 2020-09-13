package com.stevenpopovich.talktothat

import android.util.Log

fun String.verboseLog(tag: String = "TTT") {
    Log.v(tag, this)
}
