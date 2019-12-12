package com.example.todoappsandbox.ui.list

import android.graphics.Color
import android.graphics.Paint
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("changeEffect")
fun TextView.changeEffect(checked: Boolean) = apply {
    if (checked) {
        setTextColor(Color.LTGRAY)
        paint.flags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        paint.isAntiAlias = true
    } else {
        setTextColor(Color.BLACK)
        paint.flags = Paint.ANTI_ALIAS_FLAG
        paint.isAntiAlias = false
    }
}