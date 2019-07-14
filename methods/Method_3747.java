@Override public void endAnimations(){
  int count=mPendingMoves.size();
  for (int i=count - 1; i >= 0; i--) {
    MoveInfo item=mPendingMoves.get(i);
    View view=item.holder.itemView;
    view.setTranslationY(0);
    view.setTranslationX(0);
    dispatchMoveFinished(item.holder);
    mPendingMoves.remove(i);
  }
  count=mPendingRemovals.size();
  for (int i=count - 1; i >= 0; i--) {
    RecyclerView.ViewHolder item=mPendingRemovals.get(i);
    dispatchRemoveFinished(item);
    mPendingRemovals.remove(i);
  }
  count=mPendingAdditions.size();
  for (int i=count - 1; i >= 0; i--) {
    RecyclerView.ViewHolder item=mPendingAdditions.get(i);
    item.itemView.setAlpha(1);
    dispatchAddFinished(item);
    mPendingAdditions.remove(i);
  }
  count=mPendingChanges.size();
  for (int i=count - 1; i >= 0; i--) {
    endChangeAnimationIfNecessary(mPendingChanges.get(i));
  }
  mPendingChanges.clear();
  if (!isRunning()) {
    return;
  }
  int listCount=mMovesList.size();
  for (int i=listCount - 1; i >= 0; i--) {
    ArrayList<MoveInfo> moves=mMovesList.get(i);
    count=moves.size();
    for (int j=count - 1; j >= 0; j--) {
      MoveInfo moveInfo=moves.get(j);
      RecyclerView.ViewHolder item=moveInfo.holder;
      View view=item.itemView;
      view.setTranslationY(0);
      view.setTranslationX(0);
      dispatchMoveFinished(moveInfo.holder);
      moves.remove(j);
      if (moves.isEmpty()) {
        mMovesList.remove(moves);
      }
    }
  }
  listCount=mAdditionsList.size();
  for (int i=listCount - 1; i >= 0; i--) {
    ArrayList<RecyclerView.ViewHolder> additions=mAdditionsList.get(i);
    count=additions.size();
    for (int j=count - 1; j >= 0; j--) {
      RecyclerView.ViewHolder item=additions.get(j);
      View view=item.itemView;
      view.setAlpha(1);
      dispatchAddFinished(item);
      additions.remove(j);
      if (additions.isEmpty()) {
        mAdditionsList.remove(additions);
      }
    }
  }
  listCount=mChangesList.size();
  for (int i=listCount - 1; i >= 0; i--) {
    ArrayList<ChangeInfo> changes=mChangesList.get(i);
    count=changes.size();
    for (int j=count - 1; j >= 0; j--) {
      endChangeAnimationIfNecessary(changes.get(j));
      if (changes.isEmpty()) {
        mChangesList.remove(changes);
      }
    }
  }
  cancelAll(mRemoveAnimations);
  cancelAll(mMoveAnimations);
  cancelAll(mAddAnimations);
  cancelAll(mChangeAnimations);
  dispatchAnimationsFinished();
}
