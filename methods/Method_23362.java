/** 
 * ( begin auto-generated from beginCamera.xml ) The <b>beginCamera()</b> and <b>endCamera()</b> functions enable advanced customization of the camera space. The functions are useful if you want to more control over camera movement, however for most users, the <b>camera()</b> function will be sufficient.<br /><br />The camera functions will replace any transformations (such as <b>rotate()</b> or <b>translate()</b>) that occur before them in <b>draw()</b>, but they will not automatically replace the camera transform itself. For this reason, camera functions should be placed at the beginning of <b>draw()</b> (so that transformations happen afterwards), and the <b>camera()</b> function can be used after <b>beginCamera()</b> if you want to reset the camera before applying transformations.<br /><br />This function sets the matrix mode to the camera matrix so calls such as <b>translate()</b>, <b>rotate()</b>, applyMatrix() and resetMatrix() affect the camera. <b>beginCamera()</b> should always be used with a following <b>endCamera()</b> and pairs of <b>beginCamera()</b> and <b>endCamera()</b> cannot be nested. ( end auto-generated )
 * @webref lights_camera:camera
 * @see PGraphics#camera()
 * @see PGraphics#endCamera()
 * @see PGraphics#applyMatrix(PMatrix)
 * @see PGraphics#resetMatrix()
 * @see PGraphics#translate(float,float,float)
 * @see PGraphics#scale(float,float,float)
 */
public void beginCamera(){
  showMethodWarning("beginCamera");
}
