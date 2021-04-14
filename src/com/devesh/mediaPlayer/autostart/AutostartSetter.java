package com.devesh.mediaPlayer.autostart;

import com.devesh.mediaPlayer.Application;
import com.devesh.mediaPlayer.listHelpers.ListItemTransferHandler;
import com.devesh.mediaPlayer.swing.MainFrame;
import com.devesh.mediaPlayer.utils.Playlist;
import com.registry.RegStringValue;
import com.registry.RegistryKey;
import com.registry.ValueType;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

public class AutostartSetter extends javax.swing.JDialog {

	private Playlist playlist;
	private final File playlistFile = new File(
			Application.metaDir.getPath() + "\\playlist.ppl");

	private File exeFile = new File(
			new File(System.getProperty("user.dir")).getPath()
					+ "\\PlayIt.exe");

	public AutostartSetter(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		System.out.println(exeFile.getPath());
		if (playlistFile.exists())
		{
			try (ObjectInputStream oi = new ObjectInputStream(
					new FileInputStream(playlistFile)))
			{
				playlist = (Playlist) oi.readObject();
				playlist.currentSong = 0;
				oi.close();
			} catch (IOException | ClassNotFoundException ex)
			{
				playlist = new Playlist();
			}
		} else
			playlist = new Playlist();

		initComponents();
		requestFocus();
	}


	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnPannel = new javax.swing.JPanel();
        btnDisable = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();
        jScrollPane = new javax.swing.JScrollPane();
        spPanel = new javax.swing.JPanel();
        sngList = new javax.swing.JList<>();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenFile = new javax.swing.JMenu();
        btnOpen = new javax.swing.JMenuItem();
        btnLoad = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        btnPannel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        btnDisable.setText("Disable");
        btnDisable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDisableActionPerformed(evt);
            }
        });
        btnPannel.add(btnDisable);

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        btnPannel.add(btnCancel);

        btnOK.setText("OK");
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });
        btnPannel.add(btnOK);

        getContentPane().add(btnPannel, java.awt.BorderLayout.PAGE_END);

        jScrollPane.setAutoscrolls(true);
        jScrollPane.setMinimumSize(null);
        jScrollPane.setViewportView(null);

        spPanel.setToolTipText("");
        spPanel.setAutoscrolls(true);
        spPanel.setMinimumSize(null);
        spPanel.setLayout(new java.awt.BorderLayout());

        sngList.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        sngList.setModel(playlist.getListModel());
        sngList.setDoubleBuffered(true);
        sngList.setDragEnabled(true);
        sngList.setDropMode(javax.swing.DropMode.INSERT);
        sngList.setFocusable(false);
        sngList.setMinimumSize(null);
        sngList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sngListMouseClicked(evt);
            }
        });
        sngList.setTransferHandler(new ListItemTransferHandler(playlist, null));
        spPanel.add(sngList, java.awt.BorderLayout.CENTER);

        jScrollPane.setViewportView(spPanel);

        jScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        getContentPane().add(jScrollPane, java.awt.BorderLayout.CENTER);

        jMenFile.setText("File");

        btnOpen.setText("Open");
        btnOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenActionPerformed(evt);
            }
        });
        jMenFile.add(btnOpen);

        btnLoad.setText("Load Current Playlist");
        btnLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadActionPerformed(evt);
            }
        });
        jMenFile.add(btnLoad);

        jMenuBar1.add(jMenFile);

        setJMenuBar(jMenuBar1);

        setSize(new java.awt.Dimension(416, 338));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void sngListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sngListMouseClicked
        if(SwingUtilities.isRightMouseButton(evt)){
            showSngListPopup(evt);
        }

        if(!SwingUtilities.isMiddleMouseButton(evt)){
            cursor = sngList.locationToIndex(evt.getPoint());
        }
    }//GEN-LAST:event_sngListMouseClicked

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
		switch (evt.getKeyCode()) {
			case KeyEvent.VK_UP -> {
				if(evt.isShiftDown())
					moveUp(true);
				else
					moveUp(false);
			}
			
			case KeyEvent.VK_DOWN -> {
				if(evt.isShiftDown())
					moveDown(true);
				else
					moveDown(false);
			}
		}
    }//GEN-LAST:event_formKeyPressed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
		dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnDisableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDisableActionPerformed
		RegistryKey run = new RegistryKey("Software\\Microsoft\\Windows\\CurrentVersion\\Run");
		if(run.getValue("PlayIt") != null){
			run.deleteValue("PlayIt");
		}
		dispose();
    }//GEN-LAST:event_btnDisableActionPerformed

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
		savePlaylist();
		
		RegistryKey run = new RegistryKey("Software\\Microsoft\\Windows\\CurrentVersion\\Run");
		if(run.getValue("PlayIt") == null){
			run.newValue("PlayIt", ValueType.REG_SZ);
		}
		RegStringValue regValue = (RegStringValue) run.getValue("PlayIt");
		regValue.setValue(exeFile.getPath() + " \"" + playlistFile.getPath() + "\"");
		dispose();
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenActionPerformed
		File[] files = Application.showOpenDialog();
		if(files != null)
			openMedia(files);
    }//GEN-LAST:event_btnOpenActionPerformed

    private void btnLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadActionPerformed
		playlist = new Playlist(Application.getPlaylist());
		playlist.currentSong = 0;
		sngList.setModel(playlist.getListModel());
		sngList.setTransferHandler(
							new ListItemTransferHandler(playlist, null));
    }//GEN-LAST:event_btnLoadActionPerformed


	private void savePlaylist()
	{
		if (playlistFile.exists())
			playlistFile.delete();
		try
		{
			playlistFile.createNewFile();
			try (ObjectOutputStream os = new ObjectOutputStream(
					new FileOutputStream(playlistFile)))
			{
				os.writeObject(playlist);
				os.close();
			}
		} catch (IOException ex)
		{
			Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}


	public void openMedia(File[] files)
	{
		File file = files[0];
		String filename = file.getPath();

		if (filename.endsWith(".ppl"))
		{
			try
			{
				try (ObjectInputStream oi = new ObjectInputStream(
						new FileInputStream(file)))
				{
					playlist = (Playlist) oi.readObject();
					playlist.currentSong = 0;
					sngList.setModel(playlist.getListModel());
					sngList.setTransferHandler(
							new ListItemTransferHandler(playlist, null));
					oi.close();
				}
			} catch (IOException | ClassNotFoundException ex)
			{
				Logger.getLogger(MainFrame.class.getName())
						.log(Level.SEVERE, null, ex);
			}
		} else
		{
			playlist.addSongs(new ArrayList<>(
					Arrays.asList(files)));
		}
	}


	private void showSngListPopup(MouseEvent evt)
	{
		int row = sngList.locationToIndex(evt.getPoint());
		if (!sngList.isSelectedIndex(row))
			sngList.setSelectedIndex(row);

		JPopupMenu popupMenu = new JPopupMenu();

		JMenuItem miRemove = new JMenuItem("Remove");
		miRemove.addActionListener((ActionEvent ev) -> {
			while (sngList.getSelectedIndex() != -1)
			{
				playlist.removeSong(sngList.getSelectedIndex());
			}
		});

		popupMenu.add(miRemove);
		popupMenu.show(sngList, evt.getX(), evt.getY());
	}


	private void moveUp(boolean keepSelected)
	{
		if (sngList.getSelectedIndex() == -1)
			cursor = -1;
		if (cursor > 0)
		{
			if (keepSelected)
			{
				int[] selectedInd = sngList.getSelectedIndices();
				int destInd = Arrays.binarySearch(selectedInd, cursor - 1);
				int[] newSelection;
				// check if the new cursor pos is selected
				if (destInd < 0)
				{
					// if not select it
					newSelection = Arrays.copyOf(selectedInd,
							selectedInd.length + 1);
					newSelection[newSelection.length - 1] = --cursor;
				} else
				{
					// deselect it
					selectedInd[destInd + 1] = -1;
					newSelection = selectedInd;
					cursor--;
				}
				sngList.setSelectedIndices(newSelection);
			} else
				sngList.setSelectedIndex(--cursor);
			sngList.ensureIndexIsVisible(cursor);
		}
	}


	private void moveDown(boolean keepSelected)
	{
		if (sngList.getSelectedIndex() == -1)
			cursor = -1;
		if (cursor != sngList.getModel().getSize() - 1)
		{
			if (keepSelected)
			{
				int[] selectedInd = sngList.getSelectedIndices();
				int destInd = Arrays.binarySearch(selectedInd, cursor + 1);
				int[] newSelection;
				// check if the new cursor pos is selected
				if (destInd < 0)
				{
					// if not select it
					newSelection = Arrays.copyOf(selectedInd,
							selectedInd.length + 1);
					newSelection[newSelection.length - 1] = ++cursor;
				} else
				{
					// deselect it
					selectedInd[destInd - 1] = -1;
					newSelection = selectedInd;
					cursor++;
				}
				sngList.setSelectedIndices(newSelection);
			} else
				sngList.setSelectedIndex(++cursor);
			sngList.ensureIndexIsVisible(cursor);
		}
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnDisable;
    private javax.swing.JMenuItem btnLoad;
    private javax.swing.JButton btnOK;
    private javax.swing.JMenuItem btnOpen;
    private javax.swing.JPanel btnPannel;
    private javax.swing.JMenu jMenFile;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JList<String> sngList;
    private javax.swing.JPanel spPanel;
    // End of variables declaration//GEN-END:variables
	private int cursor = -1;

}
