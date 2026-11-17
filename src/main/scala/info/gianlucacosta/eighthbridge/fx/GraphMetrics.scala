package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.graphs.facets.{HasHandles, LinkHandle}
import scalafx.geometry.Point2D
import scalafx.Includes._

private object GraphMetrics {
  private def getRightBottomPoint[T](
                              items: Iterable[T],
                              rightRetriever: (T) => Double,
                              bottomRetriever: (T) => Double
                            ): Point2D = {
    if (items.nonEmpty) {
      val maxRight: Double =
        items
          .map(rightRetriever)
          .max

      val maxBottom =
        items
          .map(bottomRetriever)
          .max

      new Point2D(
        maxRight,
        maxBottom
      )
    } else
      new Point2D(0, 0)
  }

  def getVertexesRightBottomPoint(graphCanvas: GraphCanvas): Point2D = {
    val vertexNodes =
      graphCanvas
        .vertexNodesByVertex
        .values

    getRightBottomPoint[VertexNode](
      items =
        vertexNodes,
      rightRetriever =
        vertexNode =>
          vertexNode.vertex().center.x + vertexNode.width() / 2,
      bottomRetriever =
          vertexNode =>
            vertexNode.vertex().center.y + vertexNode.height() / 2
    )
  }

  def getLinkHandlesRightBottomPoint(graphCanvas: GraphCanvas): Point2D = {
    val handles =
      graphCanvas
        .linkNodesByLink
        .values
        .map(_.link())
        .collect {
          case withHandles: HasHandles =>
            withHandles.handles
        }
        .flatten

    getRightBottomPoint[LinkHandle](
      items =
        handles,
      rightRetriever =
        handle =>
          handle.center.x + handle.dimension.width / 2,
      bottomRetriever =
        handle =>
          handle.center.y + handle.dimension.height / 2
    )
  }
}
