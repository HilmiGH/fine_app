package com.example.fine

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.room.Room
import com.example.fine.databinding.ActivityDetailedBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetailedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailedBinding
    private lateinit var transaction: Transaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        transaction = intent.getSerializableExtra("transaction") as Transaction

        binding.labelInput.setText(transaction.label)
        binding.amountInput.setText(transaction.amount.toString())
        binding.descriptionInput.setText(transaction.description)

        binding.rootView.setOnClickListener {
            this.window.decorView.clearFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }

        binding.labelInput.doAfterTextChanged {
            binding.updateBtn.visibility = View.VISIBLE
            if (it != null && it.count() > 0) {
                binding.labelLayout.error = null
            }
        }

        binding.amountInput.doAfterTextChanged {
            binding.updateBtn.visibility = View.VISIBLE
            if (it != null && it.count() > 0) {
                binding.amountLayout.error = null
            }
        }

        binding.descriptionInput.doAfterTextChanged {
            binding.updateBtn.visibility = View.VISIBLE
        }

        binding.updateBtn.setOnClickListener {
            val label = binding.labelInput.text.toString()
            val description = binding.descriptionInput.text.toString()
            val amount = binding.amountInput.text.toString().toDoubleOrNull()

            if (label.isEmpty())
                binding.labelLayout.error = "Please enter a valid label"
            else if (amount == null)
                binding.amountLayout.error = "Please enter a valid amount"
            else {
                val updatedTransaction = Transaction(transaction.id, label, amount, description)
                update(updatedTransaction)
            }
        }

        binding.closeBtn.setOnClickListener {
            Log.d("AddTransactionActivity", "Close button clicked")
            finish()
        }
    }

    private fun update(transaction: Transaction) {
        val db = Room.databaseBuilder(this, AppDatabase::class.java, "transactions").build()

        GlobalScope.launch {
            db.transactionDao().update(transaction)
            finish()
        }
    }
}
