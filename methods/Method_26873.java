/** 
 * Returns whether parent changes will be tracked by the ChangeTransform. If parent changes are tracked, then the transform will adjust to the transforms of the different parents. If they aren't tracked, only the transforms of the transitioning view will be tracked. Default is true.
 * @return whether parent changes will be tracked by the ChangeTransform.
 * @attr ref android.R.styleable#ChangeTransform_reparent
 */
public boolean getReparent(){
  return mReparent;
}
