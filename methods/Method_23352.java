/** 
 * ( begin auto-generated from rotate.xml ) Rotates a shape the amount specified by the <b>angle</b> parameter. Angles should be specified in radians (values from 0 to TWO_PI) or converted to radians with the <b>radians()</b> function. <br/> <br/> Objects are always rotated around their relative position to the origin and positive numbers rotate objects in a clockwise direction. Transformations apply to everything that happens after and subsequent calls to the function accumulates the effect. For example, calling <b>rotate(HALF_PI)</b> and then <b>rotate(HALF_PI)</b> is the same as <b>rotate(PI)</b>. All tranformations are reset when <b>draw()</b> begins again. <br/> <br/> Technically, <b>rotate()</b> multiplies the current transformation matrix by a rotation matrix. This function can be further controlled by the <b>pushMatrix()</b> and <b>popMatrix()</b>. ( end auto-generated )
 * @webref transform
 * @param angle angle of rotation specified in radians
 * @see PGraphics#popMatrix()
 * @see PGraphics#pushMatrix()
 * @see PGraphics#rotateX(float)
 * @see PGraphics#rotateY(float)
 * @see PGraphics#rotateZ(float)
 * @see PGraphics#scale(float,float,float)
 * @see PApplet#radians(float)
 */
public void rotate(float angle){
  showMissingWarning("rotate");
}
