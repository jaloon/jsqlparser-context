package io.github.jaloon.jsqlparser.test.visitor

import io.github.jaloon.jsqlparser.SqlContext
import net.sf.jsqlparser.schema.Table
import net.sf.jsqlparser.statement.select.IntoTableVisitor

/**
 * @see IntoTableVisitor
 */
interface IntoTableContextVisitor {
    fun visit(table: Table?, context: SqlContext)
}