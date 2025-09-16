package io.github.jaloon.jsqlparser.test.visitor

import io.github.jaloon.jsqlparser.SqlContext
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
import net.sf.jsqlparser.statement.delete.ParenthesedDelete
import net.sf.jsqlparser.statement.drop.Drop
import net.sf.jsqlparser.statement.execute.Execute
import net.sf.jsqlparser.statement.grant.Grant
import net.sf.jsqlparser.statement.insert.Insert
import net.sf.jsqlparser.statement.insert.ParenthesedInsert
import net.sf.jsqlparser.statement.merge.Merge
import net.sf.jsqlparser.statement.refresh.RefreshMaterializedViewStatement
import net.sf.jsqlparser.statement.select.Select
import net.sf.jsqlparser.statement.show.ShowIndexStatement
import net.sf.jsqlparser.statement.show.ShowTablesStatement
import net.sf.jsqlparser.statement.truncate.Truncate
import net.sf.jsqlparser.statement.update.ParenthesedUpdate
import net.sf.jsqlparser.statement.update.Update
import net.sf.jsqlparser.statement.upsert.Upsert

/**
 * @see StatementVisitor
 */
interface StatementContextVisitor {
    fun visit(analyze: Analyze?, context: SqlContext)
    fun visit(savepointStatement: SavepointStatement?, context: SqlContext)
    fun visit(rollbackStatement: RollbackStatement?, context: SqlContext)
    fun visit(comment: Comment?, context: SqlContext)
    fun visit(commit: Commit?, context: SqlContext)
    fun visit(delete: Delete?, context: SqlContext)
    fun visit(update: Update?, context: SqlContext)
    fun visit(insert: Insert?, context: SqlContext)
    fun visit(drop: Drop?, context: SqlContext)
    fun visit(truncate: Truncate?, context: SqlContext)
    fun visit(createIndex: CreateIndex?, context: SqlContext)
    fun visit(createSchema: CreateSchema?, context: SqlContext)
    fun visit(createTable: CreateTable?, context: SqlContext)
    fun visit(createView: CreateView?, context: SqlContext)
    fun visit(alterView: AlterView?, context: SqlContext)
    fun visit(materializedView: RefreshMaterializedViewStatement?, context: SqlContext)
    fun visit(alter: Alter?, context: SqlContext)
    fun visit(statements: Statements?, context: SqlContext)
    fun visit(execute: Execute?, context: SqlContext)
    fun visit(set: SetStatement?, context: SqlContext)
    fun visit(reset: ResetStatement?, context: SqlContext)
    fun visit(showColumns: ShowColumnsStatement?, context: SqlContext)
    fun visit(showIndex: ShowIndexStatement?, context: SqlContext)
    fun visit(showTables: ShowTablesStatement?, context: SqlContext)
    fun visit(merge: Merge?, context: SqlContext)
    fun visit(select: Select?, context: SqlContext)
    fun visit(upsert: Upsert?, context: SqlContext)
    fun visit(use: UseStatement?, context: SqlContext)
    fun visit(block: Block?, context: SqlContext)
    fun visit(describe: DescribeStatement?, context: SqlContext)
    fun visit(explainStatement: ExplainStatement?, context: SqlContext)
    fun visit(showStatement: ShowStatement?, context: SqlContext)
    fun visit(declareStatement: DeclareStatement?, context: SqlContext)
    fun visit(grant: Grant?, context: SqlContext)
    fun visit(createSequence: CreateSequence?, context: SqlContext)
    fun visit(alterSequence: AlterSequence?, context: SqlContext)
    fun visit(createFunctionalStatement: CreateFunctionalStatement?, context: SqlContext)
    fun visit(createSynonym: CreateSynonym?, context: SqlContext)
    fun visit(alterSession: AlterSession?, context: SqlContext)
    fun visit(ifElseStatement: IfElseStatement?, context: SqlContext)
    fun visit(renameTableStatement: RenameTableStatement?, context: SqlContext)
    fun visit(purgeStatement: PurgeStatement?, context: SqlContext)
    fun visit(alterSystemStatement: AlterSystemStatement?, context: SqlContext)
    fun visit(unsupportedStatement: UnsupportedStatement?, context: SqlContext)
    fun visit(parenthesedInsert: ParenthesedInsert?, context: SqlContext)
    fun visit(parenthesedUpdate: ParenthesedUpdate?, context: SqlContext)
    fun visit(parenthesedDelete: ParenthesedDelete?, context: SqlContext)
}