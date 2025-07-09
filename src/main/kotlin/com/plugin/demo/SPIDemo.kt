package com.plugin.demo

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.refactoring.RefactoringFactory
import org.jetbrains.kotlin.idea.structuralsearch.visitor.KotlinRecursiveElementVisitor
import org.jetbrains.kotlin.psi.KtImportDirective
import org.jetbrains.kotlin.psi.KtNamedFunction


/**
 * PSI(Program Structure Interface)，即程序结构接口，是 IntelliJ 平台
 * 给我们提供用来解析代码文件，简化对各类编程语言（Java、Kotlin、XML）操作的接口。
 * https://plugins.jetbrains.com/docs/intellij/psi.html
 */


class GetSPIInfoAction: AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        showPSIPackageName(e)
        showPSIMethodName(e)
        showAndRefactor(e)
    }

    /**
     * 获取 psi 文件中所有的 package
     */
    private fun showPSIPackageName(e: AnActionEvent) {
        val result = StringBuilder()
        // 获取 psi 文件
        val psiFile = e.getData(CommonDataKeys.PSI_FILE) ?: return
        // accept 方法用于遍历访问 psi 文件中的元素
        psiFile.accept(object : KotlinRecursiveElementVisitor() {

            override fun visitImportDirective(importDirective: KtImportDirective) {
                result.append(importDirective.importedFqName?.toString() ?: "")
            }
        })
        Notifications.Bus.notify(
            Notification("Print", "PSIDemo showPSIPackageName", result.toString(), NotificationType.INFORMATION),
            e.project
        )
    }

    /**
     * 获取类中的方法
     */
    private fun showPSIMethodName(e: AnActionEvent) {
        val result = StringBuilder()
        // 获取 psi 文件
        val psiFile = e.getData(CommonDataKeys.PSI_FILE) ?: return
        // accept 方法用于遍历访问 psi 文件中的元素
        psiFile.accept(object : KotlinRecursiveElementVisitor() {

            override fun visitNamedFunction(function: KtNamedFunction) {
                // 拼接包名
                result.append(function.name ?: "")
                    .append("<br>")

                function.annotations
            }
        })
        Notifications.Bus.notify(
            Notification("Print", "PSIDemo showPSIGetterName", result.toString(), NotificationType.INFORMATION),
            e.project
        )
    }

    /**
     * 修改鼠标所在位置的名字
     * 具体关于 psi 的结构，比如 element.parent.parent 可以使用 PsiViewer 插件查看
     * 问题：选中代码块中的方法，此时 e.getData(CommonDataKeys.PSI_ELEMENT) 返回的是
     * 方法在类中定义的 FUN 类型，而不是调用方法时的 REFERENCE_EXPRESSION 参数表达式类型
     */
    private fun showAndRefactor(e: AnActionEvent) {
        val element = e.getData(CommonDataKeys.PSI_ELEMENT) ?: return
        // 我们也可以通过插入符号位置获取到当前位置的 PsiElement
        // PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        // PsiElement element = psiFile.findElementAt(offset);
        // 重命名
        RefactoringFactory.getInstance(e.project).createRename(element, "newName").run()
    }

}