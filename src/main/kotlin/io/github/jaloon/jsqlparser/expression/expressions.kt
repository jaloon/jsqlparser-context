package io.github.jaloon.jsqlparser.expression

import io.github.jaloon.jsqlparser.SqlContext
import io.github.jaloon.jsqlparser.SqlContextVisitor
import io.github.jaloon.jsqlparser.expression.operators.accept
import io.github.jaloon.jsqlparser.schema.accept
import io.github.jaloon.jsqlparser.statement.accept
import net.sf.jsqlparser.expression.*
import net.sf.jsqlparser.expression.operators.arithmetic.*
import net.sf.jsqlparser.expression.operators.conditional.AndExpression
import net.sf.jsqlparser.expression.operators.conditional.OrExpression
import net.sf.jsqlparser.expression.operators.conditional.XorExpression
import net.sf.jsqlparser.expression.operators.relational.*
import net.sf.jsqlparser.schema.Column
import net.sf.jsqlparser.statement.select.AllColumns
import net.sf.jsqlparser.statement.select.AllTableColumns
import net.sf.jsqlparser.statement.select.SubSelect

fun Expression.accept(visitor: SqlContextVisitor, context: SqlContext) {
    when (this) {
        is Addition -> accept(visitor, context)
        is AllColumns -> accept(visitor, context)
        is AllTableColumns -> accept(visitor, context)
        is AllValue -> accept(visitor, context)
        is AnalyticExpression -> accept(visitor, context)
        is AndExpression -> accept(visitor, context)
        is AnyComparisonExpression -> accept(visitor, context)
        is ArrayConstructor -> accept(visitor, context)
        is ArrayExpression -> accept(visitor, context)
        is Between -> accept(visitor, context)
        is BitwiseAnd -> accept(visitor, context)
        is BitwiseLeftShift -> accept(visitor, context)
        is BitwiseOr -> accept(visitor, context)
        is BitwiseRightShift -> accept(visitor, context)
        is BitwiseXor -> accept(visitor, context)
        is CaseExpression -> accept(visitor, context)
        is CastExpression -> accept(visitor, context)
        is CollateExpression -> accept(visitor, context)
        is Column -> accept(visitor, context)
        is Concat -> accept(visitor, context)
        is ConnectByRootOperator -> accept(visitor, context)
        is DateTimeLiteralExpression -> accept(visitor, context)
        is DateValue -> accept(visitor, context)
        is Division -> accept(visitor, context)
        is DoubleValue -> accept(visitor, context)
        is EqualsTo -> accept(visitor, context)
        is ExistsExpression -> accept(visitor, context)
        is ExtractExpression -> accept(visitor, context)
        is FullTextSearch -> accept(visitor, context)
        is net.sf.jsqlparser.expression.Function -> accept(visitor, context)
        is GeometryDistance -> accept(visitor, context)
        is GreaterThan -> accept(visitor, context)
        is GreaterThanEquals -> accept(visitor, context)
        is HexValue -> accept(visitor, context)
        is InExpression -> accept(visitor, context)
        is IntegerDivision -> accept(visitor, context)
        is IntervalExpression -> accept(visitor, context)
        is IsBooleanExpression -> accept(visitor, context)
        is IsDistinctExpression -> accept(visitor, context)
        is IsNullExpression -> accept(visitor, context)
        is JdbcNamedParameter -> accept(visitor, context)
        is JdbcParameter -> accept(visitor, context)
        is JsonAggregateFunction -> accept(visitor, context)
        is JsonExpression -> accept(visitor, context)
        is JsonFunction -> accept(visitor, context)
        is JsonOperator -> accept(visitor, context)
        is KeepExpression -> accept(visitor, context)
        is LikeExpression -> accept(visitor, context)
        is LongValue -> accept(visitor, context)
        is Matches -> accept(visitor, context)
        is MinorThan -> accept(visitor, context)
        is MinorThanEquals -> accept(visitor, context)
        is Modulo -> accept(visitor, context)
        is Multiplication -> accept(visitor, context)
        is MySQLGroupConcat -> accept(visitor, context)
        is NextValExpression -> accept(visitor, context)
        is NotEqualsTo -> accept(visitor, context)
        is NotExpression -> accept(visitor, context)
        is NullValue -> accept(visitor, context)
        is NumericBind -> accept(visitor, context)
        is OrExpression -> accept(visitor, context)
        is OracleHierarchicalExpression -> accept(visitor, context)
        is OracleHint -> accept(visitor, context)
        is OracleNamedFunctionParameter -> accept(visitor, context)
        is OverlapsCondition -> accept(visitor, context)
        is Parenthesis -> accept(visitor, context)
        is RegExpMatchOperator -> accept(visitor, context)
        is RegExpMySQLOperator -> accept(visitor, context)
        is RowConstructor -> accept(visitor, context)
        is RowGetExpression -> accept(visitor, context)
        is SafeCastExpression -> accept(visitor, context)
        is SignedExpression -> accept(visitor, context)
        is SimilarToExpression -> accept(visitor, context)
        is StringValue -> accept(visitor, context)
        is SubSelect -> accept(visitor, context)
        is Subtraction -> accept(visitor, context)
        is TimeKeyExpression -> accept(visitor, context)
        is TimeValue -> accept(visitor, context)
        is TimestampValue -> accept(visitor, context)
        is TimezoneExpression -> accept(visitor, context)
        is TryCastExpression -> accept(visitor, context)
        is UserVariable -> accept(visitor, context)
        is ValueListExpression -> accept(visitor, context)
        is VariableAssignment -> accept(visitor, context)
        is WhenClause -> accept(visitor, context)
        is XMLSerializeExpr -> accept(visitor, context)
        is XorExpression -> accept(visitor, context)
    }
}

fun AllValue.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun AnalyticExpression.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun AnyComparisonExpression.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun ArrayConstructor.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun ArrayExpression.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun CaseExpression.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun CastExpression.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun CollateExpression.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun ConnectByRootOperator.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun DateTimeLiteralExpression.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun DateValue.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun DoubleValue.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun ExtractExpression.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun net.sf.jsqlparser.expression.Function.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun HexValue.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun IntervalExpression.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun JdbcNamedParameter.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun JdbcParameter.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun JsonAggregateFunction.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun JsonExpression.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun JsonFunction.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun JsonFunctionExpression.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun KeepExpression.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun LongValue.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun MySQLGroupConcat.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun NextValExpression.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun NotExpression.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun NullValue.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun NumericBind.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun OracleHierarchicalExpression.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun OracleHint.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun OracleNamedFunctionParameter.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun OverlapsCondition.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun Parenthesis.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun RowConstructor.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun RowGetExpression.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun SafeCastExpression.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun SignedExpression.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun StringValue.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun TimeKeyExpression.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun TimeValue.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun TimestampValue.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun TimezoneExpression.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun TryCastExpression.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun UserVariable.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun ValueListExpression.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun VariableAssignment.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun WhenClause.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun XMLSerializeExpr.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun OrderByClause.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun PartitionByClause.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun WindowDefinition.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun WindowElement.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun WindowOffset.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun WindowRange.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}

fun SpannerInterleaveIn.accept(visitor: SqlContextVisitor, context: SqlContext) {
    visitor.visit(this, context)
}
