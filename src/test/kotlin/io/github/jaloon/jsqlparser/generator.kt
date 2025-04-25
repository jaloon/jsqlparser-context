package io.github.jaloon.jsqlparser

import net.sf.jsqlparser.expression.Expression
import net.sf.jsqlparser.expression.operators.relational.ItemsList
import net.sf.jsqlparser.statement.Statement
import net.sf.jsqlparser.statement.select.FromItem
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ConfigurationBuilder
import java.util.jar.JarFile

fun main() {
//    visitors()
//    getJarFile()
    getSubClass()
}

fun visitors() {
    val s = "StatementVisitor, ExpressionVisitor, ItemsListVisitor, SelectVisitor, SelectItemVisitor, FromItemVisitor, GroupByVisitor, OrderByVisitor, PivotVisitor"
    s.split(',')
        .forEach {
            println("* @see $it")
        }
}

fun getJarFile() {
    val jarPath = "/home/chenl/.m2/repository/com/github/jsqlparser/jsqlparser/4.6/jsqlparser-4.6.jar"
//    val pkgPath = "net/sf/jsqlparser/statement/"
    val pkgPath = "net/sf/jsqlparser/statement/select/"
//    val pkgPath = "net/sf/jsqlparser/schema/"
//    val pkgPath = "net/sf/jsqlparser/expression/"
//    val pkgPath0 = "net/sf/jsqlparser/expression/operators/"
//    val pkgPath1 = "net/sf/jsqlparser/expression/operators/arithmetic/"
//    val pkgPath2 = "net/sf/jsqlparser/expression/operators/conditional/"
//    val pkgPath3 = "net/sf/jsqlparser/expression/operators/relational/"
    val clsSuffix = ".class"
    val jarFile = JarFile(jarPath)
    val entries = jarFile.entries()
    println()
    while (entries.hasMoreElements()) {
        val entry = entries.nextElement()
        if (entry.name.startsWith(pkgPath) && entry.name.endsWith(clsSuffix)) {
            val clsName = entry.name.substring(pkgPath.length, entry.name.length - clsSuffix.length)
            if (clsName.indexOf('$') > 0 || clsName.indexOf('/') > 0) {
                continue
            }
            val code = """
                fun ${clsName}.accept(visitor: SqlContextVisitor, context: SqlContext) {
                    visitor.visit(this, context)
                }
            """.trimIndent()
            println(code)
            println()
        }
    }
//            "!/net/sf/jsqlparser/expression/operators/conditional/AndExpression.class"
//    var resources = Thread.currentThread().contextClassLoader.getResources("net/sf/jsqlparser")
//    while (resources.hasMoreElements()) {
//        val url = resources.nextElement()
//        if (url.protocol == "jar") {
//            val jarUrlConnection = url.openConnection() as JarURLConnection
//            val jarFile = jarUrlConnection.jarFile
//            val entries = jarFile.entries()
//            while (entries.hasMoreElements()) {
//                val entry = entries.nextElement()
//                println(entry.name)
//            }
//        }
//    }
}

fun getSubClass() {
    val reflections = Reflections(
        ConfigurationBuilder()
            .forPackage("net.sf.jsqlparser")    // 添加子类扫描工具 SubTypesScanner
            .addScanners(Scanners.SubTypes)         // 添加 属性注解扫描工具 FieldAnnotationsScanner
        // .addScanners(Scanners.FieldsAnnotated)
        // // 添加 方法注解扫描工具 MethodAnnotationsScanner
        // .addScanners(Scanners.MethodsAnnotated, Scanners.ConstructorsAnnotated )
        // // 添加 方法参数扫描工具 MethodParameterScanner
        // .addScanners(Scanners.MethodsParameter, Scanners.MethodsSignature, Scanners.MethodsReturn, Scanners.ConstructorsParameter, Scanners.ConstructorsSignature)
    )
//    reflections.getSubTypesOf(Expression::class.java)
//    reflections.getSubTypesOf(ItemsList::class.java)
//    reflections.getSubTypesOf(FromItem::class.java)
    reflections.getSubTypesOf(Statement::class.java)
        .map {
            it.simpleName
        }
        .sorted()
        .forEach {
            println("is $it -> accept(visitor, context)")
        }
}