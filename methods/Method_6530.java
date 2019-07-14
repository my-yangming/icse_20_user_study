public static Bitmap loadBitmap(String path,Uri uri,float maxWidth,float maxHeight,boolean useMaxScale){
  BitmapFactory.Options bmOptions=new BitmapFactory.Options();
  bmOptions.inJustDecodeBounds=true;
  InputStream inputStream=null;
  if (path == null && uri != null && uri.getScheme() != null) {
    String imageFilePath=null;
    if (uri.getScheme().contains("file")) {
      path=uri.getPath();
    }
 else {
      try {
        path=AndroidUtilities.getPath(uri);
      }
 catch (      Throwable e) {
        FileLog.e(e);
      }
    }
  }
  if (path != null) {
    BitmapFactory.decodeFile(path,bmOptions);
  }
 else   if (uri != null) {
    boolean error=false;
    try {
      inputStream=ApplicationLoader.applicationContext.getContentResolver().openInputStream(uri);
      BitmapFactory.decodeStream(inputStream,null,bmOptions);
      inputStream.close();
      inputStream=ApplicationLoader.applicationContext.getContentResolver().openInputStream(uri);
    }
 catch (    Throwable e) {
      FileLog.e(e);
      return null;
    }
  }
  float photoW=bmOptions.outWidth;
  float photoH=bmOptions.outHeight;
  float scaleFactor=useMaxScale ? Math.max(photoW / maxWidth,photoH / maxHeight) : Math.min(photoW / maxWidth,photoH / maxHeight);
  if (scaleFactor < 1) {
    scaleFactor=1;
  }
  bmOptions.inJustDecodeBounds=false;
  bmOptions.inSampleSize=(int)scaleFactor;
  if (bmOptions.inSampleSize % 2 != 0) {
    int sample=1;
    while (sample * 2 < bmOptions.inSampleSize) {
      sample*=2;
    }
    bmOptions.inSampleSize=sample;
  }
  bmOptions.inPurgeable=Build.VERSION.SDK_INT < 21;
  String exifPath=null;
  if (path != null) {
    exifPath=path;
  }
 else   if (uri != null) {
    exifPath=AndroidUtilities.getPath(uri);
  }
  Matrix matrix=null;
  if (exifPath != null) {
    try {
      ExifInterface exif=new ExifInterface(exifPath);
      int orientation=exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,1);
      matrix=new Matrix();
switch (orientation) {
case ExifInterface.ORIENTATION_ROTATE_90:
        matrix.postRotate(90);
      break;
case ExifInterface.ORIENTATION_ROTATE_180:
    matrix.postRotate(180);
  break;
case ExifInterface.ORIENTATION_ROTATE_270:
matrix.postRotate(270);
break;
}
}
 catch (Throwable ignore) {
}
}
Bitmap b=null;
if (path != null) {
try {
b=BitmapFactory.decodeFile(path,bmOptions);
if (b != null) {
if (bmOptions.inPurgeable) {
Utilities.pinBitmap(b);
}
Bitmap newBitmap=Bitmaps.createBitmap(b,0,0,b.getWidth(),b.getHeight(),matrix,true);
if (newBitmap != b) {
b.recycle();
b=newBitmap;
}
}
}
 catch (Throwable e) {
FileLog.e(e);
ImageLoader.getInstance().clearMemory();
try {
if (b == null) {
b=BitmapFactory.decodeFile(path,bmOptions);
if (b != null && bmOptions.inPurgeable) {
Utilities.pinBitmap(b);
}
}
if (b != null) {
Bitmap newBitmap=Bitmaps.createBitmap(b,0,0,b.getWidth(),b.getHeight(),matrix,true);
if (newBitmap != b) {
b.recycle();
b=newBitmap;
}
}
}
 catch (Throwable e2) {
FileLog.e(e2);
}
}
}
 else if (uri != null) {
try {
b=BitmapFactory.decodeStream(inputStream,null,bmOptions);
if (b != null) {
if (bmOptions.inPurgeable) {
Utilities.pinBitmap(b);
}
Bitmap newBitmap=Bitmaps.createBitmap(b,0,0,b.getWidth(),b.getHeight(),matrix,true);
if (newBitmap != b) {
b.recycle();
b=newBitmap;
}
}
}
 catch (Throwable e) {
FileLog.e(e);
}
 finally {
try {
inputStream.close();
}
 catch (Throwable e) {
FileLog.e(e);
}
}
}
return b;
}
