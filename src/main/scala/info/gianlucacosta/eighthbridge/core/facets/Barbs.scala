package info.gianlucacosta.eighthbridge.core.facets

case class Barbs(
                     angleInDegrees: Double,
                     length: Double
                   ) {
  require(0 < angleInDegrees && angleInDegrees < 90)

  require(length > 0)
}

object Barbs {
  val Default = Barbs(
    angleInDegrees = 30,
    length = 15
  )
}
