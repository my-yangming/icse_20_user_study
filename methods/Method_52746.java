@Override protected TokenManager getLexerForSource(SourceCode sourceCode){
  StringBuilder buffer=sourceCode.getCodeBuffer();
  return new MatlabTokenManager(IOUtil.skipBOM(new StringReader(buffer.toString())));
}
