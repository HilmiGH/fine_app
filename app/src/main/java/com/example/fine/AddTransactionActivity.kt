package com.example.fine

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.room.Room
import com.example.fine.databinding.ActivityAddTransactionBinding
import com.example.fine.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddTransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddTransactionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddTransactionBinding.inflate(layoutInflater)
//        setContentView(R.layout.activity_add_transaction)
        setContentView(binding.root)

        binding.labelInput.doAfterTextChanged {
            if (it != null && it.count() > 0) {
                binding.labelLayout.error = null
            }
        }

        binding.labelInput.doAfterTextChanged {
            if (it != null && it.count() > 0) {
                binding.amountLayout.error = null
            }
        }

//        binding.labelInput.addTextChangedListener{
//            if(it.count() > 0)
//        }

        binding.addTransactionBtn.setOnClickListener{
            val label = binding.labelInput.text.toString()
            val description = binding.descriptionInput.text.toString()
            val amount = binding.amountInput.text.toString().toDoubleOrNull()

            if(label.isEmpty())
                binding.labelLayout.error = "please enter a valid label"

            else if (amount == null)
                binding.amountLayout.error = "Please enter a valid amount"
            else {
                val  transaction = Transaction(0, label, amount, description)
                insert(transaction)
            }
        }

        binding.closeBtn.setOnClickListener{
            Log.d("AddTransactionActivity", "Close button clicked")
            finish()
        }
    }

    private fun insert(transaction: Transaction){
        val db = Room.databaseBuilder(this,
            AppDatabase::class.java,
            "transactions").build()

        GlobalScope.launch {
            db.transactionDao().insertAll(transaction)
            finish()
        }
    }
}