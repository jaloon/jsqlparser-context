package io.github.jaloon.jsqlparser

import io.github.jaloon.jsqlparser.expression.SimpleExpression
import net.sf.jsqlparser.expression.*
import net.sf.jsqlparser.expression.Function
import net.sf.jsqlparser.expression.operators.arithmetic.*
import net.sf.jsqlparser.expression.operators.conditional.AndExpression
import net.sf.jsqlparser.expression.operators.conditional.OrExpression
import net.sf.jsqlparser.expression.operators.conditional.XorExpression
import net.sf.jsqlparser.expression.operators.relational.*
import net.sf.jsqlparser.schema.Column
import net.sf.jsqlparser.schema.Table
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
import net.sf.jsqlparser.statement.insert.InsertConflictAction
import net.sf.jsqlparser.statement.insert.InsertConflictTarget
import net.sf.jsqlparser.statement.merge.Merge
import net.sf.jsqlparser.statement.merge.MergeInsert
import net.sf.jsqlparser.statement.merge.MergeUpdate
import net.sf.jsqlparser.statement.select.*
import net.sf.jsqlparser.statement.show.ShowIndexStatement
import net.sf.jsqlparser.statement.show.ShowTablesStatement
import net.sf.jsqlparser.statement.truncate.Truncate
import net.sf.jsqlparser.statement.update.Update
import net.sf.jsqlparser.statement.update.UpdateSet
import net.sf.jsqlparser.statement.upsert.Upsert
import net.sf.jsqlparser.statement.values.ValuesStatement

/**
 * SQL上下文访问器
 *
 * @author jaloon
 * @see StatementVisitor
 *
 * @see ExpressionVisitor
 *
 * @see ItemsListVisitor
 *
 * @see SelectVisitor
 *
 * @see SelectItemVisitor
 *
 * @see FromItemVisitor
 *
 * @see GroupByVisitor
 *
 * @see OrderByVisitor
 *
 * @see PivotVisitor
 *
 * @since 4.6
 */
