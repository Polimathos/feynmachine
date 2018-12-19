package model

import scala.io.StdIn.readLine
import scala.io.Source

class Agent {
  val encoderDecoder = new EncoderDecoder
  val feynmanMachine = new FeynmanMachine(encoderDecoder.maxMessageLength, encoderDecoder.chars.length)

  def teachToRepeat(cycles:Int=100): Unit ={
    val filePath = Configuration.conversationsFile
    val lines = {
      val candidate = Source.fromFile(filePath).getLines.toList
      if (candidate.length % 2 == 1){
        candidate.dropRight(1)
      } else {
        candidate
      }
    }
    for (iter <- 0 to cycles){
        for (examples <- lines.sliding(2,2)){
          println(examples)
          val inputArray = encoderDecoder.encode(examples.head)
          val trainExampleArray = encoderDecoder.encode(examples.last)
          agentRespond(inputArray,trainExampleArray)
        }
    }
    feynmanMachine.saveHierarchy
  }

  def interactiveLearning() = {
    var running = true
    var training = true
    var LastMachineResponse = ""

    while (running) {
      val input = readLine("your message:")

      if (input.matches("!save")) {
        feynmanMachine.saveHierarchy
        val input = readLine("your message:")
      }
      if (input.matches("!stop")) {
        running = false
      }

      training = feynmanMachine.trainingSwitch(input, training)

      val inputArray = encoderDecoder.encode(input)

      val trainExampleArray =
        if (training) {
          val trainExample = readLine("What should the bot say?: ")
          encoderDecoder.encode(trainExample)
        }
        else {
          encoderDecoder.encode(LastMachineResponse)
        }
      LastMachineResponse = agentRespond(inputArray, trainExampleArray)
    }
    feynmanMachine.saveHierarchy
  }

  def agentRespond(inputArray:Array[Double], trainExampleArray:Array[Double]):String = {
    val (nextInputPredictionArray, machineResponseArray) = feynmanMachine.hierarchyStep(inputArray, trainExampleArray)
    val nextInputPrediction = encoderDecoder.decode(nextInputPredictionArray)
    val machineResponse = encoderDecoder.decode(machineResponseArray)
    println("bot says: " + nextInputPrediction)
    println("Bot predicts you'll say: " + machineResponse)
    machineResponse
  }

}
