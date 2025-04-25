package io.github.jaloon.jsqlparser

import io.github.jaloon.jsqlparser.expression.SimpleExpression
import io.github.jaloon.jsqlparser.expression.accept
import io.github.jaloon.jsqlparser.expression.operators.accept
import io.github.jaloon.jsqlparser.statement.accept
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
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

/**
 * SQL上下文访问器适配器
 * @author jaloon
 * @since 4.6
 */
@Suppress("UNCHECKED_CAST")
open class SqlContextVisitorAdapter : SqlContextVisitor {
    protected fun visitBinaryExpression(expr: BinaryExpression?, context: SqlContext): Boolean {
        val changed = AtomicBoolean()
        expr?.leftExpression?.accept(this) {
            if (it == null) {
                context.replace(expr.rightExpression)
                changed.set(true)
            } else {
                expr.leftExpression = it as Expression
            }
        }
        expr?.rightExpression?.accept(this) {
            if (changed.get()) {
                context.replace(it)
            } else if (it == null) {
                context.replace(expr.leftExpression)
                changed.set(true)
            } else {
                expr.rightExpression = it as Expression
            }
        }
        return changed.get()
    }

    protected fun <T : Expression> visitExpressions(expressions: MutableList<T>?) {
        expressions ?: return
        for (i in expressions.indices.reversed()) {
            expressions[i].accept(this) {
                if (it == null) {
                    expressions.removeAt(i)
                } else {
                    expressions[i] = it as T
                }
            }
        }
    }

    protected fun <T : Statement> visitStatements(statements: MutableList<T>?) {
        statements ?: return
        for (i in statements.indices.reversed()) {
            statements[i].accept(this) {
                if (it == null) {
                    statements.removeAt(i)
                } else {
                    statements[i] = it as T
                }
            }
        }
    }

    protected fun <T : ItemsList> visitItemsLists(lists: MutableList<T>?) {
        lists ?: return
        for (i in lists.indices.reversed()) {
            lists[i].accept(this) {
                if (it == null) {
                    lists.removeAt(i)
                } else {
                    lists[i] = it as T
                }
            }
        }
    }

    protected fun <T : FromItem> visitFromItems(fromItems: MutableList<T>?) {
        fromItems ?: return
        for (i in fromItems.indices.reversed()) {
            fromItems[i].accept(this) {
                if (it == null) {
                    fromItems.removeAt(i)
                } else {
                    fromItems[i] = it as T
                }
            }
        }
    }

    protected fun <T : SelectBody> visitSelectBodies(selectBodies: MutableList<T>?) {
        selectBodies ?: return
        for (i in selectBodies.indices.reversed()) {
            selectBodies[i].accept(this) {
                if (it == null) {
                    selectBodies.removeAt(i)
                } else {
                    selectBodies[i] = it as T
                }
            }
        }
    }

    protected fun <T : SelectItem> visitSelectItems(selectItems: MutableList<T>?) {
        selectItems ?: return
        for (i in selectItems.indices.reversed()) {
            selectItems[i].accept(this) {
                if (it == null) {
                    selectItems.removeAt(i)
                } else {
                    selectItems[i] = it as T
                }
            }
        }
    }

    protected fun visitJoins(joins: MutableList<Join>?) {
        joins ?: return
        for (i in joins.indices.reversed()) {
            joins[i].accept(this) {
                if (it == null) {
                    joins.removeAt(i)
                } else {
                    joins[i] = it as Join
                }
            }
        }
    }

    protected fun visitUpdateSets(updateSets: MutableList<UpdateSet>?) {
        updateSets ?: return
        for (i in updateSets.indices.reversed()) {
            updateSets[i].accept(this) {
                if (it == null) {
                    updateSets.removeAt(i)
                } else {
                    updateSets[i] = it as UpdateSet
                }
            }
        }
    }

    protected fun visitOrderByElements(orderByElements: MutableList<OrderByElement>?) {
        orderByElements ?: return
        for (i in orderByElements.indices.reversed()) {
            orderByElements[i].accept(this) {
                if (it == null) {
                    orderByElements.removeAt(i)
                } else {
                    orderByElements[i] = it as OrderByElement
                }
            }
        }
    }

    override fun visit(bitwiseRightShift: BitwiseRightShift?, context: SqlContext) {
        visitBinaryExpression(bitwiseRightShift, context)
    }

    override fun visit(bitwiseLeftShift: BitwiseLeftShift?, context: SqlContext) {
        visitBinaryExpression(bitwiseLeftShift, context)
    }

    override fun visit(nullValue: NullValue?, context: SqlContext) {}
    override fun visit(function: Function?, context: SqlContext) {
        function?.parameters?.accept(this) { function.parameters = it as ExpressionList? }
        function?.keep?.accept(this) { function.keep = it as KeepExpression? }
        visitOrderByElements(function?.orderByElements)
    }

