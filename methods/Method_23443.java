public void apply(float n00,float n01,float n02,float n03,float n10,float n11,float n12,float n13,float n20,float n21,float n22,float n23,float n30,float n31,float n32,float n33){
  float r00=m00 * n00 + m01 * n10 + m02 * n20 + m03 * n30;
  float r01=m00 * n01 + m01 * n11 + m02 * n21 + m03 * n31;
  float r02=m00 * n02 + m01 * n12 + m02 * n22 + m03 * n32;
  float r03=m00 * n03 + m01 * n13 + m02 * n23 + m03 * n33;
  float r10=m10 * n00 + m11 * n10 + m12 * n20 + m13 * n30;
  float r11=m10 * n01 + m11 * n11 + m12 * n21 + m13 * n31;
  float r12=m10 * n02 + m11 * n12 + m12 * n22 + m13 * n32;
  float r13=m10 * n03 + m11 * n13 + m12 * n23 + m13 * n33;
  float r20=m20 * n00 + m21 * n10 + m22 * n20 + m23 * n30;
  float r21=m20 * n01 + m21 * n11 + m22 * n21 + m23 * n31;
  float r22=m20 * n02 + m21 * n12 + m22 * n22 + m23 * n32;
  float r23=m20 * n03 + m21 * n13 + m22 * n23 + m23 * n33;
  float r30=m30 * n00 + m31 * n10 + m32 * n20 + m33 * n30;
  float r31=m30 * n01 + m31 * n11 + m32 * n21 + m33 * n31;
  float r32=m30 * n02 + m31 * n12 + m32 * n22 + m33 * n32;
  float r33=m30 * n03 + m31 * n13 + m32 * n23 + m33 * n33;
  m00=r00;
  m01=r01;
  m02=r02;
  m03=r03;
  m10=r10;
  m11=r11;
  m12=r12;
  m13=r13;
  m20=r20;
  m21=r21;
  m22=r22;
  m23=r23;
  m30=r30;
  m31=r31;
  m32=r32;
  m33=r33;
}