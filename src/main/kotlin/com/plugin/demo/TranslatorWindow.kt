package com.plugin.demo

import com.intellij.openapi.project.ProjectManager
import com.intellij.ui.EditorTextField
import com.intellij.ui.GotItTooltip
import com.intellij.ui.TextFieldWithAutoCompletionListProvider
import com.intellij.util.textCompletion.TextFieldWithCompletion
import javax.swing.*
import javax.swing.table.DefaultTableModel

// 生成的 java 文件需要转换为 kotlin 文件才行。否则会报  Caused by: java.lang.NoClassDefFoundError 错误
class TranslatorWindow {
    var tabbedPane1: JTabbedPane? = null
    var notePanel: JPanel? = null
    var noteTable: JTable? = null
    var translatorPanel: JPanel? = null
    var comboBox1: JComboBox<String>? = null
    // 不清楚为什么无法通过 Morph Component（变形组件） 来改变 form 文件的
    // 的内容。这里是通过 vscode 来编辑 form 文件实现的
    var originTextArea: TextFieldWithCompletion? = null
    var comboBox2: JComboBox<String>? = null
    var translateTextArea: TextFieldWithCompletion? = null
    var translateButton: JButton? = null
    var mainPanel: JPanel? = null


    init {
        // 设置下拉框的下拉选项
        comboBox1!!.addItem("英文")
        comboBox1!!.addItem("中文")
        comboBox2!!.addItem("中文")
        comboBox2!!.addItem("英文")

        // 备忘录表格属性设置
        val header = arrayOf<String?>("原文", "译文")
        val tableModel = DefaultTableModel(null, header)
        noteTable!!.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS)
        noteTable!!.setModel(tableModel)

        // 按钮添加监听器
        translateButton?.addActionListener(TranslatorButtonActionListener(this))

        originTextArea?.isEnabled = true
        translateTextArea?.isEnabled = true

        tabbedPane1?.addChangeListener {
            // 为上方的 tab 栏目设置监听器，处于翻译 tab 时引导用户
            val tab = it.source as JTabbedPane
            if (tab.selectedIndex == 0) return@addChangeListener
            // 引导提示
            GotItTooltip("got.it.id", "翻译插件",
                ProjectManager.getInstance().defaultProject)
                .withShowCount(100) // 为了方便调试，设置为100，该提示会出现 100 次
                .withHeader("输入文本，点击翻译按钮即可完成翻译") // 引导提示内容
                .show(translateButton!!, GotItTooltip.BOTTOM_MIDDLE)  // 引导提示位置设置在翻译按钮的正下方位置
        }
    }

    // 在该方法中编写自定义 UI 组件初始化代码。
    private fun createUIComponents() {
        originTextArea = TextFieldWithCompletion(ProjectManager.getInstance().defaultProject,
            TranslatorTextProvider(), "", true, true, true, true)

        translateTextArea = TextFieldWithCompletion(ProjectManager.getInstance().defaultProject,
            TranslatorTextProvider(), "", true, true, true, true)

    }
}

// 实现自动补齐的功能
class TranslatorTextProvider: TextFieldWithAutoCompletionListProvider<String>(TranslatorTextProvider.items) {

    companion object {
        // 自动提示补全的所有可选项列表，每当我们进行翻译时，会往改列表添加元素，这些元素都是一个完整的单词
        @JvmStatic
        val items = HashSet<String>()
    }

    override fun getLookupString(item: String): String {
        // 整个输入文本框中的文本，我们只关注空格隔开后的最后一个字符串，
        // 例如：“ABC CB”这段文本，需要进行自动提示的文本前缀为 CB
        return item.substring(item.lastIndexOf(" ") + 1);
    }
}
