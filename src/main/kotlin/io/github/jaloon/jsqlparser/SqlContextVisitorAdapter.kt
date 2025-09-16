package io.github.jaloon.jsqlparser

import io.github.jaloon.jsqlparser.expression.SimpleExpression
import io.github.jaloon.jsqlparser.expression.accept
import io.github.jaloon.jsqlparser.expression.operators.accept
import io.github.jaloon.jsqlparser.schema.accept
import io.github.jaloon.jsqlparser.statement.accept
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
import net.sf.jsqlparser.statement.merge.Merge
import net.sf.jsqlparser.statement.merge.MergeDelete
import net.sf.jsqlparser.statement.merge.MergeInsert
import net.sf.jsqlparser.statement.merge.MergeUpdate
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
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference
import java.util.function.BiConsumer
import java.util.function.Consumer
import java.util.function.Supplier

/**
 * SQL上下文访问器适配器
 * @author jaloon
 */
@Suppress("UNCHECKED_CAST")
open class SqlContextVisitorAdapter : SqlContextVisitor {

    // -------------------------- visitor tool functions begin --------------------------

    protected fun <T> visitSingle(context: SqlContext,
                                  getter: Supplier<T>,
                                  setter: Consumer<T>,
                                  accept: BiConsumer<T, SqlContext>
    ): Boolean {
        val del = AtomicBoolean()
        accept.accept(getter.get()) {
            if (it == null) {
                context.remove()
                del.set(true)
            } else {
                setter.accept(it as T)
            }
        }
        return del.get()
    }

    protected fun <T : Statement?> visitSingleStatement(context: SqlContext,
                                                        getter: Supplier<T>,
                                                        setter: Consumer<T>
    ) = visitSingle(context, getter, setter) { it, cxt -> it?.accept(this, cxt) }

    protected fun <T : Expression?> visitSingleExpression(context: SqlContext,
                                                          getter: Supplier<T>,
                                                          setter: Consumer<T>
    ) = visitSingle(context, getter, setter) { it, cxt -> it?.accept(this, cxt) }

    protected fun <T : FromItem?> visitSingleFromItem(context: SqlContext,
                                                      getter: Supplier<T>,
                                                      setter: Consumer<T>
    ) = visitSingle(context, getter, setter) { it, cxt -> it?.accept(this, cxt) }

    protected fun visitPairExpression(context: SqlContext,
                                      getLeft: Supplier<Expression?>,
                                      getRight: Supplier<Expression?>,
                                      setLeft: Consumer<Expression?>,
                                      setRight: Consumer<Expression?>
    ): Boolean {
        val changed = AtomicBoolean()
        getLeft.get()?.accept(this) {
            if (it == null) {
                context.replace(getRight.get())
                changed.set(true)
            } else {
                setLeft.accept(it as Expression)
            }
        }
        getRight.get()?.accept(this) {
            if (changed.get()) {
                context.replace(it)
            } else if (it == null) {
                context.replace(getLeft.get())
                changed.set(true)
            } else {
                setRight.accept(it as Expression)
            }
        }
        return changed.get()
    }

    protected fun visitBinaryExpression(expr: BinaryExpression?, context: SqlContext) = visitPairExpression(
        context,
        { expr?.leftExpression },
        { expr?.rightExpression },
        { expr?.leftExpression = it },
        { expr?.rightExpression = it }
    )

    protected fun <T> visitList(list: MutableList<T>?, accept: BiConsumer<T, SqlContext>) {
        list ?: return
        for (i in list.indices.reversed()) {
            accept.accept(list[i]) {
                if (it == null) {
                    list.removeAt(i)
                } else {
                    list[i] = it as T
                }
            }
        }
    }

    protected fun <T : Expression> visitExpressions(expressions: MutableList<T>?) {
        visitList(expressions) { it, cxt -> it.accept(this, cxt) }
    }

    protected fun visitOrderByElements(orderByElements: MutableList<OrderByElement>?) {
        visitList(orderByElements) { it, cxt -> it.accept(this, cxt) }
    }

    // -------------------------- visitor tool functions end --------------------------


    // -------------------------- expression visitor begin --------------------------

    override fun visit(bitwiseRightShift: BitwiseRightShift?, context: SqlContext) {
        visitBinaryExpression(bitwiseRightShift, context)
    }

    override fun visit(bitwiseLeftShift: BitwiseLeftShift?, context: SqlContext) {
        visitBinaryExpression(bitwiseLeftShift, context)
    }

    override fun visit(nullValue: NullValue?, context: SqlContext) {

    }

    override fun visit(function: Function?, context: SqlContext) {
        function ?: return
        function.parameters?.accept(this) { function.parameters = it as ExpressionList<*>? }
        function.namedParameters?.accept(this) { function.namedParameters = it as NamedExpressionList<*>? }
        function.havingClause?.accept(this) { function.havingClause = it as Function.HavingClause? }
        function.limit?.accept(this) { function.limit = it as Limit? }
        function.keep?.accept(this) { function.keep = it as KeepExpression? }
        when (val attribute = function.attribute) {
            is Column     -> attribute.accept(this) { function.setAttribute(it as Column?) }
            is Expression -> attribute.accept(this) { function.setAttribute(it as Expression?) }
        }
        visitOrderByElements(function.orderByElements)
    }

    override fun visit(signedExpression: SignedExpression?, context: SqlContext) {
        signedExpression?.expression?.accept(this) {
            signedExpression.expression = it as Expression? ?: SimpleExpression(null)
        }
    }

    override fun visit(jdbcParameter: JdbcParameter?, context: SqlContext) {

    }

    override fun visit(jdbcNamedParameter: JdbcNamedParameter?, context: SqlContext) {

    }

    override fun visit(doubleValue: DoubleValue?, context: SqlContext) {

    }

    override fun visit(longValue: LongValue?, context: SqlContext) {

    }

    override fun visit(hexValue: HexValue?, context: SqlContext) {

    }

    override fun visit(dateValue: DateValue?, context: SqlContext) {

    }

    override fun visit(timeValue: TimeValue?, context: SqlContext) {

    }

    override fun visit(timestampValue: TimestampValue?, context: SqlContext) {

    }

    override fun visit(stringValue: StringValue?, context: SqlContext) {

    }

    override fun visit(booleanValue: BooleanValue?, context: SqlContext) {

    }

    override fun visit(addition: Addition?, context: SqlContext) {
        visitBinaryExpression(addition, context)
    }

    override fun visit(division: Division?, context: SqlContext) {
        visitBinaryExpression(division, context)
    }

    override fun visit(integerDivision: IntegerDivision?, context: SqlContext) {
        visitBinaryExpression(integerDivision, context)
    }

    override fun visit(multiplication: Multiplication?, context: SqlContext) {
        visitBinaryExpression(multiplication, context)
    }

    override fun visit(subtraction: Subtraction?, context: SqlContext) {
        visitBinaryExpression(subtraction, context)
    }

    override fun visit(andExpression: AndExpression?, context: SqlContext) {
        visitBinaryExpression(andExpression, context)
    }

    override fun visit(orExpression: OrExpression?, context: SqlContext) {
        visitBinaryExpression(orExpression, context)
    }

    override fun visit(xorExpression: XorExpression?, context: SqlContext) {
        visitBinaryExpression(xorExpression, context)
    }

