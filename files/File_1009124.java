/*
 * Copyright (C) 2014 Pedro Vicente Gómez Sánchez.
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
package com.pedrogomez.renderers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.pedrogomez.renderers.exception.NeedsPrototypesException;
import com.pedrogomez.renderers.exception.NullContentException;
import com.pedrogomez.renderers.exception.NullLayoutInflaterException;
import com.pedrogomez.renderers.exception.NullParentException;
import com.pedrogomez.renderers.exception.NullPrototypeClassException;
import com.pedrogomez.renderers.exception.PrototypeNotFoundException;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Class created to work as builder for Renderer objects. This class provides methods to create a
 * Renderer instances using a fluent API.
 *
 * The library users have to extends RendererBuilder and create a new one with prototypes. The
 * RendererBuilder implementation will have to declare the mapping between objects from the
 * AdapteeCollection and Renderer instances passed to the prototypes collection.
 *
 * This class is not going to implement the view recycling if is used with the RecyclerView widget
 * because RecyclerView class already implements the view recycling for us.
 *
 * @author Pedro Vicente Gómez Sánchez
 */
public class RendererBuilder<T> {

  private List<Renderer<? extends T>> prototypes;

  private T content;
  private View convertView;
  private ViewGroup parent;
  private LayoutInflater layoutInflater;
  private Integer viewType;
  private Map<Class<? extends T>, Class<? extends Renderer>> binding;

  /**
   * Initializes a RendererBuilder with an empty prototypes collection. Using this constructor some
   * binding configuration is needed.
   */
  public RendererBuilder() {
    this(new LinkedList<Renderer<? extends T>>());
  }

  /**
   * Initializes a RendererBuilder with just one prototype. Using this constructor the prototype
   * used will be always the same and the additional binding configuration wont be needed.
   */
  public RendererBuilder(Renderer<T> renderer) {
    this(Collections.<Renderer<? extends T>>singletonList(renderer));
  }

  /**
   * Initializes a RendererBuilder with a list of prototypes. Using this constructor some
   * binding configuration is needed.
   */
  public RendererBuilder(Collection<? extends Renderer<? extends T>> prototypes) {
    if (prototypes == null) {
      throw new NeedsPrototypesException(
          "RendererBuilder has to be created with a non null collection of"
              + "Collection<Renderer<T> to provide new or recycled Renderer instances");
    }
    this.prototypes = new LinkedList<>(prototypes);
    this.binding = new HashMap<Class<? extends T>, Class<? extends Renderer>>();
  }

  /**
   * Get access to the prototypes collection used to create one RendererBuilder.
   *
   * @return prototypes list.
   */
  public final List<Renderer<? extends T>> getPrototypes() {
    return Collections.unmodifiableList(prototypes);
  }

  /**
   * Configure prototypes used as Renderer instances.
   *
   * @param prototypes to use by the builder in order to create Renderer instances.
   */
  public final void setPrototypes(Collection<? extends Renderer<? extends T>> prototypes) {
    if (prototypes == null) {
      throw new NeedsPrototypesException(
          "RendererBuilder has to be created with a non null collection of"
              + "Collection<Renderer<T> to provide new or recycled Renderer instances");
    }
    this.prototypes = new LinkedList<>(prototypes);
  }

  /**
   * Configure prototypes used as Renderer instances.
   *
   * @param prototypes to use by the builder in order to create Renderer instances.
   * @return the current RendererBuilder instance.
   */
  public RendererBuilder<T> withPrototypes(Collection<? extends Renderer<? extends T>> prototypes) {
    if (prototypes == null) {
      throw new NeedsPrototypesException(
          "RendererBuilder has to be created with a non null collection of"
              + "Collection<Renderer<T> to provide new or recycled Renderer instances");
    }
    this.prototypes.addAll(prototypes);
    return this;
  }

  /**
   * Add a Renderer instance as prototype.
   *
   * @param renderer to use as prototype.
   * @return the current RendererBuilder instance.
   */
  public RendererBuilder<T> withPrototype(Renderer<? extends T> renderer) {
    if (renderer == null) {
      throw new NeedsPrototypesException(
          "RendererBuilder can't use a null Renderer<T> instance as prototype");
    }
    this.prototypes.add(renderer);
    return this;
  }

