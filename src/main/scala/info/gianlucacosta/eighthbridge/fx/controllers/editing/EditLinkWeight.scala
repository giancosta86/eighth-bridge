package info.gianlucacosta.eighthbridge.fx.controllers.editing

import info.gianlucacosta.eighthbridge.fx.VisualLink
import info.gianlucacosta.eighthbridge.fx.controllers.primitive.EditLink
import info.gianlucacosta.eighthbridge.graphs.facets.HasWeightBoundedSettable
import info.gianlucacosta.helios.fx.dialogs.InputDialogs

//TODO! Editing a link weight should also update its label?!
trait EditLinkWeight
  extends EditLink {
  override type L <:
    VisualLink with HasWeightBoundedSettable

  override protected def doEditLink(link: L): Option[L] = {
    InputDialogs.askForDouble(
        message = "Weight:",
        initialValue = link.weight,
        minValue = link.demand,
        maxValue = link.capacity,
        header = "Edit link..."
      )
      .map(newWeight =>
        link.setWeight(newWeight)
      )
  }
}
