package info.gianlucacosta.eighthbridge.fx.arcnodes.plain

import info.gianlucacosta.eighthbridge.fx.Centered
import info.gianlucacosta.eighthbridge.fx.graph.ArcHandle
import scalafx.scene.shape.Ellipse
import scalafx.Includes._

private class HandleNode(initialHandle: ArcHandle) extends Ellipse with Centered {
  styleClass.add("handle")

  private var _handle: ArcHandle =
    initialHandle

  def handle: ArcHandle =
    _handle

  def update(updatedHandle: ArcHandle): Unit = {
    _handle =
      updatedHandle

    radiusX() =
      updatedHandle.dimension.width / 2

    radiusY() =
      updatedHandle.dimension.height / 2

    centerX() =
      updatedHandle.center.x

    centerY() =
      updatedHandle.center.y
  }
}
