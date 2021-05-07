package com.kotlin.app.ui.selectable

import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.app.R
import com.example.library.extension.setNavResult
import com.kotlin.app.ui.base.BaseDialog
import com.example.library.adapter.recycler.initLayoutManager
import com.example.library.extension.isGone
import kotlinx.android.synthetic.main.selectable.*


class SelectableFragment : BaseDialog() {

    private val vm: SelectableVM by lazy {
        activityVM(SelectableVM::class)
    }

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
        vm.argLiveData.observe {
            selectableTextViewTitle.text = it.title ?: getString(R.string.app_name)
            selectableEditText.isGone(!it.isSearchable)
            onBindListItem(it.adapter)
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
            setNavResult(vm.arg?.key, position)
            findNavController().navigateUp()
        }
        selectableRecyclerView.adapter = adapter
    }

}