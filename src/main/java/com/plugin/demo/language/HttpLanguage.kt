package com.plugin.demo.language

import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.util.NlsContexts
import com.intellij.openapi.util.NlsSafe
import icons.HttpIcons
import org.jetbrains.annotations.NonNls
import javax.swing.Icon


/**
 * 通过 com.intellij.lang.Language 在 IntelliJ 中定义一种新语言
 */
class HttpLanguage private constructor(): Language("HTTP") {

    companion object {

        @JvmStatic
        private val instance = HttpLanguage()

        @JvmStatic
        fun getInstance() = instance
    }

}

/**
 * 通过 com.intellij.openapi.fileTypes.LanguageFileType 对象表示自定义语言的文件类型信息
 */
class HttpFileType private constructor(): LanguageFileType(HttpLanguage.getInstance()) {

    companion object {
        const val TYPE_NAME = "HTTP"
        const val TYPE_DESCRIBE = "HTTP language file"
        const val TYPE_EXTENSION = "http"

        // 单例对象，该对象需要配置在配置文件中
        @JvmField
        val INSTANCE = HttpFileType()
    }

    override fun getName(): @NonNls String {
        return TYPE_NAME
    }

    override fun getDescription(): @NlsContexts.Label String {
        return TYPE_DESCRIBE
    }

    override fun getDefaultExtension(): @NlsSafe String {
        // 返回文件拓展名
        return TYPE_EXTENSION
    }

    override fun getIcon(): Icon? {
        // 返回文件 Icon
        return HttpIcons.FILE
    }

}