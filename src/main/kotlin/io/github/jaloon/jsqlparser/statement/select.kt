package io.github.jaloon.jsqlparser.statement

import io.github.jaloon.jsqlparser.schema.accept
import io.github.jaloon.jsqlparser.SqlContext
import io.github.jaloon.jsqlparser.SqlContextVisitor
import net.sf.jsqlparser.expression.operators.relational.NamedExpressionList
import net.sf.jsqlparser.schema.Table
import net.sf.jsqlparser.statement.select.*
import net.sf.jsqlparser.statement.values.ValuesStatement

fun SelectBody.accept(visitor: SqlContextVisitor, context: SqlContext) {
    when (this) {
        is PlainSelect -> accept(visitor, context)
        is SetOperationList -> accept(visitor, context)
        is ValuesStatement -> accept(visitor, context)
        is WithItem -> accept(visitor, context)
    }
}

fun SelectItem.accept(visitor: SqlContextVisitor, context: SqlContext) {
    when (this) {
        is AllColumns -> accept(visitor, context)
        is AllTableColumns -> accept(visitor, context)
        is SelectExpressionItem -> accept(visitor, context)
    }
}

fun FromItem.accept(visitor: SqlContextVisitor, context: SqlContext) {
    when (this) {
        is LateralSubSelect -> accept(visitor, context)
        is ParenthesisFromItem -> accept(visitor, context)
        is SubJoin -> accept(visitor, context)
        is SubSelect -> accept(visitor, context)
        is Table -> accept(visitor, context)
        is TableFunction -> accept(visitor, context)
        is ValuesList -> accept(visitor, context)
    }
}

fun FunctionItem.accept(visitor: SqlContextVisitor, context: SqlContext) {
    when (this) {
        is TableFunction -> accept(visitor, context)
    }
}

fun AllColumns.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun AllTableColumns.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun GroupByElement.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun LateralSubSelect.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun OrderByElement.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun ParenthesisFromItem.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun Pivot.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun PivotXml.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun PlainSelect.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun Select.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun SelectExpressionItem.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun SetOperationList.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun Join.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun SubJoin.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun SubSelect.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun TableFunction.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun UnPivot.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun ValuesList.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun WithItem.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun Limit.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun Offset.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun Distinct.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun ExpressionListItem.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}
