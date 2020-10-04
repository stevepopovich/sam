package com.stevenpopovich.talktothat.objecttracker

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.drawable.Drawable

class RectangleDrawable(private val drawRect: Rect) : Drawable() {
    private val paint = Paint()

    init {
        paint.style = Paint.Style.STROKE
        paint.color = Color.YELLOW
    }

    override fun draw(canvas: Canvas) {
        canvas.drawRect(drawRect, paint)
    }

    override fun setAlpha(alpha: Int) {}

    override fun setColorFilter(colorFilter: ColorFilter?) {}

    override fun getOpacity(): Int = PixelFormat.OPAQUE
}
