/** 
 * Sets up the font info object. Adds metrics for basic fonts and useful family-style-weight triplets for lookup.
 * @param fontInfo the font info object to set up
 * @param embedFontInfoList a list of EmbedFontInfo objects
 * @param resolver the font resolver
 */
public static void setup(FontInfo fontInfo,List embedFontInfoList,FontResolver resolver){
  final boolean base14Kerning=false;
  fontInfo.addMetrics("F1",new Helvetica(base14Kerning));
  fontInfo.addMetrics("F2",new HelveticaOblique(base14Kerning));
  fontInfo.addMetrics("F3",new HelveticaBold(base14Kerning));
  fontInfo.addMetrics("F4",new HelveticaBoldOblique(base14Kerning));
  fontInfo.addMetrics("F5",new TimesRoman(base14Kerning));
  fontInfo.addMetrics("F6",new TimesItalic(base14Kerning));
  fontInfo.addMetrics("F7",new TimesBold(base14Kerning));
  fontInfo.addMetrics("F8",new TimesBoldItalic(base14Kerning));
  fontInfo.addMetrics("F9",new Courier(base14Kerning));
  fontInfo.addMetrics("F10",new CourierOblique(base14Kerning));
  fontInfo.addMetrics("F11",new CourierBold(base14Kerning));
  fontInfo.addMetrics("F12",new CourierBoldOblique(base14Kerning));
  fontInfo.addMetrics("F13",new Symbol());
  fontInfo.addMetrics("F14",new ZapfDingbats());
  fontInfo.addFontProperties("F5","any",Font.STYLE_NORMAL,Font.WEIGHT_NORMAL);
  fontInfo.addFontProperties("F6","any",Font.STYLE_ITALIC,Font.WEIGHT_NORMAL);
  fontInfo.addFontProperties("F6","any",Font.STYLE_OBLIQUE,Font.WEIGHT_NORMAL);
  fontInfo.addFontProperties("F7","any",Font.STYLE_NORMAL,Font.WEIGHT_BOLD);
  fontInfo.addFontProperties("F8","any",Font.STYLE_ITALIC,Font.WEIGHT_BOLD);
  fontInfo.addFontProperties("F8","any",Font.STYLE_OBLIQUE,Font.WEIGHT_BOLD);
  fontInfo.addFontProperties("F1","sans-serif",Font.STYLE_NORMAL,Font.WEIGHT_NORMAL);
  fontInfo.addFontProperties("F2","sans-serif",Font.STYLE_OBLIQUE,Font.WEIGHT_NORMAL);
  fontInfo.addFontProperties("F2","sans-serif",Font.STYLE_ITALIC,Font.WEIGHT_NORMAL);
  fontInfo.addFontProperties("F3","sans-serif",Font.STYLE_NORMAL,Font.WEIGHT_BOLD);
  fontInfo.addFontProperties("F4","sans-serif",Font.STYLE_OBLIQUE,Font.WEIGHT_BOLD);
  fontInfo.addFontProperties("F4","sans-serif",Font.STYLE_ITALIC,Font.WEIGHT_BOLD);
  fontInfo.addFontProperties("F1","SansSerif",Font.STYLE_NORMAL,Font.WEIGHT_NORMAL);
  fontInfo.addFontProperties("F2","SansSerif",Font.STYLE_OBLIQUE,Font.WEIGHT_NORMAL);
  fontInfo.addFontProperties("F2","SansSerif",Font.STYLE_ITALIC,Font.WEIGHT_NORMAL);
  fontInfo.addFontProperties("F3","SansSerif",Font.STYLE_NORMAL,Font.WEIGHT_BOLD);
  fontInfo.addFontProperties("F4","SansSerif",Font.STYLE_OBLIQUE,Font.WEIGHT_BOLD);
  fontInfo.addFontProperties("F4","SansSerif",Font.STYLE_ITALIC,Font.WEIGHT_BOLD);
  fontInfo.addFontProperties("F5","serif",Font.STYLE_NORMAL,Font.WEIGHT_NORMAL);
  fontInfo.addFontProperties("F6","serif",Font.STYLE_OBLIQUE,Font.WEIGHT_NORMAL);
  fontInfo.addFontProperties("F6","serif",Font.STYLE_ITALIC,Font.WEIGHT_NORMAL);
  fontInfo.addFontProperties("F7","serif",Font.STYLE_NORMAL,Font.WEIGHT_BOLD);
  fontInfo.addFontProperties("F8","serif",Font.STYLE_OBLIQUE,Font.WEIGHT_BOLD);
  fontInfo.addFontProperties("F8","serif",Font.STYLE_ITALIC,Font.WEIGHT_BOLD);
  fontInfo.addFontProperties("F9","monospace",Font.STYLE_NORMAL,Font.WEIGHT_NORMAL);
  fontInfo.addFontProperties("F10","monospace",Font.STYLE_OBLIQUE,Font.WEIGHT_NORMAL);
  fontInfo.addFontProperties("F10","monospace",Font.STYLE_ITALIC,Font.WEIGHT_NORMAL);
  fontInfo.addFontProperties("F11","monospace",Font.STYLE_NORMAL,Font.WEIGHT_BOLD);
  fontInfo.addFontProperties("F12","monospace",Font.STYLE_OBLIQUE,Font.WEIGHT_BOLD);
  fontInfo.addFontProperties("F12","monospace",Font.STYLE_ITALIC,Font.WEIGHT_BOLD);
  fontInfo.addFontProperties("F9","Monospaced",Font.STYLE_NORMAL,Font.WEIGHT_NORMAL);
  fontInfo.addFontProperties("F10","Monospaced",Font.STYLE_OBLIQUE,Font.WEIGHT_NORMAL);
  fontInfo.addFontProperties("F10","Monospaced",Font.STYLE_ITALIC,Font.WEIGHT_NORMAL);
  fontInfo.addFontProperties("F11","Monospaced",Font.STYLE_NORMAL,Font.WEIGHT_BOLD);
  fontInfo.addFontProperties("F12","Monospaced",Font.STYLE_OBLIQUE,Font.WEIGHT_BOLD);
  fontInfo.addFontProperties("F12","Monospaced",Font.STYLE_ITALIC,Font.WEIGHT_BOLD);
  fontInfo.addFontProperties("F1","Helvetica",Font.STYLE_NORMAL,Font.WEIGHT_NORMAL);
  fontInfo.addFontProperties("F2","Helvetica",Font.STYLE_OBLIQUE,Font.WEIGHT_NORMAL);
  fontInfo.addFontProperties("F2","Helvetica",Font.STYLE_ITALIC,Font.WEIGHT_NORMAL);
  fontInfo.addFontProperties("F3","Helvetica",Font.STYLE_NORMAL,Font.WEIGHT_BOLD);
  fontInfo.addFontProperties("F4","Helvetica",Font.STYLE_OBLIQUE,Font.WEIGHT_BOLD);
  fontInfo.addFontProperties("F4","Helvetica",Font.STYLE_ITALIC,Font.WEIGHT_BOLD);
  fontInfo.addFontProperties("F5","Times",Font.STYLE_NORMAL,Font.WEIGHT_NORMAL);
  fontInfo.addFontProperties("F6","Times",Font.STYLE_OBLIQUE,Font.WEIGHT_NORMAL);
  fontInfo.addFontProperties("F6","Times",Font.STYLE_ITALIC,Font.WEIGHT_NORMAL);
  fontInfo.addFontProperties("F7","Times",Font.STYLE_NORMAL,Font.WEIGHT_BOLD);
  fontInfo.addFontProperties("F8","Times",Font.STYLE_OBLIQUE,Font.WEIGHT_BOLD);
  fontInfo.addFontProperties("F8","Times",Font.STYLE_ITALIC,Font.WEIGHT_BOLD);
  fontInfo.addFontProperties("F9","Courier",Font.STYLE_NORMAL,Font.WEIGHT_NORMAL);
  fontInfo.addFontProperties("F10","Courier",Font.STYLE_OBLIQUE,Font.WEIGHT_NORMAL);
  fontInfo.addFontProperties("F10","Courier",Font.STYLE_ITALIC,Font.WEIGHT_NORMAL);
  fontInfo.addFontProperties("F11","Courier",Font.STYLE_NORMAL,Font.WEIGHT_BOLD);
  fontInfo.addFontProperties("F12","Courier",Font.STYLE_OBLIQUE,Font.WEIGHT_BOLD);
  fontInfo.addFontProperties("F12","Courier",Font.STYLE_ITALIC,Font.WEIGHT_BOLD);
  fontInfo.addFontProperties("F13","Symbol",Font.STYLE_NORMAL,Font.WEIGHT_NORMAL);
  fontInfo.addFontProperties("F14","ZapfDingbats",Font.STYLE_NORMAL,Font.WEIGHT_NORMAL);
  fontInfo.addFontProperties("F5","Times-Roman",Font.STYLE_NORMAL,Font.WEIGHT_NORMAL);
  fontInfo.addFontProperties("F6","Times-Roman",Font.STYLE_OBLIQUE,Font.WEIGHT_NORMAL);
  fontInfo.addFontProperties("F6","Times-Roman",Font.STYLE_ITALIC,Font.WEIGHT_NORMAL);
  fontInfo.addFontProperties("F7","Times-Roman",Font.STYLE_NORMAL,Font.WEIGHT_BOLD);
  fontInfo.addFontProperties("F8","Times-Roman",Font.STYLE_OBLIQUE,Font.WEIGHT_BOLD);
  fontInfo.addFontProperties("F8","Times-Roman",Font.STYLE_ITALIC,Font.WEIGHT_BOLD);
  fontInfo.addFontProperties("F5","Times Roman",Font.STYLE_NORMAL,Font.WEIGHT_NORMAL);
  fontInfo.addFontProperties("F6","Times Roman",Font.STYLE_OBLIQUE,Font.WEIGHT_NORMAL);
  fontInfo.addFontProperties("F6","Times Roman",Font.STYLE_ITALIC,Font.WEIGHT_NORMAL);
  fontInfo.addFontProperties("F7","Times Roman",Font.STYLE_NORMAL,Font.WEIGHT_BOLD);
  fontInfo.addFontProperties("F8","Times Roman",Font.STYLE_OBLIQUE,Font.WEIGHT_BOLD);
  fontInfo.addFontProperties("F8","Times Roman",Font.STYLE_ITALIC,Font.WEIGHT_BOLD);
  fontInfo.addFontProperties("F9","Computer-Modern-Typewriter",Font.STYLE_NORMAL,Font.WEIGHT_NORMAL);
  final int startNum=15;
  addConfiguredFonts(fontInfo,embedFontInfoList,startNum,resolver);
}
