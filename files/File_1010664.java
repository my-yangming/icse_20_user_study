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
package jetbrains.mps.typesystem.inference.util;

import gnu.trove.THashMap;
import gnu.trove.THashSet;
import jetbrains.mps.lang.pattern.util.MatchingUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.mps.openapi.model.SNode;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class StructuralNodeMap<T> implements Map<SNode, T> {
  private Map<SNode, SNode> myRepresentatorsMap = new THashMap<>();
  private Map<SNode, T> myMap = new THashMap<>();
  private Set<SNode> myAbsentNodes = new THashSet<>();

  public SNode getKeyRepresentator(SNode node) {
    return myRepresentatorsMap.get(node);
  }

  @Override
  public int size() {
    return myMap.size();
  }

  @Override
  public boolean isEmpty() {
    return myMap.isEmpty();
  }

  @Override
  public boolean containsValue(Object value) {
    return myMap.containsValue(value);
  }

  @Override
  public void putAll(@NotNull Map<? extends SNode, ? extends T> t) {
    throw new UnsupportedOperationException();
  }

  @NotNull
  @Override
  public Set<SNode> keySet() {
    return myMap.keySet();
  }

  @Override
  public void clear() {
    myRepresentatorsMap.clear();
    myMap.clear();
    myAbsentNodes.clear();
  }

  @NotNull
  @Override
  public Collection<T> values() {
    return myMap.values();
  }

  @NotNull
  @Override
  public Set<Entry<SNode, T>> entrySet() {
    return myMap.entrySet();
  }

  @Override
  public T get(Object key) {
    if (!(key instanceof SNode)) return null;
    SNode keyNode = (SNode) key;
    SNode representator = getKeyRepresentator(keyNode);
    if (representator == null) {
      if (myAbsentNodes.contains(keyNode)) return null;
      for (SNode node : myMap.keySet()) {
        if (MatchingUtil.matchNodes(node, keyNode)) {
          myRepresentatorsMap.put(keyNode, node);
          return myMap.get(node);
        }
      }
    }
    if (representator == null) {
      myAbsentNodes.add(keyNode);
      return null;
    }
    return myMap.get(representator);
  }

  @Override
  public T put(SNode keyNode, T value) {
    SNode representator = getKeyRepresentator(keyNode);
    if (representator == null) {
      for (SNode node : myMap.keySet()) {
        if (MatchingUtil.matchNodes(node, keyNode)) {
          myRepresentatorsMap.put(keyNode, node);
          return myMap.put(node, value);
        }
      }
    }
    if (representator == null) {
      myRepresentatorsMap.put(keyNode, keyNode);
      myAbsentNodes.remove(keyNode);
      representator = keyNode;
    }
    return myMap.put(representator, value);
  }

  @Override
  public T remove(Object key) {
    if (!(key instanceof SNode)) return null;
    SNode keyNode = (SNode) key;
    myAbsentNodes.add(keyNode);
    SNode representator = getKeyRepresentator(keyNode);
    if (representator == null) {
      for (SNode node : myMap.keySet()) {
        if (MatchingUtil.matchNodes(node, keyNode)) {
          myRepresentatorsMap.put(keyNode, node);
          return myMap.remove(node);
        }
      }
    }
    if (representator == null) return null;
    return myMap.remove(representator);
  }

  @Override
  public boolean containsKey(Object key) {
    if (!(key instanceof SNode)) return false;
    SNode keyNode = (SNode) key;
    SNode representator = getKeyRepresentator(keyNode);
    if (representator == null) {
      if (myAbsentNodes.contains(keyNode)) return false;
      for (SNode node : myMap.keySet()) {
        if (MatchingUtil.matchNodes(node, keyNode)) {
          myRepresentatorsMap.put(keyNode, node);
          return myMap.containsKey(node);
        }
      }
    }
    if (representator == null) {
      myAbsentNodes.add(keyNode);
      return false;
    }
    return myMap.containsKey(representator);
  }
}
