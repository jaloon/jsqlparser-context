package io.github.jaloon.jsqlparser.test.visitor

import io.github.jaloon.jsqlparser.SqlContext
import net.sf.jsqlparser.statement.merge.MergeDelete
import net.sf.jsqlparser.statement.merge.MergeInsert
import net.sf.jsqlparser.statement.merge.MergeOperationVisitor
import net.sf.jsqlparser.statement.merge.MergeUpdate

/**
 * @see MergeOperationVisitor
 */
interface MergeOperationContextVisitor {
    fun visit(mergeDelete: MergeDelete?, context: SqlContext)
    fun visit(mergeUpdate: MergeUpdate?, context: SqlContext)
    fun visit(mergeInsert: MergeInsert?, context: SqlContext)
} 
