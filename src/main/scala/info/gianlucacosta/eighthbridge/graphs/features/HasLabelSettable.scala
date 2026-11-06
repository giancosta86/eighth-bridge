package info.gianlucacosta.eighthbridge.graphs.features

trait HasLabelSettable extends HasTimestampSettable with HasLabel {
  protected val initialLabel: String =
    ""

  private var _label: String =
    initialLabel

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
