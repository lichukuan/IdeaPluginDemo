// This is a generated file. Not intended for manual editing.
package com.plugin.demo.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface CURLCommand extends PsiElement {

  @NotNull
  List<CURLOptions> getOptionsList();

  @NotNull
  PsiElement getUrl();

}
