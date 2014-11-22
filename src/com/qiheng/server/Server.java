package com.qiheng.server;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Server extends JFrame {
	private JPanel panel1;
	private JPanel panel2;
	private JLabel label1;
	private JLabel label2;
	private JLabel label3;
	private JTextArea textarea;
	private JTextField textfield;
	private JScrollPane scrollPane;
	private JButton button;

	private Map<String, ServerMessageThread> map = new HashMap<String, ServerMessageThread>();// 用户名与端口号的映射

	private Thread thread;

	public JLabel getLabel2() {
		return label2;
	}

	public void setLabel2(JLabel label2) {
		this.label2 = label2;
	}

	public JButton getButton() {
		return button;
	}

	public Map<String, ServerMessageThread> getMap() {
		return map;
	}

	public void setMap(Map<String, ServerMessageThread> map) {
		this.map = map;
	}

	public Server() throws HeadlessException {
		super("服务器");
		this.init();

	}

	private void init() {
		panel1 = new JPanel();
		panel2 = new JPanel();
		label1 = new JLabel();
		label2 = new JLabel();
		label3 = new JLabel();
		textarea = new JTextArea();
		textfield = new JTextField(10);
		scrollPane = new JScrollPane();
		button = new JButton();

		panel1.setBorder(BorderFactory.createTitledBorder("服务器信息"));
		panel2.setBorder(BorderFactory.createTitledBorder("在线用户列表"));
		label1.setText("服务器状态");
		label2.setText("停止");
		label3.setText("端口号");
		button.setText("启动服务器");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				check(e);
			}
		});
		textarea.setEditable(false);
		panel1.add(label1);
		panel1.add(label2);
		panel1.add(label3);
		panel1.add(textfield);
		panel1.add(button);
		textarea.setColumns(30);
		textarea.setRows(20);
		this.scrollPane.setViewportView(textarea);
		panel2.add(scrollPane);
		this.getContentPane().add(panel1, BorderLayout.NORTH);
		this.getContentPane().add(panel2, BorderLayout.SOUTH);
		this.textarea.setEditable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.textfield.setText("4000");// 调试
		this.setResizable(false);
		this.pack();
		this.setVisible(true);

	}

	private void check(ActionEvent e) {
		int port = Integer.parseInt(this.textfield.getText());

		new ServerConnection(this, port).start();
	}

	public static void main(String[] args) {
		new Server();
	}

}