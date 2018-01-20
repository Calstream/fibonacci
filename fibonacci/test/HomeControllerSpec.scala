package controllers

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._
import play.api.http.Status
import org.scalatest.concurrent.ScalaFutures
import scala.concurrent.Future
import org.scalatestplus.play.PlaySpec
import play.api.http.Status
import play.api.mvc._
import play.api.test.Helpers._
import play.api.test._
/**
  * Add your spec here.
  * You can mock out a whole application including requests, plugins etc.
  *
  * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
  */
class HomeControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  private val sequence = scala.collection.mutable.ArrayBuffer[Int]()
  private val postUrl = routes.HomeController.createWidget()
  "HomeController" should {

    "render the index page from the application(GET)" in {
      val controller = inject[HomeController]
      val home = controller.listWidgets().apply(FakeRequest(GET, "/"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Fibonacci")
    }

    "render the index page from the router(GET)" in {
      val request = FakeRequest(GET, "/")
      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Fibonacci")
    }

/*    "process a POST request successfully" in {
      // Pull the controller from the already running Play application, using Injecting
      val controller = inject[HomeController]

      // Call using the FakeRequest and the correct body information and CSRF token
      val request = FakeRequest(routes.HomeController.createWidget())
        .withFormUrlEncodedBody("length" -> "17")
        //.withCSRFToken
      val futureResult: Future[Result] = controller.createWidget().apply(request)

      // And we can get the results out using Scalatest's "Futures" trait, which gives us whenReady
      whenReady(futureResult) { result =>
        result.header.headers(LOCATION) must equal(routes.WidgetController.listWidgets().url)
      }
    }
*/
    "reject a POST request when given bad input" in {
      val controller = inject[HomeController]

      // Call the controller with negative price...
      val request = FakeRequest(routes.HomeController.createWidget())
        .withFormUrlEncodedBody("length" -> "0")

      val futureResult: Future[Result] = controller.createWidget().apply(request)

      status(futureResult) must be(Status.BAD_REQUEST)
    }

  }
}