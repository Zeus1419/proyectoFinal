package com.andres.navigationcomponentexample.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.andres.navigationcomponentexample.Camera
import com.andres.navigationcomponentexample.R


class FirstFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root=inflater.inflate(R.layout.fragment_first, container, false)

        val btnGrabadora=root.findViewById<Button>(R.id.btnNavigate)

        val btnCamara=root.findViewById<Button>(R.id.btncamera)
        btnGrabadora.setOnClickListener{
            findNavController().navigate(R.id.action_firstFragment_to_secondFragment3)
        }
        btnCamara.setOnClickListener {
            findNavController().navigate(R.id.action_firstFragment_to_camera)

        }
        return root

    }
}