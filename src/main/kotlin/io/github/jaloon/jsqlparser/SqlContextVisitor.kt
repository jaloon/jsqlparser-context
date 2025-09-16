package io.github.jaloon.jsqlparser

import io.github.jaloon.jsqlparser.expression.PostgreCollate
import io.github.jaloon.jsqlparser.expression.SimpleExpression
import net.sf.jsqlparser.expression.*
import net.sf.jsqlparser.expression.Function
import net.sf.jsqlparser.expression.operators.arithmetic.*
import net.sf.jsqlparser.expression.operators.conditional.AndExpression
import net.sf.jsqlparser.expression.operators.conditional.OrExpression
import net.sf.jsqlparser.expression.operators.conditional.XorExpression
import net.sf.jsqlparser.expression.operators.relational.*
import net.sf.jsqlparser.schema.Column
import net.sf.jsqlparser.schema.Partition
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
import net.sf.jsqlparser.statement.create.table.ColumnDefinition
import net.sf.jsqlparser.statement.create.table.CreateTable
import net.sf.jsqlparser.statement.create.table.Index
import net.sf.jsqlparser.statement.create.view.AlterView
import net.sf.jsqlparser.statement.create.view.CreateView
import net.sf.jsqlparser.statement.delete.Delete
import net.sf.jsqlparser.statement.delete.ParenthesedDelete
import net.sf.jsqlparser.statement.drop.Drop
import net.sf.jsqlparser.statement.execute.Execute
import net.sf.jsqlparser.statement.grant.Grant
import net.sf.jsqlparser.statement.insert.Insert
import net.sf.jsqlparser.statement.insert.InsertConflictAction
import net.sf.jsqlparser.statement.insert.InsertConflictTarget
import net.sf.jsqlparser.statement.insert.ParenthesedInsert
import net.sf.jsqlparser.statement.merge.*
import net.sf.jsqlparser.statement.piped.*
import net.sf.jsqlparser.statement.refresh.RefreshMaterializedViewStatement
import net.sf.jsqlparser.statement.select.*
import net.sf.jsqlparser.statement.show.ShowIndexStatement
import net.sf.jsqlparser.statement.show.ShowTablesStatement
import net.sf.jsqlparser.statement.truncate.Truncate
import net.sf.jsqlparser.statement.update.ParenthesedUpdate
import net.sf.jsqlparser.statement.update.Update
import net.sf.jsqlparser.statement.update.UpdateSet
import net.sf.jsqlparser.statement.upsert.Upsert
import net.sf.jsqlparser.util.cnfexpression.MultipleExpression

/**
 * SQL上下文访问器
 *
 * @author jaloon
 * @see StatementVisitor
 * @see ExpressionVisitor
 * @see SelectVisitor
 * @see SelectItemVisitor
 * @see FromQueryVisitor
 * @see FromItemVisitor
 * @see IntoTableVisitor
 * @see GroupByVisitor
 * @see OrderByVisitor
 * @see PivotVisitor
 * @see MergeOperationVisitor
 * @see PipeOperatorVisitor
 * @since 4.6
 */
interface SqlContextVisitor {

