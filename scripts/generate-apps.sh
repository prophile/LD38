#!/bin/bash
set -e
mkdir -p distributions/final
mkdir -p distributions/windows
mkdir -p distributions/mac
java -jar jres/packr.jar \
     --platform windows32 \
     --jdk 'jres/win32.zip' \
     --executable arcticmemories \
     --classpath desktop/build/libs/desktop-1.0.jar \
     --mainclass uk.co.alynn.games.snowglobe.desktop.DesktopLauncher \
     --minimizejre hard \
     --output distributions/windows/arcticmemories
java -jar jres/packr.jar \
     --platform mac \
     --jdk 'jres/mac64.zip' \
     --executable ArcticMemories \
     --classpath desktop/build/libs/desktop-1.0.jar \
     --mainclass uk.co.alynn.games.snowglobe.desktop.DesktopLauncher \
     --minimizejre hard \
     --output distributions/mac/ArcticMemories.app
pushd distributions/windows
zip -r ../final/arcticmemories-win32.zip arcticmemories
popd
pushd distributions/mac
zip -r ../final/arcticmemories-mac.zip ArcticMemories.app
popd