    override fun visit(between: Between?, context: SqlContext) {
        between?.leftExpression?.accept(this) { between.leftExpression = it as Expression? }
        between?.betweenExpressionStart?.accept(this) { between.betweenExpressionStart = it as Expression? }
        between?.betweenExpressionEnd?.accept(this) { between.betweenExpressionEnd = it as Expression? }
    }

    override fun visit(overlapsCondition: OverlapsCondition?, context: SqlContext) {
        overlapsCondition ?: return
        val del = AtomicBoolean()
        val ref = AtomicReference<OverlapsCondition?>()
        overlapsCondition.left?.accept(this) {
            if (it == null) {
                context.replace(overlapsCondition.right)
                del.set(true)
            } else {
                val condition = OverlapsCondition(it as ExpressionList<*>, overlapsCondition.right)
                context.replace(condition)
                ref.set(condition)
            }
        }
        overlapsCondition.right?.accept(this) {
            if (del.get()) {
                context.replace(it)
            } else {
                val left = ref.get()?.left ?: overlapsCondition.left
                if (it == null) {
                    context.replace(left)
                } else {
                    context.replace(OverlapsCondition(left, it as ExpressionList<*>))
                }
            }
        }
    }

    override fun visit(equalsTo: EqualsTo?, context: SqlContext) {
        visitBinaryExpression(equalsTo, context)
    }

    override fun visit(greaterThan: GreaterThan?, context: SqlContext) {
        visitBinaryExpression(greaterThan, context)
    }

    override fun visit(greaterThanEquals: GreaterThanEquals?, context: SqlContext) {
        visitBinaryExpression(greaterThanEquals, context)
    }

    override fun visit(inExpression: InExpression?, context: SqlContext) {
        visitPairExpression(
            context,
            { inExpression?.leftExpression },
            { inExpression?.rightExpression },
            { inExpression?.leftExpression = it },
            { inExpression?.rightExpression = it }
        )
    }

    override fun visit(includesExpression: IncludesExpression?, context: SqlContext) {
        visitPairExpression(
            context,
            { includesExpression?.leftExpression },
            { includesExpression?.rightExpression },
            { includesExpression?.leftExpression = it },
            { includesExpression?.rightExpression = it }
        )
    }

    override fun visit(excludesExpression: ExcludesExpression?, context: SqlContext) {
        visitPairExpression(
            context,
            { excludesExpression?.leftExpression },
            { excludesExpression?.rightExpression },
            { excludesExpression?.leftExpression = it },
            { excludesExpression?.rightExpression = it }
        )
    }

    override fun visit(fullTextSearch: FullTextSearch?, context: SqlContext) {
        fullTextSearch?.matchColumns?.accept(this) { fullTextSearch.matchColumns = it as ExpressionList<Column>? }
        fullTextSearch?.againstValue?.accept(this) {
            when (it) {
                is StringValue        -> fullTextSearch.setAgainstValue(it)
                is JdbcNamedParameter -> fullTextSearch.setAgainstValue(it)
                is JdbcParameter      -> fullTextSearch.setAgainstValue(it)
            }
        }
    }

    override fun visit(isNullExpression: IsNullExpression?, context: SqlContext) {
        visitSingleExpression(
            context,
            { isNullExpression?.leftExpression },
            { isNullExpression?.leftExpression = it }
        )
    }

    override fun visit(isBooleanExpression: IsBooleanExpression?, context: SqlContext) {
        visitSingleExpression(
            context,
            { isBooleanExpression?.leftExpression },
            { isBooleanExpression?.leftExpression = it }
        )
    }

    override fun visit(isUnknownExpression: IsUnknownExpression?, context: SqlContext) {
        visitSingleExpression(
            context,
            { isUnknownExpression?.leftExpression },
            { isUnknownExpression?.leftExpression = it }
        )
    }

    override fun visit(likeExpression: LikeExpression?, context: SqlContext) {
        if (!visitBinaryExpression(likeExpression, context)) {
            likeExpression?.escape?.accept(this) { likeExpression.escape = it as Expression? }
        }
    }

    override fun visit(minorThan: MinorThan?, context: SqlContext) {
        visitBinaryExpression(minorThan, context)
    }

    override fun visit(minorThanEquals: MinorThanEquals?, context: SqlContext) {
        visitBinaryExpression(minorThanEquals, context)
    }

    override fun visit(notEqualsTo: NotEqualsTo?, context: SqlContext) {
        visitBinaryExpression(notEqualsTo, context)
    }

    override fun visit(doubleAnd: DoubleAnd?, context: SqlContext) {
        visitBinaryExpression(doubleAnd, context)
    }

    override fun visit(contains: Contains?, context: SqlContext) {
        visitBinaryExpression(contains, context)
    }

    override fun visit(containedBy: ContainedBy?, context: SqlContext) {
        visitBinaryExpression(containedBy, context)
    }

    override fun visit(column: Column?, context: SqlContext) {

    }

    override fun visit(caseExpression: CaseExpression?, context: SqlContext) {
        caseExpression?.switchExpression?.accept(this) { caseExpression.switchExpression = it as Expression? }
        caseExpression?.elseExpression?.accept(this) { caseExpression.elseExpression = it as Expression? }
        visitExpressions<WhenClause>(caseExpression?.whenClauses)
    }

    override fun visit(whenClause: WhenClause?, context: SqlContext) {
        visitPairExpression(
            context,
            { whenClause?.whenExpression },
            { whenClause?.thenExpression },
            { whenClause?.whenExpression = it },
            { whenClause?.thenExpression = it }
        )
    }

    override fun visit(existsExpression: ExistsExpression?, context: SqlContext) {
        visitSingleExpression(
            context,
            { existsExpression?.rightExpression },
            { existsExpression?.rightExpression = it }
        )
    }

    override fun visit(memberOfExpression: MemberOfExpression?, context: SqlContext) {
        visitPairExpression(
            context,
            { memberOfExpression?.leftExpression },
            { memberOfExpression?.rightExpression },
            { memberOfExpression?.leftExpression = it },
            { memberOfExpression?.rightExpression = it }
        )
    }

    override fun visit(anyComparisonExpression: AnyComparisonExpression?, context: SqlContext) {
        visitSingleExpression<Select?>(context, { anyComparisonExpression?.select }) {
            context.replace(AnyComparisonExpression(anyComparisonExpression?.anyType, it))
        }
    }

    override fun visit(concat: Concat?, context: SqlContext) {
        visitBinaryExpression(concat, context)
    }

    override fun visit(matches: Matches?, context: SqlContext) {
        visitBinaryExpression(matches, context)
    }

    override fun visit(bitwiseAnd: BitwiseAnd?, context: SqlContext) {
        visitBinaryExpression(bitwiseAnd, context)
    }

    override fun visit(bitwiseOr: BitwiseOr?, context: SqlContext) {
        visitBinaryExpression(bitwiseOr, context)
    }

    override fun visit(bitwiseXor: BitwiseXor?, context: SqlContext) {
        visitBinaryExpression(bitwiseXor, context)
    }

    override fun visit(castExpression: CastExpression?, context: SqlContext) {
        visitSingleExpression(
            context,
            { castExpression?.leftExpression },
            { castExpression?.leftExpression = it }
        )
    }

    override fun visit(modulo: Modulo?, context: SqlContext) {
        visitBinaryExpression(modulo, context)
    }

