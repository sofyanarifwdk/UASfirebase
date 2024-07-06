package com.example.firebase

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.firebase.R

class HomeFragment : Fragment() {

    private lateinit var startDetectionButton: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val startDetectionButton: Button = view.findViewById(R.id.startDetectionButton)
        startDetectionButton.setOnClickListener {
            val intent = Intent(activity, FaceDetectionActivity::class.java)
            startActivity(intent)
        }

        return view;

    }
}
