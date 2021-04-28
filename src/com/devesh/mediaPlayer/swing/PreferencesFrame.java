package com.devesh.mediaPlayer.swing;

import static com.devesh.mediaPlayer.Application.prefs;
import com.devesh.mediaPlayer.Settings;

public class PreferencesFrame extends javax.swing.JFrame {

	private boolean showNotification;
	private boolean minOnClose;
	private String theme;

	public PreferencesFrame() {
		initComponents();
		loadSettings();
	}


	private void loadSettings()
	{
		theme = prefs.get(Settings.THEME_KEY, "Dark");
		minOnClose = prefs.getBoolean(Settings.CLOSE_KEY, false);
		showNotification = prefs.getBoolean(Settings.NOTIFICATION_KEY, true);

		cbTheme.setSelectedItem(theme);
		cbxMinmize.setSelected(minOnClose);
		cbxNotification.setSelected(showNotification);
	}


	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlMain = new javax.swing.JPanel();
        lblHead = new javax.swing.JLabel();
        lblTheme = new javax.swing.JLabel();
        cbTheme = new javax.swing.JComboBox<>();
        lblMinimize = new javax.swing.JLabel();
        cbxMinmize = new javax.swing.JCheckBox();
        lblNotification = new javax.swing.JLabel();
        cbxNotification = new javax.swing.JCheckBox();
        pnlSouth = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        pnlBtn = new javax.swing.JPanel();
        btnOK = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("PlayIt Preferences");
        setFont(new java.awt.Font("Agency FB", 0, 12)); // NOI18N

        pnlMain.setMinimumSize(new java.awt.Dimension(500, 200));
        pnlMain.setLayout(new java.awt.GridBagLayout());

        lblHead.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        lblHead.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHead.setText("Preferences");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(7, 4, 3, 4);
        pnlMain.add(lblHead, gridBagConstraints);

        lblTheme.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        lblTheme.setLabelFor(cbTheme);
        lblTheme.setText("Theme -");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 2, 0);
        pnlMain.add(lblTheme, gridBagConstraints);

        cbTheme.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        cbTheme.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Light", "Dark" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 2, 5);
        pnlMain.add(cbTheme, gridBagConstraints);

        lblMinimize.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        lblMinimize.setText("Minmize to tray when closed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 6, 2, 6);
        pnlMain.add(lblMinimize, gridBagConstraints);

        cbxMinmize.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        cbxMinmize.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        cbxMinmize.setDoubleBuffered(true);
        cbxMinmize.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        cbxMinmize.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        cbxMinmize.setIconTextGap(10);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(8, 5, 2, 5);
        pnlMain.add(cbxMinmize, gridBagConstraints);

        lblNotification.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        lblNotification.setText("Show notification when song changes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 6, 2, 6);
        pnlMain.add(lblNotification, gridBagConstraints);

        cbxNotification.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(8, 5, 2, 5);
        pnlMain.add(cbxNotification, gridBagConstraints);

        getContentPane().add(pnlMain, java.awt.BorderLayout.PAGE_START);

        pnlSouth.setLayout(new java.awt.BorderLayout());
        pnlSouth.add(jSeparator1, java.awt.BorderLayout.PAGE_START);

        pnlBtn.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        btnOK.setText("OK");
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });
        pnlBtn.add(btnOK);

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        pnlBtn.add(btnCancel);

        pnlSouth.add(pnlBtn, java.awt.BorderLayout.SOUTH);

        getContentPane().add(pnlSouth, java.awt.BorderLayout.PAGE_END);

        setSize(new java.awt.Dimension(587, 300));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
		dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
		prefs.put(Settings.THEME_KEY, cbTheme.getSelectedItem().toString());
		prefs.putBoolean(Settings.CLOSE_KEY, cbxMinmize.isSelected());
		prefs.putBoolean(Settings.NOTIFICATION_KEY, cbxNotification.isSelected());
		
		dispose();
    }//GEN-LAST:event_btnOKActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOK;
    private javax.swing.JComboBox<String> cbTheme;
    private javax.swing.JCheckBox cbxMinmize;
    private javax.swing.JCheckBox cbxNotification;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblHead;
    private javax.swing.JLabel lblMinimize;
    private javax.swing.JLabel lblNotification;
    private javax.swing.JLabel lblTheme;
    private javax.swing.JPanel pnlBtn;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JPanel pnlSouth;
    // End of variables declaration//GEN-END:variables
}
