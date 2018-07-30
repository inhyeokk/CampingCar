import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

/**
 * @author JI
 *
 */
public class App {
	public static void main(String[] args) {
		JFrame frame = new JFrame("Double K");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // to close normal
		frame.setResizable(false); // can't control frame size

		PrimaryPanel primary = new PrimaryPanel();
		frame.getContentPane().add(primary);

		frame.pack();
		frame.setVisible(true);

		frame.setLocationRelativeTo(null); // 프레임 중앙에 위치
	}// main()

}// App class

class PrimaryPanel extends JPanel {
	private JButton manBtn, userBtn, nameBtn;
	private JPanel firstPanel, secondPanel;

	private JPanel mainPanel;
	private JPanel tempPanel;
	private JComboBox cmbMenu;
	private String[] strMenu = { "COMPANY", "CAR", "CLIENT", "RENT", "RPSHOP", "RP" };
	private JButton btnSearch;
	private JScrollPane jScollPane;
	private JTable jTable;

	JTextField txtUserId;
	JTextField txtUserCom;

	JTextField txtComWhere;
	JTextField txtComName;
	JTextField txtComFrom;
	JTextField txtComTo;
	JTextField[] txtAttArray;

	private BtnListener btnL;

	private DatabaseConnect dbConnect;

	public PrimaryPanel() {
		// db connect
		dbConnect = new DatabaseConnect();

		// Listener
		btnL = new BtnListener();

		// primary panel
		setBackground(Color.white);
		setPreferredSize(new Dimension(1000, 640));
		setLayout(null);

		setPage();
	}

	public void setPage() {
		mainPanel = new JPanel();
		mainPanel.setBounds(0, 70, 1000, 500);
		mainPanel.setBackground(new Color(245, 245, 245));
		mainPanel.setLayout(null);

		Font Vdn18 = new Font("Verdana", Font.PLAIN, 18);
		manBtn = new JButton("MANAGER");
		manBtn.setBounds(200, 200, 150, 50);
		manBtn.setFont(Vdn18);
		manBtn.addActionListener(btnL);
		manBtn.setVisible(true);
		add(manBtn);

		userBtn = new JButton();
		userBtn = new JButton("USER");
		userBtn.setBounds(650, 200, 150, 50);
		userBtn.setFont(Vdn18);
		userBtn.addActionListener(btnL);
		userBtn.setVisible(true);
		add(userBtn);

		// combo box for choosing table
		cmbMenu = new JComboBox();
		for (String str : strMenu)
			cmbMenu.addItem(str);
		cmbMenu.setBounds(105, 10, 170, 40);
		cmbMenu.setFont(Vdn18);
		cmbMenu.setVisible(false);
		add(cmbMenu);

		btnSearch = new JButton("Search");
		btnSearch.setBounds(285, 10, 80, 40);
		btnSearch.setFont(new Font("Verdana", Font.PLAIN, 13));
		btnSearch.addActionListener(btnL);
		btnSearch.setVisible(false);
		add(btnSearch);

		add(mainPanel);
		setVisible(true);
	}

	// select * from car where car_Company = (select com_Id from COMPANY WHERE
	// com_Name = 'CO04')

	// select * from RPINFO where rp_Clt = (select clt_Num from CLIENT where
	// clt_Name = 'BAE')
	private JPanel getUserPanel(String condition) {
		JPanel jPanel = new JPanel();
		jPanel.setBounds(0, 0, 1000, 500);
		jPanel.setBackground(new Color(245, 245, 245));
		jPanel.setLayout(null);

		firstPanel = new JPanel();
		firstPanel.setBounds(0, 0, 1000, 140);
		firstPanel.setLayout(null);
		jPanel.add(firstPanel);

		Font Vdn18 = new Font("Verdana", Font.PLAIN, 18);

		JLabel lbName = new JLabel("Client ID: ");
		lbName.setBounds(10, 0, 120, 30);
		lbName.setFont(Vdn18);
		firstPanel.add(lbName);

		txtUserId = new JTextField();
		txtUserId.setBounds(140, 0, 150, 30);
		txtUserId.setFont(Vdn18);
		firstPanel.add(txtUserId);

		manBtn = new JButton("FIND");
		manBtn.setBounds(300, 0, 80, 30);
		manBtn.setFont(Vdn18);
		manBtn.addActionListener(btnL);
		manBtn.setVisible(true);
		firstPanel.add(manBtn);

		secondPanel = new JPanel();
		secondPanel.setBounds(0, 150, 1000, 140);
		secondPanel.setLayout(null);
		secondPanel.setVisible(false);
		jPanel.add(secondPanel);

		JLabel lbComName = new JLabel("Company Name: ");
		lbComName.setBounds(10, 0, 180, 30);
		lbComName.setFont(Vdn18);
		secondPanel.add(lbComName);

		txtUserCom = new JTextField();
		txtUserCom.setBounds(200, 0, 150, 30);
		txtUserCom.setFont(Vdn18);
		secondPanel.add(txtUserCom);

		nameBtn = new JButton("FIND Name");
		nameBtn.setBounds(360, 0, 150, 30);
		nameBtn.setFont(Vdn18);
		nameBtn.addActionListener(btnL);
		nameBtn.setVisible(true);
		secondPanel.add(nameBtn);

		return jPanel;
	}

	private JPanel getUserTable(String condition) {
		JPanel jPanel = new JPanel();
		jPanel.setBounds(0, 40, 1000, 70);
		jPanel.setBackground(new Color(245, 245, 245));
		jPanel.setLayout(null);

		JTable jTable = new JTable();
		DefaultTableModel model = new DefaultTableModel();

		ArrayList<DescribeTableVO> arrDescribeTableVO = dbConnect.getTableDescription("RENT");
		Object[] columnsName = new Object[arrDescribeTableVO.size()];
		for (int i = 0; i < arrDescribeTableVO.size(); i++) {
			columnsName[i] = arrDescribeTableVO.get(i).getColumn_name();
		}
		model.setColumnIdentifiers(columnsName);

		Object[] rowData = new Object[arrDescribeTableVO.size()];
		ArrayList<RentVO> arrRentVO = dbConnect.selectRent(condition);
		for (int i = 0; i < arrRentVO.size(); i++) {
			rowData[0] = arrRentVO.get(i).getRent_Id();
			rowData[1] = arrRentVO.get(i).getRent_Car();
			rowData[2] = arrRentVO.get(i).getRent_Client();
			rowData[3] = arrRentVO.get(i).getRent_Com();
			rowData[4] = arrRentVO.get(i).getRent_Stdate();
			rowData[5] = arrRentVO.get(i).getRent_Period();
			rowData[6] = arrRentVO.get(i).getRent_Fee();
			rowData[7] = arrRentVO.get(i).getRent_Due();
			rowData[8] = arrRentVO.get(i).getRent_Efc();
			rowData[9] = arrRentVO.get(i).getRent_Efcfee();
			model.addRow(rowData);
		}

		jTable.setModel(model);
		jTable.setPreferredScrollableViewportSize(jTable.getPreferredSize());
		jTable.setFillsViewportHeight(true);
		jTable.getTableHeader().setFont(new Font("Verdana", Font.PLAIN, 17));
		jTable.setFont(new Font("Verdana", Font.PLAIN, 17));
		jTable.setRowHeight(20);
		jTable.getTableHeader().setReorderingAllowed(false);

		jScollPane = new JScrollPane(jTable);
		// jScollPane.setPreferredSize(new Dimension(650, 150));
		jScollPane.setBounds(0, 0, 1000, 70);
		jScollPane.setBorder(BorderFactory.createTitledBorder("RENT" + txtUserId.getText() + "'s Info"));
		// jScollPane.getVerticalScrollBar().setValue(jScollPane.getVerticalScrollBar().getMaximum());
		jPanel.add(jScollPane);

		return jPanel;
	}

	private JPanel getUserComTable(String condition) {
		JPanel jPanel = new JPanel();
		jPanel.setBounds(0, 40, 1000, 200);
		jPanel.setBackground(new Color(245, 245, 245));
		jPanel.setLayout(null);

		JTable jTable = new JTable();
		DefaultTableModel model = new DefaultTableModel();

		ArrayList<DescribeTableVO> arrDescribeTableVO = dbConnect.getTableDescription("CAR");
		Object[] columnsName = new Object[arrDescribeTableVO.size()];
		for (int i = 0; i < arrDescribeTableVO.size(); i++) {
			columnsName[i] = arrDescribeTableVO.get(i).getColumn_name();
		}
		model.setColumnIdentifiers(columnsName);

		Object[] rowData = new Object[arrDescribeTableVO.size()];
		ArrayList<CarVO> arrCarVO = dbConnect.selectCar(condition);
		for (int i = 0; i < arrCarVO.size(); i++) {
			rowData[0] = arrCarVO.get(i).getCarId();
			rowData[1] = arrCarVO.get(i).getCarName();
			rowData[2] = arrCarVO.get(i).getCarNumber();
			rowData[3] = arrCarVO.get(i).getCarSize();
			rowData[4] = arrCarVO.get(i).getCarImg();
			rowData[5] = arrCarVO.get(i).getCarInfo();
			rowData[6] = arrCarVO.get(i).getCarCharge();
			rowData[7] = arrCarVO.get(i).getCarCompany();
			rowData[8] = arrCarVO.get(i).getCarRegidate();
			model.addRow(rowData);
		}

		jTable.setModel(model);
		jTable.setPreferredScrollableViewportSize(jTable.getPreferredSize());
		jTable.setFillsViewportHeight(true);
		jTable.getTableHeader().setFont(new Font("Verdana", Font.PLAIN, 17));
		jTable.setFont(new Font("Verdana", Font.PLAIN, 17));
		jTable.setRowHeight(20);
		jTable.getTableHeader().setReorderingAllowed(false);

		jScollPane = new JScrollPane(jTable);
		jScollPane.setBounds(0, 0, 1000, 200);
		jScollPane.setBorder(BorderFactory.createTitledBorder("COMPANY" + condition + "'s Car Info"));
		jPanel.add(jScollPane);

		return jPanel;
	}

