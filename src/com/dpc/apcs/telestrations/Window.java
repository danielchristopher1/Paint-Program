package com.dpc.apcs.telestrations;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Stack;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class Window extends JFrame {
    private JPanel drawPanel;
    private JPanel optionPanel;
    private Color drawColor;
    private Stack<PaintShape> shapes;
    private BasicStroke penStroke;
    private int eraserSize = 25;
    private static final int BACK_POP_SIZE = 25; // How much to pop off the stack when back is clicked
    private boolean lineStarted;
    private boolean isDrawing;
    
    public Window() {
        optionPanel = new JPanel();
        
        drawColor = Color.BLACK;

        shapes = new Stack<PaintShape>();
        
        penStroke = new BasicStroke(10);
        
        final JSlider eraserSizeSlider = new JSlider();
        eraserSizeSlider.setMaximum(100);
        eraserSizeSlider.setMinimum(1);
        
        eraserSizeSlider.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                eraserSize = eraserSizeSlider.getValue();
            }
            
        });
        
        JLabel eraserSizeSliderLabel = new JLabel("Eraser Size");
        
        JPanel eraserPanel = new JPanel();
        eraserPanel.add(new JLabel("Eraser Size"));
        eraserPanel.add(eraserSizeSlider);
        
        JButton chooseColor = new JButton("Choose Color");
        chooseColor.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                pickColor();
            }
            
        });
        
        JPanel checkPanel = new JPanel();
        checkPanel.setLayout(new FlowLayout());
        
        ButtonGroup group = new ButtonGroup();
        
        JButton back = new JButton("Undo");
        back.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                for(int i = 0; i < BACK_POP_SIZE; i++) {
                    
                    if(shapes.isEmpty()) {
                        break;
                    }
                    
                    shapes.pop();
                }
                
                drawPanel.repaint();
            }
            
        });
        
        JCheckBox erase = new JCheckBox("Eraser");
        erase.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                isDrawing = false;
            }
            
        });
        
        JCheckBox pen = new JCheckBox("Pen");
        pen.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                isDrawing = true;
            }
            
        });
        
        JLabel penStrokeLabel = new JLabel("Pen Stroke");
        
        final JSlider penStrokeSlider = new JSlider();
        penStrokeSlider.setMaximum(100);
        penStrokeSlider.setMinimum(1);
        
        penStrokeSlider.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                penStroke = new BasicStroke(penStrokeSlider.getValue(),
                							BasicStroke.CAP_ROUND,
                							BasicStroke.JOIN_ROUND);
            }
            
        });
        
        group.add(erase);
        group.add(pen);
        group.add(back);
        
        JPanel penStrokePanel = new JPanel();
        
        penStrokePanel.add(penStrokeLabel);
        penStrokePanel.add(penStrokeSlider);
        
        JPanel penPanel = new JPanel();
        
        penPanel.add(erase);
        penPanel.add(pen);
        penPanel.add(back);
        
        Box box = Box.createVerticalBox();
        box.add(chooseColor);
        box.add(checkPanel);
        box.add(penPanel);
        box.add(penStrokePanel);
        box.add(eraserPanel);
        
        optionPanel.add(box);
        
        drawPanel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
    
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, getWidth(), getHeight());

                for(int i = 0; i < shapes.size(); i++) {
                    PaintShape p = shapes.get(i);

                    penStroke = new BasicStroke(p.getStroke());
                    ((Graphics2D) g).setStroke(penStroke);
                    
                    if(p instanceof ColoredLine) {
                        ColoredLine l = (ColoredLine) p;
                        
                        g.setColor(l.getColor());
                        
                        g.drawLine(l.getX(),
                                   l.getY(),
                                   l.getX1(),
                                   l.getY1());
                    }
                    else if(p instanceof Circle) {
                        Circle c = (Circle) p;
                        
                        g.setColor(Color.WHITE);
                        
                        g.fillOval(c.getX(), c.getY(), c.getRadius(), c.getRadius());
                    }
                }
            }
        };
 
        drawPanel.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent arg0) {
                int x = arg0.getX();
                int y = arg0.getY();

                if(isDrawing) {
                    if (shapes.size() > 0 && lineStarted) {
                        
                        ColoredLine lastLine = null;
                        
                        for(int i = 0; i < shapes.size(); i++) {
                            if(shapes.get(i) instanceof ColoredLine) {
                                lastLine = (ColoredLine) shapes.get(i);
                            }
                        }
                        
                        if(lastLine != null) {
                            x = lastLine.getX1();
                            y = lastLine.getY1();
                        }
                    }
    
                    shapes.push(new ColoredLine(drawColor, 
                                                penStroke.getLineWidth(),
                                                x, 
                                                y, 
                                                arg0.getX(), 
                                                arg0.getY()));

                    repaint();
                    
                    lineStarted = true;
                }
                else {
                    shapes.push(new Circle(eraserSize, 
                                           penStroke.getLineWidth(),
                                           x - (eraserSize / 2), 
                                           y - (eraserSize / 2)));
                    
                    repaint();
                }
            }

            @Override
            public void mouseMoved(MouseEvent arg0) {
            }
            
        });
        
        drawPanel.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                lineStarted = false;
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
            
        });
        
        drawPanel.setPreferredSize(new Dimension(800, 800));
        
        setLayout(new FlowLayout());
        add(drawPanel);
        add(optionPanel);
        
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    private void pickColor() {
        drawColor = JColorChooser.showDialog(null, "Pick a color", Color.BLACK);
    }
    
    public static void main(String[] args) {
        changeFeel();
        
        new Window();
    }
    
    private static void changeFeel() {
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
        }
    }
}
