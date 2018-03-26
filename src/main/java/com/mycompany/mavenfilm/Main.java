/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenfilm;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.api.awt.AWTSequenceEncoder;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.io.SeekableByteChannel;
import org.jcodec.common.model.Picture;
import org.jcodec.common.model.Rational;
import org.jcodec.scale.AWTUtil;

/**
 *
 * @author B Ricks, PhD <bricks@unomaha.edu>
 */
public class Main {

    public static void main(String[] args) {
        /*try {
            int frameNumber = 42;
            Picture picture = FrameGrab.getFrameFromFile(new File("FerrisWheel.mp4"), frameNumber);

//for JDK (jcodec-javase)
            BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);
            ImageIO.write(bufferedImage, "png", new File("frame42.png"));
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JCodecException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }*/

        SeekableByteChannel out = null;
        try {
            out = NIOUtils.writableFileChannel("output.mp4");
            // for Android use: AndroidSequenceEncoder
            AWTSequenceEncoder encoder = new AWTSequenceEncoder(out, Rational.R(25, 1));
            for (int i = 0; i < 255; i++) {
                // Generate the image, for Android use Bitmap
                BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_4BYTE_ABGR);
                
                Graphics2D g = (Graphics2D)image.getGraphics();
                
                g.setColor(new Color(0,0,0));
                g.fillRect(0, 0, 640, 480);
                
                g.setColor(new Color(255, 255, 255));
                g.fillRect(i, 240, 10, 10);
                
                
                g.dispose();
                // Encode the image
                encoder.encodeImage(image);
            }
            // Finalize the encoding, i.e. clear the buffers, write the header, etc.
            encoder.finish();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            NIOUtils.closeQuietly(out);
        }
    }

}
