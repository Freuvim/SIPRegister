package io.github.freuvim.sipregister.model;

import java.io.Serializable;

public class ImageModel implements Serializable {

    private String arquivo;

    public ImageModel() {
    }
    public ImageModel(String arquivo) {
        this.arquivo = arquivo;
    }

    public String getArquivo() {
        return arquivo;
    }
}