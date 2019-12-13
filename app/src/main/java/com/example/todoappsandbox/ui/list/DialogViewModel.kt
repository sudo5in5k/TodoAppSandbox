package com.example.todoappsandbox.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DialogViewModel: ViewModel() {

    /**
     * use theirs for [DeleteConfirmDialog]
     */
    val deleteConfirmYes = MutableLiveData<Boolean>()

}