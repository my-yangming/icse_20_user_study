private boolean allCommentsAreIgnored(){
  return getProperty(OVERRIDE_CMT_DESCRIPTOR) == CommentRequirement.Ignored && getProperty(ACCESSOR_CMT_DESCRIPTOR) == CommentRequirement.Ignored && getProperty(HEADER_CMT_REQUIREMENT_DESCRIPTOR) == CommentRequirement.Ignored && getProperty(FIELD_CMT_REQUIREMENT_DESCRIPTOR) == CommentRequirement.Ignored && getProperty(PUB_METHOD_CMT_REQUIREMENT_DESCRIPTOR) == CommentRequirement.Ignored && getProperty(PROT_METHOD_CMT_REQUIREMENT_DESCRIPTOR) == CommentRequirement.Ignored && getProperty(ENUM_CMT_REQUIREMENT_DESCRIPTOR) == CommentRequirement.Ignored && getProperty(SERIAL_VERSION_UID_CMT_REQUIREMENT_DESCRIPTOR) == CommentRequirement.Ignored && getProperty(SERIAL_PERSISTENT_FIELDS_CMT_REQUIREMENT_DESCRIPTOR) == CommentRequirement.Ignored;
}