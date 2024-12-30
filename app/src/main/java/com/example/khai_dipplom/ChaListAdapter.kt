package com.example.khai_dipplom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet.Constraint
import androidx.recyclerview.widget.RecyclerView

class ChatListAdapter(val goToChat: GoToChat) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var chatsList: List<ChatRecGet> = listOf()

    interface GoToChat{
        fun onClick(chatTo: ChatRecGet)
    }

    fun setChats(chatsList: List<ChatRecGet>) {
        this.chatsList = chatsList
        notifyDataSetChanged()
    }

    inner class ChatListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val chatText = itemView.findViewById<TextView>(R.id.chat_name)
        private val consLay = itemView.findViewById<ConstraintLayout>(R.id.cons)
        fun setChatName(text: String){
            chatText.text = text
        }
        fun  setLis(chat: ChatRecGet){
            consLay.setOnClickListener{
                goToChat.onClick(chat)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ChatListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.chat_item, parent, false))
    }

    override fun getItemCount(): Int {
        return chatsList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val recHolder = holder as ChatListViewHolder
        recHolder.setChatName(chatsList[position].chatName)
        recHolder.setLis(chatsList[position])
    }
}