    override fun visit(analyticExpression: AnalyticExpression?, context: SqlContext) {
        analyticExpression ?: return
        analyticExpression.expression?.accept(this) { analyticExpression.expression = it as Expression? }
        analyticExpression.offset?.accept(this) { analyticExpression.offset = it as Expression? }
        analyticExpression.defaultValue?.accept(this) { analyticExpression.defaultValue = it as Expression? }
        analyticExpression.keep?.accept(this) { analyticExpression.keep = it as KeepExpression? }
        analyticExpression.filterExpression?.accept(this) {
            analyticExpression.filterExpression = it as Expression?
        }
        visitOrderByElements(analyticExpression.funcOrderBy)
        analyticExpression.windowDefinition?.accept(this) {
            analyticExpression.windowDefinition = it as WindowDefinition?
        }
        analyticExpression.havingClause?.accept(this) {
            analyticExpression.havingClause = it as Function.HavingClause?
        }
        analyticExpression.limit?.accept(this) { analyticExpression.limit = it as Limit? }
    }

    override fun visit(extractExpression: ExtractExpression?, context: SqlContext) {
        visitSingleExpression(
            context,
            { extractExpression?.expression },
            { extractExpression?.expression = it }
        )
    }

    override fun visit(intervalExpression: IntervalExpression?, context: SqlContext) {
        intervalExpression?.expression?.accept(this) { intervalExpression.expression = it as Expression? }
    }

    override fun visit(oracleHierarchicalExpression: OracleHierarchicalExpression?, context: SqlContext) {
        visitPairExpression(
            context,
            { oracleHierarchicalExpression?.startExpression },
            { oracleHierarchicalExpression?.connectExpression },
            { oracleHierarchicalExpression?.startExpression = it },
            { oracleHierarchicalExpression?.connectExpression = it }
        )
    }

    override fun visit(regExpMatchOperator: RegExpMatchOperator?, context: SqlContext) {
        visitBinaryExpression(regExpMatchOperator, context)
    }

    override fun visit(jsonExpression: JsonExpression?, context: SqlContext) {
        jsonExpression?.expression?.accept(this) { jsonExpression.expression = it as Expression? }
    }

    override fun visit(jsonOperator: JsonOperator?, context: SqlContext) {
        visitBinaryExpression(jsonOperator, context)
    }

    override fun visit(userVariable: UserVariable?, context: SqlContext) {

    }

    override fun visit(numericBind: NumericBind?, context: SqlContext) {

    }

    override fun visit(keepExpression: KeepExpression?, context: SqlContext) {
        visitOrderByElements(keepExpression?.orderByElements)
    }

    override fun visit(mySQLGroupConcat: MySQLGroupConcat?, context: SqlContext) {
        mySQLGroupConcat?.expressionList?.accept(this) { mySQLGroupConcat.expressionList = it as ExpressionList<*>? }
        visitOrderByElements(mySQLGroupConcat?.orderByElements)
    }

    override fun visit(expressionList: ExpressionList<out Expression?>?, context: SqlContext) {
        visitExpressions(expressionList)
        if (expressionList.isNullOrEmpty()) {
            context.remove()
        }
    }

    override fun visit(rowConstructor: RowConstructor<out Expression?>?, context: SqlContext) {
        visit(rowConstructor as ExpressionList<out Expression?>?, context)
    }

    override fun visit(rowGetExpression: RowGetExpression?, context: SqlContext) {
        visitSingleExpression(
            context,
            { rowGetExpression?.expression },
            { rowGetExpression?.expression = it }
        )
    }

    override fun visit(oracleHint: OracleHint?, context: SqlContext) {

    }

    override fun visit(timeKeyExpression: TimeKeyExpression?, context: SqlContext) {

    }

    override fun visit(dateTimeLiteralExpression: DateTimeLiteralExpression?, context: SqlContext) {

    }

    override fun visit(notExpression: NotExpression?, context: SqlContext) {
        visitSingleExpression(
            context,
            { notExpression?.expression },
            { notExpression?.expression = it }
        )
    }

    override fun visit(nextValExpression: NextValExpression?, context: SqlContext) {

    }

    override fun visit(collateExpression: CollateExpression?, context: SqlContext) {
        visitSingleExpression(
            context,
            { collateExpression?.leftExpression },
            { collateExpression?.leftExpression = it }
        )
    }

    override fun visit(similarToExpression: SimilarToExpression?, context: SqlContext) {
        visitBinaryExpression(similarToExpression, context)
    }

    override fun visit(arrayExpression: ArrayExpression?, context: SqlContext) {
        arrayExpression ?: return
        arrayExpression.objExpression?.accept(this) { arrayExpression.objExpression = it as Expression? }
        arrayExpression.indexExpression?.accept(this) { arrayExpression.indexExpression = it as Expression? }
        arrayExpression.startIndexExpression?.accept(this) { arrayExpression.startIndexExpression = it as Expression? }
        arrayExpression.stopIndexExpression?.accept(this) { arrayExpression.stopIndexExpression = it as Expression? }
    }

    override fun visit(arrayConstructor: ArrayConstructor?, context: SqlContext) {
        visitExpressions(arrayConstructor?.expressions)
        if (arrayConstructor?.expressions.isNullOrEmpty()) {
            context.remove()
        }
    }

    override fun visit(variableAssignment: VariableAssignment?, context: SqlContext) {
        variableAssignment?.variable?.accept(this) { variableAssignment.variable = it as UserVariable? }
        variableAssignment?.expression?.accept(this) { variableAssignment.expression = it as Expression? }
    }

    override fun visit(xmlSerializeExpr: XMLSerializeExpr?, context: SqlContext) {
        xmlSerializeExpr?.expression?.accept(this) { xmlSerializeExpr.expression = it as Expression? }
        visitOrderByElements(xmlSerializeExpr?.orderByElements)
    }

    override fun visit(timezoneExpression: TimezoneExpression?, context: SqlContext) {
        timezoneExpression?.leftExpression?.accept(this) { timezoneExpression.leftExpression = it as Expression? }
        visitExpressions(timezoneExpression?.timezoneExpressions)
    }

    override fun visit(jsonAggregateFunction: JsonAggregateFunction?, context: SqlContext) {
        jsonAggregateFunction ?: return
        jsonAggregateFunction.expression?.accept(this) { jsonAggregateFunction.expression = it as Expression? }
        visitOrderByElements(jsonAggregateFunction.expressionOrderByElements)
        visitOrderByElements(jsonAggregateFunction.orderByElements)
        visitExpressions(jsonAggregateFunction.partitionExpressionList)
        jsonAggregateFunction.filterExpression?.accept(this) {
            jsonAggregateFunction.filterExpression = it as Expression?
        }
        jsonAggregateFunction.windowElement?.accept(this) {
            jsonAggregateFunction.windowElement = it as WindowElement?
        }
    }

    override fun visit(jsonFunction: JsonFunction?, context: SqlContext) {
        visitList(jsonFunction?.expressions) { it, cxt -> it.accept(this, cxt) }
    }

    override fun visit(connectByRootOperator: ConnectByRootOperator?, context: SqlContext) {
        visitSingleExpression<Column?>(
            context,
            { connectByRootOperator?.column },
            { context.replace(ConnectByRootOperator(it)) }
        )
    }

    override fun visit(connectByPriorOperator: ConnectByPriorOperator?, context: SqlContext) {
        visitSingleExpression<Column?>(
            context,
            { connectByPriorOperator?.column },
            { context.replace(ConnectByRootOperator(it)) }
        )
    }

    override fun visit(oracleNamedFunctionParameter: OracleNamedFunctionParameter?, context: SqlContext) {
        visitSingleExpression(context, { oracleNamedFunctionParameter?.expression }) {
            context.replace(OracleNamedFunctionParameter(oracleNamedFunctionParameter?.name, it))
        }
    }

