package com.plugin.demo.language;

import com.intellij.lang.ParserDefinition;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

public class CURLParserDefinition implements ParserDefinition {
    // 声明解析器所被应用的语言文件
    public static final IFileElementType FILE = new IFileElementType(HTTPLanguage.INSTANCE);

    // createLexer 方法返回词法解析器，此处我们返回解析器适配类
    @Override
    public @NotNull Lexer createLexer(Project project) {
        return new CURLLexerAdapter();
    }

    // createParser 方法返回工具生成的语法解析器
    @Override
    public @NotNull PsiParser createParser(Project project) {
        return new CURLParser();
    }

    // 返回语法文件类型信息
    @Override
    public @NotNull IFileElementType getFileNodeType() {
        return FILE;
    }

    // 返回“注释”的词法单元
    @Override
    public @NotNull TokenSet getCommentTokens() {
        return TokenSet.create(CURLTypes.COMMENT);
    }

    @Override
    public @NotNull TokenSet getStringLiteralElements() {
        return TokenSet.EMPTY;
    }

    @Override
    public @NotNull PsiElement createElement(ASTNode node) {
        return CURLTypes.Factory.createElement(node);
    }

    // createFile 返回本节中“文件类型定义”中定义的文件信息
    @Override
    public @NotNull PsiFile createFile(@NotNull FileViewProvider viewProvider) {
        return new HTTPFile(viewProvider);
    }
}
