/** 
 * Sets the file where to output the SQL statements of a migration dry run.  {@code null} to execute the SQL statementsdirectly against the database. If the file specified is in a non-existent directory, Flyway will create all directories and parent directories as needed. <p><i>Flyway Pro and Flyway Enterprise only</i></p>
 * @param dryRunOutputFileName The name of the output file or {@code null} to execute the SQL statements directlyagainst the database.
 */
public void setDryRunOutputAsFileName(String dryRunOutputFileName){
  throw new org.flywaydb.core.internal.license.FlywayProUpgradeRequiredException("dryRunOutput");
}
