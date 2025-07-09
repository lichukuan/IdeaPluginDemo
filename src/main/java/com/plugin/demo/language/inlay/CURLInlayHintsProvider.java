package com.plugin.demo.language.inlay;

import com.intellij.codeInsight.hints.*;
import com.intellij.lang.Language;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import com.plugin.demo.language.HTTPLanguage;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * 内嵌提示
 */
public class CURLInlayHintsProvider implements InlayHintsProvider<NoSettings> {

    // 对于内嵌提示，可以在 IDE 的 Preference/Editor/Inlay Hints 进行配置，
    // 我们返回 true 表示在配置面板中显示当前内嵌提示的设置选项。
    @Override
    public boolean isVisibleInSettings() {
        return true;
    }

    @NotNull
    @Override
    public SettingsKey<NoSettings> getKey() {
        return new SettingsKey<>("settings.inlay.curl");
    }

    // 对于内嵌提示，可以在 IDE 的 Preference/Editor/Inlay Hints 进行配置，
    // 此处返回配置的名字
    @Nls(capitalization = Nls.Capitalization.Sentence)
    @NotNull
    @Override
    public String getName() {
        return "Curl.inlay.setting";
    }

    @Nullable
    @Override
    public String getPreviewText() {
        return null;
    }

    // 对于内嵌提示，可以在 IDE 的 Preference/Editor/Inlay Hints 进行配置，
    // 此处返回配置 UI 面板，由于我们没有什么可配置的，所以返回一个空面板即可
    @NotNull
    @Override
    public ImmediateConfigurable createConfigurable(@NotNull NoSettings noSettings) {
        return changeListener -> new JPanel();
    }

    @NotNull
    @Override
    public NoSettings createSettings() {
        return new NoSettings();
    }

    // CURLInlayHintCollector 是内嵌提示的核心类，下文中我们再进行讲解
    @Nullable
    @Override
    public InlayHintsCollector getCollectorFor(@NotNull PsiFile psiFile, @NotNull Editor editor, @NotNull NoSettings noSettings, @NotNull InlayHintsSink inlayHintsSink) {
        return new CURLInlayHintCollector(editor);
    }

    // 表示我们实现的内嵌提示 provider 只支持 HTTP 语言
    @Override
    public boolean isLanguageSupported(@NotNull Language language) {
        return language == HTTPLanguage.INSTANCE;
    }
}
