package com.plugin.demo

import com.google.common.base.CaseFormat
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction

/**
 * 获取了用户鼠标选中的文本，并将文本在下划线、驼峰之间进行转换
 */
class CaseFormatAction: AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        // 获取编辑对象
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return
        // selectionModel 表示获取当前编辑器所选内容，selectedText 是鼠标选中的文本
        val text = editor.selectionModel.selectedText ?: return
        val result = if (text.contains("_")) {
            // 选中的文本包含下划线，将其转换为大写驼峰表示法
            CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, text)
        } else if (Character.isUpperCase(text[0])) {
            // 第一个字符为大写，将其转换为小写驼峰表示法
            CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, text)
        } else {
            CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, text)
        }

        // currentCaret 可以获取到鼠标当前的插入符号
        // 我们也可以通过 primaryCaret() 方法获取到主插入符号对象，实现一样的效果
        val currentCaret = editor.caretModel.currentCaret
        // 获取到鼠标选中文本开始位置偏移量
        val start = currentCaret.selectionStart
        // 获取到鼠标选中文本结束位置偏移量
        val end = currentCaret.selectionEnd
        // 获取到当前编辑器文件的 Document 对象
        val document = editor.document
        // 对文件内容进行修改
        WriteCommandAction.runWriteCommandAction(e.project) {
            document.replaceString(start, end, result)
        }
    }

}