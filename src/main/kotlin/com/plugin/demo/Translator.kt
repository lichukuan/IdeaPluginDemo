package com.plugin.demo

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsContexts
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.JBColor
import com.intellij.ui.content.ContentFactory
import java.awt.Dimension
import java.awt.GridLayout
import java.awt.event.FocusEvent
import java.awt.event.FocusListener
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTable
import javax.swing.JTextField
import javax.swing.table.DefaultTableModel


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
        var app_id
            get() = TranslatorSetting.getInstance().appID
            set(value) {
                TranslatorSetting.getInstance().appID = value
            }

        @JvmStatic
        var security_key
            get() = TranslatorSetting.getInstance().securityKey
            set(value) {
                TranslatorSetting.getInstance().securityKey = value
            }
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
        TranslatorSetting.getInstance().apply {
            appID = componentUtils.appID.text
            securityKey = componentUtils.securityKey.text
        }
    }

}

/**
 * Setting/Preferences 中显示的UI组件
 */
class ComponentUtils {
    companion object {
        const val APP_ID_HINT = "请输入appID"
        const val SECURITY_KEY_HINT = "请输入security key"
    }

    val appID = JTextField().apply {
        text = TranslatorSetting.getInstance().appID.ifEmpty {
            APP_ID_HINT
        }
        foreground = JBColor.GRAY
        addFocusListener(TextFieldListener(this, APP_ID_HINT))
    }
    val securityKey = JTextField().apply {
        text = TranslatorSetting.getInstance().securityKey.ifEmpty { SECURITY_KEY_HINT }
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
        val project = p0.project ?: return
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

        val messageBus = project.messageBus
        // 表明了发布的消息事件是TranslateListener 中声明的翻译监听器 Topic。
        val translateListener = messageBus.syncPublisher(TranslateListener.TRANSLATE_TOPIC)
        translateListener.beforeTranslated(project)

        val editor = p0.getData(CommonDataKeys.EDITOR)
        // getSelectionModel() 可获取到鼠标选中文本对象，通过 getSelectedText() 方法获取到选中的文本字符串
        val text = editor?.selectionModel?.selectedText ?: ""
        // 模拟翻译
        p0.project?.let {
            TranslatorCache.getInstance(it)
                .transCache
                .put(text, "模拟翻译结果")
            TranslatorCache.getInstance(it)
                .transCache.forEach { it ->
                    TranslatorToolsWindow.addNote(it.key, it.value)
                }

        }
        translateListener.afterTranslated(project)

        // 点击翻译按钮时，将原文与译文添加到自动提示 provider 的提示列表中
        TranslatorTextProvider.items.add(text ?: "")
        TranslatorTextProvider.items.add("模拟翻译结果")
        Notifications.Bus.notify(
            Notification(
                "Translator", "小天才翻译件", "选中的内容为：$text",
                NotificationType.INFORMATION
            ), p0.project
        )
    }

}

/**
 * 持久化存储数据，这里的泛型就是需要持久化的数据
 * 在 \IdeaPluginDemo\build\idea-sandbox\IC-2024.2.5\config\options\translator.xml 中
 * 可以看到保存的信息
 */
@State(name = "translator", storages = [Storage(value = "translator.xml")])
class TranslatorSetting() : PersistentStateComponent<TranslatorSetting> {

    var appID: String = ""
    var securityKey: String = ""

    companion object {
        @JvmStatic
        fun getInstance(): TranslatorSetting {
            return ApplicationManager.getApplication().getService(TranslatorSetting::class.java)
        }
    }

    override fun getState(): TranslatorSetting? {
        return this
    }

    override fun loadState(p0: TranslatorSetting) {
        this.appID = p0.appID
        this.securityKey = p0.securityKey
    }
}

/**
 * 缓存之前已经翻译过的数据
 */
@State(name = "translatorcache", storages = [Storage(value = "translator-cache.xml")])
class TranslatorCache : PersistentStateComponent<TranslatorCache> {

    companion object {
        @JvmStatic
        fun getInstance(project: Project): TranslatorCache {
            return project.getService(TranslatorCache::class.java)
        }
    }

    // 不能设置 private set 不然无法序列化
    var transCache = LinkedHashMap<String, String>()

    override fun getState(): TranslatorCache? {
        return this
    }

    override fun loadState(p0: TranslatorCache) {
        this.transCache = p0.transCache
    }

}

/**
 * 显示翻译结果的工具栏窗口
 */
class TranslatorToolsWindow : ToolWindowFactory {

    companion object {
        /**
         * 使用
        @JvmStatic
        val note
            get() = TranslatorWindow()
        代替
        @JvmStatic
        val note = TranslatorWindow()
        因为 static 块会加载类时就初始化，但是这是可能插件服务还没有初始化
         */
        @JvmStatic
        val note
            get() = TranslatorWindow()

        @JvmStatic
        fun addNote(from: String, to: String) {
            (note.noteTable?.model as DefaultTableModel).addRow(arrayOf(from, to))
        }
    }

    override fun createToolWindowContent(
        project: Project,
        toolWindow: ToolWindow
    ) {
        // ContentFactory 在 IntelliJ 平台 SDK 中负责 UI 界面的管理
        val contentFactory = ContentFactory.getInstance();
        // 创建我们的工具栏界面，TranslatorNote 是基于 Swing 实现的一个窗口视图

        // 在界面工厂中创建翻译插件的界面
        val content = contentFactory.createContent(note.mainPanel, "", false)
        // 将被界面工厂代理后创建的content，添加到工具栏窗口管理器中
        toolWindow.contentManager.addContent(content)
    }

}

/**
 * 基于 swing 实现的窗口类
 */
class TranslatorNote {

    val table = JTable().apply {
        model = DefaultTableModel(null, arrayOf("原文", "译文"))
        autoResizeMode = JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS
    }

    val notePanel = JScrollPane(table).apply {
        size = Dimension(200, 800)
    }
}

