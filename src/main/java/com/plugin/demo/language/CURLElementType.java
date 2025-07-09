package com.plugin.demo.language;

import com.intellij.psi.tree.IElementType;

class CURLElementType extends IElementType {

    public CURLElementType(String debugName) {
        super(debugName, HTTPLanguage.INSTANCE);
    }
}