package com.isro.filebrowser.models;

import com.isro.filebrowser.interfaces.ITrackSelection;
import com.isro.filebrowser.utils.AssortedUtils;

import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * Created by isro on 4/15/2017.
 */
public class FileItem implements ITrackSelection {

    private File file;
    private boolean isSelected;
    private String fileSize;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public FileItem(File file) {
        this.file = file;
        try {
            if (file.isDirectory()) {
                setFileSize(FileUtils.byteCountToDisplaySize(AssortedUtils.GetMinimumDirSize(file)) +  " | ");
            } else {
                setFileSize(FileUtils.byteCountToDisplaySize(FileUtils.sizeOf(file)) +  " | ");
            }
        } catch (Exception e) {
            setFileSize("Unknown | ");
        }
    }
}
