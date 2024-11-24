package com.andres.navigationcomponentexample

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController


class FirstFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_first, container, false)

        val btnNavigate = root.findViewById<Button>(R.id.btnNavigate)

        btnNavigate.setOnClickListener {
            findNavController().navigate(FirstFragmentDirections.actionFirstFragmentToSecondFragment3(
                Zeus = "Zeus"
            )
            )

        }
        val btnFormulario=root.findViewById<Button>(R.id.btnFormulario)

        val btnRoom=root.findViewById<Button>(R.id.btnRoom)

        btnRoom.setOnClickListener{
            findNavController().navigate(R.id.action_firstFragment_to_room)
        }
        btnFormulario.setOnClickListener{
            findNavController().navigate(R.id.action_firstFragment_to_formulario)
        }
        return root

    }
}