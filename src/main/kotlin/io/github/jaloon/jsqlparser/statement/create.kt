package io.github.jaloon.jsqlparser.statement

import io.github.jaloon.jsqlparser.SqlContext
import io.github.jaloon.jsqlparser.SqlContextVisitor
import net.sf.jsqlparser.statement.create.index.CreateIndex
import net.sf.jsqlparser.statement.create.schema.CreateSchema
import net.sf.jsqlparser.statement.create.sequence.CreateSequence
import net.sf.jsqlparser.statement.create.synonym.CreateSynonym
import net.sf.jsqlparser.statement.create.table.ColumnDefinition
import net.sf.jsqlparser.statement.create.table.CreateTable
import net.sf.jsqlparser.statement.create.table.Index
import net.sf.jsqlparser.statement.create.view.AlterView
import net.sf.jsqlparser.statement.create.view.CreateView

fun CreateIndex.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun CreateSchema.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun CreateSequence.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun CreateSynonym.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun CreateTable.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun AlterView.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun CreateView.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun ColumnDefinition.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun Index.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}
