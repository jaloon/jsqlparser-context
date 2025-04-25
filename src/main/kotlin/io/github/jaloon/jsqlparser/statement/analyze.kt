package io.github.jaloon.jsqlparser.statement

import io.github.jaloon.jsqlparser.SqlContext
import io.github.jaloon.jsqlparser.SqlContextVisitor
import net.sf.jsqlparser.statement.analyze.Analyze

fun Analyze.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}
