package io.github.jaloon.jsqlparser

import io.github.jaloon.jsqlparser.statement.accept
import net.sf.jsqlparser.schema.Table
import net.sf.jsqlparser.statement.Statement
import net.sf.jsqlparser.statement.select.FromItem

/**
 *  表名访问器
 *
 *  @author jaloon
 *  @since 5.3
 */
class TableNameVisitor : SqlContextVisitorAdapter() {
    val tables: MutableList<Table> = mutableListOf()

    val tableNames: List<String>
        get() = tables.map { it.name }.distinct()

    override fun visit(table: Table?, context: SqlContext) {
        table ?: return
        tables.add(table)
    }

    companion object {
        @JvmStatic
        fun of(statement: Statement?) = TableNameVisitor().apply { statement?.accept(this, SqlContext.DO_NOTHING) }

        @JvmStatic
        fun of(fromItem: FromItem?) = TableNameVisitor().apply { fromItem?.accept(this, SqlContext.DO_NOTHING) }
    }

}