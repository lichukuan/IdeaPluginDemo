package com.plugin.demo.language

import com.intellij.psi.tree.TokenSet

/**
 * 每一种自定义语言，可能有几个到几十个词法单元，例如 Java 中，有 20+ 个关键字。
 * 为了防止插件在运行过程中，不断新创建词法单元对象，
 * 我们把所有的词法单元都声明出一个常量，在插件所有需要使用到的地方，
 * 直接使用已经声明出来的常量进行替换。
 */
interface CURLTokenSets {
    companion object {
        val COMMAND: TokenSet = TokenSet.create(CURLTypes.COMMAND)
        val KV: TokenSet = TokenSet.create(CURLTypes.KV)
        val METHOD: TokenSet = TokenSet.create(CURLTypes.METHOD)
        val OPTION: TokenSet = TokenSet.create(CURLTypes.OPTION)
        val OPTION_1: TokenSet = TokenSet.create(CURLTypes.OPTION_1)
        val OPTION_1_STATEMENT: TokenSet = TokenSet.create(CURLTypes.OPTION_1_STATEMENT)
        val OPTION_2: TokenSet = TokenSet.create(CURLTypes.OPTION_2)
        val OPTION_2_STATEMENT: TokenSet = TokenSet.create(CURLTypes.OPTION_2_STATEMENT)
        val OPTION_3: TokenSet = TokenSet.create(CURLTypes.OPTION_3)
        val OPTION_3_STATEMENT: TokenSet = TokenSet.create(CURLTypes.OPTION_3_STATEMENT)
        val OPTION_4: TokenSet = TokenSet.create(CURLTypes.OPTION_4)
        val OPTION_4_STATEMENT: TokenSet = TokenSet.create(CURLTypes.OPTION_4_STATEMENT)
        val OPTION_5: TokenSet = TokenSet.create(CURLTypes.OPTION_5)
        val OPTION_5_STATEMENT: TokenSet = TokenSet.create(CURLTypes.OPTION_5_STATEMENT)
        val OPTION_6: TokenSet = TokenSet.create(CURLTypes.OPTION_6)
        val OPTION_6_STATEMENT: TokenSet = TokenSet.create(CURLTypes.OPTION_6_STATEMENT)
        val OPTION_7: TokenSet = TokenSet.create(CURLTypes.OPTION_7)
        val OPTION_7_STATEMENT: TokenSet = TokenSet.create(CURLTypes.OPTION_7_STATEMENT)
        val BASIC_STRING: TokenSet = TokenSet.create(CURLTypes.BASIC_STRING)
        val COMMENT: TokenSet = TokenSet.create(CURLTypes.COMMENT)
        val CURL: TokenSet = TokenSet.create(CURLTypes.CURL)
        val QUOTED_STRING: TokenSet = TokenSet.create(CURLTypes.QUOTED_STRING)
        val URL: TokenSet = TokenSet.create(CURLTypes.URL)
    }
}