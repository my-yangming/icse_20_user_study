@Override public Sprite[] onCreateChild(){
  Dot[] dots=new Dot[12];
  for (int i=0; i < dots.length; i++) {
    dots[i]=new Dot();
    dots[i].setAnimationDelay(100 * i + -1200);
  }
  return dots;
}
