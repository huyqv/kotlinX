package com.kotlin.app.ui.base

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.sample.library.extension.realPathFromURI
import com.sample.library.extension.safeClose
import com.sample.library.extension.statusBarColor
import com.sample.widget.extension.backgroundColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException

abstract class BaseFragment<B : ViewBinding> : Fragment(), FragmentView {

    protected val bind: B by viewBinding(inflating())

    private var launcher: ActivityResultLauncher<Intent>? = null

    abstract fun inflating(): (LayoutInflater) -> B

    /**
     * [Fragment] implements
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = bind.root
        view.setOnTouchListener { _, _ -> true }
        statusBarColor(view.backgroundColor)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launcher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback { result ->
                val data: Intent = result?.data ?: return@ActivityResultCallback
                val uri: Uri = data.data ?: return@ActivityResultCallback
                val outputStream = ByteArrayOutputStream()
                try {
                    val path = realPathFromURI(uri)
                    val file = File(path)
                    val inputStream = FileInputStream(file)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    println("")
                } catch (ignore: IOException) {

                } finally {
                    outputStream.safeClose()
                }
            })

        onViewCreated()
        onLiveDataObserve()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        launcher?.unregister()
    }

    /**
     * [FragmentView] implements
     */
    override val fragment: Fragment get() = this

    override val backPressedCallback: OnBackPressedCallback = getBackPressCallBack()

    /**
     * [BaseFragment] properties
     */
    fun requestFocus(v: View?) {
        launch(240) { v?.requestFocus() }
    }

    fun launch(delayInterval: Long, block: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launch {
            delay(delayInterval)
            block()
        }
    }

    fun launch(block: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launch {
            block()
        }
    }

    fun inputModeAdjustResize() {
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

    }

    fun inputModeAdjustNothing() {
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
    }

}