package io.github.jaloon.jsqlparser.test.visitor

import io.github.jaloon.jsqlparser.SqlContext
import net.sf.jsqlparser.schema.Table
import net.sf.jsqlparser.statement.piped.FromQuery
import net.sf.jsqlparser.statement.select.*

/**
 * @see FromItemVisitor
 */
interface FromItemContextVisitor {
    fun visit(tableName: Table?, context: SqlContext)
    fun visit(selectBody: ParenthesedSelect?, context: SqlContext)
    fun visit(lateralSubSelect: LateralSubSelect?, context: SqlContext)
    fun visit(tableFunction: TableFunction?, context: SqlContext)
    fun visit(parenthesedFromItem: ParenthesedFromItem?, context: SqlContext)
    fun visit(values: Values?, context: SqlContext)
    fun visit(plainSelect: PlainSelect?, context: SqlContext)
    fun visit(setOperationList: SetOperationList?, context: SqlContext)
    fun visit(tableStatement: TableStatement?, context: SqlContext)
    fun visit(fromQuery: FromQuery?, context: SqlContext)
}