package io.github.jaloon.jsqlparser.test.visitor

import io.github.jaloon.jsqlparser.SqlContext
import net.sf.jsqlparser.expression.Expression
import net.sf.jsqlparser.statement.select.SelectItem
import net.sf.jsqlparser.statement.select.SelectItemVisitor

/**
 * @see SelectItemVisitor
 */
interface SelectItemContextVisitor {
    fun visit(selectItem: SelectItem<out Expression?>?, context: SqlContext)
}