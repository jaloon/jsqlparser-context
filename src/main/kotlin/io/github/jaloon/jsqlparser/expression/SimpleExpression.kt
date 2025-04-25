package io.github.jaloon.jsqlparser.expression

import io.github.jaloon.jsqlparser.SqlContext
import io.github.jaloon.jsqlparser.SqlContextVisitor
import net.sf.jsqlparser.expression.Expression
import net.sf.jsqlparser.expression.ExpressionVisitor
import net.sf.jsqlparser.parser.SimpleNode

class SimpleExpression(var value: String?) : Expression {

    override fun accept(expressionVisitor: ExpressionVisitor) {}

    fun accept(visitor: SqlContextVisitor, context: SqlContext) {
        visitor.visit(this, context)
    }

    override fun getASTNode(): SimpleNode? {
        return null
    }

    override fun setASTNode(node: SimpleNode) {}

    override fun toString(): String {
        return value ?: ""
    }
}