package com.huy.kotlin.ui.message

import android.os.Bundle
import com.huy.kotlin.R
import com.huy.kotlin.base.arch.ArchFragment
import com.huy.kotlin.ui.image_owner.ImageOwnerFragment
import kotlinx.android.synthetic.main.fragment_chat.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/28
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class MessageFragment : ArchFragment<MessageVM>() {

    private val adapter = MessageAdapter()

    override fun layoutResource() = R.layout.fragment_chat

    override fun viewModelClass() = MessageVM::class.java

    override fun onCreated(state: Bundle?) {

        adapter.bind(recyclerView) {
            reverseLayout = true
        }

        adapter.onItemClick { message, _ ->
            if (message.image != null) {
                add(ImageOwnerFragment.newInstance(message))
            }
        }
        adapter.myId = 16
    }

    override fun onRegisterLiveData() {
        viewModel.messageLiveData.observe {
            if (adapter.dataIsEmpty()) {
                adapter.submitList(it)
                recyclerView.scrollToPosition(0)
            } else {
                adapter.submitList(it)
            }
        }
    }

}