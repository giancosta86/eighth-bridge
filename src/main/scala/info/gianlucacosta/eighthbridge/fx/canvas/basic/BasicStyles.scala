package info.gianlucacosta.eighthbridge.fx.canvas.basic

/**
  * Object providing the default CSS stylesheet designed for the "basic"
  * renderers: you can employ it as a starting point in your app by adding it to
  * the scene's stylesheet list (via scene.getStylesheets().add()) or as
  * required by your GUI framework.
  */
object BasicStyles {
  val resourceUrl = getClass.getResource("BasicStyles.css")
}
