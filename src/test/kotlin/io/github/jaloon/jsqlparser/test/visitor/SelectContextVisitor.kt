package io.github.jaloon.jsqlparser.test.visitor

import io.github.jaloon.jsqlparser.SqlContext
import net.sf.jsqlparser.statement.piped.FromQuery
import net.sf.jsqlparser.statement.select.*

/**
 * @see SelectVisitor
 */
interface SelectContextVisitor {
    fun visit(parenthesedSelect: ParenthesedSelect?, context: SqlContext)
    fun visit(plainSelect: PlainSelect?, context: SqlContext)
    fun visit(fromQuery: FromQuery?, context: SqlContext)
    fun visit(setOpList: SetOperationList?, context: SqlContext)
    fun visit(withItem: WithItem<*>?, context: SqlContext)
    fun visit(values: Values?, context: SqlContext)
    fun visit(lateralSubSelect: LateralSubSelect?, context: SqlContext)
    fun visit(tableStatement: TableStatement?, context: SqlContext)
}