package io.github.jaloon.jsqlparser.schema

import io.github.jaloon.jsqlparser.SqlContext
import io.github.jaloon.jsqlparser.SqlContextVisitor
import net.sf.jsqlparser.schema.*

fun Column.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun Table.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}
