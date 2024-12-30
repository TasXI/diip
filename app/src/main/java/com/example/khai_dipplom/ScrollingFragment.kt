package com.example.khai_dipplom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.khai_dipplom.databinding.ActivityMainBinding
import com.example.khai_dipplom.databinding.FragmentScrollingBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ScrollingFragment : Fragment() {

    private val scViewModel: MainScreenViewModel by activityViewModels()

    lateinit var viewBinding: FragmentScrollingBinding
    val adapter = ChatListAdapter(ChatClick())
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewBinding = FragmentScrollingBinding.inflate(inflater, container, false)

        viewBinding.chats.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        viewBinding.fab.setOnClickListener{
            val fullScreenDialog = DialogChatAddFragment()
            fullScreenDialog.show(parentFragmentManager, "full_screen_dialog")
        }

        viewBinding.chats.adapter = adapter



        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                scViewModel.chatlist.collect{
                    if (it != null){
                        updateChats(it)
                    }

                }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                scViewModel.stateToUpdate.collect{
                    if (it == "chats") {
                        scViewModel.getChats()
                    }
                }
            }
        }

        scViewModel.getChats()




        return viewBinding.root
    }

    fun updateChats(chats: List<ChatRecGet>){
        adapter.setChats(chats)
    }

    inner class ChatClick : ChatListAdapter.GoToChat{
        override fun onClick(chatTo: ChatRecGet) {
            scViewModel.openChatFragment(chatTo)
        }

    }

}