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
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.sample.library.extension.realPathFromURI
import com.sample.library.extension.safeClose
import com.sample.library.extension.statusBarColor
import com.sample.library.util.Logger
import com.sample.widget.extension.backgroundColor
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException

abstract class BaseFragment<T : ViewBinding> : Fragment(), FragmentView {

    protected val log: Logger by lazy { Logger(this::class) }

    protected val bind: T by viewBinding(inflating())

    private var launcher: ActivityResultLauncher<Intent>? = null

    abstract fun inflating(): (LayoutInflater) -> ViewBinding

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
        log.d("onViewCreated")
        //launcher = getResultLauncher()
        onViewCreated()
        onLiveDataObserve()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        log.d("onDestroyView")
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
    fun inputModeAdjustResize() {
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

    }

    fun inputModeAdjustNothing() {
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
    }

    /**
     * Navigation
     */
    fun childNavigate(@IdRes actionId: Int, block: (NavigationBuilder.() -> Unit)? = null) {
        fragment.findNavController().navigate(actionId, block)
    }

    fun childNavigateUp() {
        fragment.findNavController().navigateUp()
    }

    fun mainNavigate(@IdRes actionId: Int, block: (NavigationBuilder.() -> Unit)? = null) {
        activityNavController()?.navigate(actionId, block)
    }

    fun mainNavigateUp() {
        activityNavController()?.navigateUp()
    }

    fun getResultLauncher(): ActivityResultLauncher<Intent> {
        return registerForActivityResult(
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
    }
}