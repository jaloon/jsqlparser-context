package io.github.jaloon.jsqlparser.statement

import io.github.jaloon.jsqlparser.SqlContext
import io.github.jaloon.jsqlparser.SqlContextVisitor
import net.sf.jsqlparser.statement.*
import net.sf.jsqlparser.statement.alter.Alter
import net.sf.jsqlparser.statement.alter.AlterSession
import net.sf.jsqlparser.statement.alter.AlterSystemStatement
import net.sf.jsqlparser.statement.alter.RenameTableStatement
import net.sf.jsqlparser.statement.alter.sequence.AlterSequence
import net.sf.jsqlparser.statement.analyze.Analyze
import net.sf.jsqlparser.statement.comment.Comment
import net.sf.jsqlparser.statement.create.index.CreateIndex
import net.sf.jsqlparser.statement.create.schema.CreateSchema
import net.sf.jsqlparser.statement.create.sequence.CreateSequence
import net.sf.jsqlparser.statement.create.synonym.CreateSynonym
import net.sf.jsqlparser.statement.create.table.CreateTable
import net.sf.jsqlparser.statement.create.view.AlterView
import net.sf.jsqlparser.statement.create.view.CreateView
import net.sf.jsqlparser.statement.delete.Delete
import net.sf.jsqlparser.statement.drop.Drop
import net.sf.jsqlparser.statement.execute.Execute
import net.sf.jsqlparser.statement.grant.Grant
import net.sf.jsqlparser.statement.insert.Insert
import net.sf.jsqlparser.statement.merge.Merge
import net.sf.jsqlparser.statement.select.Select
import net.sf.jsqlparser.statement.show.ShowIndexStatement
import net.sf.jsqlparser.statement.show.ShowTablesStatement
import net.sf.jsqlparser.statement.truncate.Truncate
import net.sf.jsqlparser.statement.update.Update
import net.sf.jsqlparser.statement.upsert.Upsert
import net.sf.jsqlparser.statement.values.ValuesStatement

fun Statement.accept(visitor: SqlContextVisitor, context: SqlContext) {
    when (this) {
        is Alter -> accept(visitor, context)
        is AlterSequence -> accept(visitor, context)
        is AlterSession -> accept(visitor, context)
        is AlterSystemStatement -> accept(visitor, context)
        is AlterView -> accept(visitor, context)
        is Analyze -> accept(visitor, context)
        is Block -> accept(visitor, context)
        is Comment -> accept(visitor, context)
        is Commit -> accept(visitor, context)
        is CreateFunctionalStatement -> accept(visitor, context)
        is CreateIndex -> accept(visitor, context)
        is CreateSchema -> accept(visitor, context)
        is CreateSequence -> accept(visitor, context)
        is CreateSynonym -> accept(visitor, context)
        is CreateTable -> accept(visitor, context)
        is CreateView -> accept(visitor, context)
        is DeclareStatement -> accept(visitor, context)
        is Delete -> accept(visitor, context)
        is DescribeStatement -> accept(visitor, context)
        is Drop -> accept(visitor, context)
        is Execute -> accept(visitor, context)
        is ExplainStatement -> accept(visitor, context)
        is Grant -> accept(visitor, context)
        is IfElseStatement -> accept(visitor, context)
        is Insert -> accept(visitor, context)
        is Merge -> accept(visitor, context)
        is PurgeStatement -> accept(visitor, context)
        is RenameTableStatement -> accept(visitor, context)
        is ResetStatement -> accept(visitor, context)
        is RollbackStatement -> accept(visitor, context)
        is SavepointStatement -> accept(visitor, context)
        is Select -> accept(visitor, context)
        is SetStatement -> accept(visitor, context)
        is ShowColumnsStatement -> accept(visitor, context)
        is ShowIndexStatement -> accept(visitor, context)
        is ShowStatement -> accept(visitor, context)
        is ShowTablesStatement -> accept(visitor, context)
        is Truncate -> accept(visitor, context)
        is UnsupportedStatement -> accept(visitor, context)
        is Update -> accept(visitor, context)
        is Upsert -> accept(visitor, context)
        is UseStatement -> accept(visitor, context)
        is ValuesStatement -> accept(visitor, context)
    }
}

fun Block.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun Commit.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun CreateFunctionalStatement.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun DeclareStatement.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun DescribeStatement.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun ExplainStatement.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun IfElseStatement.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun PurgeStatement.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun ResetStatement.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun RollbackStatement.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun SavepointStatement.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun SetStatement.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun ShowColumnsStatement.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun ShowStatement.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun Statements.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun UnsupportedStatement.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun UseStatement.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun OutputClause.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}
