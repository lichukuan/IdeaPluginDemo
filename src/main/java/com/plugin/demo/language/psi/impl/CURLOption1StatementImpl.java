// This is a generated file. Not intended for manual editing.
package com.plugin.demo.language.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.plugin.demo.language.CURLTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.plugin.demo.language.psi.*;

public class CURLOption1StatementImpl extends ASTWrapperPsiElement implements CURLOption1Statement {

  public CURLOption1StatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull CURLVisitor visitor) {
    visitor.visitOption1Statement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof CURLVisitor) accept((CURLVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public CURLOption1 getOption1() {
    return findNotNullChildByClass(CURLOption1.class);
  }

  @Override
  @NotNull
  public PsiElement getQuotedString() {
    return findNotNullChildByType(QUOTED_STRING);
  }

}
