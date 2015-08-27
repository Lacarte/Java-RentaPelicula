/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

/**
 *
 * @author Guender
 */

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import javax.swing.*;


public class Marquee extends JComponent {
private BufferedImage image;
private Dimension imageSize;
private volatile int currOffset;
private Thread internalThread;
private volatile boolean noStopRequested;

public Marquee(String text) {
currOffset = 0;
buildImage(text);
setMinimumSize(imageSize);
setPreferredSize(imageSize);
setMaximumSize(imageSize);
setSize(imageSize);

noStopRequested = true;
Runnable r = new Runnable() {
  public void run()
  {
      try {
           runWork();
          } catch (Exception x) {x.printStackTrace();}
  }
  };

  internalThread = new Thread(r, "Marquee");
  internalThread.start();
}

private void buildImage(String text) {
RenderingHints renderHints = new RenderingHints(
    RenderingHints.KEY_ANTIALIASING,
    RenderingHints.VALUE_ANTIALIAS_ON);

renderHints.put(RenderingHints.KEY_RENDERING,
    RenderingHints.VALUE_RENDER_QUALITY);

BufferedImage scratchImage = new BufferedImage(1, 1,
    BufferedImage.TYPE_INT_RGB);

Graphics2D scratchG2 = scratchImage.createGraphics();
scratchG2.setRenderingHints(renderHints);

Font font = new Font("Helvetica", Font.BOLD | Font.BOLD, 14);

FontRenderContext frc = scratchG2.getFontRenderContext();
TextLayout tl = new TextLayout(text, font, frc);
    java.awt.geom.Rectangle2D textBounds = tl.getBounds();
int textWidth = (int) Math.ceil(textBounds.getWidth());
int textHeight = (int) Math.ceil(textBounds.getHeight());

int horizontalPad = 1000;
int verticalPad = 10;

imageSize = new Dimension(textWidth + horizontalPad, textHeight
    + verticalPad);

image = new BufferedImage(imageSize.width, imageSize.height,
    BufferedImage.TYPE_INT_RGB);

Graphics2D g2 = image.createGraphics();
g2.setRenderingHints(renderHints);

int baselineOffset = (verticalPad / 2) - ((int) textBounds.getY());

g2.setColor(Color.WHITE);
g2.fillRect(0, 0, imageSize.width, imageSize.height);

g2.setColor(Color.BLACK);
tl.draw(g2, 0, baselineOffset);

// Free-up resources right away, but keep "image" for
// animation.
scratchG2.dispose();
scratchImage.flush();
g2.dispose();
 }
public void paint(Graphics g) {
// Make sure to clip the edges, regardless of curr size
g.setClip(0, 0, imageSize.width, imageSize.height);

int localOffset = currOffset; // in case it changes
g.drawImage(image, -localOffset, 0, this);
g.drawImage(image, imageSize.width - localOffset, 0, this);

// draw outline
g.setColor(Color.black);
g.drawRect(0, 0, imageSize.width - 1, imageSize.height - 1);
  }
private void runWork() {
while (noStopRequested) {
  try {
    Thread.sleep(10); // 10 frames per second

    // adjust the scroll position
    currOffset = (currOffset + 1) % imageSize.width;

    // signal the event thread to call paint()
    repaint();
  } catch (InterruptedException x) {
    Thread.currentThread().interrupt();
  }
  }
 }

public void stopRequest() {
noStopRequested = false;
internalThread.interrupt();
}

public boolean isAlive() {
return internalThread.isAlive();
}


}