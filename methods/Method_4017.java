@Override public boolean animateDisappearance(@NonNull RecyclerView.ViewHolder viewHolder,@NonNull ItemHolderInfo preLayoutInfo,@Nullable ItemHolderInfo postLayoutInfo){
  int oldLeft=preLayoutInfo.left;
  int oldTop=preLayoutInfo.top;
  View disappearingItemView=viewHolder.itemView;
  int newLeft=postLayoutInfo == null ? disappearingItemView.getLeft() : postLayoutInfo.left;
  int newTop=postLayoutInfo == null ? disappearingItemView.getTop() : postLayoutInfo.top;
  if (!viewHolder.isRemoved() && (oldLeft != newLeft || oldTop != newTop)) {
    disappearingItemView.layout(newLeft,newTop,newLeft + disappearingItemView.getWidth(),newTop + disappearingItemView.getHeight());
    if (DEBUG) {
      Log.d(TAG,"DISAPPEARING: " + viewHolder + " with view " + disappearingItemView);
    }
    return animateMove(viewHolder,oldLeft,oldTop,newLeft,newTop);
  }
 else {
    if (DEBUG) {
      Log.d(TAG,"REMOVED: " + viewHolder + " with view " + disappearingItemView);
    }
    return animateRemove(viewHolder);
  }
}
