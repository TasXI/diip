package com.example.khai_dipplom

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.khai_dipplom.databinding.FragmentChatBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "chatKey"


class ChatFragment : Fragment() {

    private var param1: ChatRecGet? = null

    private lateinit var adapter: MessageAdapter
    private val viewModel: MainScreenViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getSerializable(ARG_PARAM1) as ChatRecGet
        }
    }

    lateinit var binding: FragmentChatBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)


        if (param1 != null) {
            viewModel.setChatContent(param1!!)
        }

        binding.sendButImg.setOnClickListener{
            viewModel.sendkMessage(binding.textOfM.text.toString())
        }

        binding.messageResView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = MessageAdapter(viewModel.getCurrentUserId())
        binding.messageResView.adapter = adapter

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.stateToUpdate.collect {
                    if(it == "messages"){
                        if (param1 != null) viewModel.getMessages(param1!!)
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.currentChat.collect{
                    if(it != null){
                        updateChatContentUI(it)
                    }
                }
            }
        }


        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.messagesFlow.collect{
                    if(it != null){
                        updateMessageContentUI(it)
                    }
                }
            }
        }

        return binding.root
    }

    fun updateChatContentUI(chat: ChatRecGet){
        binding.chatNameIn.text = chat.chatName

    }

    fun updateMessageContentUI(messages: List<Message>){
        adapter.setmMessages(messages)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: ChatRecGet) =
            ChatFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)
                }
            }
    }
}