package io.github.jaloon.jsqlparser.statement

import io.github.jaloon.jsqlparser.SqlContext
import io.github.jaloon.jsqlparser.SqlContextVisitor
import net.sf.jsqlparser.statement.update.ParenthesedUpdate
import net.sf.jsqlparser.statement.update.Update
import net.sf.jsqlparser.statement.update.UpdateSet

fun Update.accept(visitor: SqlContextVisitor, context: SqlContext) {
    when (this) {
        is ParenthesedUpdate -> visitor.visit(this, context)
        else                 -> visitor.visit(this, context)
    }
}

fun UpdateSet.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}