	private JPanel getCompanyTable(String condition) {
		JPanel jPanel = new JPanel();
		jPanel.setBounds(0, 0, 1000, 500);
		jPanel.setBackground(new Color(245, 245, 245));
		jPanel.setLayout(null);

		Font Vdn17 = new Font("Verdana", Font.PLAIN, 17);
		Font Vdn15 = new Font("Verdana", Font.PLAIN, 15);

		JPanel funcPanel = new JPanel();
		funcPanel.setBounds(0, 0, 1000, 100);
		funcPanel.setBackground(new Color(245, 245, 245));
		funcPanel.setLayout(null);
		jPanel.add(funcPanel);

		/******* SELECT ********/
		JPanel selPanel = new JPanel();
		selPanel.setBounds(0, 0, 300, 100);
		selPanel.setBorder(BorderFactory.createTitledBorder("SELECT"));
		selPanel.setLayout(null);
		funcPanel.add(selPanel);

		JLabel lbWhere = new JLabel("Where : ");
		lbWhere.setBounds(10, 20, 80, 30);
		lbWhere.setFont(Vdn15);
		selPanel.add(lbWhere);

		txtComWhere = new JTextField();
		txtComWhere.setBounds(90, 20, 200, 30);
		txtComWhere.setFont(Vdn15);
		selPanel.add(txtComWhere);

		// btnSelect
		JButton btnSelect = new JButton("Select");
		btnSelect.setBounds(90, 60, 100, 30);
		btnSelect.setFont(Vdn17);
		btnSelect.addActionListener(btnL);
		selPanel.add(btnSelect);

		/******* DELETE ********/
		JPanel delPanel = new JPanel();
		delPanel.setBounds(300, 0, 300, 100);
		delPanel.setBorder(BorderFactory.createTitledBorder("DELETE"));
		delPanel.setLayout(null);
		funcPanel.add(delPanel);

		JLabel lbName = new JLabel("ID : ");
		lbName.setBounds(10, 20, 80, 30);
		lbName.setFont(Vdn15);
		delPanel.add(lbName);

		txtComName = new JTextField();
		txtComName.setBounds(90, 20, 200, 30);
		txtComName.setFont(Vdn15);
		delPanel.add(txtComName);

		JButton btnDelete = new JButton("Delete");
		btnDelete.setBounds(90, 60, 100, 30);
		btnDelete.setFont(Vdn17);
		btnDelete.addActionListener(btnL);
		delPanel.add(btnDelete);

		/******* UPDATE ********/
		JPanel updatePanel = new JPanel();
		updatePanel.setBounds(600, 0, 400, 100);
		updatePanel.setBorder(BorderFactory.createTitledBorder("UPDATE Manager_Name"));
		updatePanel.setLayout(null);
		funcPanel.add(updatePanel);

		JLabel lbFrom = new JLabel("Enter Id: ");
		lbFrom.setBounds(10, 20, 120, 30);
		lbFrom.setFont(Vdn17);
		updatePanel.add(lbFrom);

		txtComFrom = new JTextField();
		txtComFrom.setBounds(120, 20, 150, 30);
		txtComFrom.setFont(Vdn17);
		updatePanel.add(txtComFrom);

		JLabel lbTo = new JLabel("New Name: ");
		lbTo.setBounds(10, 60, 120, 30);
		lbTo.setFont(Vdn17);
		updatePanel.add(lbTo);

		txtComTo = new JTextField();
		txtComTo.setBounds(120, 60, 150, 30);
		txtComTo.setFont(Vdn17);
		updatePanel.add(txtComTo);

		JButton btnUpdate = new JButton("Update");
		btnUpdate.setBounds(290, 35, 100, 30);
		btnUpdate.setFont(Vdn17);
		btnUpdate.addActionListener(btnL);
		updatePanel.add(btnUpdate);

		/******* INSERT ********/

		JPanel attPanel = new JPanel();
		attPanel.setBounds(0, 110, 1000, 100);
		attPanel.setBackground(new Color(245, 245, 245));
		attPanel.setLayout(new GridLayout(2, 6));
		attPanel.setBorder(BorderFactory.createTitledBorder("INSERT"));
		jPanel.add(attPanel);

		JLabel[] lbAttArray = new JLabel[6];
		String[] strAtt = { "ID", "Name", "Addr", "Call", "ManName", "ManEmail" };
		for (int i = 0; i < 6; i++) {
			lbAttArray[i] = new JLabel(strAtt[i]);
			lbAttArray[i].setFont(Vdn15);
			lbAttArray[i].setHorizontalAlignment(SwingConstants.CENTER);
			attPanel.add(lbAttArray[i]);
		}

		txtAttArray = new JTextField[6];
		for (int i = 0; i < 6; i++) {
			txtAttArray[i] = new JTextField();
			attPanel.add(txtAttArray[i]);
		}
		// btnInsert
		JButton btnInsert = new JButton("Insert");
		btnInsert.setBounds(450, 210, 100, 30);
		btnInsert.setFont(Vdn17);
		btnInsert.addActionListener(btnL);
		jPanel.add(btnInsert);

		JTable jTable = new JTable();
		DefaultTableModel model = new DefaultTableModel();

		ArrayList<DescribeTableVO> arrDescribeTableVO = dbConnect.getTableDescription("COMPANY");
		Object[] columnsName = new Object[arrDescribeTableVO.size()];
		for (int i = 0; i < arrDescribeTableVO.size(); i++) {
			columnsName[i] = arrDescribeTableVO.get(i).getColumn_name();
		}
		model.setColumnIdentifiers(columnsName);

		Object[] rowData = new Object[arrDescribeTableVO.size()];
		ArrayList<CompanyVO> arrCompanyVO = dbConnect.selectCompany(condition);
		for (int i = 0; i < arrCompanyVO.size(); i++) {
			rowData[0] = arrCompanyVO.get(i).getComId();
			rowData[1] = arrCompanyVO.get(i).getComName();
			rowData[2] = arrCompanyVO.get(i).getComAddr();
			rowData[3] = arrCompanyVO.get(i).getComCall();
			rowData[4] = arrCompanyVO.get(i).getManName();
			rowData[5] = arrCompanyVO.get(i).getManEmail();

			model.addRow(rowData);
		}

		jTable.setModel(model);
		jTable.setPreferredScrollableViewportSize(jTable.getPreferredSize());
		jTable.setFillsViewportHeight(true);
		jTable.getTableHeader().setFont(Vdn17);
		jTable.setFont(Vdn17);
		jTable.setRowHeight(20);
		jTable.getTableHeader().setReorderingAllowed(false);

		jScollPane = new JScrollPane(jTable);
		// jScollPane.setPreferredSize(new Dimension(650, 150));
		jScollPane.setBounds(0, 240, 1000, 250);
		jScollPane.setBorder(BorderFactory.createTitledBorder("COMPANY"));
		// jScollPane.getVerticalScrollBar().setValue(jScollPane.getVerticalScrollBar().getMaximum());

		jPanel.add(jScollPane);

		return jPanel;
	}

	private JPanel getCarTable(String condition) {
		JPanel jPanel = new JPanel();
		jPanel.setBounds(0, 0, 1000, 500);
		jPanel.setBackground(new Color(245, 245, 245));
		jPanel.setLayout(null);

		Font Vdn17 = new Font("Verdana", Font.PLAIN, 17);
		Font Vdn15 = new Font("Verdana", Font.PLAIN, 15);

		JPanel funcPanel = new JPanel();
		funcPanel.setBounds(0, 0, 1000, 100);
		funcPanel.setBackground(new Color(245, 245, 245));
		funcPanel.setLayout(null);
		jPanel.add(funcPanel);

		/******* SELECT ********/
		JPanel selPanel = new JPanel();
		selPanel.setBounds(0, 0, 300, 100);
		selPanel.setBorder(BorderFactory.createTitledBorder("SELECT"));
		selPanel.setLayout(null);
		funcPanel.add(selPanel);

		JLabel lbWhere = new JLabel("Where : ");
		lbWhere.setBounds(10, 20, 80, 30);
		lbWhere.setFont(Vdn15);
		selPanel.add(lbWhere);

		txtComWhere = new JTextField();
		txtComWhere.setBounds(90, 20, 200, 30);
		txtComWhere.setFont(Vdn15);
		selPanel.add(txtComWhere);

		// btnSelect
		JButton btnSelect = new JButton("Select");
		btnSelect.setBounds(90, 60, 100, 30);
		btnSelect.setFont(Vdn17);
		btnSelect.addActionListener(btnL);
		selPanel.add(btnSelect);

		/******* DELETE ********/
		JPanel delPanel = new JPanel();
		delPanel.setBounds(300, 0, 300, 100);
		delPanel.setBorder(BorderFactory.createTitledBorder("DELETE"));
		delPanel.setLayout(null);
		funcPanel.add(delPanel);

		JLabel lbName = new JLabel("ID : ");
		lbName.setBounds(10, 20, 80, 30);
		lbName.setFont(Vdn15);
		delPanel.add(lbName);

		txtComName = new JTextField();
		txtComName.setBounds(90, 20, 200, 30);
		txtComName.setFont(Vdn15);
		delPanel.add(txtComName);

		JButton btnDelete = new JButton("Delete");
		btnDelete.setBounds(90, 60, 100, 30);
		btnDelete.setFont(Vdn17);
		btnDelete.addActionListener(btnL);
		delPanel.add(btnDelete);

		/******* UPDATE ********/
		JPanel updatePanel = new JPanel();
		updatePanel.setBounds(600, 0, 400, 100);
		updatePanel.setBorder(BorderFactory.createTitledBorder("UPDATE Car_Charge"));
		updatePanel.setLayout(null);
		funcPanel.add(updatePanel);

		JLabel lbFrom = new JLabel("Enter Id: ");
		lbFrom.setBounds(10, 20, 120, 30);
		lbFrom.setFont(Vdn17);
		updatePanel.add(lbFrom);

		txtComFrom = new JTextField();
		txtComFrom.setBounds(120, 20, 150, 30);
		txtComFrom.setFont(Vdn17);
		updatePanel.add(txtComFrom);

		JLabel lbTo = new JLabel("New Charge: ");
		lbTo.setBounds(10, 60, 120, 30);
		lbTo.setFont(Vdn17);
		updatePanel.add(lbTo);

		txtComTo = new JTextField();
		txtComTo.setBounds(120, 60, 150, 30);
		txtComTo.setFont(Vdn17);
		updatePanel.add(txtComTo);

		JButton btnUpdate = new JButton("Update");
		btnUpdate.setBounds(290, 35, 100, 30);
		btnUpdate.setFont(Vdn17);
		btnUpdate.addActionListener(btnL);
		updatePanel.add(btnUpdate);

		/******* INSERT ********/

		JPanel attPanel = new JPanel();
		attPanel.setBounds(0, 110, 1000, 100);
		attPanel.setBackground(new Color(245, 245, 245));
		attPanel.setLayout(new GridLayout(2, 9));
		attPanel.setBorder(BorderFactory.createTitledBorder("INSERT"));
		jPanel.add(attPanel);

		JLabel[] lbAttArray = new JLabel[9];
		String[] strAtt = { "ID", "Name", "Number", "Size", "Img", "Info", "Charge", "Company", "Regidate" };
		for (int i = 0; i < 9; i++) {
			lbAttArray[i] = new JLabel(strAtt[i]);
			lbAttArray[i].setFont(new Font("Verdana", Font.PLAIN, 15));
			lbAttArray[i].setHorizontalAlignment(SwingConstants.CENTER);
			attPanel.add(lbAttArray[i]);
		}

		txtAttArray = new JTextField[9];

		for (int i = 0; i < 9; i++) {
			txtAttArray[i] = new JTextField();
			attPanel.add(txtAttArray[i]);
		}

		// btnInsert
		JButton btnInsert = new JButton("Insert");
		btnInsert.setBounds(450, 210, 100, 30);
		btnInsert.setFont(Vdn17);
		btnInsert.addActionListener(btnL);
		jPanel.add(btnInsert);

		JTable jTable = new JTable();
		DefaultTableModel model = new DefaultTableModel();

		ArrayList<DescribeTableVO> arrDescribeTableVO = dbConnect.getTableDescription("CAR");
		Object[] columnsName = new Object[arrDescribeTableVO.size()];
		for (int i = 0; i < arrDescribeTableVO.size(); i++) {
			columnsName[i] = arrDescribeTableVO.get(i).getColumn_name();
		}
		model.setColumnIdentifiers(columnsName);

		Object[] rowData = new Object[arrDescribeTableVO.size()];
		ArrayList<CarVO> arrCarVO = dbConnect.selectCar(condition);
		for (int i = 0; i < arrCarVO.size(); i++) {
			rowData[0] = arrCarVO.get(i).getCarId();
			rowData[1] = arrCarVO.get(i).getCarName();
			rowData[2] = arrCarVO.get(i).getCarNumber();
			rowData[3] = arrCarVO.get(i).getCarSize();
			rowData[4] = arrCarVO.get(i).getCarImg();
			rowData[5] = arrCarVO.get(i).getCarInfo();
			rowData[6] = arrCarVO.get(i).getCarCharge();
			rowData[7] = arrCarVO.get(i).getCarCompany();
			rowData[8] = arrCarVO.get(i).getCarRegidate();
			model.addRow(rowData);
		}

		jTable.setModel(model);
		jTable.setPreferredScrollableViewportSize(jTable.getPreferredSize());
		jTable.setFillsViewportHeight(true);
		jTable.getTableHeader().setFont(new Font("Verdana", Font.PLAIN, 17));
		jTable.setFont(new Font("Verdana", Font.PLAIN, 17));
		jTable.setRowHeight(20);
		jTable.getTableHeader().setReorderingAllowed(false);

		jScollPane = new JScrollPane(jTable);
		// jScollPane.setPreferredSize(new Dimension(650, 150));
		jScollPane.setBounds(0, 240, 1000, 250);
		jScollPane.setBorder(BorderFactory.createTitledBorder("CAR"));
		// jScollPane.getVerticalScrollBar().setValue(jScollPane.getVerticalScrollBar().getMaximum());

		jPanel.add(jScollPane);

		return jPanel;
	}

