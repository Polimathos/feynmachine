package body

import org.apache.spark._
import org.apache.spark.streaming._
import com.ogmacorp.ogmaneo._
import java.io._
import model.{FeynmanMachine,EncoderDecoder, Agent}

import scala.io.StdIn.readLine

object Main extends App {
  val input = readLine("please choose between interactive learning [0] or learning to repeat[1]: ")
  val learner = new Agent
  input.toInt match {
    case 0 => learner.interactiveLearning
    case 1 => {
      val iterations = readLine("determine a number of iterations: ").toInt
      learner.teachToRepeat(iterations)
    }
    case 2 => learner.checkEncoderDecoder
    case _ => println("not one of the available options")
  }
}