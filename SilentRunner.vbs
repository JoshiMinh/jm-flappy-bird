Set objShell = CreateObject("WScript.Shell")

objShell.Run "cmd /c cls", 0, True

If objShell.Run("javac -version", 0, True) = 0 Then
    objShell.Run "javac *.java", 0, True
End If

objShell.Run "java -cp ""sqljdbc42.jar;"" Launcher", 0, True

Set objShell = Nothing