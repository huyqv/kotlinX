package com.huy.library.view

import android.text.method.PasswordTransformationMethod
import android.view.View

/**
 * <EditText android:id="@+id/passWordEditText"
 * android:layout_width="match_parent"
 * android:layout_height="wrap_content"
 * android:gravity="center"
 * android:inputType="textPassword"></EditText>
 *
 * edittext.setTransformationMethod(new CustomTransformMethod());
 */
class CustomTransformMethod : PasswordTransformationMethod() {

    override fun getTransformation(source: CharSequence, view: View): CharSequence {
        return PasswordCharSequence(source)
    }

    private inner class PasswordCharSequence(private val mSource: CharSequence) : CharSequence {

        override val length: Int
            get() = mSource.length

        override fun get(index: Int): Char {
            return CIRLE
        }

        override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
            return mSource.subSequence(startIndex, endIndex)
        }
    }

    companion object {

        const val CIRLE = '●'
    }
}