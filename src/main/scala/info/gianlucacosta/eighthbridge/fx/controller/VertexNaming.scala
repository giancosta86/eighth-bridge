package info.gianlucacosta.eighthbridge.fx.controller

import info.gianlucacosta.eighthbridge.fx.{VisualGraph, VisualLink, VisualVertex}
import info.gianlucacosta.eighthbridge.graphs.features.Named
import scalafx.geometry.Point2D

trait VertexNaming[V <: VisualVertex with Named, L <: VisualLink, G <: VisualGraph[V, L]]
  extends VertexEditing[V, L, G] {
  /**
   * The first index used when creating vertexes
   */
  protected val firstIndex =
    1

  /**
   * Given a vertex index, returns the vertex name
   *
   * @param vertexIndex
   * @return
   */
  protected def getVertexName(vertexIndex: Int): String =
    s"V${vertexIndex}"

  /**
   * Actually instantiate the vertex
   *
   * @param center
   * @param vertexName
   * @return
   */
  protected def instantiateVertex(center: Point2D, vertexName: String): V

  override def createVertex(center: Point2D): Option[G] = {
    val lastUsedVertexIndex = Stream.from(firstIndex)
      .takeWhile(vertexIndex => {
        val vertexName =
          getVertexName(vertexIndex)

        val vertexNameExists =
          graph.vertexes.exists(_.name == vertexName)

        vertexNameExists
      })
      .lastOption
      .getOrElse(firstIndex - 1)

    val vertexIndex =
      lastUsedVertexIndex + 1

    val vertexName =
      getVertexName(vertexIndex)

    val newVertex =
      instantiateVertex(center, vertexName)

    Some(
      graph.addVertex(newVertex)
    )
  }
}
