package com.plugin.demo

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.util.NlsContexts
import com.intellij.ui.JBColor
import java.awt.GridLayout
import java.awt.event.FocusEvent
import java.awt.event.FocusListener
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JTextField


/**
 * 示例二：实现翻译的功能
 */

/**
 * 用于在 Setting/Preferences 中显示输入 appid 和 securitykey 的配置
 */
class TranslateConfigurable : Configurable {

    companion object {
        const val SETTING_NAME = "Translator"

        @JvmStatic
        @Volatile
        var app_id = ""

        @JvmStatic
        @Volatile
        var security_key = ""
    }

    private val componentUtils = ComponentUtils()

    /**
     * 获取配置在 Setting/Preferences 中显示的名字
     */
    override fun getDisplayName(): @NlsContexts.ConfigurableName String? {
        return SETTING_NAME
    }

    /**
     * 基于 Swing 设计我们配置的UI
     */
    override fun createComponent(): JComponent? {
        return componentUtils.component
    }

    /**
     * 提供给 IDE 判断配置是否发生改变。如果返回 true，则配置界面
     * 中的 apply 按钮可点击
     */
    override fun isModified(): Boolean {
        return true
    }

    /**
     * 当在配置界面点击 apply  或者 ok 按钮时，该方法会被调用
     */
    override fun apply() {
        app_id = componentUtils.appID.text
        security_key = componentUtils.securityKey.text
    }

}

class ComponentUtils {
    companion object {
        const val APP_ID_HINT = "请输入appID"
        const val SECURITY_KEY_HINT = "请输入security key"
    }

    val appID = JTextField().apply {
        text = APP_ID_HINT
        foreground = JBColor.GRAY
        addFocusListener(TextFieldListener(this, APP_ID_HINT))
    }
    val securityKey = JTextField().apply {
        text = SECURITY_KEY_HINT
        foreground = JBColor.GRAY
        addFocusListener(TextFieldListener(this, SECURITY_KEY_HINT))
    }

    val component = JPanel().apply {
        layout = GridLayout(15, 1)
    }

    init {
        component.add(appID)
        component.add(securityKey)
    }

    class TextFieldListener(private val textField: JTextField, private val defaultHint: String?) : FocusListener {
        override fun focusGained(e: FocusEvent?) {
            // 清空提示语，设置为黑色字体
            if (textField.getText() == defaultHint) {
                textField.setText("")
                textField.setForeground(JBColor.BLACK)
            }
        }

        override fun focusLost(e: FocusEvent?) {
            // 如果内容为空，设置提示语
            if (textField.getText() == "") {
                textField.setText(defaultHint)
                textField.setForeground(JBColor.GRAY)
            }
        }
    }
}

/**
 * 触发翻译功能
 */
class Translator : AnAction() {

    override fun actionPerformed(p0: AnActionEvent) {
        if (TranslateConfigurable.app_id.isEmpty() || TranslateConfigurable.security_key.isEmpty()) {
            Notifications.Bus.notify(
                Notification(
                    "Translator", "小天才翻译件", "请先设置appid和securitykey",
                    NotificationType.INFORMATION
                ),
                p0.project
            )
            return
        }
        val editor = p0.getData(CommonDataKeys.EDITOR)
        // getSelectionModel() 可获取到鼠标选中文本对象，通过 getSelectedText() 方法获取到选中的文本字符串
        val text = editor?.selectionModel?.selectedText ?: ""
        println("选中的内容为：$text")
        Notifications.Bus.notify(
            Notification(
                "Translator", "小天才翻译件", "选中的内容为：$text",
                NotificationType.INFORMATION
            ), p0.project
        )
    }

}