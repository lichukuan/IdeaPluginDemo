package com.plugin.demo.language

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet

//class CURLParserDefinitionkt: ParserDefinition {
//
//    companion object {
//        // 声明解析器所被应用的语言文件
//        @JvmStatic
//        val FILE = IFileElementType(HttpLanguage.getInstance())
//    }
//
//    // createLexer 方法返回词法解析器，此处我们返回解析器适配类
//    override fun createLexer(p0: Project?): Lexer {
//        return CURLLexerAdapter()
//    }
//
//    // createParser 方法返回工具生成的语法解析器
//    override fun createParser(p0: Project?): PsiParser {
//        return CURLParser()
//    }
//
//    // 返回语法文件类型信息
//    override fun getFileNodeType(): IFileElementType {
//        return FILE
//    }
//
//    // 返回“注释”的词法单元
//    override fun getCommentTokens(): TokenSet {
//        return CURLTokenSets.COMMENT
//    }
//
//    override fun getStringLiteralElements(): TokenSet {
//        return TokenSet.EMPTY
//    }
//
//    override fun createElement(p0: ASTNode?): PsiElement {
//        return CURLTypes.Factory.createElement(p0!!)
//    }
//
//    // createFile 返回本节中“文件类型定义”中定义的文件信息
//    override fun createFile(p0: FileViewProvider): PsiFile {
//        return HTTPFile(viewProvider = p0)
//    }
//}