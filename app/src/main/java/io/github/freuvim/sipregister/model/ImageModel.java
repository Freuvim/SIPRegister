package io.github.freuvim.sipregister.model;

import java.io.Serializable;

public class ImageModel implements Serializable {

    private String arquivo;
    private String base64;

    public ImageModel() {}
    public ImageModel(String arquivo) {
        this.arquivo = arquivo;
    }

    public String getArquivo() {
        return arquivo;
    }

    public String getBase64() {
        return base64;
    }
}