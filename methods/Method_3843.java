@Override public void assertNotInLayoutOrScroll(String message){
  if (mPendingSavedState == null) {
    super.assertNotInLayoutOrScroll(message);
  }
}
