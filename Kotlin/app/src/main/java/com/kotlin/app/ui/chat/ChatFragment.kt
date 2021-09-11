package com.kotlin.app.ui.chat


import android.view.LayoutInflater
import android.view.View
import com.kotlin.app.databinding.ChatBinding
import com.kotlin.app.ui.base.BaseFragment


class ChatFragment : BaseFragment<ChatBinding>() {

    override fun inflating(): (LayoutInflater) -> ChatBinding {
        return ChatBinding::inflate
    }

    override fun onViewCreated() {
        addClickListener(bind.viewSend, bind.textView1, bind.textView2)
    }

    override fun onLiveDataObserve() {
    }

    override fun onViewClick(v: View?) {
        when (v) {
            bind.viewSend -> {
                bind.editTextMessage.text = null
            }
            bind.textView1 -> {
                bind.editTextMessage.setText("lorem")
            }
            bind.textView2 -> {
                bind.editTextMessage.setText("It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout.")
            }
        }
    }

}