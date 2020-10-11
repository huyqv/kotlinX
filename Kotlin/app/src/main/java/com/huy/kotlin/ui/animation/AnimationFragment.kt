package com.huy.kotlin.ui.animation

import com.huy.kotlin.R
import com.huy.kotlin.base.view.BaseFragment
import com.huy.library.extension.*
import kotlinx.android.synthetic.main.fragment_animation.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/07/12
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class AnimationFragment : BaseFragment() {

    private val itemList = listOf(
            "Translate X", "Translate Y", "Fade In", "Fade Out", "Center Scale",
            "Left Scale", "Right Scale", "Rotate", "Rotate Axis X", "Rotate Axis Y"
    )

    private val adapter = AnimationAdapter()

    override fun layoutResource(): Int {
        return R.layout.fragment_animation
    }

    override fun onViewCreated() {
        adapter.bind(recyclerView)
        adapter.set(itemList)
        adapter.onItemClick = { model, _ -> onAnimate(model) }
    }

    private fun onAnimate(animName: String) {
        val animation = when (animName) {
            "Translate X" -> animTranslateX(-viewKotlin.x, 0f)
            "Translate Y" -> animTranslateY(-viewKotlin.y, 0f)
            "Fade In" -> animFadeIn()
            "Fade Out" -> animFadeOut()
            "Center Scale" -> animCenterScale()
            "Left Scale" -> animLeftScale()
            "Right Scale" -> animRightScale()
            else -> null
        }
        if (animation != null) {
            viewKotlin.startAnimation(animation)
            return
        }
        when (animName) {
            "Rotate" -> animRotate(viewKotlin)
            "Rotate Axis X" -> animRotateAxisX(viewKotlin)
            "Rotate Axis Y" -> animRotateAxisY(viewKotlin)
            else -> null
        }?.start()

    }

}