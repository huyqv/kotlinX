package com.kotlin.app.ui.dialog.selectable

import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.library.adapter.recycler.initLayoutManager
import com.example.library.extension.isGone
import com.example.library.extension.setNavResult
import com.kotlin.app.R
import com.example.library.ui.BaseDialog
import com.kotlin.app.ui.main.MainDialog
import kotlinx.android.synthetic.main.selectable.*


class SelectableFragment : MainDialog() {


    /**
     * [BaseDialog] override
     */
    override fun layoutResource(): Int {
        return R.layout.selectable
    }

    override fun onViewCreated() {
        addClickListener(selectableViewClose)
        selectableRecyclerView.initLayoutManager()
        selectableRecyclerView.addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
    }

    override fun onLiveDataObserve() {
        dialogVM.selectableLiveData.observe {
            if (null != it) {
                selectableTextViewTitle.text = it.title ?: getString(R.string.app_name)
                selectableEditText.isGone(!it.isSearchable)
                onBindListItem(it.adapter)
            } else {
                dismissAllowingStateLoss()
            }
        }
    }

    override fun onViewClick(view: View?) {
        when (view) {
            selectableViewClose -> dismiss()
        }
    }

    /**
     * [SelectableFragment] properties
     */
    private fun onBindListItem(adapter: SelectableAdapter<*>) {
        adapter.onItemClick = { _, position ->
            adapter.notifySelectionChanged(position)
            setNavResult(dialogVM.selectableLiveData.value?.key, position)
            findNavController().navigateUp()
        }
        selectableRecyclerView.adapter = adapter
    }

}