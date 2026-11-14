package info.gianlucacosta.eighthbridge.graphs

package object directed {
  /**
   * Function passed to fold(). Its signature must include:
   * <ul>
   * <li><b>cumulatedValue</b> - the value cumulated until now</li>
   * <li><b>vertex</b> - the current vertex</li>
   * </ul>
   *
   * The function must return the next iteration value for <b>fold()</b>.
   */
  type VertexFoldProcessor[T, V <: Vertex] =
    (T, V) => T
}
