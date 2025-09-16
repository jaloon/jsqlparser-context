package io.github.jaloon.jsqlparser.test.visitor

import io.github.jaloon.jsqlparser.SqlContext
import net.sf.jsqlparser.statement.piped.*

/**
 * @see PipeOperatorVisitor
 */
interface PipeOperatorContextVisitor {
    fun visit(aggregate: AggregatePipeOperator?, context: SqlContext)
    fun visit(`as`: AsPipeOperator?, context: SqlContext)
    fun visit(call: CallPipeOperator?, context: SqlContext)
    fun visit(drop: DropPipeOperator?, context: SqlContext)
    fun visit(extend: ExtendPipeOperator?, context: SqlContext)
    fun visit(join: JoinPipeOperator?, context: SqlContext)
    fun visit(limit: LimitPipeOperator?, context: SqlContext)
    fun visit(orderBy: OrderByPipeOperator?, context: SqlContext)
    fun visit(pivot: PivotPipeOperator?, context: SqlContext)
    fun visit(rename: RenamePipeOperator?, context: SqlContext)
    fun visit(select: SelectPipeOperator?, context: SqlContext)
    fun visit(set: SetPipeOperator?, context: SqlContext)
    fun visit(tableSample: TableSamplePipeOperator?, context: SqlContext)
    fun visit(union: SetOperationPipeOperator?, context: SqlContext)
    fun visit(unPivot: UnPivotPipeOperator?, context: SqlContext)
    fun visit(where: WherePipeOperator?, context: SqlContext)
    fun visit(window: WindowPipeOperator?, context: SqlContext)
}