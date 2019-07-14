/** 
 * Returns whether changes to parent should use an overlay or not. When the parent change doesn't use an overlay, it affects the transforms of the child. The default value is <code>true</code>. <p>Note: when Overlays are not used when a parent changes, a view can be clipped when it moves outside the bounds of its parent. Setting {@link android.view.ViewGroup#setClipChildren(boolean)} and{@link android.view.ViewGroup#setClipToPadding(boolean)} can help. Also, whenOverlays are not used and the parent is animating its location, the position of the child view will be relative to its parent's final position, so it may appear to "jump" at the beginning.</p>
 * @return <code>true</code> when a changed parent should execute the transitioninside the scene root's overlay or <code>false</code> if a parent change only affects the transform of the transitioning view.
 * @attr ref android.R.styleable#ChangeTransform_reparentWithOverlay
 */
public boolean getReparentWithOverlay(){
  return mUseOverlay;
}
