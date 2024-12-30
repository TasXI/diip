package com.example.khai_dipplom

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.addCallback
import androidx.activity.findViewTreeOnBackPressedDispatcherOwner
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.replace
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.khai_dipplom.databinding.ActivityMainScreenBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainScreenBinding

    private lateinit var clientk: KtorClient

    private  val mainScreenViewModel: MainScreenViewModel by viewModels(){
        MainScreenViewModel.setApi(KtorApi((application as KtorClient).client))
        MainScreenViewModel.Factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        clientk = application as KtorClient



        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar);

        var drawerToggle = ActionBarDrawerToggle(
            this, binding.sideMenu, binding.toolbar,
            R.string.d_open, R.string.d_close);

        binding.sideMenu.addDrawerListener(drawerToggle);
        drawerToggle.syncState();


        binding.naviga.setNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.nav_profile -> {
                    val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragment_container, ProfileFragment())
                    transaction.commit()
                }
                R.id.nav_chats -> {
                    val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragment_container, ScrollingFragment())
                    transaction.commit()
                }
                R.id.nav_main_page -> {
                    val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragment_container, MainContentFragment())
                    transaction.commit()
                }
                R.id.nav_log_out -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            // Close the drawer
            binding.sideMenu.closeDrawer(GravityCompat.START)

            return@setNavigationItemSelectedListener true
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                mainScreenViewModel.curUser.collect {
                    if (it != null) {
                        binding.name1.text = it.user.name1
                        binding.name2.text = it.user.name2
                        binding.name3.text = it.user.name3
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                mainScreenViewModel.openChatFrag.collect {
                    if (it != null) {
                        openChat(it)
                    }
                }
            }
        }


        mainScreenViewModel.setUser(intent.getSerializableExtra("user_key") as UserClient)


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainScreenViewModel.stateToUpdate = clientk.getUpdFlow()

            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {


                if (!clientk.isConnected())
                    withContext(Dispatchers.IO) {
                        try {
                            clientk.connectToSocket(mainScreenViewModel.getCurrentUserId())
                        } catch (e: Exception) {
                            Log.e("Socket", "Error while connecting: ${e.message}")
                        }
                    }

            }
        }


        onBackPressedDispatcher.addCallback{
            if (binding.sideMenu.isDrawerOpen(GravityCompat.START)) {
                binding.sideMenu.closeDrawer(GravityCompat.START)
            }
        }
    }

    private fun openChat(chat: ChatRecGet) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        val chatFragment: ChatFragment = ChatFragment.newInstance(chat)
        transaction.replace(R.id.fragment_container, chatFragment)
            .addToBackStack(null).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.op_menu, menu)
        return true
    }

}
