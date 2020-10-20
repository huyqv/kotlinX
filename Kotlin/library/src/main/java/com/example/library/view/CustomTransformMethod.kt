package com.example.library.view

import android.text.method.PasswordTransformationMethod
import android.view.View

/**
 * EditText.setTransformationMethod(new CustomTransformMethod());
 */
class CustomTransformMethod(val visibleCount: Int = 1) : PasswordTransformationMethod() {

    companion object {
        private const val MASK1 = '•'
        private const val MASK2 = '●'
    }

    override fun getTransformation(source: CharSequence, view: View): CharSequence {
        return PasswordCharSequence(source)
    }

    private inner class PasswordCharSequence(private val source: CharSequence) : CharSequence {

        override val length: Int get() = source.length

        override fun get(index: Int): Char {
            return when {
                length < visibleCount -> return source[index]
                index < length - visibleCount -> '•'
                else -> source[index]
            }
        }

        override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
            return source.subSequence(startIndex, endIndex)
        }

    }

}