interface SqlContextVisitor {
    fun visit(bitwiseRightShift: BitwiseRightShift?, context: SqlContext)
    fun visit(bitwiseLeftShift: BitwiseLeftShift?, context: SqlContext)
    fun visit(nullValue: NullValue?, context: SqlContext)
    fun visit(function: Function?, context: SqlContext)
    fun visit(signedExpression: SignedExpression?, context: SqlContext)
    fun visit(jdbcParameter: JdbcParameter?, context: SqlContext)
    fun visit(jdbcNamedParameter: JdbcNamedParameter?, context: SqlContext)
    fun visit(doubleValue: DoubleValue?, context: SqlContext)
    fun visit(longValue: LongValue?, context: SqlContext)
    fun visit(hexValue: HexValue?, context: SqlContext)
    fun visit(dateValue: DateValue?, context: SqlContext)
    fun visit(timeValue: TimeValue?, context: SqlContext)
    fun visit(timestampValue: TimestampValue?, context: SqlContext)
    fun visit(parenthesis: Parenthesis?, context: SqlContext)
    fun visit(stringValue: StringValue?, context: SqlContext)
    fun visit(addition: Addition?, context: SqlContext)
    fun visit(division: Division?, context: SqlContext)
    fun visit(integerDivision: IntegerDivision?, context: SqlContext)
    fun visit(multiplication: Multiplication?, context: SqlContext)
    fun visit(subtraction: Subtraction?, context: SqlContext)
    fun visit(andExpression: AndExpression?, context: SqlContext)
    fun visit(orExpression: OrExpression?, context: SqlContext)
    fun visit(xorExpression: XorExpression?, context: SqlContext)
    fun visit(between: Between?, context: SqlContext)
    fun visit(overlapsCondition: OverlapsCondition?, context: SqlContext)
    fun visit(equalsTo: EqualsTo?, context: SqlContext)
    fun visit(greaterThan: GreaterThan?, context: SqlContext)
    fun visit(greaterThanEquals: GreaterThanEquals?, context: SqlContext)
    fun visit(inExpression: InExpression?, context: SqlContext)
    fun visit(fullTextSearch: FullTextSearch?, context: SqlContext)
    fun visit(isNullExpression: IsNullExpression?, context: SqlContext)
    fun visit(isBooleanExpression: IsBooleanExpression?, context: SqlContext)
    fun visit(likeExpression: LikeExpression?, context: SqlContext)
    fun visit(minorThan: MinorThan?, context: SqlContext)
    fun visit(minorThanEquals: MinorThanEquals?, context: SqlContext)
    fun visit(notEqualsTo: NotEqualsTo?, context: SqlContext)
    fun visit(column: Column?, context: SqlContext)
    fun visit(table: Table?, context: SqlContext)
    fun visit(subSelect: SubSelect?, context: SqlContext)
    fun visit(subJoin: SubJoin?, context: SqlContext)
    fun visit(join: Join?, context: SqlContext)
    fun visit(lateralSubSelect: LateralSubSelect?, context: SqlContext)
    fun visit(valuesList: ValuesList?, context: SqlContext)
    fun visit(tableFunction: TableFunction?, context: SqlContext)
    fun visit(parenthesisFromItem: ParenthesisFromItem?, context: SqlContext)
    fun visit(expressionList: ExpressionList?, context: SqlContext)
    fun visit(expressionListItem: ExpressionListItem?, context: SqlContext)
    fun visit(namedExpressionList: NamedExpressionList?, context: SqlContext)
    fun visit(multiExpressionList: MultiExpressionList?, context: SqlContext)
    fun visit(caseExpression: CaseExpression?, context: SqlContext)
    fun visit(whenClause: WhenClause?, context: SqlContext)
    fun visit(existsExpression: ExistsExpression?, context: SqlContext)
    fun visit(anyComparisonExpression: AnyComparisonExpression?, context: SqlContext)
    fun visit(concat: Concat?, context: SqlContext)
    fun visit(matches: Matches?, context: SqlContext)
    fun visit(bitwiseAnd: BitwiseAnd?, context: SqlContext)
    fun visit(bitwiseOr: BitwiseOr?, context: SqlContext)
    fun visit(bitwiseXor: BitwiseXor?, context: SqlContext)
    fun visit(castExpression: CastExpression?, context: SqlContext)
    fun visit(tryCastExpression: TryCastExpression?, context: SqlContext)
    fun visit(safeCastExpression: SafeCastExpression?, context: SqlContext)
    fun visit(modulo: Modulo?, context: SqlContext)
    fun visit(analyticExpression: AnalyticExpression?, context: SqlContext)
    fun visit(extractExpression: ExtractExpression?, context: SqlContext)
    fun visit(intervalExpression: IntervalExpression?, context: SqlContext)
    fun visit(oracleHierarchicalExpression: OracleHierarchicalExpression?, context: SqlContext)
    fun visit(regExpMatchOperator: RegExpMatchOperator?, context: SqlContext)
    fun visit(jsonExpression: JsonExpression?, context: SqlContext)
    fun visit(jsonOperator: JsonOperator?, context: SqlContext)
    fun visit(regExpMySQLOperator: RegExpMySQLOperator?, context: SqlContext)
    fun visit(userVariable: UserVariable?, context: SqlContext)
    fun visit(numericBind: NumericBind?, context: SqlContext)
    fun visit(keepExpression: KeepExpression?, context: SqlContext)
    fun visit(mySQLGroupConcat: MySQLGroupConcat?, context: SqlContext)
    fun visit(valueListExpression: ValueListExpression?, context: SqlContext)
    fun visit(rowConstructor: RowConstructor?, context: SqlContext)
    fun visit(rowGetExpression: RowGetExpression?, context: SqlContext)
    fun visit(oracleHint: OracleHint?, context: SqlContext)
    fun visit(timeKeyExpression: TimeKeyExpression?, context: SqlContext)
    fun visit(dateTimeLiteralExpression: DateTimeLiteralExpression?, context: SqlContext)
    fun visit(notExpression: NotExpression?, context: SqlContext)
    fun visit(nextValExpression: NextValExpression?, context: SqlContext)
    fun visit(collateExpression: CollateExpression?, context: SqlContext)
    fun visit(similarToExpression: SimilarToExpression?, context: SqlContext)
    fun visit(arrayExpression: ArrayExpression?, context: SqlContext)
    fun visit(arrayConstructor: ArrayConstructor?, context: SqlContext)
    fun visit(variableAssignment: VariableAssignment?, context: SqlContext)
    fun visit(xmlSerializeExpr: XMLSerializeExpr?, context: SqlContext)
    fun visit(timezoneExpression: TimezoneExpression?, context: SqlContext)
    fun visit(jsonAggregateFunction: JsonAggregateFunction?, context: SqlContext)
    fun visit(jsonFunction: JsonFunction?, context: SqlContext)
    fun visit(jsonFunctionExpression: JsonFunctionExpression?, context: SqlContext)
    fun visit(connectByRootOperator: ConnectByRootOperator?, context: SqlContext)
    fun visit(oracleNamedFunctionParameter: OracleNamedFunctionParameter?, context: SqlContext)
    fun visit(allColumns: AllColumns?, context: SqlContext)
    fun visit(allTableColumns: AllTableColumns?, context: SqlContext)
    fun visit(selectExpressionItem: SelectExpressionItem?, context: SqlContext)
    fun visit(allValue: AllValue?, context: SqlContext)
    fun visit(isDistinctExpression: IsDistinctExpression?, context: SqlContext)
    fun visit(geometryDistance: GeometryDistance?, context: SqlContext)
    fun visit(analyze: Analyze?, context: SqlContext)
    fun visit(savepointStatement: SavepointStatement?, context: SqlContext)
    fun visit(rollbackStatement: RollbackStatement?, context: SqlContext)
    fun visit(comment: Comment?, context: SqlContext)
    fun visit(commit: Commit?, context: SqlContext)
    fun visit(delete: Delete?, context: SqlContext)
    fun visit(update: Update?, context: SqlContext)
    fun visit(updateSet: UpdateSet?, context: SqlContext)
    fun visit(insert: Insert?, context: SqlContext)
    fun visit(insertConflictAction: InsertConflictAction?, context: SqlContext)
    fun visit(insertConflictTarget: InsertConflictTarget?, context: SqlContext)
    fun visit(drop: Drop?, context: SqlContext)
    fun visit(truncate: Truncate?, context: SqlContext)
    fun visit(createIndex: CreateIndex?, context: SqlContext)
    fun visit(createSchema: CreateSchema?, context: SqlContext)
    fun visit(createTable: CreateTable?, context: SqlContext)
    fun visit(createView: CreateView?, context: SqlContext)
    fun visit(alterView: AlterView?, context: SqlContext)
    fun visit(alter: Alter?, context: SqlContext)
    fun visit(statements: Statements?, context: SqlContext)
    fun visit(execute: Execute?, context: SqlContext)
    fun visit(setStatement: SetStatement?, context: SqlContext)
    fun visit(resetStatement: ResetStatement?, context: SqlContext)
    fun visit(showColumnsStatement: ShowColumnsStatement?, context: SqlContext)
    fun visit(showIndexStatement: ShowIndexStatement?, context: SqlContext)
    fun visit(showTablesStatement: ShowTablesStatement?, context: SqlContext)
    fun visit(merge: Merge?, context: SqlContext)
    fun visit(mergeInsert: MergeInsert?, context: SqlContext)
    fun visit(mergeUpdate: MergeUpdate?, context: SqlContext)
    fun visit(select: Select?, context: SqlContext)
    fun visit(upsert: Upsert?, context: SqlContext)
    fun visit(useStatement: UseStatement?, context: SqlContext)
    fun visit(block: Block?, context: SqlContext)
    fun visit(distinct: Distinct?, context: SqlContext)
    fun visit(plainSelect: PlainSelect?, context: SqlContext)
    fun visit(setOperationList: SetOperationList?, context: SqlContext)
    fun visit(withItem: WithItem?, context: SqlContext)
    fun visit(valuesStatement: ValuesStatement?, context: SqlContext)
    fun visit(describeStatement: DescribeStatement?, context: SqlContext)
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
    fun visit(groupByElement: GroupByElement?, context: SqlContext)
    fun visit(orderByElement: OrderByElement?, context: SqlContext)
    fun visit(pivot: Pivot?, context: SqlContext)
    fun visit(pivotXml: PivotXml?, context: SqlContext)
    fun visit(unPivot: UnPivot?, context: SqlContext)
    fun visit(limit: Limit?, context: SqlContext)
    fun visit(offset: Offset?, context: SqlContext)
    fun visit(orderByClause: OrderByClause?, context: SqlContext)
    fun visit(partitionByClause: PartitionByClause?, context: SqlContext)
    fun visit(outputClause: OutputClause?, context: SqlContext)
    fun visit(windowDefinition: WindowDefinition?, context: SqlContext)
    fun visit(windowElement: WindowElement?, context: SqlContext)
    fun visit(windowOffset: WindowOffset?, context: SqlContext)
    fun visit(windowRange: WindowRange?, context: SqlContext)
    fun visit(spannerInterleaveIn: SpannerInterleaveIn, context: SqlContext)
    fun visit(simpleExpression: SimpleExpression, context: SqlContext)
}
