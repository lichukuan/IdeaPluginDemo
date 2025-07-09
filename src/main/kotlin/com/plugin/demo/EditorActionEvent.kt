package com.plugin.demo

import com.alibaba.fastjson.JSON
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorActionHandler
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.vfs.VirtualFile
import wu.seal.jsontokotlin.library.JsonToKotlinBuilder
import java.awt.datatransfer.DataFlavor

/**
 * 监听编辑器事件
 */

/**
 * 插件监听编辑器粘贴事件，判断粘贴内容是否为 JSON 字符串，若是则转为 Java 类文件，否则直接粘贴内容
 */
class EditorPasteListener: EditorActionHandler() {

    override fun doExecute(editor: Editor, caret: Caret?, dataContext: DataContext?) {
        val text = CopyPasteManager.getInstance()
            .getContents<String>(DataFlavor.stringFlavor)
        if (text.isNullOrBlank()) return
        runCatching {
            JSON.parse(text)
        }.onFailure {
            // 解析失败，则不是json格式
            // 执行粘贴逻辑，直接在插入符号偏移量位置写入剪切板文本
            WriteCommandAction.runWriteCommandAction(ProjectManager.getInstance().defaultProject) {
                editor.document.insertString(editor.caretModel.offset, text)
            }
            return
        }

        // 通过 dataContext 获取当前上下文的虚拟文件对象，
        val file = dataContext?.getData(CommonDataKeys.VIRTUAL_FILE) ?: return

        // 获取当前文件所在文件夹，文件的父级一定是一个目录
        val parentFile = file.parent

        // json 转化为 kotlin
        val output = JsonToKotlinBuilder()
            .setPackageName(parentFile.getPackageName())
            .build(text, file.getFileName())

        WriteCommandAction.runWriteCommandAction(ProjectManager.getInstance().defaultProject) {
            editor.document.insertString(editor.caretModel.offset, output)
        }
    }

    fun VirtualFile.getPackageName(): String {
        val dic = "src/main/kotlin"
        return this.path.substring(this.path.indexOf(dic) + dic.length + 1)
            .replace('/', '.')
            .replace(".kt", "")
    }

    fun VirtualFile.getFileName(): String {
        // 获取文件名字，去掉文件格式
        return this.name.replace(".kt", "")
    }

}