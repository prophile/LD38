#!/bin/bash
set -e
mkdir -p distributions/final
mkdir -p distributions/windows
mkdir -p distributions/mac
java -jar jres/packr.jar \
     --platform windows32 \
     --jdk 'jres/win32.zip' \
     --executable snowglobe \
     --classpath desktop/build/libs/desktop-1.0.jar \
     --mainclass uk.co.alynn.games.snowglobe.desktop.DesktopLauncher \
     --minimizejre hard \
     --output distributions/windows/snowglobe
java -jar jres/packr.jar \
     --platform mac \
     --jdk 'jres/mac64.zip' \
     --executable Snowglobe \
     --classpath desktop/build/libs/desktop-1.0.jar \
     --mainclass uk.co.alynn.games.snowglobe.desktop.DesktopLauncher \
     --minimizejre hard \
     --output distributions/mac/Snowglobe.app
pushd distributions/windows
zip -r ../final/snowglobe-win32.zip snowglobe
popd
pushd distributions/mac
zip -r ../final/snowglobe-mac.zip Snowglobe.app
popd
