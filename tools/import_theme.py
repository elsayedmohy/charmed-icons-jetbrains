"""Copy SVGs from a charmed-icons checkout and write the name -> icon mapping.

Usage: python3 tools/import_theme.py <path-to-charmed-extended>
Dark themes use themes/folders, light themes use themes/folders-alt
(IntelliJ loads `x_dark.svg` automatically under a dark theme).
"""
import json, shutil, sys
from pathlib import Path

src = Path(sys.argv[1]) / "themes"
res = Path(__file__).resolve().parent.parent / "src/main/resources"
icons = res / "icons"
shutil.rmtree(icons, ignore_errors=True)
(icons / "files").mkdir(parents=True)
(icons / "folders").mkdir(parents=True)

for svg in (src / "files").glob("*.svg"):
    shutil.copy(svg, icons / "files" / svg.name)
for svg in (src / "folders").glob("*.svg"):
    alt = src / "folders-alt" / svg.name
    shutil.copy(alt if alt.exists() else svg, icons / "folders" / svg.name)
    shutil.copy(svg, icons / "folders" / f"{svg.stem}_dark.svg")

theme = json.loads((src / "charmed-icon-theme.json").read_text())
defs = theme["iconDefinitions"]

def path(icon_id):
    # "files/x.svg" / "folders/x.svg"; light "-alt" variants are paired via _dark
    p = defs[icon_id]["iconPath"].replace("folders-alt/", "folders/")
    return p

rows = [("default-folder", "-", path(theme["folder"]))]
rows += [("name", k.lower(), path(v)) for k, v in theme.get("fileNames", {}).items()]
rows += [("ext", k.lower(), path(v)) for k, v in theme.get("fileExtensions", {}).items()]
rows += [("folder", k.lower(), path(v)) for k, v in theme.get("folderNames", {}).items()]
(res / "charmed").mkdir(exist_ok=True)
(res / "charmed/mapping.tsv").write_text("".join("\t".join(r) + "\n" for r in rows))
print(f"{len(rows)} mappings")
