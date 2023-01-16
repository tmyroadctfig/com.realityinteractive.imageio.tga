package com.realityinteractive.imageio.tga;


import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.imageio.spi.IIORegistry;

import org.junit.jupiter.api.Test;

class TGAImageReaderSpiTest {

    @Test
    void testFail() throws IOException {

        URL url = getClass().getClassLoader().getResource("PlyonTexture.tga");
        BufferedImage image = ImageIO.read(url);

        assertNull(image);
    }

    @Test
    void testSuccess() throws IOException {

        IIORegistry registry = IIORegistry.getDefaultInstance();
        registry.registerServiceProvider(new TGAImageReaderSpi());

        URL url = getClass().getClassLoader().getResource("PlyonTexture.tga");
        BufferedImage image = ImageIO.read(url);

        assertEquals(BufferedImage.TYPE_3BYTE_BGR, image.getType());

        ComponentColorModel color = (ComponentColorModel) image.getColorModel();
        assertEquals(3, color.getNumComponents());
        assertEquals(24, color.getPixelSize());
        assertArrayEquals(new int[] {8, 8, 8}, color.getComponentSize());
        assertFalse(color.hasAlpha());
        assertFalse(color.isAlphaPremultiplied());

        assertEquals(1024, image.getWidth());
        assertEquals(1024, image.getHeight());

        assertNull(image.getPropertyNames());

        assertEquals(0.5, image.getAccelerationPriority());
    }
}