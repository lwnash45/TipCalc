package edu.washington.lwnash45.tipcalc

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import java.text.NumberFormat

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private var tipPercent = .15

    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        this.tipPercent = when (parent.getItemAtPosition(position).toString()) {
            "10%" -> .1
            "15%" -> .15
            "18%" -> .18
            else -> .2
        }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var amountText: EditText = findViewById(R.id.amountEditText)

        val addBtn: Button = findViewById(R.id.tipBtn)
        addBtn.isClickable= false

        val spinner: Spinner = findViewById(R.id.tipPercentSpinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.tip_percents,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = this


        amountText.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!amountText.text.isNullOrBlank()) {
                    amountText.removeTextChangedListener(this)
                }

                var amount = s.toString().replace("[$,.]".toRegex(), "").toDouble()
                var currency = NumberFormat.getCurrencyInstance().format(amount / 100)
                amountText.setText(currency)
                amountText.setSelection(currency.length)
                amountText.addTextChangedListener(this)
            }

            override fun afterTextChanged(p0: Editable?) {
                addBtn.isClickable = !amountText.text.isNullOrBlank()
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })



        addBtn.setOnClickListener {
            var amount: Double = amountText.text.toString().replace("[$,.]".toRegex(), "").toDouble()
            var currency = NumberFormat.getCurrencyInstance().format(amount / 100 * tipPercent)
            Toast.makeText(this, "Proper Tip: $currency", Toast.LENGTH_SHORT).show()
        }

    }
}
