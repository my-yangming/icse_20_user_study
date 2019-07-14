/** 
 * ( begin auto-generated from beginRaw.xml ) To create vectors from 3D data, use the <b>beginRaw()</b> and <b>endRaw()</b> commands. These commands will grab the shape data just before it is rendered to the screen. At this stage, your entire scene is nothing but a long list of individual lines and triangles. This means that a shape created with <b>sphere()</b> function will be made up of hundreds of triangles, rather than a single object. Or that a multi-segment line shape (such as a curve) will be rendered as individual segments. <br /><br /> When using <b>beginRaw()</b> and <b>endRaw()</b>, it's possible to write to either a 2D or 3D renderer. For instance, <b>beginRaw()</b> with the PDF library will write the geometry as flattened triangles and lines, even if recording from the <b>P3D</b> renderer. <br /><br /> If you want a background to show up in your files, use <b>rect(0, 0, width, height)</b> after setting the <b>fill()</b> to the background color. Otherwise the background will not be rendered to the file because the background is not shape. <br /><br /> Using <b>hint(ENABLE_DEPTH_SORT)</b> can improve the appearance of 3D geometry drawn to 2D file formats. See the <b>hint()</b> reference for more details. <br /><br /> See examples in the reference for the <b>PDF</b> and <b>DXF</b> libraries for more information. ( end auto-generated )
 * @webref output:files
 * @param renderer for example, PDF or DXF
 * @param filename filename for output
 * @see PApplet#endRaw()
 * @see PApplet#hint(int)
 */
public PGraphics beginRaw(String renderer,String filename){
  filename=insertFrame(filename);
  PGraphics rec=createGraphics(width,height,renderer,filename);
  g.beginRaw(rec);
  return rec;
}
