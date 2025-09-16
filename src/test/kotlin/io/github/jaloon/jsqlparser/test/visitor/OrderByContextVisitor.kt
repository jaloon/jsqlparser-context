package io.github.jaloon.jsqlparser.test.visitor

import io.github.jaloon.jsqlparser.SqlContext
import net.sf.jsqlparser.statement.select.OrderByElement
import net.sf.jsqlparser.statement.select.OrderByVisitor

/**
 * @see OrderByVisitor
 */
interface OrderByContextVisitor {
    fun visit(orderBy: OrderByElement?, context: SqlContext)
}