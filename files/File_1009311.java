package org.elixir_lang.structure_view.element;

import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.ElementDescriptionLocation;
import com.intellij.psi.PsiElement;
import com.intellij.usageView.UsageViewTypeLocation;
import org.elixir_lang.navigation.item_presentation.Parent;
import org.elixir_lang.psi.ElixirAccessExpression;
import org.elixir_lang.psi.ElixirList;
import org.elixir_lang.psi.QuotableKeywordList;
import org.elixir_lang.psi.QuotableKeywordPair;
import org.elixir_lang.psi.call.Call;
import org.elixir_lang.structure_view.element.modular.Modular;
import org.elixir_lang.structure_view.element.structure.Structure;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.elixir_lang.psi.call.name.Function.DEFEXCEPTION;
import static org.elixir_lang.psi.call.name.Module.KERNEL;
import static org.elixir_lang.psi.impl.PsiElementImplKt.stripAccessExpression;
import static org.elixir_lang.psi.impl.call.CallImplKt.finalArguments;

/**
 * A `defexception` with its fields and the callbacks `exception/1` and `message/1` if overridden.
 */
public class Exception extends Element<Call> {
    /*
     * Fields
     */

    @Nullable
    private List<CallDefinition> callbacks = null;
    @NotNull
    private final Modular modular;

    /*
     * Static Methods
     */

    public static String elementDescription(Call call, ElementDescriptionLocation location) {
        String elementDescription = null;

        if (location == UsageViewTypeLocation.INSTANCE) {
            elementDescription = "exception";
        }

        return elementDescription;
    }

    public static boolean is(Call call) {
        return call.isCalling(KERNEL, DEFEXCEPTION, 1);
    }

    public static boolean isCallback(Pair<String, Integer> nameArity) {
      return nameArity.second == 1 && (nameArity.first.equals("exception") || nameArity.first.equals("message"));
    }

    /*
     * Constructors
     */

    public Exception(@NotNull Modular modular, @NotNull Call call) {
        super(call);
        this.modular = modular;
    }

    /*
     * Instance Methods
     */

    /**
     * Adds callback function
     *
     * @param callback the callback function: either exception/1 or message/1.
     */
    @Contract(pure = false)
    public void callback(@NotNull final CallDefinition callback) {
        assert callback.getArity() == 1;
        assert callback.time() == Timed.Time.RUN;

        String callbackName = callback.name();
        assert callbackName.equals("exception") || callbackName.equals("message");

        if (callbacks == null) {
            callbacks = new ArrayList<CallDefinition>();
        }

        callbacks.add(callback);
    }

    /**
     * The default value elements for the struct defined for the exception.
     *
     * @return Maps the element for the key in the struct to the element in the default value.  When the list form of
     *   fields without default values is used, the Map value element is {@code  null}.
     */
    @NotNull
    public Map<PsiElement, PsiElement> defaultValueElementByKeyElement() {
        PsiElement[] finalArguments = finalArguments(navigationItem);

        assert finalArguments != null;
        assert finalArguments.length == 1;

        PsiElement finalArgument = finalArguments[0];
        Map<PsiElement, PsiElement> defaultValueElementByKeyElement = new HashMap<PsiElement, PsiElement>(finalArguments.length);

        if (finalArgument instanceof ElixirAccessExpression) {
            PsiElement accessExpressionChild = stripAccessExpression(finalArgument);

            assert accessExpressionChild instanceof ElixirList;

            ElixirList list = (ElixirList) accessExpressionChild;
            PsiElement[] listChildren = list.getChildren();

            if (listChildren.length == 1) {
                PsiElement listChild = listChildren[0];

                if (listChild instanceof QuotableKeywordList) {
                    QuotableKeywordList quotableKeywordList = (QuotableKeywordList) listChild;

                    putQuotableKeywordList(defaultValueElementByKeyElement, quotableKeywordList);
                } else {
                    defaultValueElementByKeyElement.put(listChild, null);
                }
            } else {
                for (PsiElement key : list.getChildren()) {
                    defaultValueElementByKeyElement.put(key, null);
                }
            }
        } else if (finalArgument instanceof QuotableKeywordList) {
            QuotableKeywordList quotableKeywordList = (QuotableKeywordList) finalArgument;

            putQuotableKeywordList(defaultValueElementByKeyElement, quotableKeywordList);
        } else {
            assert finalArgument != null;
        }

        return defaultValueElementByKeyElement;
    }


    /**
     * Returns the list of children of the tree element.
     *
     * @return the list of children.
     */
    @NotNull
    @Override
    public TreeElement[] getChildren() {
        List<TreeElement> childList = new ArrayList<TreeElement>();

        childList.add(
                new Structure(modular, navigationItem)
        );

        if (callbacks != null) {
            childList.addAll(callbacks);
        }

        return childList.toArray(new TreeElement[childList.size()]);
    }

    /**
     * Returns the presentation of the tree element.
     *
     * @return the element presentation.
     */
    @NotNull
    @Override
    public ItemPresentation getPresentation() {
        Parent parentPresentation = (Parent) modular.getPresentation();
        String location = parentPresentation.getLocatedPresentableText();
        int lastIndex = location.lastIndexOf('.');
        String parentLocation;
        String name;

        if (lastIndex != -1) {
            parentLocation = location.substring(0, lastIndex);
            name = location.substring(lastIndex + 1, location.length());
        } else {
            parentLocation = null;
            name = location;
        }

        return new org.elixir_lang.navigation.item_presentation.Exception(
                parentLocation,
                name
        );
    }

    private void putQuotableKeywordList(Map<PsiElement, PsiElement> defaultValueElementByKeyElement,
                                        QuotableKeywordList quotableKeywordList) {
        for (QuotableKeywordPair quotableKeywordPair : quotableKeywordList.quotableKeywordPairList()) {
            PsiElement keyElement = quotableKeywordPair.getKeywordKey();
            PsiElement valueElement = quotableKeywordPair.getKeywordValue();

            defaultValueElementByKeyElement.put(keyElement, valueElement);
        }
    }
}
