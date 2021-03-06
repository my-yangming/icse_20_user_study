@Override public void doWork(){
  final long invalidationDelay=mGifDrawable.mNativeInfoHandle.renderFrame(mGifDrawable.mBuffer);
  if (invalidationDelay >= 0) {
    mGifDrawable.mNextFrameRenderTime=SystemClock.uptimeMillis() + invalidationDelay;
    if (mGifDrawable.isVisible() && mGifDrawable.mIsRunning && !mGifDrawable.mIsRenderingTriggeredOnDraw) {
      mGifDrawable.mExecutor.remove(this);
      mGifDrawable.mRenderTaskSchedule=mGifDrawable.mExecutor.schedule(this,invalidationDelay,TimeUnit.MILLISECONDS);
    }
    if (!mGifDrawable.mListeners.isEmpty() && mGifDrawable.getCurrentFrameIndex() == mGifDrawable.mNativeInfoHandle.getNumberOfFrames() - 1) {
      mGifDrawable.mInvalidationHandler.sendEmptyMessageAtTime(mGifDrawable.getCurrentLoop(),mGifDrawable.mNextFrameRenderTime);
    }
  }
 else {
    mGifDrawable.mNextFrameRenderTime=Long.MIN_VALUE;
    mGifDrawable.mIsRunning=false;
  }
  if (mGifDrawable.isVisible() && !mGifDrawable.mInvalidationHandler.hasMessages(MSG_TYPE_INVALIDATION)) {
    mGifDrawable.mInvalidationHandler.sendEmptyMessageAtTime(MSG_TYPE_INVALIDATION,0);
  }
}
