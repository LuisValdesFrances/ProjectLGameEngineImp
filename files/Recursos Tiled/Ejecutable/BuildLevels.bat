cd "../../../src/bin/levels"
del *.map


java -jar LVTiledParser.jar level_1_hhdpi

cd "../../../files/Recursos tiled/Ejecutable/levels"

move *.map ../../../../src/bin/levels/hhdpi

cd "../../../../files/Recursos tiled/Ejecutable"

rmdir /s /q levels


java -jar LVTiledParser.jar level_rock_test_hhdpi

cd "../../../files/Recursos tiled/Ejecutable/levels

move *.map ../../../../src/bin/levels/hhdpi

cd "../../../../files/Recursos tiled/Ejecutable"

rmdir /s /q levels


java -jar LVTiledParser.jar level_1_hdpi

cd "../../../files/Recursos tiled/Ejecutable/levels"

move *.map ../../../../src/bin/levels/hdpi

cd "../../../../files/Recursos tiled/Ejecutable"

rmdir /s /q levels


java -jar LVTiledParser.jar level_rock_test_hdpi

cd "../../../files/Recursos tiled/Ejecutable/levels

move *.map ../../../../src/bin/levels/hdpi

cd "../../../../files/Recursos tiled/Ejecutable"

rmdir /s /q levels

java -jar LVTiledParser.jar level_1_mdpi

cd "../../../files/Recursos tiled/Ejecutable/levels"

move *.map ../../../../src/bin/levels/mdpi

cd "../../../../files/Recursos tiled/Ejecutable"

rmdir /s /q levels


java -jar LVTiledParser.jar level_rock_test_mdpi

cd "../../../files/Recursos tiled/Ejecutable/levels

move *.map ../../../../src/bin/levels/mdpi

cd "../../../../files/Recursos tiled/Ejecutable"

rmdir /s /q levels

pause

