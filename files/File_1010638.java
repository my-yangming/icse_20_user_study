/*
 * Copyright 2003-2011 JetBrains s.r.o.
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
package jetbrains.mps.lang.typesystem.runtime.performance;

import jetbrains.mps.lang.pattern.IMatchingPattern;
import jetbrains.mps.newTypesystem.RuntimeSupportNew;
import org.jetbrains.mps.openapi.model.SNode;
import jetbrains.mps.typesystem.inference.TypeChecker;
import jetbrains.mps.typesystem.inference.TypeCheckingContext;
import jetbrains.mps.util.Computable;

/**
 * Cyril.Konopko, 22.04.2010
 */
public class RuntimeSupport_Tracer extends RuntimeSupportNew {
  public RuntimeSupport_Tracer(TypeChecker typeChecker) {
    super(typeChecker);
  }

  @Override
  public SNode coerce_(final SNode subtype, final IMatchingPattern pattern, final boolean isWeak) {
    return myTypeChecker.computeWithTrace(() -> RuntimeSupport_Tracer.super.coerce_(subtype, pattern, isWeak), "coerce");
  }

  @Override
  public SNode coerce_(final SNode subtype, final IMatchingPattern pattern) {
    return myTypeChecker.computeWithTrace(() -> RuntimeSupport_Tracer.super.coerce_(subtype, pattern), "coerce");
  }

  @Override
  public SNode coerce_(final SNode subtype, final IMatchingPattern pattern, final boolean isWeak, final TypeCheckingContext typeCheckingContext) {
    return myTypeChecker.computeWithTrace(() -> RuntimeSupport_Tracer.super.coerce_(subtype, pattern, isWeak, typeCheckingContext), "coerce");
  }

  @Override
  public SNode coerce_(final SNode subtype, final IMatchingPattern pattern, final TypeCheckingContext typeCheckingContext) {
    return myTypeChecker.computeWithTrace(() -> RuntimeSupport_Tracer.super.coerce_(subtype, pattern, typeCheckingContext), "coerce");
  }
}
