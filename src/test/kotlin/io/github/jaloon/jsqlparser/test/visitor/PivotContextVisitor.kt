package io.github.jaloon.jsqlparser.test.visitor

import io.github.jaloon.jsqlparser.SqlContext
import net.sf.jsqlparser.statement.select.Pivot
import net.sf.jsqlparser.statement.select.PivotVisitor
import net.sf.jsqlparser.statement.select.PivotXml
import net.sf.jsqlparser.statement.select.UnPivot

/**
 * @see PivotVisitor
 */
interface PivotContextVisitor {
    fun visit(pivot: Pivot?, context: SqlContext)
    fun visit(pivotXml: PivotXml?, context: SqlContext)
    fun visit(unpivot: UnPivot?, context: SqlContext)
}