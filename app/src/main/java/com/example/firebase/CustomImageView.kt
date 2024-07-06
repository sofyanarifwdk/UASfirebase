package com.example.firebase

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.google.mlkit.vision.face.Face

class CustomImageView(context: Context, attrs: AttributeSet?) : AppCompatImageView(context, attrs) {

    private val faceRects = mutableListOf<Rect>()
    private val paint = Paint().apply {
        style = Paint.Style.STROKE
        color = android.graphics.Color.RED
        strokeWidth = 8.0f
    }

    fun setFaces(faces: List<Face>) {
        faceRects.clear()
        for (face in faces) {
            val rect = face.boundingBox
            faceRects.add(rect)
        }
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        if (canvas != null) {
            super.onDraw(canvas)
        }
        canvas?.let {
            for (rect in faceRects) {
                canvas.drawRect(rect, paint)
            }
        }
    }
}
