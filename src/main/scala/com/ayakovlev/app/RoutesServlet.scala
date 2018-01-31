package com.ayakovlev.app

import com.fasterxml.jackson.databind.{PropertyNamingStrategy, SerializationFeature}
import org.json4s.{DefaultFormats, Formats}
import org.scalatra._
import org.scalatra.json.JacksonJsonSupport

import scala.collection.mutable

class RoutesServlet extends ScalatraServlet with JacksonJsonSupport {

  var messages = Map(0 -> "Test0", 1 -> "Test1")

  protected implicit lazy val jsonFormats: Formats = DefaultFormats

  before() {
    contentType = formats("json")
  }

  post("/messages") {
    val message = ((parsedBody \ "id").extract[Int], (parsedBody \ "text").extract[String])
    if (!(messages contains message._1)) {
      messages += message
    } else
      Conflict("Message with id=" + (parsedBody \ "id").extract[Int] + " already exists")
  }

  case class Message(id:Int, text:String)

  get("/messages") {
    var messagesList = mutable.MutableList[Message]()
    messages foreach (m => messagesList += Message(m._1, m._2))
    messagesList
  }

  get("/messages/:id") {
    if (messages contains params("id").toInt)
      Message(params("id").toInt, messages(params("id").toInt))
    else
      NotFound("Message with id=" + params("id") + " not found")
  }

  put("/messages/:id") {
    if (messages contains params("id").toInt) {
      messages += (params("id").toInt -> (parsedBody \ "text").extract[String])
    } else
      NotFound("Message with id=" + params("id") + " not found")
  }

  delete("/messages/:id") {
    if (messages contains params("id").toInt)
      messages -= params("id").toInt
    else
      NotFound("Message with id=" + params("id") + " not found")
  }

  error{
    case e:org.json4s.MappingException => BadRequest(e.msg)
  }
}
