# Charmed Icons for JetBrains IDEs

Port of the [Charmed Extended](https://github.com/iotacb/charmed-extended) VS Code icon theme
(based on [littensy/charmed-icons](https://github.com/littensy/charmed-icons), MIT) to WebStorm, Rider
and every other JetBrains IDE (2024.2+).

## Install
1. `./build.sh` → `build/charmed-icons.zip`
2. IDE → Settings → Plugins → ⚙ → **Install Plugin from Disk…** → pick the zip → restart.

## Update icons from upstream
```sh
git clone https://github.com/iotacb/charmed-extended /tmp/charmed
python3 tools/import_theme.py /tmp/charmed && ./build.sh
```

Set `IDE=/path/to/Rider.app/Contents ./build.sh` to build with a different IDE's bundled JDK.
