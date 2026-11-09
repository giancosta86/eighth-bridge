package info.gianlucacosta.eighthbridge.graphs.facets

trait HasStyleClassesSettable extends TimestampedPrototype with HasStyleClasses {
  private var _styleClasses: Set[String] =
    Set()

  def styleClasses: Set[String] =
    _styleClasses

  def setStyleClasses(styleClasses: Set[String]): this.type =
    if (styleClasses != _styleClasses) {
      val result =
        clone()

      result._styleClasses =
        styleClasses

      result
    } else
      this
}
