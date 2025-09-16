package io.github.jaloon.jsqlparser.test.visitor

import io.github.jaloon.jsqlparser.SqlContext
import net.sf.jsqlparser.statement.select.GroupByElement
import net.sf.jsqlparser.statement.select.GroupByVisitor

/**
 * @see GroupByVisitor
 */
interface GroupByContextVisitor {
    fun visit(groupBy: GroupByElement?, context: SqlContext)
}