  /**
   * Given a class configures the binding between a class and a Renderer class.
   *
   * @param clazz to bind.
   * @param prototype used as Renderer.
   * @return the current RendererBuilder instance.
   */
  public <G extends T> RendererBuilder<T> bind(Class<G> clazz, Renderer<? extends G> prototype) {
    if (clazz == null || prototype == null) {
      throw new IllegalArgumentException(
          "The binding RecyclerView binding can't be configured using null instances");
    }
    prototypes.add(prototype);
    binding.put(clazz, prototype.getClass());
    return this;
  }

  public <G extends T> RendererBuilder<T> bind(Class<G> clazz, Class<? extends Renderer<? extends G>> prototypeClass) {
    if (clazz == null || prototypeClass == null) {
      throw new IllegalArgumentException(
          "The binding RecyclerView binding can't be configured using null instances");
    }
    binding.put(clazz, prototypeClass);
    return this;
  }

  RendererBuilder withContent(T content) {
    this.content = content;
    return this;
  }

  protected RendererBuilder withConvertView(View convertView) {
    this.convertView = convertView;
    return this;
  }

  RendererBuilder withParent(ViewGroup parent) {
    this.parent = parent;
    return this;
  }

  RendererBuilder withLayoutInflater(LayoutInflater layoutInflater) {
    this.layoutInflater = layoutInflater;
    return this;
  }

  RendererBuilder withViewType(Integer viewType) {
    this.viewType = viewType;
    return this;
  }

  /**
   * Return the item view type used by the adapter to implement recycle mechanism.
   *
   * @param content to be rendered.
   * @return an integer that represents the renderer inside the adapter.
   */
  int getItemViewType(T content) {
    Class prototypeClass = getPrototypeClass(content);
    validatePrototypeClass(prototypeClass);
    return getItemViewType(prototypeClass);
  }

  /**
   * Return the amount of different Renderer objects to be used in the ListView. This method has to
   * be implemented to support the ListView recycle mechanism.
   *
   * @return prototypes size collection.
   */
  int getViewTypeCount() {
    return prototypes.size();
  }

  /**
   * Main method of this class related to ListView widget. This method is the responsible of
   * recycle or create a new Renderer instance with all the needed information to implement the
   * rendering. This method will validate all the attributes passed in the builder constructor and
   * will check if can recycle or has to create a new Renderer instance.
   *
   * This method is used with ListView because the view recycling mechanism is implemented in this
   * class. RecyclerView widget will use buildRendererViewHolder method.
   *
   * @return ready to use Renderer instance.
   */
  protected Renderer build() {
    validateAttributes();

    Renderer renderer;
    if (isRecyclable(convertView, content)) {
      renderer = recycle(convertView, content);
    } else {
      renderer = createRenderer(content, parent);
    }
    return renderer;
  }

  /**
   * Main method of this class related to RecyclerView widget. This method is the responsible of
   * create a new Renderer instance with all the needed information to implement the rendering.
   * This method will validate all the attributes passed in the builder constructor and will create
   * a RendererViewHolder instance.
   *
   * This method is used with RecyclerView because the view recycling mechanism is implemented out
   * of this class and we only have to return new RendererViewHolder instances.
   *
   * @return ready to use RendererViewHolder instance.
   */
  protected RendererViewHolder buildRendererViewHolder() {
    validateAttributesToCreateANewRendererViewHolder();

    Renderer renderer = getPrototypeByIndex(viewType).copy();
    renderer.onCreate(null, layoutInflater, parent);
    return new RendererViewHolder(renderer);
  }

  /**
   * Recycles the Renderer getting it from the tag associated to the renderer root view. This view
   * is not used with RecyclerView widget.
   *
   * @param convertView that contains the tag.
   * @param content to be updated in the recycled renderer.
   * @return a recycled renderer.
   */
  private Renderer recycle(View convertView, T content) {
    Renderer renderer = (Renderer) convertView.getTag();
    renderer.onRecycle(content);
    return renderer;
  }

  /**
   * Create a Renderer getting a copy from the prototypes collection.
   *
   * @param content to render.
   * @param parent used to inflate the view.
   * @return a new renderer.
   */
  private Renderer createRenderer(T content, ViewGroup parent) {
    int prototypeIndex = getPrototypeIndex(content);
    Renderer renderer = getPrototypeByIndex(prototypeIndex).copy();
    renderer.onCreate(content, layoutInflater, parent);
    return renderer;
  }

