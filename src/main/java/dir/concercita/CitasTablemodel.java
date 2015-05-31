package dir.concercita;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class CitasTablemodel implements TableModel {

	private TableModelListener listener;
	
	// El ResultSet guardará todo el contenido de la consulta.
	private ResultSet resultado;
	
	public CitasTablemodel(Connection conex){
		try {
			Statement sentencia = conex.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, 
					ResultSet.CONCUR_READ_ONLY);
			resultado = sentencia.executeQuery("select *  from citas");
		} catch (SQLException e) {
			
		}
	}
		
	@Override
	public void addTableModelListener(TableModelListener l) {
		listener = l;
	}

	@Override
	public Class<?> getColumnClass(int columIndex) {
		switch (columIndex){
			case 0 : return Integer.class;
			case 1 : return String.class;
			case 2 : return String.class;
			case 3 : return String.class;
		}
		return null;
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public String getColumnName(int columIndex) {
		switch (columIndex){
			case 0 : return "Id";
			case 1 : return "Nombre";
			case 2 : return "Fecha/Hora";
			case 3 : return "Asunto";
		}
		return null;
	}

	@Override
	public int getRowCount() {
		try {
			resultado.last();
			return resultado.getRow(); //devuelve el numero de fila actual.
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public Object getValueAt(int rowIndex, int columIndex) {
		try {
			resultado.absolute(rowIndex + 1);//permite posicionar el cursor en una fila. ResulSet empieza en 1 y no en 0.
			switch (columIndex){
				case 0 : return resultado.getInt("id_cita");
				case 1 : return resultado.getString("nombre");
				case 2 : return resultado.getDate("fecha");
				case 3 : return resultado.getString("Asunto");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean isCellEditable(int arg0, int arg1) {
		return false;
	}

	@Override
	public void removeTableModelListener(TableModelListener arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setValueAt(Object arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
	}

}
