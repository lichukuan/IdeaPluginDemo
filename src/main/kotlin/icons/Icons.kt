package icons

import com.intellij.openapi.util.IconLoader

// 所有 icon 图片资源，通过 IconLoader.getIcon 获取，并都定义在 TranslatorIcons 接口中
interface TranslatorIcons {

    companion object {
        // 需要加上 @JvmField 才行
        @JvmField
        val TRANS = IconLoader.getIcon("/icons/translator.png", TranslatorIcons::class.java)
    }

}

interface HttpIcons {
    companion object {
        // 需要加上 @JvmField 才行
        @JvmField
        val FILE = IconLoader.getIcon("/icons/http_icon.svg", HttpIcons::class.java)
    }
}