	private JPanel getClientTable(String condition) {
		JPanel jPanel = new JPanel();
		jPanel.setBounds(0, 0, 1000, 500);
		jPanel.setBackground(new Color(245, 245, 245));
		jPanel.setLayout(null);

		Font Vdn17 = new Font("Verdana", Font.PLAIN, 17);
		Font Vdn15 = new Font("Verdana", Font.PLAIN, 15);

		JPanel funcPanel = new JPanel();
		funcPanel.setBounds(0, 0, 1000, 100);
		funcPanel.setBackground(new Color(245, 245, 245));
		funcPanel.setLayout(null);
		jPanel.add(funcPanel);

		/******* SELECT ********/
		JPanel selPanel = new JPanel();
		selPanel.setBounds(0, 0, 300, 100);
		selPanel.setBorder(BorderFactory.createTitledBorder("SELECT"));
		selPanel.setLayout(null);
		funcPanel.add(selPanel);

		JLabel lbWhere = new JLabel("Where : ");
		lbWhere.setBounds(10, 20, 80, 30);
		lbWhere.setFont(Vdn15);
		selPanel.add(lbWhere);

		txtComWhere = new JTextField();
		txtComWhere.setBounds(90, 20, 200, 30);
		txtComWhere.setFont(Vdn15);
		selPanel.add(txtComWhere);

		// btnSelect
		JButton btnSelect = new JButton("Select");
		btnSelect.setBounds(90, 60, 100, 30);
		btnSelect.setFont(Vdn17);
		btnSelect.addActionListener(btnL);
		selPanel.add(btnSelect);

		/******* DELETE ********/
		JPanel delPanel = new JPanel();
		delPanel.setBounds(300, 0, 300, 100);
		delPanel.setBorder(BorderFactory.createTitledBorder("DELETE"));
		delPanel.setLayout(null);
		funcPanel.add(delPanel);

		JLabel lbName = new JLabel("Num : ");
		lbName.setBounds(10, 20, 80, 30);
		lbName.setFont(Vdn15);
		delPanel.add(lbName);

		txtComName = new JTextField();
		txtComName.setBounds(90, 20, 200, 30);
		txtComName.setFont(Vdn15);
		delPanel.add(txtComName);

		JButton btnDelete = new JButton("Delete");
		btnDelete.setBounds(90, 60, 100, 30);
		btnDelete.setFont(Vdn17);
		btnDelete.addActionListener(btnL);
		delPanel.add(btnDelete);

		/******* UPDATE ********/
		JPanel updatePanel = new JPanel();
		updatePanel.setBounds(600, 0, 400, 100);
		updatePanel.setBorder(BorderFactory.createTitledBorder("UPDATE Client_Name"));
		updatePanel.setLayout(null);
		funcPanel.add(updatePanel);

		JLabel lbFrom = new JLabel("Enter Num: ");
		lbFrom.setBounds(10, 20, 120, 30);
		lbFrom.setFont(Vdn17);
		updatePanel.add(lbFrom);

		txtComFrom = new JTextField();
		txtComFrom.setBounds(120, 20, 150, 30);
		txtComFrom.setFont(Vdn17);
		updatePanel.add(txtComFrom);

		JLabel lbTo = new JLabel("New Name: ");
		lbTo.setBounds(10, 60, 120, 30);
		lbTo.setFont(Vdn17);
		updatePanel.add(lbTo);

		txtComTo = new JTextField();
		txtComTo.setBounds(120, 60, 150, 30);
		txtComTo.setFont(Vdn17);
		updatePanel.add(txtComTo);

		JButton btnUpdate = new JButton("Update");
		btnUpdate.setBounds(290, 35, 100, 30);
		btnUpdate.setFont(Vdn17);
		btnUpdate.addActionListener(btnL);
		updatePanel.add(btnUpdate);

		/******* INSERT ********/

		JPanel attPanel = new JPanel();
		attPanel.setBounds(0, 110, 1000, 100);
		attPanel.setBackground(new Color(245, 245, 245));
		attPanel.setLayout(new GridLayout(2, 7));
		attPanel.setBorder(BorderFactory.createTitledBorder("INSERT"));
		jPanel.add(attPanel);

		JLabel[] lbAttArray = new JLabel[7];
		String[] strAtt = { "Num", "Name", "Addr", "Call", "Email", "PreDate", "PreCarname" };
		for (int i = 0; i < 7; i++) {
			lbAttArray[i] = new JLabel(strAtt[i]);
			lbAttArray[i].setFont(new Font("Verdana", Font.PLAIN, 15));
			lbAttArray[i].setHorizontalAlignment(SwingConstants.CENTER);
			attPanel.add(lbAttArray[i]);
		}

		txtAttArray = new JTextField[7];

		for (int i = 0; i < 7; i++) {
			txtAttArray[i] = new JTextField();
			attPanel.add(txtAttArray[i]);
		}

		// btnInsert
		JButton btnInsert = new JButton("Insert");
		btnInsert.setBounds(450, 210, 100, 30);
		btnInsert.setFont(Vdn17);
		btnInsert.addActionListener(btnL);
		jPanel.add(btnInsert);

		JTable jTable = new JTable();
		DefaultTableModel model = new DefaultTableModel();

		ArrayList<DescribeTableVO> arrDescribeTableVO = dbConnect.getTableDescription("CLIENT");
		Object[] columnsName = new Object[arrDescribeTableVO.size()];
		for (int i = 0; i < arrDescribeTableVO.size(); i++) {
			columnsName[i] = arrDescribeTableVO.get(i).getColumn_name();
		}
		model.setColumnIdentifiers(columnsName);

		Object[] rowData = new Object[arrDescribeTableVO.size()];
		ArrayList<ClientVO> arrClientVO = dbConnect.selectClient(condition);
		for (int i = 0; i < arrClientVO.size(); i++) {
			rowData[0] = arrClientVO.get(i).getCltNum();
			rowData[1] = arrClientVO.get(i).getCltName();
			rowData[2] = arrClientVO.get(i).getCltAddr();
			rowData[3] = arrClientVO.get(i).getCltCall();
			rowData[4] = arrClientVO.get(i).getCltEmail();
			rowData[5] = arrClientVO.get(i).getPreDate();
			rowData[6] = arrClientVO.get(i).getPreCarname();

			model.addRow(rowData);
		}

		jTable.setModel(model);
		jTable.setPreferredScrollableViewportSize(jTable.getPreferredSize());
		jTable.setFillsViewportHeight(true);
		jTable.getTableHeader().setFont(new Font("Verdana", Font.PLAIN, 17));
		jTable.setFont(new Font("Verdana", Font.PLAIN, 17));
		jTable.setRowHeight(20);
		jTable.getTableHeader().setReorderingAllowed(false);

		jScollPane = new JScrollPane(jTable);
		// jScollPane.setPreferredSize(new Dimension(650, 150));
		jScollPane.setBounds(0, 240, 1000, 250);
		jScollPane.setBorder(BorderFactory.createTitledBorder("CLIENT"));
		// jScollPane.getVerticalScrollBar().setValue(jScollPane.getVerticalScrollBar().getMaximum());

		jPanel.add(jScollPane);

		return jPanel;
	}

	private JPanel getRentTable(String condition) {
		JPanel jPanel = new JPanel();
		jPanel.setBounds(0, 0, 1000, 500);
		jPanel.setBackground(new Color(245, 245, 245));
		jPanel.setLayout(null);

		Font Vdn17 = new Font("Verdana", Font.PLAIN, 17);
		Font Vdn15 = new Font("Verdana", Font.PLAIN, 15);

		JPanel funcPanel = new JPanel();
		funcPanel.setBounds(0, 0, 1000, 100);
		funcPanel.setBackground(new Color(245, 245, 245));
		funcPanel.setLayout(null);
		jPanel.add(funcPanel);

		/******* SELECT ********/
		JPanel selPanel = new JPanel();
		selPanel.setBounds(0, 0, 300, 100);
		selPanel.setBorder(BorderFactory.createTitledBorder("SELECT"));
		selPanel.setLayout(null);
		funcPanel.add(selPanel);

		JLabel lbWhere = new JLabel("Where : ");
		lbWhere.setBounds(10, 20, 80, 30);
		lbWhere.setFont(Vdn15);
		selPanel.add(lbWhere);

		txtComWhere = new JTextField();
		txtComWhere.setBounds(90, 20, 200, 30);
		txtComWhere.setFont(Vdn15);
		selPanel.add(txtComWhere);

		// btnSelect
		JButton btnSelect = new JButton("Select");
		btnSelect.setBounds(90, 60, 100, 30);
		btnSelect.setFont(Vdn17);
		btnSelect.addActionListener(btnL);
		selPanel.add(btnSelect);

		/******* DELETE ********/
		JPanel delPanel = new JPanel();
		delPanel.setBounds(300, 0, 300, 100);
		delPanel.setBorder(BorderFactory.createTitledBorder("DELETE"));
		delPanel.setLayout(null);
		funcPanel.add(delPanel);

		JLabel lbName = new JLabel("ID : ");
		lbName.setBounds(10, 20, 80, 30);
		lbName.setFont(Vdn15);
		delPanel.add(lbName);

		txtComName = new JTextField();
		txtComName.setBounds(90, 20, 200, 30);
		txtComName.setFont(Vdn15);
		delPanel.add(txtComName);

		JButton btnDelete = new JButton("Delete");
		btnDelete.setBounds(90, 60, 100, 30);
		btnDelete.setFont(Vdn17);
		btnDelete.addActionListener(btnL);
		delPanel.add(btnDelete);

		/******* UPDATE ********/
		JPanel updatePanel = new JPanel();
		updatePanel.setBounds(600, 0, 400, 100);
		updatePanel.setBorder(BorderFactory.createTitledBorder("UPDATE Rent_StartDate"));
		updatePanel.setLayout(null);
		funcPanel.add(updatePanel);

		JLabel lbFrom = new JLabel("Enter Id: ");
		lbFrom.setBounds(10, 20, 120, 30);
		lbFrom.setFont(Vdn17);
		updatePanel.add(lbFrom);

		txtComFrom = new JTextField();
		txtComFrom.setBounds(120, 20, 150, 30);
		txtComFrom.setFont(Vdn17);
		updatePanel.add(txtComFrom);

		JLabel lbTo = new JLabel("New Date: ");
		lbTo.setBounds(10, 60, 120, 30);
		lbTo.setFont(Vdn17);
		updatePanel.add(lbTo);

		txtComTo = new JTextField();
		txtComTo.setBounds(120, 60, 150, 30);
		txtComTo.setFont(Vdn17);
		updatePanel.add(txtComTo);

		JButton btnUpdate = new JButton("Update");
		btnUpdate.setBounds(290, 35, 100, 30);
		btnUpdate.setFont(Vdn17);
		btnUpdate.addActionListener(btnL);
		updatePanel.add(btnUpdate);

		/******* INSERT ********/

		JPanel attPanel = new JPanel();
		attPanel.setBounds(0, 110, 1000, 100);
		attPanel.setBackground(new Color(245, 245, 245));
		attPanel.setLayout(new GridLayout(2, 10));
		attPanel.setBorder(BorderFactory.createTitledBorder("INSERT"));
		jPanel.add(attPanel);

		JLabel[] lbAttArray = new JLabel[10];
		String[] strAtt = { "rent_Id", "rent_Car", "rent_Client", "rent_Com", "rent_Stdate", "rent_Peroid", "rent_Due",
				"rent_Fee", "rent_Efc", "rent_Efcfee" };
		for (int i = 0; i < 10; i++) {
			lbAttArray[i] = new JLabel(strAtt[i]);
			lbAttArray[i].setFont(new Font("Verdana", Font.PLAIN, 15));
			lbAttArray[i].setHorizontalAlignment(SwingConstants.CENTER);
			attPanel.add(lbAttArray[i]);
		}

		txtAttArray = new JTextField[10];

		for (int i = 0; i < 10; i++) {
			txtAttArray[i] = new JTextField();
			attPanel.add(txtAttArray[i]);
		}

		// btnInsert
		JButton btnInsert = new JButton("Insert");
		btnInsert.setBounds(450, 210, 100, 30);
		btnInsert.setFont(Vdn17);
		btnInsert.addActionListener(btnL);
		jPanel.add(btnInsert);

		JTable jTable = new JTable();
		DefaultTableModel model = new DefaultTableModel();

		ArrayList<DescribeTableVO> arrDescribeTableVO = dbConnect.getTableDescription("RENT");
		Object[] columnsName = new Object[arrDescribeTableVO.size()];
		for (int i = 0; i < arrDescribeTableVO.size(); i++) {
			columnsName[i] = arrDescribeTableVO.get(i).getColumn_name();
		}
		model.setColumnIdentifiers(columnsName);

		Object[] rowData = new Object[arrDescribeTableVO.size()];
		ArrayList<RentVO> arrRentVO = dbConnect.selectRent(condition);
		for (int i = 0; i < arrRentVO.size(); i++) {
			rowData[0] = arrRentVO.get(i).getRent_Id();
			rowData[1] = arrRentVO.get(i).getRent_Car();
			rowData[2] = arrRentVO.get(i).getRent_Client();
			rowData[3] = arrRentVO.get(i).getRent_Com();
			rowData[4] = arrRentVO.get(i).getRent_Stdate();
			rowData[5] = arrRentVO.get(i).getRent_Period();
			rowData[6] = arrRentVO.get(i).getRent_Fee();
			rowData[7] = arrRentVO.get(i).getRent_Due();
			rowData[8] = arrRentVO.get(i).getRent_Efc();
			rowData[9] = arrRentVO.get(i).getRent_Efcfee();
			model.addRow(rowData);
		}

		jTable.setModel(model);
		jTable.setPreferredScrollableViewportSize(jTable.getPreferredSize());
		jTable.setFillsViewportHeight(true);
		jTable.getTableHeader().setFont(new Font("Verdana", Font.PLAIN, 17));
		jTable.setFont(new Font("Verdana", Font.PLAIN, 17));
		jTable.setRowHeight(20);
		jTable.getTableHeader().setReorderingAllowed(false);

		jScollPane = new JScrollPane(jTable);
		// jScollPane.setPreferredSize(new Dimension(650, 150));
		jScollPane.setBounds(0, 240, 1000, 250);
		jScollPane.setBorder(BorderFactory.createTitledBorder("RENT"));
		// jScollPane.getVerticalScrollBar().setValue(jScollPane.getVerticalScrollBar().getMaximum());

		jPanel.add(jScollPane);

		return jPanel;
	}

