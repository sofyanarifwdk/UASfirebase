package com.example.firebase

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.face.FaceLandmark

class FaceDetectionActivity : AppCompatActivity() {

    private val REQUEST_IMAGE_CAPTURE = 1

    private lateinit var imageView: ImageView
    private lateinit var captureButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face_detection)

        imageView = findViewById(R.id.imageView)
        captureButton = findViewById(R.id.captureButton)

        captureButton.setOnClickListener {
            dispatchTakePictureIntent()
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            runFaceDetection(imageBitmap)
        }
    }

    private fun runFaceDetection(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)

        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .build()

        val detector = FaceDetection.getClient(options)

        detector.process(image)
            .addOnSuccessListener { faces ->
                val resultBitmap = drawFaceBounds(bitmap, faces)
                imageView.setImageBitmap(resultBitmap)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Face detection failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun drawFaceBounds(bitmap: Bitmap, faces: List<Face>): Bitmap {
        val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(mutableBitmap)
        val paint = Paint()
        paint.color = Color.GREEN
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2.0f

        val textPaint = Paint()
        textPaint.color = Color.BLUE
        textPaint.textSize = 5f

        for (face in faces) {
            val bounds = face.boundingBox
            canvas.drawRect(bounds, paint)

            // Draw landmarks and add labels
            drawLandmark(face, canvas, FaceLandmark.LEFT_EYE, "Left Eye", textPaint)
            drawLandmark(face, canvas, FaceLandmark.RIGHT_EYE, "Right Eye", textPaint)
            drawLandmark(face, canvas, FaceLandmark.NOSE_BASE, "Nose", textPaint)
            drawLandmark(face, canvas, FaceLandmark.MOUTH_LEFT, "Left Mouth", textPaint)
            drawLandmark(face, canvas, FaceLandmark.MOUTH_RIGHT, "Right Mouth", textPaint)
            drawLandmark(face, canvas, FaceLandmark.MOUTH_BOTTOM, "Mouth", textPaint)
            drawLandmark(face, canvas, FaceLandmark.LEFT_EAR, "Left Ear", textPaint)
            drawLandmark(face, canvas, FaceLandmark.RIGHT_EAR, "Right Ear", textPaint)
        }

        return mutableBitmap
    }

    private fun drawLandmark(face: Face, canvas: Canvas, landmarkType: Int, label: String, textPaint: Paint) {
        val landmark = face.getLandmark(landmarkType)
        if (landmark != null) {
            val cx = landmark.position.x
            val cy = landmark.position.y
            canvas.drawCircle(cx, cy, 2f, textPaint)
            canvas.drawText(label, cx + 10, cy, textPaint)
        }
    }
}
