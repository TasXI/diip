package com.example.khai_dipplom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.khai_dipplom.databinding.DialogAddChatBinding
import kotlinx.coroutines.launch

class DialogChatAddFragment : DialogFragment() {

    private lateinit var availableUsersAdapter: AvailableUsersAdapter
    private lateinit var selectedUsersAdapter: SelectedUsersAdapter
    private val availableUsers = mutableListOf<UserInfo>()
    private val selectedUsers = mutableListOf<UserInfo>()
    private val dialViewModel: MainScreenViewModel by activityViewModels()
    lateinit var binding: DialogAddChatBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogAddChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.closeDi.setOnClickListener{
            dismiss()
        }

        binding.btnCreateChat.setOnClickListener{

            val addusers = selectedUsers.map { it.id }

            if(addusers.size > 1) {
                dialViewModel.createChat(
                    binding.chatNameDi.text.toString(),
                    addusers,
                    listOf<Message>()
                )
            }

            dismiss()

        }

        dialViewModel.getAllUsersByRole()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                dialViewModel.userlist.collect{
                    if (it!= null) {
                        loadUsers(it)
                    }
                }
            }
        }


        availableUsersAdapter = AvailableUsersAdapter(availableUsers) { user ->
            addUserToSelected(user)
        }
        selectedUsersAdapter = SelectedUsersAdapter(selectedUsers) { user ->
            removeUserFromSelected(user)
        }

        binding.rvAvailableUsers.layoutManager = LinearLayoutManager(context)
        binding.rvAvailableUsers.adapter = availableUsersAdapter

        binding.rvSelectedUsers.layoutManager = LinearLayoutManager(context)
        binding.rvSelectedUsers.adapter = selectedUsersAdapter

    }

    private fun loadUsers(list: List<UserInfo>) {
        availableUsers.addAll(list)
        availableUsersAdapter.notifyDataSetChanged()
    }

    private fun addUserToSelected(user: UserInfo) {
        availableUsers.remove(user)
        selectedUsers.add(user)
        availableUsersAdapter.notifyDataSetChanged()
        selectedUsersAdapter.notifyDataSetChanged()
    }

    private fun removeUserFromSelected(user: UserInfo) {
        selectedUsers.remove(user)
        availableUsers.add(user)
        availableUsersAdapter.notifyDataSetChanged()
        selectedUsersAdapter.notifyDataSetChanged()
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
        }
    }
}