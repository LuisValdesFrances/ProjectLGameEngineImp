

java -jar LVTiledParser.jar level_1
cd "../../../src/bin/levels"
del *.map

cd "../../../files/Recursos tiled/Ejecutable/levels"

move *.map ../../../../src/bin/levels

cd "../../../../files/Recursos tiled/Ejecutable"

rmdir /s /q levels

pause