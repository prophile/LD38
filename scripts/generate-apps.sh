#!/bin/bash
set -e
mkdir -p distributions/final
mkdir -p distributions/windows
mkdir -p distributions/mac
java -jar jres/packr.jar \
     -platform windows \
     -jdk 'jres/win32.zip' \
     -executable snowglobe \
     -appjar desktop/build/libs/desktop-1.0.jar \
     -mainclass uk/co/alynn/games/snowglobe/desktop/DesktopLauncher \
     -minimizejre hard \
     -outdir distributions/windows/snowglobe
java -jar jres/packr.jar \
     -platform mac \
     -jdk 'jres/mac64.zip' \
     -executable Snowglobe \
     -appjar desktop/build/libs/desktop-1.0.jar \
     -mainclass uk/co/alynn/games/snowglobe/desktop/DesktopLauncher \
     -minimizejre hard \
     -outdir distributions/mac/Snowglobe.app
pushd distributions/windows
zip -r ../final/snowglobe-win32.zip snowglobe
popd
pushd distributions/mac
zip -r ../final/snowglobe-mac.zip Snowglobe.app
popd
