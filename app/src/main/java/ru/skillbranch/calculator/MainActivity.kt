package ru.skillbranch.calculator

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import kotlinx.android.synthetic.main.constraint.*


class MainActivity : AppCompatActivity(){
    private lateinit var inputEquation: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.constraint)
        setOnClickListeners()
        inputEquation = mutableListOf("")
        tvEquation.movementMethod = ScrollingMovementMethod()
    }

    private fun setOnClickListeners(){
        for(view in constraintLayout.children){
            view.setOnClickListener {
                addToInput((view as Button).text.toString())
            }
        }
        btnClearField.setOnClickListener {
            tvEquation.text = ""
            inputEquation = mutableListOf("")
            tvEquation.hint = ""
        }
        btnDeleteChar.setOnClickListener{
            if (inputEquation.last() != "") {
                tvEquation.text = tvEquation.text.dropLast(1)
                val str = inputEquation[inputEquation.lastIndex]
                inputEquation[inputEquation.lastIndex] = str.dropLast(1)
                if (inputEquation[inputEquation.lastIndex] == "") {
                    inputEquation.removeAt(inputEquation.lastIndex)
                }
            }
        }
        btnResult.setOnClickListener {
            if (inputEquation.last() != "") {
                tvAnswer.text = "= ${translateToRPN()}"
                tvEquation.hint = tvEquation.text
                tvEquation.text = ""
                inputEquation = mutableListOf("")
            }
        }
        eggView.setOnClickListener {
            Toast.makeText(this, getString(R.string.egg), Toast.LENGTH_SHORT).show()
        }
    }


    private fun addToInput(char: String) {
        tvEquation.append(char)
        when (char) {
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "." ->
                if (inputEquation.last().matches(Regex("([1-9]|[.])+"))) {
                    inputEquation[inputEquation.lastIndex] = inputEquation.last() + char
                } else inputEquation.add(char)
            btnSubmission.text.toString() ->
                if (inputEquation.last().matches(Regex("([1-9]|[.])+"))) {
                    inputEquation.add("-")
                } else inputEquation.add("~")
            btnSummary.text.toString() -> inputEquation.add("+")
            btnDivide.text.toString() -> inputEquation.add("/")
            btnMultiply.text.toString() -> inputEquation.add("@")
            btnBracketStart.text.toString() -> inputEquation.add("(")
            btnBracketEnd.text.toString() -> inputEquation.add(")")
        }

    }

    private fun translateToRPN(): Double {
        val operationStack = mutableListOf<String>()
        val equationInRPN = mutableListOf<String>()
        for (word in inputEquation) {
            if (word == "") {
                //just do nothing
            } else if (word.matches(Regex("([1-9]|[.])+"))) {
                equationInRPN.add(word)
            } else if (word == "~") {
                operationStack.add(word)
            } else if (word == "(") {
                operationStack.add(word)
            } else if (word == ")") {
                var operation = operationStack.last()
                while (operation != "(") {
                    equationInRPN.add(operation)
                    operationStack.removeAt(operationStack.lastIndex)
                    operation = operationStack.last()
                }
                operationStack.removeAt(operationStack.lastIndex)
            } else {
                var operation = operationStack.lastOrNull() ?: " "
                while (operation[0] > word[0] && operation != " ") {
                    equationInRPN.add(operation)
                    operationStack.removeAt(operationStack.lastIndex)
                    operation = operationStack.lastOrNull() ?: " "
                }
                operationStack.add(word)
            }
        }
        operationStack.reverse()
        for (operation in operationStack) {
            equationInRPN.add(operation)
        }
        return calculate(equationInRPN)
    }

    private fun calculate(input: MutableList<String>): Double {
        val workingStack = mutableListOf<Double>()
        for (word in input) {
            if (word.matches(Regex("([1-9]|[.])+"))) {
                workingStack.add(word.toDouble())
            } else if (word == "+") {
                val a =
                    workingStack[workingStack.lastIndex - 1] + workingStack[workingStack.lastIndex]
                workingStack.removeAt(workingStack.lastIndex)
                workingStack[workingStack.lastIndex] = a
            } else if (word == "@") {
                val a =
                    workingStack[workingStack.lastIndex - 1] * workingStack[workingStack.lastIndex]
                workingStack.removeAt(workingStack.lastIndex)
                workingStack[workingStack.lastIndex] = a
            } else if (word == "/") {
                val a =
                    workingStack[workingStack.lastIndex - 1] / workingStack[workingStack.lastIndex]
                workingStack.removeAt(workingStack.lastIndex)
                workingStack[workingStack.lastIndex] = a
            } else if (word == "-") {
                val a =
                    workingStack[workingStack.lastIndex - 1] - workingStack[workingStack.lastIndex]
                workingStack.removeAt(workingStack.lastIndex)
                workingStack[workingStack.lastIndex] = a
            } else if (word == "~") {
                val a = -workingStack[workingStack.lastIndex]
                workingStack[workingStack.lastIndex] = a
            }
        }
        return workingStack.last()
    }
}
