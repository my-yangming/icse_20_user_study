@Override public boolean equalsNode(AbstractReportNode arg0){
  if (!(arg0 instanceof ViolationNode)) {
    return false;
  }
  RuleViolation rv=((ViolationNode)arg0).getRuleViolation();
  return rv.getFilename().equals(getRuleViolation().getFilename()) && rv.getBeginLine() == getRuleViolation().getBeginLine() && rv.getBeginColumn() == getRuleViolation().getBeginColumn() && rv.getEndLine() == getRuleViolation().getEndLine() && rv.getEndColumn() == getRuleViolation().getEndColumn() && rv.getVariableName().equals(getRuleViolation().getVariableName());
}
