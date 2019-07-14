private void buildShadow(){
  final int shadowHorizontal=getShadowHorizontal(mRawShadowSize);
  final int shadowTop=getShadowTop(mRawShadowSize);
  final int shadowBottom=getShadowBottom(mRawShadowSize);
  final float shadowCornerRadius=shadowHorizontal + mCornerRadius;
  mCornerShadowPaint.setShader(new RadialGradient(shadowCornerRadius,shadowCornerRadius,shadowCornerRadius,new int[]{mShadowStartColor,mShadowStartColor,mShadowEndColor},new float[]{0f,.2f,1f},Shader.TileMode.CLAMP));
  final RectF topInnerBounds=new RectF(shadowHorizontal,shadowTop,shadowHorizontal + 2 * mCornerRadius,shadowTop + 2 * mCornerRadius);
  final RectF topOuterBounds=new RectF(0,0,2 * mCornerRadius,2 * mCornerRadius);
  mCornerShadowTopPath.reset();
  mCornerShadowTopPath.setFillType(Path.FillType.EVEN_ODD);
  mCornerShadowTopPath.moveTo(shadowHorizontal + mCornerRadius,shadowTop);
  mCornerShadowTopPath.arcTo(topInnerBounds,270f,-90f,true);
  mCornerShadowTopPath.rLineTo(-shadowHorizontal,0);
  mCornerShadowTopPath.lineTo(0,mCornerRadius);
  mCornerShadowTopPath.arcTo(topOuterBounds,180f,90f,true);
  mCornerShadowTopPath.lineTo(shadowHorizontal + mCornerRadius,0);
  mCornerShadowTopPath.rLineTo(0,shadowTop);
  mCornerShadowTopPath.close();
  final RectF bottomInnerBounds=new RectF(getShadowHorizontal(mRawShadowSize),getShadowBottom(mRawShadowSize),getShadowHorizontal(mRawShadowSize) + 2 * mCornerRadius,getShadowBottom(mRawShadowSize) + 2 * mCornerRadius);
  final RectF bottomOuterBounds=new RectF(0,0,2 * mCornerRadius,2 * mCornerRadius);
  mCornerShadowBottomPath.reset();
  mCornerShadowBottomPath.setFillType(Path.FillType.EVEN_ODD);
  mCornerShadowBottomPath.moveTo(shadowHorizontal + mCornerRadius,shadowBottom);
  mCornerShadowBottomPath.arcTo(bottomInnerBounds,270f,-90f,true);
  mCornerShadowBottomPath.rLineTo(-shadowHorizontal,0);
  mCornerShadowBottomPath.lineTo(0,mCornerRadius);
  mCornerShadowBottomPath.arcTo(bottomOuterBounds,180f,90f,true);
  mCornerShadowBottomPath.lineTo(shadowHorizontal + mCornerRadius,0);
  mCornerShadowBottomPath.rLineTo(0,shadowBottom);
  mCornerShadowBottomPath.close();
  mEdgeShadowPaint.setShader(new LinearGradient(0,shadowCornerRadius,0,0,new int[]{mShadowStartColor,mShadowStartColor,mShadowEndColor},new float[]{0f,.2f,1f},Shader.TileMode.CLAMP));
  mEdgeShadowPaint.setAntiAlias(false);
}