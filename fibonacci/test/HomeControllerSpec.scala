package controllers

import org.scalatestplus.play.guice._
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

  "HomeController" should {

    "render the index page from the application(GET)" in {
      val controller = inject[HomeController]
      val home = controller.showInputForm().apply(FakeRequest(GET, "/"))

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

    "reject a POST request when given bad input(less than minimum)" in {
      val controller = inject[HomeController]
      val request = FakeRequest(routes.HomeController.ShowSequence())
        .withFormUrlEncodedBody("length" -> "0")

      val futureResult: Future[Result] = controller.ShowSequence().apply(request)

      status(futureResult) must be(Status.BAD_REQUEST)
    }

    "reject a POST request when given bad input(greater than maximum)" in {
      val controller = inject[HomeController]
      val request = FakeRequest(routes.HomeController.ShowSequence())
        .withFormUrlEncodedBody("length" -> "73")

      val futureResult: Future[Result] = controller.ShowSequence().apply(request)

      status(futureResult) must be(Status.BAD_REQUEST)
    }

    "reject a POST request when given bad input(not a number)" in {
      val controller = inject[HomeController]
      val request = FakeRequest(routes.HomeController.ShowSequence())
        .withFormUrlEncodedBody("length" -> "-0ef")

      val futureResult: Future[Result] = controller.ShowSequence().apply(request)

      status(futureResult) must be(Status.BAD_REQUEST)
    }

  }
}