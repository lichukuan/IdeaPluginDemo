package com.plugin.demo.language;

import com.intellij.psi.tree.IElementType;

public class CURLTokenType extends IElementType {

    public CURLTokenType(String debugName) {
        super(debugName, HTTPLanguage.INSTANCE);
    }

    public String toString() {
        return "CURLTokenType." + super.toString();
    }

}