/**
 * Copyright (C) cedarsoft GmbH.
 * <p/>
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * <p/>
 * http://www.cedarsoft.org/gpl3ce
 * (GPL 3 with Classpath Exception)
 * <p/>
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 * <p/>
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 * <p/>
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * <p/>
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */

package com.cedarsoft;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.VolatileImage;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * Demo that shows a bug with volatile images.
 * <p/>
 * If a volatile image is created with width/height 0/0, g.drawString does no longer work.
 *
 *
 * This bug is reproducible with those java versions:
 * <ul>
 *   <li>1.8.0_66</li>
 *   <li>1.9.0-ea-b81</li>
 * </ul>
 *
 * System:
 * Ubuntu, 4.2.0-16-generic x86_64, Unity, Nvidia closed source graphics driver (GeForce GTX 970)
 */
public class BugDemo {
  public static void main(String[] args) {
    System.out.println("Java version " + System.getProperty("java.version"));

    JFrame frame = new JFrame();

    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsConfiguration gc = ge.getDefaultScreenDevice().getDefaultConfiguration();

    //If this line is removed, the string in line 78 will show up
    VolatileImage volatileImage = gc.createCompatibleVolatileImage(0, 0);

    frame.getContentPane().setLayout(new BorderLayout());
    JComponent component = new JComponent() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        //This string is *not* visible if the volatile image is created in line 69
        g.drawString("This is a string", 10, 15);
      }
    };

    frame.getContentPane().add(component, BorderLayout.CENTER);

    frame.setSize(1024, 768);
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    frame.setVisible(true);
  }
}
