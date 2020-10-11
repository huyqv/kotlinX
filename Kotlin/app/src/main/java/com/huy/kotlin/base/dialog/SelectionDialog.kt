package com.huy.kotlin.base.dialog

import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.huy.kotlin.R
import kotlinx.android.synthetic.main.dialog_selection.view.*

/**
 * -------------------------------------------------------------------------------------------------
 *
 * @Project: AppName
 * @Created: Huy QV 2018/10/20
 * @Description: ...
 * None Right Reserved.
 * -------------------------------------------------------------------------------------------------
 */
abstract class SelectionDialog(activity: FragmentActivity?) : BaseAlertDialog(activity) {

    abstract val isSearchable: Boolean

    /**
     * [BaseAlertDialog] implement
     */
    override fun layoutRes(): Int {
        return R.layout.dialog_selection
    }

    override fun theme(): Int {
        return R.style.App_Dialog
    }

    override fun View.onViewCreated() {
    }

    fun adapter(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
        view?.dialogRecyclerView?.adapter = adapter
    }

}
