/** 
 * @hide This method should be called by ItemTouchHelper only.
 */
@RestrictTo(LIBRARY_GROUP_PREFIX) @Override public void prepareForDrop(@NonNull View view,@NonNull View target,int x,int y){
  assertNotInLayoutOrScroll("Cannot drop a view during a scroll or layout calculation");
  ensureLayoutState();
  resolveShouldLayoutReverse();
  final int myPos=getPosition(view);
  final int targetPos=getPosition(target);
  final int dropDirection=myPos < targetPos ? LayoutState.ITEM_DIRECTION_TAIL : LayoutState.ITEM_DIRECTION_HEAD;
  if (mShouldReverseLayout) {
    if (dropDirection == LayoutState.ITEM_DIRECTION_TAIL) {
      scrollToPositionWithOffset(targetPos,mOrientationHelper.getEndAfterPadding() - (mOrientationHelper.getDecoratedStart(target) + mOrientationHelper.getDecoratedMeasurement(view)));
    }
 else {
      scrollToPositionWithOffset(targetPos,mOrientationHelper.getEndAfterPadding() - mOrientationHelper.getDecoratedEnd(target));
    }
  }
 else {
    if (dropDirection == LayoutState.ITEM_DIRECTION_HEAD) {
      scrollToPositionWithOffset(targetPos,mOrientationHelper.getDecoratedStart(target));
    }
 else {
      scrollToPositionWithOffset(targetPos,mOrientationHelper.getDecoratedEnd(target) - mOrientationHelper.getDecoratedMeasurement(view));
    }
  }
}
