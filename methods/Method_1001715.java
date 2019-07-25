static private BufferedImage resize(BufferedImage im,int minsize){
  Log.info("Resizing file to " + minsize);
  if (im.getWidth() >= minsize) {
    return im;
  }
  final BufferedImage newIm=new BufferedImage(minsize,im.getHeight(),BufferedImage.TYPE_INT_RGB);
  final Graphics2D g2d=newIm.createGraphics();
  g2d.setColor(Color.WHITE);
  g2d.fillRect(0,0,newIm.getWidth(),newIm.getHeight());
  final int delta=(minsize - im.getWidth()) / 2;
  g2d.drawImage(im,delta,0,null);
  g2d.dispose();
  return newIm;
}
