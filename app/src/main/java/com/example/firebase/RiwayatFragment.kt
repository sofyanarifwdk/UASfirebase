package com.example.firebase

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import java.io.InputStream

class RiwayatFragment : Fragment() {

    private lateinit var selectImageButton: Button
    private lateinit var imageView: ImageView

    private val SELECT_IMAGE_REQUEST = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_riwayat, container, false)

        selectImageButton = view.findViewById(R.id.button_select_image)
        imageView = view.findViewById(R.id.image_view)

        selectImageButton.setOnClickListener {
            selectImage()
        }

        return view
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, SELECT_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_IMAGE_REQUEST && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                processImage(uri)
            }
        }
    }

    private fun processImage(imageUri: Uri) {
        val imageStream: InputStream? = requireActivity().contentResolver.openInputStream(imageUri)
        val selectedImage = BitmapFactory.decodeStream(imageStream)
        imageView.setImageBitmap(selectedImage)

        val image = InputImage.fromBitmap(selectedImage, 0)
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .build()
        val detector = FaceDetection.getClient(options)

        detector.process(image)
            .addOnSuccessListener { faces ->
                processFaceResult(faces)
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }

    private fun processFaceResult(faces: List<Face>) {
        for (face in faces) {
            // Example: Log the bounds of each face
            val bounds = face.boundingBox
            println("Face bounds: $bounds")

            // Here you can add code to draw rectangles around detected faces on the ImageView, etc.
        }
    }
}
