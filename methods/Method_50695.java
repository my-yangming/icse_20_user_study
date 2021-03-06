/** 
 * Check for suppression on this node, on parents, and on contained types for ASTCompilationUnit
 * @param node
 */
public static boolean isSupressed(Node node,Rule rule){
  boolean result=suppresses(node,rule);
  if (!result) {
    Node parent=node.jjtGetParent();
    while (!result && parent != null) {
      result=suppresses(parent,rule);
      parent=parent.jjtGetParent();
    }
  }
  return result;
}
