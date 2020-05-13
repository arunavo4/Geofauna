package com.isro.filebrowser.listeners;

import java.io.File;

/**
 * Created by isro on 4/18/2017.
 */
public interface OnFileChangedListener {
    void onFileChanged(File updatedFile);
}