	private JPanel getRPshopTable(String condition) {
		JPanel jPanel = new JPanel();
		jPanel.setBounds(0, 0, 1000, 500);
		jPanel.setBackground(new Color(245, 245, 245));
		jPanel.setLayout(null);

		Font Vdn17 = new Font("Verdana", Font.PLAIN, 17);
		Font Vdn15 = new Font("Verdana", Font.PLAIN, 15);

		JPanel funcPanel = new JPanel();
		funcPanel.setBounds(0, 0, 1000, 100);
		funcPanel.setBackground(new Color(245, 245, 245));
		funcPanel.setLayout(null);
		jPanel.add(funcPanel);

		/******* SELECT ********/
		JPanel selPanel = new JPanel();
		selPanel.setBounds(0, 0, 300, 100);
		selPanel.setBorder(BorderFactory.createTitledBorder("SELECT"));
		selPanel.setLayout(null);
		funcPanel.add(selPanel);

		JLabel lbWhere = new JLabel("Where : ");
		lbWhere.setBounds(10, 20, 80, 30);
		lbWhere.setFont(Vdn15);
		selPanel.add(lbWhere);

		txtComWhere = new JTextField();
		txtComWhere.setBounds(90, 20, 200, 30);
		txtComWhere.setFont(Vdn15);
		selPanel.add(txtComWhere);

		// btnSelect
		JButton btnSelect = new JButton("Select");
		btnSelect.setBounds(90, 60, 100, 30);
		btnSelect.setFont(Vdn17);
		btnSelect.addActionListener(btnL);
		selPanel.add(btnSelect);

		/******* DELETE ********/
		JPanel delPanel = new JPanel();
		delPanel.setBounds(300, 0, 300, 100);
		delPanel.setBorder(BorderFactory.createTitledBorder("DELETE"));
		delPanel.setLayout(null);
		funcPanel.add(delPanel);

		JLabel lbName = new JLabel("ID : ");
		lbName.setBounds(10, 20, 80, 30);
		lbName.setFont(Vdn15);
		delPanel.add(lbName);

		txtComName = new JTextField();
		txtComName.setBounds(90, 20, 200, 30);
		txtComName.setFont(Vdn15);
		delPanel.add(txtComName);

		JButton btnDelete = new JButton("Delete");
		btnDelete.setBounds(90, 60, 100, 30);
		btnDelete.setFont(Vdn17);
		btnDelete.addActionListener(btnL);
		delPanel.add(btnDelete);

		/******* UPDATE ********/
		JPanel updatePanel = new JPanel();
		updatePanel.setBounds(600, 0, 400, 100);
		updatePanel.setBorder(BorderFactory.createTitledBorder("UPDATE RPShop_ManagerName"));
		updatePanel.setLayout(null);
		funcPanel.add(updatePanel);

		JLabel lbFrom = new JLabel("Enter Id: ");
		lbFrom.setBounds(10, 20, 120, 30);
		lbFrom.setFont(Vdn17);
		updatePanel.add(lbFrom);

		txtComFrom = new JTextField();
		txtComFrom.setBounds(120, 20, 150, 30);
		txtComFrom.setFont(Vdn17);
		updatePanel.add(txtComFrom);

		JLabel lbTo = new JLabel("New MName: ");
		lbTo.setBounds(10, 60, 120, 30);
		lbTo.setFont(Vdn15);
		updatePanel.add(lbTo);

		txtComTo = new JTextField();
		txtComTo.setBounds(120, 60, 150, 30);
		txtComTo.setFont(Vdn17);
		updatePanel.add(txtComTo);

		JButton btnUpdate = new JButton("Update");
		btnUpdate.setBounds(290, 35, 100, 30);
		btnUpdate.setFont(Vdn17);
		btnUpdate.addActionListener(btnL);
		updatePanel.add(btnUpdate);

		/******* INSERT ********/

		JPanel attPanel = new JPanel();
		attPanel.setBounds(0, 110, 1000, 100);
		attPanel.setBackground(new Color(245, 245, 245));
		attPanel.setLayout(new GridLayout(2, 6));
		attPanel.setBorder(BorderFactory.createTitledBorder("INSERT"));
		jPanel.add(attPanel);

		JLabel[] lbAttArray = new JLabel[6];
		String[] strAtt = { "rs_Id", "rs_Name", "rs_Addr", "rs_Call", "rs_Man", "rs_ManEm" };
		for (int i = 0; i < 6; i++) {
			lbAttArray[i] = new JLabel(strAtt[i]);
			lbAttArray[i].setFont(new Font("Verdana", Font.PLAIN, 15));
			lbAttArray[i].setHorizontalAlignment(SwingConstants.CENTER);
			attPanel.add(lbAttArray[i]);
		}

		txtAttArray = new JTextField[6];

		for (int i = 0; i < 6; i++) {
			txtAttArray[i] = new JTextField();
			attPanel.add(txtAttArray[i]);
		}

		// btnInsert
		JButton btnInsert = new JButton("Insert");
		btnInsert.setBounds(450, 210, 100, 30);
		btnInsert.setFont(Vdn17);
		btnInsert.addActionListener(btnL);
		jPanel.add(btnInsert);

		JTable jTable = new JTable();
		DefaultTableModel model = new DefaultTableModel();

		ArrayList<DescribeTableVO> arrDescribeTableVO = dbConnect.getTableDescription("RPSHOP");
		Object[] columnsName = new Object[arrDescribeTableVO.size()];
		for (int i = 0; i < arrDescribeTableVO.size(); i++) {
			columnsName[i] = arrDescribeTableVO.get(i).getColumn_name();
		}
		model.setColumnIdentifiers(columnsName);

		Object[] rowData = new Object[arrDescribeTableVO.size()];
		ArrayList<RPshopVO> arrRPshopVO = dbConnect.selectRPshop(condition);
		for (int i = 0; i < arrRPshopVO.size(); i++) {
			rowData[0] = arrRPshopVO.get(i).getRs_Id();
			rowData[1] = arrRPshopVO.get(i).getRs_Name();
			rowData[2] = arrRPshopVO.get(i).getRs_Addr();
			rowData[3] = arrRPshopVO.get(i).getRs_Call();
			rowData[4] = arrRPshopVO.get(i).getRs_Man();
			rowData[5] = arrRPshopVO.get(i).getRs_ManEm();
			model.addRow(rowData);
		}

		jTable.setModel(model);
		jTable.setPreferredScrollableViewportSize(jTable.getPreferredSize());
		jTable.setFillsViewportHeight(true);
		jTable.getTableHeader().setFont(new Font("Verdana", Font.PLAIN, 17));
		jTable.setFont(new Font("Verdana", Font.PLAIN, 17));
		jTable.setRowHeight(20);
		jTable.getTableHeader().setReorderingAllowed(false);

		jScollPane = new JScrollPane(jTable);
		// jScollPane.setPreferredSize(new Dimension(650, 150));
		jScollPane.setBounds(0, 240, 1000, 250);
		jScollPane.setBorder(BorderFactory.createTitledBorder("RPSHOP"));
		// jScollPane.getVerticalScrollBar().setValue(jScollPane.getVerticalScrollBar().getMaximum());

		jPanel.add(jScollPane);

		return jPanel;
	}

	private JPanel getRPTable(String condition) {
		JPanel jPanel = new JPanel();
		jPanel.setBounds(0, 0, 1000, 500);
		jPanel.setBackground(new Color(245, 245, 245));
		jPanel.setLayout(null);

		Font Vdn17 = new Font("Verdana", Font.PLAIN, 17);
		Font Vdn15 = new Font("Verdana", Font.PLAIN, 15);

		JPanel funcPanel = new JPanel();
		funcPanel.setBounds(0, 0, 1000, 100);
		funcPanel.setBackground(new Color(245, 245, 245));
		funcPanel.setLayout(null);
		jPanel.add(funcPanel);

		/******* SELECT ********/
		JPanel selPanel = new JPanel();
		selPanel.setBounds(0, 0, 300, 100);
		selPanel.setBorder(BorderFactory.createTitledBorder("SELECT"));
		selPanel.setLayout(null);
		funcPanel.add(selPanel);

		JLabel lbWhere = new JLabel("Where : ");
		lbWhere.setBounds(10, 20, 80, 30);
		lbWhere.setFont(Vdn15);
		selPanel.add(lbWhere);

		txtComWhere = new JTextField();
		txtComWhere.setBounds(90, 20, 200, 30);
		txtComWhere.setFont(Vdn15);
		selPanel.add(txtComWhere);

		// btnSelect
		JButton btnSelect = new JButton("Select");
		btnSelect.setBounds(90, 60, 100, 30);
		btnSelect.setFont(Vdn17);
		btnSelect.addActionListener(btnL);
		selPanel.add(btnSelect);

		/******* DELETE ********/
		JPanel delPanel = new JPanel();
		delPanel.setBounds(300, 0, 300, 100);
		delPanel.setBorder(BorderFactory.createTitledBorder("DELETE"));
		delPanel.setLayout(null);
		funcPanel.add(delPanel);

		JLabel lbName = new JLabel("Num : ");
		lbName.setBounds(10, 20, 80, 30);
		lbName.setFont(Vdn15);
		delPanel.add(lbName);

		txtComName = new JTextField();
		txtComName.setBounds(90, 20, 200, 30);
		txtComName.setFont(Vdn15);
		delPanel.add(txtComName);

		JButton btnDelete = new JButton("Delete");
		btnDelete.setBounds(90, 60, 100, 30);
		btnDelete.setFont(Vdn17);
		btnDelete.addActionListener(btnL);
		delPanel.add(btnDelete);

		/******* UPDATE ********/
		JPanel updatePanel = new JPanel();
		updatePanel.setBounds(600, 0, 400, 100);
		updatePanel.setBorder(BorderFactory.createTitledBorder("UPDATE Repair_Date"));
		updatePanel.setLayout(null);
		funcPanel.add(updatePanel);

		JLabel lbFrom = new JLabel("Enter Id: ");
		lbFrom.setBounds(10, 20, 120, 30);
		lbFrom.setFont(Vdn17);
		updatePanel.add(lbFrom);

		txtComFrom = new JTextField();
		txtComFrom.setBounds(120, 20, 150, 30);
		txtComFrom.setFont(Vdn17);
		updatePanel.add(txtComFrom);

		JLabel lbTo = new JLabel("New Date: ");
		lbTo.setBounds(10, 60, 120, 30);
		lbTo.setFont(Vdn17);
		updatePanel.add(lbTo);

		txtComTo = new JTextField();
		txtComTo.setBounds(120, 60, 150, 30);
		txtComTo.setFont(Vdn17);
		updatePanel.add(txtComTo);

		JButton btnUpdate = new JButton("Update");
		btnUpdate.setBounds(290, 35, 100, 30);
		btnUpdate.setFont(Vdn17);
		btnUpdate.addActionListener(btnL);
		updatePanel.add(btnUpdate);

		/******* INSERT ********/

		JPanel attPanel = new JPanel();
		attPanel.setBounds(0, 110, 1000, 100);
		attPanel.setBackground(new Color(245, 245, 245));
		attPanel.setLayout(new GridLayout(2, 10));
		attPanel.setBorder(BorderFactory.createTitledBorder("INSERT"));
		jPanel.add(attPanel);

		JLabel[] lbAttArray = new JLabel[10];
		String[] strAtt = { "rp_Num", "rp_Car", "rp_Rpshop", "rp_Com", "rp_Clt", "rp_List", "rp_Date", "rp_Fee",
				"rp_Due", "rp_Efclist" };
		for (int i = 0; i < 10; i++) {
			lbAttArray[i] = new JLabel(strAtt[i]);
			lbAttArray[i].setFont(new Font("Verdana", Font.PLAIN, 15));
			lbAttArray[i].setHorizontalAlignment(SwingConstants.CENTER);
			attPanel.add(lbAttArray[i]);
		}

		txtAttArray = new JTextField[10];

		for (int i = 0; i < 10; i++) {
			txtAttArray[i] = new JTextField();
			attPanel.add(txtAttArray[i]);
		}

		// btnInsert
		JButton btnInsert = new JButton("Insert");
		btnInsert.setBounds(450, 210, 100, 30);
		btnInsert.setFont(Vdn17);
		btnInsert.addActionListener(btnL);
		jPanel.add(btnInsert);

		JTable jTable = new JTable();
		DefaultTableModel model = new DefaultTableModel();

		ArrayList<DescribeTableVO> arrDescribeTableVO = dbConnect.getTableDescription("RP");
		Object[] columnsName = new Object[arrDescribeTableVO.size()];
		for (int i = 0; i < arrDescribeTableVO.size(); i++) {
			columnsName[i] = arrDescribeTableVO.get(i).getColumn_name();
		}
		model.setColumnIdentifiers(columnsName);

		Object[] rowData = new Object[arrDescribeTableVO.size()];
		ArrayList<RPVO> arrRPVO = dbConnect.selectRP(condition);
		for (int i = 0; i < arrRPVO.size(); i++) {
			rowData[0] = arrRPVO.get(i).getRp_Num();
			rowData[1] = arrRPVO.get(i).getRp_Car();
			rowData[2] = arrRPVO.get(i).getRp_Rpshop();
			rowData[3] = arrRPVO.get(i).getRp_Com();
			rowData[4] = arrRPVO.get(i).getRp_Clt();
			rowData[5] = arrRPVO.get(i).getRp_List();
			rowData[6] = arrRPVO.get(i).getRp_Date();
			rowData[7] = arrRPVO.get(i).getRp_Fee();
			rowData[8] = arrRPVO.get(i).getRp_Due();
			rowData[9] = arrRPVO.get(i).getRp_Efclist();
			model.addRow(rowData);
		}

		jTable.setModel(model);
		jTable.setPreferredScrollableViewportSize(jTable.getPreferredSize());
		jTable.setFillsViewportHeight(true);
		jTable.getTableHeader().setFont(new Font("Verdana", Font.PLAIN, 17));
		jTable.setFont(new Font("Verdana", Font.PLAIN, 17));
		jTable.setRowHeight(20);
		jTable.getTableHeader().setReorderingAllowed(false);

		jScollPane = new JScrollPane(jTable);
		// jScollPane.setPreferredSize(new Dimension(650, 150));
		jScollPane.setBounds(0, 240, 1000, 250);
		jScollPane.setBorder(BorderFactory.createTitledBorder("RP"));
		// jScollPane.getVerticalScrollBar().setValue(jScollPane.getVerticalScrollBar().getMaximum());

		jPanel.add(jScollPane);

		return jPanel;
	}

