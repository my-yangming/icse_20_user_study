@Override public void updateMeasureState(TextPaint p){
  p.setColor(textPaint.getColor());
  p.setTypeface(textPaint.getTypeface());
  p.setFlags(textPaint.getFlags());
  p.setTextSize(textPaint.getTextSize());
  p.baselineShift=textPaint.baselineShift;
  p.bgColor=textPaint.bgColor;
}
