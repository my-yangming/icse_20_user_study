/** 
 * Assert a boolean expression, throwing an  {@code IllegalStateException} if the expression evaluates to {@code false}. <p>Call  {@link #isTrue} if you wish to throw an {@code IllegalArgumentException} on an assertion failure. <preclass="code">Assert.state(id == null, "The id property must not already be initialized");</pre>
 * @param expression a boolean expression.
 * @param message the exception message to use if the assertion fails.
 * @throws IllegalStateException if {@code expression} is {@code false}
 */
public static void state(boolean expression,String message){
  if (!expression)   throw new IllegalStateException(message);
}
