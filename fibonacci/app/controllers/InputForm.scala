package controllers

object InputForm {
  import play.api.data.Form
  import play.api.data.Forms._

  case class Data(length: Int)


  val inputform = Form(
    mapping(
      "length" -> number(min = 2, max = 47)
    )(Data.apply)(Data.unapply)
  )
}

