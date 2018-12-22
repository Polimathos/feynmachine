package model

import com.ogmacorp.ogmaneo.Vec2i

object Configuration {
  val maxWordLength = 20
  val maxNumberOfWords = 30

  val architectSeed = 1234

  //Current layer prediction learning rate
  val in_p_alpha = 0.09f
  //Input field radius (onto hidden layers)
  val in_p_radius = 20

  //layers of the hierarchy
  val layerNumber = 7

  //encoder dimensions
  val encoderSize = 50

  val sfc_numSamples = 2
  //Size of a chunk
  val sfc_chunkSize = new Vec2i(6, 6)
  val sfc_ff_radius = 12

  //Number of steps to perform temporal pooling over, 1 means no pooling
  val hl_poolSteps = 1

  //Current layer prediction learning rate.
  val p_alpha = 0.075f

  //Feed back learning rate
  val p_beta = 0.075f

  //Input field radius (onto hidden layers).
  val p_radius = 20

  //Small boosting factor
  val sfc_gamma = 0.01f

  //Weight initialization range
  val sfc_initWeightRange:(Int,Int)=(-1,1)

  val conversationsFile = "/home/mgonzalo/Escritorio/research/feynman machine/src/main/resources/conversationsShort.txt"
}
