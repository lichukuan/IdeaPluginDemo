package com.plugin.demo.language.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.StandardPatterns;
import com.intellij.util.ProcessingContext;
import com.plugin.demo.language.HTTPFile;
import icons.HttpIcons;
import org.jetbrains.annotations.NotNull;
import java.util.List;

/**
 * 自动补全的功能
 */
public class CURLCommandCodeContributor extends CompletionContributor {

    // 定义需要进行自动提示的部分关键字，大家根据自己的需求进行定义即可
    private final static List<String> completionItems = List.of("curl", "--append", "--user-agent", "-anyauth", "--user", "get", "--request", "--header",
            "--output", "http://", "https://", "GET", "HEAD", "POST", "PUT", "DELETE", "CONNECT", "OPTIONS", "TRACE");

    // 自动补全构造器
    public CURLCommandCodeContributor() {
        // 调用父类方法，注册自动补全信息。
        // 第一个参数是自动提示类型，一般我们都是用 CompletionType.BASIC 即可
        // 第二个参数是需要进行自动提示的条件，以下设置的是只有在我们定义的 HTTP 文件才需要本实现类的自动补全逻辑
        // 第三个参数是自动补全代码提供者，具体含义查看后文的注释
        extend(CompletionType.BASIC, PlatformPatterns.psiElement().inFile(StandardPatterns.instanceOf(HTTPFile.class)),
                new CURLCommandCompletionProvider());
    }

    // 用户输入字符后，IDE 会调用该方法来获取自动提示的关键字集合。
    // 需要进行自动提示的集合将添加到入参 CompletionResultSet 对象中
    @Override
    public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result) {
        // 获取到当前的输入光标位置
        int offset = parameters.getOffset();
        Document document = parameters.getEditor().getDocument();
        // 获取到当前的光标所在行
        int lineStartOffset = document.getLineStartOffset(document.getLineNumber(offset));
        // 获取到当前行的光标位置前面的前缀文本
        String prefix = document.getText(TextRange.create(lineStartOffset, offset));
        // 因为我们的关键字都是通过空格切割的，我们截取出当前行位置上一个空格到当前光标位置的文本
        int lastSpace = prefix.lastIndexOf(' ') == -1 ? 0 : prefix.lastIndexOf(' ') + 1;
        // result 参数持有了自动补全提供者（CURLCommandCompletionProvider）addCompletions 后的所有自动补全选项
        // 通过 CompletionResultSet.withPrefixMatcher 筛选出该集合中，前缀为我们输入的文本的集合
        // 调用父类 fillCompletionVariants 方法，弹出筛选出来后的自动补全集合
        super.fillCompletionVariants(parameters, result.withPrefixMatcher(prefix.substring(lastSpace)));
    }

    // 该类为自动补全提供者，该类注册了所有需要进行自动补全的元素
    static class CURLCommandCompletionProvider extends CompletionProvider<CompletionParameters> {
        // 重写 addCompletions 方法，注册自动补全关键字
        @Override
        protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context,
                                      @NotNull CompletionResultSet result) {
            // 遍历前文中定义的需要进行自动补全的关键字集合
            for (String item : completionItems) {
                // 将这些自动补全的元素添加到 CompletionResultSet 对象中
                // LookupElementBuilder 类采用了构造者设计模式，用于构建自动补全元素的属性
                // LookupElementBuilder 类 create 方法指定了自动补全的关键字
                // LookupElementBuilder 类 withPresentableText 方法指定了自动补全的提示文本
                // LookupElementBuilder 类 withTypeText 方法指定了自动补全的尾部的提示文本
                // LookupElementBuilder 类 withTailText 方法指定了自动补全的随后的提示文本                // LookupElementBuilder 类 withIcon 方法指定了自动补全的提示文本的 ICON
                result.addElement(LookupElementBuilder.create(item)
                        .withPresentableText(item).withTailText(item)
                        .bold().withTypeText(item)
                        .withIcon(HttpIcons.FILE));
            }
        }
    }
}
