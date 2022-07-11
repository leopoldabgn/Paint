# Paint
Drawing app coded in java. This project is using the Java Swing library. With this app, you can draw and edit images.

## How to install

### Clone the repo
You can clone the repository and launch it with you IDE to compile it. It's a maven project, so you can go in the folder and launch the following commands :
```
mvn package
java -jar target/paint*.jar
```

### Last release
You can also download the last release. It's just the jar file :
```
java -jar paint*.jar
```

## How to use

### Add images
From the toolbar, select **File -> Open Image...** The image will then appear. If not, you can try resizing the window to refresh the screen.
You can now move the image around your screen with your mouse and resize it. When you're done, just press **ENTER**.
![paint_1](https://user-images.githubusercontent.com/95108507/178247356-1afec1ff-f021-4769-b92b-87eeb82d0d99.png)

### Brush
First, press the brush button at the top. In the toolbar, you can change the **shape**, **color** and **size** of the brush. Just go to **Edit -> Brush Shape/Color/Size** and select the settings you want.  
Finally, you can hold the **left click** of your mouse to draw on the screen. For example :  
![paint_2](https://user-images.githubusercontent.com/95108507/178248207-93928b82-674a-4fa7-ae68-4673d376af18.png)

### Selection
Press the **selection** button at the top. Now, you can select a zone on the screen. You need to hold the **left click** of your mouse and make a selection. Then, you can press **suppr** to delete the zone.

### Save
You can save your artwork : **File -> Screenshot**. Now, you image is save in the same folder as the jar file.

### Other options
- Clean : You can clean the screen : **File -> Clean**
- Quit  : **File -> Quit**
- Undo/Redo : Press CTRL+Z / CTRL+Y. If it doesn't work, you can go here : **Edit->Undo/Redo**
