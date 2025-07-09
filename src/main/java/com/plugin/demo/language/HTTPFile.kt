package com.plugin.demo.language

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider

class HTTPFile(viewProvider: FileViewProvider): PsiFileBase(viewProvider, HttpLanguage.getInstance()) {
    override fun getFileType(): FileType {
        return HttpFileType.INSTANCE
    }
}