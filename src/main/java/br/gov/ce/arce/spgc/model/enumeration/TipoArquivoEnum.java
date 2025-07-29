package br.gov.ce.arce.spgc.model.enumeration;

public enum TipoArquivoEnum {

    URL_FILE("url_file"),
    BASE64  ("base64");

    private String desc;

    TipoArquivoEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

}
