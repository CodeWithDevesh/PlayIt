package com.devesh.mediaPlayer.converter;

import com.devesh.mediaPlayer.Application;
import static com.devesh.mediaPlayer.Application.currentDir;
import static com.devesh.mediaPlayer.Application.updateCurrentDir;
import java.io.File;
import javax.swing.JFileChooser;

public class ConverterFrame extends javax.swing.JFrame {

	public ConverterFrame() {
		initComponents();
	}


	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlMain = new javax.swing.JPanel();
        lblInput = new javax.swing.JLabel();
        lblOutput = new javax.swing.JLabel();
        tfInput = new javax.swing.JTextField();
        tfOutput = new javax.swing.JTextField();
        btnInBrw = new javax.swing.JButton();
        btnOutBrw = new javax.swing.JButton();
        cbFormat = new javax.swing.JComboBox<>();
        lblFormat = new javax.swing.JLabel();
        rbFormat = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        tfTitle = new javax.swing.JTextField();
        tfFormat = new javax.swing.JTextField();
        pnlSouth = new javax.swing.JPanel();
        lblMessage = new javax.swing.JLabel();
        pnlBtn = new javax.swing.JPanel();
        btnConvert = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("PlayIt Converter");
        setMinimumSize(new java.awt.Dimension(550, 300));

        pnlMain.setName(""); // NOI18N
        pnlMain.setLayout(new java.awt.GridBagLayout());

        lblInput.setText("Input File");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 6, 0);
        pnlMain.add(lblInput, gridBagConstraints);

        lblOutput.setText("Output Folder");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 6, 0);
        pnlMain.add(lblOutput, gridBagConstraints);

        tfInput.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 5);
        pnlMain.add(tfInput, gridBagConstraints);

        tfOutput.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 5);
        pnlMain.add(tfOutput, gridBagConstraints);

        btnInBrw.setText("Browse");
        btnInBrw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInBrwActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 5);
        pnlMain.add(btnInBrw, gridBagConstraints);

        btnOutBrw.setText("Browse");
        btnOutBrw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOutBrwActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 5);
        pnlMain.add(btnOutBrw, gridBagConstraints);

        cbFormat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "mp3", "wav", "ogg", "acc", "flac" }));
        cbFormat.setMinimumSize(new java.awt.Dimension(1000, 600));
        cbFormat.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 5);
        pnlMain.add(cbFormat, gridBagConstraints);

        lblFormat.setText("Select Output format");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 6, 0);
        pnlMain.add(lblFormat, gridBagConstraints);

        rbFormat.setText("Custom Format");
        rbFormat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbFormatActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 6, 0);
        pnlMain.add(rbFormat, gridBagConstraints);

        jLabel1.setText("Title");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 6, 0);
        pnlMain.add(jLabel1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 5);
        pnlMain.add(tfTitle, gridBagConstraints);

        tfFormat.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 5);
        pnlMain.add(tfFormat, gridBagConstraints);

        getContentPane().add(pnlMain, java.awt.BorderLayout.CENTER);

        pnlSouth.setLayout(new java.awt.BorderLayout());

        lblMessage.setForeground(new java.awt.Color(255, 0, 51));
        lblMessage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pnlSouth.add(lblMessage, java.awt.BorderLayout.NORTH);

        pnlBtn.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        btnConvert.setText("Convert");
        btnConvert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConvertActionPerformed(evt);
            }
        });
        pnlBtn.add(btnConvert);

        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        pnlBtn.add(btnClose);

        pnlSouth.add(pnlBtn, java.awt.BorderLayout.SOUTH);

        getContentPane().add(pnlSouth, java.awt.BorderLayout.PAGE_END);

        setSize(new java.awt.Dimension(550, 300));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void rbFormatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbFormatActionPerformed
		if(rbFormat.isSelected()){
			tfFormat.setEnabled(true);
			cbFormat.setEnabled(false);
		}else{
			tfFormat.setEnabled(false);
			cbFormat.setEnabled(true);			
		}
    }//GEN-LAST:event_rbFormatActionPerformed

    private void btnInBrwActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInBrwActionPerformed
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setMultiSelectionEnabled(false);
		
		fileChooser.setCurrentDirectory(currentDir);
		int i = fileChooser.showOpenDialog(null);
		
		updateCurrentDir(fileChooser.getCurrentDirectory());
		
		if (i == JFileChooser.APPROVE_OPTION)
			tfInput.setText(fileChooser.getSelectedFile().getPath());
    }//GEN-LAST:event_btnInBrwActionPerformed

    private void btnOutBrwActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOutBrwActionPerformed
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setCurrentDirectory(currentDir);
		Application.updateCurrentDir(
							fileChooser.getCurrentDirectory());
		int y = fileChooser.showOpenDialog(null);
		
		if(y == JFileChooser.APPROVE_OPTION)
		{
			tfOutput.setText(fileChooser.getSelectedFile().getPath());
		}
    }//GEN-LAST:event_btnOutBrwActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
		dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnConvertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConvertActionPerformed
		File input, output;
		String title;
		String format;
		
		title = tfTitle.getText();
		if(title.isBlank()){
			lblMessage.setText("Enter valid title");
			return;
		}
		
		if(rbFormat.isSelected()){
			format = tfFormat.getText();
			if(format.isBlank()){
				lblMessage.setText("Enter valid foramt");
				return;
			}
		}else{
			format = (String) cbFormat.getSelectedItem();
		}
		
		input = new File(tfInput.getText());
		output = new File(tfOutput.getText() + "\\" + title + "." + format);
		
		if(!input.exists()){
			lblMessage.setText("Enter valid input file");
			return;
		}
		
		if(!output.getParentFile().exists()){
			lblMessage.setText("Enter valid output directory");
			return;
		}
		SngConverter.convert(input, output, title);
		
		lblMessage.setText("");
    }//GEN-LAST:event_btnConvertActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnConvert;
    private javax.swing.JButton btnInBrw;
    private javax.swing.JButton btnOutBrw;
    private javax.swing.JComboBox<String> cbFormat;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblFormat;
    private javax.swing.JLabel lblInput;
    private javax.swing.JLabel lblMessage;
    private javax.swing.JLabel lblOutput;
    private javax.swing.JPanel pnlBtn;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JPanel pnlSouth;
    private javax.swing.JRadioButton rbFormat;
    private javax.swing.JTextField tfFormat;
    private javax.swing.JTextField tfInput;
    private javax.swing.JTextField tfOutput;
    private javax.swing.JTextField tfTitle;
    // End of variables declaration//GEN-END:variables
}