	private class BtnListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			JPanel temp = new JPanel();
			JButton b = (JButton) event.getSource();
			String tableName;
			String condition = "";

			String btnName = null;
			btnName = b.getText();
			switch (btnName) {
			case "MANAGER":
				manBtn.setVisible(false);
				userBtn.setVisible(false);
				cmbMenu.setVisible(true);
				btnSearch.setVisible(true);
				break;
			case "USER":
				manBtn.setVisible(false);
				userBtn.setVisible(false);

				mainPanel.removeAll();
				mainPanel.setLayout(null);

				tempPanel = null;
				tempPanel = getUserPanel("");

				mainPanel.add(tempPanel);
				add(mainPanel);

				setVisible(true);
				validate();
				repaint();
				break;
			case "FIND":
				condition = "where rent_Client = (select clt_Num from CLIENT where clt_Name = '";
				condition += txtUserId.getText() + "')";
				temp = getUserTable(condition);
				firstPanel.add(temp);
				secondPanel.setVisible(true);
				break;
			case "FIND Name":
				condition = "where car_Company = (select com_Id from COMPANY WHERE com_Name = '";
				condition += txtUserCom.getText() + "')";
				temp = getUserComTable(condition);
				secondPanel.add(temp);
				break;
			case "Search":
				mainPanel.removeAll();
				mainPanel.setLayout(null);

				tableName = strMenu[cmbMenu.getSelectedIndex()];
				condition = "";
				jTable = null;
				tempPanel = null;

				switch (tableName) {
				case "COMPANY":
					tempPanel = getCompanyTable(condition);
					break;
				case "CAR":
					tempPanel = getCarTable(condition);
					break;
				case "CLIENT":
					tempPanel = getClientTable(condition);
					break;
				case "RENT":
					tempPanel = getRentTable(condition);
					break;
				case "RPSHOP":
					tempPanel = getRPshopTable(condition);
					break;
				case "RP":
					tempPanel = getRPTable(condition);
					break;
				}

				mainPanel.add(tempPanel);
				add(mainPanel);

				setVisible(true);
				validate();
				repaint();
				break;
			case "Insert":
				mainPanel.removeAll();
				mainPanel.setLayout(null);

				tableName = strMenu[cmbMenu.getSelectedIndex()];
				jTable = null;
				tempPanel = null;

				switch (tableName) {
				case "COMPANY":
					dbConnect.insertCompany(Integer.parseInt(txtAttArray[0].getText()), txtAttArray[1].getText(),
							txtAttArray[2].getText(), txtAttArray[3].getText(), txtAttArray[4].getText(),
							txtAttArray[5].getText());
					tempPanel = getCompanyTable(condition);
					break;
				case "CAR":
					dbConnect.insertCar(Integer.parseInt(txtAttArray[0].getText()), txtAttArray[1].getText(),
							txtAttArray[2].getText(), Integer.parseInt(txtAttArray[3].getText()),
							txtAttArray[4].getText(), txtAttArray[5].getText(),
							Integer.parseInt(txtAttArray[6].getText()), Integer.parseInt(txtAttArray[7].getText()),
							txtAttArray[8].getText());
					tempPanel = getCarTable(condition);
					break;
				case "CLIENT":
					dbConnect.insertClient(txtAttArray[0].getText(), txtAttArray[1].getText(), txtAttArray[2].getText(),
							txtAttArray[3].getText(), txtAttArray[4].getText(), txtAttArray[5].getText(),
							txtAttArray[6].getText());
					tempPanel = getClientTable(condition);
					break;
				case "RENT":
					dbConnect.insertRent(txtAttArray[0].getText(), Integer.parseInt(txtAttArray[1].getText()),
							txtAttArray[2].getText(), Integer.parseInt(txtAttArray[3].getText()),
							txtAttArray[4].getText(), Integer.parseInt(txtAttArray[5].getText()),
							Integer.parseInt(txtAttArray[6].getText()), txtAttArray[7].getText(),
							txtAttArray[8].getText(), Integer.parseInt(txtAttArray[9].getText()));
					tempPanel = getRentTable(condition);
					break;
				case "RPSHOP":
					dbConnect.insertRPshop(Integer.parseInt(txtAttArray[0].getText()), txtAttArray[1].getText(),
							txtAttArray[2].getText(), txtAttArray[3].getText(), txtAttArray[4].getText(),
							txtAttArray[5].getText());
					tempPanel = getRPshopTable(condition);
					break;
				case "RP":
					dbConnect.insertRP(Integer.parseInt(txtAttArray[0].getText()),
							Integer.parseInt(txtAttArray[1].getText()), Integer.parseInt(txtAttArray[2].getText()),
							Integer.parseInt(txtAttArray[3].getText()), txtAttArray[4].getText(),
							txtAttArray[5].getText(), txtAttArray[6].getText(),
							Integer.parseInt(txtAttArray[7].getText()), txtAttArray[8].getText(),
							txtAttArray[9].getText());
					tempPanel = getRPTable(condition);
					break;
				}

				mainPanel.add(tempPanel);
				add(mainPanel);

				setVisible(true);
				validate();
				repaint();
				break;

			case "Select":
				mainPanel.removeAll();
				mainPanel.setLayout(null);

				tableName = strMenu[cmbMenu.getSelectedIndex()];
				jTable = null;
				tempPanel = null;

				switch (tableName) {
				case "COMPANY":
					condition = "where ";
					condition += txtComWhere.getText();
					tempPanel = getCompanyTable(condition);
					break;
				case "CAR":
					condition = "where ";
					condition += txtComWhere.getText();
					tempPanel = getCarTable(condition);
					break;
				case "CLIENT":
					condition = "where ";
					condition += txtComWhere.getText();
					tempPanel = getClientTable(condition);
					break;
				case "RENT":
					condition = "where ";
					condition += txtComWhere.getText();
					tempPanel = getRentTable(condition);
					break;
				case "RPSHOP":
					condition = "where ";
					condition += txtComWhere.getText();
					tempPanel = getRPshopTable(condition);
					break;
				case "RP":
					condition = "where ";
					condition += txtComWhere.getText();
					tempPanel = getRPTable(condition);
					break;
				}

				mainPanel.add(tempPanel);
				add(mainPanel);

				setVisible(true);
				validate();
				repaint();
				break;
			case "Delete":
				mainPanel.removeAll();
				mainPanel.setLayout(null);

				tableName = strMenu[cmbMenu.getSelectedIndex()];
				jTable = null;
				tempPanel = null;
				int result;

				switch (tableName) {
				case "COMPANY":
					result = JOptionPane.showConfirmDialog(null,
							"COMPANY의 " + txtComName.getText() + "번에 해당하는 정보가 삭제됩니다.\n 삭제하시겠습니까?");
					if (result == JOptionPane.YES_OPTION)
						dbConnect.deleteCompany(Integer.parseInt(txtComName.getText()));
					tempPanel = getCompanyTable(condition);
					break;
				case "CAR":
					result = JOptionPane.showConfirmDialog(null,
							"CAR의 " + txtComName.getText() + "번에 해당하는 정보가 삭제됩니다.\n 삭제하시겠습니까?");
					if (result == JOptionPane.YES_OPTION)
						dbConnect.deleteCar(Integer.parseInt(txtComName.getText()));
					tempPanel = getCarTable(condition);
					break;
				case "CLIENT":
					result = JOptionPane.showConfirmDialog(null,
							"CLIENT의 " + txtComName.getText() + "번에 해당하는 정보가 삭제됩니다.\n 삭제하시겠습니까?");
					if (result == JOptionPane.YES_OPTION)
						dbConnect.deleteClient(txtComName.getText());
					tempPanel = getClientTable(condition);
					break;
				case "RENT":
					result = JOptionPane.showConfirmDialog(null,
							"RENT의 " + txtComName.getText() + "번에 해당하는 정보가 삭제됩니다.\n 삭제하시겠습니까?");
					if (result == JOptionPane.YES_OPTION)
						dbConnect.deleteRent(txtComName.getText());
					tempPanel = getRentTable(condition);
					break;
				case "RPSHOP":
					result = JOptionPane.showConfirmDialog(null,
							"RPSHOP의 " + txtComName.getText() + "번에 해당하는 정보가 삭제됩니다.\n 삭제하시겠습니까?");
					if (result == JOptionPane.YES_OPTION)
						dbConnect.deleteRPshop(Integer.parseInt(txtComName.getText()));
					tempPanel = getRPshopTable(condition);
					break;
				case "RP":
					result = JOptionPane.showConfirmDialog(null,
							"RP의 " + txtComName.getText() + "번에 해당하는 정보가 삭제됩니다.\n 삭제하시겠습니까?");
					if (result == JOptionPane.YES_OPTION)
						dbConnect.deleteRP(Integer.parseInt(txtComName.getText()));
					tempPanel = getRPTable(condition);
					break;
				}

				mainPanel.add(tempPanel);
				add(mainPanel);

				setVisible(true);
				validate();
				repaint();
				break;
			case "Update":
				mainPanel.removeAll();
				mainPanel.setLayout(null);

				tableName = strMenu[cmbMenu.getSelectedIndex()];
				jTable = null;
				tempPanel = null;

				switch (tableName) {
				case "COMPANY":
					dbConnect.updateCompany(Integer.parseInt(txtComFrom.getText()), txtComTo.getText());
					tempPanel = getCompanyTable(condition);
					break;
				case "CAR":
					dbConnect.updateCar(Integer.parseInt(txtComFrom.getText()), Integer.parseInt(txtComTo.getText()));
					tempPanel = getCarTable(condition);
					break;
				case "CLIENT":
					dbConnect.updateClient(txtComFrom.getText(), txtComTo.getText());
					tempPanel = getClientTable(condition);
					break;
				case "RENT":
					dbConnect.updateRent(txtComFrom.getText(), txtComTo.getText());
					tempPanel = getRentTable(condition);
					break;
				case "RPSHOP":
					dbConnect.updateRPshop(Integer.parseInt(txtComFrom.getText()), txtComTo.getText());
					tempPanel = getRPshopTable(condition);
					break;
				case "RP":
					dbConnect.updateRP(Integer.parseInt(txtComFrom.getText()), txtComTo.getText());
					tempPanel = getRPTable(condition);
					break;
				}

				mainPanel.add(tempPanel);
				add(mainPanel);

				setVisible(true);
				validate();
				repaint();
				break;
			}
		}
	} // BtnListener class
} // PrimaryPanel class

