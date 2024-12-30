package com.example.khai_dipplom

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.airbnb.lottie.LottieDrawable
import com.example.khai_dipplom.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class MainActivity : AppCompatActivity() {

    lateinit var viewBinding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModels() {
        MainViewModel.setApi(KtorApi((application as KtorClient).client))
        MainViewModel.Factory
    }

    private fun toAdminActivity(admin: UserClient) {
        val intent = Intent(this, AdminRegsActivity::class.java)
        intent.putExtra("admin_key", admin)
        startActivity(intent)
    }

    private fun toUserActivity(user: UserClient) {
        val intent = Intent(this, MainScreenActivity::class.java)
        intent.putExtra("user_key", user)
        startActivity(intent)
        finish()
    }

    private fun testFun(user: User){

        viewBinding.mailLyText.setText(user.name1)
        viewBinding.passwordLyText.setText(user.name2)
        viewBinding.signInButton.setText(user.username)

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                mainViewModel.userFlow.collect {

                    viewBinding.loadan.visibility = View.GONE
                    viewBinding.loadan.cancelAnimation()

                    if (it != null) {
                        when (it.user.role) {
                            "admin" -> toAdminActivity(it)
                            "student" -> toUserActivity(it)
                        }
                    }else{
                        Snackbar.make(this@MainActivity ,findViewById(android.R.id.content), "Failure", Snackbar.LENGTH_LONG).show()

                    }
                }
            }
        }

            viewBinding = ActivityMainBinding.inflate(layoutInflater)



            enableEdgeToEdge()
            setContentView(viewBinding.root)

            viewBinding.signInButton.setOnClickListener {

                viewBinding.loadan.visibility = View.VISIBLE
                viewBinding.loadan.setMinProgress(0.0f)
                viewBinding.loadan.setMaxProgress(1.0f)
                viewBinding.loadan.repeatCount = LottieDrawable.INFINITE
                viewBinding.loadan.repeatMode = LottieDrawable.RESTART
                viewBinding.loadan.playAnimation()
                mainViewModel.signIn(viewBinding.mailLyText.text.toString(), viewBinding.passwordLyText.text.toString())

                }





            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }


    }
}