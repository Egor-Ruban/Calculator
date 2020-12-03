package ru.tsu.calculator

import android.util.Log

object Calc {
    const val OPERATION_ADD = "+"
    const val OPERATION_SUB = "-"
    const val OPERATION_MUL = "@"
    const val OPERATION_DIV = "/"
    const val BRACKET_LEFT = "("
    const val BRACKET_RIGHT = ")"
    const val OPERATION_NEG = "~"

    private fun translateToRPN(inputEquation : MutableList<String>): MutableList<String> {
        val operationStack = mutableListOf<String>()
        val equationInRPN = mutableListOf<String>()
        for (word in inputEquation) {
            if (word.matches(Regex("([0-9]|[.])+"))) {
                equationInRPN.add(word)
            } else {
                when (word) {
                    "" -> {} //just do nothing, that`s ok. go next
                    OPERATION_NEG -> operationStack.add(word)
                    BRACKET_LEFT -> operationStack.add(word)
                    BRACKET_RIGHT -> {
                        var operation = operationStack.last()
                        while (operation != BRACKET_LEFT) {
                            equationInRPN.add(operation)
                            operationStack.removeAt(operationStack.lastIndex)
                            operation = operationStack.last()
                        }
                        operationStack.removeAt(operationStack.lastIndex)
                    }
                    else -> {
                        var operation = operationStack.lastOrNull() ?: " "
                        while (operation[0] > word[0] && operation != " ") {
                            equationInRPN.add(operation)
                            operationStack.removeAt(operationStack.lastIndex)
                            operation = operationStack.lastOrNull() ?: " "
                        }
                        operationStack.add(word)
                    }
                }
            }
        }
        operationStack.reverse()
        for (operation in operationStack) {
            equationInRPN.add(operation)
        }
        return equationInRPN
    }

    fun calculate(input : MutableList<String>): Double {
        val equationInRPN = translateToRPN(input)
        val workingStack = mutableListOf<Double>()
        for (word in equationInRPN) {
            if (word.matches(Regex("([0-9]|[.])+"))) {
                workingStack.add(word.toDouble())
            } else {
                when(word) {
                    OPERATION_ADD -> {
                        val a =
                                workingStack[workingStack.lastIndex - 1] + workingStack[workingStack.lastIndex]
                        workingStack.removeAt(workingStack.lastIndex)
                        workingStack[workingStack.lastIndex] = a
                    }
                    OPERATION_MUL -> {
                        val a =
                                workingStack[workingStack.lastIndex - 1] * workingStack[workingStack.lastIndex]
                        workingStack.removeAt(workingStack.lastIndex)
                        workingStack[workingStack.lastIndex] = a
                    }
                    OPERATION_DIV -> {
                        val a =
                                workingStack[workingStack.lastIndex - 1] / workingStack[workingStack.lastIndex]
                        workingStack.removeAt(workingStack.lastIndex)
                        workingStack[workingStack.lastIndex] = a
                    }
                    OPERATION_SUB -> {
                        val a =
                                workingStack[workingStack.lastIndex - 1] - workingStack[workingStack.lastIndex]
                        workingStack.removeAt(workingStack.lastIndex)
                        workingStack[workingStack.lastIndex] = a
                    }
                    OPERATION_NEG -> {
                        val a = -workingStack[workingStack.lastIndex]
                        workingStack[workingStack.lastIndex] = a
                    }
                }
            }
        }
        return workingStack.last()
    }
}