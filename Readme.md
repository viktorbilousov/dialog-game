# Dialog-game

This is a demo game, that contains only from dialogs. 
It's created for testing und using `dialog-system` and `dialog-creator`.

For initialisation of dialogs are using scripts file in `./script/`. 

## How to run 

### Gradle

```bash
./gradlew buildDialogAndWorldRouter  // only first start 
./gradlew build 
```

### Game runnner
```kotlin
    val game = Game() // create game object 
    
    Loader(game).load( // load phrases from file 
        "phrases-folder",
        "routers-folder",
        "graphs-folder"
    )
    
    Tester.testGame(game)  // test loaded game     
    val gameRunner = Runner(game, game.world!!) // create runner
    gameRunner.run() // run game
```


## Package `game`

* `Game` - main object
* `GameData` - included all data for Game Work
* `Loader` - create dialogs and phrases from file in a game 
* `Runner` - standard runner for game
* `Tester` - test game data for errors

## Phrases 

The Game uses own `Phrases`, that implement `FilteredPhrase`: 

* `dialog.game.phrases.AutoPhrase` - auto choosing of first `Answer` in a `Phrase`
* `dialog.game.phrases.RandomGamePhrase` - randomly chooses and print one from many textPhrase from a Phrase and 
*  `dialog.game.phrases.AutoPhrase` - implement different logic in dialogs using `PhraseFilter`. Used as default `Phrase` by creating from Script files.
  
  
  
## Phrase Filters

Phrase Filters using for making some action with current Phrase or Game Data. 
Each `PhraseFilter` can be identified in a text by `FilterLabels`, which could be written 
in a text of a `Phrase` or an `Answer`. To use labels, it must be be enclosed in square brackets: 

`some text [label1]  other text`



### Example of using Phrase Filter 

In these phrases used: 
* `SetBooleanFilter` for init and set true boolean value in a `DataGame`  [SET=nameOfvalue]
* `GetBooleanFilter` to read it [GET=nameOfValue]

In the first phrase user could choose one of these answers: 
 * `answer 1`,  this is equals (val ph1 = true). 
 * `answer 2`, variable ph2=true

Depending on users choice, `phrase 2` print different text of phrase.  

```
//phrase 1 
// default class `dialog.game.phrases.AutoPhrase` 
---- phrase1

Text of phrase 1

> [SET=ph1] answer 1 (phrase.2)
> [SET=ph2] answer 2 (phrase.2)

//phrase 2 
---- phrase2

@ [GET=ph1] Print this text, if game variable `ph1` == true 
@ [GET=ph2] Print this text, if game variable `ph2` == true 

>  answer (end #EXIT)
```  

## Create and add new Filter

There are many interfaces for different using: 

* PhraseFilter
    * InlinePhraseFilter
        * InlineTextPhraseFilter 
    * InlineChangePhraseFilter
        * InlineChangeTextPhraseFilter 





## Minigames



To enable 

## Filtered Phrases 