    override fun visit(allColumns: AllColumns?, context: SqlContext) {
        visitExpressions<Column>(allColumns?.exceptColumns)
        visitList(allColumns?.replaceExpressions) { it, cxt -> it.accept(this, cxt) }
    }

    override fun visit(functionColumns: FunctionAllColumns?, context: SqlContext) {
        functionColumns?.function?.accept(this) {
            if (it == null) {
                context.replace(AllColumns())
            } else {
                functionColumns.function = it as Function
            }
        }
    }

    override fun visit(allTableColumns: AllTableColumns?, context: SqlContext) {
        visit(allTableColumns as AllColumns, context)
        // allTableColumns.table?.accept(this) {
        //     if (it == null) {
        //         val replacement = AllColumns(
        //             allTableColumns.exceptColumns,
        //             allTableColumns.replaceExpressions,
        //             allTableColumns.exceptKeyword
        //         )
        //         context.replace(replacement)
        //     } else {
        //         allTableColumns.table = it as Table
        //     }
        // }
    }

    override fun visit(allValue: AllValue?, context: SqlContext) {

    }

    override fun visit(isDistinctExpression: IsDistinctExpression?, context: SqlContext) {

    }

    override fun visit(geometryDistance: GeometryDistance?, context: SqlContext) {

    }

    override fun visit(transcodingFunction: TranscodingFunction?, context: SqlContext) {
        visitSingleExpression(
            context,
            { transcodingFunction?.expression },
            { transcodingFunction?.expression = it }
        )
    }

    override fun visit(trimFunction: TrimFunction?, context: SqlContext) {
        trimFunction?.expression?.accept(this) { trimFunction.expression = it as Expression? }
        trimFunction?.fromExpression?.accept(this) { trimFunction.fromExpression = it as Expression? }
    }

    override fun visit(rangeExpression: RangeExpression?, context: SqlContext) {
        visitPairExpression(
            context,
            { rangeExpression?.startExpression },
            { rangeExpression?.endExpression },
            { rangeExpression?.startExpression = it },
            { rangeExpression?.endExpression = it }
        )
    }

    override fun visit(tsqlLeftJoin: TSQLLeftJoin?, context: SqlContext) {

    }

    override fun visit(tsqlRightJoin: TSQLRightJoin?, context: SqlContext) {

    }

    override fun visit(structType: StructType?, context: SqlContext) {
        visitList(structType?.arguments) { it, cxt -> it.accept(this, cxt) }
    }

    override fun visit(lambdaExpression: LambdaExpression?, context: SqlContext) {
        lambdaExpression?.expression?.accept(this) { lambdaExpression.expression = it as Expression? }
    }

    override fun visit(highExpression: HighExpression?, context: SqlContext) {
        visitSingleExpression(
            context,
            { highExpression?.expression },
            { highExpression?.expression = it }
        )
    }

    override fun visit(lowExpression: LowExpression?, context: SqlContext) {
        visitSingleExpression(
            context,
            { lowExpression?.expression },
            { lowExpression?.expression = it }
        )
    }

    override fun visit(plus: Plus?, context: SqlContext) {
        visitBinaryExpression(plus, context)
    }

    override fun visit(priorTo: PriorTo?, context: SqlContext) {
        visitBinaryExpression(priorTo, context)
    }

    override fun visit(inverse: Inverse?, context: SqlContext) {
        visitSingleExpression(context, { inverse?.expression }, { inverse?.expression = it })
    }

    override fun visit(cosineSimilarity: CosineSimilarity?, context: SqlContext) {

    }

    // -------------------------- expression visitor begin --------------------------


    // -------------------------- statement visitor begin --------------------------

    override fun visit(analyze: Analyze?, context: SqlContext) {
        analyze?.table?.accept(this) {
            if (it == null) {
                context.remove()
            } else {
                analyze.table = it as Table
            }
        }
    }

    override fun visit(savepointStatement: SavepointStatement?, context: SqlContext) {

    }

    override fun visit(rollbackStatement: RollbackStatement?, context: SqlContext) {

    }

    override fun visit(comment: Comment?, context: SqlContext) {
        // comment?.table?.accept(this) { comment.table = it as Table }
        // comment?.column?.accept(this) { comment.column = it as Column }
        // comment?.view?.accept(this) { comment.view = it as Table }
        // comment?.comment?.accept(this) { comment.comment = it as StringValue }
    }

    override fun visit(commit: Commit?, context: SqlContext) {

    }

    override fun visit(delete: Delete?, context: SqlContext) {
        delete ?: return
        visitList(delete.withItemsList) { it, cxt -> it.accept(this, cxt) }
        delete.table?.accept(this) { delete.table = it as Table? }
        delete.oracleHint?.accept(this) { delete.oracleHint = it as OracleHint? }
        visitList(delete.tables) { it, cxt -> it.accept(this, cxt) }
        visitList(delete.usingList) { it, cxt -> it.accept(this, cxt) }
        visitList(delete.joins) { it, cxt -> it.accept(this, cxt) }
        delete.where?.accept(this) { delete.where = it as Expression? }
        delete.preferringClause?.accept(this) { delete.preferringClause = it as PreferringClause? }
        delete.limit?.accept(this) { delete.limit = it as Limit? }
        visitOrderByElements(delete.orderByElements)
        delete.returningClause?.accept(this) { delete.returningClause = it as ReturningClause? }
        delete.outputClause?.accept(this) { delete.outputClause = it as OutputClause? }
    }

    override fun visit(update: Update?, context: SqlContext) {
        update ?: return
        if (visitSingleFromItem(context, { update.table }, { update.table = it })) {
            return
        }
        visitList(update.withItemsList) { it, cxt -> it.accept(this, cxt) }
        update.where?.accept(this) { update.where = it as Expression? }
        update.preferringClause?.accept(this) { update.preferringClause = it as PreferringClause? }
        visitList(update.updateSets) { it, cxt -> it.accept(this, cxt) }
        update.fromItem?.accept(this) { update.fromItem = it as FromItem? }
        visitList(update.joins) { it, cxt -> it.accept(this, cxt) }
        visitList(update.startJoins) { it, cxt -> it.accept(this, cxt) }
        visitOrderByElements(update.orderByElements)
        update.limit?.accept(this) { update.limit = it as Limit? }
        update.returningClause?.accept(this) { update.returningClause = it as ReturningClause? }
        update.outputClause?.accept(this) { update.outputClause = it as OutputClause? }
    }

    override fun visit(insert: Insert?, context: SqlContext) {
        insert ?: return
        if (visitSingleFromItem(context, { insert.table }, { insert.table = it })) {
            return
        }
        insert.oracleHint?.accept(this) { insert.oracleHint = it as OracleHint? }
        visitExpressions<Column>(insert.columns)
        visitList(insert.partitions) { it, cxt -> it.accept(this, cxt) }
        insert.select?.accept(this) { insert.select = it as Select? }
        visitList(insert.duplicateUpdateSets) { it, cxt -> it.accept(this, cxt) }
        insert.returningClause?.accept(this) { insert.returningClause = it as ReturningClause? }
        visitList(insert.setUpdateSets) { it, cxt -> it.accept(this, cxt) }
        visitList(insert.withItemsList) { it, cxt -> it.accept(this, cxt) }
        insert.outputClause?.accept(this) { insert.outputClause = it as OutputClause? }
        insert.conflictTarget?.accept(this) { insert.conflictTarget = it as InsertConflictTarget? }
        insert.conflictAction?.accept(this) { insert.conflictAction = it as InsertConflictAction? }
    }

