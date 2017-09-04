cd "../../../src/bin/levels"
del *.map


java -jar LVTiledParser.jar level_1

cd "../../../files/Recursos tiled/Ejecutable/levels"

move *.map ../../../../src/bin/levels

cd "../../../../files/Recursos tiled/Ejecutable"

rmdir /s /q levels


java -jar LVTiledParser.jar level_rock_test

cd "../../../files/Recursos tiled/Ejecutable/levels"

move *.map ../../../../src/bin/levels

cd "../../../../files/Recursos tiled/Ejecutable"

rmdir /s /q levels

pause

