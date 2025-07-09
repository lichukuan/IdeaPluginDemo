package com.plugin.demo.language.commenter;

import com.intellij.lang.Commenter;
import org.jetbrains.annotations.Nullable;

/**
 * 注释器
 */
public class CURLCommenter implements Commenter {
    // 行注释以“//”符号标记
    @Override
    public @Nullable String getLineCommentPrefix() {
        return "//";
    }

    @Override
    public @Nullable String getBlockCommentPrefix() {
        return null;
    }

    @Override
    public @Nullable String getBlockCommentSuffix() {
        return null;
    }

    @Override
    public @Nullable String getCommentedBlockCommentPrefix() {
        return null;
    }

    @Override
    public @Nullable String getCommentedBlockCommentSuffix() {
        return null;
    }
}