    override fun visit(drop: Drop?, context: SqlContext) {
        visitSingleFromItem(context, { drop?.name }, { drop?.name = it })
    }

    override fun visit(truncate: Truncate?, context: SqlContext) {
        truncate?.table?.accept(this) { truncate.table = it as Table? }
        visitList(truncate?.tables) { it, cxt -> it.accept(this, cxt) }
    }

    override fun visit(createIndex: CreateIndex?, context: SqlContext) {
        visitSingleFromItem(context, { createIndex?.table }, { createIndex?.table = it })
    }

    override fun visit(createSchema: CreateSchema?, context: SqlContext) {
        // visitList(createSchema?.statements) { it, cxt -> it.accept(this, cxt) }
    }

    override fun visit(createTable: CreateTable?, context: SqlContext) {
        createTable ?: return
        if (visitSingleFromItem(context, { createTable.table }, { createTable.table = it })) {
            return
        }
        visitList(createTable.columnDefinitions) { it, cxt -> it.accept(this, cxt) }
        visitList(createTable.indexes) { it, cxt -> it.accept(this, cxt) }
        createTable.select?.accept(this) {
            createTable.setSelect(it as Select?, createTable.isSelectParenthesis)
        }
        createTable.likeTable?.accept(this) {
            createTable.setLikeTable(it as Table?, createTable.isSelectParenthesis)
        }
        createTable.spannerInterleaveIn?.accept(this) {
            createTable.spannerInterleaveIn = it as SpannerInterleaveIn?
        }
    }

    override fun visit(createView: CreateView?, context: SqlContext) {
        createView ?: return
        if (visitSingleFromItem(context, { createView.view }, { createView.view = it })) {
            return
        }
        createView.select?.accept(this) { createView.select = it as Select? }
        visitExpressions<Column>(createView.columnNames)
    }

    override fun visit(alterView: AlterView?, context: SqlContext) {
        alterView ?: return
        if (visitSingleFromItem(context, { alterView.view }, { alterView.view = it })) {
            return
        }
        alterView.select?.accept(this) { alterView.select = it as Select? }
    }

    override fun visit(materializedView: RefreshMaterializedViewStatement?, context: SqlContext) {
        visitSingleFromItem(context, { materializedView?.view }, { materializedView?.view = it })
    }

    override fun visit(alter: Alter?, context: SqlContext) {
        visitSingleFromItem(context, { alter?.table }, { alter?.table = it })
    }

    override fun visit(statements: Statements?, context: SqlContext) {
        visitList(statements) { it, cxt -> it.accept(this, cxt) }
    }

    override fun visit(execute: Execute?, context: SqlContext) {
        visitExpressions(execute?.exprList)
        if (execute?.exprList.isNullOrEmpty()) {
            execute?.exprList = null
        }
    }

    override fun visit(setStatement: SetStatement?, context: SqlContext) {
        setStatement ?: return
        for (i in 0 until setStatement.count) {
            visitExpressions(setStatement.getExpressions(i))
        }
    }

    override fun visit(resetStatement: ResetStatement?, context: SqlContext) {

    }

    override fun visit(showColumnsStatement: ShowColumnsStatement?, context: SqlContext) {

    }

    override fun visit(showIndexStatement: ShowIndexStatement?, context: SqlContext) {

    }

    override fun visit(showTablesStatement: ShowTablesStatement?, context: SqlContext) {
        showTablesStatement?.likeExpression?.accept(this) { showTablesStatement.likeExpression = it as Expression? }
        showTablesStatement?.whereCondition?.accept(this) { showTablesStatement.whereCondition = it as Expression? }
    }

    override fun visit(merge: Merge?, context: SqlContext) {
        merge ?: return
        visitList(merge.withItemsList) { it, cxt -> it.accept(this, cxt) }
        merge.table?.accept(this) { merge.table = it as Table? }
        merge.oracleHint?.accept(this) { merge.oracleHint = it as OracleHint? }
        merge.fromItem?.accept(this) { merge.fromItem = it as FromItem? }
        merge.onCondition?.accept(this) { merge.onCondition = it as Expression? }
        merge.mergeInsert?.accept(this) { merge.mergeInsert = it as MergeInsert? }
        merge.mergeUpdate?.accept(this) { merge.mergeUpdate = it as MergeUpdate? }
        merge.outputClause?.accept(this) { merge.outputClause = it as OutputClause? }
    }

    override fun visit(select: Select?, context: SqlContext) {
        select ?: return
        select.forUpdateTable?.accept(this) { select.forUpdateTable = it as Table? }
        visitList(select.withItemsList) { it, cxt -> it.accept(this, cxt) }
        select.limitBy?.accept(this) { select.limitBy = it as Limit? }
        select.limit?.accept(this) { select.limit = it as Limit? }
        select.offset?.accept(this) { select.offset = it as Offset? }
        select.fetch?.accept(this) { select.fetch = it as Fetch? }
        visitOrderByElements(select.orderByElements)
        select.pivot?.accept(this) { select.pivot = it as Pivot? }
        select.unPivot?.accept(this) { select.unPivot = it as UnPivot? }
    }

    override fun visit(upsert: Upsert?, context: SqlContext) {
        upsert ?: return
        if (visitSingleFromItem(context, { upsert.table }, { upsert.table = it })) {
            return
        }
        visitExpressions<Column>(upsert.columns)
        visitExpressions(upsert.expressions)
        upsert.select?.accept(this) { upsert.select = it as Select? }
        visitList(upsert.updateSets) { it, cxt -> it.accept(this, cxt) }
        visitList(upsert.duplicateUpdateSets) { it, cxt -> it.accept(this, cxt) }
    }

    override fun visit(useStatement: UseStatement?, context: SqlContext) {

    }

    override fun visit(block: Block?, context: SqlContext) {
        visitList(block?.statements) { it, cxt -> it.accept(this, cxt) }
    }

    override fun visit(describeStatement: DescribeStatement?, context: SqlContext) {
        visitSingleFromItem(context, { describeStatement?.table }, { describeStatement?.table = it })
    }

    override fun visit(explainStatement: ExplainStatement?, context: SqlContext) {
        explainStatement?.statement?.accept(this) { explainStatement.statement = it as Select? }
        explainStatement?.table?.accept(this) { explainStatement.table = it as Table? }
    }

    override fun visit(showStatement: ShowStatement?, context: SqlContext) {

    }

    override fun visit(declareStatement: DeclareStatement?, context: SqlContext) {
        declareStatement?.userVariable?.accept(this) { declareStatement.userVariable = it as UserVariable? }
        visitList(declareStatement?.typeDefExprList) { it, cxt -> it.accept(this, cxt) }
        visitList(declareStatement?.columnDefinitions) { it, cxt -> it.accept(this, cxt) }
    }

    override fun visit(grant: Grant?, context: SqlContext) {

    }

    override fun visit(createSequence: CreateSequence?, context: SqlContext) {

    }

    override fun visit(alterSequence: AlterSequence?, context: SqlContext) {

    }

    override fun visit(createFunctionalStatement: CreateFunctionalStatement?, context: SqlContext) {

    }

    override fun visit(createSynonym: CreateSynonym?, context: SqlContext) {

    }

    override fun visit(alterSession: AlterSession?, context: SqlContext) {

    }

