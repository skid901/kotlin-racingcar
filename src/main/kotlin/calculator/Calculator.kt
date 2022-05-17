package calculator

import calculator.operation.Addition
import calculator.operation.Division
import calculator.operation.Multiplication
import calculator.operation.NoMatch
import calculator.operation.Subtraction

object Calculator {

    private const val INITIAL_ACCUMULATOR = 0.0

    private val DELIMITER_REGEX = """\b""".toRegex()

    private val OPERATORS = listOf(Addition, Subtraction, Multiplication, Division)

    fun calculate(formula: String): Double {
        val operationUnits = ("${Addition.operator} $formula").split(DELIMITER_REGEX)
            .map { it.trim() }
            .filter { it.isNotBlank() }

        var accumulator = INITIAL_ACCUMULATOR
        repeat(countOperation(operationUnits.size)) {
            val (operator, operand) = extractOperationPair(it, operationUnits)
            accumulator = operate(operator, accumulator, operand)
        }
        return accumulator
    }

    private fun countOperation(it: Int) = it / 2

    private fun extractOperationPair(operationTurn: Int, operationUnits: List<String>): Pair<String, Double> {
        val index = operationTurn * 2
        val (operator, operand) = operationUnits.subList(index, index + 2)

        try {
            return Pair(operator, operand.toDouble())
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("연산 불가능한 피연산자가 입력되었습니다.: $operand")
        }
    }

    private fun operate(operator: String, leftOperand: Double, rightOperand: Double) = OPERATORS.plus(NoMatch(operator))
        .filter { it.match(operator) }[0]
        .operate(leftOperand, rightOperand)
}
