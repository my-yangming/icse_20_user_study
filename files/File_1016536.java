/*
 * Copyright 2015 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.inferred.freebuilder.processor.source;

import static com.google.common.base.Preconditions.checkNotNull;

import static org.inferred.freebuilder.processor.model.ModelUtils.asElement;

import static java.util.Arrays.asList;
import static java.util.Collections.nCopies;

import com.google.common.collect.ImmutableList;

import java.util.List;

import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.DeclaredType;

/**
 * Representation of a parameterized class or interface type.
 *
 * <p>Similar to {@link DeclaredType}, with a code-generation and test-friendly focus.
 */
public abstract class Type extends ValueType implements Excerpt {

  public static Type from(DeclaredType declaredType) {
    if (declaredType.getTypeArguments().isEmpty()) {
      // Make testing easier by not introducing a distinction between Type and TypeClass for
      // non-generic types.
      return new TypeClass(
          QualifiedName.of(asElement(declaredType)),
          ImmutableList.<TypeParameterElement>of());
    } else {
      return new TypeImpl(
          QualifiedName.of(asElement(declaredType)),
          declaredType.getTypeArguments());
    }
  }

  public static Type from(Class<?> cls) {
    return new TypeImpl(QualifiedName.of(cls), asList(cls.getTypeParameters()));
  }

  /** Returns the qualified name of the type class. */
  public abstract QualifiedName getQualifiedName();

  protected abstract List<?> getTypeParameters();

  /** Returns the simple name of the type class. */
  public String getSimpleName() {
    return getQualifiedName().getSimpleName();
  }

  /** Returns true if the type class is generic. */
  public boolean isParameterized() {
    return !getTypeParameters().isEmpty();
  }

  /**
   * Returns a source excerpt suitable for constructing an instance of this type, including "new"
   * keyword but excluding brackets.
   */
  public Excerpt constructor() {
    return Excerpts.add("new %s%s", getQualifiedName(), diamondOperator());
  }

  public static class JavadocLink implements Excerpt {

    private final String fmt;
    private final Object[] args;

    public JavadocLink(String fmt, Object... args) {
      this.fmt = fmt;
      this.args = args;
    }

    @Override
    public void addTo(SourceBuilder code) {
      code.add("{@link ").add(fmt, args).add("}");
    }

    public Excerpt withText(String fmt, Object... args) {
      return code -> code.add("{@link ").add(this.fmt, this.args).add(" ").add(fmt, args).add("}");
    }
  }

  /**
   * Returns a source excerpt of a JavaDoc link to this type.
   */
  public JavadocLink javadocLink() {
    return new JavadocLink("%s", getQualifiedName());
  }

  /**
   * Returns a source excerpt of a JavaDoc link to a no-args method on this type.
   */
  public JavadocLink javadocNoArgMethodLink(String memberName) {
    return new JavadocLink("%s#%s()", getQualifiedName(), memberName);
  }

  /**
   * Returns a source excerpt of a JavaDoc link to a method on this type.
   */
  public JavadocLink javadocMethodLink(String memberName, Type... types) {
    return new JavadocLink("%s#%s(%s)",
        getQualifiedName(),
        memberName,
        (Excerpt) code -> {
          String separator = "";
          for (Type type : types) {
            code.add("%s%s", separator, type.getQualifiedName());
            separator = ", ";
          }
        });
  }

  /**
   * Returns a source excerpt of the type parameters of this type, including angle brackets.
   * Always an empty string if the type class is not generic.
   *
   * <p>e.g. {@code <N, C>}
   */
  public Excerpt typeParameters() {
    if (getTypeParameters().isEmpty()) {
      return Excerpts.EMPTY;
    } else {
      return Excerpts.add("<%s>", Excerpts.join(", ", getTypeParameters()));
    }
  }

  /**
   * Returns a source excerpt equivalent to the diamond operator for this type.
   *
   * <p>Always an empty string if the type class is not generic.
   */
  public Excerpt diamondOperator() {
    return isParameterized() ? Excerpts.add("<>") : Excerpts.EMPTY;
  }

  /**
   * Returns a new type of the same class, parameterized with wildcards ("?").
   *
   * <p>If the type class is not generic, the returned object will be equal to this one.
   */
  public Type withWildcards() {
    if (getTypeParameters().isEmpty()) {
      return this;
    }
    return new TypeImpl(getQualifiedName(), nCopies(getTypeParameters().size(), "?"));
  }

  @Override
  public void addTo(SourceBuilder source) {
    source.add("%s%s", getQualifiedName(), typeParameters());
  }

  static class TypeImpl extends Type {
    private final QualifiedName qualifiedName;
    private final List<?> typeParameters;

    TypeImpl(QualifiedName qualifiedName, List<?> typeParameters) {
      this.qualifiedName = checkNotNull(qualifiedName);
      this.typeParameters = ImmutableList.copyOf(typeParameters);
    }

    @Override
    public QualifiedName getQualifiedName() {
      return qualifiedName;
    }

    @Override
    protected List<?> getTypeParameters() {
      return typeParameters;
    }

    @Override
    protected void addFields(FieldReceiver fields) {
      fields.add("qualifiedName", qualifiedName);
      fields.add("typeParameters", typeParameters);
    }

    @Override
    public String toString() {
      // Only used when debugging, so an empty feature set is fine.
      return SourceBuilder.forTesting().add(this).toString();
    }
  }
}