    override fun visit(ifElseStatement: IfElseStatement?, context: SqlContext) {
        ifElseStatement ?: return
        val ref = AtomicReference(ifElseStatement)
        ifElseStatement.condition.accept(this) {
            if (it == null) {
                ref.set(null)
                context.remove()
            } else {
                val newValue = IfElseStatement(it as Expression, ifElseStatement.ifStatement)
                newValue.elseStatement = ifElseStatement.elseStatement
                newValue.isUsingSemicolonForIfStatement = ifElseStatement.isUsingSemicolonForIfStatement
                newValue.isUsingSemicolonForElseStatement = ifElseStatement.isUsingSemicolonForElseStatement
                ref.set(newValue)
                context.replace(newValue)
            }
        }
        if (ref.get() == null) return
        ref.get().ifStatement?.accept(this) {
            val newValue = IfElseStatement(ref.get().condition, it as Statement?)
            newValue.elseStatement = ref.get().elseStatement
            newValue.isUsingSemicolonForIfStatement = ref.get().isUsingSemicolonForIfStatement
            newValue.isUsingSemicolonForElseStatement = ref.get().isUsingSemicolonForElseStatement
            ref.set(newValue)
            context.replace(newValue)
        }
        ref.get().elseStatement?.accept(this) { ref.get().elseStatement = it as Statement? }
    }

    override fun visit(renameTableStatement: RenameTableStatement?, context: SqlContext) {

    }

    override fun visit(purgeStatement: PurgeStatement?, context: SqlContext) {

    }

    override fun visit(alterSystemStatement: AlterSystemStatement?, context: SqlContext) {

    }

    override fun visit(unsupportedStatement: UnsupportedStatement?, context: SqlContext) {

    }

    override fun visit(parenthesedInsert: ParenthesedInsert?, context: SqlContext) {
        visitSingleStatement(context, { parenthesedInsert?.insert }, { parenthesedInsert?.insert = it })
    }

    override fun visit(parenthesedUpdate: ParenthesedUpdate?, context: SqlContext) {
        visitSingleStatement(context, { parenthesedUpdate?.update }, { parenthesedUpdate?.update = it })
    }

    override fun visit(parenthesedDelete: ParenthesedDelete?, context: SqlContext) {
        visitSingleStatement(context, { parenthesedDelete?.delete }, { parenthesedDelete?.delete = it })
    }

    // -------------------------- statement visitor end --------------------------


    // -------------------------- select visitor begin --------------------------

    override fun visit(parenthesedSelect: ParenthesedSelect?, context: SqlContext) {
        parenthesedSelect ?: return
        if (visitSingleStatement(context, { parenthesedSelect.select }, { parenthesedSelect.select = it })) {
            return
        }
        parenthesedSelect.pivot?.accept(this) { parenthesedSelect.pivot = it as Pivot? }
        parenthesedSelect.unPivot?.accept(this) { parenthesedSelect.unPivot = it as UnPivot? }
        visit(parenthesedSelect as Select, context)
    }

    override fun visit(plainSelect: PlainSelect?, context: SqlContext) {
        plainSelect ?: return

        // extends Select
        visit(plainSelect as Select, context)

        // PlainSelect self
        plainSelect.distinct?.accept(this) { plainSelect.distinct = it as Distinct? }
        visitList(plainSelect.selectItems) { it, cxt -> it.accept(this, cxt) }
        visitList(plainSelect.intoTables) { it, cxt -> it.accept(this, cxt) }
        plainSelect.fromItem?.accept(this) { plainSelect.fromItem = it as FromItem? }
        visitList(plainSelect.lateralViews) { it, cxt -> it.accept(this, cxt) }
        visitList(plainSelect.joins) { it, cxt -> it.accept(this, cxt) }
        plainSelect.where?.accept(this) { plainSelect.where = it as Expression? }
        plainSelect.groupBy?.accept(this) { plainSelect.setGroupByElement(it as GroupByElement?) }
        plainSelect.having?.accept(this) { plainSelect.having = it as Expression? }
        plainSelect.qualify?.accept(this) { plainSelect.qualify = it as Expression? }
        plainSelect.skip?.accept(this) { plainSelect.skip = it as Skip? }
        plainSelect.first?.accept(this) { plainSelect.first = it as First? }
        plainSelect.top?.accept(this) { plainSelect.top = it as Top? }
        plainSelect.oracleHierarchical?.accept(this) {
            plainSelect.oracleHierarchical = it as OracleHierarchicalExpression?
        }
        plainSelect.preferringClause?.accept(this) { plainSelect.preferringClause = it as PreferringClause? }
        plainSelect.oracleHint?.accept(this) { plainSelect.oracleHint = it as OracleHint? }
        visitList(plainSelect.windowDefinitions) { it, cxt -> it.accept(this, cxt) }
        plainSelect.intoTempTable?.accept(this) { plainSelect.intoTempTable = it as Table? }
    }

    override fun visit(fromQuery: FromQuery?, context: SqlContext) {
        fromQuery ?: return
        visit(fromQuery as Select, context)
        fromQuery.fromItem?.accept(this) { fromQuery.fromItem = it as FromItem? }
        visitList(fromQuery.lateralViews) { it, cxt -> it.accept(this, cxt) }
        visitList(fromQuery.joins) { it, cxt -> it.accept(this, cxt) }
    }

    override fun visit(setOpList: SetOperationList?, context: SqlContext) {
        visit(setOpList as Select?, context)
        visitList(setOpList?.selects) { it, cxt -> it.accept(this, cxt) }
    }

    override fun visit(withItem: WithItem<out ParenthesedStatement?>?, context: SqlContext) {
        withItem ?: return
        (withItem as WithItem<ParenthesedStatement?>).parenthesedStatement?.accept(this) {
            withItem.parenthesedStatement = it as ParenthesedStatement?
        }
        visitList(withItem.withItemList) { it, cxt -> it.accept(this, cxt) }
    }

    override fun visit(values: Values?, context: SqlContext) {
        visitExpressions(values?.expressions)
        if (values?.expressions.isNullOrEmpty()) {
            context.remove()
        }
    }

    override fun visit(lateralSubSelect: LateralSubSelect?, context: SqlContext) {
        visit(lateralSubSelect as ParenthesedSelect, context)
    }

    override fun visit(tableStatement: TableStatement?, context: SqlContext) {
        tableStatement ?: return
        if (visitSingleFromItem(context, { tableStatement.table }, { tableStatement.table = it })) {
            return
        }
        visitOrderByElements(tableStatement.orderByElements)
        tableStatement.limit?.accept(this) { tableStatement.limit = it as Limit? }
        tableStatement.offset?.accept(this) { tableStatement.offset = it as Offset? }
    }

    // -------------------------- select visitor end --------------------------


    // -------------------------- selectItem visitor begin --------------------------

    override fun visit(selectItem: SelectItem<out Expression?>?, context: SqlContext) {
        val item = selectItem as SelectItem<Expression?>?
        visitSingleExpression(context, { item?.expression }, { item?.expression = it })
    }

    // -------------------------- selectItem visitor end --------------------------


    // -------------------------- fromItem visitor begin --------------------------

    override fun visit(table: Table?, context: SqlContext) {
        table?.pivot?.accept(this) { table.pivot = it as Pivot? }
        table?.unPivot?.accept(this) { table.unPivot = it as UnPivot? }
    }

