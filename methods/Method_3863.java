@Override public boolean supportsPredictiveItemAnimations(){
  return mPendingSavedState == null && mLastStackFromEnd == mStackFromEnd;
}
