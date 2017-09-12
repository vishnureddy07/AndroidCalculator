package com.example.vishnureddy.calculator

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {
    // IDs of all the numeric buttons
    private val numericButtons = intArrayOf(R.id.buttonZero, R.id.buttonOne, R.id.buttonTwo, R.id.buttonThree, R.id.buttonFour, R.id.buttonFive, R.id.buttonSix, R.id.buttonSeven, R.id.buttonEight, R.id.buttonNine)
    // IDs of all the operator buttons
    private val operatorButtons = intArrayOf(R.id.buttonAdd, R.id.buttonSub, R.id.buttonMultiply, R.id.buttonDivide)
    // TextView used to display the output
    private var txtScreen: TextView? = null
    // Represent whether the lastly pressed key is numeric or not
    private var lastNumeric: Boolean = false
    // Represent that current state is in error or not
    private var stateError: Boolean = false
    // If true, do not allow to add another DOT
    private var lastDot: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.txtScreen = findViewById(R.id.textScreen) as TextView
        // Find and set OnClickListener to numeric buttons
        setNumericOnClickListener()
        // Find and set OnClickListener to operator buttons, equal button and decimal point button
        setOperatorOnClickListener()
    }

    private fun setNumericOnClickListener() {
        // Create a common OnClickListener
        val listener = View.OnClickListener { v ->
            // Just append/set the text of clicked button
            val button = v as Button
            if (stateError) {
                // If current state is Error, replace the error message
                txtScreen!!.text = button.text
                stateError = false
            } else {
                // If not, already there is a valid expression so append to it
                txtScreen!!.append(button.text)
            }
            // Set the flag
            lastNumeric = true
        }
        // Assign the listener to all the numeric buttons
        for (id in numericButtons) {
            findViewById(id).setOnClickListener(listener)
        }
    }

    private fun setOperatorOnClickListener() {
        // Create a common OnClickListener for operators
        val listener = View.OnClickListener { v ->
            // If the current state is Error do not append the operator
            // If the last input is number only, append the operator
            if (lastNumeric && !stateError) {
                val button = v as Button
                txtScreen!!.append(button.text)
                lastNumeric = false
                lastDot = false    // Reset the DOT flag
            }
        }
        // Assign the listener to all the operator buttons
        for (id in operatorButtons) {
            findViewById(id).setOnClickListener(listener)
        }

        // Decimal point
        findViewById(R.id.buttonDot).setOnClickListener {
            if (lastNumeric && !stateError && !lastDot) {
                txtScreen!!.append(".")
                lastNumeric = false
                lastDot = true
            }
        }
        // Clear button
        findViewById(R.id.buttonClear).setOnClickListener {
            txtScreen!!.text = ""  // Clear the screen
            // Reset all the states and flags
            lastNumeric = false
            stateError = false
            lastDot = false
        }
        // Equal button
        findViewById(R.id.buttonEqual).setOnClickListener { onEqual() }
    }

    /**
     * Logic to calculate the solution.
     */

    private fun onEqual() {
        // If the current state is error, nothing to do.
        // If the last input is a number only, solution can be found.
        if (lastNumeric && !stateError) {
            // Read the expression
            val txt = txtScreen!!.text.toString()
            // Create an Expression (A class from exp4j library)
            val expression = ExpressionBuilder(txt).build()
            try {
                // Calculate the result and display
                val result = expression.evaluate()
                txtScreen!!.text = java.lang.Double.toString(result)
                lastDot = true // Result contains a dot
            } catch (ex: ArithmeticException) {
                // Display an error message
                txtScreen!!.text = "Error"
                stateError = true
                lastNumeric = false
            }

        }
    }
}

