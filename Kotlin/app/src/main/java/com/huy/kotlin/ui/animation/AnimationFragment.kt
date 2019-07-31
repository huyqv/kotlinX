package com.huy.kotlin.ui.animation

import android.os.Bundle
import android.view.View
import com.huy.kotlin.R
import com.huy.kotlin.base.view.BaseFragment
import com.huy.library.extension.*
import kotlinx.android.synthetic.main.fragment_animation.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/07/12
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class AnimationFragment : BaseFragment() {

    private val itemList = listOf(
            "Translate X", "Translate Y", "Fade In", "Fade Out", "Center Scale",
            "Left Scale", "Right Scale", "Rotate", "Rotate Axis X", "Rotate Axis Y"
    )
    private val adapter = AnimationAdapter()

    override fun layoutResource() = R.layout.fragment_animation

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.bind(recyclerView)
        adapter.set(itemList)
        adapter.onItemClick { model, _ -> onAnimate(model) }
    }


    private fun onAnimate(animName: String) {
        val animation = when (animName) {
            "Translate X" -> translateXAnim(-viewKotlin.x, 0f)
            "Translate Y" -> translateYAnim(-viewKotlin.y, 0f)
            "Fade In" -> fadeInAnim()
            "Fade Out" -> fadeOutAnim()
            "Center Scale" -> centerScaleAnim()
            "Left Scale" -> leftScaleAnim()
            "Right Scale" -> rightScaleAnim()
            else -> null
        }
        if (animation != null) {
            viewKotlin.startAnimation(animation)
            return
        }
        when (animName) {
            "Rotate" -> rotateAnimator(viewKotlin)
            "Rotate Axis X" -> rotateAxisXAnimator(viewKotlin)
            "Rotate Axis Y" -> rotateAxisYAnimator(viewKotlin)
            else -> null
        }?.start()

    }
}