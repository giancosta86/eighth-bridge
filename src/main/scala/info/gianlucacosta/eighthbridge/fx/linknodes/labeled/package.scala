package info.gianlucacosta.eighthbridge.fx.linknodes

import info.gianlucacosta.eighthbridge.fx.Delta2D

package object labeled {
  type LabelDragHandler =
    (Delta2D) => Unit

  type LabelSecondaryDoubleClickHandler =
    () => Unit
}
