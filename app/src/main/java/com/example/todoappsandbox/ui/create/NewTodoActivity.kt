package com.example.todoappsandbox.ui.create

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.todoappsandbox.R
import com.example.todoappsandbox.databinding.ActivityNewTodoBinding
import com.example.todoappsandbox.di.ViewModelFactory
import com.example.todoappsandbox.utils.Consts
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class NewTodoActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var newTodoViewModelFactory: ViewModelFactory

    private lateinit var newTodoViewModel: NewTodoViewModel
    private lateinit var binding: ActivityNewTodoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_todo)

        newTodoViewModel = ViewModelProvider(
            this,
            newTodoViewModelFactory
        ).get(NewTodoViewModel::class.java)

        binding.apply {
            viewModel = newTodoViewModel
            lifecycleOwner = this@NewTodoActivity
        }

        newTodoViewModel.backToList.observe(this, Observer {
            val intent = Intent()
            intent.putExtra(Consts.INTENT, it)
            setResult(RESULT_OK, intent)
            finish()
        })

        title = if (newTodoViewModel.entity != null) "Edit" else "New"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.save, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> newTodoViewModel.saveTodo()
            else -> Unit
        }
        return true
    }
}
