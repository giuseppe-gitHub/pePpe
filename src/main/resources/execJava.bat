 @echo off
 setLocal EnableDelayedExpansion
 set CLASSPATH=
 for %%a in (*.jar) do (
   set CLASSPATH=!CLASSPATH!;%%a
 )
 
 echo CLASSPATH: %CLASSPATH%
 echo.
 
 java  -classpath %CLASSPATH%  %1