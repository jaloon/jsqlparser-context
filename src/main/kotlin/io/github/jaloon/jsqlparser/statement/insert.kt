package io.github.jaloon.jsqlparser.statement

import io.github.jaloon.jsqlparser.SqlContext
import io.github.jaloon.jsqlparser.SqlContextVisitor
import net.sf.jsqlparser.statement.insert.Insert
import net.sf.jsqlparser.statement.insert.InsertConflictAction
import net.sf.jsqlparser.statement.insert.InsertConflictTarget
import net.sf.jsqlparser.statement.insert.ParenthesedInsert

fun Insert.accept(visitor: SqlContextVisitor, context: SqlContext) {
    when (this) {
        is ParenthesedInsert -> visitor.visit(this, context)
        else                 -> visitor.visit(this, context)
    }
}

fun InsertConflictAction.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun InsertConflictTarget.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}
