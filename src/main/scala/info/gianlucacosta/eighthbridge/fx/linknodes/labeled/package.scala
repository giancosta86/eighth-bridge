package info.gianlucacosta.eighthbridge.fx.linknodes

import info.gianlucacosta.eighthbridge.fx.graph.Delta
import scalafx.geometry.Point2D

package object labeled {
  type LabelDragHandler =
    (Delta) => Unit

  type LabelSecondaryDoubleClickHandler =
    () => Unit
}
