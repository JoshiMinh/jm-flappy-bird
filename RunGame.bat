@echo off
cls
javac Launcher.java FlappyBird.java AddScore.java ScoreBoard.java
java -cp "sqljdbc42.jar;" Launcher
exit