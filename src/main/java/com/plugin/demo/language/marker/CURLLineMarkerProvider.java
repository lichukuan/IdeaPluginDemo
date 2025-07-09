package com.plugin.demo.language.marker;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.execution.lineMarker.RunLineMarkerProvider;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

// 行标识符提供类
public class CURLLineMarkerProvider extends RunLineMarkerProvider {

    @Override
    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement element) {
        // 判断当前 Psi 元素是不是 curl 关键字，只有是的情况下才返回行标记符
        if (!(element.getText().equalsIgnoreCase("curl"))) {
            return null;
        }
        // 返回一个行标识符实现类，第二个构造器参数就是我们的行标识符 Icon
        // 此处直接使用了内置的 Icon 类中的元素
        return new RunLineMarkerInfo(element, AllIcons.Actions.Execute);
    }

    // 实现 LineMarkerInfo 对象，该对象在 getLineMarkerInfo 方法返回，对象携带了
    // Icon 、以及后续待实现的触发 Action 等信息
    static class RunLineMarkerInfo extends LineMarkerInfo<PsiElement> {
        // 传入词法单元元素，以及对应的行标识符 Icon
        RunLineMarkerInfo(PsiElement element, Icon icon) {
            // 父类构造器，传入 Icon 、词法单元对象信息
            // 父类构造器的其他参数我们按需指定即可，run 字符串指的是鼠标放在 Icon 上时，
            // 给出的提示文本，GutterIconRenderer.Alignment.CENTER 值得是 Icon 在
            // 编辑器行显示的位置
            super(element, element.getTextRange(), icon, psiElement -> "Run", null, GutterIconRenderer.Alignment.CENTER, () -> "run");
        }

        // 重写 LineMarkerInfo 类的 createGutterRenderer 方法，方法的返回值对象为
        // GutterIconRenderer ，重写该对象的 getClickAction 方法
        // getClickAction 返回的就是我们点击 Icon 后需要触发的 Action 逻辑
        @Override
        public GutterIconRenderer createGutterRenderer() {
            return new LineMarkerGutterIconRenderer<>(this) {
                @Override
                public AnAction getClickAction() {
                    return new CURLRunAction(this.getLineMarkerInfo().
                            getElement());
                }
            };
        }
    }

}