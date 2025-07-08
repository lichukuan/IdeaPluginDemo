package com.plugin.demo

import com.intellij.codeInsight.hint.HintManager
import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.EditorNotificationPanel
import com.intellij.ui.EditorNotificationProvider
import java.util.function.Function
import javax.swing.JComponent
import javax.swing.JLabel


/**
 * 该 Action 通过菜单栏选项进行触发，具体设置在 plugin.xml 的
 * 文件中的 action 标签下的 add-to-group 选项。
 */
class DialogNotification: AnAction() {

    override fun actionPerformed(p0: AnActionEvent) {
        MyDialog(p0.project).show()
    }
}

// 对于对话框通知，推荐使用 IntelliJ 平台 SDK 封装的 DialogWrapper 抽象类来创建
class MyDialog(val project: Project?): DialogWrapper(project) {

    init {
        init()
    }

    override fun createCenterPanel(): JComponent? {
        return JLabel("对话框提示")
    }

}

/**
 * 编辑器提示
 */
class HintNotification: AnAction() {

    override fun actionPerformed(p0: AnActionEvent) {
        // getData 方法用于获取 editor 对象，使用其他 PlatformDataKeys，
        // getData 方法还能用于获取 project 、光标、当前的编辑的文件对象。
        val editor = p0.getData(PlatformDataKeys.EDITOR) ?: return
        HintManager.getInstance().showInformationHint(editor, "编辑器提示")
    }

}

/**
 * 横幅通知
 */
class BannerNotificationProvider: EditorNotificationProvider {
    override fun collectNotificationData(
        p0: Project,
        p1: VirtualFile
    ): Function<in FileEditor, out JComponent?>? {

        // IDE 中的横幅提示原生实现界面
        val banner = EditorNotificationPanel()
        banner.setText("Banner 提示，请配置项目 SDK。")
        banner.setToolTipText("需要配置项目 SDK 。")
        banner.createActionLabel("去配置", object : Runnable {
            override fun run() {
                // 打开配置界面等逻辑，ShowSettingsUtil 工具在我们前面的翻译插件中使用过。
                ShowSettingsUtil.getInstance().showSettingsDialog(p0, "Editor")
            }
        })
        return Function<FileEditor, JComponent?> {
            banner
        }
    }

}


/**
 * 顶层通知
 */
class TopLevelNotification: AnAction() {
    override fun actionPerformed(p0: AnActionEvent) {
        val notification = Notification("Print", "顶层通知标题", "顶层通知内容", NotificationType.INFORMATION)
        // 为顶层通知添加 Action，触发 Action 会弹出一个新的通知
        notification.addAction(object: NotificationAction("点我") {
            override fun actionPerformed(
                p0: AnActionEvent,
                p1: Notification
            ) {
                Notifications.Bus.notify(
                    Notification(
                        "Print",
                        "顶层通知标题",
                        "套娃通知事件",
                        NotificationType.INFORMATION
                    ), p0.getProject()
                )
            }
        })
        Notifications.Bus.notify(notification, p0.project)
    }

}