    override fun visit(tableFunction: TableFunction?, context: SqlContext) {
        tableFunction ?: return
        if (visitSingleExpression(context, { tableFunction.function }, { tableFunction.function = it })) {
            return
        }
        tableFunction.pivot?.accept(this) { tableFunction.pivot = it as Pivot? }
        tableFunction.unPivot?.accept(this) { tableFunction.unPivot = it as UnPivot? }
    }

    override fun visit(parenthesedFromItem: ParenthesedFromItem?, context: SqlContext) {
        parenthesedFromItem ?: return
        if (visitSingleFromItem(context, { parenthesedFromItem.fromItem }, { parenthesedFromItem.fromItem = it })) {
            return
        }
        visitList(parenthesedFromItem.joins) { it, cxt -> it.accept(this, cxt) }
        parenthesedFromItem.pivot?.accept(this) { parenthesedFromItem.pivot = it as Pivot? }
        parenthesedFromItem.unPivot?.accept(this) { parenthesedFromItem.unPivot = it as UnPivot? }
    }

    // -------------------------- fromItem visitor end --------------------------


    // -------------------------- groupBy visitor begin --------------------------

    override fun visit(groupByElement: GroupByElement?, context: SqlContext) {
        visitExpressions(groupByElement?.groupByExpressionList)
        visitList(groupByElement?.groupingSets) { it, cxt -> it.accept(this, cxt) }
    }

    // -------------------------- groupBy visitor end --------------------------


    // -------------------------- orderBy visitor begin --------------------------

    override fun visit(orderByElement: OrderByElement?, context: SqlContext) {
        visitSingleExpression(context, { orderByElement?.expression }, { orderByElement?.expression = it })
    }

    // -------------------------- orderBy visitor end --------------------------


    // -------------------------- pivot visitor begin --------------------------

    override fun visit(pivot: Pivot?, context: SqlContext) {
        pivot ?: return
        visitList(pivot.functionItems) { it, cxt -> it.accept(this, cxt) }
        visitExpressions<Column>(pivot.forColumns)
        visitList(pivot.singleInItems) { it, cxt -> it.accept(this, cxt) }
        visitList(pivot.multiInItems) { it, cxt -> it.accept(this, cxt) }
    }

    override fun visit(pivotXml: PivotXml?, context: SqlContext) {
        visit(pivotXml as Pivot?, context)
        pivotXml?.inSelect?.accept(this) { pivotXml.inSelect = it as Select? }
    }

    override fun visit(unPivot: UnPivot?, context: SqlContext) {
        visitExpressions<Column>(unPivot?.unPivotClause)
        visitExpressions<Column>(unPivot?.unPivotForClause)
        visitList(unPivot?.unPivotInClause) { it, cxt -> it.accept(this, cxt) }
    }

    // -------------------------- pivot visitor end --------------------------


    // -------------------------- mergeOperation visitor begin --------------------------

    override fun visit(mergeDelete: MergeDelete?, context: SqlContext) {
        visitSingleExpression(context, { mergeDelete?.andPredicate }, { mergeDelete?.andPredicate = it })
    }

    override fun visit(mergeUpdate: MergeUpdate?, context: SqlContext) {
        mergeUpdate ?: return
        visitList(mergeUpdate.updateSets) { it, cxt -> it.accept(this, cxt) }
        mergeUpdate.andPredicate?.accept(this) { mergeUpdate.andPredicate = it as Expression? }
        mergeUpdate.whereCondition?.accept(this) { mergeUpdate.whereCondition = it as Expression? }
        mergeUpdate.deleteWhereCondition?.accept(this) { mergeUpdate.deleteWhereCondition = it as Expression? }
    }

    override fun visit(mergeInsert: MergeInsert?, context: SqlContext) {
        mergeInsert ?: return
        mergeInsert.andPredicate?.accept(this) { mergeInsert.andPredicate = it as Expression? }
        visitExpressions<Column>(mergeInsert.columns)
        visitExpressions(mergeInsert.values)
        mergeInsert.whereCondition?.accept(this) { mergeInsert.whereCondition = it as Expression? }
    }

    // -------------------------- mergeOperation visitor end --------------------------


    // -------------------------- pipeOperator visitor begin --------------------------

    override fun visit(aggregate: AggregatePipeOperator?, context: SqlContext) {
        visitList(aggregate?.selectItems) { it, cxt -> it.accept(this, cxt) }
        visitList(aggregate?.groupItems) { it, cxt -> it.accept(this, cxt) }
    }

    override fun visit(`as`: AsPipeOperator?, context: SqlContext) {

    }

    override fun visit(call: CallPipeOperator?, context: SqlContext) {
        visitSingleExpression(context, { call?.tableFunction }, { call?.tableFunction = it })
    }

    override fun visit(drop: DropPipeOperator?, context: SqlContext) {
        visitExpressions<Column>(drop?.columns)
    }

    override fun visit(extend: ExtendPipeOperator?, context: SqlContext) {
        visit(extend as SelectPipeOperator, context)
    }

    override fun visit(join: JoinPipeOperator?, context: SqlContext) {
        visitSingle(context, { join?.join }, { join?.join = it }) { it, cxt ->
            it?.accept(this, cxt)
        }
    }

    override fun visit(limit: LimitPipeOperator?, context: SqlContext) {
        limit ?: return
        if (visitSingleExpression(context, { limit.limitExpression }, { limit.limitExpression = it })) {
            return
        }
        limit.offsetExpression?.accept(this) { limit.offsetExpression = it as Expression? }
    }

    override fun visit(orderBy: OrderByPipeOperator?, context: SqlContext) {
        orderBy ?: return
        visitOrderByElements(orderBy.orderByElements)
        if (orderBy.orderByElements.isNullOrEmpty()) {
            context.remove()
        }
    }

    override fun visit(pivot: PivotPipeOperator?, context: SqlContext) {
        pivot ?: return
        pivot.aggregateExpression?.accept(this) { pivot.aggregateExpression = it as Function? }
        pivot.inputColumn?.accept(this) { pivot.inputColumn = it as Column? }
        visitList(pivot.pivotColumns) { it, cxt -> it.accept(this, cxt) }
    }

    override fun visit(rename: RenamePipeOperator?, context: SqlContext) {
        visit(rename as SelectPipeOperator, context)
    }

    override fun visit(select: SelectPipeOperator?, context: SqlContext) {
        visitList(select?.selectItems) { it, cxt -> it.accept(this, cxt) }
    }

    override fun visit(set: SetPipeOperator?, context: SqlContext) {
        visitList(set?.updateSets) { it, cxt -> it.accept(this, cxt) }
        if (set?.updateSets.isNullOrEmpty()) {
            context.remove()
        }
    }

    override fun visit(tableSample: TableSamplePipeOperator?, context: SqlContext) {

    }

    override fun visit(union: SetOperationPipeOperator?, context: SqlContext) {
        visitList(union?.selects) { it, cxt -> this.visit(it, cxt) }
    }

    override fun visit(unPivot: UnPivotPipeOperator?, context: SqlContext) {
        unPivot ?: return
        unPivot.valuesColumn?.accept(this) { unPivot.valuesColumn = it as Column? }
        unPivot.nameColumn?.accept(this) { unPivot.nameColumn = it as Column? }
        visitList(unPivot.pivotColumns) { it, cxt -> it.accept(this, cxt) }
    }

    override fun visit(where: WherePipeOperator?, context: SqlContext) {
        visitSingleExpression(context, { where?.expression }, { where?.expression = it })
    }

    override fun visit(window: WindowPipeOperator?, context: SqlContext) {
        visit(window as SelectPipeOperator, context)
    }