  /**
   * Search one prototype using the prototype index which is equals to the view type. This method
   * has to be implemented because prototypes member is declared with Collection and that interface
   * doesn't allow the client code to get one element by index.
   *
   * @param prototypeIndex used to search.
   * @return prototype renderer.
   */
  private Renderer getPrototypeByIndex(final int prototypeIndex) {
    Renderer prototypeSelected = null;
    int i = 0;
    for (Renderer prototype : prototypes) {
      if (i == prototypeIndex) {
        prototypeSelected = prototype;
      }
      i++;
    }
    return prototypeSelected;
  }

  /**
   * Check if one Renderer is recyclable getting it from the convertView's tag and checking the
   * class used.
   *
   * @param convertView to get the renderer if is not null.
   * @param content used to get the prototype class.
   * @return true if the renderer is recyclable.
   */
  private boolean isRecyclable(View convertView, T content) {
    boolean isRecyclable = false;
    if (convertView != null && convertView.getTag() != null) {
      Class prototypeClass = getPrototypeClass(content);
      validatePrototypeClass(prototypeClass);
      isRecyclable = prototypeClass.equals(convertView.getTag().getClass());
    }
    return isRecyclable;
  }

  private void validatePrototypeClass(Class prototypeClass) {
    if (prototypeClass == null) {
      throw new NullPrototypeClassException(
          "Your getPrototypeClass method implementation can't return a null class");
    }
  }

  /**
   * Returns the index of the prototype index using the content.
   *
   * @param content used to get the prototype index.
   * @return the prototype index.
   */
  private int getPrototypeIndex(T content) {
    return getItemViewType(content);
  }

  /**
   * Return the Renderer class associated to the prototype.
   *
   * @param prototypeClass used to search the renderer in the prototypes collection.
   * @return the prototype index associated to the prototypeClass.
   */
  private int getItemViewType(Class prototypeClass) {
    int itemViewType = -1;
    for (Renderer renderer : prototypes) {
      if (renderer.getClass().equals(prototypeClass)) {
        itemViewType = getPrototypeIndex(renderer);
        break;
      }
    }
    if (itemViewType == -1) {
      throw new PrototypeNotFoundException(
          "Review your RendererBuilder implementation, you are returning one"
              + " prototype class not found in prototypes collection");
    }
    return itemViewType;
  }

  /**
   * Return the index associated to the Renderer.
   *
   * @param renderer used to search in the prototypes collection.
   * @return the prototype index associated to the renderer passed as argument.
   */
  private int getPrototypeIndex(Renderer renderer) {
    int index = 0;
    for (Renderer prototype : prototypes) {
      if (prototype.getClass().equals(renderer.getClass())) {
        break;
      }
      index++;
    }
    return index;
  }

  /**
   * Throws one RendererException if the content parent or layoutInflater are null.
   */
  private void validateAttributes() {
    if (content == null) {
      throw new NullContentException("RendererBuilder needs content to create Renderer instances");
    }

    if (parent == null) {
      throw new NullParentException("RendererBuilder needs a parent to inflate Renderer instances");
    }

    if (layoutInflater == null) {
      throw new NullLayoutInflaterException(
          "RendererBuilder needs a LayoutInflater to inflate Renderer instances");
    }
  }

  /**
   * Throws one RendererException if the viewType, layoutInflater or parent are null.
   */
  private void validateAttributesToCreateANewRendererViewHolder() {
    if (viewType == null) {
      throw new NullContentException(
          "RendererBuilder needs a view type to create a RendererViewHolder");
    }
    if (layoutInflater == null) {
      throw new NullLayoutInflaterException(
          "RendererBuilder needs a LayoutInflater to create a RendererViewHolder");
    }
    if (parent == null) {
      throw new NullParentException(
          "RendererBuilder needs a parent to create a RendererViewHolder");
    }
  }

  /**
   * Method to be implemented by the RendererBuilder subtypes. In this method the library user will
   * define the mapping between content and renderer class.
   *
   * @param content used to map object to Renderers.
   * @return the class associated to the renderer.
   */
  protected Class getPrototypeClass(T content) {
    if (prototypes.size() == 1) {
      return prototypes.get(0).getClass();
    } else {
      return binding.get(content.getClass());
    }
  }
}
