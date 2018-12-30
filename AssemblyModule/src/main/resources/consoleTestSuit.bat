 @echo off
 setLocal EnableDelayedExpansion
 set CLASSPATH=

 for /D /R %%a in (*) do (
   set CLASSPATH=!CLASSPATH!;%%a
 )
 
 for /R %%a in (*.jar) do (
   set CLASSPATH=!CLASSPATH!;%%a
 )
 
 echo CLASSPATH: %CLASSPATH%
 echo.
 
 java  -cp %CLASSPATH%  it.gius.pePpe.testSuit.TestSuitConsoleMain

 pause