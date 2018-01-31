package com.ayakovlev.app

import org.scalatra.test.scalatest._

class RoutesServletTests extends ScalatraFunSuite {

  addServlet(classOf[RoutesServlet], "/*")

  test("GET / on RoutesServlet should return status 200"){
    get("/"){
      status should equal (200)
    }
  }

}
