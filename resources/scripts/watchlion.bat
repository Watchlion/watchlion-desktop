echo off
echo ### 
echo ### WARNING: 'mvn clean install' needed to avoid this script (Watchlion .jar would be missing elsewhere)
echo ### 
cd ../..
java -jar target/watchlion-0.0.1-SNAPSHOT-executable.jar -reference src/main/resources/watchlion.reference.json
pause