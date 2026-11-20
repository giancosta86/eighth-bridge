package info.gianlucacosta.eighthbridge.fx.controllers.creation

import info.gianlucacosta.eighthbridge.fx.{GraphCanvasController, VisualVertex}
import info.gianlucacosta.eighthbridge.core.facets.HasName
import scalafx.geometry.Point2D

trait CreateVertexWithName
  extends GraphCanvasController {
  /**
   * The first index used when creating vertexes
   */
  protected val firstNameIndex =
    1

  override def addVertex(center: Point2D): Unit = {
    val existingNames: Set[String] =
      graph
        .vertexes
        .collect {
          case vertexWithName: VisualVertex with HasName =>
            vertexWithName.name
        }

    var currentIndex =
      firstNameIndex

    while (true) {
      val currentName =
        getVertexName(currentIndex)

      if (!existingNames.contains(currentName)) {
        val newVertex =
          createNamedVertex(center, currentName)

        graph =
          graph.addVertex(
            newVertex
          )

        return
      }

      currentIndex +=
        1
    }
  }

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
  protected def createNamedVertex(center: Point2D, vertexName: String): VisualVertex with HasName
}
