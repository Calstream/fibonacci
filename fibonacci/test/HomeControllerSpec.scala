package controllers
import org.scalatest.concurrent.ScalaFutures
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
class HomeControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting with ScalaFutures {

  "HomeController" must {

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

    "reject a POST request when given bad input(less than minimum <<)" in {
      val controller = inject[HomeController]
      val request = FakeRequest(routes.HomeController.ShowSequence())
        .withFormUrlEncodedBody("length" -> "-7332582726")

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

    "reject a POST request when given bad input(greater than maximum >>)" in {
      val controller = inject[HomeController]
      val request = FakeRequest(routes.HomeController.ShowSequence())
        .withFormUrlEncodedBody("length" -> "7358924825")

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

    "reject a POST request when given bad input(empty)" in {
      val controller = inject[HomeController]
      val request = FakeRequest(routes.HomeController.ShowSequence())
        .withFormUrlEncodedBody("length" -> "")

      val futureResult: Future[Result] = controller.ShowSequence().apply(request)
      status(futureResult) must be(Status.BAD_REQUEST)
    }


      "process a POST request successfully(2)" in {
        // Pull the controller from the already running Play application, using Injecting
        val controller = inject[HomeController]
        val request = FakeRequest(routes.HomeController.ShowSequence())
          .withFormUrlEncodedBody("length" -> "2")
        val futureResult: Future[Result] = controller.ShowSequence().apply(request)
        whenReady(futureResult) { result =>
          result.header.headers(LOCATION) must equal(routes.HomeController.showInputForm().url)
        }
      }

    "process a POST request successfully(47)" in {
      // Pull the controller from the already running Play application, using Injecting
      val controller = inject[HomeController]
      val request = FakeRequest(routes.HomeController.ShowSequence())
        .withFormUrlEncodedBody("length" -> "47")
      val futureResult: Future[Result] = controller.ShowSequence().apply(request)
      whenReady(futureResult) { result =>
        result.header.headers(LOCATION) must equal(routes.HomeController.showInputForm().url)
      }
    }

    "process a POST request successfully(23)" in {
      // Pull the controller from the already running Play application, using Injecting
      val controller = inject[HomeController]
      val request = FakeRequest(routes.HomeController.ShowSequence())
        .withFormUrlEncodedBody("length" -> "23")
      val futureResult: Future[Result] = controller.ShowSequence().apply(request)
      whenReady(futureResult) { result =>
        result.header.headers(LOCATION) must equal(routes.HomeController.showInputForm().url)
      }
    }

    /*"show sequence with correct input" in {
      val controller = inject[HomeController]
      val request = FakeRequest(routes.HomeController.ShowSequence())
        .withFormUrlEncodedBody("length" -> "10")
      val futureResult: Future[Result] = controller.ShowSequence().apply(request)

      // And we can get the results out using Scalatest's "Futures" trait, which gives us whenReady
      whenReady(futureResult) { result =>
        result.body.toString must include("Fibonacci numbers:")
      }
    }*/
  }
}