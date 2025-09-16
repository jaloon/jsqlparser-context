package io.github.jaloon.jsqlparser.test

import io.github.jaloon.jsqlparser.SqlContext
import io.github.jaloon.jsqlparser.SqlContextVisitorAdapter
import io.github.jaloon.jsqlparser.expression.SimpleExpression
import net.sf.jsqlparser.expression.UserVariable
import net.sf.jsqlparser.expression.operators.relational.*
import net.sf.jsqlparser.parser.CCJSqlParserUtil
import net.sf.jsqlparser.statement.DeclareStatement
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField

fun main() {
    val dateTimeRemover = DateTimeRemover()
    val sql = " select * from t where create_time > '2016-01-01' and create_time < '2016-01-02'"
    val statements = CCJSqlParserUtil.parseStatements(sql)
    dateTimeRemover.visit(statements) { }
    println(statements)

    val typeDefExpr = DeclareStatement.TypeDefExpr(
        UserVariable("date"),
        null,
        SimpleExpression("abc")
    )

    val property = DeclareStatement.TypeDefExpr::userVariable
    property.isAccessible = true
    property.javaField?.set(typeDefExpr, UserVariable("test"))
    println(typeDefExpr)
}

class DateTimeRemover : SqlContextVisitorAdapter() {
    override fun visit(greaterThan: GreaterThan?, context: SqlContext) {
        formatDateCondition(greaterThan, context)
    }

    override fun visit(greaterThanEquals: GreaterThanEquals?, context: SqlContext) {
        formatDateCondition(greaterThanEquals, context)
    }

    override fun visit(minorThan: MinorThan?, context: SqlContext) {
        formatDateCondition(minorThan, context)
    }

    override fun visit(minorThanEquals: MinorThanEquals?, context: SqlContext) {
        formatDateCondition(minorThanEquals, context)
    }

    private fun formatDateCondition(expr: ComparisonOperator?, context: SqlContext) {
        if (isDateCondition(expr)) {
            context.remove()
        } else {
            //            visitBinaryExpression(expr, context)
        }
    }

    private fun isDateCondition(expr: ComparisonOperator?): Boolean {
        return expr.toString().contains("create_time")
    }
}
