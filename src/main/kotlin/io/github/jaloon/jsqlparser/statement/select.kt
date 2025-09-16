package io.github.jaloon.jsqlparser.statement

import io.github.jaloon.jsqlparser.SqlContext
import io.github.jaloon.jsqlparser.SqlContextVisitor
import io.github.jaloon.jsqlparser.schema.accept
import net.sf.jsqlparser.expression.Expression
import net.sf.jsqlparser.schema.Table
import net.sf.jsqlparser.statement.ParenthesedStatement
import net.sf.jsqlparser.statement.piped.FromQuery
import net.sf.jsqlparser.statement.select.*

fun FromItem.accept(visitor: SqlContextVisitor, context: SqlContext) {
    when (this) {
        is ParenthesedFromItem -> accept(visitor, context)
        is Select              -> accept(visitor, context)
        is Table               -> accept(visitor, context)
        is TableFunction       -> accept(visitor, context)
    }
}

fun AllColumns.accept(visitor: SqlContextVisitor, context: SqlContext) {
    when (this) {
        is AllTableColumns    -> visitor.visit(this, context)
        is FunctionAllColumns -> visitor.visit(this, context)
        else                  -> visitor.visit(this, context)
    }
}

fun Distinct.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun Fetch.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun First.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun GroupByElement.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun Join.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun LateralView.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun Limit.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun Offset.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun OrderByElement.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun ParenthesedFromItem.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun Pivot.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun PivotXml.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun Select.accept(visitor: SqlContextVisitor, context: SqlContext) {
    when (this) {
        is FromQuery         -> visitor.visit(this, context)
        is LateralSubSelect  -> visitor.visit(this, context)
        is ParenthesedSelect -> visitor.visit(this, context)
        is PlainSelect       -> visitor.visit(this, context)
        is SetOperationList  -> visitor.visit(this, context)
        is TableStatement    -> visitor.visit(this, context)
        is Values            -> visitor.visit(this, context)
        else                 -> visitor.visit(this, context)
    }
}

fun SelectItem<out Expression?>.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun Skip.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun TableFunction.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun Top.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun UnPivot.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun WithItem<out ParenthesedStatement>.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}
