@Override public void collectInitialPrefetchPositions(int adapterItemCount,LayoutPrefetchRegistry layoutPrefetchRegistry){
  final boolean fromEnd;
  final int anchorPos;
  if (mPendingSavedState != null && mPendingSavedState.hasValidAnchor()) {
    fromEnd=mPendingSavedState.mAnchorLayoutFromEnd;
    anchorPos=mPendingSavedState.mAnchorPosition;
  }
 else {
    resolveShouldLayoutReverse();
    fromEnd=mShouldReverseLayout;
    if (mPendingScrollPosition == RecyclerView.NO_POSITION) {
      anchorPos=fromEnd ? adapterItemCount - 1 : 0;
    }
 else {
      anchorPos=mPendingScrollPosition;
    }
  }
  final int direction=fromEnd ? LayoutState.ITEM_DIRECTION_HEAD : LayoutState.ITEM_DIRECTION_TAIL;
  int targetPos=anchorPos;
  for (int i=0; i < mInitialPrefetchItemCount; i++) {
    if (targetPos >= 0 && targetPos < adapterItemCount) {
      layoutPrefetchRegistry.addPosition(targetPos,0);
    }
 else {
      break;
    }
    targetPos+=direction;
  }
}
