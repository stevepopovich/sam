package com.stevenpopovich.talktothat

import android.util.Log

fun Any.verboseLog(tag: String = "TTT") {
    Log.v(tag, this.toString())
}
