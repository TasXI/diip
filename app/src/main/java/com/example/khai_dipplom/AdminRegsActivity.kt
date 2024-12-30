package com.example.khai_dipplom

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.airbnb.lottie.LottieDrawable
import com.example.khai_dipplom.databinding.ActivityAdminRegsBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import kotlin.time.Duration

class AdminRegsActivity : AppCompatActivity() {

    lateinit var binding: ActivityAdminRegsBinding

    private val adminViewModel: AdminViewModel by viewModels(){

        AdminViewModel.setApi(KtorApi((application as KtorClient).client))
        AdminViewModel.Factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adminViewModel.curAdmin = intent.getSerializableExtra("admin_key") as UserClient

        binding = ActivityAdminRegsBinding.inflate(layoutInflater)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                adminViewModel.isSucces.collect {

                    binding.loadan.visibility = View.GONE
                    binding.loadan.cancelAnimation()
                    binding.signUpButton.visibility = View.VISIBLE

                    when (it) {
                       201 -> Snackbar.make(findViewById(android.R.id.content), "OK", Snackbar.LENGTH_LONG).show()
                        -1 -> Snackbar.make(findViewById(android.R.id.content), "NOT", Snackbar.LENGTH_LONG).show()
                        -999 -> Snackbar.make(findViewById(android.R.id.content), "ACCESS DENIED", Snackbar.LENGTH_LONG).show()
                        else -> Snackbar.make(findViewById(android.R.id.content), "??????", Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }


        enableEdgeToEdge()

        setContentView(binding.root)


        binding.signUpButton.setOnClickListener {

            binding.signUpButton.visibility = View.GONE
            binding.loadan.visibility = View.VISIBLE
            binding.loadan.setMinProgress(0.0f)
            binding.loadan.setMaxProgress(1.0f)
            binding.loadan.repeatCount = LottieDrawable.INFINITE
            binding.loadan.repeatMode = LottieDrawable.RESTART
            binding.loadan.playAnimation()

            if (binding.passwordTextMain.text.toString() == binding.passwordTextRec.text.toString())

                adminViewModel.doSignUp(
                    binding.emailText.text.toString(),
                    binding.passwordTextMain.text.toString(),
                    binding.firstNameText.text.toString(),
                    binding.secondNameText.text.toString(),
                    binding.surnameNameText.text.toString()
                )
            else{
                binding.loadan.visibility = View.GONE
                binding.loadan.cancelAnimation()
                binding.signUpButton.visibility = View.VISIBLE
            }




        }
    }
}
