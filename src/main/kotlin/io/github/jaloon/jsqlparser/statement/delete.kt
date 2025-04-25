package io.github.jaloon.jsqlparser.statement

import io.github.jaloon.jsqlparser.SqlContext
import io.github.jaloon.jsqlparser.SqlContextVisitor
import net.sf.jsqlparser.statement.delete.Delete

fun Delete.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}
