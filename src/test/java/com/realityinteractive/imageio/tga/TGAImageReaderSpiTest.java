package com.realityinteractive.imageio.tga;


import org.junit.jupiter.api.Test;
import sun.awt.image.ByteInterleavedRaster;

import javax.imageio.ImageIO;
import javax.imageio.spi.IIORegistry;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

class TGAImageReaderSpiTest {

    @Test
    void testFail() throws IOException {

        URL url = getClass().getClassLoader().getResource("PlyonTexture.tga");
        BufferedImage image = ImageIO.read(url);

        assert image == null;
    }

    @Test
    void testSuccess() throws IOException {

        IIORegistry registry = IIORegistry.getDefaultInstance();
        registry.registerServiceProvider(new TGAImageReaderSpi());

        URL url = getClass().getClassLoader().getResource("PlyonTexture.tga");
        BufferedImage image = ImageIO.read(url);

        assert image.getType() == 4;

        ComponentColorModel color = (ComponentColorModel) image.getColorModel();
        assert color.getNumComponents() == 3;
        assert color.getPixelSize() == 8;
        assert Arrays.equals(color.getComponentSize(), new int[] {8, 8, 8});
        assert color.hasAlpha() == false;
        assert color.isAlphaPremultiplied() == false;

        ByteInterleavedRaster raster = (ByteInterleavedRaster) image.getRaster();
        assert raster.getWidth() == 1024;
        assert raster.getHeight() == 1024;
        assert raster.getNumBands() == 3;
        assert raster.getSampleModelTranslateX() == 0;
        assert raster.getSampleModelTranslateY() == 0;
        assert raster.getDataOffset(0) == 0;

        assert image.getPropertyNames() == null;

        assert image.getAccelerationPriority() == 0.5;
    }
}