@Override public boolean animateAppearance(@NonNull RecyclerView.ViewHolder viewHolder,@Nullable ItemHolderInfo preLayoutInfo,@NonNull ItemHolderInfo postLayoutInfo){
  if (preLayoutInfo != null && (preLayoutInfo.left != postLayoutInfo.left || preLayoutInfo.top != postLayoutInfo.top)) {
    if (DEBUG) {
      Log.d(TAG,"APPEARING: " + viewHolder + " with view " + viewHolder);
    }
    return animateMove(viewHolder,preLayoutInfo.left,preLayoutInfo.top,postLayoutInfo.left,postLayoutInfo.top);
  }
 else {
    if (DEBUG) {
      Log.d(TAG,"ADDED: " + viewHolder + " with view " + viewHolder);
    }
    return animateAdd(viewHolder);
  }
}
