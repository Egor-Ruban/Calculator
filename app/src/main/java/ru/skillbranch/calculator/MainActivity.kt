package ru.skillbranch.calculator

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var inputEquation: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        inputEquation = mutableListOf("")
        tv_equation.movementMethod = ScrollingMovementMethod()
    }

    override fun onClick(p0: View?) {
        if (p0?.id == R.id.btn_operation_delete) {
            tv_equation.text = ""
            inputEquation = mutableListOf("")
            tv_equation.hint = ""
        } else if (p0?.id == btn_delete_char.id) {
            if (inputEquation.last() != "") {
                tv_equation.text = tv_equation.text.dropLast(1)
                val str = inputEquation[inputEquation.lastIndex]
                inputEquation[inputEquation.lastIndex] = str.dropLast(1)
                if (inputEquation[inputEquation.lastIndex] == "") {
                    inputEquation.removeAt(inputEquation.lastIndex)
                }
            }
        } else if (p0?.id == R.id.btn_result) {
            if (inputEquation.last() != "") {
                tv_answer.text = "= ${translateToRPN()}"
                tv_equation.hint = tv_equation.text
                tv_equation.text = ""
                inputEquation = mutableListOf("")
            }
        } else {
            addToInput((p0 as Button).text.toString())
        }
    }

    private fun addToInput(char: String) {
        tv_equation.append(char)
        when (char) {
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "." ->
                if (inputEquation.last().matches(Regex("([1-9]|[.])+"))) {
                    inputEquation[inputEquation.lastIndex] = inputEquation.last() + char
                } else inputEquation.add(char)
            btn_operation_sub.text.toString() ->
                if (inputEquation.last().matches(Regex("([1-9]|[.])+"))) {
                    inputEquation.add("-")
                } else inputEquation.add("~")
            btn_operation_sum.text.toString() -> inputEquation.add("+")
            btn_operation_divide.text.toString() -> inputEquation.add("/")
            btn_operation_mul.text.toString() -> inputEquation.add("@")
            btn_bracket_1.text.toString() -> inputEquation.add("(")
            btn_bracket_2.text.toString() -> inputEquation.add(")")
        }

    }

    private fun checkInput(): Boolean {
        return false
    }

    private fun translateToRPN(): Double {
        val operationStack = mutableListOf<String>()
        val equationInRPN = mutableListOf<String>()
        for (word in inputEquation) {
            if (word == "") {
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

    private fun init() {
        btn_number_one.setOnClickListener(this)
        btn_number_two.setOnClickListener(this)
        btn_number_three.setOnClickListener(this)
        btn_number_four.setOnClickListener(this)
        btn_number_five.setOnClickListener(this)
        btn_number_six.setOnClickListener(this)
        btn_number_seven.setOnClickListener(this)
        btn_number_eight.setOnClickListener(this)
        btn_number_nine.setOnClickListener(this)
        btn_number_zero.setOnClickListener(this)
        btn_operation_divide.setOnClickListener(this)
        btn_operation_mul.setOnClickListener(this)
        btn_operation_sub.setOnClickListener(this)
        btn_operation_sum.setOnClickListener(this)
        btn_operation_delete.setOnClickListener(this)
        btn_result.setOnClickListener(this)
        btn_number_dot.setOnClickListener(this)
        btn_bracket_1.setOnClickListener(this)
        btn_bracket_2.setOnClickListener(this)
        btn_delete_char.setOnClickListener(this)
    }

    fun onEggClick(v: View) {
        Toast.makeText(this, "there is nothing interesting", Toast.LENGTH_SHORT).show()
    }
}
