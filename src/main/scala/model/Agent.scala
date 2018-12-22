package model

import scala.io.StdIn.readLine
import scala.io.Source

class Agent {
  val encoderDecoder = new EncoderDecoder
  val feynmanMachine = new FeynmanMachine(encoderDecoder.maxMessageLength, encoderDecoder.chars.length)

  def processTrainingExamples(filePath:String): List[String] ={
    val candidate = Source.fromFile(filePath).getLines.toList
    if (candidate.length % 2 == 1){
      candidate.dropRight(1)
    } else {
      candidate
    }
  }

  def teachToRepeat(cycles:Int=100): Unit ={
    val filePath = Configuration.conversationsFile
    val lines = processTrainingExamples(filePath)
    for (iter <- 0 to cycles){
        for (examples <- lines.sliding(2,2)){
          val inputArray = encoderDecoder.encodeWordForWord(examples.head)
          val trainExampleArray = encoderDecoder.encodeWordForWord(examples.last)

          encoderDecoder.synchronizeInputArrays()
          agentRespondWordForWord(inputArray,trainExampleArray)
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
      }
      if (input.matches("!stop")) {
        running = false
      }
      training = feynmanMachine.trainingSwitch(input, training)
      val inputArray = encoderDecoder.encodeWordForWord(input)
      val trainExampleArray =
        if (training) {
          val trainExample = readLine("What should the bot say?: ")
          encoderDecoder.encodeWordForWord(trainExample)
        }
        else {
          encoderDecoder.encodeWordForWord(LastMachineResponse)
        }
      val responseTuple = agentRespondWordForWord(inputArray, trainExampleArray)
      val decodedTuple = encoderDecoder.decodeTupleWordForWord(responseTuple)
      printResponse(decodedTuple)
      LastMachineResponse = decodedTuple._2
    }
    feynmanMachine.saveHierarchy
  }

  def agentRespond(inputArray:Array[Double], trainExampleArray:Array[Double]):(Array[Double], Array[Double]) = {
    val (nextInputPredictionArray, machineResponseArray) = feynmanMachine.hierarchyStep(inputArray, trainExampleArray)
    (nextInputPredictionArray,machineResponseArray)
  }

  def printResponse(responseTuple:(String,String)): Unit ={
    println("Ava says : "+responseTuple._2)
    println("Ava predicts you'll say: "+responseTuple._1)
  }

  def agentRespondWordForWord(messageArrayTuple:(Array[Array[Double]],Array[Array[Double]])):(Array[Array[Double]], Array[Array[Double]]) ={
    val zippedArrays = messageArrayTuple._1.zip(messageArrayTuple._2)
    var nextInputPrediction = Array[Array[Double]]()
    var machineResponse = Array[Array[Double]]()
    for (tuple <- zippedArrays){
      val (nextInputWord, machineWord) = agentRespond(tuple._1, tuple._2)
      nextInputPrediction ++= Array(nextInputWord)
      machineResponse ++= Array(machineWord)
    }
    (nextInputPrediction,machineResponse)
  }
}
