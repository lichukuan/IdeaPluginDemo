package com.plugin.demo.language.marker;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.TextRange;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Objects;

public class CURLRunAction extends AnAction {

    private final PsiElement info;

    public CURLRunAction(PsiElement info) {
        this.info = info;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (editor == null) {
            return;
        }
        Document document = editor.getDocument();
        // 文本编辑小节介绍过如何获取编辑器特定行的文本内容，若是大家对下面的方法不熟悉，
        // 可在第十小节进行回顾
        int offset = info.getTextOffset();
        // 获取到偏移量的行号
        int lineNumber = document.getLineNumber(offset);
        int startOffset = document.getLineStartOffset(lineNumber);
        int endOffset = document.getLineEndOffset(lineNumber);
        // 获取某偏移量范围内的文本
        String text = document.getText(new TextRange(startOffset, endOffset));
        if (Objects.equals(text, "")) {
            return;
        }
        try {
            // 调用系统终端，执行命令，如果大家的系统没有安装 curl ，可能会执行失败。
            // mac 操作系统的用户可通过 brew install curl 在终端中进行安装
            // windows 操作系统用户可在网上下载安装包进行安装，安装后需要添加 curl 到环境变量中
            Process process = Runtime.getRuntime().exec(text);
            // 获取执行结果
            InputStreamReader isr = new InputStreamReader(process.getInputStream());
            LineNumberReader lineNumberReader = new LineNumberReader(isr);
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = lineNumberReader.readLine()) != null) {
                output.append(line).append("\n");
            }
            // 通过 notification 显示执行结果
            Notifications.Bus.notify(new Notification("Print", "请求结果", output.toString(), NotificationType.INFORMATION));
        } catch (IOException ex) {
            Notifications.Bus.notify(new Notification("Print", "执行失败", ex.getMessage(), NotificationType.ERROR));
        }
    }
}