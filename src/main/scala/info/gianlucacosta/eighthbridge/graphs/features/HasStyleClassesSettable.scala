package info.gianlucacosta.eighthbridge.graphs.features

trait HasStyleClassesSettable extends HasTimestampSettable with HasStyleClasses {
  protected val initialStyleClasses: Set[String] =
    Set()

  private var _styleClasses: Set[String] =
    initialStyleClasses

  def styleClasses: Set[String] =
    _styleClasses

  def setStyleClasses(styleClasses: Set[String]): this.type =
    if (styleClasses != _styleClasses) {
      val result =
        updateTimestamp()

      result._styleClasses =
        styleClasses

      result
    } else
      this
}
