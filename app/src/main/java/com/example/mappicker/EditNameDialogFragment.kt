package com.example.mappicker

import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment


class EditNameDialogFragment : DialogFragment() {
    private val mEditText: EditText? = null

    fun newInstance(title: String?): EditNameDialogFragment? {
        val frag : EditNameDialogFragment = EditNameDialogFragment()
        val args = Bundle()
        args.putString("title", title)
        frag.setArguments(args)
        return frag
    }
}