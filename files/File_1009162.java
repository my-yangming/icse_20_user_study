// This is a generated file. Not intended for manual editing.
package org.elixir_lang.psi.impl;

import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangTuple;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import org.elixir_lang.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ElixirLiteralWordsLineImpl extends ASTWrapperPsiElement implements ElixirLiteralWordsLine {

  public ElixirLiteralWordsLineImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ElixirVisitor visitor) {
    visitor.visitLiteralWordsLine(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ElixirVisitor) accept((ElixirVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public ElixirLiteralWordsBody getLiteralWordsBody() {
    return PsiTreeUtil.getChildOfType(this, ElixirLiteralWordsBody.class);
  }

  @Override
  @Nullable
  public ElixirSigilModifiers getSigilModifiers() {
    return PsiTreeUtil.getChildOfType(this, ElixirSigilModifiers.class);
  }

  @NotNull
  public List<Integer> addEscapedCharacterCodePoints(@Nullable List<Integer> codePointList, @NotNull ASTNode node) {
    return ElixirPsiImplUtil.addEscapedCharacterCodePoints(this, codePointList, node);
  }

  @NotNull
  public List<Integer> addEscapedEOL(@Nullable List<Integer> maybeCodePointList, @NotNull ASTNode node) {
    return ElixirPsiImplUtil.addEscapedEOL(this, maybeCodePointList, node);
  }

  @NotNull
  public List<Integer> addFragmentCodePoints(@Nullable List<Integer> codePointList, @NotNull ASTNode node) {
    return ElixirPsiImplUtil.addFragmentCodePoints(this, codePointList, node);
  }

  @NotNull
  public List<Integer> addHexadecimalEscapeSequenceCodePoints(@Nullable List<Integer> codePointList, @NotNull ASTNode node) {
    return ElixirPsiImplUtil.addHexadecimalEscapeSequenceCodePoints(this, codePointList, node);
  }

  public Body getBody() {
    return ElixirPsiImplUtil.getBody(this);
  }

  public IElementType getFragmentType() {
    return ElixirPsiImplUtil.getFragmentType(this);
  }

  @NotNull
  public OtpErlangObject quote() {
    return ElixirPsiImplUtil.quote(this);
  }

  @NotNull
  public OtpErlangObject quote(@NotNull OtpErlangObject quotedContent) {
    return ElixirPsiImplUtil.quote(this, quotedContent);
  }

  @NotNull
  public OtpErlangObject quoteBinary(OtpErlangTuple binary) {
    return ElixirPsiImplUtil.quoteBinary(this, binary);
  }

  @NotNull
  public OtpErlangObject quoteEmpty() {
    return ElixirPsiImplUtil.quoteEmpty(this);
  }

  @NotNull
  public OtpErlangObject quoteLiteral(List<Integer> codePointList) {
    return ElixirPsiImplUtil.quoteLiteral(this, codePointList);
  }

  public char sigilName() {
    return ElixirPsiImplUtil.sigilName(this);
  }

  public char terminator() {
    return ElixirPsiImplUtil.terminator(this);
  }

}