    // -------------------------- expression visitor begin --------------------------
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
    fun visit(stringValue: StringValue?, context: SqlContext)
    fun visit(booleanValue: BooleanValue?, context: SqlContext)
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
    fun visit(includesExpression: IncludesExpression?, context: SqlContext)
    fun visit(excludesExpression: ExcludesExpression?, context: SqlContext)
    fun visit(fullTextSearch: FullTextSearch?, context: SqlContext)
    fun visit(isNullExpression: IsNullExpression?, context: SqlContext)
    fun visit(isBooleanExpression: IsBooleanExpression?, context: SqlContext)
    fun visit(isUnknownExpression: IsUnknownExpression?, context: SqlContext)
    fun visit(likeExpression: LikeExpression?, context: SqlContext)
    fun visit(minorThan: MinorThan?, context: SqlContext)
    fun visit(minorThanEquals: MinorThanEquals?, context: SqlContext)
    fun visit(notEqualsTo: NotEqualsTo?, context: SqlContext)
    fun visit(doubleAnd: DoubleAnd?, context: SqlContext)
    fun visit(contains: Contains?, context: SqlContext)
    fun visit(containedBy: ContainedBy?, context: SqlContext)
    // /*[Duplicate]*/ fun visit(select: ParenthesedSelect?, context: SqlContext)
    fun visit(column: Column?, context: SqlContext)
    fun visit(caseExpression: CaseExpression?, context: SqlContext)
    fun visit(whenClause: WhenClause?, context: SqlContext)
    fun visit(existsExpression: ExistsExpression?, context: SqlContext)
    fun visit(memberOfExpression: MemberOfExpression?, context: SqlContext)
    fun visit(anyComparisonExpression: AnyComparisonExpression?, context: SqlContext)
    fun visit(concat: Concat?, context: SqlContext)
    fun visit(matches: Matches?, context: SqlContext)
    fun visit(bitwiseAnd: BitwiseAnd?, context: SqlContext)
    fun visit(bitwiseOr: BitwiseOr?, context: SqlContext)
    fun visit(bitwiseXor: BitwiseXor?, context: SqlContext)
    fun visit(castExpression: CastExpression?, context: SqlContext)
    fun visit(modulo: Modulo?, context: SqlContext)
    fun visit(analyticExpression: AnalyticExpression?, context: SqlContext)
    fun visit(extractExpression: ExtractExpression?, context: SqlContext)
    fun visit(intervalExpression: IntervalExpression?, context: SqlContext)
    fun visit(oracleHierarchicalExpression: OracleHierarchicalExpression?, context: SqlContext)
    fun visit(regExpMatchOperator: RegExpMatchOperator?, context: SqlContext)
    fun visit(jsonExpression: JsonExpression?, context: SqlContext)
    fun visit(jsonOperator: JsonOperator?, context: SqlContext)
    fun visit(userVariable: UserVariable?, context: SqlContext)
    fun visit(numericBind: NumericBind?, context: SqlContext)
    fun visit(keepExpression: KeepExpression?, context: SqlContext)
    fun visit(mySQLGroupConcat: MySQLGroupConcat?, context: SqlContext)
    fun visit(expressionList: ExpressionList<out Expression?>?, context: SqlContext)
    fun visit(rowConstructor: RowConstructor<out Expression?>?, context: SqlContext)
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
    fun visit(connectByRootOperator: ConnectByRootOperator?, context: SqlContext)
    fun visit(connectByPriorOperator: ConnectByPriorOperator?, context: SqlContext)
    fun visit(oracleNamedFunctionParameter: OracleNamedFunctionParameter?, context: SqlContext)
    fun visit(allColumns: AllColumns?, context: SqlContext)
    fun visit(functionColumns: FunctionAllColumns?, context: SqlContext)
    fun visit(allTableColumns: AllTableColumns?, context: SqlContext)
    fun visit(allValue: AllValue?, context: SqlContext)
    fun visit(isDistinctExpression: IsDistinctExpression?, context: SqlContext)
    fun visit(geometryDistance: GeometryDistance?, context: SqlContext)
    // /*[Duplicate]*/ fun visit(select: Select?, context: SqlContext)
    fun visit(transcodingFunction: TranscodingFunction?, context: SqlContext)
    fun visit(trimFunction: TrimFunction?, context: SqlContext)
    fun visit(rangeExpression: RangeExpression?, context: SqlContext)
    fun visit(tsqlLeftJoin: TSQLLeftJoin?, context: SqlContext)
    fun visit(tsqlRightJoin: TSQLRightJoin?, context: SqlContext)
    fun visit(structType: StructType?, context: SqlContext)
    fun visit(lambdaExpression: LambdaExpression?, context: SqlContext)
    fun visit(highExpression: HighExpression?, context: SqlContext)
    fun visit(lowExpression: LowExpression?, context: SqlContext)
    fun visit(plus: Plus?, context: SqlContext)
    fun visit(priorTo: PriorTo?, context: SqlContext)
    fun visit(inverse: Inverse?, context: SqlContext)
    fun visit(cosineSimilarity: CosineSimilarity?, context: SqlContext)
    // /*[Duplicate]*/ fun visit(fromQuery: FromQuery?, context: SqlContext)
    // -------------------------- expression visitor end --------------------------

    // -------------------------- statement visitor begin --------------------------
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
    fun visit(setStatement: SetStatement?, context: SqlContext)
    fun visit(resetStatement: ResetStatement?, context: SqlContext)
    fun visit(showColumnsStatement: ShowColumnsStatement?, context: SqlContext)
    fun visit(showIndexStatement: ShowIndexStatement?, context: SqlContext)
    fun visit(showTablesStatement: ShowTablesStatement?, context: SqlContext)
    fun visit(merge: Merge?, context: SqlContext)
    fun visit(select: Select?, context: SqlContext)
    fun visit(upsert: Upsert?, context: SqlContext)
    fun visit(useStatement: UseStatement?, context: SqlContext)
    fun visit(block: Block?, context: SqlContext)
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
    fun visit(parenthesedInsert: ParenthesedInsert?, context: SqlContext)
    fun visit(parenthesedUpdate: ParenthesedUpdate?, context: SqlContext)
    fun visit(parenthesedDelete: ParenthesedDelete?, context: SqlContext)
    // -------------------------- statement visitor end --------------------------

    // -------------------------- select visitor begin --------------------------
    fun visit(parenthesedSelect: ParenthesedSelect?, context: SqlContext)
    fun visit(plainSelect: PlainSelect?, context: SqlContext)
    fun visit(fromQuery: FromQuery?, context: SqlContext)
    fun visit(setOpList: SetOperationList?, context: SqlContext)
    fun visit(withItem: WithItem<out ParenthesedStatement?>?, context: SqlContext)
    fun visit(values: Values?, context: SqlContext)
    fun visit(lateralSubSelect: LateralSubSelect?, context: SqlContext)
    fun visit(tableStatement: TableStatement?, context: SqlContext)
    // -------------------------- select visitor end --------------------------

    // -------------------------- selectItem visitor begin --------------------------
    fun visit(selectItem: SelectItem<out Expression?>?, context: SqlContext)
    // -------------------------- selectItem visitor end --------------------------

