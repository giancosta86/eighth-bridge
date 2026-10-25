package info.gianlucacosta.eighthbridge.fx.arcnodes

import info.gianlucacosta.eighthbridge.fx.graph.Delta
import scalafx.geometry.Point2D

package object labeled {
  type LabelDragHandler =
    (Delta) => Unit

  type LabelDoubleClickhandler =
    () => Unit
}
