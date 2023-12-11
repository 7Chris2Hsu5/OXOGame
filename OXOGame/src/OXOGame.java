
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class OXOGame extends Frame
    implements WindowListener, ActionListener, MouseListener, KeyListener {
  private static final long serialVersionUID = 4493180057657097249L;
  private static final Font FONT = new Font("SansSerif", Font.PLAIN, 14);

  OXOController controller;
  TextField inputBox;
  OXOView view;

  public static void main(String[] args) {
    System.setProperty("awt.useSystemAAFontSettings", "on");
    System.setProperty("swing.aatext", "true");
    new OXOGame(350, 400);
  }

  public OXOGame(int width, int height) {
    super("OXO Board");
    OXOModel model = new OXOModel(3, 3, 3);
    model.addPlayer(new OXOPlayer('X'));
    model.addPlayer(new OXOPlayer('O'));
    controller = new OXOController(model);
    inputBox = new TextField("");
    inputBox.addActionListener(this);
    inputBox.setFont(FONT);
    inputBox.addKeyListener(this);
    view = new OXOView(model);
    view.addMouseListener(this);
    view.addKeyListener(this);
    Panel contentPane = new Panel();
    contentPane.setLayout(new BorderLayout());
    contentPane.add(inputBox, BorderLayout.SOUTH);
    contentPane.add(view, BorderLayout.CENTER);
    this.setLayout(new GridLayout(1, 1));
    this.add(contentPane);
    this.setSize(width, height);
    this.setVisible(true);
    this.addWindowListener(this);
  }

  @Override
  public Insets getInsets() {
    return new Insets(30, 7, 7, 7);
  }

  @Override
  public void actionPerformed(ActionEvent event) { // input data
    try {
      String command = inputBox.getText();
      inputBox.setText("");
      controller.handleIncomingCommand(command);
      view.repaint();
    } catch (OXOMoveException exception) {
      System.out.println("Game move exception: " + exception);
    }
  }

  @Override
  public void mousePressed(MouseEvent event) { // mouse click
    if (event.getX() < 35) {
      if (event.isPopupTrigger()) controller.removeRow();
      else if (event.getModifiersEx() == MouseEvent.BUTTON3_DOWN_MASK) controller.removeRow();
      else controller.addRow();
    }
    if (event.getY() < 35) {
      if (event.isPopupTrigger()) controller.removeColumn();
      else if (event.getModifiersEx() == MouseEvent.BUTTON3_DOWN_MASK) controller.removeColumn();
      else controller.addColumn();
    }
    view.repaint();
  }

  @Override
  public void keyPressed(KeyEvent event) {
    inputBox.setText(inputBox.getText().replace("+",""));
    inputBox.setText(inputBox.getText().replace("-",""));
  }

  @Override
  public void keyReleased(KeyEvent event) {
    inputBox.setText(inputBox.getText().replace("+",""));
    inputBox.setText(inputBox.getText().replace("-",""));
  }

  @Override
  public void keyTyped(KeyEvent event) {
    if (event.getKeyChar() == '+') controller.increaseWinThreshold();
    if (event.getKeyChar() == '-') controller.decreaseWinThreshold();
  }

  @Override
  public void mouseClicked(MouseEvent event) {}

  @Override
  public void mouseEntered(MouseEvent event) {}

  @Override
  public void mouseExited(MouseEvent event) {}

  @Override
  public void mouseReleased(MouseEvent event) {}

  @Override
  public void windowActivated(WindowEvent event) {}

  @Override
  public void windowDeactivated(WindowEvent event) {}

  @Override
  public void windowDeiconified(WindowEvent event) {}

  @Override
  public void windowIconified(WindowEvent event) {}

  @Override
  public void windowClosed(WindowEvent event) {}

  @Override
  public void windowOpened(WindowEvent event) {}

  @Override
  public void windowClosing(WindowEvent e) {
    this.dispose();
    System.exit(0);
  }
}