    // -------------------------- fromItem visitor begin --------------------------
    fun visit(table: Table?, context: SqlContext)
    // /*[Duplicate]*/ fun visit(selectBody: ParenthesedSelect?, context: SqlContext)
    // /*[Duplicate]*/ fun visit(lateralSubSelect: LateralSubSelect?, context: SqlContext)
    fun visit(tableFunction: TableFunction?, context: SqlContext)
    fun visit(parenthesedFromItem: ParenthesedFromItem?, context: SqlContext)
    // /*[Duplicate]*/ fun visit(values: Values?, context: SqlContext)
    // /*[Duplicate]*/ fun visit(plainSelect: PlainSelect?, context: SqlContext)
    // /*[Duplicate]*/ fun visit(setOperationList: SetOperationList?, context: SqlContext)
    // /*[Duplicate]*/ fun visit(tableStatement: TableStatement?, context: SqlContext)
    // /*[Duplicate]*/ fun visit(fromQuery: FromQuery?, context: SqlContext)
    // -------------------------- fromItem visitor end --------------------------

    // -------------------------- fromQuery visitor begin --------------------------
    // /*[Duplicate]*/ fun visit(fromQuery: FromQuery?, context: SqlContext)
    // -------------------------- fromQuery visitor end --------------------------

    // -------------------------- intoTable visitor begin --------------------------
    // /*[Duplicate]*/ fun visit(table: Table?, context: SqlContext)
    // -------------------------- intoTable visitor end --------------------------

    // -------------------------- groupBy visitor begin --------------------------
    fun visit(groupByElement: GroupByElement?, context: SqlContext)
    // -------------------------- groupBy visitor end --------------------------

    // -------------------------- orderBy visitor begin --------------------------
    fun visit(orderByElement: OrderByElement?, context: SqlContext)
    // -------------------------- orderBy visitor end --------------------------

    // -------------------------- pivot visitor begin --------------------------
    fun visit(pivot: Pivot?, context: SqlContext)
    fun visit(pivotXml: PivotXml?, context: SqlContext)
    fun visit(unPivot: UnPivot?, context: SqlContext)
    // -------------------------- pivot visitor end --------------------------

    // -------------------------- mergeOperation visitor begin --------------------------
    fun visit(mergeDelete: MergeDelete?, context: SqlContext)
    fun visit(mergeUpdate: MergeUpdate?, context: SqlContext)
    fun visit(mergeInsert: MergeInsert?, context: SqlContext)
    // -------------------------- mergeOperation visitor end --------------------------

    // -------------------------- pipeOperator visitor begin --------------------------
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
    // -------------------------- pipeOperator visitor end --------------------------

    // -------------------------- other visitor begin --------------------------
    fun visit(havingClause: Function.HavingClause?, context: SqlContext)
    fun visit(typeDefExpr: DeclareStatement.TypeDefExpr?, context: SqlContext)
    fun visit(updateSet: UpdateSet?, context: SqlContext)
    fun visit(insertConflictAction: InsertConflictAction?, context: SqlContext)
    fun visit(insertConflictTarget: InsertConflictTarget?, context: SqlContext)
    fun visit(columnDefinition: ColumnDefinition?, context: SqlContext)
    fun visit(index: Index?, context: SqlContext)
    fun visit(distinct: Distinct?, context: SqlContext)
    fun visit(fetch: Fetch?, context: SqlContext)
    fun visit(first: First?, context: SqlContext)
    fun visit(join: Join?, context: SqlContext)
    fun visit(jsonFunctionExpression: JsonFunctionExpression?, context: SqlContext)
    fun visit(multipleExpression: MultipleExpression?, context: SqlContext)
    fun visit(namedExpressionList: NamedExpressionList<out Expression?>?, context: SqlContext)
    fun visit(lateralView: LateralView?, context: SqlContext)
    fun visit(limit: Limit?, context: SqlContext)
    fun visit(offset: Offset?, context: SqlContext)
    fun visit(skip: Skip?, context: SqlContext)
    fun visit(top: Top?, context: SqlContext)
    fun visit(orderByClause: OrderByClause?, context: SqlContext)
    fun visit(partition: Partition?, context: SqlContext)
    fun visit(partitionByClause: PartitionByClause?, context: SqlContext)
    fun visit(preferringClause: PreferringClause?, context: SqlContext)
    fun visit(outputClause: OutputClause?, context: SqlContext)
    fun visit(returningClause: ReturningClause?, context: SqlContext)
    fun visit(windowDefinition: WindowDefinition?, context: SqlContext)
    fun visit(windowElement: WindowElement?, context: SqlContext)
    fun visit(windowOffset: WindowOffset?, context: SqlContext)
    fun visit(windowRange: WindowRange?, context: SqlContext)
    fun visit(spannerInterleaveIn: SpannerInterleaveIn?, context: SqlContext)
    fun visit(postgreCollate: PostgreCollate?, context: SqlContext)
    fun visit(simpleExpression: SimpleExpression?, context: SqlContext)
    // -------------------------- other visitor end --------------------------

}
