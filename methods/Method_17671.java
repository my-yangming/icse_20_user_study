@Override public void reset(){
  super.reset();
  mHasNewLayout=true;
  mWidth=YogaConstants.UNDEFINED;
  mHeight=YogaConstants.UNDEFINED;
  mTop=YogaConstants.UNDEFINED;
  mLeft=YogaConstants.UNDEFINED;
  mMarginLeft=0;
  mMarginTop=0;
  mMarginRight=0;
  mMarginBottom=0;
  mPaddingLeft=0;
  mPaddingTop=0;
  mPaddingRight=0;
  mPaddingBottom=0;
  mBorderLeft=0;
  mBorderTop=0;
  mBorderRight=0;
  mBorderBottom=0;
  mLayoutDirection=0;
  mDoesLegacyStretchFlagAffectsLayout=false;
}
