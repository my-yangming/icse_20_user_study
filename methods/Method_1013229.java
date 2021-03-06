final public SyntaxTreeNode Substitution() throws ParseException {
  SyntaxTreeNode zn[]=new SyntaxTreeNode[3];
  SyntaxTreeNode tn=null;
  Token t;
  anchor=null;
  String n;
  bpa("Substitution");
switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
case IDENTIFIER:
    tn=Identifier();
  zn[0]=tn;
break;
case op_76:
case op_26:
case op_29:
case op_58:
case CASESEP:
case op_61:
case op_112:
case op_113:
case op_114:
case op_115:
case op_116:
tn=NonExpPrefixOp();
zn[0]=tn;
break;
case op_1:
case AND:
case op_3:
case op_4:
case OR:
case op_6:
case op_7:
case op_8:
case op_9:
case op_10:
case op_11:
case op_12:
case op_13:
case op_14:
case op_15:
case op_16:
case op_17:
case op_18:
case op_19:
case IN:
case op_21:
case op_22:
case op_23:
case op_24:
case op_25:
case op_27:
case op_30:
case op_31:
case op_32:
case op_33:
case op_34:
case op_35:
case op_36:
case op_37:
case op_38:
case op_39:
case op_40:
case op_41:
case op_42:
case op_43:
case op_44:
case op_45:
case op_46:
case op_47:
case op_48:
case op_49:
case op_50:
case op_51:
case op_52:
case op_53:
case op_54:
case op_55:
case op_56:
case op_59:
case op_62:
case op_63:
case op_64:
case EQUALS:
case op_66:
case op_67:
case op_71:
case op_72:
case op_73:
case op_74:
case op_75:
case op_77:
case op_78:
case op_79:
case op_80:
case op_81:
case op_82:
case op_83:
case op_84:
case op_85:
case op_86:
case op_87:
case op_88:
case op_89:
case op_90:
case op_91:
case op_92:
case op_93:
case op_94:
case op_95:
case op_96:
case op_97:
case op_98:
case op_100:
case op_101:
case op_102:
case op_103:
case op_104:
case op_105:
case op_106:
case op_107:
case op_108:
case op_109:
case op_110:
case op_111:
case op_117:
case op_118:
case op_119:
tn=InfixOp();
zn[0]=tn;
break;
case op_57:
case op_68:
case op_69:
case op_70:
tn=PostfixOp();
zn[0]=tn;
break;
default :
jj_la1[35]=jj_gen;
jj_consume_token(-1);
throw new ParseException();
}
expecting="<-";
t=jj_consume_token(SUBSTITUTE);
n=tn.getImage();
zn[1]=new SyntaxTreeNode(mn,t);
expecting="Expression or Op. Symbol";
tn=OpOrExpr();
epa();
zn[2]=tn;
{
if (true) return new SyntaxTreeNode(mn,N_Substitution,zn);
}
throw new Error("Missing return statement in function");
}
