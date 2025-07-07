package com.plugin.demo

import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ex.ToolWindowManagerListener
import com.intellij.util.messages.Topic
import java.awt.event.ActionEvent
import javax.swing.AbstractAction


/**
 * 监听项目从被打开到被关闭之间的生命周期
 */
class TranslatorSettingListener : ProjectManagerListener {


}

/**
 * 由于 ProjectManagerListener 的 projectOpened 被废弃，这里使用
 * ProjectActivity 来代替
 */
class TranslatorNotifier : ProjectActivity {
    override suspend fun execute(project: Project) {
        val notification =
            Notification("Print", "小天才翻译器", "请配置翻译API的appid和密钥", NotificationType.INFORMATION)
        notification.addAction(OpenTranslatorSettingAction())
        Notifications.Bus.notify(notification, project)
    }
}

/**
 * 跳转到指定配置位置的 Action
 */
class OpenTranslatorSettingAction : NotificationAction("打开翻译插件配置") {
    override fun actionPerformed(
        p0: AnActionEvent,
        p1: Notification
    ) {
        // IntelliJ SDK 提供的一个工具类，可以通过配置项名字，直接显示对应的配置界面
        ShowSettingsUtil.getInstance().showSettingsDialog(p0.project, "Translator")
        p1.expire()
    }

}

/**
 * 工具栏窗口的监视器
 */
class InitTranslatorWindowListener(val project: Project) : ToolWindowManagerListener {
    // 打开插件的窗口时，加载缓存的翻译结果到表格中，这里每次打开一次翻译窗口就会加载一次，
    //大家可以思考一下，如何解决（例如内存中维护一个静态变量记录一下，抑或是使用 toolWindowRegistered 回调方法实现）。
    override fun toolWindowShown(toolWindow: ToolWindow) {
        if (toolWindow.id != "Translator") return
        TranslatorCache.getInstance(project).transCache.forEach {
            TranslatorToolsWindow.addNote(it.key, it.value)
        }
    }
}

/**
 * 自定义事件
 */
interface TranslateListener {

    companion object {
        // 定义 Topic
        val TRANSLATE_TOPIC: Topic<TranslateListener> = Topic.create("translate", TranslateListener::class.java)
    }

    fun beforeTranslated(project: Project) {}

    fun afterTranslated(project: Project){}
}

// 第一个监听器实现类
class TranslatorActionListenerA : TranslateListener {

    override fun beforeTranslated(project: Project) {
        Notifications.Bus.notify(
            Notification(
                "Print",
                "小天才翻译机",
                "第一个监听器：beforeTranslated",
                NotificationType.INFORMATION
            ), project
        )
    }

    override fun afterTranslated(project: Project) {
        Notifications.Bus.notify(
            Notification(
                "Print",
                "小天才翻译机",
                "第一个监听器：afterTranslated",
                NotificationType.INFORMATION
            ), project
        )
    }
}

// 第二个监听器实现类
class TranslatorActionListenerB : TranslateListener {
    override fun beforeTranslated(project: Project) {
        Notifications.Bus.notify(
            Notification(
                "Print",
                "小天才翻译机",
                "第二个监听器：beforeTranslated",
                NotificationType.INFORMATION
            ), project
        )
    }

    override fun afterTranslated(project: Project) {
        Notifications.Bus.notify(
            Notification(
                "Print",
                "小天才翻译机",
                "第二个监听器：afterTranslated",
                NotificationType.INFORMATION
            ), project
        )
    }
}

/**
 * 为翻译按钮增加点击监听
 */
class TranslatorButtonActionListener(val window: TranslatorWindow): AbstractAction() {

    val langMap = HashMap<String, String>()
        .apply {
            put("中文", "zh")
            put("英文", "en")
        }

    override fun actionPerformed(e: ActionEvent?) {
        // 获取原语言文本、原语言、和目标翻译语言
        val originalText = window.originTextArea?.text
        val fromLang = langMap[window.comboBox1?.selectedItem]
        val toLang = langMap[window.comboBox2?.selectedItem]
        // 翻译后，将文本设置到翻译结果文本输入框
        window.translateTextArea?.text = "模拟翻译结果"

        // 点击翻译按钮时，将原文与译文添加到自动提示 provider 的提示列表中
        TranslatorTextProvider.items.add(originalText ?: "")
        TranslatorTextProvider.items.add("模拟翻译结果")
    }

}

