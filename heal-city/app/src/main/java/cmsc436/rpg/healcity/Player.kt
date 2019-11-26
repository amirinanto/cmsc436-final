package cmsc436.rpg.healcity

data class Player(val name: String,
                  var target: Int,
                  var level: Int = 1,
                  var exp: Int = 0,
                  var steps: Int = 0)