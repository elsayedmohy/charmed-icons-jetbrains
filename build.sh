#!/bin/sh
# Builds build/charmed-icons.zip using the JDK and platform jars bundled with an installed JetBrains IDE.
set -e
cd "$(dirname "$0")"
IDE="${IDE:-$HOME/Applications/WebStorm.app/Contents}"
JDK="$IDE/jbr/Contents/Home/bin"
rm -rf build && mkdir -p build/classes build/charmed-icons/lib
"$JDK/javac" --release 17 -nowarn -cp "$IDE/lib/*" -d build/classes src/main/java/dev/charmed/icons/*.java
cp -R src/main/resources/ build/classes/
(cd build/classes && zip -qr ../charmed-icons/lib/charmed-icons.jar .)
(cd build && zip -qr charmed-icons.zip charmed-icons)
echo "Built $(pwd)/build/charmed-icons.zip"
