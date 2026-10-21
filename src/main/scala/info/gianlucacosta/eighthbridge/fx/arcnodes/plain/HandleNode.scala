package info.gianlucacosta.eighthbridge.fx.arcnodes.plain

import info.gianlucacosta.eighthbridge.fx.graph.ArcHandle
import scalafx.scene.shape.Ellipse
import scalafx.Includes._

class HandleNode(initialArcHandle: ArcHandle) extends Ellipse {
  styleClass.add("handle")

  private var arcHandle: ArcHandle =
    initialArcHandle

  private[plain] def update(updatedArcHandle: ArcHandle): Unit = {
    arcHandle =
      updatedArcHandle

    radiusX() =
      updatedArcHandle.dimension.width / 2

    radiusY() =
      updatedArcHandle.dimension.height / 2

    centerX() =
      updatedArcHandle.center.x

    centerY() =
      updatedArcHandle.center.y
  }
}
