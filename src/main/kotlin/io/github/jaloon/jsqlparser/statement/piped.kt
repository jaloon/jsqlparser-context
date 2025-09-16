package io.github.jaloon.jsqlparser.statement

import io.github.jaloon.jsqlparser.SqlContext
import io.github.jaloon.jsqlparser.SqlContextVisitor
import net.sf.jsqlparser.statement.piped.AggregatePipeOperator
import net.sf.jsqlparser.statement.piped.AsPipeOperator
import net.sf.jsqlparser.statement.piped.CallPipeOperator
import net.sf.jsqlparser.statement.piped.DropPipeOperator
import net.sf.jsqlparser.statement.piped.ExtendPipeOperator
import net.sf.jsqlparser.statement.piped.FromQuery
import net.sf.jsqlparser.statement.piped.JoinPipeOperator
import net.sf.jsqlparser.statement.piped.LimitPipeOperator
import net.sf.jsqlparser.statement.piped.OrderByPipeOperator
import net.sf.jsqlparser.statement.piped.PipeOperator
import net.sf.jsqlparser.statement.piped.PivotPipeOperator
import net.sf.jsqlparser.statement.piped.RenamePipeOperator
import net.sf.jsqlparser.statement.piped.SelectPipeOperator
import net.sf.jsqlparser.statement.piped.SetOperationPipeOperator
import net.sf.jsqlparser.statement.piped.SetPipeOperator
import net.sf.jsqlparser.statement.piped.TableSamplePipeOperator
import net.sf.jsqlparser.statement.piped.UnPivotPipeOperator
import net.sf.jsqlparser.statement.piped.WherePipeOperator
import net.sf.jsqlparser.statement.piped.WindowPipeOperator

fun PipeOperator.accept(visitor: SqlContextVisitor, context: SqlContext) {
    when (this) {
        is AggregatePipeOperator    -> accept(visitor, context)
        is AsPipeOperator           -> accept(visitor, context)
        is CallPipeOperator         -> accept(visitor, context)
        is DropPipeOperator         -> accept(visitor, context)
        is JoinPipeOperator         -> accept(visitor, context)
        is LimitPipeOperator        -> accept(visitor, context)
        is OrderByPipeOperator      -> accept(visitor, context)
        is PivotPipeOperator        -> accept(visitor, context)
        is SelectPipeOperator       -> accept(visitor, context)
        is SetOperationPipeOperator -> accept(visitor, context)
        is SetPipeOperator          -> accept(visitor, context)
        is TableSamplePipeOperator  -> accept(visitor, context)
        is UnPivotPipeOperator      -> accept(visitor, context)
        is WherePipeOperator        -> accept(visitor, context)
    }
}

fun AggregatePipeOperator.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun AsPipeOperator.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun CallPipeOperator.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun DropPipeOperator.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun FromQuery.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun JoinPipeOperator.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun LimitPipeOperator.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun OrderByPipeOperator.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun PivotPipeOperator.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun SelectPipeOperator.accept(visitor: SqlContextVisitor, context: SqlContext) {
    when (this) {
        is ExtendPipeOperator -> visitor.visit(this, context)
        is RenamePipeOperator -> visitor.visit(this, context)
        is WindowPipeOperator -> visitor.visit(this, context)
        else                  -> visitor.visit(this, context)
    }
}

fun SetOperationPipeOperator.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun SetPipeOperator.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun TableSamplePipeOperator.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun UnPivotPipeOperator.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun WherePipeOperator.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}
