## Copy of Reality Interactive's ImageIO TGA library

### Building

`ant build`

### Usage
Simply register the plugin with ImageIO:

``` java
IIORegistry registry = IIORegistry.getDefaultInstance();
registry.registerServiceProvider(new com.realityinteractive.imageio.tga.TGAImageReaderSpi());
```

For performance reasons, when using this spi use ImageIO methods that use File as argument not Streams

``` java
// do not use
ImageIO.read(new BufferedInputStream(new FileInputStream(path.toFile())));
// use
ImageIO.read(path.toFile());
```