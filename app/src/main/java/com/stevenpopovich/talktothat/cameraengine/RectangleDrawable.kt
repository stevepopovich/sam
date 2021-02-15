package com.stevenpopovich.talktothat.cameraengine

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.drawable.Drawable

class RectangleDrawable(private val drawRect: Rect, private val paint: Paint = Paint()) : Drawable() {
    override fun draw(canvas: Canvas) {
        paint.style = Paint.Style.STROKE
        paint.color = Color.YELLOW

        canvas.drawRect(drawRect, paint)
    }

    override fun setAlpha(alpha: Int) {}

    override fun setColorFilter(colorFilter: ColorFilter?) {}

    override fun getOpacity(): Int = PixelFormat.OPAQUE
}
