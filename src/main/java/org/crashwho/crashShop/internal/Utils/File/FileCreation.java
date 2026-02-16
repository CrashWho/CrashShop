package org.crashwho.crashShop.internal.Utils.File;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FileCreation {

    private final List<FileManager> files;

    public FileCreation(@NotNull FileManager... files) {
        this.files = new ArrayList<>(Arrays.asList(files));
    }

    public void reloadAll() {
        for (FileManager file : files)
            file.reloadFile();


    }







}
