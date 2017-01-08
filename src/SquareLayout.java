import java.awt.*;

public class SquareLayout implements LayoutManager, java.io.Serializable
{
	private int cgap;
	private int rows;
	private int cols;

	public SquareLayout(int r, int c, int hgap) {
		if ((r == 0) && (c == 0)) {
			throw new IllegalArgumentException("rows and cols cannot both be zero");
		}
		this.rows = r;
		this.cols = c;
		this.cgap = hgap;
	}
 

	public int getRows() {
		return rows;
	}
	public void setRows(int r) {
		if ((r == 0) && (this.cols == 0)) {
			throw new IllegalArgumentException("rows and cols cannot both be zero");
		}
		this.rows = r;
	}

	public int getColumns() {
		return cols;
	}
	public void setColumns(int c) {
		if ((c == 0) && (this.rows == 0)) {
			throw new IllegalArgumentException("rows and cols cannot both be zero");
		}
		this.cols = c;
	}
 

	public int getGap() {
		return cgap;
	}
	public void setGap(int gap) {
		this.cgap = gap;
	}

	public void addLayoutComponent(String name, Component comp)
	{
		// do nothing
	}

	public void removeLayoutComponent(Component comp)
	{
		// do nothing
	}

	public Dimension preferredLayoutSize(Container parent)
	{
        return new Dimension(500, 500);
	}

	public Dimension minimumLayoutSize(Container parent)
	{
        return new Dimension(500, 500);
	}

	public void layoutContainer(Container parent)
	{
		synchronized (parent.getTreeLock())
		{
			Insets insets = parent.getInsets();
			int ncomponents = parent.getComponentCount();
			int nrows = rows;
			int ncols = cols;
 
			if ( ncomponents == 0 )
			{
				return;
			}
			if ( nrows > 0 )
			{
				ncols = (ncomponents + nrows - 1) / nrows;
			}
			else
			{
				nrows = (ncomponents + ncols - 1) / ncols;
			}
 
			int w = parent.getWidth() - (insets.left + insets.right);
			int h = parent.getHeight() - (insets.top + insets.bottom);
 
			w = (int)((w - (ncols - 1) * cgap) / (ncols + (nrows>1?0.5f:0.0f)));
 
			float effectiveRows = 1 + ((nrows - 1) * 0.75f);
			h = (int)((h - (nrows - 1) * cgap) / effectiveRows);
 
			int xoffset = (w+cgap)/2;
			int yoffset = (int)(h * 0.75f);
			boolean staggeredRow = false;
 
			for ( int r = 0, y = insets.top; r < nrows; r++, y += yoffset + cgap )
			{
				int offset = 0;
 
				if ( staggeredRow )
					offset = xoffset;
 
				for ( int c = 0, x = insets.left; c < ncols; c++, x += w + cgap )
				{
					int i = r * ncols + c;
 
					if ( i < ncomponents )
					{
                        offset = 0;
						parent.getComponent(i).setBounds(x+offset, y, w, h);
					}
 
				}
 
				staggeredRow = !staggeredRow;
			}
		}
	}

	public String toString() {
		return getClass().getName() + "[gap=" + cgap +
		",rows=" + rows + ",cols=" + cols + "]";
	}
 
}