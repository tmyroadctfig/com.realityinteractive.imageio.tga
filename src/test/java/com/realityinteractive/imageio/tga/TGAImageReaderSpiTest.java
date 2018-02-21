package com.realityinteractive.imageio.tga;


import org.junit.jupiter.api.Test;
import sun.awt.image.IntegerInterleavedRaster;

import javax.imageio.ImageIO;
import javax.imageio.spi.IIORegistry;
import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.io.IOException;
import java.net.URL;

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

        DirectColorModel color = (DirectColorModel) image.getColorModel();
        assert color.getRedMask() == 0xff;
        assert color.getGreenMask() == 0xff00;
        assert color.getBlueMask() == 0xff0000;
        assert color.getAlphaMask() == 0;

        IntegerInterleavedRaster raster = (IntegerInterleavedRaster) image.getRaster();
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