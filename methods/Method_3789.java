@Override public boolean supportsPredictiveItemAnimations(){
  return mPendingSavedState == null && !mPendingSpanCountChange;
}
