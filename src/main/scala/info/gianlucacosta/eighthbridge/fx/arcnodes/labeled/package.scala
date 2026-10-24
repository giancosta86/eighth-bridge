package info.gianlucacosta.eighthbridge.fx.arcnodes

import scalafx.geometry.Point2D

package object labeled {
  type LabelDragHandler =
    (Point2D) => Unit

  type LabelDoubleClickhandler =
    () => Unit
}
