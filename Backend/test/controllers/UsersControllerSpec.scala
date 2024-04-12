package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._

class UsersControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "UsersController" should {

    "create a user" in {
      val request = FakeRequest(POST, "/api/user-create").withJsonBody(Json.obj("id" -> 0, "firstName" -> "Test", "lastName" -> "User", "birthDate" -> "2002-02-01", "gender" -> 1))
      val result = route(app, request).get

      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include ("User created successfully")
    }

    "update an existing user" in {
      val userId = 8
      val request = FakeRequest(POST, s"/api/user-update/$userId").withJsonBody(Json.obj("id" -> 0, "firstName" -> "Best", "lastName" -> "User", "birthDate" -> "2002-02-01", "gender" -> 1))
      val result = route(app, request).get

      status(result) mustBe OK
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) must include ("User updated successfully")
    }

    "read a user" in {
      val userId = 8
      val request = FakeRequest(GET, s"/api/user-read/$userId")
      val result = route(app, request).get

      status(result) mustBe OK
      contentType(result) mustBe Some("text/plain")
    }

    "delete a user" in {
      val userId = 8
      val request = FakeRequest(GET, s"/api/user-delete/$userId")
      val result = route(app, request).get

      status(result) mustBe OK
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) must include ("User deleted successfully")
    }

    // Similarly, add tests for update and delete actions

  }
}
