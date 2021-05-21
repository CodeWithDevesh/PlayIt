package com.devesh.mediaPlayer.swing;

import javax.swing.DefaultListCellRenderer;
import javax.swing.SwingConstants;

public class KeymapsFrame extends javax.swing.JFrame {

	public KeymapsFrame() {
		initComponents();
	}


	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        panel = new javax.swing.JPanel();
        listActions = new javax.swing.JList<>();
        listKeys = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Keymaps");
        getContentPane().setLayout(new java.awt.BorderLayout(10, 10));

        panel.setLayout(new java.awt.BorderLayout());

        listActions.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Play/Pause", "Next Song", "Previous Song", "Increase Volume", "Decrease Volume", "Hide Window", "Quit", "Save Playlist", "Open Playlist", "Preferences", "Gallery View", "Download Songs", "Global Play/Pause", "Global Next Song", "Global Previous Song", "Global Volume Increase", "Global Volume Decrease" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        listActions.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        panel.add(listActions, java.awt.BorderLayout.WEST);

        listKeys.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        listKeys.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Space", "Ctrl+Right Arrow", "Ctrl+Left Arrow", "Ctrl+Up Arrow", "Ctrl+Down Arrow", "Ctrl+W", "Ctrl+Q", "Ctrl+S", "Ctrl+O", "Ctrl+P", "Ctrl+G", "Ctrl+D", "Pause/Break", "Pause/Break+Right Arrow", "Pause/Break+Left Arrow", "Pause/Break+Up Arrow", "Pause/Break+Down Arrow" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        listKeys.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        listKeys.setDoubleBuffered(true);
        DefaultListCellRenderer cellRenderer = (DefaultListCellRenderer) listKeys.getCellRenderer();
        cellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(listKeys, java.awt.BorderLayout.CENTER);

        jScrollPane1.setViewportView(panel);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        setSize(new java.awt.Dimension(416, 338));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList<String> listActions;
    private javax.swing.JList<String> listKeys;
    private javax.swing.JPanel panel;
    // End of variables declaration//GEN-END:variables
}
