package io.github.jaloon.jsqlparser.expression

import io.github.jaloon.jsqlparser.SqlContext
import io.github.jaloon.jsqlparser.SqlContextVisitor
import net.sf.jsqlparser.expression.Expression
import net.sf.jsqlparser.expression.ExpressionVisitor
import net.sf.jsqlparser.parser.ASTNodeAccessImpl

/**
 * PostgreSQL 字符串排序规则 COLLATE
 *
 * @since 5.3
 */
class PostgreCollate(var expression: Expression?, var collate: String) : ASTNodeAccessImpl(), Expression {

    constructor(expression: Expression?) : this(expression, "C")

    override fun <T : Any?, S : Any?> accept(expressionVisitor: ExpressionVisitor<T>?, context: S): T? {
        return expression?.accept(expressionVisitor, context)
    }

    fun accept(visitor: SqlContextVisitor, context: SqlContext) {
        visitor.visit(this, context)
    }

    override fun toString() = "$expression COLLATE \"$collate\""

}