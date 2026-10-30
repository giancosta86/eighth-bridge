package info.gianlucacosta.eighthbridge.fx.controller.advanced

import info.gianlucacosta.eighthbridge.fx.GraphCanvasController
import info.gianlucacosta.eighthbridge.fx.graph.VisualVertex
import info.gianlucacosta.eighthbridge.graphs.features.Named
import scalafx.geometry.Point2D

trait CreateNamedVertex
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

  override def createVertex(center: Point2D): Unit = {
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

    graph =
      graph.addVertex(
        newVertex
      )
  }

  /**
   * Actually instantiate the vertex
   *
   * @param center
   * @param vertexName
   * @return
   */
  protected def createNamedVertex(center: Point2D, vertexName: String): VisualVertex with Named
}
