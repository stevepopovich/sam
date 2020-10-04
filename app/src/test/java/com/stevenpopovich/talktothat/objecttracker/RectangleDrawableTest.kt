package com.stevenpopovich.talktothat.objecttracker

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Rect
import com.stevenpopovich.talktothat.testutils.relaxedMock
import io.mockk.verifyAll
import org.junit.Assert.assertEquals
import org.junit.Test

class RectangleDrawableTest {
    private val rect: Rect = relaxedMock()
    private val canvas: Canvas = relaxedMock()
    private val paint: Paint = relaxedMock()

    private val rectangleDrawable = RectangleDrawable(rect, paint)

    @Test
    fun `rectangle drawable will draw a gold outlined rectangle`() {
        rectangleDrawable.draw(canvas)

        verifyAll {
            paint.style = Paint.Style.STROKE
            paint.color = Color.YELLOW

            canvas.drawRect(rect, paint)
        }
    }

    @Test
    fun `rectangle drawable is opaque`() {
        assertEquals(PixelFormat.OPAQUE, rectangleDrawable.opacity)
    }

    @Test
    fun `rectangle drawable doesn't do much else`() {
        rectangleDrawable.alpha = 0
        rectangleDrawable.colorFilter = null
    }
}
