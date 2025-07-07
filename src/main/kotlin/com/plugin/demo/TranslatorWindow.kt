package com.plugin.demo

import javax.swing.*
import javax.swing.table.DefaultTableModel

// 生成的 java 文件需要转换为 kotlin 文件才行。否则会报  Caused by: java.lang.NoClassDefFoundError 错误
class TranslatorWindow {
    var tabbedPane1: JTabbedPane? = null
    var notePanel: JPanel? = null
    var noteTable: JTable? = null
    var translatorPanel: JPanel? = null
    var comboBox1: JComboBox<String>? = null
    var originTextArea: JTextArea? = null
    var comboBox2: JComboBox<String>? = null
    var translateTextArea: JTextArea? = null
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
    }
}
