package info.gianlucacosta.eighthbridge.graphs.facets

trait HasLabelSettable extends HasTimestampSettable with HasLabel {
  private var _label: String =
    ""

  override def label: String =
    _label

  def setLabel(label: String): this.type =
    if (label != _label) {
      val result =
        updateTimestamp()

      result._label =
        label

      result
    } else
      this
}
