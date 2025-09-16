package io.github.jaloon.jsqlparser.test.visitor
import io.github.jaloon.jsqlparser.SqlContext
import net.sf.jsqlparser.statement.piped.FromQuery
import net.sf.jsqlparser.statement.piped.FromQueryVisitor
/**
 * @see FromQueryVisitor
 */
interface FromQueryContextVisitor {
    fun visit(fromQuery: FromQuery?, context: SqlContext)
}