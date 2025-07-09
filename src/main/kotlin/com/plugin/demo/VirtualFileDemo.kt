package com.plugin.demo

import com.alibaba.fastjson.JSON
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileEvent
import com.intellij.openapi.vfs.VirtualFileListener
import wu.seal.jsontokotlin.library.JsonToKotlinBuilder

/**
 * 使用 VirtualFileListener 来监听文件内容的变化。通过这个来实现把 Json 字符串转化为
 * 对应的 kotlin 类
 */
class VirtualFileListenerImpl: VirtualFileListener {

    override fun contentsChanged(event: VirtualFileEvent) {
        // 获取事件对应的虚拟文件
        val file = event.file
        // 获取当前虚拟文件整个文件的内容
        val text = VfsUtil.loadText(file).ifEmpty { return }
        // 解析json
        runCatching {
            JSON.parse(text)
        }.onFailure {
            // 如果不是 json 则直接
            return
        }

        // 获取当前文件所在文件夹，文件的父级一定是一个目录
        val parentFile = file.parent

        // json 转化为 kotlin
        val output = JsonToKotlinBuilder()
            .setPackageName(parentFile.getPackageName())
            .build(text, file.getFileName())


        // createChildData 方法是虚拟文件系统中用于创建子文件的 api,
        // 在当前文件夹下创建类文件
        val kotlinFile = parentFile.createChildData(null, file.name)
        // 生成文件内容
        kotlinFile.setBinaryContent(output.toByteArray())
    }

    fun VirtualFile.getPackageName(): String {
        val dic = "src/main/kotlin"
        return this.path.substring(this.path.indexOf(dic) + dic.length + 1)
            .replace('/', '.')
            .replace(".kt", "")
    }

    fun VirtualFile.getFileName(): String {
        // 获取文件名字，去掉文件格式
        return this.name.replace(".kt", "")
    }
}