    // -------------------------- pipeOperator visitor end --------------------------


    // -------------------------- other visitor begin --------------------------

    override fun visit(havingClause: Function.HavingClause?, context: SqlContext) {
        visitSingleExpression(context, { havingClause?.expression }, { havingClause?.expression = it })
    }

    override fun visit(typeDefExpr: DeclareStatement.TypeDefExpr?, context: SqlContext) {
        val ref = AtomicReference(typeDefExpr)
        typeDefExpr?.userVariable?.accept(this) {
            val newValue = DeclareStatement.TypeDefExpr(
                it as UserVariable?,
                typeDefExpr.colDataType,
                typeDefExpr.defaultExpr
            )
            context.replace(newValue)
            ref.set(newValue)
        }
        ref.get()?.defaultExpr?.accept(this) {
            val newValue = DeclareStatement.TypeDefExpr(
                ref.get()?.userVariable,
                ref.get()?.colDataType,
                it as Expression?
            )
            context.replace(newValue)
        }
    }

    override fun visit(updateSet: UpdateSet?, context: SqlContext) {
        visitExpressions<Column>(updateSet?.columns)
        visitExpressions(updateSet?.values)
    }

    override fun visit(insertConflictAction: InsertConflictAction?, context: SqlContext) {
        insertConflictAction?.whereExpression?.accept(this) {
            insertConflictAction.whereExpression = it as Expression?
        }
        visitList(insertConflictAction?.updateSets) { it, cxt -> it.accept(this, cxt) }
    }

    override fun visit(insertConflictTarget: InsertConflictTarget?, context: SqlContext) {
        insertConflictTarget?.indexExpression?.accept(this) {
            insertConflictTarget.indexExpression = it as Expression?
        }
        insertConflictTarget?.whereExpression?.accept(this) {
            insertConflictTarget.whereExpression = it as Expression?
        }
    }

    override fun visit(columnDefinition: ColumnDefinition?, context: SqlContext) {

    }

    override fun visit(index: Index?, context: SqlContext) {

    }

    override fun visit(distinct: Distinct?, context: SqlContext) {
        visitList(distinct?.onSelectItems) { it, cxt -> it.accept(this, cxt) }
        if (distinct?.onSelectItems.isNullOrEmpty()) {
            context.remove()
        }
    }

    override fun visit(fetch: Fetch?, context: SqlContext) {
        fetch?.expression?.accept(this) { fetch.expression = it as Expression? }
    }

    override fun visit(first: First?, context: SqlContext) {
        first?.jdbcParameter?.accept(this) { first.jdbcParameter = it as JdbcParameter? }
    }

    override fun visit(join: Join?, context: SqlContext) {
        join ?: return
        if (visitSingleFromItem(context, { join.rightItem }, { join.rightItem = it })) {
            return
        }
        visitExpressions(join.onExpressions as LinkedList<Expression>?)
        visitExpressions<Column>(join.usingColumns)
    }

    override fun visit(jsonFunctionExpression: JsonFunctionExpression?, context: SqlContext) {
        visitSingleExpression(context, { jsonFunctionExpression?.expression }) {
            val replacement = JsonFunctionExpression(it)
            replacement.isUsingFormatJson = jsonFunctionExpression?.isUsingFormatJson ?: false
            context.replace(replacement)
        }
    }

    override fun visit(multipleExpression: MultipleExpression?, context: SqlContext) {
        visitExpressions(multipleExpression?.list)
    }

    override fun visit(namedExpressionList: NamedExpressionList<out Expression?>?, context: SqlContext) {
        visit(namedExpressionList as ExpressionList<out Expression?>, context)
    }

    override fun visit(lateralView: LateralView?, context: SqlContext) {
        visitSingleExpression<Function?>(
            context,
            { lateralView?.generatorFunction },
            { lateralView?.generatorFunction = it }
        )
    }

    override fun visit(limit: Limit?, context: SqlContext) {
        limit?.rowCount?.accept(this) { limit.rowCount = it as Expression? }
        limit?.offset?.accept(this) { limit.offset = it as Expression? }
        visitExpressions(limit?.byExpressions)
    }

    override fun visit(offset: Offset?, context: SqlContext) {
        visitSingleExpression(context, { offset?.offset }, { offset?.offset = it })
    }

    override fun visit(skip: Skip?, context: SqlContext) {
        skip?.jdbcParameter?.accept(this) { skip.jdbcParameter = it as JdbcParameter? }
    }

    override fun visit(top: Top?, context: SqlContext) {
        visitSingleExpression(context, { top?.expression }, { top?.expression = it })
    }

    override fun visit(orderByClause: OrderByClause?, context: SqlContext) {
        visitOrderByElements(orderByClause?.orderByElements)
    }

    override fun visit(partition: Partition?, context: SqlContext) {
        partition ?: return
        if (visitSingleExpression<Column>(context, { partition.column }, { partition.column = it })) {
            return
        }
        partition.value?.accept(this) { partition.value = it as Expression? }
    }

    override fun visit(partitionByClause: PartitionByClause?, context: SqlContext) {
        visitExpressions(partitionByClause?.partitionExpressionList)
        if (partitionByClause?.partitionExpressionList.isNullOrEmpty()) {
            context.remove()
        }
    }

    override fun visit(preferringClause: PreferringClause?, context: SqlContext) {
        preferringClause ?: return
        if (visitSingleExpression(context, { preferringClause.preferring }, { preferringClause.preferring = it })) {
            return
        }
        preferringClause.partitionBy?.accept(this) { preferringClause.partitionBy = it as PartitionByClause? }
    }

    override fun visit(outputClause: OutputClause?, context: SqlContext) {
        visitList(outputClause?.selectItemList) { it, cxt -> it.accept(this, cxt) }
        outputClause?.tableVariable?.accept(this) { outputClause.tableVariable = it as UserVariable? }
        outputClause?.outputTable?.accept(this) { outputClause.outputTable = it as Table? }
    }

    override fun visit(returningClause: ReturningClause?, context: SqlContext) {
        visitList(returningClause) { it, cxt -> it.accept(this, cxt) }
    }

    override fun visit(windowDefinition: WindowDefinition?, context: SqlContext) {
        visitOrderByElements(windowDefinition?.orderByElements)
        visitExpressions(windowDefinition?.partitionExpressionList)
        windowDefinition?.windowElement?.accept(this) { windowDefinition.windowElement = it as WindowElement? }
    }

    override fun visit(windowElement: WindowElement?, context: SqlContext) {
        windowElement?.offset?.accept(this) { windowElement.offset = it as WindowOffset? }
        windowElement?.range?.accept(this) { windowElement.range = it as WindowRange? }
    }

    override fun visit(windowOffset: WindowOffset?, context: SqlContext) {
        windowOffset?.expression?.accept(this) { windowOffset.expression = it as Expression? }
    }

    override fun visit(windowRange: WindowRange?, context: SqlContext) {
        windowRange?.start?.accept(this) { windowRange.start = it as WindowOffset? }
        windowRange?.end?.accept(this) { windowRange.end = it as WindowOffset? }
    }

    override fun visit(spannerInterleaveIn: SpannerInterleaveIn, context: SqlContext) {
        visitSingleFromItem(context, { spannerInterleaveIn.table }, { spannerInterleaveIn.table = it })
    }

    override fun visit(simpleExpression: SimpleExpression, context: SqlContext) {

    }

    // -------------------------- other visitor end --------------------------

}
