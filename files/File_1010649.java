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
package jetbrains.mps.newTypesystem;

import jetbrains.mps.lang.pattern.IMatchingPattern;
import jetbrains.mps.lang.typesystem.runtime.RuntimeSupport;
import org.jetbrains.mps.openapi.model.SNode;
import jetbrains.mps.typesystem.inference.TypeChecker;
import jetbrains.mps.typesystem.inference.TypeCheckingContext;

public class RuntimeSupportNew extends RuntimeSupport {
  protected TypeChecker myTypeChecker;

  public RuntimeSupportNew(TypeChecker typeChecker) {
    myTypeChecker = typeChecker;
  }

  @Override
  public SNode coerce_(SNode subtype, IMatchingPattern pattern, boolean isWeak) {
    SubTypingManagerNew subTyping = (SubTypingManagerNew) myTypeChecker.getSubtypingManager();
    return subTyping.coerceSubTypingNew(subtype, pattern, isWeak, null);
  }

  @Override
  public SNode coerce_(SNode subtype, IMatchingPattern pattern) {
    SubTypingManagerNew subTyping = (SubTypingManagerNew) myTypeChecker.getSubtypingManager();
    return subTyping.coerceSubTypingNew(subtype, pattern, true, null);
  }

  @Override
  public SNode coerce_(SNode subtype, IMatchingPattern pattern, boolean isWeak, TypeCheckingContext typeCheckingContext) {
    if (typeCheckingContext == null) {
      return coerce_(subtype, pattern);
    }
    return ((SubTypingManagerNew)TypeChecker.getInstance().getSubtypingManager()).coerceSubTypingNew(subtype, pattern, isWeak, typeCheckingContext);
  }

  @Override
  public SNode coerce_(SNode subtype, IMatchingPattern pattern, TypeCheckingContext typeCheckingContext) {
    if (typeCheckingContext == null) {
      return coerce_(subtype, pattern);
    }
    return ((SubTypingManagerNew)TypeChecker.getInstance().getSubtypingManager()).coerceSubTypingNew(subtype, pattern, true, typeCheckingContext);
  }
}
