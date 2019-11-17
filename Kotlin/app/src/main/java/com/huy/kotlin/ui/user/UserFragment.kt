package com.huy.kotlin.ui.user

import android.os.Bundle
import com.huy.kotlin.R
import com.huy.kotlin.base.arch.ArchFragment
import kotlinx.android.synthetic.main.fragment_load_more.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/28
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class UserFragment : ArchFragment<UserVM>() {

    private val adapter = UserAdapter()
    override fun layoutResource() = R.layout.fragment_load_more
    override fun viewModelClass() = UserVM::class.java
    override fun onCreated(state: Bundle?) {
        adapter.bind(recyclerView, 3)
        adapter.onBindFooter { _, i -> viewModel.fetchUsers(i / 10 + 1) }
        onRegisterLiveData()
    }

    override fun onRegisterLiveData() {
        viewModel.fetchUsers(1)
        viewModel.userLiveData.observe { adapter.add(it) }
    }
}