public static float[] MultiplyMat4f(float[] a,float[] b){
  float out[]=new float[16];
  out[0]=a[0] * b[0] + a[4] * b[1] + a[8] * b[2] + a[12] * b[3];
  out[1]=a[1] * b[0] + a[5] * b[1] + a[9] * b[2] + a[13] * b[3];
  out[2]=a[2] * b[0] + a[6] * b[1] + a[10] * b[2] + a[14] * b[3];
  out[3]=a[3] * b[0] + a[7] * b[1] + a[11] * b[2] + a[15] * b[3];
  out[4]=a[0] * b[4] + a[4] * b[5] + a[8] * b[6] + a[12] * b[7];
  out[5]=a[1] * b[4] + a[5] * b[5] + a[9] * b[6] + a[13] * b[7];
  out[6]=a[2] * b[4] + a[6] * b[5] + a[10] * b[6] + a[14] * b[7];
  out[7]=a[3] * b[4] + a[7] * b[5] + a[11] * b[6] + a[15] * b[7];
  out[8]=a[0] * b[8] + a[4] * b[9] + a[8] * b[10] + a[12] * b[11];
  out[9]=a[1] * b[8] + a[5] * b[9] + a[9] * b[10] + a[13] * b[11];
  out[10]=a[2] * b[8] + a[6] * b[9] + a[10] * b[10] + a[14] * b[11];
  out[11]=a[3] * b[8] + a[7] * b[9] + a[11] * b[10] + a[15] * b[11];
  out[12]=a[0] * b[12] + a[4] * b[13] + a[8] * b[14] + a[12] * b[15];
  out[13]=a[1] * b[12] + a[5] * b[13] + a[9] * b[14] + a[13] * b[15];
  out[14]=a[2] * b[12] + a[6] * b[13] + a[10] * b[14] + a[14] * b[15];
  out[15]=a[3] * b[12] + a[7] * b[13] + a[11] * b[14] + a[15] * b[15];
  return out;
}
