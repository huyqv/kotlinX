package com.huy.kotlin.base.arch

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/07/05
 * @Description: The class handle data sharing between fragments
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class SharingViewModel<T> : ViewModel() {

    private val sharedModel = MutableLiveData<T>()

    fun select(item: T) {
        sharedModel.value = item
    }

    fun getSelected(): LiveData<T> {
        return sharedModel
    }


    /**
     * EXAMPLE FOR DATA SHARING
     */
    class StringSharedViewModel : SharingViewModel<String>()

    class MasterFragment : Fragment(), View.OnClickListener {

        private lateinit var shareViewModel: StringSharedViewModel

        override fun onAttach(context: Context) {
            super.onAttach(context)
            activity?.run {
                shareViewModel = ViewModelProviders.of(this).get(StringSharedViewModel::class.java)
            }

        }

        override fun onClick(v: View?) {
            shareViewModel.select("Hello World")
        }
    }

    class DetailFragment : Fragment() {

        private lateinit var shareViewModel: StringSharedViewModel

        override fun onAttach(context: Context) {
            super.onAttach(context)
            activity?.run {
                shareViewModel = ViewModelProviders.of(this).get(StringSharedViewModel::class.java)
            }
        }

        override fun onStart() {
            super.onStart()
            shareViewModel.getSelected().observe(this, Observer<String> {
                print(it)
            })
        }
    }

}

