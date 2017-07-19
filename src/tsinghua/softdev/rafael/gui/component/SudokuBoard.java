package tsinghua.softdev.rafael.gui.component;

import tsinghua.softdev.rafael.data.Cell;
import tsinghua.softdev.rafael.data.SudokuConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.Enumeration;

/**
 * This class extends the JTable class, it sets it's initial properties and
 * defines a custom cell renderer for the JTable.
 * 
 * @author Rafael da Silva Costa - 2015280364
 * @version 1.0
 */
public class SudokuBoard extends JTable implements SudokuConstants
{
	private static final long serialVersionUID = 6472748116017391165L;

	/**
	 * Constructor method - sets the JTable's model and initial properties, also
	 * assigning a custom cell renderer to the JTable.
	 * 
	 * @param model
	 *            : The SudokuBoardModel to be used.
	 */
	public SudokuBoard(TableModel model)
	{
		super(model);
		setCellSelectionEnabled(true);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setDefaultRenderer(getColumnClass(0), new CellRenderer());
		setRowHeight(CELL_HEIGHT);
		setFont(new Font(Font.SANS_SERIF, Font.BOLD, CELL_FONT_SIZE));
		setBorder(BorderFactory.createLoweredBevelBorder());

		Enumeration<TableColumn> columns = getColumnModel().getColumns();

		while (columns.hasMoreElements())
		{
			columns.nextElement().setPreferredWidth(CELL_WIDTH);
		}
	}

	/**
	 * This inner class contains the custom cell renderer used in this JTable,
	 * it extends DefaultTableCellRenderer to inherit the default behaviour in
	 * order to customise the display for the Sudoku game to be displayed
	 * properly on the JTable.
	 */
	private static class CellRenderer extends DefaultTableCellRenderer
	{
		private static final long serialVersionUID = 8745512768071893271L;

		/**
		 * Constructor method - sets the CellRenderer initial properties and
		 * initialises the border objects used in the cells.
		 */
		CellRenderer()
		{
			super();
			setHorizontalAlignment(SwingConstants.CENTER);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column)
		{
			int blockNumber = (row / BLOCK_SIZE) * BLOCK_SIZE + (column / BLOCK_SIZE);

			setBackground(blockNumber % 2 == 0 ? null : Color.LIGHT_GRAY);
			setBackground(((SudokuBoardModel) table.getModel()).isDuplicate(row, column) ? Color.RED
			                                                                             : getBackground());
			setForeground(((Cell) value).isHost() ? null : Color.YELLOW);
			setForeground(((Cell) value).isFixed() ? Color.GRAY : null);

			return super.getTableCellRendererComponent(table, value, isSelected,
			                                           hasFocus, row, column);
		}
	}
}