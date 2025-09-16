package io.github.jaloon.jsqlparser.statement

import io.github.jaloon.jsqlparser.SqlContext
import io.github.jaloon.jsqlparser.SqlContextVisitor
import net.sf.jsqlparser.statement.refresh.RefreshMaterializedViewStatement

fun RefreshMaterializedViewStatement.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}