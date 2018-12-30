
CLASSPATH=

echo $CLASSPATH

for currDir in $( find . -type d); do
	CLASSPATH="$CLASSPATH;$currDir"
done

for currJar in $( find . -name "*.jar"); do
	CLASSPATH="$CLASSPATH;$currJar"
done

echo "CLASSPATH: $CLASSPATH"

java  -cp $CLASSPATH  $1