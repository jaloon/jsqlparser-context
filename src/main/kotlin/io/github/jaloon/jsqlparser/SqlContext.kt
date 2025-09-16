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

    /**
     * @since 5.3
     */
    companion object {
        /**
         * 默认上下文 (不进行任何操作)
         *
         * @since 5.3
         */
        @JvmField
        val DO_NOTHING: SqlContext = SqlContext {}
    }
}
