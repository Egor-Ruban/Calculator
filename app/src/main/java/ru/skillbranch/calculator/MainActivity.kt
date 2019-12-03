package ru.skillbranch.calculator

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var btnOne:Button
    private lateinit var btnTwo:Button
    private lateinit var btnThree:Button
    private lateinit var btnFour:Button
    private lateinit var btnFive:Button
    private lateinit var btnSix:Button
    private lateinit var btnSeven:Button
    private lateinit var btnEight:Button
    private lateinit var btnNine:Button
    private lateinit var btnZero:Button

    private lateinit var btnDivide:Button
    private lateinit var btnMul:Button
    private lateinit var btnSub:Button
    private lateinit var btnSum:Button

    private lateinit var btnDelete:Button

    private lateinit var tvEquation:TextView
    private lateinit var tvAnswer:TextView

    private lateinit var btnBracketFirst:Button
    private lateinit var btnBracketSecond:Button
    private lateinit var btnDeleteChar:ImageButton
    private lateinit var btnResult:Button
    private lateinit var btnDot:Button

    private lateinit var inputEquation:MutableList<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        tvEquation.text = savedInstanceState?.getString("EQUATION") ?: ""
        tvAnswer.text = savedInstanceState?.getString("ANSWER") ?: "= 0.0"
        inputEquation = mutableListOf("")
        tvEquation.movementMethod = ScrollingMovementMethod()
    }



    override fun onClick(p0: View?) {
        if(p0?.id==R.id.btn_operation_delete) {
            tvEquation.text = ""
            inputEquation = mutableListOf("")
        }else if(p0?.id==btnDeleteChar.id){
            if(inputEquation.last()!="") {
                tvEquation.text = tvEquation.text.dropLast(1)
                var str = inputEquation[inputEquation.lastIndex]
                inputEquation[inputEquation.lastIndex] = str.dropLast(1)
                if (inputEquation[inputEquation.lastIndex] == "") {
                    inputEquation.removeAt(inputEquation.lastIndex)
                }
            }
        }else if(p0?.id==R.id.btn_result) {
            tvAnswer.text = "= ${translateToRPN()}"
            tvEquation.hint = tvEquation.text
            tvEquation.text = ""
            inputEquation = mutableListOf("")
        }else{
            addToInput((p0 as Button).text.toString())
        }
    }

    private fun addToInput(char:String){
        tvEquation.append(char)
        when(char){
            "0","1","2","3","4","5","6","7","8","9","." ->
                                                if(inputEquation.last().matches(Regex("([1-9]|[.])+"))){
                                                    inputEquation[inputEquation.lastIndex] = inputEquation.last()+char
                                                } else  inputEquation.add(char)
            btnSub.text.toString() ->
                                                if(inputEquation.last().matches(Regex("([1-9]|[.])+"))){
                                                    inputEquation.add("-")
                                                } else  inputEquation.add("~")
            btnSum.text.toString()           -> inputEquation.add("+")
            btnDivide.text.toString()        -> inputEquation.add("/")
            btnMul.text.toString()           -> inputEquation.add("@")
            btnBracketFirst.text.toString()  -> inputEquation.add("(")
            btnBracketSecond.text.toString() -> inputEquation.add(")")
        }

    }

    private fun checkInput(equation:String):Boolean{
        //TODO return
        return false
    }

    private fun translateToRPN():Double{
        val operationStack = mutableListOf<String>()
        val equationInRPN = mutableListOf<String>()
        for(word in inputEquation){
            if(word==""){
            }
            else if(word.matches(Regex("([1-9]|[.])+"))){
                equationInRPN.add(word)
            }
            else if(word=="~"){
                operationStack.add(word)
            }
            else if(word=="("){
                operationStack.add(word)
            }
            else if(word==")"){
                var operation = operationStack.last()
                while(operation!="("){
                    equationInRPN.add(operation)
                    operationStack.removeAt(operationStack.lastIndex)
                    operation = operationStack.last()
                }
                operationStack.removeAt(operationStack.lastIndex)
            }
            else{
                var operation = operationStack.lastOrNull() ?: " "
                while(operation[0]>word[0] && operation!=" "){
                    equationInRPN.add(operation)
                    operationStack.removeAt(operationStack.lastIndex)
                    operation = operationStack.lastOrNull() ?: " "
                }
                operationStack.add(word)
            }
        }
        operationStack.reverse()
        for(operation in operationStack){
            equationInRPN.add(operation)
        }
        return calculate(equationInRPN)
    }

    private fun calculate(input:MutableList<String>):Double{
        val workingStack = mutableListOf<Double>()
        for(word in input){
            if(word.matches(Regex("([1-9]|[.])+"))){
                workingStack.add(word.toDouble())
            } else if(word=="+"){
                val a = workingStack[workingStack.lastIndex-1]+workingStack[workingStack.lastIndex]
                workingStack.removeAt(workingStack.lastIndex)
                workingStack[workingStack.lastIndex]=a
            } else if(word=="@"){
                val a = workingStack[workingStack.lastIndex-1]*workingStack[workingStack.lastIndex]
                workingStack.removeAt(workingStack.lastIndex)
                workingStack[workingStack.lastIndex]=a
            } else if(word=="/"){
                val a = workingStack[workingStack.lastIndex-1]/workingStack[workingStack.lastIndex]
                workingStack.removeAt(workingStack.lastIndex)
                workingStack[workingStack.lastIndex]=a
            } else if(word=="-"){
                val a = workingStack[workingStack.lastIndex-1]-workingStack[workingStack.lastIndex]
                workingStack.removeAt(workingStack.lastIndex)
                workingStack[workingStack.lastIndex]=a
            } else if(word=="~"){
                val a = -workingStack[workingStack.lastIndex]
                workingStack[workingStack.lastIndex]=a
            }
        }
        return workingStack.last()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString("EQUATION", tvEquation.text.toString())
        outState?.putString("ANSWER", tvAnswer.text.toString())
    }

    private fun init(){
        btnOne = btn_number_one
        btnTwo = btn_number_two
        btnThree = btn_number_three
        btnFour = btn_number_four
        btnFive = btn_number_five
        btnSix = btn_number_six
        btnSeven = btn_number_seven
        btnEight = btn_number_eight
        btnNine = btn_number_nine
        btnZero = btn_number_zero
        btnDivide = btn_operation_divide
        btnMul = btn_operation_mul
        btnSub = btn_operation_sub
        btnSum = btn_operation_sum
        btnDelete = btn_operation_delete

        tvEquation = tv_equation
        tvAnswer = tv_answer

        btnBracketFirst = btn_bracket_1
        btnBracketSecond = btn_bracket_2
        btnDeleteChar = btn_delete_char
        btnResult = btn_result
        btnDot = btn_number_dot

        btnOne.setOnClickListener(this)
        btnTwo.setOnClickListener(this)
        btnThree.setOnClickListener(this)
        btnFour.setOnClickListener(this)
        btnFive.setOnClickListener(this)
        btnSix.setOnClickListener(this)
        btnSeven.setOnClickListener(this)
        btnEight.setOnClickListener(this)
        btnNine.setOnClickListener(this)
        btnZero.setOnClickListener(this)
        btnDivide.setOnClickListener(this)
        btnMul.setOnClickListener(this)
        btnSub.setOnClickListener(this)
        btnSum.setOnClickListener(this)
        btnDelete.setOnClickListener(this)
        btnResult.setOnClickListener(this)
        btnDot.setOnClickListener(this)
        btnBracketFirst.setOnClickListener(this)
        btnBracketSecond.setOnClickListener(this)
        btnDeleteChar.setOnClickListener(this)
    }
}
