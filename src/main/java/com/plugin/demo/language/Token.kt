package com.plugin.demo.language

import com.intellij.psi.tree.IElementType

/**
 * 通过 com.intellij.psi.tree.IElementType 表示词法单元的定义
 */
class CURLTokenType(debugName: String): IElementType(debugName, HttpLanguage.getInstance()) {

    override fun toString(): String {
        return "CURLTokenType.${super.toString()}"
    }

}

class CURLElementType(debugName: String): IElementType(debugName, HttpLanguage.getInstance()) {


}