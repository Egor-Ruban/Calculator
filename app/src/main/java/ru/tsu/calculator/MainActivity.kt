package ru.tsu.calculator

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import kotlinx.android.synthetic.main.constraint.*
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.constraint)
        setOnClickListeners()
        tvEquation.movementMethod = ScrollingMovementMethod()
    }

    private fun setOnClickListeners() {
        var inputEquation = mutableListOf("")
        for (view in layout_buttons.children) {
            view.setOnClickListener {
                addToInput(inputEquation, (view as TextView).text.toString())
            }
        }

        btnClearField.setOnClickListener {
            tvEquation.text = ""
            inputEquation = mutableListOf("")
            tvEquation.hint = ""
        }

        with(inputEquation){
            btnDeleteChar.setOnClickListener {
                if (last() != "") {
                    tvEquation.text = tvEquation.text.dropLast(1)
                    inputEquation[lastIndex] = inputEquation[lastIndex].dropLast(1)
                    if (inputEquation[lastIndex] == "") {
                        inputEquation.removeAt(lastIndex)
                    }
                }
            }
        }

        btnResult.setOnClickListener {
            if (inputEquation.last() != "") {
                val resultOfEquation = Calc.calculate(inputEquation)
                if(resultOfEquation > 999999999.99){
                    tvAnswer.text = "too much"
                } else {
                    tvAnswer.text = getString(R.string.result, resultOfEquation)
                }
                    tvEquation.hint = tvEquation.text
                    tvEquation.text = ""
                    inputEquation = mutableListOf("")
            }
        }
    }

    private fun addToInput(inputEquation: MutableList<String>, char: String) {
        tvEquation.append(char)
        when (char) {
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "." ->
                if (inputEquation.last().matches(Regex("([0-9]|[.])+"))) {
                    inputEquation[inputEquation.lastIndex] = inputEquation.last() + char
                } else inputEquation.add(char)
            btnSubmission.text.toString() ->
                if (inputEquation.last().matches(Regex("([0-9]|[.])+"))) {
                    inputEquation.add(Calc.OPERATION_SUB)
                } else inputEquation.add(Calc.OPERATION_NEG)
            btnSummary.text.toString() -> inputEquation.add(Calc.OPERATION_ADD)
            btnDivide.text.toString() -> inputEquation.add(Calc.OPERATION_DIV)
            btnMultiply.text.toString() -> inputEquation.add(Calc.OPERATION_MUL)
            btnBracketStart.text.toString() -> inputEquation.add(Calc.BRACKET_LEFT)
            btnBracketEnd.text.toString() -> inputEquation.add(Calc.BRACKET_RIGHT)
        }
    }
}
