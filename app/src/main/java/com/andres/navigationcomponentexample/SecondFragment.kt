package com.andres.navigationcomponentexample

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import com.andres.navigationcomponentexample.databinding.FragmentSecondBinding

class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    private var mediaRecorder: MediaRecorder? = null
    private var audioFile: File? = null
    private var isRecording = false
    private var mediaPlayer: MediaPlayer? = null

    private val recordPermission = Manifest.permission.RECORD_AUDIO
    private val storagePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)

        // Verificar y solicitar permisos si no se han otorgado
        if (ContextCompat.checkSelfPermission(requireContext(), recordPermission) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(requireContext(), storagePermission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(recordPermission, storagePermission), 1)
        }

        // Configurar los botones
        binding.btnGrabar.setOnClickListener {
            if (!isRecording) {
                startRecording()
                binding.btnGrabar.isEnabled = false
                binding.btnDetener.isEnabled = true
            }
        }

        binding.btnDetener.setOnClickListener {
            if (isRecording) {
                stopRecording()
                binding.btnGrabar.isEnabled = true
                binding.btnDetener.isEnabled = false
            }
        }

        // Mostrar los audios grabados
        displayRecordedAudios()

        return binding.root
    }

    private fun startRecording() {
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
        val date = dateFormat.format(Date())
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        audioFile = File(storageDir, "audio_$date.3gp")

        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(audioFile?.absolutePath)
        }

        try {
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            isRecording = true
            Toast.makeText(requireContext(), "Grabando...", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Error al iniciar la grabación", Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopRecording() {
        try {
            mediaRecorder?.stop()
            mediaRecorder?.release()
            mediaRecorder = null
            isRecording = false
            Toast.makeText(requireContext(), "Grabación detenida y guardada", Toast.LENGTH_SHORT).show()
            displayRecordedAudios()
        } catch (e: RuntimeException) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Error al detener la grabación", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayRecordedAudios() {
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        val audioFiles = storageDir?.listFiles { _, name -> name.endsWith(".3gp") }?.toList() ?: emptyList()

        val adapter = AudioAdapter(requireContext(), audioFiles) { selectedFile ->
            playAudio(selectedFile)
        }
        binding.recyclerViewAudios.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewAudios.adapter = adapter
    }


    private fun playAudio(file: File) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(file.absolutePath)
                prepare()
                start()
                Toast.makeText(requireContext(), "Reproduciendo ${file.name}", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Error al reproducir el archivo", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permisos concedidos
            displayRecordedAudios()
        } else {
            Toast.makeText(requireContext(), "Se requieren permisos para grabar audio", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Liberar recursos y evitar fugas de memoria
        mediaPlayer?.release()
        _binding = null
    }
}