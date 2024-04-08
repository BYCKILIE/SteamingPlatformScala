package controllers

import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.FakeRequest
import play.api.test.Helpers._

import org.scalatestplus.play._
import play.api.test._
import play.api.test.Helpers._
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers.any
import org.mockito.stubbing.OngoingStubbing

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

//import controllers.UsersController

class UserControllerSpec extends PlaySpec with GuiceOneAppPerTest {

  // Mocking the UserService
  val userServiceMock: services.UsersService = mock(classOf[services.UsersService])

  // Creating an instance of the controller with the mocked service
  val controller = new UsersController(stubControllerComponents(), userServiceMock)(global)

  "UsersController" should {
//    "respond with user JSON when user found" in {
//      // Define behavior of userServiceMock
//      when(userServiceMock.readUser(1)).thenReturn(Future.successful(User(1, "Test User")))
//
//      // Make a fake request to the controller
//      val result = controller.readUser(1)(FakeRequest(GET, "/"))
//
//      // Validate the response
//      status(result) mustEqual OK
//      contentType(result) mustBe Some("application/json")
//      contentAsString(result) must include("Test User")
//    }

    "respond with 404 when user not found" in {
      // Define behavior of userServiceMock for user not found scenario
      when(userServiceMock.readUser(2)).thenReturn(Future.failed(new NoSuchElementException()))

      // Make a fake request to the controller
      val result = controller.readUser(2)(FakeRequest(GET, "/"))

      // Validate the response
      status(result) mustEqual NOT_FOUND
      contentAsString(result) must include("User not found with id: 2")
    }

    "respond with 500 when an error occurs" in {
      // Define behavior of userServiceMock for generic error scenario
      when(userServiceMock.readUser(3)).thenReturn(Future.failed(new RuntimeException("Something went wrong")))

      // Make a fake request to the controller
      val result = controller.readUser(3)(FakeRequest(GET, "/"))

      // Validate the response
      status(result) mustEqual INTERNAL_SERVER_ERROR
      contentAsString(result) must include("An error occurred: Something went wrong")
    }
  }
}
