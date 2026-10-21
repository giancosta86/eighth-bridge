package info.gianlucacosta.eighthbridge.fx.arcnodes.plain

import info.gianlucacosta.eighthbridge.fx.{Centered, VertexNode}
import scalafx.geometry.Point2D
import scalafx.scene.shape.Line

private class ShaftSegment(start: Centered, end: Centered) extends Line {
  styleClass.add("line")

  startX <==
    start.centerX

  startY <==
    start.centerY

  endX <==
    end.centerX

  endY <==
    end.centerY
}
