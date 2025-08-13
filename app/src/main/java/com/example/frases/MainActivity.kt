package com.example.frases

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.frases.databinding.ActivityMainBinding
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var frases: List<String> = emptyList()
    private var fraseAtual: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding: habilite no build.gradle.kts
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Carregar as frases do arquivo uma única vez
        frases = carregarFrasesDoAssets("frases.txt")

        // Restaura a frase atual após a rotação
        if(savedInstanceState != null) {
            fraseAtual = savedInstanceState.getString("fraseAtual")
            fraseAtual?.let { binding.tvFrase.text = it }
        }

        binding.btnNovaFrase.setOnClickListener {
            if(frases.isEmpty()) {
                binding.tvFrase.text = "Arquivo de frases vazio ou não encontrado."
            } else {
                val nova = frases[Random.nextInt(frases.size)]
                fraseAtual = nova
                binding.tvFrase.text = nova
            }
        }


    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("fraseAtual", fraseAtual)
    }

    private fun carregarFrasesDoAssets(nomeArquivo: String): List<String> {
        return try {
            assets.open(nomeArquivo).use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use { br ->
                    br.lineSequence()
                        .map { it.trim() }
                        .filter { it.isNotEmpty() }
                        .toList()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}