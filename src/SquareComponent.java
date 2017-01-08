import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;


public class SquareComponent extends JComponent{

	private Polygon square = new Polygon();

	public boolean contains(Point p)
	{
		return square.contains(p);
	}

	public boolean contains(int x, int y)
	{
		return square.contains(x, y);
	}

	public void setSize(Dimension d)
	{
		super.setSize(d);
		calculateCoords();
	}

	public void setSize(int w, int h)
	{
		super.setSize(w, h);
		calculateCoords();
	}

	public void setBounds(int x, int y, int width, int height)
	{
		super.setBounds(x, y, width, height);
		calculateCoords();
	}

	public void setBounds(Rectangle r)
	{
		super.setBounds(r);
		calculateCoords();
	}

	protected void processMouseEvent(MouseEvent e)
	{
		if ( contains(e.getPoint()) )
			super.processMouseEvent(e);
	}

	private void calculateCoords()
	{
		int w = 27;
		int h = 27;

		int nPoints = 4;
		int[] squareX = new int[nPoints];
		int[] squareY = new int[nPoints];

		agressiveCoords(w, h, squareX, squareY);

		square = new Polygon(squareX, squareY, nPoints);
	}


	private void agressiveCoords(int w, int h, int[] squareX, int[] squareY) {

		squareX[0] = 0;
		squareY[0] = 0;

		squareX[1] = 0;
		squareY[1] = h;

		squareX[2] = w;
		squareY[2] = h;

		squareX[3] = w;
		squareY[3] = 0;

	}

	protected void paintComponent(Graphics g)
	{
		g.setColor(getBackground());
		g.fillPolygon(square);
		g.setColor(getForeground());
		g.drawPolygon(square);
	}

	protected void paintBorder(Graphics g)
	{
	// do nothing
	}
	 
}
