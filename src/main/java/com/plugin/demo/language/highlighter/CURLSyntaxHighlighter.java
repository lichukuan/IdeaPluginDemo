package com.plugin.demo.language.highlighter;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.plugin.demo.language.CURLLexerAdapter;
import com.plugin.demo.language.CURLTypes;
import org.jetbrains.annotations.NotNull;

/**
 * 词法单元高亮功能
 */
public class CURLSyntaxHighlighter extends SyntaxHighlighterBase {

    // 定义关键字高亮样式，DefaultLanguageHighlighterColors 类中有编程语言常见的代码元素默认样式定义。
    // 例如：关键字、字符串、行注释、全局变量、点等元素。此处我们使用使用 DefaultLanguageHighlighterColors.KEYWORD
    // 作为关键字高亮颜色
    public static final TextAttributesKey KEYWORD =
            TextAttributesKey.createTextAttributesKey("keyword", DefaultLanguageHighlighterColors.KEYWORD);
    // 类似上面 KEYWORD ，使用 DefaultLanguageHighlighterColors.STRING 作为字符串的高亮颜色
    public static final TextAttributesKey STRING =
            TextAttributesKey.createTextAttributesKey("value", DefaultLanguageHighlighterColors.STRING);
    // 注释高亮颜色
    public static final TextAttributesKey COMMENT =
            TextAttributesKey.createTextAttributesKey("comment", DefaultLanguageHighlighterColors.LINE_COMMENT);
    // 无法识别的词法单元高亮颜色
    public static final TextAttributesKey BAD_CHARACTER =
            TextAttributesKey.createTextAttributesKey("bad_character", HighlighterColors.BAD_CHARACTER);

    // 将上面定义的 TextAttributesKey 包装为数组，作为下面 getTokenHighlights 方法的返回值
    private static final TextAttributesKey[] BAD_CHAR_KEYS = new TextAttributesKey[]{BAD_CHARACTER} ;
    private static final TextAttributesKey[] KEYWORD_KEYS = new TextAttributesKey[]{KEYWORD} ;
    private static final TextAttributesKey[] STRING_KEYS = new TextAttributesKey[]{STRING} ;
    private static final TextAttributesKey[] COMMENT_KEYS = new TextAttributesKey[]{COMMENT} ;
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        // 返回词法解析器，符号高亮器需要通过词法解析器解析出字符的词法单元，用于下面的 getTokenHighlights 方法的入参
        return new CURLLexerAdapter();
    }

    @Override
    public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
        // 如果元素类型为 CURL 、OPTION、HTTP 请求方法这几个词法单元，返回关键字高亮格式
        if (tokenType.equals(CURLTypes.CURL) || tokenType.equals(CURLTypes.OPTION) || tokenType.equals(CURLTypes.METHOD) ) {
            return KEYWORD_KEYS;
        }
        // 如果元素为双引号字符串和基础字符串，返回字符串高亮格式
        if (tokenType.equals(CURLTypes.QUOTED_STRING) || tokenType.equals(CURLTypes.BASIC_STRING) ) {
            return STRING_KEYS;
        }
        if (tokenType.equals(CURLTypes.COMMENT) ) {
            return COMMENT_KEYS;
        }
        if (tokenType.equals(TokenType.BAD_CHARACTER) ) {
            return BAD_CHAR_KEYS;
        }
        return EMPTY_KEYS;
    }

}
