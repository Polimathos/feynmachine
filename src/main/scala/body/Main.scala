package body

import org.apache.spark._
import org.apache.spark.streaming._
import com.ogmacorp.ogmaneo._
import java.io._
import model.{FeynmanMachine,EncoderDecoder}

import scala.io.StdIn.readLine

object Main extends App {
  val encoderDecoder = new EncoderDecoder
  val feynmanMachine = new FeynmanMachine(encoderDecoder.maxMessageLength,encoderDecoder.chars.length)

  var running = true
  var training = true
  var LastMachineResponse = ""

  while (running){
    val input = readLine("your message:")

    if (input.matches("!stop")){
      running = false
    }
    training = feynmanMachine.trainingSwitch(input,training)

    val inputArray = encoderDecoder.encode(input)

    val trainExampleArray =
    if (training){
      val trainExample =  readLine("What should the bot say?: ")
      encoderDecoder.encode(trainExample)
    }
    else {
      encoderDecoder.encode(LastMachineResponse)
    }

    val (predOut, predIn) = feynmanMachine.hierarchyStep(inputArray,trainExampleArray)
    val messageOut = encoderDecoder.decode(predOut)
    val messageIn = encoderDecoder.decode(predIn)
    println("bot says: "+ messageIn)
    println("Bot predicts you'll say: "+ messageOut)
  }
}