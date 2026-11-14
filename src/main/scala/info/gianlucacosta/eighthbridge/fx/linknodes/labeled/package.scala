package info.gianlucacosta.eighthbridge.fx.linknodes

import info.gianlucacosta.eighthbridge.fx.graph.Delta

package object labeled {
  type LabelDragHandler =
    (Delta) => Unit

  type LabelSecondaryDoubleClickHandler =
    () => Unit
}
