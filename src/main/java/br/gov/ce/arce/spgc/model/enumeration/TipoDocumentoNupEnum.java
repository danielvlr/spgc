package br.gov.ce.arce.spgc.model.enumeration;

public enum TipoDocumentoNupEnum {

    PETICAO_INICIAL(6, "Peticao inicial"),
    REQUERIMENTO(19, "Requerimento"),
    ANEXO(7, "Anexo");

    private Integer id;
    private String type;

    TipoDocumentoNupEnum(Integer id, String type) {
        this.id = id;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
