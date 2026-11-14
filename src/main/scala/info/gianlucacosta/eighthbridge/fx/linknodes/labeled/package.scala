package info.gianlucacosta.eighthbridge.fx.linknodes

import info.gianlucacosta.eighthbridge.fx.Delta

package object labeled {
  type LabelDragHandler =
    (Delta) => Unit

  type LabelSecondaryDoubleClickHandler =
    () => Unit
}
