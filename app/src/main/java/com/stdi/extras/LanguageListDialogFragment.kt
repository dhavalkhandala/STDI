package com.stdi.extras

import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatCheckedTextView
import com.stdi.R
import kotlinx.android.synthetic.main.fragment_language_list_dialog.*
import kotlinx.android.synthetic.main.fragment_language_list_dialog_item.view.*

class LanguageListDialogFragment : BottomSheetDialogFragment() {

    private var mListener: Listener? = null

    companion object {
        fun newInstance(): LanguageListDialogFragment = LanguageListDialogFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_language_list_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list.layoutManager = LinearLayoutManager(context)
        list.adapter = LanguageAdapter()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parent = parentFragment
        mListener = if (parent != null) parent as Listener else context as Listener
    }

    override fun onDetach() {
        mListener = null
        super.onDetach()
    }

    interface Listener {
        fun onLanguageClicked(position: Int)
    }

    private inner class ViewHolder internal constructor(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.fragment_language_list_dialog_item, parent, false)) {

        internal val text: AppCompatCheckedTextView = itemView.text

        init {
            text.setOnClickListener {
                mListener?.let {
                    it.onLanguageClicked(adapterPosition)
                    dismiss()
                }
            }
        }
    }

    private inner class LanguageAdapter internal constructor() :
        RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context), parent)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.text.isChecked = (AppUtils.defaultLangPos == position)
            holder.text.text = AppUtils.languageList[position].name
        }

        override fun getItemCount(): Int {
            return AppUtils.languageList.size
        }
    }

}
