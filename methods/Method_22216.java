@NonNull private String rotationToString(int rotation){
switch (rotation) {
case Surface.ROTATION_0:
    return "ROTATION_0";
case Surface.ROTATION_90:
  return "ROTATION_90";
case Surface.ROTATION_180:
return "ROTATION_180";
case Surface.ROTATION_270:
return "ROTATION_270";
default :
return String.valueOf(rotation);
}
}
