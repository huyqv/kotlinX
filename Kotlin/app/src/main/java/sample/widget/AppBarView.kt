package sample.widget

import android.app.Activity
import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import com.example.library.extension.getSize
import com.example.library.extension.statusBarHeight
import com.example.library.widget.AppCustomView
import com.kotlin.app.R
import kotlinx.android.synthetic.main.widget_app_bar.view.*

class AppBarView : AppCustomView {

    /**
     * [AppCustomView] override
     */
    override fun layoutResource(): Int {
        return R.layout.widget_app_bar
    }

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs)

    override fun onInitialize(context: Context, types: TypedArray) {

        appBarImageViewBackground.setImageResource(types.background)

        textViewTitle.also {
            it.text = types.text
            it.isAllCaps = types.textAllCaps
            it.setTypeface(textViewTitle.typeface, it.typeface.style)
            it.setTextColor(types.textColor)
        }

        imageViewDrawableStart.also {
            it.setColorFilter(types.textColor)
            it.setImageDrawable(types.drawableStart)
        }

        imageViewDrawableEnd.also {
            it.setColorFilter(types.textColor)
            it.setImageDrawable(types.drawableEnd)
        }

    }

    /**
     * [AppBarView] properties
     */
    var title: String? = null
        set(value) {
            textViewTitle.text = value
        }

    var backgroundImage: Drawable? = null
        set(value) {
            appBarImageViewBackground.setImageDrawable(value)
        }

    fun startButtonClickListener(block: () -> Unit) {
        imageViewDrawableStart.setOnClickListener { block() }
    }

    fun endButtonClickListener(block: () -> Unit) {
        imageViewDrawableEnd.setOnClickListener { block() }
    }

    var drawableStart: Drawable? = null
        set(value) {
            imageViewDrawableStart.setImageDrawable(value)
        }

    var drawableEnd: Drawable? = null
        set(value) {
            imageViewDrawableEnd.setImageDrawable(value)
        }

    var progressVisible: Boolean
        get() = appBarProgressBar.visibility == View.VISIBLE
        set(value) {
            appBarProgressBar.visibility = if (value) View.VISIBLE else View.INVISIBLE
        }

    fun addPopBackClick() {
        imageViewDrawableStart.apply {
            post {
                setImageResource(R.drawable.ic_back)
                setOnClickListener { v ->
                    (v.context as? Activity)?.onBackPressed()
                }
            }
        }
    }

    fun updateStatusBar() {
        this.getSize { _, h ->
            val extraHeight = statusBarHeight
            appBarViewControls.setPadding(0, extraHeight, 0, 0)
            val lp = this.layoutParams
            lp.height = h + extraHeight
            this.layoutParams = lp
            appBarViewControls.invalidate()
        }
    }

}
