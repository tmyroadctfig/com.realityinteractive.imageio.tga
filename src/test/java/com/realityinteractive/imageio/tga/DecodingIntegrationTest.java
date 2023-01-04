package com.realityinteractive.imageio.tga;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.spi.IIORegistry;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

/**
 * Loads & decodes 16, 24, 32 bit TGAs with/without RLE and compares loaded pixels with pixels
 * from equivalent PNG image.
 * 
 * @author Jan Mothes
 */
class DecodingIntegrationTest
{   
    private static final boolean loggingEnabled = false;
    private static TGAImageReaderSpi spi;
    
    void assertImageEquals(BufferedImage expected, BufferedImage actual, String imageName) {
        assertEquals(expected.getWidth(), actual.getWidth());
        assertEquals(expected.getHeight(), actual.getHeight());
        if (loggingEnabled) {
            System.out.println("Comparing '" + imageName + "'");
        }
        
        for (int y = 0; y < expected.getHeight(); y++) {
            for (int x = 0; x < expected.getWidth(); x++) {
                int expectedPixel = expected.getRGB(x, y);
                int actualPixel = actual.getRGB(x, y);
                if (expectedPixel != actualPixel) {
                    throw new AssertionFailedError(
                            "for '" + imageName + "' expected pixel at (width=" + x + ", height=" + y + ") to be\n"
                                    + "               A        R        G        B\n"
                                    + "        " + formatPixelBinary(expectedPixel) + "\n"
                                    + "but was " + formatPixelBinary(actualPixel));
                }
            }
        }
    }
    
    private String formatPixelBinary(int argb) {
        int a = (argb & 0xff000000) >>> 24;
        int r = (argb & 0x00ff0000) >>> 16;
        int g = (argb & 0x0000ff00) >>> 8;
        int b = argb & 0x000000ff;
        return String.format("%1$08d %2$08d %3$08d %4$08d", 
                Integer.parseInt(Integer.toBinaryString(a)),
                Integer.parseInt(Integer.toBinaryString(r)),
                Integer.parseInt(Integer.toBinaryString(g)),
                Integer.parseInt(Integer.toBinaryString(b)));
    }
    
    @BeforeAll
    static void setup() {
        IIORegistry registry = IIORegistry.getDefaultInstance();
        spi = new TGAImageReaderSpi();
        registry.registerServiceProvider(spi);
    }
    
    @AfterAll
    static void cleanup() {
        IIORegistry registry = IIORegistry.getDefaultInstance();
        registry.deregisterServiceProvider(spi);
    }
    
    @Test
    void test() throws IOException {        
        String[] comparedFiles = new String[] {
                "test_mono_1_bit",            
                "test_mono_8_bit",                
                "test_mono_16_bit",
                "test_16_bit",
                "test_24_bit",
                "test_32_bit",
                "test_small_mono_1_bit",            
                "test_small_mono_8_bit",
                "test_small_mono_16_bit",
                "test_small_16_bit",
                "test_small_24_bit",
                "test_small_32_bit"
        };
        String basePath = "integration/";
        String suffixRLE = "_RLE";
        
        for (String image : comparedFiles) {
            String pngName = image + ".png";
            String tgaName = image + ".tga";
            String tgaRleName = image + suffixRLE + ".tga";
            
            BufferedImage png = read(basePath + pngName);
            {
                BufferedImage tga = read(basePath + tgaName);
                assertImageEquals(png, tga, tgaName);
            }
            if (!image.endsWith("_1_bit")) //RLE is unsupported for 1bit images
            {
                BufferedImage tga = read(basePath + tgaRleName);
                assertImageEquals(png, tga, tgaRleName);
            }
        }
    }
    
    private BufferedImage read(String resourcePath) throws IOException {
        return ImageIO.read(getClass().getClassLoader().getResource(resourcePath));
    }
}
