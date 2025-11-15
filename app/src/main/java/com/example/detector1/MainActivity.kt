package com.example.detector1

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.detector1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val PERMISSION_REQUEST_CODE = 1001

    private val REQUIRED_PERMISSIONS = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
        arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
        )
    } else {
        arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Permisos al iniciar
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSION_REQUEST_CODE)
        }

        // === Animaciones de tarjetas ===
        addCardAnimation(binding.cardRecolectar)
        addCardAnimation(binding.cardEvaluacion)
        addCardAnimation(binding.cardListado)
        // Cuando agregues más cards, simplemente las añades aquí:
        // addCardAnimation(binding.cardEntrenar)
        // addCardAnimation(binding.cardExportar)
        // addCardAnimation(binding.cardTranscripcion)
        // addCardAnimation(binding.cardMisTranscripciones)
        // addCardAnimation(binding.cardInstrucciones)

        // === Clicks de las tarjetas ===
        binding.cardRecolectar.setOnClickListener {
            if (allPermissionsGranted()) {
                startActivity(Intent(this, RecordGestureActivity::class.java))
            } else requestPermissions()
        }

        binding.cardEvaluacion.setOnClickListener {
            if (allPermissionsGranted()) {
                startActivity(Intent(this, EvaluateGestureActivity::class.java))
            } else requestPermissions()
        }

        binding.cardListado.setOnClickListener {
            startActivity(Intent(this, ListGesturesActivity::class.java))
        }

        // Ejemplo para futuros cards:
        /*
        binding.cardTranscripcion.setOnClickListener {
            if (allPermissionsGranted()) {
                startActivity(Intent(this, TranscriptionActivity::class.java))
            } else requestPermissions()
        }

        binding.cardMisTranscripciones.setOnClickListener {
            startActivity(Intent(this, ListTranscriptionsActivity::class.java))
        }

        binding.cardInstrucciones.setOnClickListener {
            startActivity(Intent(this, InstructionsActivity::class.java))
        }
        */
    }

    // === Efecto de animación táctil ===
    private fun addCardAnimation(card: CardView) {
        val clickAnim = AnimationUtils.loadAnimation(this, R.anim.button_click)
        val releaseAnim = AnimationUtils.loadAnimation(this, R.anim.button_release)

        card.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.startAnimation(clickAnim)
                    v.animate().scaleX(0.96f).scaleY(0.96f).setDuration(100).start()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    v.startAnimation(releaseAnim)
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
                }
            }
            false
        }
    }

    // === Manejo de permisos ===
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            REQUIRED_PERMISSIONS,
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE && !allPermissionsGranted()) {
            Toast.makeText(
                this,
                "Se requieren permisos para usar esta aplicación",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
