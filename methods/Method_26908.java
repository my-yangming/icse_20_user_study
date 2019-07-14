/** 
 * Whether to add the children of given target to the list of target children to exclude from this transition. The <code>exclude</code> parameter specifies whether the target should be added to or removed from the excluded list. <p/> <p>Excluding targets is a general mechanism for allowing transitions to run on a view hierarchy while skipping target views that should not be part of the transition. For example, you may want to avoid animating children of a specific ListView or Spinner. Views can be excluded either by their id, or by their instance reference, or by the Class of that view (eg,  {@link Spinner}).</p>
 * @param target  The target to ignore when running this transition.
 * @param exclude Whether to add the target to or remove the target from thecurrent list of excluded targets.
 * @return This transition object.
 * @see #excludeTarget(View,boolean)
 * @see #excludeChildren(int,boolean)
 * @see #excludeChildren(Class,boolean)
 */
@NonNull public Transition excludeChildren(@Nullable View target,boolean exclude){
  mTargetChildExcludes=excludeObject(mTargetChildExcludes,target,exclude);
  return this;
}
