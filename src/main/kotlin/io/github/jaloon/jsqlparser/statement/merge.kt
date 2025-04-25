package io.github.jaloon.jsqlparser.statement

import io.github.jaloon.jsqlparser.SqlContext
import io.github.jaloon.jsqlparser.SqlContextVisitor
import net.sf.jsqlparser.statement.merge.Merge
import net.sf.jsqlparser.statement.merge.MergeInsert
import net.sf.jsqlparser.statement.merge.MergeUpdate

fun Merge.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun MergeInsert.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun MergeUpdate.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}