    override fun visit(signedExpression: SignedExpression?, context: SqlContext) {
        signedExpression?.expression?.accept(this) {
            signedExpression.expression = it as Expression? ?: SimpleExpression(null)
        }
    }

    override fun visit(jdbcParameter: JdbcParameter?, context: SqlContext) {}
    override fun visit(jdbcNamedParameter: JdbcNamedParameter?, context: SqlContext) {}
    override fun visit(doubleValue: DoubleValue?, context: SqlContext) {}
    override fun visit(longValue: LongValue?, context: SqlContext) {}
    override fun visit(hexValue: HexValue?, context: SqlContext) {}
    override fun visit(dateValue: DateValue?, context: SqlContext) {}
    override fun visit(timeValue: TimeValue?, context: SqlContext) {}
    override fun visit(timestampValue: TimestampValue?, context: SqlContext) {}
    override fun visit(parenthesis: Parenthesis?, context: SqlContext) {
        parenthesis?.expression?.accept(this) {
            if (it == null) {
                context.remove()
            } else {
                parenthesis.expression = it as Expression
            }
        }
    }

    override fun visit(stringValue: StringValue?, context: SqlContext) {}
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
        val del = AtomicBoolean()
        val ref = AtomicReference<OverlapsCondition?>()
        overlapsCondition?.left?.accept(this) {
            if (it == null) {
                context.replace(overlapsCondition.right)
                del.set(true)
            } else {
                val condition = OverlapsCondition(it as ExpressionList, overlapsCondition.right)
                context.replace(condition)
                ref.set(condition)
            }
        }
        overlapsCondition?.right?.accept(this) {
            if (del.get()) {
                context.replace(it)
            } else {
                val left = ref.get()?.left ?: overlapsCondition.left
                if (it == null) {
                    context.replace(left)
                } else {
                    context.replace(OverlapsCondition(left, it as ExpressionList))
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
        inExpression?.leftExpression?.accept(this) { inExpression.leftExpression = it as Expression? }
        if (inExpression?.rightExpression != null) {
            inExpression.rightExpression.accept(this) { inExpression.rightExpression = it as Expression? }
        } else if (inExpression?.rightItemsList != null) {
            inExpression.rightItemsList.accept(this) { inExpression.rightItemsList = it as ExpressionList? }
        }
    }

    override fun visit(fullTextSearch: FullTextSearch?, context: SqlContext) {
        fullTextSearch?.againstValue?.accept(this) {
            when (it) {
                is StringValue -> fullTextSearch.setAgainstValue(it)
                is JdbcNamedParameter -> fullTextSearch.setAgainstValue(it)
                is JdbcParameter -> fullTextSearch.setAgainstValue(it)
            }
        }
        visitExpressions<Column>(fullTextSearch?.matchColumns)
    }

    override fun visit(isNullExpression: IsNullExpression?, context: SqlContext) {
        isNullExpression?.leftExpression?.accept(this) {
            if (it == null) {
                context.remove()
            } else {
                isNullExpression.leftExpression = it as Expression
            }
        }
    }

    override fun visit(isBooleanExpression: IsBooleanExpression?, context: SqlContext) {
        isBooleanExpression?.leftExpression?.accept(this) {
            if (it == null) {
                context.remove()
            } else {
                isBooleanExpression.leftExpression = it as Expression
            }
        }
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

    override fun visit(column: Column?, context: SqlContext) {
        column?.table?.accept(this) { column.table = it as Table? }
    }

    override fun visit(table: Table?, context: SqlContext) {
        table?.pivot?.accept(this) { table.pivot = it as Pivot? }
        table?.unPivot?.accept(this) { table.unPivot = it as UnPivot? }
    }

    override fun visit(subSelect: SubSelect?, context: SqlContext) {
        subSelect?.selectBody?.accept(this) { subSelect.selectBody = it as SelectBody? }
        subSelect?.pivot?.accept(this) { subSelect.pivot = it as Pivot? }
        visitSelectBodies<WithItem>(subSelect?.withItemsList)
    }

    override fun visit(subJoin: SubJoin?, context: SqlContext) {
        subJoin?.left?.accept(this) { subJoin.left = it as FromItem? }
        visitJoins(subJoin?.joinList)
    }

    override fun visit(join: Join?, context: SqlContext) {
        join?.rightItem?.accept(this) { join.rightItem = it as FromItem? }
        visitExpressions(join?.onExpressions as LinkedList<Expression>?)
        visitExpressions<Column>(join?.usingColumns)
    }

    override fun visit(lateralSubSelect: LateralSubSelect?, context: SqlContext) {
        val removed = AtomicBoolean()
        lateralSubSelect?.subSelect?.accept(this) {
            if (it == null) {
                context.remove()
                removed.set(true)
            } else {
                lateralSubSelect.subSelect = it as SubSelect
            }
        }
        if (!removed.get()) {
            lateralSubSelect?.pivot?.accept(this) { lateralSubSelect.pivot = it as Pivot? }
            lateralSubSelect?.unPivot?.accept(this) { lateralSubSelect.unPivot = it as UnPivot? }
        }
    }

    override fun visit(valuesList: ValuesList?, context: SqlContext) {
        valuesList?.multiExpressionList?.accept(this) {
            if (it == null) {
                valuesList.multiExpressionList.expressionLists.clear()
            } else {
                valuesList.multiExpressionList = it as MultiExpressionList
            }
        }
    }

    override fun visit(tableFunction: TableFunction?, context: SqlContext) {
        tableFunction?.function?.accept(this) { tableFunction.function = it as Function? }
    }

    override fun visit(parenthesisFromItem: ParenthesisFromItem?, context: SqlContext) {
        parenthesisFromItem?.fromItem?.accept(this) {
            if (it == null) {
                context.remove()
            } else {
                parenthesisFromItem.fromItem = it as FromItem
            }
        }
    }

    override fun visit(expressionList: ExpressionList?, context: SqlContext) {
        visitExpressions(expressionList?.expressions)
    }

    override fun visit(expressionListItem: ExpressionListItem?, context: SqlContext) {
        expressionListItem?.expressionList?.accept(this) {
            if (it == null) {
                context.remove()
            } else {
                expressionListItem.expressionList = it as ExpressionList
            }
        }
    }

    override fun visit(namedExpressionList: NamedExpressionList?, context: SqlContext) {
        visitExpressions(namedExpressionList?.expressions)
    }

    override fun visit(multiExpressionList: MultiExpressionList?, context: SqlContext) {
        visitItemsLists<ExpressionList>(multiExpressionList?.expressionLists)
    }

    override fun visit(caseExpression: CaseExpression?, context: SqlContext) {
        caseExpression?.switchExpression?.accept(this) { caseExpression.switchExpression = it as Expression? }
        caseExpression?.elseExpression?.accept(this) { caseExpression.elseExpression = it as Expression? }
        visitExpressions<WhenClause>(caseExpression?.whenClauses)
    }

    override fun visit(whenClause: WhenClause?, context: SqlContext) {
        whenClause?.whenExpression?.accept(this) { whenClause.whenExpression = it as Expression? }
        whenClause?.thenExpression?.accept(this) { whenClause.thenExpression = it as Expression? }
    }

    override fun visit(existsExpression: ExistsExpression?, context: SqlContext) {
        existsExpression?.rightExpression?.accept(this) { existsExpression.rightExpression = it as Expression? }
    }

    override fun visit(anyComparisonExpression: AnyComparisonExpression?, context: SqlContext) {
        val ref = AtomicReference<SubSelect?>(anyComparisonExpression?.subSelect)
        anyComparisonExpression?.subSelect?.accept(this) {
            ref.set(it as SubSelect?)
            context.replace(AnyComparisonExpression(anyComparisonExpression.anyType, it))
        }
        if (ref.get() == null) {
            anyComparisonExpression?.itemsList?.accept(this) {
                if (it == null) {
                    context.remove()
                } else {
                    context.replace(AnyComparisonExpression(anyComparisonExpression.anyType, it as ItemsList))
                }
            }
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
        castExpression?.leftExpression?.accept(this) { castExpression.leftExpression = it as Expression? }
        castExpression?.rowConstructor?.accept(this) { castExpression.rowConstructor = it as RowConstructor? }
    }

    override fun visit(tryCastExpression: TryCastExpression?, context: SqlContext) {
        tryCastExpression?.leftExpression?.accept(this) { tryCastExpression.leftExpression = it as Expression? }
        tryCastExpression?.rowConstructor?.accept(this) { tryCastExpression.rowConstructor = it as RowConstructor? }
    }

    override fun visit(safeCastExpression: SafeCastExpression?, context: SqlContext) {
        safeCastExpression?.leftExpression?.accept(this) { safeCastExpression.leftExpression = it as Expression? }
        safeCastExpression?.rowConstructor?.accept(this) { safeCastExpression.rowConstructor = it as RowConstructor? }
    }

    override fun visit(modulo: Modulo?, context: SqlContext) {
        visitBinaryExpression(modulo, context)
    }

    override fun visit(analyticExpression: AnalyticExpression?, context: SqlContext) {
        analyticExpression?.expression?.accept(this) { analyticExpression.expression = it as Expression? }
        analyticExpression?.defaultValue?.accept(this) { analyticExpression.defaultValue = it as Expression? }
        analyticExpression?.offset?.accept(this) { analyticExpression.offset = it as Expression? }
        analyticExpression?.keep?.accept(this) { analyticExpression.keep = it as KeepExpression? }
        analyticExpression?.filterExpression?.accept(this) { analyticExpression.filterExpression = it as Expression? }
        visitOrderByElements(analyticExpression?.funcOrderBy)
        analyticExpression?.windowDefinition?.accept(this) {
            analyticExpression.windowDefinition = it as WindowDefinition?
        }
    }

    override fun visit(extractExpression: ExtractExpression?, context: SqlContext) {
        extractExpression?.expression?.accept(this) { extractExpression.expression = it as Expression? }
    }

    override fun visit(intervalExpression: IntervalExpression?, context: SqlContext) {
        intervalExpression?.expression?.accept(this) { intervalExpression.expression = it as Expression? }
    }

    override fun visit(oracleHierarchicalExpression: OracleHierarchicalExpression?, context: SqlContext) {
        oracleHierarchicalExpression?.startExpression?.accept(this) {
            oracleHierarchicalExpression.startExpression = it as Expression?
        }
        oracleHierarchicalExpression?.connectExpression?.accept(this) {
            oracleHierarchicalExpression.connectExpression = it as Expression?
        }
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

    override fun visit(regExpMySQLOperator: RegExpMySQLOperator?, context: SqlContext) {
        visitBinaryExpression(regExpMySQLOperator, context)
    }

    override fun visit(userVariable: UserVariable?, context: SqlContext) {}
    override fun visit(numericBind: NumericBind?, context: SqlContext) {}
    override fun visit(keepExpression: KeepExpression?, context: SqlContext) {
        visitOrderByElements(keepExpression?.orderByElements)
    }

    override fun visit(mySQLGroupConcat: MySQLGroupConcat?, context: SqlContext) {
        mySQLGroupConcat?.expressionList?.accept(this) { mySQLGroupConcat.expressionList = it as ExpressionList? }
        visitOrderByElements(mySQLGroupConcat?.orderByElements)
    }

    override fun visit(valueListExpression: ValueListExpression?, context: SqlContext) {
        valueListExpression?.expressionList?.accept(this) { valueListExpression.expressionList = it as ExpressionList? }
    }

    override fun visit(rowConstructor: RowConstructor?, context: SqlContext) {
        rowConstructor?.exprList?.accept(this) { rowConstructor.exprList = it as ExpressionList? }
    }

    override fun visit(rowGetExpression: RowGetExpression?, context: SqlContext) {
        rowGetExpression?.expression?.accept(this) { rowGetExpression.expression = it as Expression? }
    }

    override fun visit(oracleHint: OracleHint?, context: SqlContext) {}
    override fun visit(timeKeyExpression: TimeKeyExpression?, context: SqlContext) {}
    override fun visit(dateTimeLiteralExpression: DateTimeLiteralExpression?, context: SqlContext) {}
    override fun visit(notExpression: NotExpression?, context: SqlContext) {
        notExpression?.expression?.accept(this) {
            if (it == null) {
                context.remove()
            } else {
                notExpression.expression = it as Expression
            }
        }
    }

    override fun visit(nextValExpression: NextValExpression?, context: SqlContext) {}
    override fun visit(collateExpression: CollateExpression?, context: SqlContext) {
        collateExpression?.leftExpression?.accept(this) {
            if (it == null) {
                context.remove()
            } else {
                collateExpression.leftExpression = it as Expression
            }
        }
    }

    override fun visit(similarToExpression: SimilarToExpression?, context: SqlContext) {
        visitBinaryExpression(similarToExpression, context)
    }

    override fun visit(arrayExpression: ArrayExpression?, context: SqlContext) {
        arrayExpression?.objExpression?.accept(this) { arrayExpression.objExpression = it as Expression? }
        arrayExpression?.indexExpression?.accept(this) { arrayExpression.indexExpression = it as Expression? }
        arrayExpression?.startIndexExpression?.accept(this) { arrayExpression.startIndexExpression = it as Expression? }
        arrayExpression?.stopIndexExpression?.accept(this) { arrayExpression.stopIndexExpression = it as Expression? }
    }

    override fun visit(arrayConstructor: ArrayConstructor?, context: SqlContext) {
        visitExpressions(arrayConstructor?.expressions)
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
        jsonAggregateFunction?.expression?.accept(this) { jsonAggregateFunction.expression = it as Expression? }
        visitOrderByElements(jsonAggregateFunction?.expressionOrderByElements)
        visitOrderByElements(jsonAggregateFunction?.orderByElements)
        jsonAggregateFunction?.partitionExpressionList?.accept(this) {
            if (it == null) {
                jsonAggregateFunction.partitionExpressionList.expressions = null
            } else {
                jsonAggregateFunction.partitionExpressionList = it as ExpressionList?
            }
        }
        jsonAggregateFunction?.filterExpression?.accept(this) {
            jsonAggregateFunction.filterExpression = it as Expression?
        }
        jsonAggregateFunction?.windowElement?.accept(this) {
            jsonAggregateFunction.windowElement = it as WindowElement?
        }
    }

    override fun visit(jsonFunction: JsonFunction?, context: SqlContext) {
        val jsonFunctionExpressions = jsonFunction?.expressions ?: return
        for (i in jsonFunctionExpressions.indices.reversed()) {
            jsonFunctionExpressions[i].accept(this) {
                if (it == null) {
                    jsonFunctionExpressions.removeAt(i)
                } else {
                    jsonFunctionExpressions[i] = it as JsonFunctionExpression
                }
            }
        }
    }

    override fun visit(jsonFunctionExpression: JsonFunctionExpression?, context: SqlContext) {
        jsonFunctionExpression?.expression?.accept(this) {
            if (it == null) {
                context.remove()
            } else {
                context.replace(JsonFunctionExpression(it as Expression))
            }
        }
    }

    override fun visit(connectByRootOperator: ConnectByRootOperator?, context: SqlContext) {
        connectByRootOperator?.column?.accept(this) {
            if (it == null) {
                context.remove()
            } else {
                context.replace(ConnectByRootOperator(it as Column))
            }
        }
    }

    override fun visit(oracleNamedFunctionParameter: OracleNamedFunctionParameter?, context: SqlContext) {
        oracleNamedFunctionParameter?.expression?.accept(this) {
            context.replace(OracleNamedFunctionParameter(oracleNamedFunctionParameter.name, it as Expression?))
        }
    }

    override fun visit(allColumns: AllColumns?, context: SqlContext) {}
    override fun visit(allTableColumns: AllTableColumns?, context: SqlContext) {
        allTableColumns?.table?.accept(this) {
            if (it == null) {
                context.replace(AllColumns())
            } else {
                context.replace(AllTableColumns(it as Table))
            }
        }
    }

    override fun visit(selectExpressionItem: SelectExpressionItem?, context: SqlContext) {
        selectExpressionItem?.expression?.accept(this) {
            if (it == null) {
                context.remove()
            } else {
                selectExpressionItem.expression = it as Expression
            }
        }
    }

    override fun visit(allValue: AllValue?, context: SqlContext) {}
    override fun visit(isDistinctExpression: IsDistinctExpression?, context: SqlContext) {
        visitBinaryExpression(isDistinctExpression, context)
    }

    override fun visit(geometryDistance: GeometryDistance?, context: SqlContext) {
        visitBinaryExpression(geometryDistance, context)
    }

    override fun visit(analyze: Analyze?, context: SqlContext) {
        analyze?.table?.accept(this) {
            if (it == null) {
                context.remove()
            } else {
                analyze.table = it as Table
            }
        }
    }

    override fun visit(savepointStatement: SavepointStatement?, context: SqlContext) {}
    override fun visit(rollbackStatement: RollbackStatement?, context: SqlContext) {}
    override fun visit(comment: Comment?, context: SqlContext) {
        comment?.table?.accept(this) { comment.table = it as Table }
        comment?.column?.accept(this) { comment.column = it as Column }
        comment?.view?.accept(this) { comment.view = it as Table }
        comment?.comment?.accept(this) { comment.comment = it as StringValue }
    }

    override fun visit(commit: Commit?, context: SqlContext) {}
    override fun visit(delete: Delete?, context: SqlContext) {
        visitSelectBodies<WithItem>(delete?.withItemsList)
        delete?.table?.accept(this) { delete.table = it as Table? }
        visitFromItems<Table>(delete?.tables)
        visitFromItems<Table>(delete?.usingList)
        visitJoins(delete?.joins)
        delete?.where?.accept(this) { delete.where = it as Expression? }
        delete?.limit?.accept(this) { delete.limit = it as Limit? }
        visitOrderByElements(delete?.orderByElements)
        visitSelectItems(delete?.returningExpressionList)
        delete?.outputClause?.accept(this) { delete.outputClause = it as OutputClause? }
    }

    override fun visit(update: Update?, context: SqlContext) {
        visitSelectBodies<WithItem>(update?.withItemsList)
        update?.table?.accept(this) { update.table = it as Table? }
        update?.where?.accept(this) { update.where = it as Expression? }
        visitUpdateSets(update?.updateSets)
        update?.fromItem?.accept(this) { update.fromItem = it as FromItem? }
        visitJoins(update?.joins)
        visitJoins(update?.startJoins)
        visitOrderByElements(update?.orderByElements)
        update?.limit?.accept(this) { update.limit = it as Limit? }
        visitSelectItems(update?.returningExpressionList)
        update?.outputClause?.accept(this) { update.outputClause = it as OutputClause? }
    }

    override fun visit(updateSet: UpdateSet?, context: SqlContext) {
        visitExpressions<Column>(updateSet?.columns)
        visitExpressions(updateSet?.expressions)
    }

    override fun visit(insert: Insert?, context: SqlContext) {
        insert?.table?.accept(this) { insert.table = it as Table? }
        insert?.oracleHint?.accept(this) { insert.oracleHint = it as OracleHint? }
        visitExpressions<Column>(insert?.columns)
        insert?.select?.accept(this) { insert.select = it as Select? }
        visitExpressions<Column>(insert?.duplicateUpdateColumns)
        visitExpressions(insert?.duplicateUpdateExpressionList)
        visitSelectItems(insert?.returningExpressionList)
        visitExpressions<Column>(insert?.setColumns)
        visitExpressions(insert?.setExpressionList)
        visitSelectBodies<WithItem>(insert?.withItemsList)
        insert?.outputClause?.accept(this) { insert.outputClause = it as OutputClause? }
        insert?.conflictTarget?.accept(this) { insert.conflictTarget = it as InsertConflictTarget? }
        insert?.conflictAction?.accept(this) { insert.conflictAction = it as InsertConflictAction? }
    }

    override fun visit(insertConflictAction: InsertConflictAction?, context: SqlContext) {
        visitUpdateSets(insertConflictAction?.updateSets)
        insertConflictAction?.whereExpression?.accept(this) { insertConflictAction.whereExpression = it as Expression? }
    }

    override fun visit(insertConflictTarget: InsertConflictTarget?, context: SqlContext) {
        insertConflictTarget?.indexExpression?.accept(this) { insertConflictTarget.indexExpression = it as Expression? }
        insertConflictTarget?.whereExpression?.accept(this) { insertConflictTarget.whereExpression = it as Expression? }
    }

    override fun visit(drop: Drop?, context: SqlContext) {
        drop?.name?.accept(this) { drop.name = it as Table? }
    }

    override fun visit(truncate: Truncate?, context: SqlContext) {
        truncate?.table?.accept(this) { truncate.table = it as Table? }
    }

    override fun visit(createIndex: CreateIndex?, context: SqlContext) {
        createIndex?.table?.accept(this) { createIndex.table = it as Table? }
    }

    override fun visit(createSchema: CreateSchema?, context: SqlContext) {
        visitStatements(createSchema?.statements)
    }

    override fun visit(createTable: CreateTable?, context: SqlContext) {
        createTable?.table?.accept(this) { createTable.table = it as Table? }
        createTable?.select?.accept(this) { createTable.setSelect(it as Select?, createTable.isSelectParenthesis) }
        createTable?.likeTable?.accept(this) { createTable.setLikeTable(it as Table?, createTable.isSelectParenthesis) }
        createTable?.spannerInterleaveIn?.accept(this) { createTable.spannerInterleaveIn = it as SpannerInterleaveIn? }
    }

    override fun visit(createView: CreateView?, context: SqlContext) {
        createView?.view?.accept(this) { createView.view = it as Table? }
        createView?.select?.accept(this) { createView.select = it as Select? }
    }

    override fun visit(alterView: AlterView?, context: SqlContext) {
        alterView?.view?.accept(this) { alterView.view = it as Table? }
        alterView?.selectBody?.accept(this) { alterView.selectBody = it as SelectBody? }
    }

    override fun visit(alter: Alter?, context: SqlContext) {
        alter?.table?.accept(this) { alter.table = it as Table? }
    }

    override fun visit(statements: Statements?, context: SqlContext) {
        visitStatements(statements?.statements)
    }

    override fun visit(execute: Execute?, context: SqlContext) {
        execute?.exprList?.accept(this) { execute.exprList = it as ExpressionList? }
    }

    override fun visit(setStatement: SetStatement?, context: SqlContext) {
        setStatement ?: return
        for (i in 0 until setStatement.count) {
            visitExpressions(setStatement.getExpressions(i))
        }
    }

    override fun visit(resetStatement: ResetStatement?, context: SqlContext) {}
    override fun visit(showColumnsStatement: ShowColumnsStatement?, context: SqlContext) {}
    override fun visit(showIndexStatement: ShowIndexStatement?, context: SqlContext) {}
    override fun visit(showTablesStatement: ShowTablesStatement?, context: SqlContext) {
        showTablesStatement?.likeExpression?.accept(this) { showTablesStatement.likeExpression = it as Expression? }
        showTablesStatement?.whereCondition?.accept(this) { showTablesStatement.whereCondition = it as Expression? }
    }

    override fun visit(merge: Merge?, context: SqlContext) {
        visitSelectBodies<WithItem>(merge?.withItemsList)
        merge?.table?.accept(this) { merge.table = it as Table? }
        merge?.oracleHint?.accept(this) { merge.oracleHint = it as OracleHint? }
        merge?.usingTable?.accept(this) { merge.usingTable = it as Table? }
        merge?.usingSelect?.accept(this) { merge.usingSelect = it as SubSelect? }
        merge?.onCondition?.accept(this) { merge.onCondition = it as Expression? }
        merge?.mergeInsert?.accept(this) { merge.mergeInsert = it as MergeInsert? }
        merge?.mergeUpdate?.accept(this) { merge.mergeUpdate = it as MergeUpdate? }
    }

    override fun visit(mergeInsert: MergeInsert?, context: SqlContext) {
        visitExpressions<Column>(mergeInsert?.columns)
        visitExpressions(mergeInsert?.values)
        mergeInsert?.whereCondition?.accept(this) { mergeInsert.whereCondition = it as Expression? }
    }

    override fun visit(mergeUpdate: MergeUpdate?, context: SqlContext) {
        visitExpressions<Column>(mergeUpdate?.columns)
        visitExpressions(mergeUpdate?.values)
        mergeUpdate?.whereCondition?.accept(this) { mergeUpdate.whereCondition = it as Expression? }
        mergeUpdate?.deleteWhereCondition?.accept(this) { mergeUpdate.deleteWhereCondition = it as Expression? }
    }

    override fun visit(select: Select?, context: SqlContext) {
        select?.selectBody?.accept(this) { select.selectBody = it as SelectBody? }
        visitSelectBodies<WithItem>(select?.withItemsList)
    }

    override fun visit(upsert: Upsert?, context: SqlContext) {
        upsert?.table?.accept(this) { upsert.table = it as Table? }
        visitExpressions<Column>(upsert?.columns)
        upsert?.itemsList?.accept(this) { upsert.itemsList = it as ItemsList? }
        upsert?.select?.accept(this) { upsert.select = it as Select? }
        visitExpressions<Column>(upsert?.duplicateUpdateColumns)
        visitExpressions(upsert?.duplicateUpdateExpressionList)
    }

    override fun visit(useStatement: UseStatement?, context: SqlContext) {}
    override fun visit(block: Block?, context: SqlContext) {
        block?.statements?.accept(this) { block.statements = it as Statements? }
    }

    override fun visit(distinct: Distinct?, context: SqlContext) {
        visitSelectItems(distinct?.onSelectItems)
    }

    override fun visit(plainSelect: PlainSelect?, context: SqlContext) {
        plainSelect?.distinct?.accept(this) { plainSelect.distinct = it as Distinct? }
        visitSelectItems(plainSelect?.selectItems)
        visitFromItems<Table>(plainSelect?.intoTables)
        plainSelect?.fromItem?.accept(this) { plainSelect.fromItem = it as FromItem? }
        visitJoins(plainSelect?.joins)
        plainSelect?.where?.accept(this) { plainSelect.where = it as Expression? }
        plainSelect?.groupBy?.accept(this) { plainSelect.setGroupByElement(it as GroupByElement?) }
        visitOrderByElements(plainSelect?.orderByElements)
        plainSelect?.having?.accept(this) { plainSelect.having = it as Expression? }
        plainSelect?.limit?.accept(this) { plainSelect.limit = it as Limit? }
        plainSelect?.offset?.accept(this) { plainSelect.offset = it as Offset? }
        plainSelect?.oracleHierarchical?.accept(this) {
            plainSelect.oracleHierarchical = it as OracleHierarchicalExpression?
        }
        plainSelect?.oracleHint?.accept(this) { plainSelect.oracleHint = it as OracleHint? }
        plainSelect?.forUpdateTable?.accept(this) { plainSelect.forUpdateTable = it as Table? }
        val windowDefinitions = plainSelect?.windowDefinitions ?: return
        for (i in windowDefinitions.indices.reversed()) {
            windowDefinitions[i].accept(this) {
                if (it == null) {
                    windowDefinitions.removeAt(i)
                } else {
                    windowDefinitions[i] = it as WindowDefinition
                }
            }
        }
    }

    override fun visit(setOperationList: SetOperationList?, context: SqlContext) {
        visitSelectBodies(setOperationList?.selects)
        visitOrderByElements(setOperationList?.orderByElements)
        setOperationList?.limit?.accept(this) { setOperationList.limit = it as Limit? }
        setOperationList?.offset?.accept(this) { setOperationList.offset = it as Offset? }
    }

    override fun visit(withItem: WithItem?, context: SqlContext) {
        visitSelectItems(withItem?.withItemList)
        withItem?.itemsList?.accept(this) { withItem.itemsList = it as ItemsList? }
        withItem?.subSelect?.accept(this) { withItem.subSelect = it as SubSelect? }
    }

    override fun visit(valuesStatement: ValuesStatement?, context: SqlContext) {
        valuesStatement?.expressions?.accept(this) { valuesStatement.expressions = it as ItemsList? }
    }

    override fun visit(describeStatement: DescribeStatement?, context: SqlContext) {
        describeStatement?.table?.accept(this) { describeStatement.table = it as Table? }
    }

    override fun visit(explainStatement: ExplainStatement?, context: SqlContext) {
        explainStatement?.statement?.accept(this) { explainStatement.statement = it as Select? }
    }

    override fun visit(showStatement: ShowStatement?, context: SqlContext) {}
    override fun visit(declareStatement: DeclareStatement?, context: SqlContext) {
        declareStatement?.userVariable?.accept(this) { declareStatement.userVariable = it as UserVariable? }
    }

    override fun visit(grant: Grant?, context: SqlContext) {}
    override fun visit(createSequence: CreateSequence?, context: SqlContext) {}
    override fun visit(alterSequence: AlterSequence?, context: SqlContext) {}
    override fun visit(createFunctionalStatement: CreateFunctionalStatement?, context: SqlContext) {}
    override fun visit(createSynonym: CreateSynonym?, context: SqlContext) {}
    override fun visit(alterSession: AlterSession?, context: SqlContext) {}
    override fun visit(ifElseStatement: IfElseStatement?, context: SqlContext) {
        ifElseStatement?.elseStatement?.accept(this) { ifElseStatement.elseStatement = it as Statement? }
    }

    override fun visit(renameTableStatement: RenameTableStatement?, context: SqlContext) {}
    override fun visit(purgeStatement: PurgeStatement?, context: SqlContext) {}
    override fun visit(alterSystemStatement: AlterSystemStatement?, context: SqlContext) {}
    override fun visit(unsupportedStatement: UnsupportedStatement?, context: SqlContext) {}
    override fun visit(groupByElement: GroupByElement?, context: SqlContext) {
        groupByElement?.groupByExpressionList?.accept(this) {
            groupByElement.groupByExpressionList = it as ExpressionList?
        }
    }

    override fun visit(orderByElement: OrderByElement?, context: SqlContext) {
        orderByElement?.expression?.accept(this) { orderByElement.expression = it as Expression? }
    }

    override fun visit(pivot: Pivot?, context: SqlContext) {
        if (pivot?.functionItems != null) {
            for (i in pivot.functionItems.indices.reversed()) {
                pivot.functionItems[i].accept(this) {
                    if (it == null) {
                        pivot.functionItems.removeAt(i)
                    } else {
                        pivot.functionItems[i] = it as FunctionItem
                    }
                }
            }
        }
        visitExpressions<Column>(pivot?.forColumns)
        visitSelectItems<SelectExpressionItem>(pivot?.singleInItems)
        if (pivot?.multiInItems != null) {
            for (i in pivot.multiInItems.indices.reversed()) {
                pivot.multiInItems[i].accept(this) {
                    if (it == null) {
                        pivot.multiInItems.removeAt(i)
                    } else {
                        pivot.multiInItems[i] = it as ExpressionListItem
                    }
                }
            }
        }
    }

    override fun visit(pivotXml: PivotXml?, context: SqlContext) {
        visit(pivotXml as Pivot?, context)
        pivotXml?.inSelect?.accept(this) { pivotXml.inSelect = it as SelectBody? }
    }

    override fun visit(unPivot: UnPivot?, context: SqlContext) {
        visitExpressions<Column>(unPivot?.unPivotClause)
        visitExpressions<Column>(unPivot?.unPivotForClause)
        visitSelectItems<SelectExpressionItem>(unPivot?.unPivotInClause)
    }

    override fun visit(limit: Limit?, context: SqlContext) {
        limit?.offset?.accept(this) { limit.offset = it as Expression? }
        limit?.rowCount?.accept(this) { limit.rowCount = it as Expression? }
    }

    override fun visit(offset: Offset?, context: SqlContext) {
        offset?.offset?.accept(this) { offset.offset = it as Expression? }
    }

    override fun visit(orderByClause: OrderByClause?, context: SqlContext) {
        visitOrderByElements(orderByClause?.orderByElements)
    }

    override fun visit(partitionByClause: PartitionByClause?, context: SqlContext) {
        partitionByClause?.partitionExpressionList?.accept(this) {
            partitionByClause.partitionExpressionList = it as ExpressionList?
        }
    }

    override fun visit(outputClause: OutputClause?, context: SqlContext) {
        visitSelectItems(outputClause?.selectItemList)
        outputClause?.tableVariable?.accept(this) { outputClause.tableVariable = it as UserVariable? }
        outputClause?.outputTable?.accept(this) { outputClause.outputTable = it as Table? }
    }

    override fun visit(windowDefinition: WindowDefinition?, context: SqlContext) {
        windowDefinition?.orderBy?.accept(this) {
            if (it == null) {
                windowDefinition.orderBy.orderByElements = null
            } else {
                windowDefinition.orderBy.orderByElements = (it as OrderByClause).orderByElements
            }
        }
        windowDefinition?.partitionBy?.accept(this) {
            if (it == null) {
                windowDefinition.partitionBy.partitionExpressionList = null
            } else {
                val other = it as PartitionByClause
                windowDefinition.partitionBy.setPartitionExpressionList(other.partitionExpressionList, other.isBrackets)
            }
        }
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
        spannerInterleaveIn.table?.accept(this) { spannerInterleaveIn.table = it as Table? }
    }

    override fun visit(simpleExpression: SimpleExpression, context: SqlContext) {}
}
