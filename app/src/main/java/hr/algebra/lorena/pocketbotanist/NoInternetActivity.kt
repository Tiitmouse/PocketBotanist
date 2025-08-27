package hr.algebra.lorena.pocketbotanist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hr.algebra.lorena.pocketbotanist.databinding.ActivityNoInternetBinding

class NoInternetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoInternetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoInternetBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}