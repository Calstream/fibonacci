import org.scalatestplus.play._
import play.api.mvc.Request
import controllers.InputForm
import play.api.data.FormError
import play.api.i18n._
import play.api.test._
class tests extends PlaySpec{

  "inputForm" must {

    "apply successfully from request" in {
      // The easiest way to test a form is by passing it a fake request.
      val call = controllers.routes.HomeController.ShowSequence()
      implicit val request: Request[_] = FakeRequest(call).withFormUrlEncodedBody("length" -> "37")
      // A successful binding using an implicit request will give you a form with a value.
      val boundForm = InputForm.inputform.bindFromRequest()
      // You can then get the widget data out and test it.
      val data = boundForm.value.get
      data.length must equal(37)
    }

    "apply successfully from map" in {
      // You can also bind directy from a map, if you don't have a request handy.
      val inp = Map("length" -> "12")
      // A successful binding will give you a form with a value.
      val boundForm = InputForm.inputform.bind(inp)
      // You can then get the widget data out and test it.
      val data = boundForm.value.get
      data.length must equal(12)
    }

    "show errors when applied unsuccessfully(-3)" in {
      val inp = Map("length" -> "-3")
      val errorForm = InputForm.inputform.bind(inp)
      val listOfErrors = errorForm.errors
      val formError: FormError = listOfErrors.head
      formError.key must equal("length")
      errorForm.hasGlobalErrors mustBe false
      formError.message must equal("error.min")
      val lang: Lang = Lang.defaultLang
      val messagesApi: MessagesApi = new DefaultMessagesApi(Map(lang.code -> Map("error.min" -> "Must be greater or equal to {0}")))
      val messagesProvider: MessagesProvider = messagesApi.preferred(Seq(lang))
      val message: String = Messages(formError.message, formError.args: _*)(messagesProvider)
      message must equal("Must be greater or equal to 2")
    }

    "show errors when applied unsuccessfully(1)" in {
      val inp = Map("length" -> "1")
      val errorForm = InputForm.inputform.bind(inp)
      val listOfErrors = errorForm.errors
      val formError: FormError = listOfErrors.head
      formError.key must equal("length")
      errorForm.hasGlobalErrors mustBe false
      formError.message must equal("error.min")
      val lang: Lang = Lang.defaultLang
      val messagesApi: MessagesApi = new DefaultMessagesApi(Map(lang.code -> Map("error.min" -> "Must be greater or equal to {0}")))
      val messagesProvider: MessagesProvider = messagesApi.preferred(Seq(lang))
      val message: String = Messages(formError.message, formError.args: _*)(messagesProvider)
      message must equal("Must be greater or equal to 2")
    }

    "show errors when applied unsuccessfully(48)" in {
      val inp = Map("length" -> "48")
      val errorForm = InputForm.inputform.bind(inp)
      val listOfErrors = errorForm.errors
      val formError: FormError = listOfErrors.head
      formError.key must equal("length")
      errorForm.hasGlobalErrors mustBe false
      formError.message must equal("error.max")
      val lang: Lang = Lang.defaultLang
      val messagesApi: MessagesApi = new DefaultMessagesApi(Map(lang.code -> Map("error.max" -> "Must be less or equal to {0}")))
      val messagesProvider: MessagesProvider = messagesApi.preferred(Seq(lang))
      val message: String = Messages(formError.message, formError.args: _*)(messagesProvider)
      message must equal("Must be less or equal to 47")
    }

    "show errors when applied unsuccessfully(not a number)" in {
      val inp = Map("length" -> "text")
      val errorForm = InputForm.inputform.bind(inp)
      val listOfErrors = errorForm.errors
      val formError: FormError = listOfErrors.head
      formError.key must equal("length")
      errorForm.hasGlobalErrors mustBe false
      formError.message must equal("error.number")
      val lang: Lang = Lang.defaultLang
      val messagesApi: MessagesApi = new DefaultMessagesApi(Map(lang.code -> Map("error.number" -> "Numeric value expected")))
      val messagesProvider: MessagesProvider = messagesApi.preferred(Seq(lang))
      val message: String = Messages(formError.message, formError.args: _*)(messagesProvider)
      message must equal("Numeric value expected")
    }


  }
}
