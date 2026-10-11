package info.gianlucacosta.eighthbridge.fx.controller

import info.gianlucacosta.eighthbridge.fx.graph.{AnyVisualGraph, VisualGraph, VisualLink, VisualVertex}
import info.gianlucacosta.eighthbridge.fx.GraphCanvasController
import info.gianlucacosta.eighthbridge.theory.features.Named
import scalafx.geometry.Point2D

trait VertexNaming
  extends GraphCanvasController {
  /**
   * The first index used when creating vertexes
   */
  protected val firstNameIndex =
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
  protected def createNamedVertex(center: Point2D, vertexName: String): VisualVertex with Named

  override def createVertex(center: Point2D): Option[AnyVisualGraph] = {
    val lastUsedVertexIndex = Stream.from(firstNameIndex)
      .takeWhile(vertexIndex => {
        val vertexName =
          getVertexName(vertexIndex)

        val vertexNameExists =
          graph.vertexes.exists(_.asInstanceOf[Named].name == vertexName)

        vertexNameExists
      })
      .lastOption
      .getOrElse(firstNameIndex - 1)

    val vertexIndex =
      lastUsedVertexIndex + 1

    val vertexName =
      getVertexName(vertexIndex)

    val newVertex =
      createNamedVertex(center, vertexName)

    Some(
      graph.addVertex(newVertex)
    )
  }
}