// DB
class DatabaseConnect {
	private Connection conn = null;
	private Statement stmt = null;

	private static final String USERNAME = "KIM";
	private static final String PASSWORD = "shinystar";
	private static final String URL = "jdbc:oracle:thin:@localhost:1521:DBSERVER";

	// private static final String USERNAME = "s14011046";
	// private static final String PASSWORD = "s1401104620180309";
	// private static final String URL = "jdbc:oracle:thin:@localhost:1521:orcl";

	public DatabaseConnect() {
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			System.out.println("DB Connection OK!");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("DB Driver Error!");
		} catch (SQLException se) {
			se.printStackTrace();
			System.out.println("DB Connection Error!");

		}
	}

	public ArrayList<CompanyVO> selectCompany(String condition) {
		String sql = "select * from COMPANY ";
		sql += condition;
		PreparedStatement pstmt = null;
		ArrayList<CompanyVO> arrCompanyVO = new ArrayList<CompanyVO>();

		try {
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				CompanyVO tempCompanyVO = new CompanyVO(rs.getInt("com_Id"), rs.getString("com_Name"),
						rs.getString("com_Addr"), rs.getString("com_Call"), rs.getString("man_Name"),
						rs.getString("man_Email"));
				arrCompanyVO.add(tempCompanyVO);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null && !pstmt.isClosed())
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return arrCompanyVO;
	}

	public ArrayList<CarVO> selectCar(String condition) {
		String sql = "select * from CAR ";
		sql += condition;
		PreparedStatement pstmt = null;
		ArrayList<CarVO> arrCarVO = new ArrayList<CarVO>();

		try {
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				CarVO tempCarVO = new CarVO(rs.getInt("car_Id"), rs.getString("car_Name"), rs.getString("car_Number"),
						rs.getInt("car_Size"), rs.getString("car_Img"), rs.getString("car_Info"),
						rs.getInt("car_Charge"), rs.getInt("car_Company"), rs.getString("car_Regidate"));
				arrCarVO.add(tempCarVO);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null && !pstmt.isClosed())
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return arrCarVO;
	}

	public ArrayList<ClientVO> selectClient(String condition) {
		String sql = "select * from CLIENT ";
		sql += condition;
		PreparedStatement pstmt = null;
		ArrayList<ClientVO> arrClientVO = new ArrayList<ClientVO>();

		try {
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				ClientVO tempClientVO = new ClientVO(rs.getString("clt_Num"), rs.getString("clt_Name"),
						rs.getString("clt_Addr"), rs.getString("clt_Call"), rs.getString("clt_Email"),
						rs.getString("pre_Date"), rs.getString("pre_Carname"));
				arrClientVO.add(tempClientVO);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null && !pstmt.isClosed())
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return arrClientVO;
	}

	public ArrayList<RentVO> selectRent(String condition) {
		String sql = "select * from RENT ";
		sql += condition;
		PreparedStatement pstmt = null;
		ArrayList<RentVO> arrRentVO = new ArrayList<RentVO>();

		try {
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				RentVO tempRentVO = new RentVO(rs.getString("rent_Id"), rs.getInt("rent_Car"),
						rs.getString("rent_Client"), rs.getInt("rent_Com"), rs.getString("rent_Stdate"),
						rs.getInt("rent_Period"), rs.getInt("rent_Fee"), rs.getString("rent_Due"),
						rs.getString("rent_Efc"), rs.getInt("rent_Efcfee"));
				arrRentVO.add(tempRentVO);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null && !pstmt.isClosed())
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return arrRentVO;
	}

	public ArrayList<RPshopVO> selectRPshop(String condition) {
		String sql = "select * from RPSHOP ";
		sql += condition;
		PreparedStatement pstmt = null;
		ArrayList<RPshopVO> arrRPshopVO = new ArrayList<RPshopVO>();

		try {
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				RPshopVO tempRPshopVO = new RPshopVO(rs.getInt("rs_Id"), rs.getString("rs_Name"),
						rs.getString("rs_Addr"), rs.getString("rs_Call"), rs.getString("rs_Man"),
						rs.getString("rs_ManEm"));
				arrRPshopVO.add(tempRPshopVO);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null && !pstmt.isClosed())
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return arrRPshopVO;
	}

	public ArrayList<RPVO> selectRP(String condition) {
		String sql = "select * from RP ";
		sql += condition;
		PreparedStatement pstmt = null;
		ArrayList<RPVO> arrRPVO = new ArrayList<RPVO>();

		try {
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				RPVO tempRPVO = new RPVO(rs.getInt("rp_Num"), rs.getInt("rp_Car"), rs.getInt("rp_Rpshop"),
						rs.getInt("rp_Com"), rs.getString("rp_Clt"), rs.getString("rp_List"), rs.getString("rp_Date"),
						rs.getInt("rp_Fee"), rs.getString("rp_Due"), rs.getString("rp_Efclist"));
				arrRPVO.add(tempRPVO);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null && !pstmt.isClosed())
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return arrRPVO;
	}

	public ArrayList<DescribeTableVO> getTableDescription(String tableName) {
		String sql = "select COLUMN_NAME from COLS where table_name=?";
		PreparedStatement pstmt = null;
		ArrayList<DescribeTableVO> arrDescribeTableVO = new ArrayList<DescribeTableVO>();

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, tableName.toUpperCase());
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				DescribeTableVO tempDescribeTableVO = new DescribeTableVO(rs.getString("COLUMN_NAME"));
				arrDescribeTableVO.add(tempDescribeTableVO);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null && !pstmt.isClosed())
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return arrDescribeTableVO;
	}

	public void insertCompany(int comId, String comName, String comAddr, String comCall, String manName,
			String manEmail) {
		String sql = "insert into COMPANY values (?,?,?,?,?,?)";
		PreparedStatement pstmt = null;

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, comId);
			pstmt.setString(2, comName);
			pstmt.setString(3, comAddr);
			pstmt.setString(4, comCall);
			pstmt.setString(5, manName);
			pstmt.setString(6, manEmail);
			int op = pstmt.executeUpdate();
			if (op > 0)
				System.out.println("삽입 성공!");
			else
				System.out.println("삽입 실패!");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "중복되는 ID를 입력했습니다", "에러", JOptionPane.WARNING_MESSAGE);
			System.out.println("에러: 중복되는 ID를 입력했습니다");
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null && !pstmt.isClosed())
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void insertCar(int carId, String carName, String carNumber, int carSize, String carImg, String carInfo,
			int carCharge, int carCompany, String carRegidate) {
		String sql = "insert into CAR values (?,?,?,?,?,?,?,?,?)";
		PreparedStatement pstmt = null;

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, carId);
			pstmt.setString(2, carName);
			pstmt.setString(3, carNumber);
			pstmt.setInt(4, carSize);
			pstmt.setString(5, carImg);
			pstmt.setString(6, carInfo);
			pstmt.setInt(7, carCharge);
			pstmt.setInt(8, carCompany);
			pstmt.setString(9, carRegidate);
			int op = pstmt.executeUpdate();
			if (op > 0)
				System.out.println("삽입 성공!");
			else
				System.out.println("삽입 실패!");

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "올바르지 않은 값이 입력되었습니다.\n(중복되는 ID값 입력 또는  부모키가 존재하지 않음)", "에러",
					JOptionPane.WARNING_MESSAGE);
			System.out.println("에러: 중복되는 ID를 입력했습니다");
			System.out.println("무결성위배");
			// 여기서잡아냄
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null && !pstmt.isClosed())
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void insertClient(String cltNum, String cltName, String cltAddr, String cltCall, String cltEmail,
			String preDate, String preCarname) {
		String sql = "insert into CLIENT values (?,?,?,?,?,?,?)";
		PreparedStatement pstmt = null;

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, cltNum);
			pstmt.setString(2, cltName);
			pstmt.setString(3, cltAddr);
			pstmt.setString(4, cltCall);
			pstmt.setString(5, cltEmail);
			pstmt.setString(6, preDate);
			pstmt.setString(7, preCarname);
			int op = pstmt.executeUpdate();
			if (op > 0)
				System.out.println("삽입 성공!");
			else
				System.out.println("삽입 실패!");

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "중복되는 Number를 입력했습니다", "에러", JOptionPane.WARNING_MESSAGE);
			System.out.println("에러: 중복되는 Number를 입력했습니다");
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null && !pstmt.isClosed())
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void insertRent(String id, int car, String client, int com, String stdate, int period, int fee, String due,
			String efc, int efcfee) {
		String sql = "insert into RENT values (?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement pstmt = null;

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setInt(2, car);
			pstmt.setString(3, client);
			pstmt.setInt(4, com);
			pstmt.setString(5, stdate);
			pstmt.setInt(6, period);
			pstmt.setInt(7, fee);
			pstmt.setString(8, due);
			pstmt.setString(9, efc);
			pstmt.setInt(10, efcfee);
			int op = pstmt.executeUpdate();
			if (op > 0)
				System.out.println("삽입 성공!");
			else
				System.out.println("삽입 실패!");

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "올바르지 않은 값이 입력되었습니다.\n(중복되는 ID값 입력 또는  부모키가 존재하지 않음)", "에러",
					JOptionPane.WARNING_MESSAGE);
			System.out.println("에러: 중복되는 ID를 입력했습니다");
			System.out.println("무결성위배");
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null && !pstmt.isClosed())
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void insertRPshop(int id, String name, String addr, String call, String man, String manem) {
		String sql = "insert into RPSHOP values (?,?,?,?,?,?)";
		PreparedStatement pstmt = null;

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			pstmt.setString(2, name);
			pstmt.setString(3, addr);
			pstmt.setString(4, call);
			pstmt.setString(5, man);
			pstmt.setString(6, manem);
			int op = pstmt.executeUpdate();
			if (op > 0)
				System.out.println("삽입 성공!");
			else
				System.out.println("삽입 실패!");

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "중복되는 ID를 입력했습니다", "에러", JOptionPane.WARNING_MESSAGE);
			System.out.println("에러: 중복되는 ID를 입력했습니다");
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null && !pstmt.isClosed())
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void insertRP(int num, int car, int rpshop, int com, String clt, String list, String date, int fee,
			String due, String efclist) {
		String sql = "insert into RP values (?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement pstmt = null;

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			pstmt.setInt(2, car);
			pstmt.setInt(3, rpshop);
			pstmt.setInt(4, com);
			pstmt.setString(5, clt);
			pstmt.setString(6, list);
			pstmt.setString(7, date);
			pstmt.setInt(8, fee);
			pstmt.setString(9, due);
			pstmt.setString(10, efclist);
			int op = pstmt.executeUpdate();
			if (op > 0)
				System.out.println("삽입 성공!");
			else
				System.out.println("삽입 실패!");

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "올바르지 않은 값이 입력되었습니다.\n(중복되는 Number값 입력 또는  부모키가 존재하지 않음)", "에러",
					JOptionPane.WARNING_MESSAGE);
			System.out.println("에러: 중복되는 Number를 입력했습니다");
			System.out.println("무결성위배");
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null && !pstmt.isClosed())
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void deleteCompany(int id) {
		String sql = "delete from COMPANY where com_Id = ?";
		String sql1 = "delete from CAR where car_Company = ?";
		String sql2 = "delete from RENT where rent_Com = ?";
		String sql3 = "delete from RP where rp_Com = ?";
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		try {
			int result = JOptionPane.showConfirmDialog(null, "입력된 값과 관련된 모든 정보가 지워질 수 있습니다. 계속하시겠습니까?");
			if (result == JOptionPane.YES_OPTION) {
				System.out.println("삭제시도.");
				pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setInt(1, id);
				pstmt1.executeUpdate();
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setInt(1, id);
				pstmt2.executeUpdate();
				pstmt3 = conn.prepareStatement(sql3);
				pstmt3.setInt(1, id);
				pstmt3.executeUpdate();

				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, id);
				int op = pstmt.executeUpdate();
				if (op > 0)
					System.out.println("Company삭제 성공.");
				else {
					JOptionPane.showMessageDialog(null, "올바른 값을 입력하세요. 삭제할 값이 존재하지 않습니다", "에러",
							JOptionPane.WARNING_MESSAGE);
					System.out.println("에러: 삭제할 기본키가 없음");
					System.out.println("Company삭제 실패.");
				}
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "올바르지 않은 값이 입력되었습니다.\n(입력된 값을 참조하는 자식키가존재합니다.)", "에러",
					JOptionPane.WARNING_MESSAGE);

			System.out.println("에러: 해당 키를 참조하는 자식키가 존재함");
			System.out.println("무결성위배");
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null && !pstmt.isClosed())
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void deleteCar(int id) {
		String sql = "delete from CAR where car_Id = ?";
		String sql1 = "delete from RENT where rent_Car = ?";
		String sql2 = "delete from RP where rp_Car = ?";
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		try {
			int result = JOptionPane.showConfirmDialog(null, "입력된 값과 관련된 모든 정보가 지워질 수 있습니다. 계속하시겠습니까?");
			if (result == JOptionPane.YES_OPTION) {
				System.out.println("삭제시도.");
				pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setInt(1, id);
				pstmt1.executeUpdate();

				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setInt(1, id);
				pstmt2.executeUpdate();

				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, id);
				int op = pstmt.executeUpdate();
				if (op > 0)
					System.out.println("Car삭제 성공.");
				else {
					JOptionPane.showMessageDialog(null, "올바른 값을 입력하세요. 삭제할 값이 존재하지 않습니다", "에러",
							JOptionPane.WARNING_MESSAGE);
					System.out.println("에러: 삭제할 기본키가 없음");
					System.out.println("삭제 실패.");
				}
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "올바르지 않은 값이 입력되었습니다.\n(입력된 값을 참조하는 자식키가 존재합니다.)", "에러",
					JOptionPane.WARNING_MESSAGE);
			System.out.println("에러: 해당 키를 참조하는 자식키가 존재함");
			System.out.println("무결성위배");
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null && !pstmt.isClosed())
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void deleteClient(String num) {
		String sql = "delete from CLIENT where clt_Num = ?";
		String sql1 = "delete from RENT where rent_Clt";
		String sql2 = "delete from RP where rp_Clt";
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		try {
			int result = JOptionPane.showConfirmDialog(null, "입력된 값과 관련된 모든 정보가 지워질 수 있습니다. 계속하시겠습니까?");
			if (result == JOptionPane.YES_OPTION) {
				System.out.println("삭제시도.");
				pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setString(1, num);
				pstmt1.executeUpdate();

				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setString(1, num);
				pstmt2.executeUpdate();

				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, num);

				int op = pstmt.executeUpdate();
				if (op > 0)
					System.out.println("Client삭제 성공.");
				else {
					JOptionPane.showMessageDialog(null, "올바른 값을 입력하세요. 삭제할 값이 존재하지 않습니다", "에러",
							JOptionPane.WARNING_MESSAGE);
					System.out.println("에러: 삭제할 기본키가 없음");
					System.out.println("삭제 실패.");
				}
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "올바르지 않은 값이 입력되었습니다.\n(입력된 값을 참조하는 자식키가 존재합니다.)", "에러",
					JOptionPane.WARNING_MESSAGE);
			System.out.println("에러: 해당 키를 참조하는 자식키가 존재함");
			System.out.println("무결성위배");
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null && !pstmt.isClosed())
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void deleteRent(String id) {
		String sql = "delete from RENT where rent_Id = ?";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			int op = pstmt.executeUpdate();
			if (op > 0)
				System.out.println("Rent삭제 성공.");
			else {
				JOptionPane.showMessageDialog(null, "올바른 값을 입력하세요. 삭제할 값이 존재하지 않습니다", "에러",
						JOptionPane.WARNING_MESSAGE);
				System.out.println("에러: 삭제할 기본키가 없음");
				System.out.println("삭제 실패.");
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "올바르지 않은 값이 입력되었습니다.\n(입력된 값을 참조하는 자식키가 존재합니다.)", "에러",
					JOptionPane.WARNING_MESSAGE);
			System.out.println("에러: 해당 키를 참조하는 자식키가 존재함");
			System.out.println("무결성위배");
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null && !pstmt.isClosed())
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void deleteRPshop(int id) {
		String sql = "delete from RPSHOP where rs_Id = ?";
		String sql1 = "delete from RP where rp_Num = ?";
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		try {
			int result = JOptionPane.showConfirmDialog(null, "입력된 값과 관련된 모든 정보가 지워질 수 있습니다. 계속하시겠습니까?");
			if (result == JOptionPane.YES_OPTION) {
				System.out.println("삭제시도.");
				pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setInt(1, id);
				pstmt1.executeUpdate();

				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, id);
				int op = pstmt.executeUpdate();
				if (op > 0)
					System.out.println("RPshop삭제 성공.");
				else {
					JOptionPane.showMessageDialog(null, "올바른 값을 입력하세요. 삭제할 값이 존재하지 않습니다", "에러",
							JOptionPane.WARNING_MESSAGE);
					System.out.println("에러: 삭제할 기본키가 없음");
					System.out.println("삭제 실패.");
				}
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "올바르지 않은 값이 입력되었습니다.\n(입력된 값을 참조하는 자식키가 존재합니다.)", "에러",
					JOptionPane.WARNING_MESSAGE);
			System.out.println("에러: 해당 키를 참조하는 자식키가 존재함");
			System.out.println("무결성위배");
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null && !pstmt.isClosed())
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void deleteRP(int num) {
		String sql = "delete from RP where rp_Num = ?";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			int op = pstmt.executeUpdate();
			if (op > 0)
				System.out.println("RP삭제 성공.");
			else {
				JOptionPane.showMessageDialog(null, "올바른 값을 입력하세요. 삭제할 값이 존재하지 않습니다", "에러",
						JOptionPane.WARNING_MESSAGE);
				System.out.println("에러: 삭제할 기본키가 없음");
				System.out.println("삭제 실패.");
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "올바르지 않은 값이 입력되었습니다.\n(입력된 값을 참조하는 자식키가 존재합니다.)", "에러",
					JOptionPane.WARNING_MESSAGE);
			System.out.println("에러: 해당 키를 참조하는 자식키가 존재함");
			System.out.println("무결성위배");
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null && !pstmt.isClosed())
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void updateCompany(int id, String name) {
		String sql = "UPDATE COMPANY SET man_Name=? WHERE com_Id=?";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setInt(2, id);
			int op = pstmt.executeUpdate();
			if (op > 0)
				System.out.println("Company변경 성공.");
			else {
				JOptionPane.showMessageDialog(null, "올바른 값을 입력하세요. 변경할 값이 존재하지 않습니다", "에러",
						JOptionPane.WARNING_MESSAGE);
				System.out.println("에러: 변경할 값이 없음");
				System.out.println("변경 실패.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null && !pstmt.isClosed())
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void updateCar(int id, int charge) {
		String sql = "UPDATE CAR SET car_Charge=? WHERE car_Id=?";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, charge);
			pstmt.setInt(2, id);
			int op = pstmt.executeUpdate();
			if (op > 0)
				System.out.println("Car변경 성공.");
			else {
				JOptionPane.showMessageDialog(null, "올바른 값을 입력하세요. 변경할 값이 존재하지 않습니다", "에러",
						JOptionPane.WARNING_MESSAGE);
				System.out.println("에러: 변경할 값이 없음");
				System.out.println("변경 실패.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null && !pstmt.isClosed())
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void updateClient(String num, String name) {
		String sql = "UPDATE CLIENT SET clt_Name=? WHERE clt_Num=?";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setString(2, num);
			int op = pstmt.executeUpdate();
			if (op > 0)
				System.out.println("Client변경 성공.");
			else {
				JOptionPane.showMessageDialog(null, "올바른 값을 입력하세요. 변경할 값이 존재하지 않습니다", "에러",
						JOptionPane.WARNING_MESSAGE);
				System.out.println("에러: 변경할 값이 없음");
				System.out.println("변경 실패.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null && !pstmt.isClosed())
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void updateRent(String id, String date) {
		String sql = "UPDATE RENT SET rent_Stdate=? WHERE rent_Id=?";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, date);
			pstmt.setString(2, id);
			int op = pstmt.executeUpdate();
			if (op > 0)
				System.out.println("Rent변경 성공.");
			else {
				JOptionPane.showMessageDialog(null, "올바른 값을 입력하세요. 변경할 값이 존재하지 않습니다", "에러",
						JOptionPane.WARNING_MESSAGE);
				System.out.println("에러: 변경할 값이 없음");
				System.out.println("변경 실패.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null && !pstmt.isClosed())
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void updateRPshop(int id, String man) {
		String sql = "UPDATE RPSHOP SET rs_Man=? WHERE rs_Id=?";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, man);
			pstmt.setInt(2, id);
			int op = pstmt.executeUpdate();
			if (op > 0)
				System.out.println("RPshop변경 성공.");
			else {
				JOptionPane.showMessageDialog(null, "올바른 값을 입력하세요. 변경할 값이 존재하지 않습니다", "에러",
						JOptionPane.WARNING_MESSAGE);
				System.out.println("에러: 변경할 값이 없음");
				System.out.println("변경 실패.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null && !pstmt.isClosed())
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void updateRP(int num, String date) {
		String sql = "UPDATE RP SET rp_Date=? WHERE rp_Num=?";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, date);
			pstmt.setInt(2, num);
			int op = pstmt.executeUpdate();
			if (op > 0)
				System.out.println("RP변경 성공.");
			else {
				JOptionPane.showMessageDialog(null, "올바른 값을 입력하세요. 변경할 값이 존재하지 않습니다", "에러",
						JOptionPane.WARNING_MESSAGE);
				System.out.println("에러: 변경할 값이 없음");
				System.out.println("변경 실패.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null && !pstmt.isClosed())
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}// DatabaseConnect

class DescribeTableVO {
	private String column_name;

	public DescribeTableVO(String column_name) {
		super();
		this.column_name = column_name;
	}

	public String getColumn_name() {
		return column_name;
	}

	public void setColumn_name(String column_name) {
		this.column_name = column_name;
	}

	@Override
	public String toString() {
		return "DescribeTableVO [column_name=" + column_name + "]";
	}

}// DescribeTableVO

class CompanyVO {
	private int comId;
	private String comName;
	private String comAddr;
	private String comCall;
	private String manName;
	private String manEmail;

	public CompanyVO(int comId, String comName, String comAddr, String comCall, String manName, String manEmail) {
		super();
		this.comId = comId;
		this.comName = comName;
		this.comAddr = comAddr;
		this.comCall = comCall;
		this.manName = manName;
		this.manEmail = manEmail;
	}

	public int getComId() {
		return comId;
	}

	public void setcomId(int comId) {
		this.comId = comId;
	}

	public String getComName() {
		return comName;
	}

	public void setcomName(String comName) {
		this.comName = comName;
	}

	public String getComAddr() {
		return comAddr;
	}

	public void setcomAddr(String comAddr) {
		this.comAddr = comAddr;
	}

	public String getComCall() {
		return comCall;
	}

	public void setcomCall(String comCall) {
		this.comCall = comCall;
	}

	public String getManName() {
		return manName;
	}

	public void setManName(String manName) {
		this.manName = manName;
	}

	public String getManEmail() {
		return manEmail;
	}

	public void setManEmail(String manEmail) {
		this.manEmail = manEmail;
	}

	@Override
	public String toString() {
		return "CompanyVO [comId=" + comId + ", comName=" + comName + ", comAddr=" + comAddr + ", comCall=" + comCall
				+ ", manName=" + manName + ", manEmail=" + manEmail + "]";
	}
}// CompanyVO

class CarVO {
	private int carId;
	private String carName;
	private String carNumber;
	private int carSize;
	private String carImg;
	private String carInfo;
	private int carCharge;
	private int carCompany;
	private String carRegidate;

	public CarVO(int id, String name, String number, int size, String img, String info, int charge, int company,
			String regidate) {
		super();
		this.carId = id;
		this.carName = name;
		this.carNumber = number;
		this.carSize = size;
		this.carImg = img;
		this.carInfo = info;
		this.carCharge = charge;
		this.carCompany = company;
		this.carRegidate = regidate;
	}

	public int getCarId() {
		return carId;
	}

	public void setcarId(int id) {
		this.carId = id;
	}

	public String getCarName() {
		return carName;
	}

	public void setcarName(String name) {
		this.carName = name;
	}

	public String getCarNumber() {
		return carNumber;
	}

	public void setcarNumber(String number) {
		this.carNumber = number;
	}

	public int getCarSize() {
		return carSize;
	}

	public void setcarSize(int size) {
		this.carSize = size;
	}

	public String getCarImg() {
		return carImg;
	}

	public void setcarImg(String img) {
		this.carImg = img;
	}

	public String getCarInfo() {
		return carInfo;
	}

	public void setcarInfo(String info) {
		this.carInfo = info;
	}

	public int getCarCharge() {
		return carCharge;
	}

	public void setcarCharge(int charge) {
		this.carCharge = charge;
	}

	public int getCarCompany() {
		return carCompany;
	}

	public void setcarCompany(int company) {
		this.carCompany = company;
	}

	public String getCarRegidate() {
		return carRegidate;
	}

	public void setcarRegidate(String regidate) {
		this.carRegidate = regidate;
	}

	@Override
	public String toString() {
		return "CarVO [carId=" + carId + ", carName=" + carName + ", carNumber=" + carNumber + ", carSize=" + carSize
				+ ", carImg=" + carImg + ", carInfo=" + carInfo + ", carCharge=" + carCharge + ", carCompany="
				+ carCompany + ", carRegidate=" + carRegidate + "]";
	}
}// CompanyVO

class ClientVO {
	private String cltNum;
	private String cltName;
	private String cltAddr;
	private String cltCall;
	private String cltEmail;
	private String preDate;
	private String preCarname;

	public ClientVO(String num, String name, String addr, String call, String email, String date, String carname) {
		super();
		this.cltNum = num;
		this.cltName = name;
		this.cltAddr = addr;
		this.cltCall = call;
		this.cltEmail = email;
		this.preDate = date;
		this.preCarname = carname;
	}

	public String getCltNum() {
		return cltNum;
	}

	public void setCltNum(String cltNum) {
		this.cltNum = cltNum;
	}

	public String getCltName() {
		return cltName;
	}

	public void setCltName(String cltName) {
		this.cltName = cltName;
	}

	public String getCltAddr() {
		return cltAddr;
	}

	public void setCltAddr(String cltAddr) {
		this.cltAddr = cltAddr;
	}

	public String getCltCall() {
		return cltCall;
	}

	public void setCltCall(String cltCall) {
		this.cltCall = cltCall;
	}

	public String getCltEmail() {
		return cltEmail;
	}

	public void setCltEmail(String cltEmail) {
		this.cltEmail = cltEmail;
	}

	public String getPreDate() {
		return preDate;
	}

	public void setPreDate(String preDate) {
		this.preDate = preDate;
	}

	public String getPreCarname() {
		return preCarname;
	}

	public void setPreCarname(String preCarname) {
		this.preCarname = preCarname;
	}

	@Override
	public String toString() {
		return "ClientVO [cltNum=" + cltNum + ", cltName=" + cltName + ", cltAddr=" + cltAddr + ", cltCall=" + cltCall
				+ ", cltEmail=" + cltEmail + ", preDate=" + preDate + ", preCarname=" + preCarname + "]";
	}
}// ClientVO

class RentVO {
	private String rent_Id;
	private int rent_Car;
	private String rent_Client;
	private int rent_Com;
	private String rent_Stdate;
	private int rent_Period;
	private int rent_Fee;
	private String rent_Due;
	private String rent_Efc;
	private int rent_Efcfee;

	public RentVO(String rent_Id, int rent_Car, String rent_Client, int rent_Com, String rent_Stdate, int rent_Period,
			int rent_Fee, String rent_Due, String rent_Efc, int rent_Efcfee) {
		super();
		this.rent_Id = rent_Id;
		this.rent_Car = rent_Car;
		this.rent_Client = rent_Client;
		this.rent_Com = rent_Com;
		this.rent_Stdate = rent_Stdate;
		this.rent_Period = rent_Period;
		this.rent_Fee = rent_Fee;
		this.rent_Due = rent_Due;
		this.rent_Efc = rent_Efc;
		this.rent_Efcfee = rent_Efcfee;
	}

	public String getRent_Id() {
		return rent_Id;
	}

	public void setRent_Id(String rent_Id) {
		this.rent_Id = rent_Id;
	}

	public int getRent_Car() {
		return rent_Car;
	}

	public void setRent_Car(int rent_Car) {
		this.rent_Car = rent_Car;
	}

	public String getRent_Client() {
		return rent_Client;
	}

	public void setRent_Client(String rent_Client) {
		this.rent_Client = rent_Client;
	}

	public int getRent_Com() {
		return rent_Com;
	}

	public void setRent_Com(int rent_Com) {
		this.rent_Com = rent_Com;
	}

	public String getRent_Stdate() {
		return rent_Stdate;
	}

	public void setRent_Stdate(String rent_Stdate) {
		this.rent_Stdate = rent_Stdate;
	}

	public int getRent_Period() {
		return rent_Period;
	}

	public void setRent_Period(int rent_Period) {
		this.rent_Period = rent_Period;
	}

	public int getRent_Fee() {
		return rent_Fee;
	}

	public void setRent_Fee(int rent_Fee) {
		this.rent_Fee = rent_Fee;
	}

	public String getRent_Due() {
		return rent_Due;
	}

	public void setRent_Due(String rent_Due) {
		this.rent_Due = rent_Due;
	}

	public String getRent_Efc() {
		return rent_Efc;
	}

	public void setRent_Efc(String rent_Efc) {
		this.rent_Efc = rent_Efc;
	}

	public int getRent_Efcfee() {
		return rent_Efcfee;
	}

	public void setRent_Efcfee(int rent_Efcfee) {
		this.rent_Efcfee = rent_Efcfee;
	}

	@Override
	public String toString() {
		return "RentVO [rent_Id=" + rent_Id + ", rent_Car=" + rent_Car + ", rent_Client=" + rent_Client + ", rent_Com="
				+ rent_Com + ", rent_Stdate=" + rent_Stdate + ", rent_Peroid=" + rent_Period + ", rent_Fee=" + rent_Fee
				+ ", rent_Due=" + rent_Due + "," + " rent_Efc=" + rent_Efc + ", rent_Efcfee=" + rent_Efcfee + "]";
	}
}// RentVO

class RPshopVO {
	private int rs_Id;
	private String rs_Name;
	private String rs_Addr;
	private String rs_Call;
	private String rs_Man;
	private String rs_ManEm;

	public RPshopVO(int rs_Id, String rs_Name, String rs_Addr, String rs_Call, String rs_Man, String rs_ManEm) {
		super();
		this.rs_Id = rs_Id;
		this.rs_Addr = rs_Name;
		this.rs_Name = rs_Addr;
		this.rs_Call = rs_Call;
		this.rs_Man = rs_Man;
		this.rs_ManEm = rs_ManEm;
	}

	@Override
	public String toString() {
		return "RPshopVO [rs_Id=" + rs_Id + ", rs_Name=" + rs_Name + ", rs_Addr=" + rs_Addr + ", rs_Call=" + rs_Call
				+ ", rs_Man=" + rs_Man + ", rs_ManEm=" + rs_ManEm + "]";
	}

	public int getRs_Id() {
		return rs_Id;
	}

	public void setRs_Id(int rs_Id) {
		this.rs_Id = rs_Id;
	}

	public String getRs_Name() {
		return rs_Name;
	}

	public void setRs_Name(String rs_Name) {
		this.rs_Name = rs_Name;
	}

	public String getRs_Addr() {
		return rs_Addr;
	}

	public void setRs_Addr(String rs_Addr) {
		this.rs_Addr = rs_Addr;
	}

	public String getRs_Call() {
		return rs_Call;
	}

	public void setRs_Call(String rs_Call) {
		this.rs_Call = rs_Call;
	}

	public String getRs_Man() {
		return rs_Man;
	}

	public void setRs_Man(String rs_Man) {
		this.rs_Man = rs_Man;
	}

	public String getRs_ManEm() {
		return rs_ManEm;
	}

	public void setRs_ManEm(String rs_ManEm) {
		this.rs_ManEm = rs_ManEm;
	}

}// RentVO

class RPVO {
	private int rp_Num;
	private int rp_Car;
	private int rp_Rpshop;
	private int rp_Com;
	private String rp_Clt;
	private String rp_List;
	private String rp_Date;
	private int rp_Fee;
	private String rp_Due;
	private String rp_Efclist;

	public RPVO(int rp_Num, int rp_Car, int rp_Rpshop, int rp_Com, String rp_Clt, String rp_List, String rp_Date,
			int rp_Fee, String rp_Due, String rp_Efclist) {
		super();
		this.rp_Num = rp_Num;
		this.rp_Car = rp_Car;
		this.rp_Rpshop = rp_Rpshop;
		this.rp_Com = rp_Com;
		this.rp_Clt = rp_Clt;
		this.rp_List = rp_List;
		this.rp_Date = rp_Date;
		this.rp_Fee = rp_Fee;
		this.rp_Due = rp_Due;
		this.rp_Efclist = rp_Efclist;
	}

	@Override
	public String toString() {
		return "RPVO [rp_Num=" + rp_Num + ", rp_Car=" + rp_Car + ", rp_Rpshop=" + rp_Rpshop + ", rp_Com=" + rp_Com
				+ ", rp_Clt=" + rp_Clt + ", rp_List=" + rp_List + ", rp_Date=" + rp_Date + ", rp_Fee=" + rp_Fee + ","
				+ " rp_Due=" + rp_Due + ", rp_Efclist=" + rp_Efclist + "]";
	}

	public int getRp_Num() {
		return rp_Num;
	}

	public void setRp_Num(int rp_Num) {
		this.rp_Num = rp_Num;
	}

	public int getRp_Car() {
		return rp_Car;
	}

	public void setRp_Car(int rp_Car) {
		this.rp_Car = rp_Car;
	}

	public int getRp_Rpshop() {
		return rp_Rpshop;
	}

	public void setRp_Rpshop(int rp_Rpshop) {
		this.rp_Rpshop = rp_Rpshop;
	}

	public int getRp_Com() {
		return rp_Com;
	}

	public void setRp_Com(int rp_Com) {
		this.rp_Com = rp_Com;
	}

	public String getRp_Clt() {
		return rp_Clt;
	}

	public void setRp_Clt(String rp_Clt) {
		this.rp_Clt = rp_Clt;
	}

	public String getRp_List() {
		return rp_List;
	}

	public void setRp_List(String rp_List) {
		this.rp_List = rp_List;
	}

	public String getRp_Date() {
		return rp_Date;
	}

	public void setRp_Date(String rp_Date) {
		this.rp_Date = rp_Date;
	}

	public int getRp_Fee() {
		return rp_Fee;
	}

	public void setRp_Fee(int rp_Fee) {
		this.rp_Fee = rp_Fee;
	}

	public String getRp_Due() {
		return rp_Due;
	}

	public void setRp_Due(String rp_Due) {
		this.rp_Due = rp_Due;
	}

	public String getRp_Efclist() {
		return rp_Efclist;
	}

	public void setRp_Efclist(String rp_Efclist) {
		this.rp_Efclist = rp_Efclist;
	}

}// RentVO
