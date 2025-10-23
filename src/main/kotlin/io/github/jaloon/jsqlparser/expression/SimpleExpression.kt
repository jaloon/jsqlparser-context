package io.github.jaloon.jsqlparser.expression

import io.github.jaloon.jsqlparser.SqlContext
import io.github.jaloon.jsqlparser.SqlContextVisitor
import io.github.jaloon.jsqlparser.annotation.AllOpen
import net.sf.jsqlparser.expression.Expression
import net.sf.jsqlparser.expression.ExpressionVisitor
import net.sf.jsqlparser.parser.SimpleNode

@AllOpen
class SimpleExpression(var value: String?) : Expression {

    override fun <T : Any?, S : Any?> accept(expressionVisitor: ExpressionVisitor<T?>?, context: S?) = null

    fun accept(visitor: SqlContextVisitor, context: SqlContext) {
        visitor.visit(this, context)
    }

    override fun getASTNode() = null

    override fun setASTNode(node: SimpleNode) {}

    override fun toString() = value ?: ""

}