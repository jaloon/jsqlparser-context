package io.github.jaloon.jsqlparser

/**
 * SQL 上下文
 *
 * @author jaloon
 * @since 4.6
 */
@FunctionalInterface
fun interface SqlContext {
    /**
     * 移除节点
     */
    fun remove() {
        replace(null)
    }

    /**
     * 替换节点
     */
    fun replace(replacement: Any?)
}
