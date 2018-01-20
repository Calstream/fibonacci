package controllers

import javax.inject._


import play.api._
import play.api.data.Form
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */

class HomeController @Inject()(cc: MessagesControllerComponents) extends MessagesAbstractController(cc) {
  import InputForm._
  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  private val sequence = scala.collection.mutable.ArrayBuffer[Int]()
  private val postUrl = routes.HomeController.createWidget()
  private val debug_message = "ERROR"

  def index = Action {
    Ok(views.html.index())
  }

  def listWidgets = Action { implicit request: MessagesRequest[AnyContent] =>
    // Pass an unpopulated form to the template
    Ok(views.html.showSequence(sequence, inputform, postUrl))
  }
  def createWidget = Action { implicit request: MessagesRequest[AnyContent] =>
    val errorFunction = { formWithErrors: Form[Data]=>
      // This is the bad case, where the form had validation errors.
      // Let's show the user the form again, with the errors highlighted.
      // Note how we pass the form with errors to the template.
      val empty = scala.collection.mutable.ArrayBuffer()
      BadRequest(views.html.showSequence(empty, formWithErrors, postUrl))
    }

    val successFunction = { data: Data =>
      // This is the good case, where the form was successfully parsed as a Data object.
      sequence.clear()
      sequence.append(0)
      sequence.append(1)
      var index = 2
      while(index < data.length  ) {
        val c = sequence(index-2) + sequence(index-1)
        sequence.append(c)
        index += 1
      }
      Redirect(routes.HomeController.listWidgets()).flashing("" -> "")
    }

    val formValidationResult = inputform.bindFromRequest
    formValidationResult.fold(errorFunction, successFunction)
  }
}

